package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public abstract class ModRecipeProvider extends RecipeProvider {
   public ModRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   public void buildRecipes(RecipeOutput consumer) {
   }

   public Identifier modLoc(String path) {
      return Identifier.fromNamespaceAndPath("kaleidoscope_cookery", path);
   }

   public String getRecipeIdWithCount(ItemLike itemLike, int count) {
      return RecipeBuilder.getDefaultRecipeId(itemLike.asItem()).getPath() + "_" + count;
   }

   public ItemLike[] getItemsWithCount(ItemLike itemLike, int count) {
      ItemLike[] items = new ItemLike[count];
      Arrays.fill(items, itemLike);
      return items;
   }

   public TagKey<Item>[] getItemsWithCount(TagKey<Item> itemLike, int count) {
      TagKey<Item>[] items = new TagKey[count];
      Arrays.fill(items, itemLike);
      return items;
   }
}
