package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class TeapotBuilder implements RecipeBuilder {
   private static final String NAME = "teapot";
   private Identifier teaFluid = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
   private Ingredient ingredient = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private int ingredientCount = 12;
   private int time = 2400;
   private ItemStack result = ItemStack.EMPTY;

   public static TeapotBuilder builder() {
      return new TeapotBuilder();
   }

   public TeapotBuilder setTeaFluid(Identifier baseTeaFluid) {
      this.teaFluid = baseTeaFluid;
      return this;
   }

   public TeapotBuilder setTeaFluid(Fluid fluid) {
      this.teaFluid = BuiltInRegistries.FLUID.getKey(fluid);
      return this;
   }

   public TeapotBuilder setIngredient(Object ingredient) {
      if (ingredient instanceof ItemLike itemLike) {
         this.ingredient = Ingredient.of(new ItemLike[]{itemLike});
      } else if (ingredient instanceof ItemStack stack) {
         this.ingredient = Ingredient.of(new ItemStack[]{stack});
      } else if (ingredient instanceof TagKey tagKey) {
         this.ingredient = Ingredient.of(tagKey);
      } else if (ingredient instanceof Ingredient ingredientObj) {
         this.ingredient = ingredientObj;
      } else {
         if (!(ingredient instanceof DeferredItem)) {
            throw new IllegalArgumentException("Unsupported ingredient type: " + ingredient.getClass().getName());
         }

         this.ingredient = Ingredient.of(new ItemLike[]{(ItemLike)((DeferredItem)ingredient).get()});
      }

      return this;
   }

   public TeapotBuilder setIngredientCount(int ingredientCount) {
      this.ingredientCount = ingredientCount;
      return this;
   }

   public TeapotBuilder setTime(int time) {
      this.time = time;
      return this;
   }

   public TeapotBuilder setResult(ItemStack result) {
      this.result = result;
      return this;
   }

   public TeapotBuilder setResult(ItemLike result) {
      this.result = new ItemStack(result);
      return this;
   }

   public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
      return this;
   }

   public RecipeBuilder group(@Nullable String groupName) {
      return this;
   }

   public Item getResult() {
      return this.result.getItem();
   }

   public void save(RecipeOutput output) {
      String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "teapot/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "teapot/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      recipeOutput.accept(id, new TeapotRecipe(this.teaFluid, this.ingredient, this.ingredientCount, this.time, this.result), null);
   }
}
