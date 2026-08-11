package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.SteamerBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class SteamerRecipeProvider extends ModRecipeProvider {
   public SteamerRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      SteamerBuilder.builder().setIngredient((ItemLike)ModItems.STUFFED_DOUGH_FOOD.get()).setResult((ItemLike)ModItems.BAOZI.get()).save(consumer);
      SteamerBuilder.builder().setIngredient(TagCommon.DOUGH).setResult((ItemLike)ModItems.MANTOU.get()).save(consumer);
      SteamerBuilder.builder().setIngredient(Items.SLIME_BALL).setResult((ItemLike)ModItems.QINGTUAN.get()).save(consumer);
      SteamerBuilder.builder().setIngredient((ItemLike)ModItems.RAW_BAMBOO_TUBE_RICE.get()).setResult((ItemLike)ModItems.BAMBOO_TUBE_RICE.get()).save(consumer);
   }
}
