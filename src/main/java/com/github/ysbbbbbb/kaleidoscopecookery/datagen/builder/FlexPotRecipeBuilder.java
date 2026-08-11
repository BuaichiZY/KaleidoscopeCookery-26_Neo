package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class FlexPotRecipeBuilder implements RecipeBuilder {
   private static final String NAME = "flex_pot";
   private int time = 200;
   private int stirFryCount = 3;
   private Ingredient carrier = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
   private List<Ingredient> ingredients = Lists.newArrayList();
   private ItemStack result = ItemStack.EMPTY;

   public static FlexPotRecipeBuilder builder() {
      return new FlexPotRecipeBuilder();
   }

   public FlexPotRecipeBuilder setTime(int time) {
      this.time = time;
      return this;
   }

   public FlexPotRecipeBuilder setStirFryCount(int stirFryCount) {
      this.stirFryCount = stirFryCount;
      return this;
   }

   public FlexPotRecipeBuilder setCarrier(Ingredient ingredient) {
      this.carrier = ingredient;
      return this;
   }

   public FlexPotRecipeBuilder setCarrier(TagKey<Item> tagKey) {
      this.carrier = Ingredient.of(tagKey);
      return this;
   }

   public FlexPotRecipeBuilder setCarrier(ItemLike itemLike) {
      this.carrier = Ingredient.of(new ItemLike[]{itemLike});
      return this;
   }

   public FlexPotRecipeBuilder setBowlCarrier() {
      this.carrier = Ingredient.of(new ItemLike[]{Items.BOWL});
      return this;
   }

   public FlexPotRecipeBuilder addInput(Object... ingredients) {
      for (Object ingredient : ingredients) {
         if (ingredient instanceof ItemLike itemLike) {
            this.ingredients.add(Ingredient.of(new ItemLike[]{itemLike}));
         } else if (ingredient instanceof ItemStack stack) {
            this.ingredients.add(Ingredient.of(new ItemStack[]{stack}));
         } else if (ingredient instanceof TagKey tagKey) {
            this.ingredients.add(Ingredient.of(tagKey));
         } else if (ingredient instanceof Ingredient ingredientObj) {
            this.ingredients.add(ingredientObj);
         } else if (ingredient instanceof DeferredItem registryObject) {
            this.ingredients.add(Ingredient.of(new ItemLike[]{(ItemLike)registryObject.get()}));
         }
      }

      return this;
   }

   public FlexPotRecipeBuilder setResult(Item result) {
      this.result = new ItemStack(result);
      return this;
   }

   public FlexPotRecipeBuilder setResult(Identifier result) {
      this.result = new ItemStack((ItemLike)BuiltInRegistries.ITEM.getValue(result));
      return this;
   }

   public FlexPotRecipeBuilder setResult(Item result, int count) {
      this.result = new ItemStack(result, count);
      return this;
   }

   public FlexPotRecipeBuilder setResult(Identifier result, int count) {
      this.result = new ItemStack((ItemLike)BuiltInRegistries.ITEM.getValue(result), count);
      return this;
   }

   public FlexPotRecipeBuilder setResult(ItemStack result) {
      this.result = result;
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
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "flex_pot/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "flex_pot/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      FlexPotRecipe recipe = new FlexPotRecipe(this.time, this.stirFryCount, this.carrier, this.ingredients, this.result);
      recipeOutput.accept(id, recipe, null);
   }
}
