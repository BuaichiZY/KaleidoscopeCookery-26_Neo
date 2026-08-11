package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ReiFlexPotRecipeCategory implements DisplayCategory<ReiFlexPotRecipeCategory.FlexPotRecipeDisplay> {
   public static final CategoryIdentifier<ReiFlexPotRecipeCategory.FlexPotRecipeDisplay> ID = CategoryIdentifier.of("kaleidoscope_cookery", "plugin/flex_pot");
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/pot.png");
   private static final Component TITLE = ComponentUtils.formatList(
      List.of(Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), Component.translatable("block.kaleidoscope_cookery.pot")), CommonComponents.SPACE
   );
   public static final int WIDTH = 176;
   public static final int HEIGHT = 102;

   public CategoryIdentifier<ReiFlexPotRecipeCategory.FlexPotRecipeDisplay> getCategoryIdentifier() {
      return ID;
   }

   public List<Widget> setupDisplay(ReiFlexPotRecipeCategory.FlexPotRecipeDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y;
      Component stirFryCount = Component.translatable("jei.kaleidoscope_cookery.pot.stir_fry_count", new Object[]{display.stirFryCount});
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, 176, 102));
      widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((guiGraphics, mouseX, mouseY, v) -> {
         this.drawCenteredString(guiGraphics, Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), 88, 5);
         this.drawCenteredString(guiGraphics, stirFryCount, 88, 85);
      }), startX, startY, 0.0));
      List<EntryIngredient> inputs = display.getInputEntries();

      for (int i = 0; i < inputs.size(); i++) {
         int xOffset = i % 3 * 18 + 15;
         int yOffset = i / 3 * 18 + 24;
         widgets.add(Widgets.createSlot(new Point(startX + xOffset, startY + yOffset)).entries((Collection)inputs.get(i)).disableBackground().markInput());
         if (!inputs.get(i).isEmpty()) {
            widgets.add(
               Widgets.withTranslate(
                  Widgets.createDrawableWidget(
                     (guiGraphics, mouseX, mouseY, v) -> guiGraphics.drawString(
                        Minecraft.getInstance().font, Component.literal("*"), xOffset, yOffset, 16777215, true
                     )
                  ),
                  startX,
                  startY,
                  0.0
               )
            );
         }
      }

      if (!display.carrier.isEmpty()) {
         widgets.add(Widgets.createSlot(new Point(startX + 133, startY + 18)).entries(display.carrier).disableBackground().markInput());
      }

      widgets.add(
         Widgets.createSlot(new Point(startX + 143, startY + 60)).entries((Collection)display.getOutputEntries().get(0)).disableBackground().markOutput()
      );
      return widgets;
   }

   private void drawCenteredString(GuiGraphics guiGraphics, Component text, int centerX, int y) {
      Font font = Minecraft.getInstance().font;
      FormattedCharSequence sequence = text.getVisualOrderText();
      guiGraphics.drawString(font, sequence, centerX - font.width(sequence) / 2, y, 5592405, false);
   }

   public int getDisplayWidth(ReiFlexPotRecipeCategory.FlexPotRecipeDisplay display) {
      return 176;
   }

   public int getDisplayHeight() {
      return 102;
   }

   public Component getTitle() {
      return TITLE;
   }

   public Renderer getIcon() {
      return EntryStacks.of((ItemLike)ModItems.POT.get());
   }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiFlexPotRecipeCategory());
      registry.addWorkstations(
         ID,
         new EntryIngredient[]{
            ReiUtil.ofItem((Item)ModItems.POT.get()), ReiUtil.ofIngredient(Ingredient.of(TagMod.KITCHEN_SHOVEL)), ReiUtil.ofItem((Item)ModItems.OIL.get())
         }
      );
   }

   public static void registerDisplays(DisplayRegistry registry) {
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_POT_RECIPE).forEach(holder -> {
         FlexPotRecipe r = (FlexPotRecipe)holder.value();
         List<EntryIngredient> inputs = ReiUtil.ofIngredients(r.getIngredients());
         List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResultItem(RegistryAccess.EMPTY));
         EntryIngredient carrier = r.carrier().isEmpty() ? EntryIngredient.empty() : ReiUtil.ofIngredient(r.carrier());
         registry.add(new ReiFlexPotRecipeCategory.FlexPotRecipeDisplay(holder.id(), inputs, output, carrier, r.stirFryCount()));
      });
   }

   public static class FlexPotRecipeDisplay extends BasicDisplay {
      public final EntryIngredient carrier;
      public final int stirFryCount;

      public FlexPotRecipeDisplay(
         Identifier location, List<EntryIngredient> inputs, List<EntryIngredient> outputs, EntryIngredient carrier, int stirFryCount
      ) {
         super(inputs, outputs, Optional.of(location));
         this.carrier = carrier;
         this.stirFryCount = stirFryCount;
      }

      public CategoryIdentifier<?> getCategoryIdentifier() {
         return ReiFlexPotRecipeCategory.ID;
      }
   }
}
