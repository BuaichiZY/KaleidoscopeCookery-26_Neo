package com.github.ysbbbbbb.kaleidoscopecookery.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.block.Block;

public class PlateBlockItem extends WithTooltipsBlockItem {
   public PlateBlockItem(Block block, Properties properties, String name) {
      super(block, properties, name);
   }

   public PlateBlockItem(Block block, String name) {
      super(block, name);
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      MutableComponent full = Component.translatable(this.key).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC});
      String text = full.getString();

      for (String line : text.split("\n")) {
         if (!line.isEmpty()) {
            tooltip.add(Component.literal(line).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
         }
      }
   }
}
