package com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class ClientItemContainerTooltip implements ClientTooltipComponent {
   private final NonNullList<ItemStack> items = NonNullList.create();
   @Nullable
   private MutableComponent emptyTip = null;

   public ClientItemContainerTooltip(ItemContainerTooltip containerTooltip) {
      IItemHandler handler = containerTooltip.handler();

      for (int i = 0; i < handler.getSlots(); i++) {
         ItemStack stack = handler.getStackInSlot(i);
         if (!stack.isEmpty()) {
            this.items.add(stack);
         }
      }

      if (this.items.isEmpty()) {
         this.emptyTip = Component.translatable("tooltip.kaleidoscope_cookery.item_container.empty");
      }
   }

   public int getHeight() {
      if (this.emptyTip != null) {
         return 10;
      } else {
         int row = (this.items.size() - 1) / 8 + 1;
         return 20 * row;
      }
   }

   public int getWidth(Font font) {
      if (this.emptyTip != null) {
         return font.width(this.emptyTip);
      } else {
         int maxInRow = Math.min(this.items.size(), 8);
         return maxInRow * 20;
      }
   }

   public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
      if (this.emptyTip != null) {
         guiGraphics.drawString(font, this.emptyTip, pX, pY, ChatFormatting.GRAY.getColor());
      } else {
         int i = 0;

         for (ItemStack stack : this.items) {
            int xOffset = pX + i % 8 * 20;
            int yOffset = pY + i / 8 * 20;
            guiGraphics.renderFakeItem(stack, xOffset, yOffset);
            guiGraphics.renderItemDecorations(font, stack, xOffset, yOffset);
            i++;
         }
      }
   }
}
