package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

/** Renders the recorded recipe's result over the parchment block. */
public final class RuntimeRecipeBlockRenderer
   implements BlockEntityRenderer<RecipeBlockEntity, RuntimeRecipeBlockRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeRecipeBlockRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      RecipeBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.facing = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
      state.attachFace = blockEntity.getBlockState().getValue(BlockStateProperties.ATTACH_FACE);
      state.output.clear();

      ItemStack recipeStack = blockEntity.getItems().getStackInSlot(0);
      RecipeItem.RecipeRecord record = RecipeItem.getRecipe(recipeStack);
      if (record != null && !record.output().isEmpty()) {
         this.itemModelResolver.updateForTopItem(
            state.output,
            record.output(),
            ItemDisplayContext.FIXED,
            blockEntity.getLevel(),
            null,
            (int)blockEntity.getBlockPos().asLong()
         );
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.output.isEmpty()) {
         return;
      }

      int rotationX = state.attachFace.ordinal();
      int rotationY = state.facing.get2DDataValue() + (state.attachFace == AttachFace.CEILING ? 2 : 0);
      poseStack.pushPose();
      poseStack.translate(0.5F, 0.5F, 0.5F);
      poseStack.mulPose(Axis.YP.rotationDegrees(-rotationY * 90.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(90.0F - rotationX * 90.0F));
      poseStack.translate(-0.5F, -0.5F, -0.5F);
      poseStack.scale(0.5F, 0.5F, 0.5F);
      if (state.attachFace == AttachFace.WALL) {
         poseStack.translate(1.0F, 1.25F, 0.0F);
      } else {
         poseStack.translate(1.0F, 0.75F, 2.0F);
      }
      state.output.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
      poseStack.popPose();
   }

   public static final class State extends BlockEntityRenderState {
      private final ItemStackRenderState output = new ItemStackRenderState();
      private Direction facing = Direction.NORTH;
      private AttachFace attachFace = AttachFace.WALL;
   }
}
