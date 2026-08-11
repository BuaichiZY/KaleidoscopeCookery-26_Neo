package com.github.ysbbbbbb.kaleidoscopecookery.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.block.Block;

public class LiftBlockItem extends WithTooltipsBlockItem {
   public LiftBlockItem(Block block, Properties properties, String name) {
      super(block, properties, name);
   }

   public LiftBlockItem(Block block, String name) {
      super(block, name);
   }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable(this.key).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
   }
}
