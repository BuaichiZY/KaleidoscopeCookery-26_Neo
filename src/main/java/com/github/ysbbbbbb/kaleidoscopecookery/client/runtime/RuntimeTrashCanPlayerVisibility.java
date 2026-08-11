package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeTrashCanPlayerVisibility {
   private RuntimeTrashCanPlayerVisibility() {
   }

   @SubscribeEvent
   public static void beforePlayerRender(RenderPlayerEvent.Pre<?> event) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         return;
      }
      Entity player = minecraft.level.getEntity(event.getRenderState().id);
      if (player != null && player.getVehicle() instanceof SitEntity seat && seat.getSitType() == SitEntity.TRASH_CAN) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void lockViewToHorizontal(ClientTickEvent.Post event) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null
         && minecraft.player.getVehicle() instanceof SitEntity seat
         && seat.getSitType() == SitEntity.TRASH_CAN
         && minecraft.options.getCameraType().isFirstPerson()) {
         minecraft.player.setXRot(0.0F);
         minecraft.player.xRotO = 0.0F;
      }
   }

   @SubscribeEvent
   public static void blockInteractionsInsideTrashCan(InputEvent.InteractionKeyMappingTriggered event) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null
         && minecraft.player.getVehicle() instanceof SitEntity seat
         && seat.getSitType() == SitEntity.TRASH_CAN
         && (event.isAttack() || event.isUseItem() || event.isPickBlock())) {
         event.setSwingHand(false);
         event.setCanceled(true);
      }
   }
}
