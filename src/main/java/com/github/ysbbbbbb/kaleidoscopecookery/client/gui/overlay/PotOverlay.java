package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public class PotOverlay implements Layer {
   public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
         if (minecraft.hitResult instanceof BlockHitResult result) {
            if (result.getType() == Type.BLOCK) {
               LocalPlayer player = minecraft.player;
               if (player != null) {
                  Level level = player.level();
                  BlockPos blockPos = result.getBlockPos();
                  BlockState blockState = player.level().getBlockState(blockPos);
                  if (blockState.is((Block)ModBlocks.POT.get())) {
                     if (level.getBlockEntity(blockPos) instanceof PotBlockEntity pot) {
                        Font font = Minecraft.getInstance().font;
                        int x = guiGraphics.guiWidth() / 2;
                        int y = guiGraphics.guiHeight() - 72;
                        if (minecraft.gui.overlayMessageTime > 0) {
                           y -= 12;
                        }

                        if ((Boolean)blockState.getValue(PotBlock.HAS_OIL) && pot.hasHeatSource(level)) {
                           int status = pot.getStatus();
                           if (status == 0) {
                              drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.add_ingredient"), x, y, 16777215);
                              return;
                           }

                           if (status == 1) {
                              drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.need_stir_fry"), x, y, 16777215);
                              return;
                           }

                           if (status == 2) {
                              drawWordWrap(guiGraphics, font, Component.translatable("tip.kaleidoscope_cookery.pot.done"), x, y, ChatFormatting.RED.getColor());
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void drawWordWrap(GuiGraphics graphics, Font font, MutableComponent text, int pX, int pY, int color) {
      for (FormattedCharSequence sequence : font.split(text, 100)) {
         graphics.drawString(font, sequence, pX - font.width(sequence) / 2, pY, color);
         pY += 9;
      }
   }
}
