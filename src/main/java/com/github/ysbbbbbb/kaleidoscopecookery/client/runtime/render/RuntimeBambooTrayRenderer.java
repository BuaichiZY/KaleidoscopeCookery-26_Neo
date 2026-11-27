package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class RuntimeBambooTrayRenderer implements BlockEntityRenderer<BambooTrayBlockEntity, RuntimeBambooTrayRenderer.State> {
   private static final double[][] POSITIONS = {{0.25, 0.25}, {0.75, 0.25}, {0.25, 0.75}, {0.75, 0.75}};
   private final ItemModelResolver itemModelResolver;

   public RuntimeBambooTrayRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   public State createRenderState() { return new State(); }

   public void extractRenderState(BambooTrayBlockEntity tray, State state, float partialTicks, Vec3 cameraPosition,
                                  ModelFeatureRenderer.CrumblingOverlay breakProgress) {
      BlockEntityRenderer.super.extractRenderState(tray, state, partialTicks, cameraPosition, breakProgress);
      List<DisplayedItem> displayed = new ArrayList<>();
      for (int slot = 0; slot < tray.getItems().size(); slot++) {
         ItemStack stack = tray.getItems().get(slot);
         if (stack.isEmpty()) continue;
         int copies = stack.getCount() == 1 ? 1 : Math.min(5, (stack.getCount() - 1) / 16 + 2);
         RandomSource random = RandomSource.create(tray.getBlockPos().asLong() + slot * 31L);
         for (int copy = 0; copy < copies; copy++) {
            ItemStackRenderState item = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(item, stack, ItemDisplayContext.FIXED, tray.getLevel(), null, slot * 7 + copy);
            displayed.add(new DisplayedItem(item, slot, copy, (random.nextDouble() - 0.5) * 0.1,
               (random.nextDouble() - 0.5) * 0.1, random.nextFloat() * 360.0F));
         }
      }
      state.items = displayed;
   }

   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      for (DisplayedItem displayed : state.items) {
         poseStack.pushPose();
         poseStack.translate(POSITIONS[displayed.slot][0] + displayed.xOffset,
            0.14 + displayed.copy * 0.01 + displayed.slot * 0.005,
            POSITIONS[displayed.slot][1] + displayed.zOffset);
         poseStack.rotateDegrees(Axis.XP, 90.0F);
         poseStack.rotateDegrees(Axis.ZP, displayed.rotation);
         poseStack.scale(0.5F, 0.5F, 0.5F);
         displayed.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
   }

   private record DisplayedItem(ItemStackRenderState item, int slot, int copy, double xOffset, double zOffset, float rotation) {}
   public static final class State extends BlockEntityRenderState { private List<DisplayedItem> items = List.of(); }
}
