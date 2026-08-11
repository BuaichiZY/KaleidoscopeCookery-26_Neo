package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RecipeBlockEntityRender implements BlockEntityRenderer<RecipeBlockEntity> {
   private final Context context;

   public RecipeBlockEntityRender(Context context) {
      this.context = context;
   }

   public void render(RecipeBlockEntity recipeBlock, float pPartialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         ItemStack stack = recipeBlock.getItems().getStackInSlot(0);
         if (!stack.isEmpty()) {
            RecipeItem.RecipeRecord record = (RecipeItem.RecipeRecord)stack.get(ModDataComponents.RECIPE_RECORD);
            if (record != null) {
               ItemStack output = record.output();
               Direction facing = (Direction)recipeBlock.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
               AttachFace attachFace = (AttachFace)recipeBlock.getBlockState().getValue(BlockStateProperties.ATTACH_FACE);
               int rotationX = attachFace.ordinal();
               int rotationY = facing.get2DDataValue() + (attachFace == AttachFace.CEILING ? 2 : 0);
               ItemRenderer itemRenderer = this.context.getItemRenderer();
               poseStack.pushPose();
               poseStack.translate(0.5, 0.5, 0.5);
               poseStack.mulPose(Axis.YP.rotationDegrees(-rotationY * 90));
               poseStack.mulPose(Axis.XP.rotationDegrees(90 - rotationX * 90));
               poseStack.translate(-0.5, -0.5, -0.5);
               poseStack.scale(0.5F, 0.5F, 0.5F);
               if (attachFace == AttachFace.WALL) {
                  poseStack.translate(1.0, 1.25, 0.0);
               } else {
                  poseStack.translate(1.0, 0.75, 2.0);
               }

               itemRenderer.renderStatic(output, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, recipeBlock.getLevel(), 0);
               poseStack.popPose();
            }
         }
      }
   }
}
