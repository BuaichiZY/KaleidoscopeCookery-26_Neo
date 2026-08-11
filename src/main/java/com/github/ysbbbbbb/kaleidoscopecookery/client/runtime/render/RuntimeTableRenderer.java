package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.util.CarpetColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class RuntimeTableRenderer implements BlockEntityRenderer<TableBlockEntity, RuntimeTableRenderer.State> {
   private final ItemModelResolver itemModelResolver;

   public RuntimeTableRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      TableBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.axis = blockEntity.getBlockState().getValue(TableBlock.AXIS);
      state.carpet = new ItemStackRenderState();
      if (blockEntity.getBlockState().getValue(TableBlock.HAS_CARPET)) {
         String position = switch (blockEntity.getBlockState().getValue(TableBlock.POSITION)) {
            case TableBlock.LEFT -> "left";
            case TableBlock.MIDDLE -> "middle";
            case TableBlock.RIGHT -> "right";
            default -> "single";
         };
         ItemStack carpet = new ItemStack(CarpetColor.getCarpetByColor(blockEntity.getColor()));
         carpet.set(
            DataComponents.ITEM_MODEL,
            Identifier.fromNamespaceAndPath(
               "kaleidoscope_cookery", "carpet/table/" + blockEntity.getColor().getName() + "_" + position
            )
         );
         this.itemModelResolver.updateForTopItem(
            state.carpet, carpet, ItemDisplayContext.NONE, blockEntity.getLevel(), null, (int)blockEntity.getBlockPos().asLong()
         );
      }

      state.items = new ArrayList<>();
      state.blockItems = new ArrayList<>();
      for (int slot = 0; slot < blockEntity.getItems().getSlots(); slot++) {
         ItemStack stack = blockEntity.getItems().getStackInSlot(slot);
         if (!stack.isEmpty()) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
               itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, (int)(blockEntity.getBlockPos().asLong() + slot)
            );
            state.items.add(itemState);
            state.blockItems.add(stack.getItem() instanceof BlockItem);
         }
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (!state.carpet.isEmpty()) {
         poseStack.pushPose();
         poseStack.translate(0.5F, 0.5F, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(state.axis == Direction.Axis.X ? -180.0F : -270.0F));
         state.carpet.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }

      int count = state.items.size();
      for (int i = 0; i < count; i++) {
         float[] offset = itemOffset(count, i);
         poseStack.pushPose();
         poseStack.translate(0.5F, 1.3125F, 0.5F);
         poseStack.scale(0.65F, 0.65F, 0.65F);
         poseStack.mulPose(Axis.YP.rotationDegrees(state.axis == Direction.Axis.X ? 180.0F : 90.0F));
         poseStack.translate(offset[0], offset[1] - (state.blockItems.get(i) ? 0.25F : 0.0F), offset[2]);
         state.items.get(i).submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
   }

   private static float[] itemOffset(int count, int index) {
      if (count == 1) {
         return new float[]{0.0F, 0.0F, 0.0F};
      }
      if (count == 2) {
         return index == 0 ? new float[]{-0.25F, 0.0F, 0.1F} : new float[]{0.25F, 0.01F, -0.1F};
      }
      if (count == 3) {
         return switch (index) {
            case 0 -> new float[]{0.25F, 0.0F, -0.2F};
            case 1 -> new float[]{-0.25F, 0.01F, 0.0F};
            default -> new float[]{0.24F, 0.02F, 0.2F};
         };
      }
      return switch (index) {
         case 0 -> new float[]{0.25F, 0.0F, -0.3F};
         case 1 -> new float[]{-0.24F, 0.01F, -0.1F};
         case 2 -> new float[]{0.24F, 0.02F, 0.1F};
         default -> new float[]{-0.25F, 0.03F, 0.3F};
      };
   }

   public static final class State extends BlockEntityRenderState {
      private ItemStackRenderState carpet = new ItemStackRenderState();
      private List<ItemStackRenderState> items = List.of();
      private List<Boolean> blockItems = List.of();
      private Direction.Axis axis = Direction.Axis.Z;
   }
}
