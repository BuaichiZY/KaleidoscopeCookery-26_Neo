package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeTeapotModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public final class RuntimeTeapotRenderer implements BlockEntityRenderer<TeapotBlockEntity, RuntimeTeapotRenderer.State> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/block/teapot.png"
   );
   private final RuntimeTeapotModel model;

   public RuntimeTeapotRenderer(BlockEntityRendererProvider.Context context) {
      this.model = new RuntimeTeapotModel(context.bakeLayer(RuntimeTeapotModel.LAYER_LOCATION));
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      TeapotBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(TeapotBlock.FACING);
      state.variant = blockEntity.getBlockState().getValue(TeapotBlock.VARIANT);
      state.age = blockEntity.getLevel() == null ? 0.0F : blockEntity.getLevel().getGameTime() + partialTicks;
      state.boiling = blockEntity.getStatus() == 2
         && blockEntity.getLevel() != null
         && blockEntity.hasHeatSource(blockEntity.getLevel());
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      poseStack.pushPose();
      poseStack.translate(0.5F, 1.5F, 0.5F);
      poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
      poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - state.facing.get2DDataValue() * 90.0F));
      collector.submitModel(
         this.model, new RuntimeTeapotModel.State(state.variant, state.boiling, state.age),
         poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress
      );
      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private Direction facing = Direction.NORTH;
      private int variant;
      private float age;
      private boolean boiling;
   }
}
