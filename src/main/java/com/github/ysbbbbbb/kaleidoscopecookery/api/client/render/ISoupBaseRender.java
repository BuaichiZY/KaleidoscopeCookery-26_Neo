package com.github.ysbbbbbb.kaleidoscopecookery.api.client.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public interface ISoupBaseRender {
   static void renderSurface(TextureAtlasSprite sprite, int color, PoseStack poseStack, MultiBufferSource buffer, int light, float y) {
      VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
      Matrix4f matrix = poseStack.last().pose();
      float min = 0.1875F;
      float max = 0.8125F;
      vertexConsumer.addVertex(matrix, min, y, min)
         .setColor(color)
         .setUv(sprite.getU0(), sprite.getV0())
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(light)
         .setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, min, y, max)
         .setColor(color)
         .setUv(sprite.getU0(), sprite.getV(0.625F))
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(light)
         .setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, max, y, max)
         .setColor(color)
         .setUv(sprite.getU(0.625F), sprite.getV(0.625F))
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(light)
         .setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, max, y, min)
         .setColor(color)
         .setUv(sprite.getU(0.625F), sprite.getV0())
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(light)
         .setNormal(0.0F, 1.0F, 0.0F);
   }

   void renderWhenPutIngredient(StockpotBlockEntity var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, int var6, float var7);

   void renderWhenCooking(StockpotBlockEntity var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, int var6, Identifier var7, float var8);

   void renderWhenFinished(StockpotBlockEntity var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, int var6, Identifier var7, float var8);
}
