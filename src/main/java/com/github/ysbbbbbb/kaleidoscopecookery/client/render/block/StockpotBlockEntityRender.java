package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class StockpotBlockEntityRender implements BlockEntityRenderer<StockpotBlockEntity> {
   private final Context context;
   private final Function<Identifier, ISoupBaseRender> soupBaseRender;

   public StockpotBlockEntityRender(Context context) {
      this.context = context;
      this.soupBaseRender = Util.memoize(id -> {
         ISoupBase soupBase = SoupBaseManager.getSoupBase(id);
         return soupBase != null ? soupBase.getRender() : null;
      });
   }

   public void render(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         if (!(Boolean)stockpot.getBlockState().getValue(StockpotBlock.HAS_LID)) {
            int status = stockpot.getStatus();
            ISoupBaseRender soupBase = this.soupBaseRender.apply(stockpot.getSoupBaseId());
            if (status == 1) {
               soupBase.renderWhenPutIngredient(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, 0.38F);
               this.renderItems(stockpot, poseStack, buffer, packedLight, packedOverlay, false);
            } else if (status == 2) {
               StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
               soupBase.renderWhenCooking(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, visuals.cookingTexture(), 0.38F);
               this.renderItems(stockpot, poseStack, buffer, packedLight, packedOverlay, true);
            } else if (status == 3) {
               StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
               int takeoutCount = stockpot.getTakeoutCount();
               int maxCount = Math.min(stockpot.getResult().getCount(), 9);
               float soupHeight = 0.065F + 0.315F / maxCount * takeoutCount;
               soupBase.renderWhenFinished(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, visuals.finishedTexture(), soupHeight);
            }
         }
      }
   }

   private void renderItems(StockpotBlockEntity stockpot, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean isFinished) {
      NonNullList<ItemStack> items = stockpot.getInputs();
      items.forEach(stack -> {
         if (!stack.isEmpty()) {
            int random = stack.hashCode();
            long time = random + System.currentTimeMillis();
            float offsetX = random % 100 * 0.002F;
            float offsetZ = (float)(Math.sin(time * 5.0E-4) * 0.2);
            float offsetY = random % 50 * 0.004F;
            float yRot = (random % 2 == 0 ? -1 : 1) * 20 + random % 10;
            BakedModel model;
            if (isFinished) {
               model = ItemRenderReplacer.getModel(stockpot.getLevel(), stack, ItemRenderReplacerReloadListener.INSTANCE.stockpotFinished());
            } else {
               model = ItemRenderReplacer.getModel(stockpot.getLevel(), stack, ItemRenderReplacerReloadListener.INSTANCE.stockpotCooking());
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(85 + random % 10));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.9 + offsetX, 0.9 + offsetY, -0.5 + offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random % 360));
            this.context.getItemRenderer().render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, model);
            poseStack.popPose();
         }
      });
   }
}
