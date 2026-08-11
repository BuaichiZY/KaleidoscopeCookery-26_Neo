package com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.blockentity.IShawarmaSpit;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShawarmaSpitBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final MapCodec<ShawarmaSpitBlock> CODEC = simpleCodec(p -> new ShawarmaSpitBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
   public static final VoxelShape UPPER_AABB = Shapes.or(Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), Block.box(6.0, 0.0, 6.0, 10.0, 14.0, 10.0));
   public static final VoxelShape LOWER_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0), Block.box(6.0, 7.0, 6.0, 10.0, 16.0, 10.0));

   public ShawarmaSpitBlock() {
      super(
         ModRegistrationProperties.blockProperties()
            .mapColor(MapColor.METAL)
            .noOcclusion()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .lightLevel(state -> state.getValue(POWERED) ? 8 : 0)
            .sound(SoundType.METAL)
      );
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH))
                  .setValue(HALF, DoubleBlockHalf.LOWER))
               .setValue(WATERLOGGED, false))
            .setValue(POWERED, false)
      );
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
   ) {
      return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (player.isShiftKeyDown()) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         if (level.getBlockEntity(pos) instanceof IShawarmaSpit shawarmaSpit) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (shawarmaSpit.onPutCookingItem(level, heldItem)) {
               return InteractionResult.SUCCESS;
            }

            if (shawarmaSpit.onTakeCookedItem(level, player)) {
               return InteractionResult.SUCCESS;
            }
         }

         return InteractionResult.TRY_WITH_EMPTY_HAND;
      }
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
      return createTickerHelper(blockEntityType, ModBlocks.SHAWARMA_SPIT_BE.get(), (levelIn, blockPos, blockState, spit) -> {
         if ((Boolean)blockState.getValue(POWERED)) {
            spit.tick();
         }
      });
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_AABB : UPPER_AABB;
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos currentPos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      DoubleBlockHalf half = (DoubleBlockHalf)state.getValue(HALF);
      boolean isLowerHalf = half == DoubleBlockHalf.LOWER && direction == Direction.UP;
      boolean isUpperHalf = half == DoubleBlockHalf.UPPER && direction == Direction.DOWN;
      if (direction.getAxis() == Axis.Y && (isLowerHalf || isUpperHalf)) {
         return neighborState.is(this) && neighborState.getValue(HALF) != half
            ? (BlockState)((BlockState)state.setValue(FACING, (Direction)neighborState.getValue(FACING)))
               .setValue(POWERED, (Boolean)neighborState.getValue(POWERED))
            : Blocks.AIR.defaultBlockState();
      } else {
         return super.updateShape(state, levelAccessor, ticks, currentPos, direction, neighborPos, neighborState, random);
      }
   }

   @Override
   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean isMoving) {
      Direction direction = state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
      BlockPos otherPos = pos.relative(direction);
      boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(otherPos);
      if (powered != (Boolean)state.getValue(POWERED)) {
         level.setBlock(pos, (BlockState)state.setValue(POWERED, powered), 2);
         BlockState otherState = level.getBlockState(otherPos);
         if (otherState.is(this)
            && otherState.getValue(HALF) != state.getValue(HALF)
            && powered != (Boolean)otherState.getValue(POWERED)) {
            level.setBlock(otherPos, (BlockState)otherState.setValue(POWERED, powered), 2);
         }
      }
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative()) {
         if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
               this.dropCookItems(level, below);
               BlockState airBlockState = belowState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
               level.setBlock(below, airBlockState, 35);
               level.levelEvent(player, 2001, below, Block.getId(belowState));
            }
         } else {
            this.dropCookItems(level, pos);
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   private void dropCookItems(Level level, BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof ShawarmaSpitBlockEntity shawarmaSpit) {
         if (!shawarmaSpit.cookingItem.isEmpty()) {
            popResource(level, pos, shawarmaSpit.cookingItem.copy());
            shawarmaSpit.cookingItem = ItemStack.EMPTY;
         } else if (!shawarmaSpit.cookedItem.isEmpty()) {
            popResource(level, pos, shawarmaSpit.cookedItem.copy());
            shawarmaSpit.cookedItem = ItemStack.EMPTY;
         }
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos pos = context.getClickedPos();
      Level level = context.getLevel();
      FluidState fluidState = context.getLevel().getFluidState(pos);
      if (pos.getY() < level.getMaxY() + 1 - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
         boolean isPowered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
         return (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()))
                  .setValue(POWERED, isPowered))
               .setValue(HALF, DoubleBlockHalf.LOWER))
            .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      } else {
         return null;
      }
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      FluidState fluidState = level.getFluidState(pos);
      BlockState blockState = (BlockState)((BlockState)state.setValue(HALF, DoubleBlockHalf.UPPER)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      level.setBlockAndUpdate(pos.above(), blockState);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HALF, WATERLOGGED, POWERED});
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new ShawarmaSpitBlockEntity(pos, state);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops;
      if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
         drops = super.getDrops(state, lootParamsBuilder);
      } else {
         drops = Lists.newArrayList();
      }

      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof ShawarmaSpitBlockEntity shawarmaSpit) {
         if (!shawarmaSpit.cookingItem.isEmpty()) {
            drops.add(shawarmaSpit.cookingItem.copy());
         } else if (!shawarmaSpit.cookedItem.isEmpty()) {
            drops.add(shawarmaSpit.cookedItem.copy());
         }
      }

      return drops;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.shawarma_spit").withStyle(ChatFormatting.GRAY));
   }
}
