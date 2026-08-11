package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.block.Block;

public class WithTooltipsBlockItem extends BlockItem {
   protected final String key;

   public WithTooltipsBlockItem(Block block, Properties properties, String name) {
      super(block, properties);
      this.key = "tooltip.kaleidoscope_cookery." + name;
   }

   public WithTooltipsBlockItem(Block block, String name) {
      this(block, ModRegistrationProperties.itemProperties(), name);
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable(this.key).withStyle(ChatFormatting.GRAY));
   }
}
