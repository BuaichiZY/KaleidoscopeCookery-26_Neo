package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Finish;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class PreservationEvent {
   @SubscribeEvent
   public static void onEatFood(Finish event) {
      ItemStack stack = event.getItem();
      LivingEntity entity = event.getEntity();
      if (stack.has(DataComponents.FOOD) && entity.hasEffect(ModEffects.PRESERVATION)) {
         FoodProperties foodProperties = stack.get(DataComponents.FOOD);
         if (foodProperties == null) {
            return;
         }

         for (ModFoods.LegacyEffect effectPair : ModFoods.effectsFor(foodProperties)) {
            Holder<MobEffect> effect = effectPair.effect().getEffect();
            if (((MobEffect)effect.value()).getCategory() == MobEffectCategory.HARMFUL) {
               entity.removeEffect(effect);
            }
         }
      }
   }
}
