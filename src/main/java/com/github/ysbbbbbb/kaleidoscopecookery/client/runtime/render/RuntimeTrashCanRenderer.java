package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeTrashCanModel;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class RuntimeTrashCanRenderer implements BlockEntityRenderer<TrashCanBlockEntity, RuntimeTrashCanRenderer.State> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/block/trash_can.png"
   );
   private static final float DEG_TO_RAD = (float)(Math.PI / 180.0);
   private final RuntimeTrashCanModel model;

   public RuntimeTrashCanRenderer(BlockEntityRendererProvider.Context context) {
      this.model = new RuntimeTrashCanModel(context.bakeLayer(RuntimeTrashCanModel.LAYER_LOCATION));
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      TrashCanBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(TrashCanBlock.FACING);
      state.age = blockEntity.getLevel() == null ? 0.0F : blockEntity.getLevel().getGameTime() + partialTicks;
      state.occupied = false;
      if (blockEntity.getLevel() != null) {
         List<SitEntity> seats = blockEntity.getLevel().getEntitiesOfClass(SitEntity.class, new AABB(blockEntity.getBlockPos()));
         state.occupied = seats.stream().anyMatch(seat -> seat.getSitType() == SitEntity.TRASH_CAN && !seat.getPassengers().isEmpty());
      }
      state.enterSeconds = blockEntity.enterState.isStarted()
         ? blockEntity.enterState.getTimeInMillis(state.age) / 1000.0F
         : Float.MAX_VALUE;
      state.putSeconds = blockEntity.putState.isStarted()
         ? blockEntity.putState.getTimeInMillis(state.age) / 1000.0F
         : Float.MAX_VALUE;
      state.withdrawSeconds = blockEntity.withdrawState.isStarted()
         ? blockEntity.withdrawState.getTimeInMillis(state.age) / 1000.0F
         : Float.MAX_VALUE;
      state.player1Seconds = blockEntity.player1State.isStarted()
         ? blockEntity.player1State.getTimeInMillis(state.age) / 1000.0F
         : Float.MAX_VALUE;
      state.player2Seconds = blockEntity.player2State.isStarted()
         ? blockEntity.player2State.getTimeInMillis(state.age) / 1000.0F
         : Float.MAX_VALUE;
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      float bodyYaw = 0.0F;
      float lidPitch = 0.0F;
      float lidRoll = 0.0F;
      float lidLift = 0.0F;
      float eyeLift = 0.0F;
      float eyeSlide = 0.0F;
      boolean blink = false;

      if (state.occupied) {
         lidPitch = -10.0F * DEG_TO_RAD;
         lidLift = 0.5F;
         eyeLift = 4.0F;
         if (state.player1Seconds < 1.0F) {
            blink = state.player1Seconds >= 0.4583F && state.player1Seconds <= 0.5833F;
         } else if (state.player2Seconds < 2.75F) {
            float time = state.player2Seconds;
            if (time < 0.1667F) {
               eyeSlide = 0.0F;
            } else if (time < 0.375F) {
               eyeSlide = -Mth.clamp((time - 0.1667F) / 0.2083F, 0.0F, 1.0F);
            } else if (time < 1.0417F) {
               eyeSlide = -1.0F;
            } else if (time < 1.4167F) {
               eyeSlide = -1.0F + 2.0F * Mth.clamp((time - 1.0417F) / 0.375F, 0.0F, 1.0F);
            } else if (time < 2.4583F) {
               eyeSlide = 1.0F;
            } else if (time < 2.6667F) {
               eyeSlide = 1.0F - Mth.clamp((time - 2.4583F) / 0.2084F, 0.0F, 1.0F);
            }
         }
      }

      if (state.enterSeconds < 0.667F) {
         float p = Mth.clamp(state.enterSeconds / 0.667F, 0.0F, 1.0F);
         float bump = Mth.sin(p * (float)Math.PI);
         lidLift = Math.max(lidLift, 5.0F * bump);
         lidRoll = Mth.sin(p * (float)Math.PI * 4.0F) * 8.0F * DEG_TO_RAD * (1.0F - p);
         bodyYaw = Mth.sin(p * (float)Math.PI * 6.0F) * 5.0F * DEG_TO_RAD * (1.0F - p);
      } else if (state.putSeconds < 0.584F) {
         float time = state.putSeconds;
         // Match the original PUT keyframes.  In particular, the two-pixel
         // lift finishes at 0.2083 s instead of being stretched over 0.5833 s.
         if (time < 0.125F) {
            lidLift = Math.max(lidLift, 2.0F * time / 0.125F);
            lidPitch += -12.5F * DEG_TO_RAD * time / 0.125F;
         } else {
            if (time < 0.2083F) {
               float p = smooth((time - 0.125F) / 0.0833F);
               lidLift = Math.max(lidLift, 2.0F * (1.0F - p));
               lidPitch += -12.5F * DEG_TO_RAD;
            } else if (time < 0.3333F) {
               lidPitch += smoothLerp(-12.5F, 7.5F, (time - 0.2083F) / 0.125F) * DEG_TO_RAD;
            } else if (time < 0.4167F) {
               lidPitch += smoothLerp(7.5F, -7.5F, (time - 0.3333F) / 0.0834F) * DEG_TO_RAD;
            } else if (time < 0.5F) {
               lidPitch += smoothLerp(-7.5F, 2.5F, (time - 0.4167F) / 0.0833F) * DEG_TO_RAD;
            } else if (time < 0.5417F) {
               lidPitch += smoothLerp(2.5F, -2.5F, (time - 0.5F) / 0.0417F) * DEG_TO_RAD;
            } else {
               lidPitch += smoothLerp(-2.5F, 0.0F, (time - 0.5417F) / 0.0416F) * DEG_TO_RAD;
            }
         }
      } else if (state.withdrawSeconds < 0.5F) {
         float p = state.withdrawSeconds / 0.5F;
         lidLift = Math.max(lidLift, Mth.sin(p * (float)Math.PI));
         lidPitch += -7.5F * DEG_TO_RAD * Mth.sin(p * (float)Math.PI * 2.0F);
      }

      RuntimeTrashCanModel.State modelState = new RuntimeTrashCanModel.State(
         bodyYaw, lidPitch, lidRoll, lidLift, eyeLift, eyeSlide, blink
      );
      poseStack.pushPose();
      poseStack.translate(0.5F, 1.5F, 0.5F);
      poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
      poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - state.facing.get2DDataValue() * 90.0F));
      collector.submitModel(
         this.model, modelState, poseStack, TEXTURE, state.lightCoords,
         OverlayTexture.NO_OVERLAY, 0, state.breakProgress
      );
      poseStack.popPose();
   }

   private static float smooth(float value) {
      float p = Mth.clamp(value, 0.0F, 1.0F);
      return p * p * (3.0F - 2.0F * p);
   }

   private static float smoothLerp(float from, float to, float progress) {
      return Mth.lerp(smooth(progress), from, to);
   }

   @Override
   public boolean shouldRenderOffScreen() {
      return true;
   }

   public static final class State extends BlockEntityRenderState {
      private Direction facing = Direction.NORTH;
      private float age;
      private boolean occupied;
      private float enterSeconds;
      private float putSeconds;
      private float withdrawSeconds;
      private float player1Seconds;
      private float player2Seconds;
   }
}
