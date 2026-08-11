package com.github.ysbbbbbb.kaleidoscopecookery.init.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface TagMod {
   TagKey<Item> COOKERY_MOD_ITEMS = itemTag("cookery_mod_items");
   TagKey<Item> COOKERY_MOD_SEEDS = itemTag("cookery_mod_seeds");
   TagKey<Item> LIT_STOVE = itemTag("lit_stove");
   TagKey<Item> EXTINGUISH_STOVE = itemTag("extinguish_stove");
   TagKey<Item> OIL = itemTag("oil");
   TagKey<Item> CATERPILLARS = itemTag("caterpillars");
   TagKey<Item> STRAW_HAT = itemTag("straw_hat");
   TagKey<Item> STRAW_BALE = itemTag("straw_bale");
   TagKey<Item> KITCHEN_KNIFE = itemTag("kitchen_knife");
   TagKey<Item> KITCHEN_SHOVEL = itemTag("kitchen_shovel");
   TagKey<Item> FARMER_ARMOR = itemTag("farmer_armor");
   TagKey<Item> INGREDIENT_BLOCKLIST = itemTag("ingredient_blocklist");
   TagKey<Item> INGREDIENT_CONTAINER = itemTag("ingredient_container");
   TagKey<Item> BOWL_CONTAINER = itemTag("bowl_container");
   TagKey<Item> BUCKET_CONTAINER = itemTag("bucket_container");
   TagKey<Item> GLASS_BOTTLE_CONTAINER = itemTag("glass_bottle_container");
   TagKey<Item> MEALS = itemTag("meals");
   TagKey<Item> FEASTS = itemTag("feasts");
   TagKey<Block> TUNDRA_STRIDER_SPEED_BLOCKS = blockTag("tundra_strider_speed_blocks");
   TagKey<Block> WARMTH_HEAT_SOURCE_BLOCKS = blockTag("warmth_heat_source_blocks");
   TagKey<Block> CAT_LIE_ON_BLOCKS = blockTag("cat_lie_on_blocks");
   TagKey<Block> HEAT_SOURCE_BLOCKS_WITHOUT_LIT = blockTag("heat_source_blocks_without_lit");
   TagKey<Block> COOK_STOOL = blockTag("cook_stool");
   TagKey<Block> CHAIR = blockTag("chair");
   TagKey<Block> TABLE = blockTag("table");
   TagKey<Block> SITTABLE = blockTag("sittable");
   TagKey<Block> RICE_PLANTABLE = blockTag("rice_plantable");
   TagKey<Block> SICKLE_HARVEST_BLACKLIST = blockTag("sickle_harvest_blacklist");
   TagKey<EntityType<?>> PIG_OIL_SOURCE = entityTag("pig_oil_source");
   TagKey<EntityType<?>> MILLSTONE_BINDABLE = entityTag("millstone_bindable");
   TagKey<EntityType<?>> RICE_GROWTH_BOOSTER = entityTag("rice_growth_booster");
   TagKey<DamageType> SATIATED_SHIELD_WEAKNESS = damageTypeTag("satiated_shield_weakness");

   static TagKey<Item> itemTag(String name) {
      return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }

   static TagKey<Block> blockTag(String name) {
      return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }

   static TagKey<EntityType<?>> entityTag(String name) {
      return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }

   static TagKey<DamageType> damageTypeTag(String name) {
      return TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }
}
