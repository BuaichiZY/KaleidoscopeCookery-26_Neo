package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei;

import java.util.List;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeRecipeCache {
   private static volatile RecipeMap recipes = RecipeMap.EMPTY;

   private RuntimeRecipeCache() {
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRecipesReceived(RecipesReceivedEvent event) {
      recipes = event.getRecipeMap();
   }

   public static <I extends net.minecraft.world.item.crafting.RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipes(RecipeType<T> type) {
      return List.copyOf(recipes.byType(type));
   }
}
