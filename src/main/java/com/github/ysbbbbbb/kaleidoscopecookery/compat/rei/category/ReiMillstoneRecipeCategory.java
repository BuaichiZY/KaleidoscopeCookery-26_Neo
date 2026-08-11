package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.create.CreateCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class ReiMillstoneRecipeCategory implements DisplayCategory<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> {
   public static final CategoryIdentifier<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> ID = CategoryIdentifier.of(
      "kaleidoscope_cookery", "plugin/millstone"
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/millstone.png");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.millstone");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 95;

   public CategoryIdentifier<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> getCategoryIdentifier() {
      return ID;
   }

   public List<Widget> setupDisplay(ReiMillstoneRecipeCategory.MillstoneRecipeDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y;
      List<EntryIngredient> outputs = display.getOutputEntries();
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, 176, 95));
      widgets.add(Widgets.createSlot(new Point(startX + 69, startY + 39)).entries((Collection)display.getInputEntries().get(0)).markInput());
      widgets.add(Widgets.createSlot(new Point(startX + 146, startY + 47)).entries((Collection)outputs.get(0)).markOutput());
      if (outputs.size() > 1) {
         for (int i = 1; i < outputs.size(); i++) {
            int x = 166 + i * -20;
            int y = 26;
            widgets.add(Widgets.createSlot(new Point(startX + x, startY + y)).entries((Collection)outputs.get(i)));
         }
      }

      return widgets;
   }

   public int getDisplayWidth(ReiMillstoneRecipeCategory.MillstoneRecipeDisplay display) {
      return 176;
   }

   public int getDisplayHeight() {
      return 95;
   }

   public Component getTitle() {
      return TITLE;
   }

   public Renderer getIcon() {
      return EntryStacks.of((ItemLike)ModItems.MILLSTONE.get());
   }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiMillstoneRecipeCategory());
      registry.addWorkstations(ID, new EntryIngredient[]{ReiUtil.ofItem((Item)ModItems.MILLSTONE.get())});
   }

   public static void registerDisplays(DisplayRegistry registry) {
      List<RecipeHolder<MillstoneRecipe>> millstoneRecipes = Lists.newArrayList();
      millstoneRecipes.addAll(registry.getRecipeManager().getAllRecipesFor(ModRecipes.MILLSTONE_RECIPE));
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         CreateCompat.getTransformRecipeForSearch(level, millstoneRecipes);
      }

      millstoneRecipes.forEach(r -> {
         List<EntryIngredient> input = ReiUtil.ofIngredients(((MillstoneRecipe)r.value()).getIngredients());
         List<EntryIngredient> outputs = Lists.newArrayList();
         ((MillstoneRecipe)r.value()).results().stream().filter(output -> !output.isEmpty()).forEach(output -> {
            EntryIngredient entry = ReiUtil.ofItemStack(output.stack());
            outputs.add(entry);
         });
         registry.add(new ReiMillstoneRecipeCategory.MillstoneRecipeDisplay(r.id(), input, outputs));
      });
   }

   public static class MillstoneRecipeDisplay extends BasicDisplay {
      public MillstoneRecipeDisplay(Identifier location, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
         super(inputs, outputs, Optional.of(location));
      }

      public CategoryIdentifier<?> getCategoryIdentifier() {
         return ReiMillstoneRecipeCategory.ID;
      }
   }
}
