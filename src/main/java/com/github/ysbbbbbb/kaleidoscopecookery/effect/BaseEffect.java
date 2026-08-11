package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class BaseEffect extends MobEffect {
   public BaseEffect(MobEffectCategory category, int color) {
      super(category, color);
   }

   public BaseEffect(int color) {
      this(MobEffectCategory.BENEFICIAL, color);
   }

   public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
      return true;
   }

   public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return false;
   }
}
