package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
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
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class RuntimeChoppingBoardRenderer
   implements BlockEntityRenderer<ChoppingBoardBlockEntity, RuntimeChoppingBoardRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeChoppingBoardRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      ChoppingBoardBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(ChoppingBoardBlock.FACING);
      ItemStack displayStack = blockEntity.getCurrentCutStack().copy();
      Identifier modelId = blockEntity.getModelId();
      if (!displayStack.isEmpty() && modelId != null) {
         int stage = Math.min(blockEntity.getCurrentCutCount(), blockEntity.getMaxCutCount());
         displayStack.set(
            DataComponents.ITEM_MODEL,
            Identifier.fromNamespaceAndPath(modelId.getNamespace(), "chopping_board/" + modelId.getPath() + "/" + stage)
         );
      }
      this.itemModelResolver.updateForTopItem(
         state.item,
         displayStack,
         ItemDisplayContext.NONE,
         blockEntity.getLevel(),
         null,
         (int)blockEntity.getBlockPos().asLong()
      );
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.item.isEmpty()) {
         return;
      }

      poseStack.pushPose();
      poseStack.translate(0.5F, 0.0F, 0.5F);
      poseStack.mulPose(Axis.YP.rotationDegrees(state.facing.get2DDataValue() * 90.0F));
      // ItemStackRenderState centers baked item models by another half block.
      // Compensate here so the original block-space chopping model sits on the board.
      poseStack.translate(0.0F, 0.625F, 0.0F);
      state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private final ItemStackRenderState item = new ItemStackRenderState();
      private Direction facing = Direction.NORTH;
   }
}
