package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import org.jetbrains.annotations.Nullable;

public class StrawHatItem extends Item {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/models/armor/straw_hat.png");
   private static final Identifier TEXTURE_FLOWER = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/models/armor/straw_hat_flower.png"
   );
   private final boolean hasFlower;

   public StrawHatItem(boolean hasFlower) {
      super(ModRegistrationProperties.itemProperties().stacksTo(1).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
      this.hasFlower = hasFlower;
   }

   public boolean hasFlower() {
      return this.hasFlower;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.straw_hat").withStyle(ChatFormatting.GRAY));
   }
}
