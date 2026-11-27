package cookery.regression;

public final class ReiRegression {
    @SuppressWarnings({"rawtypes", "unchecked"})
    static void verify() {
        var categories = me.shedaniel.rei.api.client.registry.category.CategoryRegistry.getInstance().stream()
            .filter(c -> c.getIdentifier().getNamespace().equals("kaleidoscope_cookery")).toList();
        ClientRegression.check(categories.size() == 8, "REI category count=" + categories.size());
        for (var config : categories) {
            var displays = me.shedaniel.rei.api.client.registry.display.DisplayRegistry.getInstance().get(config.getCategoryIdentifier());
            ClientRegression.check(!displays.isEmpty(), "REI empty category " + config.getIdentifier());
            me.shedaniel.rei.api.client.registry.display.DisplayCategory category = config.getCategory();
            for (var display : displays) {
                var widgets = category.setupDisplay(display, new me.shedaniel.math.Rectangle(0, 0, category.getDisplayWidth(display), category.getDisplayHeight()));
                ClientRegression.check(!widgets.isEmpty(), "REI failed display layout " + config.getIdentifier());
                for (var ingredient : display.getInputEntries()) {
                    for (var entry : ingredient) {
                        if (entry.getValue() instanceof net.minecraft.world.item.ItemStack stack) {
                            ClientRegression.check(!stack.is(net.minecraft.world.item.Items.STRUCTURE_VOID), "REI leaked empty ingredient placeholder");
                        }
                    }
                }
            }
            ClientRegression.log("REI " + config.getIdentifier() + " recipes/layouts=" + displays.size());
        }
    }
}
