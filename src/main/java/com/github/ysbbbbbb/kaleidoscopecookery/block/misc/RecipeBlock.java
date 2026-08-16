package com.github.ysbbbbbb.kaleidoscopecookery.block.misc;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoundType;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RecipeBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
   public static final MapCodec<RecipeBlock> CODEC = simpleCodec(p -> new RecipeBlock());
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape CEILING_AABB_X = Block.box(1.5, 15.75, 3.0, 14.5, 16.0, 13.0);
   private static final VoxelShape CEILING_AABB_Z = Block.box(3.0, 15.75, 1.5, 13.0, 16.0, 14.5);
   private static final VoxelShape FLOOR_AABB_X = Block.box(1.5, 0.0, 3.0, 14.5, 0.25, 13.0);
   private static final VoxelShape FLOOR_AABB_Z = Block.box(3.0, 0.0, 1.5, 13.0, 0.25, 14.5);
   private static final VoxelShape NORTH_AABB = Block.box(3.0, 1.5, 15.75, 13.0, 14.5, 16.0);
   private static final VoxelShape SOUTH_AABB = Block.box(3.0, 1.5, 0.0, 13.0, 14.5, 0.25);
   private static final VoxelShape WEST_AABB = Block.box(15.75, 1.5, 3.0, 16.0, 14.5, 13.0);
   private static final VoxelShape EAST_AABB = Block.box(0.0, 1.5, 3.0, 0.25, 14.5, 13.0);

   public RecipeBlock() {
      super(ModRegistrationProperties.blockProperties().mapColor(MapColor.COLOR_YELLOW).instabreak().noOcclusion().sound(ModSoundType.RECIPE_BLOCK));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(FACE, AttachFace.WALL))
            .setValue(WATERLOGGED, false)
      );
   }

   public InteractionResult useItemOn(
      ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
   ) {
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else {
         ItemStack mainHandItem = player.getMainHandItem();
         if (!mainHandItem.isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
         } else if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
         } else if (level.getBlockEntity(pos) instanceof RecipeBlockEntity recipeBlockEntity) {
            ItemStack itemStack = recipeBlockEntity.getItems().getStackInSlot(0);
            if (itemStack.isEmpty()) {
               return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
               player.setItemInHand(hand, itemStack.copy());
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
               level.playSound(null, pos, ModSoundType.RECIPE_BLOCK.getBreakSound(), player.getSoundSource(), 1.0F, 1.0F);
               return InteractionResult.SUCCESS;
            }
         } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
         }
      }
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader levelAccessor, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      return super.updateShape(state, levelAccessor, ticks, pos, direction, neighborPos, neighborState, random);
   }

   public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      Direction facing = (Direction)state.getValue(FACING);

      return switch ((AttachFace)state.getValue(FACE)) {
         case FLOOR -> facing.getAxis() == Axis.X ? FLOOR_AABB_X : FLOOR_AABB_Z;
         case WALL -> {
            switch (facing) {
               case EAST:
                  yield EAST_AABB;
               case WEST:
                  yield WEST_AABB;
               case SOUTH:
                  yield SOUTH_AABB;
               case NORTH:
               case UP:
               case DOWN:
                  yield NORTH_AABB;
               default:
                  throw new MatchException(null, null);
            }
         }
         default -> facing.getAxis() == Axis.X ? CEILING_AABB_X : CEILING_AABB_Z;
      };
   }

   protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      BlockState stateForPlacement = super.getStateForPlacement(context);
      return stateForPlacement != null ? (BlockState)stateForPlacement.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER) : null;
   }

   public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity livingEntity, ItemStack stack) {
      if (!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof RecipeBlockEntity recipeBlockEntity) {
         recipeBlockEntity.getItems().setStackInSlot(0, stack.copyWithCount(1));
         recipeBlockEntity.refresh();
      }
   }

   public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
      if (level.getBlockEntity(pos) instanceof RecipeBlockEntity recipeBlockEntity) {
         ItemStack itemStack = recipeBlockEntity.getItems().getStackInSlot(0);
         if (!itemStack.isEmpty()) {
            return itemStack.copy();
         }
      }

      return super.getCloneItemStack(level, pos, state, includeData, player);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      pBuilder.add(new Property[]{FACING, FACE, WATERLOGGED});
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      List<ItemStack> drops = super.getDrops(state, lootParamsBuilder);
      BlockEntity parameter = (BlockEntity)lootParamsBuilder.getParameter(LootContextParams.BLOCK_ENTITY);
      if (parameter instanceof RecipeBlockEntity recipeBlock) {
         drops.add(recipeBlock.getItems().getStackInSlot(0).copyWithCount(1));
      }

      return drops;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new RecipeBlockEntity(pPos, pState);
   }
}
