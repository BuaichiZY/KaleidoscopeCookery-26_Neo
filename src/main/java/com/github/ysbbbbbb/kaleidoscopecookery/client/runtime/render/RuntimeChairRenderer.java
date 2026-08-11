package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.ChairBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor;
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

public final class RuntimeChairRenderer implements BlockEntityRenderer<ChairBlockEntity, RuntimeChairRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeChairRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      ChairBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(ChairBlock.FACING);
      state.carpet = new ItemStackRenderState();
      if (blockEntity.getBlockState().getValue(ChairBlock.HAS_CARPET)) {
         ItemStack carpet = new ItemStack(CarpetColor.getCarpetByColor(blockEntity.getColor()));
         carpet.set(
            DataComponents.ITEM_MODEL,
            Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "carpet/chair/" + blockEntity.getColor().getName())
         );
         this.itemModelResolver.updateForTopItem(
            state.carpet, carpet, ItemDisplayContext.NONE, blockEntity.getLevel(), null, (int)blockEntity.getBlockPos().asLong()
         );
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.carpet.isEmpty()) {
         return;
      }

      poseStack.pushPose();
      poseStack.translate(0.5F, 0.5F, 0.5F);
      poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.getOpposite().get2DDataValue() * 90.0F));
      state.carpet.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private ItemStackRenderState carpet = new ItemStackRenderState();
      private Direction facing = Direction.SOUTH;
   }
}
