package com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TrashCanOverlay implements Layer {
   private static final Identifier TRASH_CAN_OVERLAY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/trash_can_overlay.png");

   public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
         LocalPlayer player = minecraft.player;
         if (player != null) {
            if (!(player.getVehicle() instanceof SitEntity sitEntity && sitEntity.getSitType() == 1)) {
               this.renderTrashCanTip(guiGraphics, guiGraphics.guiWidth(), guiGraphics.guiHeight(), minecraft, player);
            } else if (minecraft.options.getCameraType().isFirstPerson()) {
               this.renderTextureOverlay(guiGraphics, TRASH_CAN_OVERLAY, 1.0F);
            }
         }
      }
   }

   private void renderTrashCanTip(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Minecraft minecraft, LocalPlayer player) {
      if (minecraft.hitResult instanceof BlockHitResult result) {
         Level level = player.level();
         BlockPos blockPos = result.getBlockPos();
         BlockState blockState = player.level().getBlockState(blockPos);
         if (blockState.is((Block)ModBlocks.TRASH_CAN.get())) {
            if (level.getBlockEntity(blockPos) instanceof TrashCanBlockEntity trashCan) {
               Font font = Minecraft.getInstance().font;
               int x = screenWidth / 2 - 28;
               int y = screenHeight / 2 + 4;
               ItemStackHandler storage = trashCan.getStorage();

               for (int i = 0; i < storage.getSlots(); i++) {
                  ItemStack stack = storage.getStackInSlot(i);
                  if (!stack.isEmpty()) {
                     guiGraphics.renderFakeItem(stack, x, y);
                     guiGraphics.renderItemDecorations(font, stack, x, y);
                     x += 20;
                  }
               }
            }
         }
      }
   }

   private void renderTextureOverlay(GuiGraphics guiGraphics, Identifier shaderLocation, float alpha) {
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.enableBlend();
      guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
      guiGraphics.blit(shaderLocation, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
