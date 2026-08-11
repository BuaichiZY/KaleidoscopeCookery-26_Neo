package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class OilPotItem extends BlockItem {
   public static final Identifier HAS_OIL_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_oil");
   private static final String OIL_COUNT = "oil_count";
   private static final int NO_OIL = 0;
   private static final int HAS_OIL = 1;

   public OilPotItem() {
      super((Block)ModBlocks.OIL_POT.get(), ModRegistrationProperties.itemProperties().stacksTo(1));
   }

   public static void setOilCount(ItemStack stack, int count) {
      count = Mth.clamp(count, 0, 256);
      stack.set(ModDataComponents.OIL_POT_OIL_COUNT, count);
   }

   public static int getOilCount(ItemStack stack) {
      return (Integer)stack.getOrDefault(ModDataComponents.OIL_POT_OIL_COUNT, 0);
   }

   public static boolean hasOil(ItemStack stack) {
      return getOilCount(stack) > 0;
   }

   public static void shrinkOilCount(ItemStack stack) {
      int currentCount = getOilCount(stack);
      if (currentCount > 0) {
         setOilCount(stack, currentCount - 1);
      }
   }

   public static ItemStack getFullOilPot() {
      ItemStack stack = new ItemStack((ItemLike)ModBlocks.OIL_POT.get());
      setOilCount(stack, 256);
      return stack;
   }

   public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      int oilCount = getOilCount(pStack);
      if (oilCount > 0) {
         pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.count", new Object[]{oilCount}).withStyle(ChatFormatting.GRAY));
      } else {
         pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.oil_pot.empty").withStyle(ChatFormatting.GRAY));
      }
   }
}
