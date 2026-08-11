package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IStockpot;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StockpotBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<StockpotBlock> CODEC = simpleCodec(p -> new StockpotBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");
   public static final BooleanProperty HAS_BASE = BooleanProperty.create("has_base");
   public static final BooleanProperty HAS_CHAINS = BooleanProperty.create("has_chains");
   private static final VoxelShape AABB = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 5.0, 14.0), Block.box(1.0, 5.0, 1.0, 15.0, 7.0, 15.0));
   private static final VoxelShape AABB_WITH_LID = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 9.0, 14.0), Block.box(1.0, 5.0, 1.0, 15.0, 7.0, 15.0));

   public StockpotBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.METAL).sound(ModSoundType.POT).noOcclusion().strength(1.5F, 6.0F));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH))
                     .setValue(WATERLOGGED, false))
                  .setValue(HAS_LID, false))
               .setValue(HAS_BASE, false))
            .setValue(HAS_CHAINS, false)
      );
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer instanceof Player player && level.getBlockEntity(pos) instanceof IStockpot stockpot && stockpot.hasHeatSource(level)) {
         ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_stockpot_on_heat_source");
      }
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      if (direction == Direction.DOWN && !(Boolean)state.getValue(HAS_CHAINS)) {
         return (BlockState)((BlockState)state.setValue(HAS_CHAINS, canSupportCenter(levelAccessor, pos.above(), Direction.DOWN)))
            .setValue(HAS_BASE, !neighborState.isFaceSturdy(levelAccessor, neighborPos, Direction.UP));
      } else if (direction == Direction.UP && !(Boolean)state.getValue(HAS_BASE)) {
         BlockState belowState = levelAccessor.getBlockState(pos.below());
         return (BlockState)((BlockState)state.setValue(HAS_CHAINS, canSupportCenter(levelAccessor, neighborPos, Direction.DOWN)))
            .setValue(HAS_BASE, !belowState.isFaceSturdy(levelAccessor, pos.below(), Direction.UP));
      } else {
         return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
      }
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (!(level.getBlockEntity(pos) instanceof IStockpot stockpot)) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         // Recipe/carrier lookup is server-only.  Let the server own the whole
         // interaction so a flower pot is not predicted as a placed block.
         if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
         }
         ItemStack mainHandItem = player.getMainHandItem();
         if (stockpot.onLitClick(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
         } else if (stockpot.addSoupBase(level, player, mainHandItem)) {
            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "put_soup_base_in_stockpot");
            return InteractionResult.SUCCESS;
         } else if (stockpot.removeSoupBase(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
         } else if (!mainHandItem.isEmpty() && stockpot.addIngredient(level, player, mainHandItem)) {
            return InteractionResult.SUCCESS;
         } else if ((mainHandItem.isEmpty() || mainHandItem.is(TagMod.INGREDIENT_CONTAINER)) && stockpot.removeIngredient(level, player)) {
            return InteractionResult.SUCCESS;
         } else {
            return stockpot.takeOutProduct(level, player, mainHandItem)
               ? InteractionResult.SUCCESS
               : InteractionResult.TRY_WITH_EMPTY_HAND;
         }
      }
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new StockpotBlockEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return level.isClientSide()
         ? createTickerHelper(blockEntityType, ModBlocks.STOCKPOT_BE.get(), (lvl, blockPos, blockState, pot) -> pot.clientTick())
         : createTickerHelper(blockEntityType, ModBlocks.STOCKPOT_BE.get(), (lvl, blockPos, blockState, pot) -> pot.tick(lvl));
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Level level = context.getLevel();
      FluidState fluidState = level.getFluidState(context.getClickedPos());
      Direction clickFace = context.getClickedFace();
      BlockState blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      BlockPos abovePos = context.getClickedPos().above();
      if (clickFace == Direction.DOWN && canSupportCenter(level, abovePos, Direction.DOWN)) {
         return (BlockState)blockState.setValue(HAS_CHAINS, true);
      } else {
         BlockPos belowPos = context.getClickedPos().below();
         BlockState belowState = level.getBlockState(belowPos);
         return !belowState.isFaceSturdy(level, belowPos, Direction.UP) ? (BlockState)blockState.setValue(HAS_BASE, true) : blockState;
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, WATERLOGGED, HAS_LID, HAS_BASE, HAS_CHAINS});
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return state.getValue(HAS_LID) ? AABB_WITH_LID : AABB;
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      if ((Boolean)state.getValue(HAS_LID)) {
         BlockEntity parameter = (BlockEntity)lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
         if (parameter instanceof StockpotBlockEntity stockpot && !stockpot.getLidItem().isEmpty()) {
            drops.add(stockpot.getLidItem().copy());
         } else {
            drops.add(new ItemStack((ItemLike)ModItems.STOCKPOT_LID.get()));
         }
      }

      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof StockpotBlockEntity stockpotBlock && stockpotBlock.getStatus() == 1) {
         stockpotBlock.getInputs().forEach(stack -> {
            if (!stack.isEmpty()) {
               drops.add(stack);
            }
         });
      }

      return drops;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot").withStyle(ChatFormatting.GRAY));
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot.fail").withStyle(ChatFormatting.GRAY));
   }
}
