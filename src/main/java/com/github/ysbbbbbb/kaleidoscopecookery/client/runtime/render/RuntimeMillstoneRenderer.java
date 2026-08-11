package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeMillstoneModel;
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
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class RuntimeMillstoneRenderer implements BlockEntityRenderer<MillstoneBlockEntity, RuntimeMillstoneRenderer.State> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/block/millstone.png");
   private static final float DEGREES_TO_RADIANS = (float)(Math.PI / 180.0);

   private final ItemModelResolver itemModelResolver;
   private final RuntimeMillstoneModel model;

   public RuntimeMillstoneRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
      this.model = new RuntimeMillstoneModel(context.bakeLayer(RuntimeMillstoneModel.LAYER_LOCATION));
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      MillstoneBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(MillstoneBlock.FACING);
      int facingDegrees = state.facing.get2DDataValue() * 90;
      float rotation = blockEntity.hasEntity() && blockEntity.getLevel() != null
         ? facingDegrees + blockEntity.getRotation(blockEntity.getLevel(), partialTicks)
         : facingDegrees + blockEntity.getCacheRot();
      state.wheelRotation = -rotation * DEGREES_TO_RADIANS;
      state.rollRotation = blockEntity.hasEntity() ? rotation * DEGREES_TO_RADIANS : 0.0F;
      state.stickRotation = blockEntity.hasEntity() ? -blockEntity.getLiftAngle() * DEGREES_TO_RADIANS : 0.0F;

      ItemStack stack = !blockEntity.getOutputs().getStackInSlot(0).isEmpty()
         ? blockEntity.getOutputs().getStackInSlot(0)
         : blockEntity.getInput();
      state.itemCount = Math.min(stack.getCount(), 8);
      state.seed = blockEntity.getBlockPos().asLong();
      this.itemModelResolver.updateForTopItem(
         state.item,
         stack,
         ItemDisplayContext.FIXED,
         blockEntity.getLevel(),
         null,
         (int)state.seed
      );
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      int facingDegrees = state.facing.get2DDataValue() * 90;
      poseStack.pushPose();
      poseStack.translate(0.5F, 1.5F, 0.5F);
      poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
      poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - facingDegrees));
      collector.submitModel(
         this.model,
         new RuntimeMillstoneModel.State(state.wheelRotation, state.rollRotation, state.stickRotation),
         poseStack,
         TEXTURE,
         state.lightCoords,
         OverlayTexture.NO_OVERLAY,
         0,
         state.breakProgress
      );
      poseStack.popPose();

      if (!state.item.isEmpty()) {
         RandomSource random = RandomSource.create(state.seed);
         for (int i = 0; i < state.itemCount; i++) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.875F, 0.0F);
            poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45.0F + random.nextInt(15)), 0.5F, 0.0F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextInt(20)));
            poseStack.mulPose(Axis.XN.rotationDegrees(80.0F + random.nextInt(20)));
            poseStack.scale(0.65F, 0.65F, 0.65F);
            state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
         }
      }
   }

   @Override
   public boolean shouldRenderOffScreen() {
      return true;
   }

   public static final class State extends BlockEntityRenderState {
      private final ItemStackRenderState item = new ItemStackRenderState();
      private Direction facing = Direction.NORTH;
      private int itemCount;
      private long seed;
      private float wheelRotation;
      private float rollRotation;
      private float stickRotation;
   }
}
