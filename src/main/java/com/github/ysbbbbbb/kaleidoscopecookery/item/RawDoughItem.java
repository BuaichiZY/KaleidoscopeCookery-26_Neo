package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class RawDoughItem extends Item {
   public static final Identifier PULL_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pull");
   private static final int MIN_USE_DURATION = 30;

   public RawDoughItem() {
      super(ModRegistrationProperties.itemProperties());
   }

   public int getUseDuration(ItemStack stack, LivingEntity entity) {
      return 72000;
   }

   public ItemUseAnimation getUseAnimation(ItemStack pStack) {
      return ItemUseAnimation.BOW;
   }

   public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack stack = playerIn.getItemInHand(handIn);
      playerIn.startUsingItem(handIn);
      return InteractionResult.CONSUME;
   }

   public boolean releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
      int time = stack.getUseDuration(entityLiving) - timeLeft;
      if (time >= 30) {
         int count = stack.getCount();
         ItemStack noodles = new ItemStack((ItemLike)ModItems.RAW_NOODLES.get(), count);
         stack.setCount(0);
         ItemUtils.getItemToLivingEntity(entityLiving, noodles);
         if (worldIn.isClientSide()) {
            entityLiving.playSound((SoundEvent)ModSounds.ITEM_DOUGH_TRANSFORM.get(), 1.0F, 1.0F);
         }

         if (entityLiving instanceof ServerPlayer serverPlayer) {
            ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(serverPlayer, "pull_the_dough");
         }

         return true;
      }

      return false;
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.raw_dough").withStyle(ChatFormatting.GRAY));
   }
}
