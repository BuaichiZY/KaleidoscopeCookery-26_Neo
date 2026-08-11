package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class ChangeTargetEvent {
   @SubscribeEvent
   public static void onTarget(LivingChangeTargetEvent event) {
      if (event.getNewAboutToBeSetTarget() instanceof Player player && player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == 1) {
         event.setCanceled(true);
      }
   }
}
