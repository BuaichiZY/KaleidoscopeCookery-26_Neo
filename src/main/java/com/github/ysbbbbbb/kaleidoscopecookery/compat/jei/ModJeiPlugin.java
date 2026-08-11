package com.github.ysbbbbbb.kaleidoscopecookery.compat.jei;

import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.ChoppingBoardRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.FlexPotRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.FlexStockpotRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.MillstoneRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.PotRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.SteamerRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.StockpotRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.category.TeapotRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.RiceBowlRecipeMaker;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
   private static final Identifier UID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "jei");

   public void registerCategories(IRecipeCategoryRegistration registration) {
      registration.addRecipeCategories(new IRecipeCategory[]{new PotRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new FlexPotRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new ChoppingBoardRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new StockpotRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new FlexStockpotRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new MillstoneRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new SteamerRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new TeapotRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
   }

   public void registerRecipes(IRecipeRegistration registration) {
      registration.addRecipes(PotRecipeCategory.TYPE, PotRecipeCategory.getRecipes());
      registration.addRecipes(FlexPotRecipeCategory.TYPE, FlexPotRecipeCategory.getRecipes());
      registration.addRecipes(ChoppingBoardRecipeCategory.TYPE, ChoppingBoardRecipeCategory.getRecipes());
      registration.addRecipes(StockpotRecipeCategory.TYPE, StockpotRecipeCategory.getRecipes());
      registration.addRecipes(FlexStockpotRecipeCategory.TYPE, FlexStockpotRecipeCategory.getRecipes());
      registration.addRecipes(MillstoneRecipeCategory.TYPE, MillstoneRecipeCategory.getRecipes());
      registration.addRecipes(SteamerRecipeCategory.TYPE, SteamerRecipeCategory.getRecipes());
      registration.addRecipes(TeapotRecipeCategory.TYPE, TeapotRecipeCategory.getRecipes());
      registration.addRecipes(RecipeTypes.CRAFTING, RiceBowlRecipeMaker.createRecipes());
   }

   public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
      registration.addRecipeCatalyst((ItemLike)ModItems.POT.get(), new RecipeType[]{PotRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.POT.get(), new RecipeType[]{FlexPotRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.CHOPPING_BOARD.get(), new RecipeType[]{ChoppingBoardRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.STOCKPOT.get(), new RecipeType[]{StockpotRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.STOCKPOT.get(), new RecipeType[]{FlexStockpotRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.MILLSTONE.get(), new RecipeType[]{MillstoneRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.STEAMER.get(), new RecipeType[]{SteamerRecipeCategory.TYPE});
      registration.addRecipeCatalyst((ItemLike)ModItems.TEAPOT.get(), new RecipeType[]{TeapotRecipeCategory.TYPE});
   }

   public Identifier getPluginUid() {
      return UID;
   }
}
