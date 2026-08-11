package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.network.message.SimpleC2SModMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent.Key;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public class FlatulenceEvent {
   @SubscribeEvent
   public static void onShiftKeyPressed(Key event) {
      KeyMapping keyShift = Minecraft.getInstance().options.keyShift;
      if (keyShift.matches(event.getKey(), event.getScanCode())) {
         if (!isInGame()) {
            return;
         }

         if (event.getAction() != 1) {
            return;
         }

         LocalPlayer player = Minecraft.getInstance().player;
         if (player == null || player.isShiftKeyDown()) {
            return;
         }

         if (!player.hasEffect(ModEffects.FLATULENCE)) {
            return;
         }

         keyShift.consumeClick();
         player.addDeltaMovement(new Vec3(0.0, 0.75, 0.0));
         PacketDistributor.sendToServer(new SimpleC2SModMessage(0), new CustomPacketPayload[0]);
      }
   }

   private static boolean isInGame() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.getOverlay() != null) {
         return false;
      } else if (mc.screen != null) {
         return false;
      } else {
         return !mc.mouseHandler.isMouseGrabbed() ? false : mc.isWindowActive();
      }
   }
}
