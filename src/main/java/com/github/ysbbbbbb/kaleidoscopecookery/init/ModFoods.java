package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public interface ModFoods {
   Map<FoodProperties, List<LegacyEffect>> LEGACY_EFFECTS = Collections.synchronizedMap(new IdentityHashMap<>());
   Set<FoodProperties> FAST_FOODS = Collections.newSetFromMap(new IdentityHashMap<>());

   record LegacyEffect(Supplier<MobEffectInstance> effectSupplier, float probability) {
      public MobEffectInstance effect() {
         return this.effectSupplier.get();
      }
   }

   /**
    * Keeps the legacy food declarations source-compatible while consumable
    * effects are moved to Minecraft 26's Consumable data component system.
    */
   class LegacyBuilder extends Builder {
      private final List<LegacyEffect> effects = new ArrayList<>();
      private boolean fast;

      @Override
      public LegacyBuilder nutrition(int nutrition) {
         super.nutrition(nutrition);
         return this;
      }

      @Override
      public LegacyBuilder saturationModifier(float saturationModifier) {
         super.saturationModifier(saturationModifier);
         return this;
      }

      @Override
      public LegacyBuilder alwaysEdible() {
         super.alwaysEdible();
         return this;
      }

      public LegacyBuilder effect(Supplier<MobEffectInstance> effect, float probability) {
         this.effects.add(new LegacyEffect(effect, probability));
         return this;
      }

      public LegacyBuilder fast() {
         this.fast = true;
         return this;
      }

      @Override
      public FoodProperties build() {
         FoodProperties properties = super.build();
         LEGACY_EFFECTS.put(properties, List.copyOf(this.effects));
         if (this.fast) {
            FAST_FOODS.add(properties);
         }
         return properties;
      }
   }

   static List<LegacyEffect> effectsFor(FoodProperties properties) {
      return LEGACY_EFFECTS.getOrDefault(properties, List.of());
   }

   static Consumable consumableFor(FoodProperties properties) {
      Consumable.Builder builder = Consumable.builder();
      if (FAST_FOODS.contains(properties)) {
         builder.consumeSeconds(0.8F);
      }

      for (LegacyEffect effect : effectsFor(properties)) {
         builder.onConsume(new ApplyStatusEffectsConsumeEffect(effect.effect(), effect.probability()));
      }
      return builder.build();
   }

   FoodProperties TOMATO = new LegacyBuilder().nutrition(2).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties CHILI = new LegacyBuilder().nutrition(1).saturationModifier(0.0F).alwaysEdible().build();
   FoodProperties LETTUCE = new LegacyBuilder().nutrition(2).saturationModifier(0.0F).alwaysEdible().build();
   FoodProperties CATERPILLAR = new LegacyBuilder()
      .nutrition(18)
      .saturationModifier(0.2F)
      .alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 200), 1.0F)
      .build();
   FoodProperties SASHIMI = new LegacyBuilder().nutrition(1).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties RAW_LAMB_CHOPS = new LegacyBuilder().nutrition(1).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties RAW_COW_OFFAL = new LegacyBuilder().nutrition(2).saturationModifier(0.3F).alwaysEdible().build();
   FoodProperties RAW_PORK_BELLY = new LegacyBuilder().nutrition(2).saturationModifier(0.3F).alwaysEdible().build();
   FoodProperties RAW_DONKEY_MEAT = new LegacyBuilder().nutrition(2).saturationModifier(0.3F).alwaysEdible().build();
   FoodProperties RAW_CUT_SMALL_MEATS = new LegacyBuilder().nutrition(2).saturationModifier(0.3F).alwaysEdible().build();
   FoodProperties RAW_MEATBALL = new LegacyBuilder().nutrition(4).saturationModifier(0.3F).alwaysEdible().build();
   FoodProperties COOKED_LAMB_CHOPS = new LegacyBuilder().nutrition(3).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COOKED_COW_OFFAL = new LegacyBuilder().nutrition(4).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COOKED_PORK_BELLY = new LegacyBuilder().nutrition(4).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COOKED_DONKEY_MEAT = new LegacyBuilder().nutrition(6).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COOKED_CUT_SMALL_MEATS = new LegacyBuilder().nutrition(4).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COOKED_MEATBALL = new LegacyBuilder().nutrition(8).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties DONKEY_BURGER = new LegacyBuilder()
      .nutrition(12)
      .saturationModifier(0.8F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 900), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BAOZI = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DUMPLING = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SAMSA = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 2000, 1), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties MANTOU = new LegacyBuilder().nutrition(6).saturationModifier(0.9F).fast().alwaysEdible().build();
   FoodProperties MEAT_PIE = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties QINGTUAN = new LegacyBuilder().nutrition(5).saturationModifier(0.6F).alwaysEdible().build();
   FoodProperties STICKY_CANDY = new LegacyBuilder().nutrition(6).saturationModifier(1.0F).alwaysEdible().build();
   FoodProperties STICKY_RICE_CAKE = new LegacyBuilder().nutrition(8).saturationModifier(0.875F).alwaysEdible().build();
   FoodProperties ZONGZI = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(0.625F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties RAW_BAMBOO_TUBE_RICE = new LegacyBuilder().nutrition(10).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties BAMBOO_TUBE_RICE = new LegacyBuilder().nutrition(10).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties BEEF_NOODLE = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.VITALITY, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties HUI_NOODLE = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.VITALITY, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties UDON_NOODLE = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.VITALITY, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties HOT_DRY_NOODLES = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.VITALITY, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties LABA_CONGEE = new LegacyBuilder()
      .nutrition(12)
      .saturationModifier(0.5F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FRIED_EGG = new LegacyBuilder().nutrition(4).saturationModifier(0.5F).alwaysEdible().build();
   FoodProperties DARK_CUISINE_BLOCK = new LegacyBuilder()
      .nutrition(1)
      .saturationModifier(0.0F)
      .alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 300), 0.33F)
      .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 0.33F)
      .build();
   FoodProperties DARK_CUISINE_ITEM = new LegacyBuilder()
      .nutrition(2)
      .saturationModifier(0.0F)
      .alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 300), 0.33F)
      .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 0.33F)
      .build();
   FoodProperties SUSPICIOUS_STIR_FRY_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.4F)
      .alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.SPEED, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.LUCK, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.MINING_FATIGUE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 400), 0.15F)
      .build();
   FoodProperties SUSPICIOUS_STIR_FRY_ITEM = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.4F)
      .alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.SPEED, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.LUCK, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.MINING_FATIGUE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 400), 0.15F)
      .build();
   FoodProperties SLIME_BALL_MEAL_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SLIME_BALL_MEAL_ITEM = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FONDANT_PIE_BLOCK = new LegacyBuilder()
      .nutrition(5)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FONDANT_PIE_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DONGPO_PORK_BLOCK = new LegacyBuilder()
      .nutrition(7)
      .saturationModifier(0.521F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DONGPO_PORK_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FONDANT_SPIDER_EYE_BLOCK = new LegacyBuilder()
      .nutrition(2)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FONDANT_SPIDER_EYE_ITEM = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CHORUS_FRIED_EGG_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CHORUS_FRIED_EGG_ITEM = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_FISH_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_FISH_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties GOLDEN_SALAD_BLOCK = new LegacyBuilder()
      .nutrition(5)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(MobEffects.RESISTANCE, 2000), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties GOLDEN_SALAD_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(MobEffects.RESISTANCE, 2000), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_CHICKEN_BLOCK = new LegacyBuilder()
      .nutrition(5)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_CHICKEN_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties YAKITORI_BLOCK = new LegacyBuilder()
      .nutrition(5)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties YAKITORI_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CRYSTAL_LAMB_CHOP_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.625F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 6000, 1), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CRYSTAL_LAMB_CHOP_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 6000, 1), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties NETHER_STYLE_SASHIMI_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties NETHER_STYLE_SASHIMI_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties PAN_SEARED_KNIGHT_STEAK_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.625F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1800), 1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties PAN_SEARED_KNIGHT_STEAK_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STARGAZY_PIE_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 600), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STARGAZY_PIE_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 600), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SWEET_AND_SOUR_ENDER_PEARLS_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SWEET_AND_SOUR_ENDER_PEARLS_ITEM = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 700), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BLAZE_LAMB_CHOP_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.625F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BLAZE_LAMB_CHOP_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FROST_LAMB_CHOP_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.625F)
      .effect(() -> new MobEffectInstance(ModEffects.TUNDRA_STRIDER, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FROST_LAMB_CHOP_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.TUNDRA_STRIDER, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties END_STYLE_SASHIMI_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties END_STYLE_SASHIMI_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DESERT_STYLE_SASHIMI_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DESERT_STYLE_SASHIMI_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties TUNDRA_STYLE_SASHIMI_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties TUNDRA_STYLE_SASHIMI_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COLD_STYLE_SASHIMI_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COLD_STYLE_SASHIMI_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.1875F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SHENGJIAN_MANTOU_ITEM = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SHENGJIAN_MANTOU_BLOCK = new LegacyBuilder()
      .nutrition(2)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SCRAMBLE_EGG_WITH_TOMATOES = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STIR_FRIED_BEEF_OFFAL = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_BEEF = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STIR_FRIED_PORK_WITH_PEPPERS = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SWEET_AND_SOUR_PORK = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FISH_FLAVORED_SHREDDED_PORK = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COUNTRY_STYLE_MIXED_VEGETABLES = new LegacyBuilder()
      .nutrition(9)
      .saturationModifier(0.611F)
      .effect(() -> new MobEffectInstance(ModEffects.PRESERVATION, 1800), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COOKED_RICE = new LegacyBuilder().nutrition(6).saturationModifier(1.0F).alwaysEdible().build();
   FoodProperties EGG_FRIED_RICE = new LegacyBuilder().nutrition(8).saturationModifier(0.875F).alwaysEdible().build();
   FoodProperties DELICIOUS_EGG_FRIED_RICE = new LegacyBuilder()
      .nutrition(12)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SUSPICIOUS_STIR_FRY_RICE_BOWL = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(0.4F)
      .effect(() -> new MobEffectInstance(MobEffects.SPEED, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.HASTE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.LUCK, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.MINING_FATIGUE, 1200), 0.15F)
      .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 400), 0.15F)
      .alwaysEdible()
      .build();
   FoodProperties SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STIR_FRIED_BEEF_OFFAL_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_BEEF_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SWEET_AND_SOUR_PORK_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_FISH_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_CHICKEN_RICE_BOWL = new LegacyBuilder()
      .nutrition(14)
      .saturationModifier(0.643F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties PORK_BONE_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.VIGOR, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SEAFOOD_MISO_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FEARSOME_THICK_SOUP = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.SULFUR, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties LAMB_AND_RADISH_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.TUNDRA_STRIDER, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_BEEF_WITH_POTATOES = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties WILD_MUSHROOM_RABBIT_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.SPEED, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties TOMATO_BEEF_BRISKET_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties PUFFERFISH_SOUP = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.0F)
      .effect(() -> new MobEffectInstance(ModEffects.MUSTARD, 12000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BORSCHT = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BEEF_MEATBALL_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CHICKEN_AND_MUSHROOM_STEW = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DONKEY_SOUP = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 9600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CANDIED_POTATO_BLOCK = new LegacyBuilder()
      .nutrition(5)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CANDIED_POTATO_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DOUGH_DROP_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(2)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties DOUGH_DROP_SOUP_ITEM = new LegacyBuilder()
      .nutrition(8)
      .saturationModifier(1.0F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STUFFED_TIGER_SKIN_PEPPER_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties STUFFED_TIGER_SKIN_PEPPER_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_RABBIT_HEAD_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_RABBIT_HEAD_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FOUR_JOY_MEATBALL_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FOUR_JOY_MEATBALL_SOUP_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties NUMBING_SPICY_CHICKEN_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties NUMBING_SPICY_CHICKEN_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FRIED_CATERPILLAR_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.8F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 200), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FRIED_CATERPILLAR_ITEM = new LegacyBuilder()
      .nutrition(12)
      .saturationModifier(0.8F)
      .effect(() -> new MobEffectInstance(ModEffects.FLATULENCE, 200), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FRIED_SPRING_ROLL_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties FRIED_SPRING_ROLL_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_BLOOD_STEW_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties SPICY_BLOOD_STEW_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BROWN_MUSHROOM_POT_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BROWN_MUSHROOM_POT_SOUP_ITEM = new LegacyBuilder()
      .nutrition(18)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties RED_MUSHROOM_POT_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties RED_MUSHROOM_POT_SOUP_ITEM = new LegacyBuilder()
      .nutrition(18)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 6000), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties WARPED_FUNGUS_POT_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties WARPED_FUNGUS_POT_SOUP_ITEM = new LegacyBuilder()
      .nutrition(18)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CRIMSON_FUNGUS_POT_SOUP_BLOCK = new LegacyBuilder()
      .nutrition(6)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties CRIMSON_FUNGUS_POT_SOUP_ITEM = new LegacyBuilder()
      .nutrition(18)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BUDDHA_JUMPS_OVER_THE_WALL_BLOCK = new LegacyBuilder()
      .nutrition(7)
      .saturationModifier(0.521F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BUDDHA_JUMPS_OVER_THE_WALL_ITEM = new LegacyBuilder()
      .nutrition(20)
      .saturationModifier(0.55F)
      .effect(() -> new MobEffectInstance(ModEffects.SATIATED_SHIELD, 3600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_PORK_RIBS_BLOCK = new LegacyBuilder()
      .nutrition(4)
      .saturationModifier(0.8F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties BRAISED_PORK_RIBS_ITEM = new LegacyBuilder()
      .nutrition(16)
      .saturationModifier(0.8F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COLD_ROASTED_MEAT_BLOCK = new LegacyBuilder().nutrition(4).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties COLD_ROASTED_MEAT_ITEM = new LegacyBuilder().nutrition(16).saturationModifier(0.8F).alwaysEdible().build();
   FoodProperties OIL_SPLASHED_FISH_BLOCK = new LegacyBuilder()
      .nutrition(3)
      .saturationModifier(0.667F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties OIL_SPLASHED_FISH_ITEM = new LegacyBuilder()
      .nutrition(13)
      .saturationModifier(0.615F)
      .effect(() -> new MobEffectInstance(ModEffects.WARMTH, 1600), 1.0F)
      .alwaysEdible()
      .build();
   FoodProperties COLD_CUT_HAM_SLICES_BLOCK = new LegacyBuilder().nutrition(4).saturationModifier(0.8F).alwaysEdible().build();
}
