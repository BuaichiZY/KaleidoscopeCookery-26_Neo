package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class GeneralConfig {
   public static BooleanValue SATIATED_SHIELD_ABSORB_ENABLED;
   public static BooleanValue SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE;
   public static BooleanValue IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT;
   public static IntValue SATIATED_SHIELD_MIN_FOOD_LEVEL;
   public static DoubleValue SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE;
   public static DoubleValue SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT;
   public static DoubleValue SATIATED_SHIELD_MAX_DAMAGE_REDUCTION;
   public static DoubleValue SATIATED_SHIELD_MIN_DAMAGE;
   public static DoubleValue SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER;

   public static ModConfigSpec init() {
      Builder builder = new Builder();
      general(builder);
      return builder.build();
   }

   private static void general(Builder builder) {
      builder.push("cookery");
      builder.comment("Whether enabling the Satiated Shield effect.");
      SATIATED_SHIELD_ABSORB_ENABLED = builder.define("SatiatedShieldAbsorbEnabled", true);
      builder.comment("Whether the Satiated Shield effect should absorb excess damage beyond its capacity.");
      SATIATED_SHIELD_ABSORB_EXCESS_DAMAGE = builder.define("SatiatedShieldAbsorbExcessDamage", true);
      builder.comment("If true, the Satiated Shield effect will not apply while the player has the Hunger effect.");
      IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT = builder.define("IS_SATIATED_SHIELD_DISABLE_WHEN_HUNGRY_EFFECT", true);
      builder.comment("Minimum Hunger Value required for the Satiated Shield to apply (int).");
      SATIATED_SHIELD_MIN_FOOD_LEVEL = builder.defineInRange("SATIATED_SHIELD_MIN_FOOD_LEVEL", 4, 1, 20);
      builder.comment("The exhaustion added each time the player takes damage.");
      SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE = builder.defineInRange("SATIATED_SHIELD_ADDITIONAL_EXHAUSTION_PER_DAMAGE", 2.0, 0.0, 40.0);
      builder.comment("The damage reduction percentage of the Satiated Shield effect.");
      SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT = builder.defineInRange("SATIATED_SHIELD_DAMAGE_REDUCTION_PERCENT", 1.0, 0.0, 1.0);
      builder.comment("The maximum damage reduction amount of the Satiated Shield effect.");
      SATIATED_SHIELD_MAX_DAMAGE_REDUCTION = builder.defineInRange("SATIATED_SHIELD_MAX_DAMAGE_REDUCTION", 64.0, 0.0, 2.147483647E9);
      builder.comment("The minimum damage that can be got in the Satiated Shield effect.");
      SATIATED_SHIELD_MIN_DAMAGE = builder.defineInRange("SATIATED_SHIELD_MIN_DAMAGE", 0.0, 0.0, 2.147483647E9);
      builder.comment("The multiplier for the exhaustion added per point of Satiated Shield Weakness Damage.");
      SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER = builder.defineInRange("SATIATED_SHIELD_WEAKNESS_DAMAGE_MULTIPLIER", 2.0, 1.0, 2.147483647E9);
      builder.pop();
   }
}
