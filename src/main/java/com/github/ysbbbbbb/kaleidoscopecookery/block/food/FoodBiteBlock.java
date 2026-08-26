package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FoodBiteBlock extends FoodBlock {
   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final IntegerProperty QUALITY = IntegerProperty.create("quality", 0, Quality.values().length);
   // Registered foods use at most six bites. Keeping sixteen theoretical
   // values forces the client to bake thousands of unreachable model states.
   private static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
   public static final int DEFAULT_QUALITY = Quality.values().length;
   protected final FoodProperties foodProperties;
   protected final IntegerProperty bites;
   protected final int maxBites;
   @Nullable
   protected final FoodBiteAnimateTicks.AnimateTick animateTick;
   protected VoxelShape aabb = FoodBlock.AABB;

   public FoodBiteBlock(FoodProperties foodProperties, int maxBites, @Nullable FoodBiteAnimateTicks.AnimateTick animateTick) {
      this.maxBites = maxBites;
      this.foodProperties = foodProperties;
      this.bites = BITES;
      this.animateTick = animateTick;
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.bites, 0)).setValue(FACING, Direction.SOUTH))
            .setValue(QUALITY, DEFAULT_QUALITY)
      );
   }

   public FoodBiteBlock(FoodProperties foodProperties) {
      this(foodProperties, 3, null);
   }

   public FoodBiteBlock setAABB(VoxelShape aabb) {
      this.aabb = aabb;
      return this;
   }

   public IntegerProperty getBites() {
      return this.bites;
   }

   public int getMaxBites() {
      return this.maxBites;
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      if (this.animateTick != null) {
         this.animateTick.animateTick(state, level, pos, random);
      }
   }

   public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
      int bites = (Integer)state.getValue(this.bites);
      if (bites >= this.getMaxBites()) {
         level.destroyBlock(pos, true, player);
         return InteractionResult.SUCCESS;
      } else {
         return level.isClientSide() && this.eat(level, pos, state, player).consumesAction() ? InteractionResult.SUCCESS : this.eat(level, pos, state, player);
      }
   }

   protected InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
      if (!player.canEat(this.foodProperties.canAlwaysEat())) {
         return InteractionResult.PASS;
      } else {
         double radio = 1.0;
         int qualityNum = (Integer)state.getValue(QUALITY);
         if (qualityNum != DEFAULT_QUALITY) {
            radio = Quality.BY_ID.apply(qualityNum).getRatio();
         }

         player.getFoodData().eat((int)Math.round(this.foodProperties.nutrition() * radio), (float)(this.foodProperties.saturation() * radio));

         for (ModFoods.LegacyEffect possibleEffect : ModFoods.effectsFor(this.foodProperties)) {
            MobEffectInstance instance = possibleEffect.effect();
            if (!level.isClientSide() && level.getRandom().nextFloat() < possibleEffect.probability()) {
               MobEffectInstance newInstance = new MobEffectInstance(
                  instance.getEffect(), (int)Math.round(instance.getDuration() * radio), instance.getAmplifier()
               );
               player.addEffect(newInstance);
            }
         }

         level.playSound(null, pos, SoundEvents.GENERIC_EAT.value(), SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
         level.gameEvent(player, GameEvent.EAT, pos);
         int bites = (Integer)state.getValue(this.bites);
         if (bites < this.getMaxBites()) {
            level.setBlock(pos, (BlockState)state.setValue(this.bites, bites + 1), 3);
         }

         return InteractionResult.SUCCESS;
      }
   }

   @Override
   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return this.aabb;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{BITES, FACING, QUALITY});
   }

   protected void createBitesBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{BITES, FACING, QUALITY});
   }

   public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
      int value = (Integer)state.getValue(this.bites);
      return (3 - value) * 5;
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
      return false;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Direction opposite = context.getHorizontalDirection().getOpposite();
      int quality = DEFAULT_QUALITY;
      ItemStack itemInHand = context.getItemInHand();
      if (QualityUtils.hasQuality(itemInHand)) {
         quality = QualityUtils.getQuality(itemInHand).getId();
      }

      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, opposite)).setValue(QUALITY, quality);
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      List<ItemStack> drops = super.getDrops(state, params);
      int value = (Integer)state.getValue(QUALITY);
      if (value == DEFAULT_QUALITY) {
         return drops;
      } else if ((Integer)state.getValue(this.bites) != 0) {
         return drops;
      } else {
         drops.forEach(stack -> {
            if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FoodBiteBlock) {
               Quality quality = Quality.BY_ID.apply(value);
               QualityUtils.setQuality(stack, quality);
            }
         });
         return drops;
      }
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
   }
}
