package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.Identifier;

public class SitRenderer extends EntityRenderer<SitEntity> {
   private static final Identifier EMPTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/entity/empty.png");

   public SitRenderer(Context context) {
      super(context);
   }

   public void render(SitEntity entitySit, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
   }

   public Identifier getTextureLocation(SitEntity entitySit) {
      return EMPTY;
   }
}
