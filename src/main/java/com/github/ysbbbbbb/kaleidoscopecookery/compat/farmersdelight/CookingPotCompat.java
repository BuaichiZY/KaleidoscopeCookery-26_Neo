package com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.StockpotMatchRecipeEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeLookup;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
public final class CookingPotCompat {
    static void getTransformRecipeForJei(Level level, List<RecipeHolder<StockpotRecipe>> recipes) {
        if (level == null) return;
        var source = level instanceof ServerLevel serverLevel
            ? RecipeLookup.byType(serverLevel.recipeAccess(), ModRecipeTypes.COOKING.get())
            : com.github.ysbbbbbb.kaleidoscopecookery.compat.jei.RuntimeRecipeCache.getRecipes(ModRecipeTypes.COOKING.get());
        for (var holder : source) recipes.add(transformRecipe(holder));
    }
    public static RecipeHolder<StockpotRecipe> transformRecipe(RecipeHolder<CookingPotRecipe> holder) {
        var recipe = holder.value();
        return new RecipeHolder<>(holder.id(), new StockpotRecipe(recipe.getIngredients(), recipe.getResultItem(),
            recipe.getCookTime(), recipe.getOutputContainer()));
    }
    static void afterStockpotRecipeMatch(StockpotMatchRecipeEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)
            || !event.getRawOutput().equals(StockpotRecipeSerializer.EMPTY_ID)
            || !event.getInput().getSoupBase().equals(StockpotRecipeSerializer.DEFAULT_SOUP_BASE)) return;
        var inputs = event.getInput().getInputs().stream().filter(stack -> !stack.isEmpty()).toList();
        if (inputs.size() > CookingPotRecipe.INPUT_SLOTS) return;
        var inventory = new ItemStackHandler(CookingPotRecipe.INPUT_SLOTS);
        for (int i = 0; i < inputs.size(); i++) inventory.setStackInSlot(i, inputs.get(i));
        level.recipeAccess().getRecipeFor(ModRecipeTypes.COOKING.get(), new RecipeWrapper(inventory), level)
            .ifPresent(holder -> event.setOutput(transformRecipe(holder)));
    }
}
