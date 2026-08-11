package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.text.DecimalFormat;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class MillstoneRecipeCategory implements IRecipeCategory<RecipeHolder<MillstoneRecipe>> {
   public static final RecipeType<RecipeHolder<MillstoneRecipe>> TYPE = RecipeType.createRecipeHolderType(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone")
   );
   private static final DecimalFormat FORMAT = new DecimalFormat("0.##%");
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/millstone.png");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.millstone");
   public static final int WIDTH = 196;
   public static final int HEIGHT = 95;
   private final IDrawable bgDraw;
   private final IDrawable iconDraw;

   public MillstoneRecipeCategory(IGuiHelper guiHelper) {
      this.bgDraw = guiHelper.createDrawable(BG, 0, 0, 196, 95);
      this.iconDraw = guiHelper.createDrawableItemLike((ItemLike)ModItems.MILLSTONE.get());
   }

   public static List<RecipeHolder<MillstoneRecipe>> getRecipes() {
      return RuntimeRecipeCache.getRecipes(ModRecipes.MILLSTONE_RECIPE);
   }

   public static IRecipeSlotRichTooltipCallback addChanceTooltip(RandomOutput output) {
      return (view, tooltip) -> {
         float chance = output.chance();
         if (chance != 1.0F) {
            tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.chance", new Object[]{FORMAT.format(chance)}).withStyle(ChatFormatting.GOLD));
         }
      };
   }

   public void draw(RecipeHolder<MillstoneRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
      this.bgDraw.draw(guiGraphics);
   }

   public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MillstoneRecipe> holder, IFocusGroup focuses) {
      MillstoneRecipe recipe = (MillstoneRecipe)holder.value();
      Ingredient input = recipe.ingredient();
      List<RandomOutput> outputs = recipe.results();
      ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 69, 39).addIngredients(input)).setStandardSlotBackground();
      RandomOutput output = outputs.get(0);
      ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 150, 47).addItemStack(output.stack()))
         .setOutputSlotBackground()
         .addRichTooltipCallback(addChanceTooltip(output));
      if (outputs.size() > 1) {
         for (int i = 1; i < outputs.size(); i++) {
            RandomOutput randomOutput = outputs.get(i);

            int x = switch (i) {
               case 2 -> 128;
               case 3 -> 172;
               default -> 150;
            };
            int y = 20;
            ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(randomOutput.stack()))
               .setStandardSlotBackground()
               .addRichTooltipCallback(addChanceTooltip(randomOutput));
         }
      }
   }

   public RecipeType<RecipeHolder<MillstoneRecipe>> getRecipeType() {
      return TYPE;
   }

   public Component getTitle() {
      return TITLE;
   }

   public int getWidth() {
      return 196;
   }

   public int getHeight() {
      return 95;
   }

   @Nullable
   public IDrawable getIcon() {
      return this.iconDraw;
   }
}
