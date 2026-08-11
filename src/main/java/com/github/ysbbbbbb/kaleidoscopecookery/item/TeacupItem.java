package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TeacupItem extends BlockItem implements IHasContainer {
   private final List<Pair<Supplier<MobEffectInstance>, Float>> effects;
   private final List<MobEffectInstance> showEffects = Lists.newArrayList();

   public TeacupItem(Block block, List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
      super(block, ModRegistrationProperties.itemProperties().stacksTo(16));
      this.effects = effects;
      this.effects.forEach(effect -> {
         if ((Float)effect.getSecond() >= 1.0F) {
            this.showEffects.add((MobEffectInstance)((Supplier)effect.getFirst()).get());
         }
      });
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      return player != null && !player.isSecondaryUseActive() ? InteractionResult.PASS : super.useOn(context);
   }

   public int getUseDuration(ItemStack stack, LivingEntity entity) {
      return 32;
   }

   public ItemUseAnimation getUseAnimation(ItemStack stack) {
      return ItemUseAnimation.DRINK;
   }

   public InteractionResult use(Level level, Player player, InteractionHand hand) {
      return ItemUtils.startUsingInstantly(level, player, hand);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (entity instanceof ServerPlayer serverPlayer) {
         CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
         serverPlayer.awardStat(Stats.ITEM_USED.get(this));
      }

      this.addTeaEffect(level, entity);
      if (entity instanceof Player player && !player.isCreative()) {
         stack.shrink(1);
      }

      return this.returnContainerToEntity(stack, level, entity);
   }

   protected void addTeaEffect(Level level, LivingEntity entity) {
      for (Pair<Supplier<MobEffectInstance>, Float> entry : this.effects) {
         MobEffectInstance instance = (MobEffectInstance)((Supplier)entry.getFirst()).get();
         float probability = (Float)entry.getSecond();
         if (!level.isClientSide() && level.getRandom().nextFloat() < probability) {
            entity.addEffect(instance);
         }
      }
   }

   @Override
   public Item getContainerItem() {
      return (Item)ModItems.EMPTY_CUP.get();
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
      Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
      String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
      MutableComponent full = Component.translatable(key).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC});
      String text = full.getString();

      for (String line : text.split("\n")) {
         if (!line.isEmpty()) {
            tooltip.add(Component.literal(line).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
         } else {
            tooltip.add(CommonComponents.EMPTY);
         }
      }

      if (!this.showEffects.isEmpty()) {
         tooltip.add(CommonComponents.space());
         PotionContents.addPotionTooltip(this.showEffects, tooltip::add, 1.0F, context.tickRate());
      }
   }
}
