package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ShawarmaSpitBlockEntityRender implements BlockEntityRenderer<ShawarmaSpitBlockEntity> {
   private final Context context;

   public ShawarmaSpitBlockEntityRender(Context context) {
      this.context = context;
   }

   public void render(
      ShawarmaSpitBlockEntity shawarmaSpit, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay
   ) {
      ItemStack cookedItem = shawarmaSpit.cookedItem;
      if (!cookedItem.isEmpty()) {
         ItemStack renderItem;
         if (!shawarmaSpit.cookingItem.isEmpty()) {
            renderItem = shawarmaSpit.cookingItem;
         } else {
            renderItem = cookedItem;
         }

         BlockState blockState = shawarmaSpit.getBlockState();
         Boolean powered = (Boolean)blockState.getValue(BlockStateProperties.POWERED);
         DoubleBlockHalf half = (DoubleBlockHalf)blockState.getValue(ShawarmaSpitBlock.HALF);
         ItemRenderer itemRenderer = this.context.getItemRenderer();
         if (powered) {
            long time = System.currentTimeMillis() % 3600L;
            poseStack.rotateAround(Axis.YP.rotationDegrees((float)time / 10.0F), 0.5F, 0.0F, 0.5F);
         }

         if (half == DoubleBlockHalf.UPPER) {
            poseStack.translate(0.25, 0.5, 0.25);
            this.renderItems(shawarmaSpit, poseStack, buffer, packedLight, packedOverlay, renderItem, itemRenderer);
         } else if (half == DoubleBlockHalf.LOWER) {
            poseStack.translate(0.25, 0.875, 0.25);
            this.renderItems(shawarmaSpit, poseStack, buffer, packedLight, packedOverlay, renderItem, itemRenderer);
         }
      }
   }

   private void renderItems(
      ShawarmaSpitBlockEntity shawarmaSpit,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      ItemStack renderItem,
      ItemRenderer itemRenderer
   ) {
      for (int i = 0; i < renderItem.getCount(); i++) {
         poseStack.pushPose();
         poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45), 0.25F, 0.0F, 0.25F);
         poseStack.scale(0.65F, 0.65F, 0.65F);
         itemRenderer.renderStatic(renderItem, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, shawarmaSpit.getLevel(), 0);
         poseStack.popPose();
      }
   }
}
