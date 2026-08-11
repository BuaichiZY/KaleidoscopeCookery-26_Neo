package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
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

public final class RuntimeKitchenwareRacksRenderer
   implements BlockEntityRenderer<KitchenwareRacksBlockEntity, RuntimeKitchenwareRacksRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeKitchenwareRacksRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      KitchenwareRacksBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
      state.items = new ArrayList<>();
      this.addItem(state, blockEntity, blockEntity.getItemLeft(), -0.2F, 0);
      this.addItem(state, blockEntity, blockEntity.getItemRight(), 0.2F, 1);
   }

   private void addItem(State state, KitchenwareRacksBlockEntity blockEntity, ItemStack stack, float x, int seedOffset) {
      if (stack.isEmpty()) {
         return;
      }
      ItemStackRenderState itemState = new ItemStackRenderState();
      this.itemModelResolver.updateForTopItem(
         itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null,
         (int)(blockEntity.getBlockPos().asLong() + seedOffset)
      );
      state.items.add(new RackItem(itemState, x));
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.items.isEmpty()) {
         return;
      }
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.0F, 0.5F);
      poseStack.mulPose(Axis.YN.rotationDegrees(state.facing.get2DDataValue() * 90.0F));
      for (RackItem rackItem : state.items) {
         poseStack.pushPose();
         poseStack.translate(rackItem.x(), 0.4375F, -0.3F);
         poseStack.scale(0.75F, 0.75F, 0.75F);
         poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.YN.rotationDegrees(-25.0F));
         poseStack.mulPose(Axis.ZN.rotationDegrees(45.0F));
         rackItem.item().submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
      poseStack.popPose();
   }

   private record RackItem(ItemStackRenderState item, float x) {
   }

   public static final class State extends BlockEntityRenderState {
      private List<RackItem> items = List.of();
      private Direction facing = Direction.NORTH;
   }
}
