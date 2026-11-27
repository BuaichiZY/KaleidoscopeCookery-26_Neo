package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ClayPotMilkTeaItem extends BlockItem implements IHasContainer {
   public ClayPotMilkTeaItem(Block block) {
      super(block, ModRegistrationProperties.blockItemProperties().stacksTo(16).component(DataComponents.CONSUMABLE,
         Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK)
            .sound(SoundEvents.HONEY_DRINK).hasConsumeParticles(false).build()));
   }

   @Override public int getUseDuration(ItemStack stack, LivingEntity entity) { return 32; }
   @Override public ItemUseAnimation getUseAnimation(ItemStack stack) { return ItemUseAnimation.DRINK; }
   @Override public InteractionResult use(Level level, Player player, InteractionHand hand) { return ItemUtils.startUsingInstantly(level, player, hand); }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (!level.isClientSide()) {
         for (MobEffectInstance effect : new ArrayList<>(entity.getActiveEffects())) {
            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) entity.removeEffect(effect.getEffect());
         }
      }
      return returnContainerToEntity(super.finishUsingItem(stack, level, entity), level, entity);
   }

   @Override public Item getContainerItem() { return Items.FLOWER_POT; }

   @Override
   public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                               Consumer<Component> tooltip, TooltipFlag flag) {
      tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.clay_pot_milk_tea.effect").withStyle(ChatFormatting.GRAY));
   }
}
