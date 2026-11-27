package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TeaBannerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeTeaBannerModels;
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
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;

public final class RuntimeTeaBannerRenderer implements BlockEntityRenderer<TeaBannerBlockEntity, RuntimeTeaBannerRenderer.State> {
   private final RuntimeTeaBannerModels.Normal normal;
   private final RuntimeTeaBannerModels.Wall wall;
   private final RuntimeTeaBannerModels.Pattern pattern;
   public RuntimeTeaBannerRenderer(BlockEntityRendererProvider.Context context) {
      this.normal = new RuntimeTeaBannerModels.Normal(context.bakeLayer(RuntimeTeaBannerModels.Normal.LAYER));
      this.wall = new RuntimeTeaBannerModels.Wall(context.bakeLayer(RuntimeTeaBannerModels.Wall.LAYER));
      this.pattern = new RuntimeTeaBannerModels.Pattern(context.bakeLayer(RuntimeTeaBannerModels.Pattern.LAYER));
   }
   public State createRenderState() { return new State(); }
   public void extractRenderState(TeaBannerBlockEntity banner, State state, float partialTicks, Vec3 cameraPosition,
                                  ModelFeatureRenderer.CrumblingOverlay breakProgress) {
      BlockEntityRenderer.super.extractRenderState(banner, state, partialTicks, cameraPosition, breakProgress);
      state.facing = banner.getBlockState().getValue(TeaBannerBlock.FACING);
      state.wallMounted = banner.getBlockState().getValue(TeaBannerBlock.FACE) == AttachFace.WALL;
      long phase = banner.getBlockPos().getX() * 7L + banner.getBlockPos().getY() * 9L + banner.getBlockPos().getZ() * 13L;
      float time = phase + (banner.getLevel() == null ? 0 : banner.getLevel().getGameTime()) + partialTicks;
      state.wave = (-0.0125F + 0.01F * Mth.cos(Mth.TWO_PI * time / 100.0F)) * Mth.HALF_PI;
      state.baseTexture = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/entity/banner/" + banner.getColor().getName() + ".png");
      String name = TeaBannerBlockEntity.getPatternTexture(banner.getPatternItem());
      state.patternTexture = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/entity/pattern/" + (name == null ? "tea" : name) + ".png");
   }
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      RuntimeTeaBannerModels.State modelState = new RuntimeTeaBannerModels.State(state.wave);
      poseStack.pushPose();
      poseStack.translate(0.5, 1.5, 0.5);
      poseStack.mulPose(Axis.ZN.rotationDegrees(180));
      poseStack.mulPose(Axis.YN.rotationDegrees(180 - state.facing.get2DDataValue() * 90));
      if (state.wallMounted) poseStack.mulPose(Axis.YP.rotationDegrees(-90));
      if (state.wallMounted) collector.submitModel(this.wall, modelState, poseStack, state.baseTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
      else collector.submitModel(this.normal, modelState, poseStack, state.baseTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
      if (state.wallMounted) poseStack.translate(-1, 3, 0);
      collector.submitModel(this.pattern, modelState, poseStack, state.patternTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
      poseStack.popPose();
   }
   public static final class State extends BlockEntityRenderState {
      private Direction facing = Direction.NORTH; private boolean wallMounted; private float wave;
      private Identifier baseTexture; private Identifier patternTexture;
   }
}
