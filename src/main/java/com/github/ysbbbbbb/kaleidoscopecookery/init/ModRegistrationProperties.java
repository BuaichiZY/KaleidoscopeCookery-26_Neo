package com.github.ysbbbbbb.kaleidoscopecookery.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * Bridges the legacy no-argument block and item constructors to the registry-id
 * aware properties required by Minecraft 26.
 */
public final class ModRegistrationProperties {
   private static final ThreadLocal<ResourceKey<Block>> BLOCK_ID = new ThreadLocal<>();
   private static final ThreadLocal<ResourceKey<Item>> ITEM_ID = new ThreadLocal<>();

   private ModRegistrationProperties() {
   }

   public static <B extends Block> B withBlockId(Identifier id, Supplier<? extends B> factory) {
      ResourceKey<Block> previous = BLOCK_ID.get();
      BLOCK_ID.set(ResourceKey.create(Registries.BLOCK, id));

      try {
         return factory.get();
      } finally {
         restore(BLOCK_ID, previous);
      }
   }

   public static <I extends Item> I withItemId(Identifier id, Supplier<? extends I> factory) {
      ResourceKey<Item> previous = ITEM_ID.get();
      ITEM_ID.set(ResourceKey.create(Registries.ITEM, id));

      try {
         return factory.get();
      } finally {
         restore(ITEM_ID, previous);
      }
   }

   public static BlockBehaviour.Properties blockProperties() {
      ResourceKey<Block> id = BLOCK_ID.get();
      if (id == null) {
         throw new IllegalStateException("Block properties requested outside a block registration");
      }
      return BlockBehaviour.Properties.of().setId(id);
   }

   public static Item.Properties itemProperties() {
      ResourceKey<Item> id = ITEM_ID.get();
      if (id == null) {
         throw new IllegalStateException("Item properties requested outside an item registration");
      }
      Item.Properties properties = new Item.Properties().setId(id);
      // Minecraft 26.3 reads the default compostable component for player and hopper insertion.
      return switch (id.identifier().getPath()) {
         case "tomato_seed", "chili_seed", "lettuce_seed", "wild_rice", "rice", "tea_seed",
              "fresh_tea_leaves", "dried_tea_leaves" -> properties.compostable(ContextIntProviders.COMPOSTABLE_LOW);
         case "tomato", "red_chili", "green_chili", "lettuce",
              "rice_panicle" -> properties.compostable(ContextIntProviders.COMPOSTABLE_MEDIUM);
         case "caterpillar" -> properties.compostable(ContextIntProviders.COMPOSTABLE_ALWAYS_ADD_ONE);
         default -> properties;
      };
   }

   /**
    * Minecraft 26 no longer makes {@link net.minecraft.world.item.BlockItem}
    * inherit its block's translation key automatically. Use this for block
    * items whose item and block registry paths are identical.
    */
   public static Item.Properties blockItemProperties() {
      return itemProperties().useBlockDescriptionPrefix();
   }

   /**
    * Uses the translation key of a block whose registry path differs from the
    * item path, such as {@code recipe_item -> recipe_block}.
    */
   public static Item.Properties blockItemProperties(String blockPath) {
      return itemProperties().overrideDescription("block.kaleidoscope_cookery." + blockPath);
   }

   private static <T> void restore(ThreadLocal<T> local, T previous) {
      if (previous == null) {
         local.remove();
      } else {
         local.set(previous);
      }
   }
}
