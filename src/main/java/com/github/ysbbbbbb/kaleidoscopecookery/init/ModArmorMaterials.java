package com.github.ysbbbbbb.kaleidoscopecookery.init;

import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class ModArmorMaterials {
   private static final ResourceKey<EquipmentAsset> FARMER_ASSET = ResourceKey.create(
      EquipmentAssets.ROOT_ID,
      Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "cookery_farmer")
   );

   public static final ArmorMaterial FARMER = new ArmorMaterial(
      15,
      Map.of(
         ArmorType.HELMET, 1,
         ArmorType.CHESTPLATE, 4,
         ArmorType.LEGGINGS, 5,
         ArmorType.BOOTS, 2,
         ArmorType.BODY, 4
      ),
      12,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      0.0F,
      0.0F,
      ItemTags.REPAIRS_LEATHER_ARMOR,
      FARMER_ASSET
   );

   private ModArmorMaterials() {
   }
}
