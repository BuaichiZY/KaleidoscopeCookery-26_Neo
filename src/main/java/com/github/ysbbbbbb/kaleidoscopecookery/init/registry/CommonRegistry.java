package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.PlateBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.OilPotDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.TeacupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteOneByTwoBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModSoupBases;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.PlateBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeacupItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class CommonRegistry {
   @SubscribeEvent
   public static void onSetupEvent(FMLCommonSetupEvent event) {
      event.enqueueWork(ModSoupBases::registerAll);
      event.enqueueWork(CommonRegistry::addDispenserBehavior);
   }

   @SubscribeEvent
   public static void onBlockRegistryEvent(RegisterEvent event) {
      if (event.getRegistry().equals(BuiltInRegistries.BLOCK)) {
         FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> ModRegistrationProperties.withBlockId(resourceLocation, () -> {
            FoodBiteBlock biteBlock;
            if (data.blockType() == FoodBiteRegistry.BlockType.ONE_BY_TWO) {
               biteBlock = new FoodBiteOneByTwoBlock(data.blockFood(), data.maxBites(), data.animateTick());
            } else {
               biteBlock = new FoodBiteBlock(data.blockFood(), data.maxBites(), data.animateTick());
            }

            VoxelShape aabb = data.getAABB();
            if (aabb != null) {
               biteBlock.setAABB(aabb);
            }

            return biteBlock;
          })));
         TeacupRegistry.TEACUP_DATA_MAP.forEach((resourceLocation, data) -> event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> ModRegistrationProperties.withBlockId(resourceLocation, () -> {
            TeacupBlock teacupBlock = new TeacupBlock(data.getMaxCount());
            VoxelShape aabb = data.getAABB();
            if (aabb != null) {
               teacupBlock.setAABB(aabb);
            }

            return teacupBlock;
          })));
         PlateRegistry.PLATE_DATA_MAP.forEach((resourceLocation, data) -> event.register(BuiltInRegistries.BLOCK.key(), resourceLocation, () -> ModRegistrationProperties.withBlockId(resourceLocation, () -> {
            PlateBlock plateBlock = new PlateBlock(data.getMaxCount(), data.getServingItems());
            VoxelShape aabb = data.getAABB();
            if (aabb != null) {
               plateBlock.setAABB(aabb);
            }

            return plateBlock;
          })));
      }

      if (event.getRegistry().equals(BuiltInRegistries.ITEM)) {
         FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> {
            Block block = (Block)BuiltInRegistries.BLOCK.getValue(resourceLocation);
            ItemLike first = data.getLootItems().getFirst();
             event.register(BuiltInRegistries.ITEM.key(), resourceLocation, () -> ModRegistrationProperties.withItemId(resourceLocation, () -> new BowlFoodBlockItem(block, data.itemFood(), first)));
         });
         TeacupRegistry.TEACUP_DATA_MAP.forEach((resourceLocation, data) -> {
            Block block = (Block)BuiltInRegistries.BLOCK.getValue(resourceLocation);
             event.register(BuiltInRegistries.ITEM.key(), resourceLocation, () -> ModRegistrationProperties.withItemId(resourceLocation, () -> new TeacupItem(block, data.getEffects())));
         });
         PlateRegistry.PLATE_DATA_MAP.forEach((resourceLocation, data) -> {
            Block block = (Block)BuiltInRegistries.BLOCK.getValue(resourceLocation);
             event.register(BuiltInRegistries.ITEM.key(), resourceLocation, () -> ModRegistrationProperties.withItemId(resourceLocation, () -> new PlateBlockItem(block, resourceLocation.getPath())));
         });
      }
   }

   private static void addDispenserBehavior() {
      DispenserBlock.registerBehavior((ItemLike)ModItems.OIL_POT.get(), new OilPotDispenseBehavior());
   }
}
