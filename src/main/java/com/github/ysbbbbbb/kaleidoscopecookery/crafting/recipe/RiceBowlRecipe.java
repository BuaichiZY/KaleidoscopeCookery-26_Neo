package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RiceBowlRecipe extends CustomRecipe {
   private final CraftingBookCategory category;
   private final Ingredient ingredient;
   private final ItemStack result;

   public RiceBowlRecipe(CraftingBookCategory category, Ingredient ingredient, ItemStack result) {
      this.category = category;
      this.ingredient = ingredient;
      this.result = result;
   }

   public boolean matches(CraftingInput container, Level level) {
      List<ItemStack> items = container.items();
      return items.stream().filter(this.ingredient).count() != 1L ? false : items.stream().filter(stack -> stack.is(TagCommon.COOKED_RICE)).count() == 1L;
   }

   public ItemStack assemble(CraftingInput container) {
      ItemStack assembled = this.result.copy();
      copyBestQuality(container, assembled);
      return assembled;
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   public NonNullList<Ingredient> getIngredients() {
      return NonNullList.of(ModRecipes.EMPTY_INGREDIENT, new Ingredient[]{this.ingredient});
   }

   public ItemStack getResultItem(Provider registries) {
      return this.result.copy();
   }

   public ItemStack getResult() {
      return this.result;
   }

   public Ingredient getIngredient() {
      return this.ingredient;
   }

   @SuppressWarnings("unchecked")
   public RecipeSerializer<RiceBowlRecipe> getSerializer() {
      return (RecipeSerializer<RiceBowlRecipe>)(RecipeSerializer<?>)ModRecipes.RICE_BOWL_SERIALIZER.get();
   }

   @Override
   public CraftingBookCategory category() {
      return this.category;
   }

   private static void copyBestQuality(CraftingInput container, ItemStack result) {
      boolean hasQuality = false;
      Quality bestQuality = Quality.POOR;

      for (int i = 0; i < container.size(); i++) {
         ItemStack ingredient = container.getItem(i);
         if (QualityUtils.hasQuality(ingredient)) {
            Quality quality = QualityUtils.getQuality(ingredient);
            if (!hasQuality || quality.getScore() > bestQuality.getScore()) {
               bestQuality = quality;
               hasQuality = true;
            }
         }
      }

      if (hasQuality) {
         QualityUtils.setQuality(result, bestQuality);
      }
   }
}
