package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Post;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class ArmorEffectEvent {
   @SubscribeEvent
   public static void onLivingTick(Post event) {
      if (event.getEntity() instanceof LivingEntity entity) {
         if (entity.tickCount % 20 == 0) {
            if (entity.isInWater()) {
               for (EquipmentSlot slot : EquipmentSlot.values()) {
                  if (slot.getType() == Type.HUMANOID_ARMOR && !entity.getItemBySlot(slot).is(TagMod.FARMER_ARMOR)) {
                     return;
                  }
               }

               entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 25, 0, true, true, false));
            }
         }
      }
   }
}
