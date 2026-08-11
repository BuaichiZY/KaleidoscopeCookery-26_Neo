package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

public final class RuntimeShawarmaSpitRenderer
   implements BlockEntityRenderer<ShawarmaSpitBlockEntity, RuntimeShawarmaSpitRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeShawarmaSpitRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      ShawarmaSpitBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      ItemStack stack = blockEntity.cookingItem.isEmpty() ? blockEntity.cookedItem : blockEntity.cookingItem;
      state.count = Math.min(stack.getCount(), 8);
      state.half = blockEntity.getBlockState().getValue(ShawarmaSpitBlock.HALF);
      state.rotation = blockEntity.getBlockState().getValue(BlockStateProperties.POWERED)
         ? (System.currentTimeMillis() % 3600L) / 10.0F
         : 0.0F;
      this.itemModelResolver.updateForTopItem(
         state.item,
         stack,
         ItemDisplayContext.FIXED,
         blockEntity.getLevel(),
         null,
         (int)blockEntity.getBlockPos().asLong()
      );
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.count == 0 || state.item.isEmpty()) {
         return;
      }

      poseStack.pushPose();
      poseStack.rotateAround(Axis.YP.rotationDegrees(state.rotation), 0.5F, 0.0F, 0.5F);
      if (state.half == DoubleBlockHalf.UPPER) {
         poseStack.translate(0.25F, 0.5F, 0.25F);
      } else {
         poseStack.translate(0.25F, 0.875F, 0.25F);
      }

      for (int i = 0; i < state.count; i++) {
         poseStack.pushPose();
         poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45.0F), 0.25F, 0.0F, 0.25F);
         poseStack.scale(0.65F, 0.65F, 0.65F);
         state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }

      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private final ItemStackRenderState item = new ItemStackRenderState();
      private DoubleBlockHalf half = DoubleBlockHalf.LOWER;
      private int count;
      private float rotation;
   }
}
