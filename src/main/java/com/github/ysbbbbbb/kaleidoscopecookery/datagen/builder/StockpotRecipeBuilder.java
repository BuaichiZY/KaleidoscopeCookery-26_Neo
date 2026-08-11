package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
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
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class StockpotRecipeBuilder implements RecipeBuilder {
   private static final String NAME = "stockpot";
   private List<Ingredient> ingredients = Lists.newArrayList();
   private ItemStack result = ItemStack.EMPTY;
   private int time = 300;
   private Ingredient carrier = StockpotRecipeSerializer.DEFAULT_CARRIER;
   private boolean emptyCarrier = false;
   private Identifier soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
   private StockpotVisuals visuals = StockpotVisuals.DEFAULT;

   public static StockpotRecipeBuilder builder() {
      return new StockpotRecipeBuilder();
   }

   public StockpotRecipeBuilder addInput(Object... ingredients) {
      for (Object ingredient : ingredients) {
         if (ingredient instanceof ItemLike itemLike) {
            this.ingredients.add(Ingredient.of(new ItemLike[]{itemLike}));
         } else if (ingredient instanceof ItemStack stack) {
            this.ingredients.add(Ingredient.of(new ItemStack[]{stack}));
         } else if (ingredient instanceof TagKey tagKey) {
            this.ingredients.add(Ingredient.of(tagKey));
         } else if (ingredient instanceof Ingredient ingredientObj) {
            this.ingredients.add(ingredientObj);
         } else if (ingredient instanceof DeferredItem<?> deferredItem) {
            this.ingredients.add(Ingredient.of(new ItemLike[]{(ItemLike)deferredItem.get()}));
         }
      }

      return this;
   }

   public StockpotRecipeBuilder setSoupBase(Identifier soupBase) {
      this.soupBase = soupBase;
      return this;
   }

   public StockpotRecipeBuilder setResult(Item result) {
      this.result = new ItemStack(result, 3);
      return this;
   }

   public StockpotRecipeBuilder setResult(Item result, int count) {
      return this.setResult(new ItemStack(result, count));
   }

   public StockpotRecipeBuilder setResult(Identifier result) {
      this.result = new ItemStack(Objects.requireNonNull((Item)BuiltInRegistries.ITEM.getValue(result)));
      return this;
   }

   public StockpotRecipeBuilder setResult(ItemStack result) {
      this.result = result;
      return this;
   }

   public StockpotRecipeBuilder setTime(int time) {
      this.time = time;
      return this;
   }

   public StockpotRecipeBuilder setCarrier(ItemLike carrier) {
      this.carrier = Ingredient.of(new ItemLike[]{carrier});
      this.emptyCarrier = false;
      return this;
   }

   public StockpotRecipeBuilder setEmptyCarrier() {
      this.carrier = com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT;
      this.emptyCarrier = true;
      return this;
   }

   public StockpotRecipeBuilder setCookingTexture(Identifier cookingTexture) {
      this.visuals = new StockpotVisuals(cookingTexture, this.visuals.finishedTexture(), this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
      return this;
   }

   public StockpotRecipeBuilder setFinishedTexture(Identifier finishedTexture) {
      this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), finishedTexture, this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
      return this;
   }

   public StockpotRecipeBuilder setCookingBubbleColor(int cookingBubbleColor) {
      this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(), cookingBubbleColor, this.visuals.finishedBubbleColor());
      return this;
   }

   public StockpotRecipeBuilder setFinishedBubbleColor(int finishedBubbleColor) {
      this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(), this.visuals.cookingBubbleColor(), finishedBubbleColor);
      return this;
   }

   public StockpotRecipeBuilder setBubbleColors(int cookingBubbleColor, int finishedBubbleColor) {
      this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(), cookingBubbleColor, finishedBubbleColor);
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
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot/" + path);
      this.save(output, filePath);
   }

   public void save(RecipeOutput output, String recipeId) {
      Identifier filePath = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot/" + recipeId);
      this.save(output, filePath);
   }

   public void save(RecipeOutput recipeOutput, Identifier id) {
      StockpotRecipe recipe = new StockpotRecipe(this.ingredients, this.soupBase, this.result, this.time, this.carrier, this.visuals);
      recipeOutput.accept(id, recipe, null);
   }
}
