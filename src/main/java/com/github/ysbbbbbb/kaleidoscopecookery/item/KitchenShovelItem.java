package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class KitchenShovelItem extends ShovelItem {
   public static final Identifier HAS_OIL_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_oil");
   private static final int NO_OIL = 0;
   private static final int HAS_OIL = 1;

   public KitchenShovelItem() {
      super(ToolMaterial.IRON, -1.0F, -2.0F, ModRegistrationProperties.itemProperties());
   }

   public static void setHasOil(ItemStack stack, boolean hasOil) {
      stack.set(ModDataComponents.KITCHEN_SHOVEL_HAS_OIL, hasOil);
   }

   public static boolean hasOil(ItemStack stack) {
      return stack.has(ModDataComponents.KITCHEN_SHOVEL_HAS_OIL) ? Boolean.TRUE.equals(stack.get(ModDataComponents.KITCHEN_SHOVEL_HAS_OIL)) : false;
   }

   @OnlyIn(Dist.CLIENT)
   public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
      return hasOil(stack) ? 1.0F : 0.0F;
   }

   public InteractionResult useOn(UseOnContext context) {
      InteractionResult result = super.useOn(context);
      if (result.consumesAction() && hasOil(context.getItemInHand())) {
         setHasOil(context.getItemInHand(), false);
      }

      return result;
   }

   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
      BlockPos clickedPos = context.getClickedPos();
      Level level = context.getLevel();
      Player player = context.getPlayer();
      if (level.getBlockEntity(clickedPos) instanceof PotBlockEntity potBlockEntity
         && player != null
         && player.isSecondaryUseActive()
         && potBlockEntity.getStatus() == 2
         && !potBlockEntity.hasCarrier()) {
         potBlockEntity.takeOutProduct(level, player, stack);
         return InteractionResult.SUCCESS;
      } else {
         return super.onItemUseFirst(stack, context);
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.kitchen_shovel").withStyle(ChatFormatting.GRAY));
   }
}
