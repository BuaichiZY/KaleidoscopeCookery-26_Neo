package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MillstoneBlockEntityRender implements BlockEntityRenderer<MillstoneBlockEntity> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/block/millstone.png");
   private final Context context;
   private final MillstoneModel bodyModel;

   public MillstoneBlockEntityRender(Context context) {
      this.context = context;
      this.bodyModel = new MillstoneModel(context.bakeLayer(MillstoneModel.LAYER_LOCATION));
   }

   public void render(MillstoneBlockEntity millstone, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      Level level = millstone.getLevel();
      if (level != null) {
         Direction facing = (Direction)millstone.getBlockState().getValue(MillstoneBlock.FACING);
         this.renderBody(poseStack, buffer, packedLight, packedOverlay, partialTick, facing, millstone, level);
         if (!millstone.getOutputs().getStackInSlot(0).isEmpty()) {
            this.renderItems(millstone, poseStack, buffer, packedLight, packedOverlay, millstone.getOutputs().getStackInSlot(0), this.context.getItemRenderer());
         } else if (!millstone.getInput().isEmpty()) {
            this.renderItems(millstone, poseStack, buffer, packedLight, packedOverlay, millstone.getInput(), this.context.getItemRenderer());
         }
      }
   }

   private void renderBody(
      PoseStack poseStack,
      MultiBufferSource bufferIn,
      int combinedLightIn,
      int combinedOverlayIn,
      float partialTick,
      Direction facing,
      MillstoneBlockEntity millstone,
      Level level
   ) {
      int facingDeg = facing.get2DDataValue() * 90;
      if (millstone.hasEntity()) {
         float rot = facingDeg + millstone.getRotation(level, partialTick);
         this.bodyModel.getWheel().yRot = -rot * (float) (Math.PI / 180.0);
         this.bodyModel.getRoll().zRot = rot * (float) (Math.PI / 180.0);
         this.bodyModel.getRotStick().xRot = -millstone.getLiftAngle() * (float) (Math.PI / 180.0);
      } else {
         float rot = facingDeg + millstone.getCacheRot();
         this.bodyModel.getWheel().yRot = -rot * (float) (Math.PI / 180.0);
      }

      poseStack.pushPose();
      poseStack.translate(0.5, 1.5, 0.5);
      poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
      poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
      VertexConsumer checkerBoardBuff = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      this.bodyModel.renderToBuffer(poseStack, checkerBoardBuff, combinedLightIn, combinedOverlayIn);
      poseStack.popPose();
      this.bodyModel.getWheel().yRot = 0.0F;
      this.bodyModel.getRoll().zRot = 0.0F;
      this.bodyModel.getRotStick().xRot = 0.0F;
   }

   private void renderItems(
      MillstoneBlockEntity millstone,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      ItemStack renderItem,
      ItemRenderer itemRenderer
   ) {
      RandomSource source = RandomSource.create(millstone.hashCode());
      int maxCount = Math.min(renderItem.getCount(), 8);
      BakedModel model = ItemRenderReplacer.getModel(millstone.getLevel(), renderItem, ItemRenderReplacerReloadListener.INSTANCE.millstone());

      for (int i = 0; i < maxCount; i++) {
         poseStack.pushPose();
         poseStack.translate(0.0, 0.875, 0.0);
         poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45 + source.nextInt(15)), 0.5F, 0.0F, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(source.nextInt(20)));
         poseStack.mulPose(Axis.XN.rotationDegrees(80 + source.nextInt(20)));
         poseStack.scale(0.65F, 0.65F, 0.65F);
         itemRenderer.render(renderItem, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, model);
         poseStack.popPose();
      }
   }

   public boolean shouldRenderOffScreen(MillstoneBlockEntity millstone) {
      return true;
   }

   public AABB getRenderBoundingBox(MillstoneBlockEntity blockEntity) {
      BlockPos pos = blockEntity.getBlockPos();
      return getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
   }

   private static AABB getAABB(BlockPos pStart, BlockPos pEnd) {
      return new AABB(pStart.getX(), pStart.getY(), pStart.getZ(), pEnd.getX(), pEnd.getY(), pEnd.getZ());
   }
}
