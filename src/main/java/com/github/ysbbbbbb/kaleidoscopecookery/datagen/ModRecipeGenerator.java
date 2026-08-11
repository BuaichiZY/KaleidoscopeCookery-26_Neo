package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.ChoppingBoardRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.DecorationRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.FoodBiteRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.MillstoneRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.ModRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.PotRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.ShapedRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.ShapelessRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.SimpleCookingRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.SimplePotRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.SteamerRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.StockpotRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.TeapotRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;

public class ModRecipeGenerator extends ModRecipeProvider {
   private final List<ModRecipeProvider> providers = Lists.newArrayList();

   public ModRecipeGenerator(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
      this.providers.add(new ChoppingBoardRecipeProvider(output, registries));
      this.providers.add(new DecorationRecipeProvider(output, registries));
      this.providers.add(new FoodBiteRecipeProvider(output, registries));
      this.providers.add(new PotRecipeProvider(output, registries));
      this.providers.add(new ShapedRecipeProvider(output, registries));
      this.providers.add(new ShapelessRecipeProvider(output, registries));
      this.providers.add(new SimpleCookingRecipeProvider(output, registries));
      this.providers.add(new SimplePotRecipeProvider(output, registries));
      this.providers.add(new StockpotRecipeProvider(output, registries));
      this.providers.add(new MillstoneRecipeProvider(output, registries));
      this.providers.add(new SteamerRecipeProvider(output, registries));
      this.providers.add(new TeapotRecipeProvider(output, registries));
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      netheriteSmithing(consumer, (Item)ModItems.DIAMOND_KITCHEN_KNIFE.get(), RecipeCategory.TOOLS, (Item)ModItems.NETHERITE_KITCHEN_KNIFE.get());

      for (ModRecipeProvider provider : this.providers) {
         provider.buildRecipes(consumer);
      }
   }
}
