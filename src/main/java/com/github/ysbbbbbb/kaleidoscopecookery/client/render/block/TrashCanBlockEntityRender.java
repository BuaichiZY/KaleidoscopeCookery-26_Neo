package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TrashCanAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TrashCanModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class TrashCanBlockEntityRender implements BlockEntityRenderer<TrashCanBlockEntity> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/block/trash_can.png");
   private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
   private final Context context;
   private final TrashCanModel model;

   public TrashCanBlockEntityRender(Context context) {
      this.context = context;
      this.model = new TrashCanModel(context.bakeLayer(TrashCanModel.LAYER_LOCATION));
   }

   public void render(TrashCanBlockEntity trashCan, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      Level level = trashCan.getLevel();
      if (level != null) {
         Direction facing = (Direction)trashCan.getBlockState().getValue(TrashCanBlock.FACING);
         int facingDeg = facing.get2DDataValue() * 90;
         float ageInTicks = (float)trashCan.getLevel().getGameTime() + partialTick;
         this.model.root().getAllParts().forEach(ModelPart::resetPose);
         this.animate(trashCan.putState, TrashCanAnimation.PUT, ageInTicks);
         this.animate(trashCan.withdrawState, TrashCanAnimation.WITHDRAW, ageInTicks);
         this.animate(trashCan.player1State, TrashCanAnimation.PLAYER1, ageInTicks);
         this.animate(trashCan.player2State, TrashCanAnimation.PLAYER2, ageInTicks);
         this.animate(trashCan.enterState, TrashCanAnimation.ENTER, ageInTicks);
         poseStack.pushPose();
         poseStack.translate(0.5, 1.5, 0.5);
         poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
         VertexConsumer checkerBoardBuff = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
         this.model.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay);
         poseStack.popPose();
      }
   }

   protected void animate(AnimationState animationState, AnimationDefinition definition, float ageInTicks) {
      animationState.updateTime(ageInTicks, 1.0F);
      animationState.ifStarted(state -> KeyframeAnimations.animate(this.model, definition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
   }
}
