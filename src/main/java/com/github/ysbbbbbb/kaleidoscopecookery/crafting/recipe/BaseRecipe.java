package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.List;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public interface BaseRecipe<C extends RecipeInput> extends Recipe<C> {
   int RECIPES_SIZE = 9;

   static Ingredient[] fillInputs(List<Ingredient> inputs) {
      return inputs.stream()
         .filter(ingredient -> !ModRecipes.isEmptyIngredient(ingredient))
         .limit(RECIPES_SIZE)
         .toArray(Ingredient[]::new);
   }

   static ItemStack displayStack(Ingredient ingredient) {
      return ingredient.items().findFirst().map(holder -> new ItemStack(holder.value())).orElse(ItemStack.EMPTY);
   }

   ItemStack getResultItem(Provider registryAccess);

   default ItemStack assemble(C container) {
      return this.getResultItem(RegistryAccess.EMPTY).copy();
   }

   default boolean isSpecial() {
      return true;
   }

   default boolean canCraftInDimensions(int width, int height) {
      return false;
   }

   default boolean showNotification() {
      return false;
   }

   default String group() {
      return "";
   }

   default PlacementInfo placementInfo() {
      return PlacementInfo.NOT_PLACEABLE;
   }

   default RecipeBookCategory recipeBookCategory() {
      return RecipeBookCategories.CRAFTING_MISC;
   }
}
