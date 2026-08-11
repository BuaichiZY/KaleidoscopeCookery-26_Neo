package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class FoodBiteOneByTwoBlock extends FoodBiteBlock {
   public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 1);
   public static final int LEFT = 0;
   public static final int RIGHT = 1;

   public FoodBiteOneByTwoBlock(FoodProperties foodProperties, int maxBites, @Nullable FoodBiteAnimateTicks.AnimateTick animateTick) {
      super(foodProperties, maxBites, animateTick);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.bites, 0)).setValue(FACING, Direction.SOUTH))
               .setValue(POSITION, 1))
            .setValue(QUALITY, DEFAULT_QUALITY)
      );
   }

   public BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random) {
      int position = (Integer)state.getValue(POSITION);
      Direction facing = (Direction)state.getValue(FACING);
      if (position == 0 && direction == facing.getCounterClockWise() || position == 1 && direction == facing.getClockWise()) {
         if (!neighborState.is(this) || neighborState.getValue(FACING) != facing || (Integer)neighborState.getValue(POSITION) == position) {
            return Blocks.AIR.defaultBlockState();
         }

         int neighborBites = (Integer)neighborState.getValue(this.bites);
         if (neighborBites != (Integer)state.getValue(this.bites)) {
            return (BlockState)state.setValue(this.bites, neighborBites);
         }
      }

      return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative() && (Integer)state.getValue(POSITION) == 0) {
         BlockPos right = pos.relative(((Direction)state.getValue(FACING)).getCounterClockWise());
         BlockState rightState = level.getBlockState(right);
         if (rightState.is(state.getBlock()) && (Integer)rightState.getValue(POSITION) == 1) {
            BlockState airBlockState = rightState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(right, airBlockState, 35);
            level.levelEvent(player, 2001, right, Block.getId(rightState));
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos rightPos = context.getClickedPos();
      Direction facing = context.getHorizontalDirection().getOpposite();
      Level level = context.getLevel();
      BlockPos leftPos = rightPos.relative(facing.getClockWise());
      return level.getBlockState(leftPos).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
   }

   public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
      Direction facing = (Direction)pState.getValue(FACING);
      BlockPos leftPos = pPos.relative(facing.getClockWise());
      BlockState leftState = (BlockState)pState.setValue(POSITION, 0);
      pLevel.setBlock(leftPos, leftState, 3);
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{POSITION});
   }

   @Override
   protected void createBitesBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{this.bites, FACING, QUALITY, POSITION});
   }

   @Override
   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder pParams) {
      return state.getValue(POSITION) == 0 ? Collections.emptyList() : super.getDrops(state, pParams);
   }
}
