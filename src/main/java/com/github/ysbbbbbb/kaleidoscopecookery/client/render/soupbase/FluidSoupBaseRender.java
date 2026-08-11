package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class FluidSoupBaseRender implements ISoupBaseRender {
   private final Fluid fluid;

   public FluidSoupBaseRender(Fluid fluid) {
      this.fluid = fluid;
   }

   @Override
   public void renderWhenPutIngredient(
      StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float soupHeight
   ) {
      ISoupBaseRender.renderSurface(this.getStillFluidSprite(this.fluid), this.getFluidColor(this.fluid), poseStack, buffer, packedLight, soupHeight);
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
      Function<Identifier, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
      TextureAtlasSprite sprite = atlas.apply(cookingTexture);
      ISoupBaseRender.renderSurface(sprite, -1, poseStack, buffer, packedLight, soupHeight);
   }

   @Override
   public void renderWhenFinished(
      StockpotBlockEntity stockpot,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      Identifier finishedTexture,
      float soupHeight
   ) {
      Function<Identifier, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
      TextureAtlasSprite sprite = atlas.apply(finishedTexture);
      ISoupBaseRender.renderSurface(sprite, -1, poseStack, buffer, packedLight, soupHeight);
   }

   private TextureAtlasSprite getStillFluidSprite(Fluid fluid) {
      IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
      Identifier fluidStill = renderProperties.getStillTexture();
      return (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
   }

   private int getFluidColor(Fluid fluid) {
      IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid);
      return ext.getTintColor();
   }
}
