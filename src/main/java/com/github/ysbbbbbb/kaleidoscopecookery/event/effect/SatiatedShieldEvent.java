package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class SatiatedShieldEvent {
   @SubscribeEvent
   public static void onPlayerHurt(Pre event) {
      DamageSource source = event.getSource();
      if (event.getEntity() instanceof Player player && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         if (!(Boolean)GeneralConfig.SATIATED_SHIELD_ABSORB_ENABLED.get()) {
            return;
         }

         if (!isSatiatedShieldApply(player)) {
            return;
         }

         float originalDamage = event.getContainer().getNewDamage();
         float finalDamage = calculateFinalDamage(player, source, originalDamage);
         event.getContainer().setNewDamage(finalDamage);
      }
   }

   public static boolean isSatiatedShieldApply(Player player) {
      return player.hasEffect(MobEffects.HUNGER) && GeneralConfig.IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT.get()
         ? false
         : player.getFoodData().getFoodLevel() >= (Integer)GeneralConfig.SATIATED_SHIELD_MIN_FOOD_LEVEL.get() && player.hasEffect(ModEffects.SATIATED_SHIELD);
   }

   public static float calculateFinalDamage(Player player, DamageSource source, float originalDamage) {
      float reducedDamage = (float)(originalDamage * (Double)GeneralConfig.SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT.get());
      if (reducedDamage > (Double)GeneralConfig.SATIATED_SHIELD_MAX_DAMAGE_REDUCTION.get()) {
         reducedDamage = (float)((Double)GeneralConfig.SATIATED_SHIELD_MAX_DAMAGE_REDUCTION.get() * 1.0);
      }

      float finalDamage = originalDamage - reducedDamage;
      if (originalDamage > (Double)GeneralConfig.SATIATED_SHIELD_MIN_DAMAGE.get()) {
         finalDamage = (float)Math.max((double)finalDamage, (Double)GeneralConfig.SATIATED_SHIELD_MIN_DAMAGE.get());
         reducedDamage = originalDamage - finalDamage;
      }

      int exhaustionAmount = Math.toIntExact(Math.round(reducedDamage * (Double)GeneralConfig.SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE.get()));
      if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
         exhaustionAmount = (int)(exhaustionAmount * (Double)GeneralConfig.SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER.get());
      }

      if (!(Boolean)GeneralConfig.SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE.get()) {
         float absorbedDamage = (float)(player.getFoodData().getFoodLevel() * 4 / (Double)GeneralConfig.SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT.get());
         if (source.is(TagMod.SATIATED_SHIELD_WEAKNESS)) {
            absorbedDamage = (float)(absorbedDamage / (Double)GeneralConfig.SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER.get());
         }

         finalDamage += Math.max(0.0F, reducedDamage - absorbedDamage);
      }

      float exhaustion = Math.max(0, exhaustionAmount);
      player.causeFoodExhaustion(exhaustion);
      return finalDamage;
   }
}
