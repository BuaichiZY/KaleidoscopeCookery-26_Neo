package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StockpotLidItem extends ShieldItem {
   public static final Identifier USING_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "using");
   private static final int NORMAL = 0;
   private static final int USING = 1;

   public StockpotLidItem() {
      super(ModRegistrationProperties.itemProperties().durability(120));
   }

   @OnlyIn(Dist.CLIENT)
   public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
      return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.stockpot_lid").withStyle(ChatFormatting.GRAY));
   }
}
