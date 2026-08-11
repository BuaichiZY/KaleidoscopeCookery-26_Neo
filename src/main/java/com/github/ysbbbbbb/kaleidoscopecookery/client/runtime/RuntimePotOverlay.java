package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimePotOverlay implements GuiLayer {
   private static final Identifier ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "runtime_pot_overlay");

   @SubscribeEvent
   public static void register(RegisterGuiLayersEvent event) {
      event.registerAbove(VanillaGuiLayers.CROSSHAIR, ID, new RuntimePotOverlay());
   }

   @Override
   public void render(GuiGraphicsExtractor gui, DeltaTracker deltaTracker) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null || minecraft.player.isSpectator()
         || !(minecraft.hitResult instanceof BlockHitResult hit)) {
         return;
      }

      BlockPos pos = hit.getBlockPos();
      BlockState state = minecraft.player.level().getBlockState(pos);
      if (!state.is(ModBlocks.POT.get())
         || !(minecraft.player.level().getBlockEntity(pos) instanceof PotBlockEntity pot)) {
         return;
      }

      // Heat-source tags and block-state updates can arrive a frame later than the
      // block-entity packet.  Use either synchronized state as the activity signal
      // so the original status prompt is not suppressed on the client.
      if (!state.getValue(PotBlock.HAS_OIL) && pot.getCurrentTick() <= 0 && pot.getStatus() == 0) {
         return;
      }

      String key;
      int color;
      switch (pot.getStatus()) {
         case 0 -> {
            key = "tip.kaleidoscope_cookery.pot.add_ingredient";
            color = 0xFFFFFF;
         }
         case 1 -> {
            key = "tip.kaleidoscope_cookery.pot.need_stir_fry";
            color = 0xFFFFFF;
         }
         case 2 -> {
            key = "tip.kaleidoscope_cookery.pot.done";
            color = 0xFF5555;
         }
         default -> {
            return;
         }
      }

      int y = gui.guiHeight() - 72;
      gui.centeredText(minecraft.font, Component.translatable(key), gui.guiWidth() / 2, y, color);
   }
}
