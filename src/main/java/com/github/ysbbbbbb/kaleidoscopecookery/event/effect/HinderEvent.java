package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class HinderEvent {
   @SubscribeEvent
   public static void onLivingDamage(Pre event) {
      LivingEntity target = event.getEntity();
      if (!target.level().isClientSide()) {
         DamageSource source = event.getSource();
         if (source.getEntity() instanceof LivingEntity attacker && attacker.hasEffect(ModEffects.HINDER)) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
         }
      }
   }
}
