package com.github.ysbbbbbb.kaleidoscopecookery.compat.create;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import java.util.List;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public class CreateCompat {
   public static final String ID = "create";
   public static boolean IS_LOADED = false;

   public static void init() {
      ModList.get().getModContainerById("create").ifPresent(modContainer -> {
         IS_LOADED = true;
         NeoForge.EVENT_BUS.addListener(MillstoneCompat::afterMillstoneRecipeMatch);
      });
   }

   public static void getTransformRecipeForSearch(Level level, List<RecipeHolder<MillstoneRecipe>> recipes) {
      if (IS_LOADED) {
         MillstoneCompat.getTransformRecipeForSearch(level, recipes);
      }
   }
}
