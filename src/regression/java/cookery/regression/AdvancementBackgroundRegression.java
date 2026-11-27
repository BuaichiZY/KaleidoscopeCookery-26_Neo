package cookery.regression;

import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;

/** Exercises the synchronized display asset and the real advancements screen. */
final class AdvancementBackgroundRegression {
    private static final Identifier ROOT = Identifier.parse("kaleidoscope_cookery:root");
    private static final Identifier TEXTURE = Identifier.parse("kaleidoscope_cookery:textures/advancement/background.png");
    private static boolean requested, opened;

    static void prepare(Minecraft mc, int readyTicks) {
        if (!requested && readyTicks >= 100) {
            requested = true;
            var server = mc.getSingleplayerServer();
            ClientRegression.check(server != null, "Background regression needs integrated server");
            var uuid = mc.player.getUUID();
            server.execute(() -> {
                var player = server.getPlayerList().getPlayer(uuid);
                var root = server.getAdvancements().get(ROOT);
                if (player != null && root != null) {
                    for (var criterion : root.value().criteria().keySet()) player.getAdvancements().award(root, criterion);
                }
            });
        }
        if (!opened && readyTicks >= 160) {
            var advancements = mc.player.connection.getAdvancements();
            var root = advancements.get(ROOT);
            if (root != null) {
                mc.setScreen(new AdvancementsScreen(advancements));
                advancements.setSelectedTab(root, true);
                opened = true;
            }
        }
    }

    static void verify(Minecraft mc) throws Exception {
        ClientRegression.check(opened && mc.screen instanceof AdvancementsScreen, "Cookery advancement page did not open");
        var holder = mc.player.connection.getAdvancements().get(ROOT);
        var display = holder.value().display().orElseThrow();
        var background = display.getBackground().orElseThrow();
        ClientRegression.check(background.id().equals(Identifier.parse("kaleidoscope_cookery:advancement/background")),
            "Advancement background must use a logical asset id");
        ClientRegression.check(background.texturePath().equals(TEXTURE), "Doubled textures prefix or png extension: " + background.texturePath());
        try (var contents = TextureContents.load(mc.getResourceManager(), background.texturePath())) {
            var image = contents.image();
            ClientRegression.check(image.getWidth() == 16 && image.getHeight() == 16, "Unexpected advancement background dimensions");
            boolean missingPink = false;
            for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) {
                if ((image.getPixel(x, y) & 0xFFFFFF) == 0xFF00FF) missingPink = true;
            }
            ClientRegression.check(!missingPink, "Advancement background decoded as missing-texture checkerboard");
        }
        var field = AdvancementsScreen.class.getDeclaredField("selectedTab");
        field.setAccessible(true);
        ClientRegression.check(field.get(mc.screen) != null, "Cookery advancement tab was not selected");
        ClientRegression.log("ADVANCEMENT_BACKGROUND logical id resolves to original 16x16 texture; actual page opened and selected");
    }

    static void captureAndStop(Minecraft mc) {
        Screenshot.takeScreenshot(mc.getMainRenderTarget(), image -> {
            try (image) {
                var output = Path.of("cookery-advancement-background.png");
                image.writeToFile(output);
                System.out.println("COOKERY_BACKGROUND_SCREENSHOT " + output.toAbsolutePath());
            } catch (Exception error) {
                error.printStackTrace();
            } finally {
                mc.execute(mc::stop);
            }
        });
    }
}
