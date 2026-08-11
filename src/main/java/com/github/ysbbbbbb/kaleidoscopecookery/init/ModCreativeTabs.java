package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
   public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "kaleidoscope_cookery");
   public static Supplier<CreativeModeTab> COOKERY_MAIN_TAB = TABS.register(
      "cookery_main",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_main.name"))
         .icon(() -> ((Item)ModItems.IRON_KITCHEN_KNIFE.get()).getDefaultInstance())
         .displayItems((par, output) -> {
            output.accept((ItemLike)ModItems.STOVE.get());
            output.accept((ItemLike)ModItems.SHAWARMA_SPIT.get());
            output.accept((ItemLike)ModItems.STRAW_BLOCK.get());
            output.accept((ItemLike)ModItems.OIL_BLOCK.get());
            output.accept((ItemLike)ModItems.POT.get());
            output.accept((ItemLike)ModItems.STOCKPOT.get());
            output.accept((ItemLike)ModItems.STOCKPOT_LID.get());
            output.accept((ItemLike)ModItems.CHOPPING_BOARD.get());
            output.accept((ItemLike)ModItems.MILLSTONE.get());
            output.accept((ItemLike)ModItems.STEAMER.get());
            output.accept((ItemLike)ModItems.TEAPOT.get());
            output.accept((ItemLike)ModItems.TRASH_CAN.get());
            output.accept((ItemLike)ModItems.KITCHENWARE_RACKS.get());
            output.accept((ItemLike)ModItems.FRUIT_BASKET.get());
            output.accept((ItemLike)ModItems.SCARECROW.get());
            output.accept((ItemLike)ModItems.ENAMEL_BASIN.get());
            output.accept((ItemLike)ModItems.OIL_POT.get());
            output.accept(OilPotItem.getFullOilPot());
            output.accept((ItemLike)ModItems.OIL.get());
            output.accept((ItemLike)ModItems.RECIPE_ITEM.get());
            output.accept((ItemLike)ModItems.TRANSMUTATION_LUNCH_BAG.get());
            output.accept((ItemLike)ModItems.FLOUR.get());
            output.accept((ItemLike)ModItems.RAW_DOUGH.get());
            output.accept((ItemLike)ModItems.RAW_NOODLES.get());
            output.accept((ItemLike)ModItems.STUFFED_DOUGH_FOOD.get());
            output.accept((ItemLike)ModItems.RAW_ZONGZI.get());
            output.accept((ItemLike)ModItems.RAW_BAMBOO_TUBE_RICE.get());
            output.accept((ItemLike)ModItems.CHILI_RISTRA.get());
            output.accept((ItemLike)ModItems.STRUNG_MUSHROOMS.get());
            output.accept((ItemLike)ModItems.RICE_SEED.get());
            output.accept((ItemLike)ModItems.WILD_RICE_SEED.get());
            output.accept((ItemLike)ModItems.TOMATO_SEED.get());
            output.accept((ItemLike)ModItems.CHILI_SEED.get());
            output.accept((ItemLike)ModItems.LETTUCE_SEED.get());
            output.accept((ItemLike)ModItems.KITCHEN_SHOVEL.get());
            output.accept((ItemLike)ModItems.SICKLE.get());
            output.accept((ItemLike)ModItems.GOLD_KITCHEN_KNIFE.get());
            output.accept((ItemLike)ModItems.IRON_KITCHEN_KNIFE.get());
            output.accept((ItemLike)ModItems.DIAMOND_KITCHEN_KNIFE.get());
            output.accept((ItemLike)ModItems.NETHERITE_KITCHEN_KNIFE.get());
            output.accept((ItemLike)ModItems.STRAW_HAT.get());
            output.accept((ItemLike)ModItems.STRAW_HAT_FLOWER.get());
            output.accept((ItemLike)ModItems.FARMER_CHEST_PLATE.get());
            output.accept((ItemLike)ModItems.FARMER_LEGGINGS.get());
            output.accept((ItemLike)ModItems.FARMER_BOOTS.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_OAK.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_SPRUCE.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_ACACIA.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_BAMBOO.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_BIRCH.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_CHERRY.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_CRIMSON.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_DARK_OAK.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_JUNGLE.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_MANGROVE.get());
            output.accept((ItemLike)ModItems.COOK_STOOL_WARPED.get());
            output.accept((ItemLike)ModItems.CHAIR_OAK.get());
            output.accept((ItemLike)ModItems.CHAIR_SPRUCE.get());
            output.accept((ItemLike)ModItems.CHAIR_ACACIA.get());
            output.accept((ItemLike)ModItems.CHAIR_BAMBOO.get());
            output.accept((ItemLike)ModItems.CHAIR_BIRCH.get());
            output.accept((ItemLike)ModItems.CHAIR_CHERRY.get());
            output.accept((ItemLike)ModItems.CHAIR_CRIMSON.get());
            output.accept((ItemLike)ModItems.CHAIR_DARK_OAK.get());
            output.accept((ItemLike)ModItems.CHAIR_JUNGLE.get());
            output.accept((ItemLike)ModItems.CHAIR_MANGROVE.get());
            output.accept((ItemLike)ModItems.CHAIR_WARPED.get());
            output.accept((ItemLike)ModItems.TABLE_OAK.get());
            output.accept((ItemLike)ModItems.TABLE_SPRUCE.get());
            output.accept((ItemLike)ModItems.TABLE_ACACIA.get());
            output.accept((ItemLike)ModItems.TABLE_BAMBOO.get());
            output.accept((ItemLike)ModItems.TABLE_BIRCH.get());
            output.accept((ItemLike)ModItems.TABLE_CHERRY.get());
            output.accept((ItemLike)ModItems.TABLE_CRIMSON.get());
            output.accept((ItemLike)ModItems.TABLE_DARK_OAK.get());
            output.accept((ItemLike)ModItems.TABLE_JUNGLE.get());
            output.accept((ItemLike)ModItems.TABLE_MANGROVE.get());
            output.accept((ItemLike)ModItems.TABLE_WARPED.get());
         })
         .build()
   );
   public static Supplier<CreativeModeTab> COOKERY_FOOD_TAB = TABS.register(
      "cookery_food",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("item_group.kaleidoscope_cookery.cookery_food.name"))
         .icon(() -> ((Item)ModItems.RED_CHILI.get()).getDefaultInstance())
         .withTabsBefore(new Identifier[]{Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "cookery_main")})
         .displayItems((par, output) -> {
            output.accept((ItemLike)ModItems.TOMATO.get());
            output.accept((ItemLike)ModItems.RED_CHILI.get());
            output.accept((ItemLike)ModItems.GREEN_CHILI.get());
            output.accept((ItemLike)ModItems.LETTUCE.get());
            output.accept((ItemLike)ModItems.RICE_PANICLE.get());
            output.accept((ItemLike)ModItems.CATERPILLAR.get());
            output.accept((ItemLike)ModItems.SASHIMI.get());
            output.accept((ItemLike)ModItems.RAW_LAMB_CHOPS.get());
            output.accept((ItemLike)ModItems.COOKED_LAMB_CHOPS.get());
            output.accept((ItemLike)ModItems.RAW_COW_OFFAL.get());
            output.accept((ItemLike)ModItems.COOKED_COW_OFFAL.get());
            output.accept((ItemLike)ModItems.RAW_PORK_BELLY.get());
            output.accept((ItemLike)ModItems.COOKED_PORK_BELLY.get());
            output.accept((ItemLike)ModItems.RAW_CUT_SMALL_MEATS.get());
            output.accept((ItemLike)ModItems.COOKED_CUT_SMALL_MEATS.get());
            output.accept((ItemLike)ModItems.RAW_MEATBALL.get());
            output.accept((ItemLike)ModItems.COOKED_MEATBALL.get());
            output.accept((ItemLike)ModItems.DONKEY_BURGER.get());
            output.accept((ItemLike)ModItems.MANTOU.get());
            output.accept((ItemLike)ModItems.BAOZI.get());
            output.accept((ItemLike)ModItems.SHENGJIAN_MANTOU.get());
            output.accept((ItemLike)ModItems.SAMSA.get());
            output.accept((ItemLike)ModItems.MEAT_PIE.get());
            output.accept((ItemLike)ModItems.DUMPLING.get());
            output.accept((ItemLike)ModItems.FRIED_EGG.get());
            output.accept((ItemLike)ModItems.STICKY_CANDY.get());
            output.accept((ItemLike)ModItems.STICKY_RICE_CAKE.get());
            output.accept((ItemLike)ModItems.BAMBOO_TUBE_RICE.get());
            output.accept((ItemLike)ModItems.ZONGZI.get());
            output.accept((ItemLike)ModItems.QINGTUAN.get());
            output.accept((ItemLike)ModItems.COOKED_RICE.get());
            output.accept((ItemLike)ModItems.EGG_FRIED_RICE.get());
            output.accept((ItemLike)ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get());
            output.accept((ItemLike)ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get());
            output.accept((ItemLike)ModItems.BRAISED_BEEF.get());
            output.accept((ItemLike)ModItems.BRAISED_BEEF_RICE_BOWL.get());
            output.accept((ItemLike)ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get());
            output.accept((ItemLike)ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get());
            output.accept((ItemLike)ModItems.SWEET_AND_SOUR_PORK.get());
            output.accept((ItemLike)ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get());
            output.accept((ItemLike)ModItems.FISH_FLAVORED_SHREDDED_PORK.get());
            output.accept((ItemLike)ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get());
            output.accept((ItemLike)ModItems.PORK_BONE_SOUP.get());
            output.accept((ItemLike)ModItems.SEAFOOD_MISO_SOUP.get());
            output.accept((ItemLike)ModItems.FEARSOME_THICK_SOUP.get());
            output.accept((ItemLike)ModItems.LAMB_AND_RADISH_SOUP.get());
            output.accept((ItemLike)ModItems.BRAISED_BEEF_WITH_POTATOES.get());
            output.accept((ItemLike)ModItems.WILD_MUSHROOM_RABBIT_SOUP.get());
            output.accept((ItemLike)ModItems.PUFFERFISH_SOUP.get());
            output.accept((ItemLike)ModItems.BORSCHT.get());
            output.accept((ItemLike)ModItems.BEEF_MEATBALL_SOUP.get());
            output.accept((ItemLike)ModItems.CHICKEN_AND_MUSHROOM_STEW.get());
            output.accept((ItemLike)ModItems.LABA_CONGEE.get());
            output.accept((ItemLike)ModItems.BEEF_NOODLE.get());
            output.accept((ItemLike)ModItems.HUI_NOODLE.get());
            output.accept((ItemLike)ModItems.UDON_NOODLE.get());
            output.accept((ItemLike)ModItems.HOT_DRY_NOODLES.get());
            FoodBiteRegistry.FOOD_DATA_MAP.keySet().forEach(foodName -> {
               if (foodName.equals(FoodBiteRegistry.DOUGH_DROP_SOUP)) {
                  output.accept((ItemLike)ModItems.COLD_CUT_HAM_SLICES.get());
               }

               Item foodItem = (Item)BuiltInRegistries.ITEM.getValue(foodName);
               output.accept(foodItem);
            });
            PlateRegistry.PLATE_DATA_MAP.keySet().forEach(plateName -> {
               Item plateItem = (Item)BuiltInRegistries.ITEM.getValue(plateName);
               output.accept(plateItem);
            });
            output.accept((ItemLike)ModItems.EMPTY_CUP.get());
            TeacupRegistry.TEACUP_DATA_MAP.keySet().forEach(teacupName -> {
               Item teacupItem = (Item)BuiltInRegistries.ITEM.getValue(teacupName);
               output.accept(teacupItem);
            });
         })
         .build()
   );
}
