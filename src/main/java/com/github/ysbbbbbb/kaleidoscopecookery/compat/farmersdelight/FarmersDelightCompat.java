package com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import java.util.List;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public class FarmersDelightCompat {
   public static final String ID = "farmersdelight";
   public static boolean IS_LOADED = false;

   public static void init() {
      ModList.get().getModContainerById("farmersdelight").ifPresent(modContainer -> {
         IS_LOADED = true;
         NeoForge.EVENT_BUS.addListener(CookingPotCompat::afterStockpotRecipeMatch);
      });
   }

   public static void getTransformRecipeForJei(Level level, List<RecipeHolder<StockpotRecipe>> recipes) {
      if (IS_LOADED) {
         CookingPotCompat.getTransformRecipeForJei(level, recipes);
      }
   }
}
