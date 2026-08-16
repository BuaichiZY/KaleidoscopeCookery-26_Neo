package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlateRegistry {
   public static final Map<Identifier, PlateRegistry.PlateData> PLATE_DATA_MAP = Maps.newLinkedHashMap();
   public static Identifier SHENGJIAN_MANTOU_PLATE;
   public static Identifier BAOZI_PLATE;
   public static Identifier QINGTUAN_PLATE;
   public static Identifier STICKY_CANDY_PLATE;
   public static Identifier STICKY_RICE_CAKE_PLATE;
   public static Identifier ZONGZI_PLATE;
   public static Identifier BERRY_PLATTER;
   public static Identifier APPLE_PLATTER;
   public static Identifier TOMATO_PLATTER;
   public static Identifier WATERMELON_PLATTER;
   public static Identifier CHORUS_FRUIT_PLATTER;

   public static void init() {
      PlateRegistry registry = new PlateRegistry();
      SHENGJIAN_MANTOU_PLATE = registry.registerPlateData(
         "shengjian_mantou_plate", PlateRegistry.PlateData.create(5).setServingItems(ModItems.SHENGJIAN_MANTOU).setLootItem(Items.BOWL)
      );
      BAOZI_PLATE = registry.registerPlateData(
         "baozi_plate", PlateRegistry.PlateData.create(5).setServingItems(ModItems.BAOZI).setLootItem(Items.BOWL)
      );
      QINGTUAN_PLATE = registry.registerPlateData(
         "qingtuan_plate", PlateRegistry.PlateData.create(4).setServingItems(ModItems.QINGTUAN).setLootItem(Items.BOWL)
      );
      STICKY_CANDY_PLATE = registry.registerPlateData(
         "sticky_candy_plate", PlateRegistry.PlateData.create(4).setServingItems(ModItems.STICKY_CANDY).setLootItem(Items.BOWL)
      );
      STICKY_RICE_CAKE_PLATE = registry.registerPlateData(
         "sticky_rice_cake_plate", PlateRegistry.PlateData.create(5).setServingItems(ModItems.STICKY_RICE_CAKE).setLootItem(Items.BOWL)
      );
      ZONGZI_PLATE = registry.registerPlateData("zongzi_plate", PlateRegistry.PlateData.create(4).setServingItems(ModItems.ZONGZI).setLootItem(Items.BOWL));
      BERRY_PLATTER = registry.registerPlateData(
         "berry_platter",
         PlateRegistry.PlateData.create(4).addServingItems(() -> Items.SWEET_BERRIES, () -> Items.GLOW_BERRIES).setLootItem(Items.BOWL).platterAABB()
      );
      APPLE_PLATTER = registry.registerPlateData(
         "apple_platter", PlateRegistry.PlateData.create(4).setServingItems(() -> Items.APPLE).setLootItem(Items.BOWL).platterAABB()
      );
      TOMATO_PLATTER = registry.registerPlateData(
         "tomato_platter", PlateRegistry.PlateData.create(5).setServingItems(ModItems.TOMATO).setLootItem(Items.BOWL).platterAABB()
      );
      WATERMELON_PLATTER = registry.registerPlateData(
         "watermelon_platter", PlateRegistry.PlateData.create(3).setServingItems(() -> Items.MELON_SLICE).setLootItem(Items.BOWL).platterAABB()
      );
      CHORUS_FRUIT_PLATTER = registry.registerPlateData(
         "chorus_fruit_platter", PlateRegistry.PlateData.create(5).setServingItems(() -> Items.CHORUS_FRUIT).setLootItem(Items.BOWL).platterAABB()
      );
   }

   public Identifier registerPlateData(Identifier id, PlateRegistry.PlateData data) {
      PLATE_DATA_MAP.put(id, data);
      return id;
   }

   public Identifier registerPlateData(String name, PlateRegistry.PlateData data) {
      return this.registerPlateData(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name), data);
   }

   public static int getCount(Identifier id) {
      return PLATE_DATA_MAP.containsKey(id) ? PLATE_DATA_MAP.get(id).getMaxCount() : 0;
   }

   public static Item getItem(Identifier name) {
      return (Item)BuiltInRegistries.ITEM.getValue(name);
   }

   public static Block getBlock(Identifier name) {
      return (Block)BuiltInRegistries.BLOCK.getValue(name);
   }

   public static final class PlateData {
      private final int maxCount;
      private final List<Supplier<Item>> servingItems = Lists.newArrayList();
      private final List<ItemLike> lootItems = Lists.newArrayList();
      @Nullable
      private VoxelShape aabb = null;

      public PlateData(int maxCount) {
         this.maxCount = maxCount;
      }

      public static PlateRegistry.PlateData create(int maxCount) {
         return new PlateRegistry.PlateData(maxCount);
      }

      @SafeVarargs
      public final PlateRegistry.PlateData addServingItems(Supplier<Item>... servingItems) {
         this.servingItems.addAll(Arrays.stream(servingItems).toList());
         return this;
      }

      public PlateRegistry.PlateData setServingItems(Supplier<Item> servingItem) {
         this.servingItems.clear();
         this.servingItems.add(servingItem);
         return this;
      }

      public PlateRegistry.PlateData addLootItems(ItemLike... lootItems) {
         this.lootItems.addAll(Arrays.stream(lootItems).toList());
         return this;
      }

      public PlateRegistry.PlateData setLootItem(ItemLike lootItem) {
         this.lootItems.clear();
         this.lootItems.add(lootItem);
         return this;
      }

      public PlateRegistry.PlateData setAABB(VoxelShape aabb) {
         this.aabb = aabb;
         return this;
      }

      public PlateRegistry.PlateData platterAABB() {
         this.aabb = Shapes.or(
            Block.box(4.0, 0.0, 4.0, 12.0, 2.0, 12.0), new VoxelShape[]{Block.box(6.0, 2.0, 6.0, 10.0, 4.0, 10.0), Block.box(1.0, 4.0, 1.0, 15.0, 6.0, 15.0)}
         );
         return this;
      }

      public int getMaxCount() {
         return this.maxCount;
      }

      @Nullable
      public VoxelShape getAABB() {
         return this.aabb;
      }

      public List<Supplier<Item>> getServingItems() {
         return this.servingItems;
      }

      public List<ItemLike> getLootItems() {
         return this.lootItems;
      }

   }
}
