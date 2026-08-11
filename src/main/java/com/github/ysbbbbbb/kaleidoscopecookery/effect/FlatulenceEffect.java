package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModAttachmentType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import net.minecraft.advancements.criterion.DistanceTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

public class FlatulenceEffect extends BaseEffect {
   public FlatulenceEffect(int color) {
      super(color);
   }

   @Override
   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      if (duration < 5) {
      }

      return duration % 10 == 5;
   }

   @Override
   public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
      if (livingEntity instanceof ServerPlayer serverPlayer) {
         if (!serverPlayer.hasData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION)) {
            serverPlayer.setData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION, serverPlayer.position());
         } else {
            Vec3 position = (Vec3)serverPlayer.getData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION);
            ((DistanceTrigger)ModTrigger.FLATULENCE_FLY_HEIGHT.get()).trigger(serverPlayer, position);
         }
      }

      return true;
   }

   public void removeAttributeModifiers(AttributeMap attributeMap) {
      super.removeAttributeModifiers(attributeMap);
   }
}
