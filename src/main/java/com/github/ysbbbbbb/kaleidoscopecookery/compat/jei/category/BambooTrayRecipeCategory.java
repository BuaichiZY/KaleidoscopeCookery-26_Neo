package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class BambooTrayRecipeCategory implements IRecipeCategory<RecipeHolder<BambooTrayRecipe>> {
   public static final RecipeType<RecipeHolder<BambooTrayRecipe>> TYPE = RecipeType.createRecipeHolderType(
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "bamboo_tray"));
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/bamboo_tray.png");
   private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.bamboo_tray");
   private final IDrawable background;
   private final IDrawable icon;

   public BambooTrayRecipeCategory(IGuiHelper helper) {
      this.background = helper.createDrawable(BG, 0, 0, 176, 78);
      this.icon = helper.createDrawableItemStack(((Item)ModItems.BAMBOO_TRAY.get()).getDefaultInstance());
   }

   public static List<RecipeHolder<BambooTrayRecipe>> getRecipes() {
      return RuntimeRecipeCache.getRecipesByClass(BambooTrayRecipe.class);
   }

   @Override
   public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BambooTrayRecipe> holder, IFocusGroup focuses) {
      BambooTrayRecipe recipe = holder.value();
      builder.addSlot(RecipeIngredientRole.INPUT, 41, 27).addIngredients(recipe.getIngredient());
      builder.addSlot(RecipeIngredientRole.OUTPUT, 131, 29).addItemStack(recipe.getResult());
   }

   @Override public void draw(RecipeHolder<BambooTrayRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) { this.background.draw(graphics); }
   @Override public RecipeType<RecipeHolder<BambooTrayRecipe>> getRecipeType() { return TYPE; }
   @Override public Component getTitle() { return TITLE; }
   @Override public int getWidth() { return 176; }
   @Override public int getHeight() { return 78; }
   @Override public @Nullable IDrawable getIcon() { return this.icon; }
}
