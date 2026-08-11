package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class OilPotBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<OilPotBlock> CODEC = simpleCodec(p -> new OilPotBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
   private static final VoxelShape AABB = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

   public OilPotBlock() {
      super(
         ModRegistrationProperties.blockProperties().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.BELL).instabreak().pushReaction(PushReaction.DESTROY).sound(SoundType.LANTERN)
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, false)).setValue(FACING, Direction.NORTH))
            .setValue(HAS_OIL, false)
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

      return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be && stack.getItem() instanceof OilPotItem) {
         int oilCount = OilPotItem.getOilCount(stack);
         be.setOilCount(oilCount);
      }
   }

   protected InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (level.getBlockEntity(pos) instanceof OilPotBlockEntity oilPot) {
         ItemStack mainHandItem = player.getMainHandItem();
         if (mainHandItem.isEmpty()) {
            int currentOilCount = oilPot.getOilCount();
            if (currentOilCount <= 0) {
               return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
               int needOilCount = Math.min(currentOilCount, 64);
               ItemStack oilStack = new ItemStack((ItemLike)ModItems.OIL.get(), needOilCount);
               player.setItemInHand(hand, oilStack);
               oilPot.setOilCount(currentOilCount - needOilCount);
               player.playSound(SoundEvents.LANTERN_HIT, 1.0F, player.getRandom().nextFloat() * 0.2F + 0.8F);
               return InteractionResult.SUCCESS;
            }
         } else if (mainHandItem.is((Item)ModItems.OIL.get())) {
            int currentOilCount = oilPot.getOilCount();
            int needOilCount = 256 - currentOilCount;
            if (needOilCount <= 0) {
               return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
               int addOilCount = Math.min(needOilCount, mainHandItem.getCount());
               oilPot.setOilCount(currentOilCount + addOilCount);
               if (!player.isCreative()) {
                  mainHandItem.shrink(addOilCount);
               }

               player.playSound(SoundEvents.LANTERN_HIT, 1.0F, player.getRandom().nextFloat() * 0.2F + 0.4F);
               return InteractionResult.SUCCESS;
            }
         } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      } else {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{WATERLOGGED, FACING, HAS_OIL});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return AABB;
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be) {
         double signal = be.getOilCount() / 256.0;
         int baseSignal = be.getOilCount() > 0 ? 1 : 0;
         return Mth.floor(signal * 14.0) + baseSignal;
      } else {
         return 0;
      }
   }

   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
      ItemStack stack = super.getCloneItemStack(level, pos, state, includeData, player);
      if (level.getBlockEntity(pos) instanceof OilPotBlockEntity be) {
         int oilCount = be.getOilCount();
         OilPotItem.setOilCount(stack, oilCount);
         return stack;
      } else {
         return stack;
      }
   }

   public List<ItemStack> getDrops(BlockState pState, net.minecraft.world.level.storage.loot.LootParams.Builder pParams) {
      List<ItemStack> stacks = super.getDrops(pState, pParams);
      BlockEntity blockEntity = (BlockEntity)pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (blockEntity instanceof OilPotBlockEntity oilPot) {
         stacks.forEach(s -> {
            if (s.is((Item)ModItems.OIL_POT.get())) {
               int oilCount = oilPot.getOilCount();
               OilPotItem.setOilCount(s, oilCount);
            }
         });
         return stacks;
      } else {
         return stacks;
      }
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new OilPotBlockEntity(pPos, pState);
   }
}
