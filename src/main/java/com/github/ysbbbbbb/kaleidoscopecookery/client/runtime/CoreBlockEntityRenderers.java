package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeMillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeTeapotModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeTrashCanModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeStrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeChoppingBoardRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeChairRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeFruitBasketRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeKitchenwareRacksRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeMillstoneRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimePotRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeRecipeBlockRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeScarecrowRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeShawarmaSpitRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeSitRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeStockpotRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeSteamerRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeTableRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeTeapotRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.render.RuntimeTrashCanRenderer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class CoreBlockEntityRenderers {
   private CoreBlockEntityRenderers() {
   }

   @SubscribeEvent
   public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
      event.registerLayerDefinition(RuntimeMillstoneModel.LAYER_LOCATION, RuntimeMillstoneModel::createBodyLayer);
      event.registerLayerDefinition(RuntimeScarecrowModel.LAYER_LOCATION, RuntimeScarecrowModel::createBodyLayer);
      event.registerLayerDefinition(RuntimeTeapotModel.LAYER_LOCATION, RuntimeTeapotModel::createBodyLayer);
      event.registerLayerDefinition(RuntimeTrashCanModel.LAYER_LOCATION, RuntimeTrashCanModel::createBodyLayer);
      event.registerLayerDefinition(RuntimeStrawHatModel.LAYER_LOCATION, RuntimeStrawHatModel::createBodyLayer);
   }

   @SubscribeEvent
   public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
      event.registerBlockEntityRenderer(ModBlocks.CHOPPING_BOARD_BE.get(), RuntimeChoppingBoardRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.MILLSTONE_BE.get(), RuntimeMillstoneRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.SHAWARMA_SPIT_BE.get(), RuntimeShawarmaSpitRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.POT_BE.get(), RuntimePotRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.STOCKPOT_BE.get(), RuntimeStockpotRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.STEAMER_BE.get(), RuntimeSteamerRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.TEAPOT_BE.get(), RuntimeTeapotRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.KITCHENWARE_RACKS_BE.get(), RuntimeKitchenwareRacksRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.FRUIT_BASKET_BE.get(), RuntimeFruitBasketRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.RECIPE_BLOCK_BE.get(), RuntimeRecipeBlockRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.TABLE_BE.get(), RuntimeTableRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.CHAIR_BE.get(), RuntimeChairRenderer::new);
      event.registerBlockEntityRenderer(ModBlocks.TRASH_CAN_BE.get(), RuntimeTrashCanRenderer::new);
      event.registerEntityRenderer(ModEntities.SCARECROW.get(), RuntimeScarecrowRenderer::new);
      event.registerEntityRenderer(ModEntities.SIT.get(), RuntimeSitRenderer::new);
      event.registerEntityRenderer(ModEntities.THROWABLE_BAOZI.get(), ThrownItemRenderer::new);
   }
}
