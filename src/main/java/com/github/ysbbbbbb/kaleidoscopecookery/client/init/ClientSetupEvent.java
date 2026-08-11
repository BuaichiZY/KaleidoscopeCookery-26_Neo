package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.CustomArmPose;
import com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay.PotOverlay;
import com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay.TrashCanOverlay;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.StrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.ChairBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.ChoppingBoardBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.FoodBiteThreeByThreeBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.FruitBasketBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.KitchenwareRacksBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.MillstoneBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.PotBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.RecipeBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.ShawarmaSpitBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.SteamerBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.StockpotBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.TableBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.TeapotBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.TrashCanBlockEntityRender;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.LegacyPackRepositorySource;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init.PonderCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RawDoughItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StockpotLidItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial.Layer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = Dist.CLIENT, modid = "kaleidoscope_cookery")
public class ClientSetupEvent {
   @SubscribeEvent
   public static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.KITCHEN_SHOVEL.get(), KitchenShovelItem.HAS_OIL_PROPERTY, KitchenShovelItem::getTexture));
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.STOCKPOT_LID.get(), StockpotLidItem.USING_PROPERTY, StockpotLidItem::getTexture));
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.OIL_POT.get(), OilPotItem.HAS_OIL_PROPERTY, OilPotItem::getTexture));
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.RAW_DOUGH.get(), RawDoughItem.PULL_PROPERTY, RawDoughItem::getTexture));
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.RECIPE_ITEM.get(), RecipeItem.HAS_RECIPE_PROPERTY, RecipeItem::getTexture));
      event.enqueueWork(
         () -> ItemProperties.register(
            (Item)ModItems.TRANSMUTATION_LUNCH_BAG.get(), TransmutationLunchBagItem.HAS_ITEMS_PROPERTY, TransmutationLunchBagItem::getTexture
         )
      );
      event.enqueueWork(() -> ItemProperties.register((Item)ModItems.STEAMER.get(), SteamerItem.HAS_ITEMS, SteamerItem::getTexture));
      PonderCompat.init();
   }

   @SubscribeEvent
   public static void onEntityRenderers(RegisterRenderers evt) {
      BlockEntityRenderers.register(ModBlocks.POT_BE.get(), PotBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.FRUIT_BASKET_BE.get(), FruitBasketBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.CHOPPING_BOARD_BE.get(), ChoppingBoardBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.STOCKPOT_BE.get(), StockpotBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.KITCHENWARE_RACKS_BE.get(), KitchenwareRacksBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.CHAIR_BE.get(), ChairBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.TABLE_BE.get(), TableBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.SHAWARMA_SPIT_BE.get(), ShawarmaSpitBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.MILLSTONE_BE.get(), MillstoneBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.RECIPE_BLOCK_BE.get(), RecipeBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.STEAMER_BE.get(), SteamerBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.FOOD_BITE_THREE_BY_THREE_BE.get(), FoodBiteThreeByThreeBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.TEAPOT_BE.get(), TeapotBlockEntityRender::new);
      BlockEntityRenderers.register(ModBlocks.TRASH_CAN_BE.get(), TrashCanBlockEntityRender::new);
   }

   @SubscribeEvent
   public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
      event.registerAbove(VanillaGuiLayers.CROSSHAIR, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot_overlay"), new PotOverlay());
      event.registerAbove(VanillaGuiLayers.CROSSHAIR, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "trash_can_overlay"), new TrashCanOverlay());
   }

   @SubscribeEvent
   public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
      event.registerReloadListener(new ItemRenderReplacerReloadListener());
   }

   @SubscribeEvent
   public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
      event.registerItem(new IClientItemExtensions() {
         private final ArmPose liftPose = (ArmPose)CustomArmPose.LIFT_POSE.getValue();

         public ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
            return !stack.isEmpty() ? this.liftPose : ArmPose.EMPTY;
         }
      }, new Item[]{(Item)ModItems.COLD_CUT_HAM_SLICES.get()});
      event.registerItem(new IClientItemExtensions() {
         private StrawHatModel cachedModel = null;

         public int getArmorLayerTintColor(ItemStack stack, LivingEntity entity, Layer layer, int layerIdx, int fallbackColor) {
            return layerIdx == 0 ? super.getArmorLayerTintColor(stack, entity, layer, layerIdx, fallbackColor) : 0;
         }

         @NotNull
         public Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            if (this.cachedModel == null) {
               this.cachedModel = new StrawHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(StrawHatModel.LAYER_LOCATION));
            }

            ModelPart head = this.cachedModel.getHead();
            head.copyFrom(original.head);
            return this.cachedModel;
         }
      }, new Item[]{(Item)ModItems.STRAW_HAT.get(), (Item)ModItems.STRAW_HAT_FLOWER.get()});
   }

   @SubscribeEvent
   public static void onAddPackFinders(AddPackFindersEvent event) {
      if (event.getPackType() == PackType.CLIENT_RESOURCES) {
         event.addRepositorySource(new LegacyPackRepositorySource());
      }
   }
}
