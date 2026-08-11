package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ChoppingBoardBlockEntityRender implements BlockEntityRenderer<ChoppingBoardBlockEntity> {
   private final ItemRenderer itemRenderer;

   public ChoppingBoardBlockEntityRender(Context context) {
      this.itemRenderer = context.getItemRenderer();
   }

   public void render(
      ChoppingBoardBlockEntity choppingBoard, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay
   ) {
      Identifier modelId = choppingBoard.getModelId();
      if (modelId != null) {
         if (choppingBoard.previousModel == null || !modelId.equals(choppingBoard.previousModel.id())) {
            choppingBoard.previousModel = ModelIdentifier.standalone(modelId);
            choppingBoard.cacheModels = new ModelIdentifier[choppingBoard.getMaxCutCount() + 1];

            for (int i = 0; i <= choppingBoard.getMaxCutCount(); i++) {
               Identifier location = Identifier.fromNamespaceAndPath(modelId.getNamespace(), "chopping_board/" + modelId.getPath() + "/" + i);
               choppingBoard.cacheModels[i] = ModelIdentifier.standalone(location);
            }
         }

         if (choppingBoard.cacheModels != null) {
            int index = Math.min(choppingBoard.getCurrentCutCount(), choppingBoard.cacheModels.length - 1);
            ModelIdentifier cacheModel = choppingBoard.cacheModels[index];
            poseStack.pushPose();
            int rotation = ((Direction)choppingBoard.getBlockState().getValue(ChoppingBoardBlock.FACING)).get2DDataValue();
            poseStack.translate(0.5, 0.0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 90));
            poseStack.translate(-0.5, 0.125, -0.5);
            BakedModel model = this.itemRenderer.getItemModelShaper().getModelManager().getModel(cacheModel);
            RenderType renderType = Sheets.cutoutBlockSheet();
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, false);
            this.itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);
            poseStack.popPose();
         }
      }
   }
}
