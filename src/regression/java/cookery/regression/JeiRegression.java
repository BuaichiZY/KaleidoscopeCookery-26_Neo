package cookery.regression;

@mezz.jei.api.JeiPlugin
public final class JeiRegression implements mezz.jei.api.IModPlugin {
    private static mezz.jei.api.runtime.IJeiRuntime runtime;
    public net.minecraft.resources.Identifier getPluginUid() {
        return net.minecraft.resources.Identifier.parse("cookery_regression:jei");
    }
    public void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime value) { runtime = value; }
    @SuppressWarnings({"rawtypes", "unchecked"})
    static void verify() {
        ClientRegression.check(runtime != null, "JEI runtime missing");
        var manager = runtime.getRecipeManager();
        var categories = manager.createRecipeCategoryLookup().includeHidden().get()
            .filter(c -> c.getRecipeType().getUid().getNamespace().equals("kaleidoscope_cookery")).toList();
        ClientRegression.check(categories.size() == 9, "JEI category count=" + categories.size());
        for (var category : categories) {
            var recipes = manager.createRecipeLookup(category.getRecipeType()).includeHidden().get().toList();
            ClientRegression.check(!recipes.isEmpty(), "JEI empty category " + category.getRecipeType().getUid());
            for (var recipe : recipes) {
                var layout = manager.createRecipeLayoutDrawable((mezz.jei.api.recipe.category.IRecipeCategory) category, recipe,
                    runtime.getJeiHelpers().getFocusFactory().getEmptyFocusGroup());
                ClientRegression.check(layout.isPresent(), "JEI failed recipe layout " + category.getRecipeType().getUid());
                if (recipe instanceof net.minecraft.world.item.crafting.RecipeHolder<?> holder
                    && holder.value() instanceof com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe chopping) {
                    var view = (mezz.jei.api.gui.IRecipeLayoutDrawable<?>) layout.get();
                    var slots = view.getRecipeSlotsView().getSlotViews(mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT);
                    ClientRegression.check(slots.size() == chopping.getResults().size(), "JEI chopping output count " + holder.id());
                    for (int i = 0; i < slots.size(); i++) {
                        var shown = slots.get(i).getDisplayedItemStack().orElseThrow();
                        ClientRegression.check(net.minecraft.world.item.ItemStack.matches(shown, chopping.getResults().get(i)),
                            "JEI chopping output mismatch " + holder.id());
                    }
                }
            }
            ClientRegression.log("JEI " + category.getRecipeType().getUid() + " recipes/layouts=" + recipes.size());
        }
    }
}
