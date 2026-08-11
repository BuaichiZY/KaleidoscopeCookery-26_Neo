package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class TeapotRecipeCategory implements IRecipeCategory<RecipeHolder<TeapotRecipe>> {
   public static final RecipeType<RecipeHolder<TeapotRecipe>> TYPE = RecipeType.createRecipeHolderType(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "teapot")
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/teapot.png");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.teapot");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 78;
   private final IDrawable bgDraw;
   private final IDrawable iconDraw;

   public TeapotRecipeCategory(IGuiHelper guiHelper) {
      this.bgDraw = guiHelper.createDrawable(BG, 0, 0, 176, 78);
      this.iconDraw = guiHelper.createDrawableItemLike((ItemLike)ModItems.TEAPOT.get());
   }

   public static List<RecipeHolder<TeapotRecipe>> getRecipes() {
      return RuntimeRecipeCache.getRecipes(ModRecipes.TEAPOT_RECIPE);
   }

   public void draw(RecipeHolder<TeapotRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
      this.bgDraw.draw(guiGraphics);
      Component brewTime = Component.translatable("jei.kaleidoscope_cookery.teapot.time", new Object[]{((TeapotRecipe)recipe.value()).time() / 20});
      this.drawCenteredString(guiGraphics, brewTime, 88, 70);
   }

   private void drawCenteredString(GuiGraphicsExtractor guiGraphics, Component text, int centerX, int y) {
      Font font = Minecraft.getInstance().font;
      guiGraphics.centeredText(font, text, centerX, y, 5592405);
   }

   public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<TeapotRecipe> holder, IFocusGroup focuses) {
      TeapotRecipe recipe = (TeapotRecipe)holder.value();
      List<ItemStack> inputs = recipe.ingredient().items().map(item -> new ItemStack(item, recipe.ingredientCount())).toList();
      ItemStack output = recipe.result().copyWithCount(12);
      Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(recipe.teaFluid());
      Item bucket = fluid.getBucket();
      builder.addSlot(RecipeIngredientRole.INPUT, 65, 3).setStandardSlotBackground().addItemLike(bucket);
      builder.addSlot(RecipeIngredientRole.INPUT, 83, 3).setStandardSlotBackground().addItemStacks(inputs);
      builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 30).addItemStack(output);
   }

   public RecipeType<RecipeHolder<TeapotRecipe>> getRecipeType() {
      return TYPE;
   }

   public Component getTitle() {
      return TITLE;
   }

   public int getWidth() {
      return 176;
   }

   public int getHeight() {
      return 78;
   }

   @Nullable
   public IDrawable getIcon() {
      return this.iconDraw;
   }
}
