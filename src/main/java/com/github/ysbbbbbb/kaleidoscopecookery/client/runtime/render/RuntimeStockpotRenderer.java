package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.FluidSoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SimpleSoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class RuntimeStockpotRenderer implements BlockEntityRenderer<StockpotBlockEntity, RuntimeStockpotRenderer.State> {
   private final ItemModelResolver itemModelResolver;
   private final SpriteGetter sprites;
   private final EntityRenderDispatcher entityRenderer;

   public RuntimeStockpotRenderer(BlockEntityRendererProvider.Context context) {
      this.itemModelResolver = context.itemModelResolver();
      this.sprites = context.sprites();
      this.entityRenderer = context.entityRenderer();
   }

   @Override
   public State createRenderState() {
      return new State();
   }

   @Override
   public void extractRenderState(
      StockpotBlockEntity blockEntity,
      State state,
      float partialTicks,
      Vec3 cameraPosition,
      ModelFeatureRenderer.CrumblingOverlay breakProgress
   ) {
      BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
      state.hiddenByLid = blockEntity.getBlockState().getValue(StockpotBlock.HAS_LID);
      state.status = blockEntity.getStatus();
      state.age = blockEntity.getLevel() == null ? 0.0F : blockEntity.getLevel().getGameTime() + partialTicks;
      state.surface = null;
      if (!state.hiddenByLid && blockEntity.getStatus() > 0) {
         Identifier texture = null;
         int color = -1;
         if (blockEntity.getStatus() == 1) {
            if (blockEntity.getSoupBase() instanceof FluidSoupBase fluidSoupBase) {
               FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                  .get(fluidSoupBase.getFluid().defaultFluidState());
               TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();
               color = fluidModel.fluidTintSource() == null
                  ? -1
                  : fluidModel.fluidTintSource().color(fluidSoupBase.getFluid().defaultFluidState());
               state.surface = new Surface(sprite, color, 0.38F);
            } else if (blockEntity.getSoupBase() instanceof SimpleSoupBase simpleSoupBase) {
               texture = simpleSoupBase.getSoupBaseTexture();
            }
         } else {
            StockpotVisuals visuals = blockEntity.visuals == null ? StockpotVisuals.DEFAULT : blockEntity.visuals;
            texture = blockEntity.getStatus() == 2 ? visuals.cookingTexture() : visuals.finishedTexture();
         }
         if (texture != null) {
            TextureAtlasSprite sprite = this.sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, texture));
        int maxCount = Math.max(1, Math.min(blockEntity.getResult().getCount(), 9));
        float height = blockEntity.getStatus() == 3
                ? 0.065F + 0.315F / maxCount * Math.min(blockEntity.getTakeoutCount(), maxCount)
                : 0.38F;
            state.surface = new Surface(sprite, color, height);
         }
      }
      state.mobEntity = null;
      EntityType<?> mobType = mobType(blockEntity.getSoupBaseId());
      if (!state.hiddenByLid && state.status > 0 && state.status < 3 && mobType != null && blockEntity.getLevel() != null) {
         Entity entity = blockEntity.renderEntity;
         if (entity == null || entity.getType() != mobType) {
            entity = mobType.create(blockEntity.getLevel(), EntitySpawnReason.LOAD);
            blockEntity.renderEntity = entity;
            if (entity != null) {
               entity.setOnGround(true);
            }
         }
         if (entity != null) {
            state.mobEntity = this.entityRenderer.extractEntity(entity, partialTicks);
            state.mobEntity.lightCoords = state.lightCoords;
            state.mobScale = 0.48F / Math.max(1.0F, Math.max(entity.getBbWidth(), entity.getBbHeight()));
            state.mobYaw = Math.floorMod(blockEntity.getBlockPos().hashCode(), 360);
         }
      }
      state.items = new ArrayList<>();
      int index = 0;
      for (ItemStack stack : blockEntity.getInputs()) {
         if (!stack.isEmpty() && index < 9) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
               itemState,
               stack,
               ItemDisplayContext.FIXED,
               blockEntity.getLevel(),
               null,
               (int)(blockEntity.getBlockPos().asLong() + index)
            );
            state.items.add(new ItemDisplay(itemState, stack.hashCode()));
            index++;
         }
      }
   }

   @Override
   public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
      if (state.hiddenByLid) {
         return;
      }

      if (state.surface != null) {
         TextureAtlasSprite sprite = state.surface.sprite();
         int color = state.surface.color();
         float y = state.surface.height();
         int light = state.lightCoords;
         collector.submitCustomGeometry(
            poseStack,
            RenderTypes.entityTranslucent(net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS),
            (pose, vertexConsumer) -> renderSurface(pose.pose(), vertexConsumer, sprite, color, light, y)
         );
      }

      if (state.mobEntity != null) {
         poseStack.pushPose();
         float bob = (float)Math.sin(state.age * 0.1F + state.mobYaw) * 0.035F;
         poseStack.translate(0.5F, 0.39F + bob, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(state.mobYaw));
         poseStack.scale(state.mobScale, state.mobScale, state.mobScale);
         this.entityRenderer.submit(state.mobEntity, camera, 0.0, 0.0, 0.0, poseStack, collector);
         poseStack.popPose();
      }

      for (ItemDisplay item : state.items) {
         int hash = item.hash();
         float offsetX = Math.floorMod(hash, 100) * 0.002F;
         float offsetY = Math.floorMod(hash, 50) * 0.004F;
         float wave = (float)Math.sin((hash + state.age * 50.0F) * 0.0005F) * 0.2F;
         float yaw = (Math.floorMod(hash, 2) == 0 ? -20.0F : 20.0F) + Math.floorMod(hash, 10);
         poseStack.pushPose();
         poseStack.mulPose(Axis.XP.rotationDegrees(85.0F + Math.floorMod(hash, 10)));
         poseStack.scale(0.5F, 0.5F, 0.5F);
         poseStack.translate(0.9F + offsetX, 0.9F + offsetY, -0.5F + wave);
         poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
         poseStack.mulPose(Axis.ZP.rotationDegrees(Math.floorMod(hash, 360)));
         item.model().submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
         poseStack.popPose();
      }
   }

   private static EntityType<?> mobType(Identifier soupBase) {
      if (ModSoupBases.AXOLOTL_BUCKET.equals(soupBase)) {
         return EntityType.AXOLOTL;
      }
      if (ModSoupBases.COD_BUCKET.equals(soupBase)) {
         return EntityType.COD;
      }
      if (ModSoupBases.SALMON_BUCKET.equals(soupBase)) {
         return EntityType.SALMON;
      }
      if (ModSoupBases.TROPICAL_FISH_BUCKET.equals(soupBase)) {
         return EntityType.TROPICAL_FISH;
      }
      if (ModSoupBases.PUFFERFISH_BUCKET.equals(soupBase)) {
         return EntityType.PUFFERFISH;
      }
      if (ModSoupBases.TADPOLE_BUCKET.equals(soupBase)) {
         return EntityType.TADPOLE;
      }
      return null;
   }

   private static void renderSurface(
      Matrix4f matrix, VertexConsumer vertexConsumer, TextureAtlasSprite sprite, int color, int light, float y
   ) {
      float min = 0.1875F;
      float max = 0.8125F;
      vertexConsumer.addVertex(matrix, min, y, min).setColor(color).setUv(sprite.getU0(), sprite.getV0())
         .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, min, y, max).setColor(color).setUv(sprite.getU0(), sprite.getV(0.625F))
         .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, max, y, max).setColor(color).setUv(sprite.getU(0.625F), sprite.getV(0.625F))
         .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
      vertexConsumer.addVertex(matrix, max, y, min).setColor(color).setUv(sprite.getU(0.625F), sprite.getV0())
         .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
   }

   private record Surface(TextureAtlasSprite sprite, int color, float height) {
   }

   private record ItemDisplay(ItemStackRenderState model, int hash) {
   }

   public static final class State extends BlockEntityRenderState {
      private List<ItemDisplay> items = List.of();
      private boolean hiddenByLid;
      private Surface surface;
      private int status;
      private float age;
      private EntityRenderState mobEntity;
      private float mobScale;
      private float mobYaw;
   }
}
