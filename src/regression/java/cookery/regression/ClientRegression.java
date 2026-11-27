package cookery.regression;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = "cookery_regression", value = Dist.CLIENT)
public final class ClientRegression {
    static final List<String> report = new ArrayList<>();
    private static int readyTicks, ticks;
    private static boolean done;
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    static void log(String line) {
        report.add(line);
        System.out.println("COOKERY_CLIENT " + line);
    }
    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (done) return;
        var mc = Minecraft.getInstance();
        ticks++;
        if (ticks == 20) log("LOADING_ISSUES " + net.neoforged.fml.ModLoader.getLoadingIssues());
        if (ticks == 40 && mc.gui.screen() instanceof net.neoforged.neoforge.client.gui.LoadingErrorScreen screen) {
            if (net.neoforged.fml.ModLoader.getLoadingErrors().isEmpty()) {
                try {
                    var field = screen.getClass().getDeclaredField("nextScreenTask");
                    field.setAccessible(true);
                    ((Runnable) field.get(screen)).run();
                    log("CONTINUED non-fatal dependency warnings");
                } catch (ReflectiveOperationException error) {
                    throw new RuntimeException("Could not continue past warning screen", error);
                }
            } else {
                log("FAIL loader screen: " + net.neoforged.fml.ModLoader.getLoadingIssues());
                done = true;
                try { Files.write(Path.of("cookery-client-result.txt"), report); } catch (Exception error) { error.printStackTrace(); }
                mc.stop();
                return;
            }
        }
        if (ticks % 200 == 0) System.out.println("COOKERY_CLIENT state ticks=" + ticks + " ready=" + readyTicks + " screen=" + mc.gui.screen());
        if (mc.level != null && mc.player != null) readyTicks++;
        if (mc.level != null && mc.player != null) AdvancementBackgroundRegression.prepare(mc, readyTicks);
        if (readyTicks < 240 && ticks < 6000) return;
        done = true;
        try {
            check(mc.level != null && mc.player != null, "Quick play world did not load");
            check(net.neoforged.fml.ModLoader.getLoadingErrors().isEmpty(), "Loader reported errors");
            verifyTranslations();
            AdvancementBackgroundRegression.verify(mc);
            var position = mc.player.blockPosition();
            var before = mc.level.getBlockState(position);
            var pot = new com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity(position,
                com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks.TEAPOT.get().defaultBlockState());
            // A client prediction must not even ask for the server-side drops.
            var status = pot.getClass().getDeclaredField("status");
            status.setAccessible(true);
            for (int value = 0; value <= 2; value++) {
                status.setInt(pot, value);
                check(pot.takeTeapot(mc.level, null) == (value != 1), "Client teapot status prediction " + value);
                check(mc.level.getBlockState(position).equals(before), "Client teapot pickup mutated world");
            }
            log("VERSION_1_5_1 client teapot prediction for all states has no world/inventory side effects");
            var bag = new net.minecraft.world.item.ItemStack(com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.TRANSMUTATION_LUNCH_BAG.get());
            var items = com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem.getItems(bag);
            items.setStackInSlot(0, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.BREAD, 5));
            com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem.setItems(bag, items);
            mc.player.getFoodData().setFoodLevel(10);
            bag.getItem().finishUsingItem(bag, mc.level, mc.player);
            check(com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem.getItems(bag).getStackInSlot(0).getCount() == 5,
                "Lunch bag client prediction consumed food");
            for (String key : new String[] {"enchantment.kaleidoscope_cookery.quick_knife", "enchantment.kaleidoscope_cookery.sweep",
                "tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line1", "tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line2",
                "tooltip.kaleidoscope_cookery.transmutation_lunch_bag.line3"}) {
                check(net.minecraft.locale.Language.getInstance().has(key), "Missing 1.6.0 translation " + key);
            }
            log("VERSION_1_6_0 lunch bag client inventory preserved; enchantment and bag translations resolved");
            if (ModList.get().isLoaded("jei")) JeiRegression.verify();
            log("PASS joined world; jade=" + ModList.get().isLoaded("jade") + "; farmersdelight=" + ModList.get().isLoaded("farmersdelight"));
        } catch (Throwable error) {
            log("FAIL " + error);
            error.printStackTrace();
        } finally {
            try { Files.write(Path.of("cookery-client-result.txt"), report); }
            catch (Exception error) { error.printStackTrace(); }
            AdvancementBackgroundRegression.captureAndStop(mc);
        }
    }
    private static void verifyTranslations() {
        int checked = 0;
        for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
            var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
            if (!"kaleidoscope_cookery".equals(id.getNamespace())) continue;
            String key = item.getDescriptionId();
            check(net.minecraft.locale.Language.getInstance().has(key),
                "Missing item translation " + id + " -> " + key);
            checked++;
        }
        var recipe = net.minecraft.core.registries.BuiltInRegistries.ITEM
            .getValue(net.minecraft.resources.Identifier.parse("kaleidoscope_cookery:recipe_item"));
        check("block.kaleidoscope_cookery.recipe_block".equals(recipe.getDescriptionId()),
            "Recipe item does not use the upstream recipe block translation");
        log("TRANSLATIONS all registered item names resolved=" + checked);
    }

}
