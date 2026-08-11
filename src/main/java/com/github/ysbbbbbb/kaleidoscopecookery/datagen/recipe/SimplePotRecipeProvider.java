package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SimplePotRecipeProvider extends ModRecipeProvider {
   public SimplePotRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      this.addSingleItemRecipe(Items.POTATO, Items.BAKED_POTATO, "potato", consumer);
      this.addSingleItemRecipe(Items.KELP, Items.DRIED_KELP, "kelp", consumer);
      this.addSingleItemRecipe(Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, "chorus_fruit", consumer);
      this.addSingleItemRecipe(net.neoforged.neoforge.common.Tags.Items.EGGS, (Item)ModItems.FRIED_EGG.get(), "egg", consumer);
      this.addSingleItemRecipe(Items.BEEF, Items.COOKED_BEEF, "beef", consumer);
      this.addSingleItemRecipe(Items.CHICKEN, Items.COOKED_CHICKEN, "chicken", consumer);
      this.addSingleItemRecipe(Items.COD, Items.COOKED_COD, "cod", consumer);
      this.addSingleItemRecipe(Items.SALMON, Items.COOKED_SALMON, "salmon", consumer);
      this.addSingleItemRecipe(Items.MUTTON, Items.COOKED_MUTTON, "mutton", consumer);
      this.addSingleItemRecipe(Items.PORKCHOP, Items.COOKED_PORKCHOP, "porkchop", consumer);
      this.addSingleItemRecipe(Items.RABBIT, Items.COOKED_RABBIT, "rabbit", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.RAW_LAMB_CHOPS.get(), (Item)ModItems.COOKED_LAMB_CHOPS.get(), "raw_lamb_chops", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.RAW_COW_OFFAL.get(), (Item)ModItems.COOKED_COW_OFFAL.get(), "raw_cow_offal", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.RAW_PORK_BELLY.get(), (Item)ModItems.COOKED_PORK_BELLY.get(), "raw_pork_belly", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.RAW_CUT_SMALL_MEATS.get(), (Item)ModItems.COOKED_CUT_SMALL_MEATS.get(), "raw_cut_small_meats", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.RAW_MEATBALL.get(), (Item)ModItems.COOKED_MEATBALL.get(), "raw_meatball", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.STUFFED_DOUGH_FOOD.get(), (Item)ModItems.MEAT_PIE.get(), "stuffed_dough_food", consumer);
      this.addSingleItemRecipe((ItemLike)ModItems.COOKED_RICE.get(), (Item)ModItems.STICKY_RICE_CAKE.get(), "cooked_rice", consumer);
   }

   public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
      this.addSingleItemRecipe(inputItem, outputItem, idInput, com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, consumer);
   }

   public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
      this.addSingleItemRecipe(inputItem, outputItem, idInput, com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, consumer);
   }

   public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, Ingredient carrier, RecipeOutput consumer) {
      for (int i = 1; i <= 9; i++) {
         TagKey<Item>[] inputs = this.getItemsWithCount(inputItem, i);
         ItemStack output = new ItemStack(outputItem, i);
         String idOutput = this.getRecipeIdWithCount(outputItem, i);
         String id = String.format("%s_to_%s", idInput, idOutput);
         PotRecipeBuilder.builder().addInput(inputs).setResult(output).setCarrier(carrier).save(consumer, id);
      }
   }

   public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Ingredient carrier, RecipeOutput consumer) {
      for (int i = 1; i <= 9; i++) {
         ItemLike[] inputs = this.getItemsWithCount(inputItem, i);
         ItemStack output = new ItemStack(outputItem, i);
         String idOutput = this.getRecipeIdWithCount(outputItem, i);
         String id = String.format("%s_to_%s", idInput, idOutput);
         PotRecipeBuilder.builder().addInput(inputs).setResult(output).setCarrier(carrier).save(consumer, id);
      }
   }
}
