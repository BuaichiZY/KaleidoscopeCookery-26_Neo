package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeKitchenShelfOverlay implements GuiLayer {
    @SubscribeEvent
    public static void register(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR,
            Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "kitchenshelf_info"), new RuntimeKitchenShelfOverlay());
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !(minecraft.hitResult instanceof BlockHitResult hit)
            || hit.getType() != HitResult.Type.BLOCK
            || !player.level().getBlockState(hit.getBlockPos()).is(ModBlocks.KITCHEN_SHELF.get())) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        BlockEntity entity = player.level().getBlockEntity(pos);
        if (!(entity instanceof KitchenShelfBlockEntity shelf)) {
            return;
        }

        ItemStack stored = shelf.getStoredStack();
        if (!stored.isEmpty()) {
            int x = graphics.guiWidth() / 2 - 8;
            int y = graphics.guiHeight() / 2 + 8;
            graphics.fakeItem(stored, x, y);
            graphics.itemDecorations(minecraft.font, stored, x, y);
        }
    }
}
