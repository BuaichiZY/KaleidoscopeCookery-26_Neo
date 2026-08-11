package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.item.BambooTubeRiceBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodOnlyItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.ChiliItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.EmptyCupItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FlourItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FoodWithEffectsItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FruitBasketItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenKnifeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.LiftBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RawDoughItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RiceItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.ScarecrowItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SickleItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StockpotLidItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StrawHatItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TeapotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.WithTooltipsBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.WithTooltipsItem;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public final class ModItems {
   public static final Items ITEMS = DeferredRegister.createItems("kaleidoscope_cookery");
   public static DeferredItem<Item> STOVE = registerItem("stove", () -> new BlockItem((Block)ModBlocks.STOVE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> POT = registerItem("pot", () -> new BlockItem((Block)ModBlocks.POT.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> STOCKPOT = registerItem("stockpot", () -> new BlockItem((Block)ModBlocks.STOCKPOT.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> STOCKPOT_LID = registerItem("stockpot_lid", StockpotLidItem::new);
   public static DeferredItem<Item> CHOPPING_BOARD = registerItem(
      "chopping_board", () -> new BlockItem((Block)ModBlocks.CHOPPING_BOARD.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> KITCHENWARE_RACKS = registerItem(
      "kitchenware_racks", () -> new WithTooltipsBlockItem((Block)ModBlocks.KITCHENWARE_RACKS.get(), "kitchenware_racks")
   );
   public static DeferredItem<Item> SHAWARMA_SPIT = registerItem("shawarma_spit", () -> new BlockItem((Block)ModBlocks.SHAWARMA_SPIT.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> MILLSTONE = registerItem("millstone", () -> new BlockItem((Block)ModBlocks.MILLSTONE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> STEAMER = registerItem("steamer", SteamerItem::new);
   public static DeferredItem<Item> TEAPOT = registerItem("teapot", TeapotItem::new);
   public static DeferredItem<Item> EMPTY_CUP = registerItem("empty_cup", EmptyCupItem::new);
   public static DeferredItem<Item> TRASH_CAN = registerItem("trash_can", () -> new BlockItem((Block)ModBlocks.TRASH_CAN.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> OIL = registerItem("oil", OilItem::new);
   public static DeferredItem<Item> OIL_POT = registerItem("oil_pot", OilPotItem::new);
   public static DeferredItem<Item> OIL_BLOCK = registerItem("oil_block", () -> new BlockItem((Block)ModBlocks.OIL_BLOCK.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> ENAMEL_BASIN = registerItem("enamel_basin", () -> new BlockItem((Block)ModBlocks.ENAMEL_BASIN.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHILI_RISTRA = registerItem("chili_ristra", () -> new BlockItem((Block)ModBlocks.CHILI_RISTRA.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> STRUNG_MUSHROOMS = registerItem(
      "strung_mushrooms", () -> new BlockItem((Block)ModBlocks.STRUNG_MUSHROOMS.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> STRAW_BLOCK = registerItem("straw_block", () -> new BlockItem((Block)ModBlocks.STRAW_BLOCK.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> FRUIT_BASKET = registerItem("fruit_basket", FruitBasketItem::new);
   public static DeferredItem<Item> SCARECROW = registerItem("scarecrow", ScarecrowItem::new);
   public static DeferredItem<Item> RECIPE_ITEM = registerItem("recipe_item", RecipeItem::new);
   public static DeferredItem<Item> TRANSMUTATION_LUNCH_BAG = registerItem("transmutation_lunch_bag", TransmutationLunchBagItem::new);
   public static DeferredItem<Item> IRON_KITCHEN_KNIFE = registerItem("iron_kitchen_knife", () -> new KitchenKnifeItem(ToolMaterial.IRON));
   public static DeferredItem<Item> GOLD_KITCHEN_KNIFE = registerItem("gold_kitchen_knife", () -> new KitchenKnifeItem(ToolMaterial.GOLD));
   public static DeferredItem<Item> DIAMOND_KITCHEN_KNIFE = registerItem("diamond_kitchen_knife", () -> new KitchenKnifeItem(ToolMaterial.DIAMOND));
   public static DeferredItem<Item> NETHERITE_KITCHEN_KNIFE = registerItem(
      "netherite_kitchen_knife", () -> new KitchenKnifeItem(ToolMaterial.NETHERITE, ModRegistrationProperties.itemProperties().fireResistant())
   );
   public static DeferredItem<Item> KITCHEN_SHOVEL = registerItem("kitchen_shovel", KitchenShovelItem::new);
   public static DeferredItem<Item> SICKLE = registerItem("sickle", SickleItem::new);
   public static DeferredItem<Item> STRAW_HAT = registerItem("straw_hat", () -> new StrawHatItem(false));
   public static DeferredItem<Item> STRAW_HAT_FLOWER = registerItem("straw_hat_flower", () -> new StrawHatItem(true));
   public static final DeferredItem<Item> FARMER_CHEST_PLATE = registerItem(
      "farmer_chest_plate", () -> new Item(ModRegistrationProperties.itemProperties().stacksTo(1).humanoidArmor(ModArmorMaterials.FARMER, ArmorType.CHESTPLATE))
   );
   public static final DeferredItem<Item> FARMER_LEGGINGS = registerItem(
      "farmer_leggings", () -> new Item(ModRegistrationProperties.itemProperties().stacksTo(1).humanoidArmor(ModArmorMaterials.FARMER, ArmorType.LEGGINGS))
   );
   public static final DeferredItem<Item> FARMER_BOOTS = registerItem(
      "farmer_boots", () -> new Item(ModRegistrationProperties.itemProperties().stacksTo(1).humanoidArmor(ModArmorMaterials.FARMER, ArmorType.BOOTS))
   );
   public static DeferredItem<Item> TOMATO_SEED = registerItem(
      "tomato_seed", () -> new BlockItem((Block)ModBlocks.TOMATO_CROP.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> CHILI_SEED = registerItem("chili_seed", () -> new BlockItem((Block)ModBlocks.CHILI_CROP.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> LETTUCE_SEED = registerItem(
      "lettuce_seed", () -> new BlockItem((Block)ModBlocks.LETTUCE_CROP.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> RICE_SEED = registerItem("rice", RiceItem::new);
   public static DeferredItem<Item> WILD_RICE_SEED = registerItem(
      "wild_rice", () -> new BlockItem((Block)ModBlocks.RICE_CROP.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_OAK = registerItem(
      "cook_stool_oak", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_OAK.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_SPRUCE = registerItem(
      "cook_stool_spruce", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_SPRUCE.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_ACACIA = registerItem(
      "cook_stool_acacia", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_ACACIA.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_BAMBOO = registerItem(
      "cook_stool_bamboo", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_BAMBOO.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_BIRCH = registerItem(
      "cook_stool_birch", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_BIRCH.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_CHERRY = registerItem(
      "cook_stool_cherry", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_CHERRY.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_CRIMSON = registerItem(
      "cook_stool_crimson", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_CRIMSON.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_DARK_OAK = registerItem(
      "cook_stool_dark_oak", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_DARK_OAK.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_JUNGLE = registerItem(
      "cook_stool_jungle", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_JUNGLE.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_MANGROVE = registerItem(
      "cook_stool_mangrove", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_MANGROVE.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> COOK_STOOL_WARPED = registerItem(
      "cook_stool_warped", () -> new BlockItem((Block)ModBlocks.COOK_STOOL_WARPED.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> CHAIR_OAK = registerItem("chair_oak", () -> new BlockItem((Block)ModBlocks.CHAIR_OAK.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_SPRUCE = registerItem("chair_spruce", () -> new BlockItem((Block)ModBlocks.CHAIR_SPRUCE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_ACACIA = registerItem("chair_acacia", () -> new BlockItem((Block)ModBlocks.CHAIR_ACACIA.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_BAMBOO = registerItem("chair_bamboo", () -> new BlockItem((Block)ModBlocks.CHAIR_BAMBOO.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_BIRCH = registerItem("chair_birch", () -> new BlockItem((Block)ModBlocks.CHAIR_BIRCH.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_CHERRY = registerItem("chair_cherry", () -> new BlockItem((Block)ModBlocks.CHAIR_CHERRY.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_CRIMSON = registerItem("chair_crimson", () -> new BlockItem((Block)ModBlocks.CHAIR_CRIMSON.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_DARK_OAK = registerItem(
      "chair_dark_oak", () -> new BlockItem((Block)ModBlocks.CHAIR_DARK_OAK.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> CHAIR_JUNGLE = registerItem("chair_jungle", () -> new BlockItem((Block)ModBlocks.CHAIR_JUNGLE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CHAIR_MANGROVE = registerItem(
      "chair_mangrove", () -> new BlockItem((Block)ModBlocks.CHAIR_MANGROVE.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> CHAIR_WARPED = registerItem("chair_warped", () -> new BlockItem((Block)ModBlocks.CHAIR_WARPED.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_OAK = registerItem("table_oak", () -> new BlockItem((Block)ModBlocks.TABLE_OAK.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_SPRUCE = registerItem("table_spruce", () -> new BlockItem((Block)ModBlocks.TABLE_SPRUCE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_ACACIA = registerItem("table_acacia", () -> new BlockItem((Block)ModBlocks.TABLE_ACACIA.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_BAMBOO = registerItem("table_bamboo", () -> new BlockItem((Block)ModBlocks.TABLE_BAMBOO.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_BIRCH = registerItem("table_birch", () -> new BlockItem((Block)ModBlocks.TABLE_BIRCH.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_CHERRY = registerItem("table_cherry", () -> new BlockItem((Block)ModBlocks.TABLE_CHERRY.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_CRIMSON = registerItem("table_crimson", () -> new BlockItem((Block)ModBlocks.TABLE_CRIMSON.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_DARK_OAK = registerItem(
      "table_dark_oak", () -> new BlockItem((Block)ModBlocks.TABLE_DARK_OAK.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> TABLE_JUNGLE = registerItem("table_jungle", () -> new BlockItem((Block)ModBlocks.TABLE_JUNGLE.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TABLE_MANGROVE = registerItem(
      "table_mangrove", () -> new BlockItem((Block)ModBlocks.TABLE_MANGROVE.get(), ModRegistrationProperties.itemProperties())
   );
   public static DeferredItem<Item> TABLE_WARPED = registerItem("table_warped", () -> new BlockItem((Block)ModBlocks.TABLE_WARPED.get(), ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> TOMATO = registerItem("tomato", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.TOMATO, ModFoods.consumableFor(ModFoods.TOMATO))));
   public static DeferredItem<Item> RED_CHILI = registerItem("red_chili", () -> new ChiliItem(2));
   public static DeferredItem<Item> GREEN_CHILI = registerItem("green_chili", () -> new ChiliItem(1));
   public static DeferredItem<Item> LETTUCE = registerItem("lettuce", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.LETTUCE, ModFoods.consumableFor(ModFoods.LETTUCE))));
   public static DeferredItem<Item> RICE_PANICLE = registerItem("rice_panicle", () -> new Item(ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> CATERPILLAR = registerItem(
      "caterpillar", () -> new WithTooltipsItem(ModRegistrationProperties.itemProperties().food(ModFoods.CATERPILLAR, ModFoods.consumableFor(ModFoods.CATERPILLAR)), "caterpillar")
   );
   public static DeferredItem<Item> FLOUR = registerItem("flour", FlourItem::new);
   public static DeferredItem<Item> RAW_DOUGH = registerItem("raw_dough", RawDoughItem::new);
   public static DeferredItem<Item> RAW_NOODLES = registerItem("raw_noodles", () -> new Item(ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> STUFFED_DOUGH_FOOD = registerItem("stuffed_dough_food", () -> new Item(ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> RAW_ZONGZI = registerItem("raw_zongzi", () -> new Item(ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> RAW_BAMBOO_TUBE_RICE = registerItem("raw_bamboo_tube_rice", () -> new Item(ModRegistrationProperties.itemProperties()));
   public static DeferredItem<Item> FRIED_EGG = registerItem("fried_egg", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.FRIED_EGG, ModFoods.consumableFor(ModFoods.FRIED_EGG))));
   public static DeferredItem<Item> DONKEY_BURGER = registerItem("donkey_burger", () -> new FoodWithEffectsItem(ModFoods.DONKEY_BURGER));
   public static DeferredItem<Item> BAOZI = registerItem("baozi", () -> new FoodWithEffectsItem(ModFoods.BAOZI));
   public static DeferredItem<Item> SHENGJIAN_MANTOU = registerItem("shengjian_mantou", () -> new FoodWithEffectsItem(ModFoods.SHENGJIAN_MANTOU_ITEM));
   public static DeferredItem<Item> DUMPLING = registerItem("dumpling", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.DUMPLING, ModFoods.consumableFor(ModFoods.DUMPLING))));
   public static DeferredItem<Item> SAMSA = registerItem("samsa", () -> new FoodWithEffectsItem(ModFoods.SAMSA));
   public static DeferredItem<Item> MANTOU = registerItem("mantou", () -> new FoodWithEffectsItem(ModFoods.MANTOU));
   public static DeferredItem<Item> MEAT_PIE = registerItem("meat_pie", () -> new FoodWithEffectsItem(ModFoods.MEAT_PIE));
   public static DeferredItem<Item> QINGTUAN = registerItem("qingtuan", () -> new FoodWithEffectsItem(ModFoods.QINGTUAN));
   public static DeferredItem<Item> STICKY_CANDY = registerItem("sticky_candy", () -> new FoodWithEffectsItem(ModFoods.STICKY_CANDY));
   public static DeferredItem<Item> STICKY_RICE_CAKE = registerItem("sticky_rice_cake", () -> new FoodWithEffectsItem(ModFoods.STICKY_RICE_CAKE));
   public static DeferredItem<Item> ZONGZI = registerItem("zongzi", () -> new FoodWithEffectsItem(ModFoods.ZONGZI));
   public static DeferredItem<Item> BAMBOO_TUBE_RICE = registerItem(
      "bamboo_tube_rice", () -> new BambooTubeRiceBlockItem((Block)ModBlocks.BAMBOO_TUBE_RICE.get(), ModFoods.BAMBOO_TUBE_RICE)
   );
   public static DeferredItem<Item> COOKED_RICE = registerItem(
      "cooked_rice", () -> new BowlFoodOnlyItem(ModFoods.COOKED_RICE, net.minecraft.world.item.Items.BOWL)
   );
   public static DeferredItem<Item> EGG_FRIED_RICE = registerItem("egg_fried_rice", () -> new BowlFoodOnlyItem(ModFoods.EGG_FRIED_RICE));
   public static DeferredItem<Item> SCRAMBLE_EGG_WITH_TOMATOES = registerItem(
      "scramble_egg_with_tomatoes", () -> new BowlFoodOnlyItem(ModFoods.SCRAMBLE_EGG_WITH_TOMATOES)
   );
   public static DeferredItem<Item> SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = registerItem(
      "scramble_egg_with_tomatoes_rice_bowl", () -> new BowlFoodOnlyItem(ModFoods.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL)
   );
   public static DeferredItem<Item> BRAISED_BEEF = registerItem("braised_beef", () -> new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF));
   public static DeferredItem<Item> BRAISED_BEEF_RICE_BOWL = registerItem(
      "braised_beef_rice_bowl", () -> new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF_RICE_BOWL)
   );
   public static DeferredItem<Item> STIR_FRIED_PORK_WITH_PEPPERS = registerItem(
      "stir_fried_pork_with_peppers", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_PORK_WITH_PEPPERS)
   );
   public static DeferredItem<Item> STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = registerItem(
      "stir_fried_pork_with_peppers_rice_bowl", () -> new BowlFoodOnlyItem(ModFoods.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL)
   );
   public static DeferredItem<Item> SWEET_AND_SOUR_PORK = registerItem("sweet_and_sour_pork", () -> new BowlFoodOnlyItem(ModFoods.SWEET_AND_SOUR_PORK));
   public static DeferredItem<Item> SWEET_AND_SOUR_PORK_RICE_BOWL = registerItem(
      "sweet_and_sour_pork_rice_bowl", () -> new BowlFoodOnlyItem(ModFoods.SWEET_AND_SOUR_PORK_RICE_BOWL)
   );
   public static DeferredItem<Item> FISH_FLAVORED_SHREDDED_PORK = registerItem(
      "fish_flavored_shredded_pork", () -> new BowlFoodOnlyItem(ModFoods.FISH_FLAVORED_SHREDDED_PORK)
   );
   public static DeferredItem<Item> FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = registerItem(
      "fish_flavored_shredded_pork_rice_bowl", () -> new BowlFoodOnlyItem(ModFoods.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL)
   );
   public static DeferredItem<Item> PORK_BONE_SOUP = registerItem("pork_bone_soup", () -> new BowlFoodOnlyItem(ModFoods.PORK_BONE_SOUP));
   public static DeferredItem<Item> SEAFOOD_MISO_SOUP = registerItem("seafood_miso_soup", () -> new BowlFoodOnlyItem(ModFoods.SEAFOOD_MISO_SOUP));
   public static DeferredItem<Item> FEARSOME_THICK_SOUP = registerItem("fearsome_thick_soup", () -> new BowlFoodOnlyItem(ModFoods.FEARSOME_THICK_SOUP));
   public static DeferredItem<Item> LAMB_AND_RADISH_SOUP = registerItem("lamb_and_radish_soup", () -> new BowlFoodOnlyItem(ModFoods.LAMB_AND_RADISH_SOUP));
   public static DeferredItem<Item> BRAISED_BEEF_WITH_POTATOES = registerItem(
      "braised_beef_with_potatoes", () -> new BowlFoodOnlyItem(ModFoods.BRAISED_BEEF_WITH_POTATOES)
   );
   public static DeferredItem<Item> WILD_MUSHROOM_RABBIT_SOUP = registerItem(
      "wild_mushroom_rabbit_soup", () -> new BowlFoodOnlyItem(ModFoods.WILD_MUSHROOM_RABBIT_SOUP)
   );
   public static DeferredItem<Item> PUFFERFISH_SOUP = registerItem("pufferfish_soup", () -> new BowlFoodOnlyItem(ModFoods.PUFFERFISH_SOUP));
   public static DeferredItem<Item> BORSCHT = registerItem("borscht", () -> new BowlFoodOnlyItem(ModFoods.BORSCHT));
   public static DeferredItem<Item> BEEF_MEATBALL_SOUP = registerItem("beef_meatball_soup", () -> new BowlFoodOnlyItem(ModFoods.BEEF_MEATBALL_SOUP));
   public static DeferredItem<Item> CHICKEN_AND_MUSHROOM_STEW = registerItem(
      "chicken_and_mushroom_stew", () -> new BowlFoodOnlyItem(ModFoods.CHICKEN_AND_MUSHROOM_STEW)
   );
   public static DeferredItem<Item> LABA_CONGEE = registerItem("laba_congee", () -> new BowlFoodOnlyItem(ModFoods.LABA_CONGEE));
   public static DeferredItem<Item> BEEF_NOODLE = registerItem("beef_noodle", () -> new BowlFoodOnlyItem(ModFoods.BEEF_NOODLE));
   public static DeferredItem<Item> HUI_NOODLE = registerItem("hui_noodle", () -> new BowlFoodOnlyItem(ModFoods.HUI_NOODLE));
   public static DeferredItem<Item> UDON_NOODLE = registerItem("udon_noodle", () -> new BowlFoodOnlyItem(ModFoods.UDON_NOODLE));
   public static DeferredItem<Item> HOT_DRY_NOODLES = registerItem("hot_dry_noodles", () -> new BowlFoodOnlyItem(ModFoods.HOT_DRY_NOODLES));
   public static DeferredItem<Item> SASHIMI = registerItem("sashimi", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.SASHIMI, ModFoods.consumableFor(ModFoods.SASHIMI))));
   public static DeferredItem<Item> RAW_LAMB_CHOPS = registerItem("raw_lamb_chops", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.RAW_LAMB_CHOPS, ModFoods.consumableFor(ModFoods.RAW_LAMB_CHOPS))));
   public static DeferredItem<Item> RAW_COW_OFFAL = registerItem("raw_cow_offal", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.RAW_COW_OFFAL, ModFoods.consumableFor(ModFoods.RAW_COW_OFFAL))));
   public static DeferredItem<Item> RAW_PORK_BELLY = registerItem("raw_pork_belly", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.RAW_PORK_BELLY, ModFoods.consumableFor(ModFoods.RAW_PORK_BELLY))));
   public static DeferredItem<Item> RAW_CUT_SMALL_MEATS = registerItem(
      "raw_cut_small_meats", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.RAW_CUT_SMALL_MEATS, ModFoods.consumableFor(ModFoods.RAW_CUT_SMALL_MEATS)))
   );
   public static DeferredItem<Item> RAW_MEATBALL = registerItem("raw_meatball", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.RAW_MEATBALL, ModFoods.consumableFor(ModFoods.RAW_MEATBALL))));
   public static DeferredItem<Item> COOKED_LAMB_CHOPS = registerItem("cooked_lamb_chops", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.COOKED_LAMB_CHOPS, ModFoods.consumableFor(ModFoods.COOKED_LAMB_CHOPS))));
   public static DeferredItem<Item> COOKED_COW_OFFAL = registerItem("cooked_cow_offal", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.COOKED_COW_OFFAL, ModFoods.consumableFor(ModFoods.COOKED_COW_OFFAL))));
   public static DeferredItem<Item> COOKED_PORK_BELLY = registerItem("cooked_pork_belly", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.COOKED_PORK_BELLY, ModFoods.consumableFor(ModFoods.COOKED_PORK_BELLY))));
   public static DeferredItem<Item> COOKED_CUT_SMALL_MEATS = registerItem(
      "cooked_cut_small_meats", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.COOKED_CUT_SMALL_MEATS, ModFoods.consumableFor(ModFoods.COOKED_CUT_SMALL_MEATS)))
   );
   public static DeferredItem<Item> COOKED_MEATBALL = registerItem("cooked_meatball", () -> new Item(ModRegistrationProperties.itemProperties().food(ModFoods.COOKED_MEATBALL, ModFoods.consumableFor(ModFoods.COOKED_MEATBALL))));
   public static DeferredItem<Item> COLD_CUT_HAM_SLICES = registerItem(
      "cold_cut_ham_slices", () -> new LiftBlockItem((Block)ModBlocks.COLD_CUT_HAM_SLICES.get(), "cold_cut_ham_slices")
   );

   private static <I extends Item> DeferredItem<I> registerItem(String name, Supplier<? extends I> factory) {
      return ITEMS.register(name, id -> ModRegistrationProperties.withItemId(id, factory));
   }
}
