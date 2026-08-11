package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent.ComputeCameraAngles;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public class CameraEvent {
   @SubscribeEvent
   public static void onCameraTick(ComputeCameraAngles event) {
      CameraType cameraType = Minecraft.getInstance().options.getCameraType();
      if (cameraType.isFirstPerson()) {
         LocalPlayer player = Minecraft.getInstance().player;
         if (player != null) {
            if (player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == 1) {
               event.setPitch(0.0F);
            }
         }
      }
   }
}
