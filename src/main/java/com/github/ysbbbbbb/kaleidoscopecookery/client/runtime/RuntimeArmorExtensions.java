package com.github.ysbbbbbb.kaleidoscopecookery.client.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.client.runtime.model.RuntimeStrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StrawHatItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class RuntimeArmorExtensions {
   private static final Identifier STRAW_HAT_TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/models/armor/straw_hat.png"
   );
   private static final Identifier STRAW_HAT_FLOWER_TEXTURE = Identifier.fromNamespaceAndPath(
      "kaleidoscope_cookery", "textures/models/armor/straw_hat_flower.png"
   );

   private RuntimeArmorExtensions() {
   }

   @SubscribeEvent
   public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
      event.registerItem(new IClientItemExtensions() {
         private RuntimeStrawHatModel model;

         @Override
         public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
            return this.getModel();
         }

         @Override
         public Model getGenericArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
            return this.getModel();
         }

         @Override
         public Identifier getArmorTexture(
            ItemStack stack,
            EquipmentClientInfo.LayerType type,
            EquipmentClientInfo.Layer layer,
            Identifier fallback
         ) {
            return stack.getItem() instanceof StrawHatItem hat && hat.hasFlower()
               ? STRAW_HAT_FLOWER_TEXTURE
               : STRAW_HAT_TEXTURE;
         }

         @Override
         public int getArmorLayerTintColor(ItemStack stack, EquipmentClientInfo.Layer layer, int layerIndex, int fallbackColor) {
            return 0xFFFFFFFF;
         }

         private RuntimeStrawHatModel getModel() {
            if (this.model == null) {
               this.model = new RuntimeStrawHatModel(
                  Minecraft.getInstance().getEntityModels().bakeLayer(RuntimeStrawHatModel.LAYER_LOCATION)
               );
            }
            return this.model;
         }
      }, new Item[]{ModItems.STRAW_HAT.get(), ModItems.STRAW_HAT_FLOWER.get()});
   }
}
