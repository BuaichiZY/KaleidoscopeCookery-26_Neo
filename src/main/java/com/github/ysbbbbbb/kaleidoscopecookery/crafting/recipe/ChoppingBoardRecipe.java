package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.level.Level;

public class ChoppingBoardRecipe implements BaseRecipe<SingleRecipeInput> {
   private final Ingredient ingredient;
   private final ItemStack result;
   private final int cutCount;
   private final Identifier modelId;

   public ChoppingBoardRecipe(Ingredient ingredient, ItemStack result, int cutCount, Identifier modelId) {
      this.ingredient = ingredient;
      this.result = result;
      this.cutCount = Math.max(cutCount, 1);
      this.modelId = modelId;
   }

   public boolean matches(SingleRecipeInput inv, Level level) {
      return this.ingredient.test(inv.getItem(0));
   }

   public boolean isSpecial() {
      return true;
   }

   public Ingredient getIngredient() {
      return this.ingredient;
   }

   public ItemStack getResult() {
      return this.result;
   }

   public ItemStack getResultItem(Provider registries) {
      return this.result;
   }

   @SuppressWarnings("unchecked")
   public RecipeSerializer<ChoppingBoardRecipe> getSerializer() {
      return (RecipeSerializer<ChoppingBoardRecipe>)(RecipeSerializer<?>)ModRecipes.CHOPPING_BOARD_SERIALIZER.get();
   }

   public net.minecraft.world.item.crafting.RecipeType<ChoppingBoardRecipe> getType() {
      return ModRecipes.CHOPPING_BOARD_RECIPE;
   }

   public int getCutCount() {
      return this.cutCount;
   }

   public Identifier getModelId() {
      return this.modelId;
   }
}
