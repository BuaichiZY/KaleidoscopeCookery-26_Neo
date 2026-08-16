package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public final class QualityUtils {
   public static void setQuality(ItemStack food, Quality quality) {
      food.set(ModDataComponents.QUALITY, quality);
      FoodProperties rawFood = food.getItem().components().get(DataComponents.FOOD);
      if (rawFood != null) {
         food.set(DataComponents.FOOD, modifyFoodProperties(rawFood, quality));
      }

      Consumable rawConsumable = food.getItem().components().get(DataComponents.CONSUMABLE);
      if (rawConsumable != null) {
         food.set(DataComponents.CONSUMABLE, modifyConsumable(rawConsumable, quality));
      }
   }

   public static Quality getQuality(ItemStack food) {
      return food.has(ModDataComponents.QUALITY) ? (Quality)food.get(ModDataComponents.QUALITY) : Quality.STANDARD;
   }

   public static boolean hasQuality(ItemStack food) {
      return food.has(ModDataComponents.QUALITY);
   }

   public static List<MobEffectInstance> modifyEffects(List<MobEffectInstance> effectInstances, Quality quality) {
      List<MobEffectInstance> list = Lists.newArrayList();

      for (MobEffectInstance instance : effectInstances) {
         int duration = (int)Math.round(quality.getRatio() * instance.getDuration());
         if (duration > 0) {
            list.add(new MobEffectInstance(instance.getEffect(), duration, instance.getAmplifier()));
         }
      }

      return list;
   }

   public static FoodProperties modifyFoodProperties(FoodProperties raw, Quality quality) {
      double ratio = quality.getRatio();
      int nutrition = (int)Math.round(ratio * raw.nutrition());
      float saturation = (float)ratio * raw.saturation();
      List<ModFoods.LegacyEffect> effects = Lists.newArrayList();
      ModFoods.effectsFor(raw).forEach(effect -> {
         if (effect.probability() >= 1.0F) {
            MobEffectInstance instance = effect.effect();
            int duration = (int)Math.round(ratio * instance.getDuration());
            int amplifier = instance.getAmplifier();
            if (duration > 0) {
               MobEffectInstance newEffect = new MobEffectInstance(instance.getEffect(), duration, amplifier);
               ModFoods.LegacyEffect possibleEffect = new ModFoods.LegacyEffect(() -> newEffect, effect.probability());
               effects.add(possibleEffect);
            }
         }
      });
      FoodProperties modified = new FoodProperties(nutrition, saturation, raw.canAlwaysEat());
      ModFoods.LEGACY_EFFECTS.put(modified, List.copyOf(effects));
      return modified;
   }

   private static Consumable modifyConsumable(Consumable raw, Quality quality) {
      Consumable.Builder builder = Consumable.builder()
         .consumeSeconds(raw.consumeSeconds())
         .animation(raw.animation())
         .sound(raw.sound())
         .hasConsumeParticles(raw.hasConsumeParticles());

      for (ConsumeEffect effect : raw.onConsumeEffects()) {
         if (effect instanceof ApplyStatusEffectsConsumeEffect statusEffects) {
            if (statusEffects.probability() >= 1.0F) {
               List<MobEffectInstance> modified = modifyEffects(statusEffects.effects(), quality);
               if (!modified.isEmpty()) {
                  builder.onConsume(new ApplyStatusEffectsConsumeEffect(modified, statusEffects.probability()));
               }
            }
         } else {
            builder.onConsume(effect);
         }
      }

      return builder.build();
   }
}
