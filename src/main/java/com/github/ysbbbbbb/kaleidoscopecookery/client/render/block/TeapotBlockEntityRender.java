package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TeapotAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TeapotModel;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Vector3f;

public class TeapotBlockEntityRender implements BlockEntityRenderer<TeapotBlockEntity> {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/block/teapot.png");
   private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
   private final Context context;
   private final TeapotModel model;
   private final Function<Identifier, Component> fluidNameCache = Util.memoize(id -> {
      if (id.equals(TeapotRecipeSerializer.EMPTY_TEA_FLUID)) {
         return Component.translatable("mco.configure.world.slot.empty");
      } else {
         Fluid value = (Fluid)BuiltInRegistries.FLUID.getValue(id);
         String descriptionId = value.getFluidType().getDescriptionId();
         return Component.translatable(descriptionId);
      }
   });

   public TeapotBlockEntityRender(Context context) {
      this.context = context;
      this.model = new TeapotModel(context.bakeLayer(TeapotModel.LAYER_LOCATION));
   }

   public void render(TeapotBlockEntity teapot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      Level level = teapot.getLevel();
      if (level != null) {
         BlockEntityRenderDispatcher dispatcher = this.context.getBlockEntityRenderDispatcher();
         if (dispatcher.cameraHitResult instanceof BlockHitResult hitResult && hitResult.getBlockPos().equals(teapot.getBlockPos())) {
            Camera camera = dispatcher.camera;
            Font font = this.context.getFont();
            poseStack.pushPose();
            poseStack.translate(0.5, 1.0, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F + camera.getYRot()));
            poseStack.scale(0.015625F, -0.015625F, 0.015625F);
            Component statusText = teapot.getStatusText();
            float width = -font.width(statusText) / 2 + 0.5F;
            font.drawInBatch(statusText, width, -5.0F, 16777215, false, poseStack.last().pose(), buffer, DisplayMode.POLYGON_OFFSET, 0, packedLight);
            int status = teapot.getStatus();
            if (status == 0) {
               Component fluidText = this.fluidNameCache.apply(teapot.getTeaFluidId());
               ItemStack input = teapot.getInput();
               int count = input.getCount();
               Component itemText = (Component)(input.isEmpty()
                  ? Component.translatable("mco.configure.world.slot.empty")
                  : ComponentUtils.formatList(Arrays.asList(input.getHoverName(), Component.literal("x%d".formatted(count))), CommonComponents.space()));
               Component info = Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.fluid_ingredient", new Object[]{fluidText, itemText, count});
               float infoWidth = -font.width(info) / 2 + 0.5F;
               font.drawInBatch(info, infoWidth, 5.0F, 16777215, false, poseStack.last().pose(), buffer, DisplayMode.POLYGON_OFFSET, 0, packedLight);
            }

            if (status == 2) {
               ItemStack result = teapot.getResult();
               int count = result.getCount();
               Component itemText = (Component)(result.isEmpty()
                  ? Component.translatable("mco.configure.world.slot.empty")
                  : ComponentUtils.formatList(Arrays.asList(result.getHoverName(), Component.literal("x%d".formatted(count))), CommonComponents.space()));
               Component info = Component.translatable("tooltip.kaleidoscope_cookery.teapot.statue.result", new Object[]{itemText, count});
               float infoWidth = -font.width(info) / 2 + 0.5F;
               font.drawInBatch(info, infoWidth, 5.0F, 16777215, false, poseStack.last().pose(), buffer, DisplayMode.POLYGON_OFFSET, 0, packedLight);
            }

            poseStack.popPose();
         }

         Direction facing = (Direction)teapot.getBlockState().getValue(TeapotBlock.FACING);
         int facingDeg = facing.get2DDataValue() * 90;
         float ageInTicks = (float)teapot.getLevel().getGameTime() + partialTick;
         int variant = (Integer)teapot.getBlockState().getValue(TeapotBlock.VARIANT);
         this.model.root().getAllParts().forEach(ModelPart::resetPose);
         this.model.updateVariant(variant);
         teapot.boilingState.updateTime(ageInTicks, 1.0F);
         teapot.boilingState
            .ifStarted(state -> KeyframeAnimations.animate(this.model, TeapotAnimation.BOILING, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
         poseStack.pushPose();
         poseStack.translate(0.5, 1.5, 0.5);
         poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
         VertexConsumer checkerBoardBuff = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
         this.model.renderToBuffer(poseStack, checkerBoardBuff, packedLight, packedOverlay);
         poseStack.popPose();
      }
   }
}
