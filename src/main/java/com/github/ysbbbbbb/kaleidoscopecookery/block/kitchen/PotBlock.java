package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IPot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PotBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<PotBlock> CODEC = simpleCodec(p -> new PotBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
   public static final BooleanProperty SHOW_OIL = BooleanProperty.create("show_oil");
   public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");
   private static final VoxelShape AABB = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
   private static final double DURABILITY_COST_PROBABILITY = 0.25;

   public PotBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.METAL).sound(ModSoundType.POT).noOcclusion().strength(1.5F, 6.0F));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH))
                     .setValue(HAS_OIL, false))
                  .setValue(SHOW_OIL, false))
               .setValue(WATERLOGGED, false))
            .setValue(HAS_BASE, false)
      );
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      return direction == Direction.DOWN
         ? (BlockState)state.setValue(HAS_BASE, !neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP))
         : super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer instanceof Player player && level.getBlockEntity(pos) instanceof IPot pot && pot.hasHeatSource(level)) {
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_pot_on_heat_source");
      }
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand == InteractionHand.OFF_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (!(level.getBlockEntity(pos) instanceof IPot pot)) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack var11 = player.getItemInHand(hand);
         RandomSource random = level.getRandom();
         if ((var11.isEmpty() || var11.is(TagMod.INGREDIENT_CONTAINER)) && pot.removeIngredient(level, player)) {
            return InteractionResult.SUCCESS;
         } else if (pot.takeOutProduct(level, player, player.getMainHandItem())) {
            return InteractionResult.SUCCESS;
         } else if (!pot.hasHeatSource(level)) {
            this.sendBarMessage(player, "tip.kaleidoscope_cookery.pot.need_lit_stove");
            return InteractionResult.FAIL;
         } else if (!(Boolean)state.getValue(HAS_OIL)) {
            if (pot.onPlaceOil(level, player, var11)) {
               return InteractionResult.SUCCESS;
            } else {
               this.sendBarMessage(player, "tip.kaleidoscope_cookery.pot.need_oil");
               return InteractionResult.FAIL;
            }
         } else if (var11.is(TagMod.KITCHEN_SHOVEL)) {
            if (level.getRandom().nextDouble() < 0.25) {
               var11.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }

            pot.onShovelHit(level, player, var11);
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            return InteractionResult.SUCCESS;
         } else {
            return pot.addIngredient(level, player, var11) ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      }
   }

   private void sendBarMessage(Player player, String key) {
      if (player instanceof ServerPlayer serverPlayer) {
         MutableComponent message = Component.translatable(key);
         serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
      }
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new PotBlockEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      if (level.isClientSide()) {
         return null;
      } else {
         return !state.getValue(HAS_OIL)
            ? null
            : createTickerHelper(blockEntityType, ModBlocks.POT_BE.get(), (levelIn, pos, stateIn, pot) -> pot.tick(levelIn));
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      BlockState blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
      return !belowState.isFaceSturdy(context.getLevel(), context.getClickedPos().below(), Direction.UP)
         ? (BlockState)blockState.setValue(HAS_BASE, true)
         : blockState;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{HAS_OIL, SHOW_OIL, HAS_BASE, FACING, WATERLOGGED});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return AABB;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.pot").withStyle(ChatFormatting.GRAY));
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.pot.fail").withStyle(ChatFormatting.GRAY));
   }
}
