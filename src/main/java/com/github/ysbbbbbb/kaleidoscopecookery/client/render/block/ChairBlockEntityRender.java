package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.function.Function;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class ChairBlockEntityRender implements BlockEntityRenderer<ChairBlockEntity> {
   private static final Function<DyeColor, ModelIdentifier> CACHE_MODEL = Util.memoize(color -> {
      Identifier location = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "block/carpet/chair/" + color.getName());
      return ModelIdentifier.standalone(location);
   });
   private final Context context;

   public ChairBlockEntityRender(Context context) {
      this.context = context;
   }

   public void render(ChairBlockEntity chair, float pPartialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      ItemRenderer itemRenderer = this.context.getItemRenderer();
      ModelIdentifier cacheModel = CACHE_MODEL.apply(chair.getColor());
      poseStack.pushPose();
      int rotation = ((Direction)chair.getBlockState().getValue(HorizontalDirectionalBlock.FACING)).getOpposite().get2DDataValue();
      poseStack.translate(0.5, 0.0, 0.5);
      poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 90));
      poseStack.translate(-0.5, 0.0, -0.5);
      BakedModel model = itemRenderer.getItemModelShaper().getModelManager().getModel(cacheModel);
      RenderType renderType = RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS);
      VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, false);
      itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);
      poseStack.popPose();
   }
}
