package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public final class RuntimePotRenderer implements BlockEntityRenderer<PotBlockEntity, RuntimePotRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimePotRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      PotBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
      state.seed = blockEntity.getSeed();
      state.items = new ArrayList<>();
      state.slots = new ArrayList<>();
      PotBlockEntity.StirFryAnimationData animation = blockEntity.animationData;
      long now = System.currentTimeMillis();
      if (animation.preSeed == -1L) {
         animation.preSeed = state.seed;
      }

      if (animation.preSeed != state.seed) {
         animation.preSeed = state.seed;
         if (now - animation.timestamp > 1000L) {
            animation.timestamp = now;
            animation.randomHeights = new float[9];
            RandomSource heightsRandom = RandomSource.create(state.seed);
            for (int i = 0; i < animation.randomHeights.length; i++) {
               animation.randomHeights[i] = 0.25F + heightsRandom.nextFloat();
            }
         }
      }

      state.animationTime = now - animation.timestamp;
      state.randomHeights = animation.randomHeights.clone();
      boolean showInputs = blockEntity.getStatus() != 2 && blockEntity.getStatus() != 3;
      List<ItemStack> stacks = showInputs || blockEntity.hasCarrier() ? blockEntity.getInputs() : List.of(blockEntity.getResult());
      for (int slot = 0; slot < stacks.size() && slot < 9; slot++) {
         ItemStack stack = stacks.get(slot);
         if (!stack.isEmpty()) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
               itemState,
               stack,
               ItemDisplayContext.FIXED,
               blockEntity.getLevel(),
               null,
               (int)(blockEntity.getBlockPos().asLong() + slot)
            );
            state.items.add(itemState);
            state.slots.add(slot);
         }
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.items.isEmpty()) {
         return;
      }

      RandomSource random = RandomSource.create(state.seed);
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.13F, 0.5F);
      poseStack.mulPose(Axis.YN.rotationDegrees(state.facing.get2DDataValue() * 90.0F));
      poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
      poseStack.scale(0.5F, 0.5F, 0.5F);
      for (int i = 0; i < state.items.size(); i++) {
         poseStack.pushPose();
         int slot = state.slots.get(i);
         poseStack.mulPose(Axis.ZN.rotationDegrees(slot * (90.0F + random.nextInt(90))));
         if (state.animationTime >= 0L && state.animationTime < 1000L && slot < state.randomHeights.length) {
            float progress = (float)state.animationTime / 1000.0F;
            poseStack.translate(0.0F, 0.0F, state.randomHeights[slot] * Mth.sin((float)Math.PI * progress));
            poseStack.mulPose(Axis.XN.rotationDegrees(0.72F * (float)state.animationTime));
         }
         state.items.get(i).submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
         poseStack.translate(0.0F, 0.0F, 0.025F);
      }
      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private List<ItemStackRenderState> items = List.of();
      private List<Integer> slots = List.of();
      private Direction facing = Direction.NORTH;
      private long seed;
      private long animationTime = Long.MAX_VALUE;
      private float[] randomHeights = new float[9];
   }
}
