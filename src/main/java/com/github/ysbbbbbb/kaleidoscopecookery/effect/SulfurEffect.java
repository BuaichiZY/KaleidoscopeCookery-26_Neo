package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.AABB;

public class SulfurEffect extends BaseEffect {
   public SulfurEffect(int color) {
      super(color);
   }

   @Override
   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return duration % 5 == 0;
   }

   @Override
   public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
      AABB aabb = new AABB(livingEntity.blockPosition()).inflate(8.0, 16.0, 8.0);

      for (Phantom phantom : livingEntity.level().getEntitiesOfClass(Phantom.class, aabb)) {
         if (livingEntity.equals(phantom.getTarget())) {
            phantom.setTarget(null);
         }
      }

      return true;
   }
}
