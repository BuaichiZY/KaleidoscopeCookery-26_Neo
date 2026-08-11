package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PotBlockEntityRender implements BlockEntityRenderer<PotBlockEntity> {
   private final Context context;

   public PotBlockEntityRender(Context context) {
      this.context = context;
   }

   public void render(PotBlockEntity pot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      RandomSource source = RandomSource.create(pot.getSeed());
      PotBlockEntity.StirFryAnimationData data = pot.animationData;
      long time = System.currentTimeMillis() - data.timestamp;
      if (data.preSeed == -1L) {
         data.preSeed = pot.getSeed();
      }

      if (data.preSeed != pot.getSeed()) {
         data.preSeed = pot.getSeed();
         if (time > 1000L) {
            data.timestamp = System.currentTimeMillis();
            data.randomHeights = new float[9];

            for (int i = 0; i < 9; i++) {
               data.randomHeights[i] = 0.25F + source.nextFloat() * 1.0F;
            }
         }
      }

      ItemRenderer itemRenderer = this.context.getItemRenderer();
      int rotation = ((Direction)pot.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)).get2DDataValue() * 90;
      poseStack.pushPose();
      poseStack.translate(0.5, 0.1, 0.5);
      poseStack.mulPose(Axis.YN.rotationDegrees(rotation));
      poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
      poseStack.scale(0.5F, 0.5F, 0.5F);
      boolean showInputs = pot.getStatus() != 2 && pot.getStatus() != 3;
      if (!showInputs && !pot.hasCarrier()) {
         this.renderItem(pot, poseStack, buffer, packedLight, packedOverlay, source, 0, time, data, itemRenderer, pot.getResult());
      } else {
         List<ItemStack> items = pot.getInputs();

         for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            if (!item.isEmpty()) {
               this.renderItem(pot, poseStack, buffer, packedLight, packedOverlay, source, i, time, data, itemRenderer, item);
               poseStack.translate(0.0, 0.0, 0.025);
            }
         }
      }

      poseStack.popPose();
   }

   private void renderItem(
      PotBlockEntity pot,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      RandomSource source,
      int index,
      long time,
      PotBlockEntity.StirFryAnimationData data,
      ItemRenderer itemRenderer,
      ItemStack item
   ) {
      poseStack.pushPose();
      int count = 90 + source.nextInt(90);
      poseStack.mulPose(Axis.ZN.rotationDegrees(index * count));
      if (time < 1000L) {
         poseStack.translate(0.0F, 0.0F, data.randomHeights[index] * Mth.sin((float) Math.PI * (float)time / 1000.0F));
         poseStack.mulPose(Axis.XN.rotationDegrees(0.72F * (float)time));
      }

      if (pot.getStatus() == 3) {
         int tick = pot.getCurrentTick();
         int burntLevel = Mth.clamp(tick / 25, 0, 16);
         packedLight = OverlayTexture.u(burntLevel);
      }

      BakedModel model = ItemRenderReplacer.getModel(pot.getLevel(), item, ItemRenderReplacerReloadListener.INSTANCE.pot());
      itemRenderer.render(item, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, model);
      poseStack.popPose();
   }
}
