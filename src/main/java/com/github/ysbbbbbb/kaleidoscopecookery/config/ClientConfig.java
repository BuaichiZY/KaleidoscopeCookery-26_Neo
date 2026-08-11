package com.github.ysbbbbbb.kaleidoscopecookery.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class ClientConfig {
   public static BooleanValue SHOW_FOOD_EFFECT_TOOLTIPS;

   public static ModConfigSpec init() {
      Builder builder = new Builder();
      client(builder);
      return builder.build();
   }

   private static void client(Builder builder) {
      builder.push("cookery");
      builder.comment("Whether to show food effect tooltips when hovering over food items.");
      SHOW_FOOD_EFFECT_TOOLTIPS = builder.define("ShowFoodEffectTooltips", true);
      builder.pop();
   }
}
