package com.github.ysbbbbbb.kaleidoscopecookery.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;

public class WithTooltipsItem extends Item {
   private final String key;

   public WithTooltipsItem(Properties properties, String name) {
      super(properties);
      this.key = "tooltip.kaleidoscope_cookery." + name;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable(this.key).withStyle(ChatFormatting.GRAY));
   }
}
