package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ReiFlexStockpotRecipeCategory implements DisplayCategory<ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay> {
   public static final CategoryIdentifier<ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay> ID = CategoryIdentifier.of(
      "kaleidoscope_cookery", "plugin/flex_stockpot"
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/stockpot.png");
   private static final Component TITLE = ComponentUtils.formatList(
      List.of(Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), Component.translatable("block.kaleidoscope_cookery.stockpot")),
      CommonComponents.SPACE
   );
   public static final int WIDTH = 176;
   public static final int HEIGHT = 102;

   public CategoryIdentifier<ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay> getCategoryIdentifier() {
      return ID;
   }

   public List<Widget> setupDisplay(ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int startX = bounds.x;
      int startY = bounds.y;
      widgets.add(Widgets.createRecipeBase(bounds));
      widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0.0F, 0.0F, 176, 102));
      widgets.add(
         Widgets.withTranslate(
            Widgets.createDrawableWidget(
               (guiGraphics, mouseX, mouseY, v) -> this.drawCenteredString(guiGraphics, Component.translatable("jei.kaleidoscope_cookery.flex_recipe"), 88, 90)
            ),
            startX,
            startY,
            0.0
         )
      );
      if (!display.soupBase.isEmpty()) {
         widgets.add(Widgets.createSlot(new Point(startX + 72, startY + 61)).entries(display.soupBase).disableBackground().markInput());
      }

      List<EntryIngredient> inputs = display.getInputEntries();

      for (int i = 0; i < inputs.size(); i++) {
         int xOffset = i % 3 * 18 + 15;
         int yOffset = i / 3 * 18 + 25;
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

   public int getDisplayWidth(ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay display) {
      return 176;
   }

   public int getDisplayHeight() {
      return 102;
   }

   public Component getTitle() {
      return TITLE;
   }

   public Renderer getIcon() {
      return EntryStacks.of((ItemLike)ModItems.STOCKPOT.get());
   }

   public static void registerCategories(CategoryRegistry registry) {
      registry.add(new ReiFlexStockpotRecipeCategory());
      registry.addWorkstations(ID, new EntryIngredient[]{ReiUtil.ofItem((Item)ModItems.STOCKPOT.get()), ReiUtil.ofItem((Item)ModItems.STOCKPOT_LID.get())});
   }

   public static void registerDisplays(DisplayRegistry registry) {
      registry.getRecipeManager().getAllRecipesFor(ModRecipes.FLEX_STOCKPOT_RECIPE).forEach(holder -> {
         FlexStockpotRecipe r = (FlexStockpotRecipe)holder.value();
         List<EntryIngredient> inputs = ReiUtil.ofIngredients(r.getIngredients());
         List<EntryIngredient> output = ReiUtil.ofItemStacks(r.getResultItem(RegistryAccess.EMPTY));
         EntryIngredient carrier = r.carrier().isEmpty() ? EntryIngredient.empty() : ReiUtil.ofIngredient(r.carrier());
         ISoupBase soupBase = SoupBaseManager.getSoupBase(r.soupBase());
         if (soupBase == null) {
            throw new RuntimeException("No soup found for " + r.soupBase());
         } else {
            ItemStack displayStack = soupBase.getDisplayStack();
            EntryIngredient soupBaseEntry = displayStack.isEmpty() ? EntryIngredient.empty() : ReiUtil.ofItemStack(displayStack);
            registry.add(new ReiFlexStockpotRecipeCategory.FlexStockpotRecipeDisplay(holder.id(), inputs, output, carrier, soupBaseEntry));
         }
      });
   }

   public static class FlexStockpotRecipeDisplay extends BasicDisplay {
      public final EntryIngredient carrier;
      public final EntryIngredient soupBase;

      public FlexStockpotRecipeDisplay(
         Identifier location, List<EntryIngredient> inputs, List<EntryIngredient> outputs, EntryIngredient carrier, EntryIngredient soupBase
      ) {
         super(inputs, outputs, Optional.of(location));
         this.carrier = carrier;
         this.soupBase = soupBase;
      }

      public CategoryIdentifier<?> getCategoryIdentifier() {
         return ReiFlexStockpotRecipeCategory.ID;
      }
   }
}
