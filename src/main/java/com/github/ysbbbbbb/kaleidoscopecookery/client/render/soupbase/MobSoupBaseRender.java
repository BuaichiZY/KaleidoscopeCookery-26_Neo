package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

public class MobSoupBaseRender extends FluidSoupBaseRender {
   private final EntityType<?> mobType;

   public MobSoupBaseRender(Fluid fluid, EntityType<?> mobType) {
      super(fluid);
      this.mobType = mobType;
   }

   @Override
   public void renderWhenPutIngredient(
      StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float soupHeight
   ) {
      super.renderWhenPutIngredient(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, soupHeight);
      this.renderInputEntity(stockpot, poseStack, buffer, packedLight);
   }

   @Override
   public void renderWhenCooking(
      StockpotBlockEntity stockpot,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      Identifier cookingTexture,
      float soupHeight
   ) {
      super.renderWhenCooking(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, cookingTexture, soupHeight);
      this.renderInputEntity(stockpot, poseStack, buffer, packedLight);
   }

   private void renderInputEntity(StockpotBlockEntity stockpot, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      ClientLevel world = Minecraft.getInstance().level;
      if (world != null) {
         Entity renderEntity = stockpot.renderEntity;
         boolean shouldRefreshCache = renderEntity == null || renderEntity.getType() != this.mobType;
         if (shouldRefreshCache) {
            stockpot.renderEntity = this.mobType.create(world);
            if (stockpot.renderEntity != null) {
               stockpot.renderEntity.setOnGround(true);
            }
         }

         if (stockpot.renderEntity != null) {
            int random = stockpot.renderEntity.hashCode();
            float entityY = (float)(Math.sin(random + System.currentTimeMillis() * 5.0E-4) * 0.25);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(random % 360));
            poseStack.translate(-0.5, -0.5, -0.5);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance()
               .getEntityRenderDispatcher()
               .render(stockpot.renderEntity, 1.0, 0.375F + entityY, 1.0, 0.0F, 0.0F, poseStack, buffer, packedLight);
            poseStack.popPose();
         }
      }
   }
}
