package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

public class ReiTeapotRecipeCategory implements DisplayCategory<ReiTeapotRecipeCategory.TeapotRecipeDisplay> {
   public static final CategoryIdentifier<ReiTeapotRecipeCategory.TeapotRecipeDisplay> ID = CategoryIdentifier.of("kaleidoscope_cookery", "plugin/teapot");
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/teapot.png");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.teapot");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 88;

   public CategoryIdentifier<ReiTeapotRecipeCategory.TeapotRecipeDisplay> getCategoryIdentifier() {
      return ID;
   }

   public List<Widget> setupDisplay(ReiTeapotRecipeCategory.TeapotRecipeDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y + 4;
      Component brewTime = Component.translatable("jei.kaleidoscope_cookery.teapot.time", new Object[]{display.brewTime / 20});
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, 176, 88));
      widgets.add(
         Widgets.withTranslate(
            Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> this.drawCenteredString(guiGraphics, brewTime, 88, 70)), startX, startY, 0.0
         )
      );
      widgets.add(Widgets.createSlot(new Point(startX + 65, startY + 3)).entries((Collection)display.getInputEntries().get(0)).markInput());
      widgets.add(Widgets.createSlot(new Point(startX + 83, startY + 3)).entries((Collection)display.getInputEntries().get(1)).markInput());
      widgets.add(
         Widgets.createSlot(new Point(startX + 128, startY + 30)).entries((Collection)display.getOutputEntries().get(0)).backgroundEnabled(false).markOutput()
      );
      return widgets;
   }

   private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
      Font font = Minecraft.getInstance().font;
      guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, 5592405, false);
   }

   public int getDisplayWidth(ReiTeapotRecipeCategory.TeapotRecipeDisplay display) {
      return 176;
   }

   public int getDisplayHeight() {
      return 88;
   }

   public Component getTitle() {
      return TITLE;
   }

   public Renderer getIcon() {
      return EntryStacks.of((ItemLike)ModItems.TEAPOT.get());
   }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiTeapotRecipeCategory());
      registry.addWorkstations(ID, new EntryIngredient[]{ReiUtil.ofItem((Item)ModItems.TEAPOT.get())});
   }

   public static void registerDisplays(DisplayRegistry registry) {
      registry.getRecipeManager()
         .getAllRecipesFor(ModRecipes.TEAPOT_RECIPE)
         .forEach(
            r -> {
               Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(((TeapotRecipe)r.value()).teaFluid());
               Item bucket = fluid.getBucket();
               List<EntryIngredient> fluidInput = ReiUtil.ofItems(bucket);
               List<EntryIngredient> inputs = List.of(
                  EntryIngredient.of(
                     Arrays.stream(((TeapotRecipe)r.value()).ingredient().getItems())
                        .map(stack -> EntryStacks.of(stack.copyWithCount(((TeapotRecipe)r.value()).ingredientCount())))
                        .toList()
                  )
               );
               List<EntryIngredient> output = ReiUtil.ofItemStacks(((TeapotRecipe)r.value()).result().copyWithCount(12));
               registry.add(new ReiTeapotRecipeCategory.TeapotRecipeDisplay(r.id(), fluidInput, inputs, output, ((TeapotRecipe)r.value()).time()));
            }
         );
   }

   public static class TeapotRecipeDisplay extends BasicDisplay {
      public final int brewTime;

      public TeapotRecipeDisplay(
         Identifier location, List<EntryIngredient> fluidInput, List<EntryIngredient> ingredientInput, List<EntryIngredient> outputs, int brewTime
      ) {
         super(List.of(fluidInput.getFirst(), ingredientInput.getFirst()), outputs, Optional.of(location));
         this.brewTime = brewTime;
      }

      public CategoryIdentifier<?> getCategoryIdentifier() {
         return ReiTeapotRecipeCategory.ID;
      }
   }
}
