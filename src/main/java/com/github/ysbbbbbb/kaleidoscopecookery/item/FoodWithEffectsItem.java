package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

public class FoodWithEffectsItem extends Item {
   private final List<MobEffectInstance> effectInstances = Lists.newArrayList();
   private final Function<Quality, List<MobEffectInstance>> effectCache = Util.memoize(quality -> QualityUtils.modifyEffects(this.effectInstances, quality));
   private final BiFunction<Quality, FoodProperties, FoodProperties> foodPropertiesCache = Util.memoize(
      (quality, raw) -> QualityUtils.modifyFoodProperties(raw, quality)
   );

   public FoodWithEffectsItem(FoodProperties properties) {
      super(ModRegistrationProperties.itemProperties().food(properties, ModFoods.consumableFor(properties)));
      ModFoods.effectsFor(properties).forEach(effect -> {
         if (effect.probability() >= 1.0F) {
            this.effectInstances.add(effect.effect());
         }
      });
   }

   public FoodWithEffectsItem(FoodProperties properties, Item craftingItem) {
      super(ModRegistrationProperties.itemProperties().food(properties, ModFoods.consumableFor(properties)).craftRemainder(craftingItem).usingConvertsTo(craftingItem));
      ModFoods.effectsFor(properties).forEach(effect -> {
         if (effect.probability() >= 1.0F) {
            this.effectInstances.add(effect.effect());
         }
      });
   }

   @Nullable
   public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
      FoodProperties raw = stack.get(DataComponents.FOOD);
      if (QualityUtils.hasQuality(stack) && raw != null) {
         Quality quality = QualityUtils.getQuality(stack);
         return this.foodPropertiesCache.apply(quality, raw);
      } else {
         return raw;
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
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

      boolean showEffect = !this.effectInstances.isEmpty()
         && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS
         && (Boolean)ClientConfig.SHOW_FOOD_EFFECT_TOOLTIPS.get();
      if (QualityUtils.hasQuality(stack)) {
         Quality quality = QualityUtils.getQuality(stack);
         tooltip.add(quality.getTooltip());
         if (showEffect) {
            tooltip.add(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectCache.apply(quality), tooltip::add, 1.0F, context.tickRate());
         }
      } else {
         tooltip.add(CommonComponents.space());
         PotionContents.addPotionTooltip(this.effectInstances, tooltip::add, 1.0F, context.tickRate());
      }
   }
}
