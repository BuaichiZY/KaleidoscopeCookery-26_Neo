package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
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

public final class RuntimeFruitBasketRenderer
   implements BlockEntityRenderer<FruitBasketBlockEntity, RuntimeFruitBasketRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeFruitBasketRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      FruitBasketBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
      state.items = new ArrayList<>();
      for (int slot = 0; slot < blockEntity.getItems().getSlots(); slot++) {
         ItemStack stack = blockEntity.getItems().getStackInSlot(slot);
         if (!stack.isEmpty()) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
               itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null,
               (int)(blockEntity.getBlockPos().asLong() + slot)
            );
            state.items.add(new BasketItem(itemState, slot));
         }
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.0F, 0.5F);
      poseStack.mulPose(Axis.YN.rotationDegrees(state.facing.get2DDataValue() * 90.0F));
      poseStack.translate(-0.5F, 0.0F, -0.5F);
      for (BasketItem basketItem : state.items) {
         int slot = basketItem.slot();
         int row = slot / 4;
         int column = slot % 4;
         poseStack.pushPose();
         poseStack.translate(0.25F + column * 0.15F, 0.31F + (slot % 2) * 0.012F, 0.35F + row * 0.32F);
         poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
         poseStack.mulPose(Axis.XN.rotationDegrees(-30.0F));
         poseStack.scale(0.375F, 0.375F, 0.375F);
         basketItem.item().submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
      poseStack.popPose();
   }

   private record BasketItem(ItemStackRenderState item, int slot) {
   }

   public static final class State extends BlockEntityRenderState {
      private List<BasketItem> items = List.of();
      private Direction facing = Direction.NORTH;
   }
}
