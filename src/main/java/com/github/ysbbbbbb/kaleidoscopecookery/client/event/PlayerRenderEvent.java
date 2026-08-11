package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent.Pre;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public class PlayerRenderEvent {
   @SubscribeEvent
   public static void onPlayerRender(Pre event) {
      Player player = event.getEntity();
      if (player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == 1) {
         event.setCanceled(true);
      }
   }
}
