package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class EmiStockpotRecipe extends BasicEmiRecipe {
   public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
      Identifier.parse(ModRecipes.STOCKPOT_RECIPE.toString()), EmiIngredient.of(Ingredient.of(new ItemLike[]{(ItemLike)ModItems.STOCKPOT.get()}))
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/stockpot.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 102;
   private final EmiStack soupBase;

   public EmiStockpotRecipe(Identifier id, List<EmiIngredient> inputs, List<EmiStack> outputs, List<EmiIngredient> catalysts, EmiStack soupBase) {
      super(CATEGORY, id, 176, 102);
      this.inputs = inputs;
      this.outputs = outputs;
      this.catalysts = catalysts;
      this.soupBase = soupBase;
   }

   public static void register(EmiRegistry registry) {
      registry.addCategory(CATEGORY);
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.STOCKPOT.get()));
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.STOCKPOT_LID.get()));
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.STOCKPOT_RECIPE).forEach(r -> registerRecipe(registry, r));
      List<RecipeHolder<StockpotRecipe>> compatRecipes = Lists.newArrayList();
      FarmersDelightCompat.getTransformRecipeForJei(Minecraft.getInstance().level, compatRecipes);
      if (!compatRecipes.isEmpty()) {
         compatRecipes.forEach(r -> registerRecipe(registry, (RecipeHolder<StockpotRecipe>)r));
      }
   }

   private static void registerRecipe(EmiRegistry registry, RecipeHolder<StockpotRecipe> holder) {
      StockpotRecipe recipe = (StockpotRecipe)holder.value();
      List<EmiIngredient> inputs = recipe.getIngredients().stream().<EmiIngredient>map(EmiIngredient::of).toList();
      List<EmiStack> outputs = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
      List<EmiIngredient> catalysts = recipe.carrier().isEmpty() ? List.of() : List.of(EmiIngredient.of(recipe.carrier()));
      ISoupBase soupBase = SoupBaseManager.getSoupBase(recipe.soupBase());
      if (soupBase == null) {
         throw new RuntimeException("No soup found for " + recipe.soupBase());
      } else {
         EmiStack soupBaseItem = EmiStack.of(soupBase.getDisplayStack());
         registry.addRecipe(new EmiStockpotRecipe(holder.id(), inputs, outputs, catalysts, soupBaseItem));
      }
   }

   public void addWidgets(WidgetHolder widgets) {
      widgets.addTexture(BG, 1, 1, 176, 102, 0, 0);
      widgets.addText(Component.translatable("jei.kaleidoscope_cookery.strict_recipe"), 88, 90, 5592405, false).horizontalAlign(Alignment.CENTER);

      for (int i = 0; i < this.inputs.size(); i++) {
         int xOffset = i % 3 * 18 + 15;
         int yOffset = i / 3 * 18 + 25;
         widgets.addSlot((EmiIngredient)this.inputs.get(i), xOffset, yOffset).drawBack(false);
      }

      if (!this.soupBase.isEmpty()) {
         widgets.addSlot(this.soupBase, 72, 61).drawBack(false);
      }

      if (!this.catalysts.isEmpty()) {
         widgets.addSlot((EmiIngredient)this.catalysts.getFirst(), 133, 18).drawBack(false);
      }

      widgets.addSlot((EmiIngredient)this.outputs.getFirst(), 143, 60).drawBack(false).recipeContext(this);
   }
}
