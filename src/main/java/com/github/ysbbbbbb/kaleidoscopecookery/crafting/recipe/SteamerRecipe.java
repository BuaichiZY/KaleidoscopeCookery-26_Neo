package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.level.Level;

public class SteamerRecipe implements BaseRecipe<SingleRecipeInput> {
   private final Ingredient ingredient;
   private final ItemStack result;
   private final int cookTick;

   public SteamerRecipe(Ingredient ingredient, ItemStack result, int cookTick) {
      this.ingredient = ingredient;
      this.result = result;
      this.cookTick = Math.max(cookTick, 1);
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
   public RecipeSerializer<SteamerRecipe> getSerializer() {
      return (RecipeSerializer<SteamerRecipe>)(RecipeSerializer<?>)ModRecipes.STEAMER_SERIALIZER.get();
   }

   public net.minecraft.world.item.crafting.RecipeType<SteamerRecipe> getType() {
      return ModRecipes.STEAMER_RECIPE;
   }

   public int getCookTick() {
      return this.cookTick;
   }
}
