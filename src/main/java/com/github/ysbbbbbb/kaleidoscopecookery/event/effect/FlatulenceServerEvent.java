package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModAttachmentType;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class FlatulenceServerEvent {
   @SubscribeEvent
   public static void onMobEffectEvent(Remove event) {
      LivingEntity entity = event.getEntity();
      Holder<MobEffect> effect = event.getEffect();
      if (entity instanceof ServerPlayer serverPlayer
         && effect.is(ModEffects.FLATULENCE)
         && serverPlayer.hasData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION)) {
         serverPlayer.removeData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION);
      }
   }

   @SubscribeEvent
   public static void onMobEffectEvent(Expired event) {
      LivingEntity entity = event.getEntity();
      MobEffectInstance effect = event.getEffectInstance();
      if (entity instanceof ServerPlayer serverPlayer
         && effect.is(ModEffects.FLATULENCE)
         && serverPlayer.hasData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION)) {
         serverPlayer.removeData(ModAttachmentType.FLATULENCE_EFFECT_STARTING_POSITION);
      }
   }
}
