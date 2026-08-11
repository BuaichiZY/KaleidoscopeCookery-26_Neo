package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.NinePart;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.FoodBiteThreeByThreeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FoodBiteThreeByThreeBlock extends FoodBiteBlock implements EntityBlock {
   public static final EnumProperty<NinePart> PART = EnumProperty.create("part", NinePart.class);
   private static final VoxelShape LEFT_UP = Block.box(4.0, 0.0, 4.0, 16.0, 2.0, 16.0);
   private static final VoxelShape UP = Block.box(0.0, 0.0, 4.0, 16.0, 2.0, 16.0);
   private static final VoxelShape RIGHT_UP = Block.box(0.0, 0.0, 4.0, 12.0, 2.0, 16.0);
   private static final VoxelShape LEFT_CENTER = Block.box(4.0, 0.0, 0.0, 16.0, 2.0, 16.0);
   private static final VoxelShape CENTER = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
   private static final VoxelShape RIGHT_CENTER = Block.box(0.0, 0.0, 0.0, 12.0, 2.0, 16.0);
   private static final VoxelShape LEFT_DOWN = Block.box(4.0, 0.0, 0.0, 16.0, 2.0, 12.0);
   private static final VoxelShape DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 12.0);
   private static final VoxelShape RIGHT_DOWN = Block.box(0.0, 0.0, 0.0, 12.0, 2.0, 12.0);

   public FoodBiteThreeByThreeBlock(FoodProperties foodProperties, int maxBites, @Nullable FoodBiteAnimateTicks.AnimateTick animateTick) {
      super(foodProperties, maxBites, animateTick);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.bites, 0)).setValue(FACING, Direction.SOUTH))
               .setValue(PART, NinePart.CENTER))
            .setValue(QUALITY, DEFAULT_QUALITY)
      );
   }

   private static void handleRemove(Level world, BlockPos pos, BlockState state, @Nullable Player player) {
      if (!world.isClientSide()) {
         NinePart part = (NinePart)state.getValue(PART);
         BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
         BlockEntity te = world.getBlockEntity(centerPos);
         if (te instanceof FoodBiteThreeByThreeBlockEntity) {
            for (int i = -1; i < 2; i++) {
               for (int j = -1; j < 2; j++) {
                  BlockPos offsetPos = centerPos.offset(i, 0, j);
                  if (i == 0 && j == 0) {
                     world.destroyBlock(offsetPos, true, player);
                  } else {
                     world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 35);
                  }
               }
            }
         }
      }
   }

   @Override
   public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
      NinePart part = (NinePart)state.getValue(PART);
      BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
      BlockState centerState = level.getBlockState(centerPos);
      if (!centerState.is(this)) {
         return InteractionResult.PASS;
      } else {
         int bites = (Integer)centerState.getValue(this.bites);
         if (bites >= this.getMaxBites()) {
            handleRemove(level, centerPos, centerState, player);
            return InteractionResult.SUCCESS;
         } else {
            return level.isClientSide() && this.eat(level, centerPos, centerState, player).consumesAction()
               ? InteractionResult.SUCCESS
               : this.eat(level, centerPos, centerState, player);
         }
      }
   }

   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
      handleRemove(world, pos, state, player);
      return super.playerWillDestroy(world, pos, state, player);
   }

   public void onBlockExploded(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, Explosion explosion) {
      handleRemove(world, pos, state, null);
      super.onBlockExploded(state, world, pos, explosion);
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos centerPos = context.getClickedPos();

      for (int i = -1; i < 2; i++) {
         for (int j = -1; j < 2; j++) {
            BlockPos searchPos = centerPos.offset(i, 0, j);
            if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
               return null;
            }
         }
      }

      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(worldIn, pos, state, placer, stack);
      if (!worldIn.isClientSide()) {
         for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
               BlockPos searchPos = pos.offset(i, 0, j);
               NinePart part = NinePart.getPartByPos(i, j);
               if (part != null && !part.isCenter()) {
                  worldIn.setBlock(searchPos, (BlockState)state.setValue(PART, part), 3);
               }
            }
         }
      }
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      super.createBlockStateDefinition(pBuilder);
      pBuilder.add(new Property[]{PART});
   }

   @Override
   protected void createBitesBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{this.bites, FACING, QUALITY, PART});
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return ((NinePart)state.getValue(PART)).isCenter() ? new FoodBiteThreeByThreeBlockEntity(pos, state) : null;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.INVISIBLE;
   }

   @Override
   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      NinePart value = (NinePart)pState.getValue(PART);

      return switch (value) {
         case LEFT_UP -> LEFT_UP;
         case UP -> UP;
         case RIGHT_UP -> RIGHT_UP;
         case LEFT_CENTER -> LEFT_CENTER;
         case CENTER -> CENTER;
         case RIGHT_CENTER -> RIGHT_CENTER;
         case LEFT_DOWN -> LEFT_DOWN;
         case DOWN -> DOWN;
         case RIGHT_DOWN -> RIGHT_DOWN;
      };
   }

   @Override
   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder pParams) {
      return state.getValue(PART) != NinePart.CENTER ? Collections.emptyList() : super.getDrops(state, pParams);
   }
}
