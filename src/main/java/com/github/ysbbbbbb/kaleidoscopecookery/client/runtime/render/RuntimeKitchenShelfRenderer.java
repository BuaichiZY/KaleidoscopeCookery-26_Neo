package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
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
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public final class RuntimeKitchenShelfRenderer
   implements BlockEntityRenderer<KitchenShelfBlockEntity, RuntimeKitchenShelfRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeKitchenShelfRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      KitchenShelfBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
      state.items = new ArrayList<>();
      ItemStack stored = blockEntity.getStoredStack();
      if (stored.isEmpty()) return;
      ItemStackRenderState item = new ItemStackRenderState();
      this.itemModelResolver.updateForTopItem(item, stored.copyWithCount(1), ItemDisplayContext.FIXED,
         blockEntity.getLevel(), null, (int)blockEntity.getBlockPos().asLong());
      for (int index = 0; index < Math.min(16, stored.getCount()); index++) {
         state.items.add(new RackItem(item, index));
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.items.isEmpty()) {
         return;
      }
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.0F, 0.5F);
      poseStack.mulPose(Axis.YN.rotationDegrees(state.facing.toYRot() + 180.0F));
      for (RackItem rackItem : state.items) {
         poseStack.pushPose();
         int slot = rackItem.index() % 8;
         poseStack.translate(slot % 2 == 0 ? -0.25 : 0.25, 0.7 - rackItem.index() / 8 * 0.5, 0.3 - slot / 2 * 0.2);
         poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
         poseStack.scale(0.5F, 0.5F, 0.5F);
         rackItem.item().submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
      poseStack.popPose();
   }

   private record RackItem(ItemStackRenderState item, int index) {
   }

   public static final class State extends BlockEntityRenderState {
      private List<RackItem> items = List.of();
      private Direction facing = Direction.NORTH;
   }
}
