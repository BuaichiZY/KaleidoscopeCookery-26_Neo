package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class RuntimeSteamerRenderer implements BlockEntityRenderer<SteamerBlockEntity, RuntimeSteamerRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeSteamerRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      SteamerBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.hiddenByLid = blockEntity.getBlockState().getValue(SteamerBlock.HAS_LID);
      state.items = new ArrayList<>();
      int visibleSlots = blockEntity.getBlockState().getValue(SteamerBlock.HALF) ? 4 : 8;
      for (int slot = 0; slot < visibleSlots; slot++) {
         ItemStack stack = blockEntity.getItems().get(slot);
         if (!stack.isEmpty()) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
               itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null,
               (int)(blockEntity.getBlockPos().asLong() + slot)
            );
            state.items.add(new SteamerItem(itemState, slot));
         }
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.hiddenByLid) {
         return;
      }
      for (SteamerItem steamerItem : state.items) {
         int slot = steamerItem.slot();
         float x = (slot % 2) * 0.3F + 0.35F;
         float y = (slot / 4) * 0.5F + 0.275F + (slot % 4) * 0.004F;
         float z = ((slot / 2) % 2) * 0.3F + 0.35F;
         poseStack.pushPose();
         poseStack.translate(x, y, z);
         poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
         poseStack.mulPose(Axis.ZP.rotationDegrees(slot * 17.0F));
         poseStack.scale(0.5F, 0.5F, 0.5F);
         steamerItem.item().submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
   }

   private record SteamerItem(ItemStackRenderState item, int slot) {
   }

   public static final class State extends BlockEntityRenderState {
      private List<SteamerItem> items = List.of();
      private boolean hiddenByLid;
   }
}
