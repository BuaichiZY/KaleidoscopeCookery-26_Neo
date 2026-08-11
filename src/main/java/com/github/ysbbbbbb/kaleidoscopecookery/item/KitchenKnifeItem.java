package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class KitchenKnifeItem extends Item {
   public KitchenKnifeItem(ToolMaterial material) {
      this(material, ModRegistrationProperties.itemProperties());
   }

   public KitchenKnifeItem(ToolMaterial material, Properties properties) {
      super(material.applySwordProperties(properties, 0.0F, -2.0F));
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.kitchen_knife").withStyle(ChatFormatting.GRAY));
   }

   public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
      return itemAbility == ItemAbility.get("sword_dig");
   }
}
