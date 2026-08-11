package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class PotRecipeCategory implements IRecipeCategory<RecipeHolder<PotRecipe>> {
   public static final RecipeType<RecipeHolder<PotRecipe>> TYPE = RecipeType.createRecipeHolderType(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot")
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/pot.png");
   private static final Component TITLE = ComponentUtils.formatList(
      List.of(Component.translatable("jei.kaleidoscope_cookery.strict_recipe"), Component.translatable("block.kaleidoscope_cookery.pot")),
      CommonComponents.SPACE
   );
   public static final int WIDTH = 176;
   public static final int HEIGHT = 102;
   private final IDrawable bgDraw;
   private final IDrawable slotDraw;
   private final IDrawable iconDraw;

   public PotRecipeCategory(IGuiHelper guiHelper) {
      this.slotDraw = guiHelper.getSlotDrawable();
      this.bgDraw = guiHelper.createDrawable(BG, 0, 0, 176, 102);
      this.iconDraw = guiHelper.createDrawableItemStack(((Item)ModItems.POT.get()).getDefaultInstance());
   }

   public static List<RecipeHolder<PotRecipe>> getRecipes() {
      return RuntimeRecipeCache.getRecipes(ModRecipes.POT_RECIPE);
   }

   public void draw(RecipeHolder<PotRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
      this.bgDraw.draw(guiGraphics);
      Component type = Component.translatable("jei.kaleidoscope_cookery.strict_recipe");
      this.drawCenteredString(guiGraphics, type, 88, 5);
      Component stirFryCount = Component.translatable("jei.kaleidoscope_cookery.pot.stir_fry_count", new Object[]{((PotRecipe)recipe.value()).stirFryCount()});
      this.drawCenteredString(guiGraphics, stirFryCount, 88, 85);
   }

   private void drawCenteredString(GuiGraphicsExtractor guiGraphics, Component text, int centerX, int y) {
      Font font = Minecraft.getInstance().font;
      guiGraphics.centeredText(font, text, centerX, y, 5592405);
   }

   public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PotRecipe> recipe, IFocusGroup focuses) {
      NonNullList<Ingredient> inputs = ((PotRecipe)recipe.value()).getIngredients();
      ItemStack output = ((PotRecipe)recipe.value()).result();

      for (int i = 0; i < inputs.size(); i++) {
         int xOffset = i % 3 * 18 + 15;
         int yOffset = i / 3 * 18 + 24;
         ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, xOffset, yOffset).addIngredients((Ingredient)inputs.get(i)))
            .setBackground(this.slotDraw, -1, -1);
      }

      if (!ModRecipes.isEmptyIngredient(((PotRecipe)recipe.value()).carrier())) {
         builder.addSlot(RecipeIngredientRole.INPUT, 133, 18).addIngredients(((PotRecipe)recipe.value()).carrier());
      }

      ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 143, 60).addItemStack(output)).setBackground(this.slotDraw, -1, -1);
   }

   public RecipeType<RecipeHolder<PotRecipe>> getRecipeType() {
      return TYPE;
   }

   public Component getTitle() {
      return TITLE;
   }

   public int getWidth() {
      return 176;
   }

   public int getHeight() {
      return 102;
   }

   @Nullable
   public IDrawable getIcon() {
      return this.iconDraw;
   }
}
