package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowHandLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowParrotOnShoulderLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ScarecrowRender extends LivingEntityRenderer<ScarecrowEntity, ScarecrowModel> {
   public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/entity/scarecrow.png");

   public ScarecrowRender(Context context) {
      super(context, new ScarecrowModel(context.bakeLayer(ScarecrowModel.LAYER_LOCATION)), 0.0F);
      this.addLayer(new ScarecrowHandLayer(this, context.getItemInHandRenderer(), context.getBlockRenderDispatcher()));
      this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
      this.addLayer(new ScarecrowParrotOnShoulderLayer(this, context.getModelSet()));
   }

   protected void setupRotations(ScarecrowEntity scarecrow, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
      float time = (float)(scarecrow.level().getGameTime() - scarecrow.lastHit) + partialTick;
      if (time < 5.0F) {
         poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(time / 1.5F * (float) Math.PI) * 3.0F));
      }
   }

   protected boolean shouldShowName(ScarecrowEntity scarecrow) {
      double distance = this.entityRenderDispatcher.distanceToSqr(scarecrow);
      return distance < 4096.0 && scarecrow.isCustomNameVisible();
   }

   public Identifier getTextureLocation(ScarecrowEntity pEntity) {
      return TEXTURE;
   }
}
