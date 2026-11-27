package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeScarecrowModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;

public final class RuntimeScarecrowHandLayer
   extends RenderLayer<RuntimeScarecrowRenderer.State, RuntimeScarecrowModel> {

   public RuntimeScarecrowHandLayer(RuntimeScarecrowRenderer renderer) {
      super(renderer);
   }

   @Override
   public void submit(
      PoseStack poseStack,
      SubmitNodeCollector collector,
      int packedLight,
      RuntimeScarecrowRenderer.State state,
      float yRot,
      float xRot
   ) {
      if (!state.rightHandItemState.isEmpty()) {
         poseStack.pushPose();
         this.getParentModel().translateToHand(state, HumanoidArm.RIGHT, poseStack);
         poseStack.rotateDegrees(Axis.XP, -90.0F);
         poseStack.rotateDegrees(Axis.YP, 180.0F);
         poseStack.translate(0.125F, 0.0F, -1.375F);
         poseStack.rotateDegrees(Axis.ZP, -90.0F);
         poseStack.rotateDegrees(Axis.XP, 85.0F);
         poseStack.scale(0.75F, 0.75F, 0.75F);
         state.rightHandItemState.submit(poseStack, collector, packedLight, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }

      if (!state.leftHandItemState.isEmpty()) {
         poseStack.pushPose();
         this.getParentModel().translateToHand(state, HumanoidArm.LEFT, poseStack);
         poseStack.rotateDegrees(Axis.XP, -90.0F);
         poseStack.rotateDegrees(Axis.YP, 180.0F);
         poseStack.translate(-0.125F, 0.0F, -1.375F);
         poseStack.rotateDegrees(Axis.ZP, 90.0F);
         poseStack.rotateDegrees(Axis.XP, 85.0F);
         poseStack.scale(0.75F, 0.75F, 0.75F);
         state.leftHandItemState.submit(poseStack, collector, packedLight, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
   }
}
