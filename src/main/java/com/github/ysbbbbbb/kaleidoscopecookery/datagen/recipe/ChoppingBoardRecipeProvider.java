package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.ChoppingBoardBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class ChoppingBoardRecipeProvider extends ModRecipeProvider {
   public ChoppingBoardRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.MUTTON)
         .setResult((ItemLike)ModItems.RAW_LAMB_CHOPS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_lamb_chops"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_MUTTON)
         .setResult((ItemLike)ModItems.COOKED_LAMB_CHOPS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_lamb_chops"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.TROPICAL_FISH)
         .setResult((ItemLike)ModItems.SASHIMI.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("sashimi"))
         .save(consumer, "sashimi_from_tropical_fish");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COD)
         .setResult((ItemLike)ModItems.SASHIMI.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cod"))
         .save(consumer, "sashimi_from_cod");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_COD)
         .setResult(Items.BONE, 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_cod"))
         .save(consumer, "bone_from_cod");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.SALMON)
         .setResult((ItemLike)ModItems.SASHIMI.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("salmon"))
         .save(consumer, "sashimi_from_salmon");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_SALMON)
         .setResult(Items.BONE, 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_salmon"))
         .save(consumer, "bone_from_salmon");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.BEEF)
         .setResult((ItemLike)ModItems.RAW_COW_OFFAL.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_cow_offal"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_BEEF)
         .setResult((ItemLike)ModItems.COOKED_COW_OFFAL.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_cow_offal"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.PORKCHOP)
         .setResult((ItemLike)ModItems.RAW_PORK_BELLY.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_pork_belly"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_PORKCHOP)
         .setResult((ItemLike)ModItems.COOKED_PORK_BELLY.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_pork_belly"))
         .save(consumer);
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.CHICKEN)
         .setResult((ItemLike)ModItems.RAW_CUT_SMALL_MEATS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_chicken"))
         .save(consumer, "raw_cut_small_meats_from_chicken");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_CHICKEN)
         .setResult((ItemLike)ModItems.COOKED_CUT_SMALL_MEATS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_chicken"))
         .save(consumer, "cooked_cut_small_meats_from_chicken");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.RABBIT)
         .setResult((ItemLike)ModItems.RAW_CUT_SMALL_MEATS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_rabbit"))
         .save(consumer, "raw_cut_small_meats_from_rabbit");
      ChoppingBoardBuilder.builder()
         .setIngredient(Items.COOKED_RABBIT)
         .setResult((ItemLike)ModItems.COOKED_CUT_SMALL_MEATS.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("cooked_rabbit"))
         .save(consumer, "cooked_cut_small_meats_from_rabbit");
      ChoppingBoardBuilder.builder()
         .setIngredient(TagCommon.DOUGH)
         .setResult((ItemLike)ModItems.RAW_NOODLES.get(), 2)
         .setCutCount(4)
         .setModelId(this.modLoc("raw_dough"))
         .save(consumer, "raw_noodles_from_raw_dough");
   }
}
