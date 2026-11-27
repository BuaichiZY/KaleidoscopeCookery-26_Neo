package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
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
import net.minecraft.world.level.ItemLike;

public class ReiBambooTrayRecipeCategory implements DisplayCategory<DefaultCustomDisplay> {
   public static final CategoryIdentifier<DefaultCustomDisplay> ID = CategoryIdentifier.of("kaleidoscope_cookery", "plugin/bamboo_tray");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.bamboo_tray");
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/bamboo_tray.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 78;

   public CategoryIdentifier<? extends DefaultCustomDisplay> getCategoryIdentifier() { return ID; }

   public List<Widget> setupDisplay(DefaultCustomDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y;
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, WIDTH, HEIGHT));
      widgets.add(Widgets.createSlot(new Point(startX + 41, startY + 27)).entries((Collection)display.getInputEntries().get(0)).disableBackground().markInput());
      widgets.add(Widgets.createSlot(new Point(startX + 131, startY + 29)).entries((Collection)display.getOutputEntries().get(0)).disableBackground().markOutput());
      return widgets;
   }

   public int getDisplayWidth(DefaultCustomDisplay display) { return WIDTH; }
   public int getDisplayHeight() { return HEIGHT; }
   public Component getTitle() { return TITLE; }
   public Renderer getIcon() { return EntryStacks.of((ItemLike)ModItems.BAMBOO_TRAY.get()); }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiBambooTrayRecipeCategory());
      registry.addWorkstations(ID, new EntryIngredient[]{ReiUtil.ofItem((Item)ModItems.BAMBOO_TRAY.get())});
   }

   public static void registerDisplays(DisplayRegistry registry) {
      com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache.getRecipes(ModRecipes.BAMBOO_TRAY_RECIPE).forEach(holder -> {
         BambooTrayRecipe recipe = holder.value();
         List<EntryIngredient> input = ReiUtil.ofIngredients(recipe.getIngredient());
         List<EntryIngredient> output = ReiUtil.ofItemStacks(recipe.getResult());
         registry.add(new DefaultCustomDisplay(input, output, java.util.Optional.of(holder.id().identifier())) {
            public CategoryIdentifier<?> getCategoryIdentifier() { return ReiBambooTrayRecipeCategory.ID; }
         });
      });
   }
}
