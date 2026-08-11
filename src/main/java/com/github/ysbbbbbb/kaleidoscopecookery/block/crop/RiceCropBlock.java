package com.github.ysbbbbbb.kaleidoscopecookery.block.crop;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class RiceCropBlock extends BaseCropBlock implements SimpleWaterloggedBlock {
   public static final int DOWN = 0;
   public static final int MIDDLE = 1;
   public static final int UP = 2;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final IntegerProperty LOCATION = IntegerProperty.create("location", 0, 2);
   private static final Predicate<LivingEntity> RICE_GROWTH_BOOSTER = e -> e.isAlive() && e.getType().builtInRegistryHolder().is(TagMod.RICE_GROWTH_BOOSTER);
   private static final VoxelShape BASE_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
   private static final VoxelShape EMPTY_SHAPE = Shapes.empty();
   private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
      Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 0.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0),
      Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)
   };

   public RiceCropBlock() {
      super(ModItems.RICE_PANICLE, ModItems.RICE_SEED);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.getAgeProperty(), 0)).setValue(WATERLOGGED, false))
            .setValue(LOCATION, 0)
      );
   }

   public BlockState updateShape(
      BlockState state, net.minecraft.world.level.LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks,
      BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }

      if (facing == Direction.DOWN) {
         if (state.canSurvive(level, currentPos)) {
            return facingState.is(this) ? (BlockState)state.setValue(AGE, (Integer)facingState.getValue(AGE)) : state;
         } else {
            return Blocks.AIR.defaultBlockState();
         }
      } else {
         if (facing == Direction.UP) {
            int location = (Integer)state.getValue(LOCATION);
            if (location == 0) {
               if (facingState.is(this) && (Integer)facingState.getValue(LOCATION) == 1) {
                  return (BlockState)state.setValue(AGE, (Integer)facingState.getValue(AGE));
               }

               return Blocks.AIR.defaultBlockState();
            }

            if (location == 1) {
               if (facingState.is(this) && (Integer)facingState.getValue(LOCATION) == 2) {
                  return (BlockState)state.setValue(AGE, (Integer)facingState.getValue(AGE));
               }

               return Blocks.AIR.defaultBlockState();
            }
         }

         return state;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos blockPos = context.getClickedPos();
      Level level = context.getLevel();
      boolean isWaterlogged = level.getFluidState(blockPos).is(FluidTags.WATER);
      boolean aboveMatches = level.getBlockState(blockPos.above(1)).isAir();
      boolean above2Matches = level.getBlockState(blockPos.above(2)).isAir();
      return blockPos.getY() < level.getMaxY() + 1 - 2 && isWaterlogged && aboveMatches && above2Matches
         ? (BlockState)this.defaultBlockState().setValue(WATERLOGGED, true)
         : null;
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      level.setBlock(pos.above(1), (BlockState)this.getStateForAge(0).setValue(LOCATION, 1), 3);
      level.setBlock(pos.above(2), (BlockState)this.getStateForAge(0).setValue(LOCATION, 2), 3);
   }

   public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
      if (levelReader.getRawBrightness(pos, 0) < 8 && !levelReader.canSeeSky(pos)) {
         return false;
      } else {
         int location = (Integer)state.getValue(LOCATION);
         if (location == 0) {
            BlockPos basePos = pos.below();
            BlockState baseState = levelReader.getBlockState(basePos);
            return this.mayPlaceOn(baseState, levelReader, basePos) && state.getFluidState().is(FluidTags.WATER);
         } else if (location == 1) {
            BlockPos downPos = pos.below();
            BlockState downState = levelReader.getBlockState(downPos);
            return downState.is(this) && (Integer)downState.getValue(LOCATION) == 0;
         } else if (location != 2) {
            return false;
         } else {
            BlockPos middlePos = pos.below();
            BlockState middleState = levelReader.getBlockState(middlePos);
            return middleState.is(this) && (Integer)middleState.getValue(LOCATION) == 1;
         }
      }
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return super.mayPlaceOn(state, level, pos) || state.is(TagMod.RICE_PLANTABLE);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
      if (!this.isThreeBlock(state) && (Integer)state.getValue(LOCATION) == 1) {
         return SHAPE_BY_AGE[state.getValue(AGE)];
      } else if ((Integer)state.getValue(LOCATION) == 2) {
         return this.isThreeBlock(state) ? SHAPE_BY_AGE[state.getValue(AGE)] : EMPTY_SHAPE;
      } else {
         return BASE_SHAPE;
      }
   }

   private boolean isThreeBlock(BlockState state) {
      return state.is(this) && (Integer)state.getValue(AGE) > 3;
   }

   public boolean isRandomlyTicking(BlockState state) {
      return super.isRandomlyTicking(state) && (Integer)state.getValue(LOCATION) == 0;
   }

   public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
      if (serverLevel.isAreaLoaded(pos, 1)) {
         if ((Integer)state.getValue(LOCATION) == 0) {
            if (serverLevel.getSkyDarken() >= 4) {
               serverLevel.playSound(
                  null,
                  pos.above(),
                  (SoundEvent)ModSounds.BLOCK_PADDY.get(),
                  SoundSource.BLOCKS,
                  serverLevel.getRandom().nextFloat() * 0.2F + 0.2F,
                  serverLevel.getRandom().nextFloat() * 0.1F + 0.9F
               );
            }

            if (serverLevel.getRawBrightness(pos, 0) >= 9) {
               int age = this.getAge(state);
               if (age >= this.getMaxAge()) {
                  return;
               }

               float speed = getGrowthSpeed(state, serverLevel, pos) / 2.0F;
               List<LivingEntity> fish = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.0, 0.0, 1.0), RICE_GROWTH_BOOSTER);
               if (!fish.isEmpty()) {
                  float size = (float)(Math.log(fish.size()) / Math.log(2.0));
                  speed += speed * size;
               }

               if (CommonHooks.canCropGrow(serverLevel, pos, state, random.nextInt((int)(25.0F / speed) + 1) == 0)) {
                  this.setCropState(serverLevel, pos, age + 1);
                  CommonHooks.fireCropGrowPost(serverLevel, pos, state);
               }
            }
         }
      }
   }

   private void setCropState(Level level, BlockPos startPos, int age) {
      level.setBlock(startPos, (BlockState)this.getStateForAge(age).setValue(WATERLOGGED, true), 2);
   }

   public void growCrops(Level level, BlockPos pos, BlockState state) {
      int boneAge = this.getAge(state) + this.getBonemealAgeIncrease(level);
      int maxAge = this.getMaxAge();
      if (boneAge > maxAge) {
         boneAge = maxAge;
      }

      this.setCropState(level, pos.below((Integer)state.getValue(LOCATION)), boneAge);
   }

   protected int getBonemealAgeIncrease(Level level) {
      return Mth.nextInt(level.getRandom(), 1, 2);
   }

   public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
      return InteractionResult.PASS;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AGE, LOCATION, WATERLOGGED});
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder lootParamsBuilder) {
      return state.getValue(LOCATION) != 0 ? Collections.emptyList() : super.getDrops(state, lootParamsBuilder);
   }

   public ItemStack pickupBlock(Player player, LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
      if ((Boolean)state.getValue(BlockStateProperties.WATERLOGGED)) {
         levelAccessor.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, false), 3);
         if ((Integer)state.getValue(LOCATION) == 0) {
            levelAccessor.destroyBlock(pos, true);
         }

         return new ItemStack(Items.WATER_BUCKET);
      } else {
         return ItemStack.EMPTY;
      }
   }
}
