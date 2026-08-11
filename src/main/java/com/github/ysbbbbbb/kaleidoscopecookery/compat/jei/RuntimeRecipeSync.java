package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public final class RuntimeRecipeSync {
   private RuntimeRecipeSync() {
   }

   @SubscribeEvent
   public static void onDatapackSync(OnDatapackSyncEvent event) {
      event.sendRecipes(
         ModRecipes.POT_RECIPE,
         ModRecipes.FLEX_POT_RECIPE,
         ModRecipes.CHOPPING_BOARD_RECIPE,
         ModRecipes.STOCKPOT_RECIPE,
         ModRecipes.FLEX_STOCKPOT_RECIPE,
         ModRecipes.MILLSTONE_RECIPE,
         ModRecipes.STEAMER_RECIPE,
         ModRecipes.TEAPOT_RECIPE
      );
   }
}
