package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FruitBasketBlockEntityRender implements BlockEntityRenderer<FruitBasketBlockEntity> {
   private final Context context;

   public FruitBasketBlockEntityRender(Context context) {
      this.context = context;
   }

   public void render(FruitBasketBlockEntity basket, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      ItemStackHandler items = basket.getItems();
      ItemRenderer itemRenderer = this.context.getItemRenderer();
      int rotation = ((Direction)basket.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)).get2DDataValue() * 90;
      poseStack.pushPose();
      poseStack.translate(0.5, 0.0, 0.5);
      poseStack.mulPose(Axis.YN.rotationDegrees(rotation));
      poseStack.translate(-0.5, 0.0, -0.5);
      poseStack.translate(0.1, 0.3, 0.35);

      for (int i = 0; i < 2; i++) {
         poseStack.pushPose();

         for (int j = 0; j < 4; j++) {
            int index = i * 4 + j;
            ItemStack itemStack = items.getStackInSlot(index);
            if (!itemStack.isEmpty()) {
               poseStack.translate(0.15, 0.0, 0.0);
               poseStack.pushPose();
               poseStack.translate(0.0F, 0.0F, index % 2 == 0 ? -0.01F : 0.01F);
               poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
               poseStack.mulPose(Axis.XN.rotationDegrees(-30.0F));
               poseStack.scale(0.375F, 0.375F, 0.375F);
               itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, basket.getLevel(), 0);
               poseStack.popPose();
            }
         }

         poseStack.popPose();
         poseStack.translate(0.0, 0.0, 0.32);
      }

      poseStack.popPose();
   }
}
