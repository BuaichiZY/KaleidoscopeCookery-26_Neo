package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeTrashCanOverlay implements GuiLayer {
   private static final Identifier ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "trash_can_overlay");
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/gui/trash_can_overlay.png"
   );

   @SubscribeEvent
   public static void register(RegisterGuiLayersEvent event) {
      event.registerAbove(VanillaGuiLayers.CROSSHAIR, ID, new RuntimeTrashCanOverlay());
   }

   @Override
   public void render(GuiGraphicsExtractor gui, DeltaTracker deltaTracker) {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer player = minecraft.player;
      if (player == null || player.isSpectator()) {
         return;
      }

      if (player.getVehicle() instanceof SitEntity sit && sit.getSitType() == SitEntity.TRASH_CAN) {
         if (minecraft.options.getCameraType().isFirstPerson()) {
            gui.blit(TEXTURE, 0, 0, gui.guiWidth(), gui.guiHeight(), 0.0F, 1.0F, 0.0F, 1.0F);
         }
         return;
      }

      if (!(minecraft.hitResult instanceof BlockHitResult hit)) {
         return;
      }

      BlockPos pos = hit.getBlockPos();
      if (!player.level().getBlockState(pos).is(ModBlocks.TRASH_CAN.get())
         || !(player.level().getBlockEntity(pos) instanceof TrashCanBlockEntity trashCan)) {
         return;
      }

      int x = gui.guiWidth() / 2 - 28;
      int y = gui.guiHeight() / 2 + 4;
      for (int slot = 0; slot < trashCan.getStorage().getSlots(); slot++) {
         ItemStack stack = trashCan.getStorage().getStackInSlot(slot);
         if (!stack.isEmpty()) {
            gui.fakeItem(stack, x, y);
            gui.itemDecorations(minecraft.font, stack, x, y);
            x += 20;
         }
      }
   }
}
