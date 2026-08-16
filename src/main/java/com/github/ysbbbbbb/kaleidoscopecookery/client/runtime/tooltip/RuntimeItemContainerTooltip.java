package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.tooltip;

import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/** Prevents the same unknown-component crash for item-backed containers. */
public final class RuntimeItemContainerTooltip implements ClientTooltipComponent {
   private static final int TOOLTIP_TEXT_COLOR = 0xFFAAAAAA;

   private final List<ItemStack> items = new ArrayList<>();
   private final Component emptyTip;

   public RuntimeItemContainerTooltip(ItemContainerTooltip tooltip) {
      IItemHandler handler = tooltip.handler();
      for (int slot = 0; slot < handler.getSlots(); slot++) {
         ItemStack stack = handler.getStackInSlot(slot);
         if (!stack.isEmpty()) {
            this.items.add(stack.copy());
         }
      }
      this.emptyTip = this.items.isEmpty()
         ? Component.translatable("tooltip.kaleidoscope_cookery.item_container.empty")
         : null;
   }

   @Override
   public int getHeight(Font font) {
      return this.emptyTip != null ? 10 : 20 * ((this.items.size() - 1) / 8 + 1);
   }

   @Override
   public int getWidth(Font font) {
      return this.emptyTip != null ? font.width(this.emptyTip) : Math.min(this.items.size(), 8) * 20;
   }

   @Override
   public void extractImage(
      Font font,
      int x,
      int y,
      int tooltipWidth,
      int tooltipHeight,
      GuiGraphicsExtractor graphics
   ) {
      if (this.emptyTip != null) {
         graphics.text(font, this.emptyTip, x, y, TOOLTIP_TEXT_COLOR);
         return;
      }
      for (int index = 0; index < this.items.size(); index++) {
         int itemX = x + index % 8 * 20;
         int itemY = y + index / 8 * 20;
         ItemStack stack = this.items.get(index);
         graphics.fakeItem(stack, itemX, itemY);
         graphics.itemDecorations(font, stack, itemX, itemY);
      }
   }
}
