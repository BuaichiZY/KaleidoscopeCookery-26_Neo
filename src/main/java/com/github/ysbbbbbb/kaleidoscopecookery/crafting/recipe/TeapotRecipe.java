package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.TeapotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TeapotRecipe(Identifier teaFluid, Ingredient ingredient, int ingredientCount, int time, ItemStack result) implements BaseRecipe<TeapotInput> {
   public static final int OUTPUT_COUNT = 12;

   public boolean matches(TeapotInput container, Level level) {
      ItemStack stack = container.getItemStack();
      Identifier fluid = container.getTeaFluid();
      return this.teaFluid.equals(fluid) && this.ingredient.test(stack) && stack.getCount() >= this.ingredientCount;
   }

   public ItemStack getResultItem(Provider provider) {
      return this.result;
   }

   public ItemStack assemble(TeapotInput container, Provider registryAccess) {
      return this.getResultItem(registryAccess).copyWithCount(12);
   }

   public RecipeSerializer<TeapotRecipe> getSerializer() {
      return (RecipeSerializer<TeapotRecipe>)(RecipeSerializer<?>)ModRecipes.TEAPOT_SERIALIZER.get();
   }

   public RecipeType<TeapotRecipe> getType() {
      return ModRecipes.TEAPOT_RECIPE;
   }
}
