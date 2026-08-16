package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.mixin.GuiAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
         || !(minecraft.hitResult instanceof BlockHitResult hit)
         || hit.getType() != HitResult.Type.BLOCK) {
         return;
      }

      BlockPos pos = hit.getBlockPos();
      BlockState state = minecraft.player.level().getBlockState(pos);
      if (!state.is(ModBlocks.POT.get())
         || !(minecraft.player.level().getBlockEntity(pos) instanceof PotBlockEntity pot)) {
         return;
      }

      // Match the 1.4.1 overlay: the operation prompt is shown only while the
      // wok contains oil and is sitting on an active heat source.
      if (!state.getValue(PotBlock.HAS_OIL) || !pot.hasHeatSource(minecraft.player.level())) {
         return;
      }

      MutableComponent message;
      int color;
      switch (pot.getStatus()) {
         case 0 -> {
            message = Component.translatable("tip.kaleidoscope_cookery.pot.add_ingredient");
            color = 0xFFFFFFFF;
         }
         case 1 -> {
            message = Component.translatable("tip.kaleidoscope_cookery.pot.need_stir_fry");
            color = 0xFFFFFFFF;
         }
         case 2 -> {
            message = Component.translatable("tip.kaleidoscope_cookery.pot.done");
            color = 0xFFFF5555;
         }
         default -> {
            return;
         }
      }

      int y = gui.guiHeight() - 72;
      if (((GuiAccessor)minecraft.gui).kaleidoscopeCookery$getOverlayMessageTime() > 0) {
         y -= 12;
      }

      drawWordWrap(gui, minecraft.font, message, gui.guiWidth() / 2, y, color);
   }

   private static void drawWordWrap(GuiGraphicsExtractor gui, Font font, MutableComponent text, int centerX, int y, int color) {
      for (FormattedCharSequence line : font.split(text, 100)) {
         gui.text(font, line, centerX - font.width(line) / 2, y, color);
         y += 9;
      }
   }
}
