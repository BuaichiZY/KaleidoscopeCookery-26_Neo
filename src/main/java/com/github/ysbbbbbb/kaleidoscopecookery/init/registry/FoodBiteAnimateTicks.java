package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBiteAnimateTicks {
   public static final FoodBiteAnimateTicks.AnimateTick DARK_CUISINE_ANIMATE_TICK = new FoodBiteAnimateTicks.AnimateTick() {
      @Override
      public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
         if (random.nextInt(2) == 0) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            level.addParticle(
               ParticleTypes.SMOKE,
               x + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               y + random.nextDouble() / 3.0,
               z + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
               0.0,
               0.02,
               0.0
            );
         }
      }
   };
   public static final FoodBiteAnimateTicks.AnimateTick SUSPICIOUS_STIR_FRY_ANIMATE_TICK = new FoodBiteAnimateTicks.AnimateTick() {
      @Override
      public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
         if (random.nextInt(3) == 0) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            level.addParticle(
               ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.40784314F, 0.21176471F, 0.5019608F),
               x + random.nextDouble() / 5.0 * (random.nextBoolean() ? 1 : -1),
               y + random.nextDouble() / 5.0,
               z + random.nextDouble() / 5.0 * (random.nextBoolean() ? 1 : -1),
               0.1,
               0.1,
               0.1
            );
         }
      }
   };
   public static final FoodBiteAnimateTicks.AnimateTick POT_SOUP_ANIMATE_TICK = new FoodBiteAnimateTicks.AnimateTick() {
      @Override
      public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
         if (random.nextInt(2) == 0) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            level.addParticle(
               (ParticleOptions)ModParticles.COOKING.get(),
               x + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
               y + random.nextDouble() / 4.0,
               z + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
               0.0,
               0.03,
               0.0
            );
         }
      }
   };

   public interface AnimateTick {
      default void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      }
   }
}
