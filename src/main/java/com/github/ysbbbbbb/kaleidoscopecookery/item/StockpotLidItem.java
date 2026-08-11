package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;

public class StockpotLidItem extends ShieldItem {
   public static final Identifier USING_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "using");
   private static final int NORMAL = 0;
   private static final int USING = 1;

   public StockpotLidItem() {
      super(ModRegistrationProperties.itemProperties().durability(120));
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot_lid").withStyle(ChatFormatting.GRAY));
   }
}
