package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class ReiSteamerRecipeCategory implements DisplayCategory<DefaultCustomDisplay> {
   public static final CategoryIdentifier<DefaultCustomDisplay> ID = CategoryIdentifier.of("kaleidoscope_cookery", "plugin/steamer");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.steamer");
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/steamer.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 78;

   public CategoryIdentifier<? extends DefaultCustomDisplay> getCategoryIdentifier() {
      return ID;
   }

   public List<Widget> setupDisplay(DefaultCustomDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y;
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, 176, 78));
      widgets.add(Widgets.createSlot(new Point(startX + 38, startY + 27)).entries((Collection)display.getInputEntries().get(0)).disableBackground().markInput());
      widgets.add(
         Widgets.createSlot(new Point(startX + 128, startY + 30)).entries((Collection)display.getOutputEntries().get(0)).disableBackground().markOutput()
      );
      return widgets;
   }

   public int getDisplayWidth(DefaultCustomDisplay display) {
      return 176;
   }

   public int getDisplayHeight() {
      return 78;
   }

   public Component getTitle() {
      return TITLE;
   }

   public Renderer getIcon() {
      return EntryStacks.of((ItemLike)ModItems.STEAMER.get());
   }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiSteamerRecipeCategory());
      registry.addWorkstations(
         ID, new EntryIngredient[]{ReiUtil.ofItem((Item)ModItems.STEAMER.get()), ReiUtil.ofIngredient(Ingredient.of(TagMod.KITCHEN_KNIFE))}
      );
   }

   public static void registerDisplays(DisplayRegistry registry) {
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.STEAMER_RECIPE).forEach(r -> {
         List<EntryIngredient> input = ReiUtil.ofIngredients(((SteamerRecipe)r.value()).getIngredients());
         List<EntryIngredient> output = ReiUtil.ofItemStacks(((SteamerRecipe)r.value()).getResult());
         registry.add(new DefaultCustomDisplay(r, input, output) {
            public CategoryIdentifier<?> getCategoryIdentifier() {
               return ReiSteamerRecipeCategory.ID;
            }
         });
      });
   }
}
