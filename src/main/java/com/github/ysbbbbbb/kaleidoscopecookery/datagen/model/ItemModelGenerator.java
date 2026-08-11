package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RawDoughItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StockpotLidItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import java.util.Objects;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
   public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
      super(output, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void registerModels() {
      this.withExistingParent("stove", this.modLoc("block/stove"));
      this.withExistingParent("pot", this.modLoc("block/pot"));
      this.withExistingParent("stockpot", this.modLoc("block/stockpot"));
      this.handheldItem((Item)ModItems.IRON_KITCHEN_KNIFE.get());
      this.handheldItem((Item)ModItems.GOLD_KITCHEN_KNIFE.get());
      this.handheldItem((Item)ModItems.DIAMOND_KITCHEN_KNIFE.get());
      this.handheldItem((Item)ModItems.NETHERITE_KITCHEN_KNIFE.get());
      this.handheldItem((Item)ModItems.SICKLE.get());
      this.basicItem((Item)ModItems.OIL.get());
      this.basicItem((Item)ModItems.FRIED_EGG.get());
      this.basicItem((Item)ModItems.SCARECROW.get());
      this.basicItem((Item)ModItems.TOMATO.get());
      this.basicItem((Item)ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get());
      this.basicItem((Item)ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get());
      this.basicItem((Item)ModItems.BRAISED_BEEF.get());
      this.basicItem((Item)ModItems.BRAISED_BEEF_RICE_BOWL.get());
      this.basicItem((Item)ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get());
      this.basicItem((Item)ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get());
      this.basicItem((Item)ModItems.SWEET_AND_SOUR_PORK.get());
      this.basicItem((Item)ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get());
      this.basicItem((Item)ModItems.FISH_FLAVORED_SHREDDED_PORK.get());
      this.basicItem((Item)ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get());
      this.basicItem((Item)ModItems.EGG_FRIED_RICE.get());
      this.basicItem((Item)ModItems.PORK_BONE_SOUP.get());
      this.basicItem((Item)ModItems.SEAFOOD_MISO_SOUP.get());
      this.basicItem((Item)ModItems.FEARSOME_THICK_SOUP.get());
      this.basicItem((Item)ModItems.LAMB_AND_RADISH_SOUP.get());
      this.basicItem((Item)ModItems.BRAISED_BEEF_WITH_POTATOES.get());
      this.basicItem((Item)ModItems.WILD_MUSHROOM_RABBIT_SOUP.get());
      this.basicItem((Item)ModItems.PUFFERFISH_SOUP.get());
      this.basicItem((Item)ModItems.BORSCHT.get());
      this.basicItem((Item)ModItems.BEEF_MEATBALL_SOUP.get());
      this.basicItem((Item)ModItems.CHICKEN_AND_MUSHROOM_STEW.get());
      this.basicItem((Item)ModItems.STRAW_HAT.get());
      this.basicItem((Item)ModItems.STRAW_HAT_FLOWER.get());
      this.basicItem((Item)ModItems.FARMER_CHEST_PLATE.get());
      this.basicItem((Item)ModItems.FARMER_LEGGINGS.get());
      this.basicItem((Item)ModItems.FARMER_BOOTS.get());
      this.basicItem((Item)ModItems.TOMATO_SEED.get());
      this.basicItem((Item)ModItems.RICE_SEED.get());
      this.basicItem((Item)ModItems.WILD_RICE_SEED.get());
      this.basicItem((Item)ModItems.RICE_PANICLE.get());
      this.basicItem((Item)ModItems.SASHIMI.get());
      this.basicItem((Item)ModItems.RAW_LAMB_CHOPS.get());
      this.basicItem((Item)ModItems.RAW_COW_OFFAL.get());
      this.basicItem((Item)ModItems.RAW_PORK_BELLY.get());
      this.basicItem((Item)ModItems.COOKED_LAMB_CHOPS.get());
      this.basicItem((Item)ModItems.COOKED_COW_OFFAL.get());
      this.basicItem((Item)ModItems.COOKED_PORK_BELLY.get());
      this.basicItem((Item)ModItems.COOKED_RICE.get());
      this.basicItem((Item)ModItems.RED_CHILI.get());
      this.basicItem((Item)ModItems.GREEN_CHILI.get());
      this.basicItem((Item)ModItems.CHILI_SEED.get());
      this.basicItem((Item)ModItems.LETTUCE.get());
      this.basicItem((Item)ModItems.LETTUCE_SEED.get());
      this.basicItem((Item)ModItems.CATERPILLAR.get());
      this.basicItem((Item)ModItems.ENAMEL_BASIN.get());
      this.basicItem((Item)ModItems.KITCHENWARE_RACKS.get());
      this.basicItem((Item)ModItems.MILLSTONE.get());
      this.basicItem((Item)ModItems.RAW_NOODLES.get());
      this.basicItem((Item)ModItems.STUFFED_DOUGH_FOOD.get());
      this.basicItem((Item)ModItems.DONKEY_BURGER.get());
      this.basicItem((Item)ModItems.FLOUR.get());
      this.basicItem((Item)ModItems.RAW_CUT_SMALL_MEATS.get());
      this.basicItem((Item)ModItems.COOKED_CUT_SMALL_MEATS.get());
      this.basicItem((Item)ModItems.RAW_MEATBALL.get());
      this.basicItem((Item)ModItems.COOKED_MEATBALL.get());
      this.basicItem((Item)ModItems.BAOZI.get());
      this.basicItem((Item)ModItems.SHENGJIAN_MANTOU.get());
      this.basicItem((Item)ModItems.DUMPLING.get());
      this.basicItem((Item)ModItems.SAMSA.get());
      this.basicItem((Item)ModItems.MANTOU.get());
      this.basicItem((Item)ModItems.MEAT_PIE.get());
      this.basicItem((Item)ModItems.STICKY_CANDY.get());
      this.basicItem((Item)ModItems.STICKY_RICE_CAKE.get());
      this.basicItem((Item)ModItems.QINGTUAN.get());
      this.basicItem((Item)ModItems.RAW_ZONGZI.get());
      this.basicItem((Item)ModItems.ZONGZI.get());
      this.basicItem((Item)ModItems.RAW_BAMBOO_TUBE_RICE.get());
      this.basicItem((Item)ModItems.BAMBOO_TUBE_RICE.get());
      this.basicItem((Item)ModItems.BEEF_NOODLE.get());
      this.basicItem((Item)ModItems.HUI_NOODLE.get());
      this.basicItem((Item)ModItems.UDON_NOODLE.get());
      this.basicItem((Item)ModItems.HOT_DRY_NOODLES.get());
      this.basicItem((Item)ModItems.LABA_CONGEE.get());
      this.basicItem(this.modLoc("honey"));
      this.basicItem(this.modLoc("egg"));
      this.basicItem(this.modLoc("oil_in_millstone"));
      Identifier chileRistra = BuiltInRegistries.ITEM.getKey((Item)ModItems.CHILI_RISTRA.get());
      ((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(chileRistra.toString())).parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "block/chili_ristra/head"));
      Identifier strungMushrooms = BuiltInRegistries.ITEM.getKey((Item)ModItems.STRUNG_MUSHROOMS.get());
      ((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(strungMushrooms.toString())).parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "block/strung_mushrooms/head"));
      Identifier shovel = BuiltInRegistries.ITEM.getKey((Item)ModItems.KITCHEN_SHOVEL.get());
      ItemModelBuilder shovelNoOil = this.handheldItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "kitchen_shovel_no_oil"));
      ItemModelBuilder shovelHasOil = this.handheldItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "kitchen_shovel_has_oil"));
      ((ItemModelBuilder)this.getBuilder(shovel.toString()))
         .override()
         .model(shovelNoOil)
         .predicate(KitchenShovelItem.HAS_OIL_PROPERTY, 0.0F)
         .end()
         .override()
         .model(shovelHasOil)
         .predicate(KitchenShovelItem.HAS_OIL_PROPERTY, 1.0F)
         .end();
      Identifier stockpotLid = BuiltInRegistries.ITEM.getKey((Item)ModItems.STOCKPOT_LID.get());
      ModelFile normal = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/stockpot_lid_normal"));
      ModelFile using = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/stockpot_lid_using"));
      ((ItemModelBuilder)this.getBuilder(stockpotLid.toString()))
         .override()
         .model(normal)
         .predicate(StockpotLidItem.USING_PROPERTY, 0.0F)
         .end()
         .override()
         .model(using)
         .predicate(StockpotLidItem.USING_PROPERTY, 1.0F)
         .end();
      Identifier oilPot = BuiltInRegistries.ITEM.getKey((Item)ModItems.OIL_POT.get());
      if (oilPot != null) {
         ItemModelBuilder potNoOil = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot_no_oil"));
         ItemModelBuilder potHasOil = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot_has_oil"));
         ((ItemModelBuilder)this.getBuilder(oilPot.toString()))
            .override()
            .model(potNoOil)
            .predicate(OilPotItem.HAS_OIL_PROPERTY, 0.0F)
            .end()
            .override()
            .model(potHasOil)
            .predicate(OilPotItem.HAS_OIL_PROPERTY, 1.0F)
            .end();
      }

      Identifier recipeItem = BuiltInRegistries.ITEM.getKey((Item)ModItems.RECIPE_ITEM.get());
      if (recipeItem != null) {
         ItemModelBuilder noRecipe = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "recipe_item_no_recipe"));
         ItemModelBuilder hasRecipe = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "recipe_item_has_recipe"));
         ((ItemModelBuilder)this.getBuilder(recipeItem.toString()))
            .override()
            .model(noRecipe)
            .predicate(RecipeItem.HAS_RECIPE_PROPERTY, 0.0F)
            .end()
            .override()
            .model(hasRecipe)
            .predicate(RecipeItem.HAS_RECIPE_PROPERTY, 1.0F)
            .end();
      }

      Identifier bagItem = BuiltInRegistries.ITEM.getKey((Item)ModItems.TRANSMUTATION_LUNCH_BAG.get());
      if (bagItem != null) {
         ItemModelBuilder noItems = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "transmutation_lunch_bag_no_items"));
         ItemModelBuilder hasItems = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "transmutation_lunch_bag_has_items"));
         ((ItemModelBuilder)this.getBuilder(bagItem.toString()))
            .override()
            .model(noItems)
            .predicate(TransmutationLunchBagItem.HAS_ITEMS_PROPERTY, 0.0F)
            .end()
            .override()
            .model(hasItems)
            .predicate(TransmutationLunchBagItem.HAS_ITEMS_PROPERTY, 1.0F)
            .end();
      }

      Identifier rawDough = BuiltInRegistries.ITEM.getKey((Item)ModItems.RAW_DOUGH.get());
      if (rawDough != null) {
         ItemModelBuilder builder = (ItemModelBuilder)this.getBuilder(rawDough.toString());
         UncheckedModelFile file0 = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/raw_dough_0"));
         UncheckedModelFile file1 = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/raw_dough_1"));
         UncheckedModelFile file2 = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/raw_dough_2"));
         UncheckedModelFile file3 = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/raw_dough_3"));
         UncheckedModelFile file4 = new UncheckedModelFile(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "item/raw_dough_4"));
         builder.override().model(file0).predicate(RawDoughItem.PULL_PROPERTY, 0.0F).end();
         builder.override().model(file1).predicate(RawDoughItem.PULL_PROPERTY, 0.1F).end();
         builder.override().model(file2).predicate(RawDoughItem.PULL_PROPERTY, 1.0F).end();
         builder.override().model(file3).predicate(RawDoughItem.PULL_PROPERTY, 2.0F).end();
         builder.override().model(file4).predicate(RawDoughItem.PULL_PROPERTY, 3.0F).end();
      }

      Identifier steamerItem = BuiltInRegistries.ITEM.getKey((Item)ModItems.STEAMER.get());
      if (steamerItem != null) {
         ItemModelBuilder noItems = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "steamer_no_items"));
         ItemModelBuilder hasItems = this.basicItem(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "steamer_has_items"));
         ((ItemModelBuilder)this.getBuilder(steamerItem.toString()))
            .override()
            .model(noItems)
            .predicate(SteamerItem.HAS_ITEMS, 0.0F)
            .end()
            .override()
            .model(hasItems)
            .predicate(SteamerItem.HAS_ITEMS, 1.0F)
            .end();
      }

      FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
         Item item = (Item)BuiltInRegistries.ITEM.getValue(key);
         this.basicItem(item);
      });
      PlateRegistry.PLATE_DATA_MAP.forEach((key, value) -> {
         Item item = (Item)BuiltInRegistries.ITEM.getValue(key);
         this.basicItem(item);
      });
      this.basicItem((Item)ModItems.EMPTY_CUP.get());
      TeacupRegistry.TEACUP_DATA_MAP.forEach((key, value) -> {
         Item item = (Item)BuiltInRegistries.ITEM.getValue(key);
         this.basicItem(item);
      });
      this.withExistingParent("cook_stool_oak", this.modLoc("block/cook_stool/oak"));
      this.withExistingParent("cook_stool_spruce", this.modLoc("block/cook_stool/spruce"));
      this.withExistingParent("cook_stool_acacia", this.modLoc("block/cook_stool/acacia"));
      this.withExistingParent("cook_stool_bamboo", this.modLoc("block/cook_stool/bamboo"));
      this.withExistingParent("cook_stool_birch", this.modLoc("block/cook_stool/birch"));
      this.withExistingParent("cook_stool_cherry", this.modLoc("block/cook_stool/cherry"));
      this.withExistingParent("cook_stool_crimson", this.modLoc("block/cook_stool/crimson"));
      this.withExistingParent("cook_stool_dark_oak", this.modLoc("block/cook_stool/dark_oak"));
      this.withExistingParent("cook_stool_jungle", this.modLoc("block/cook_stool/jungle"));
      this.withExistingParent("cook_stool_mangrove", this.modLoc("block/cook_stool/mangrove"));
      this.withExistingParent("cook_stool_warped", this.modLoc("block/cook_stool/warped"));
      this.withExistingParent("chair_oak", this.modLoc("block/chair/oak"));
      this.withExistingParent("chair_spruce", this.modLoc("block/chair/spruce"));
      this.withExistingParent("chair_acacia", this.modLoc("block/chair/acacia"));
      this.withExistingParent("chair_bamboo", this.modLoc("block/chair/bamboo"));
      this.withExistingParent("chair_birch", this.modLoc("block/chair/birch"));
      this.withExistingParent("chair_cherry", this.modLoc("block/chair/cherry"));
      this.withExistingParent("chair_crimson", this.modLoc("block/chair/crimson"));
      this.withExistingParent("chair_dark_oak", this.modLoc("block/chair/dark_oak"));
      this.withExistingParent("chair_jungle", this.modLoc("block/chair/jungle"));
      this.withExistingParent("chair_mangrove", this.modLoc("block/chair/mangrove"));
      this.withExistingParent("chair_warped", this.modLoc("block/chair/warped"));
      this.withExistingParent("table_oak", this.modLoc("block/table/oak_single"));
      this.withExistingParent("table_spruce", this.modLoc("block/table/spruce_single"));
      this.withExistingParent("table_acacia", this.modLoc("block/table/acacia_single"));
      this.withExistingParent("table_bamboo", this.modLoc("block/table/bamboo_single"));
      this.withExistingParent("table_birch", this.modLoc("block/table/birch_single"));
      this.withExistingParent("table_cherry", this.modLoc("block/table/cherry_single"));
      this.withExistingParent("table_crimson", this.modLoc("block/table/crimson_single"));
      this.withExistingParent("table_dark_oak", this.modLoc("block/table/dark_oak_single"));
      this.withExistingParent("table_jungle", this.modLoc("block/table/jungle_single"));
      this.withExistingParent("table_mangrove", this.modLoc("block/table/mangrove_single"));
      this.withExistingParent("table_warped", this.modLoc("block/table/warped_single"));
      this.withExistingParent("chopping_board", this.modLoc("block/chopping_board"));
      this.withExistingParent("oil_block", this.modLoc("block/oil_block"));
      this.withExistingParent("straw_block", this.modLoc("block/straw_block"));
      ItemModelBuilder fruitBasketFull = (ItemModelBuilder)new ItemModelBuilder(this.modLoc("fruit_basket"), this.existingFileHelper)
         .parent(new UncheckedModelFile(this.modLoc("item/fruit_basket_full")));
      ItemModelBuilder fruitBasketItem = (ItemModelBuilder)((ItemModelBuilder)new ItemModelBuilder(this.modLoc("fruit_basket"), this.existingFileHelper)
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("item/fruit_basket"));
      ItemModelBuilder fruitBasketBlock = (ItemModelBuilder)new ItemModelBuilder(this.modLoc("fruit_basket"), this.existingFileHelper)
         .parent(new UncheckedModelFile(this.modLoc("block/fruit_basket")));
      ((SeparateTransformsModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder("fruit_basket")).guiLight(GuiLight.FRONT))
            .customLoader(SeparateTransformsModelBuilder::begin))
         .base(fruitBasketFull)
         .perspective(ItemDisplayContext.GROUND, fruitBasketBlock)
         .perspective(ItemDisplayContext.GUI, fruitBasketItem)
         .perspective(ItemDisplayContext.FIXED, fruitBasketItem)
         .perspective(ItemDisplayContext.GROUND, fruitBasketItem);
      ItemModelBuilder flatColdCutHamSlices = (ItemModelBuilder)((ItemModelBuilder)new ItemModelBuilder(
               this.modLoc("cold_cut_ham_slices"), this.existingFileHelper
            )
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("item/cold_cut_ham_slices_gui"));
      ItemModelBuilder blockColdCutHamSlices = (ItemModelBuilder)new ItemModelBuilder(this.modLoc("cold_cut_ham_slices"), this.existingFileHelper)
         .parent(new UncheckedModelFile(this.modLoc("item/cold_cut_ham_slices_block")));
      ((SeparateTransformsModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder("cold_cut_ham_slices")).guiLight(GuiLight.FRONT))
            .customLoader(SeparateTransformsModelBuilder::begin))
         .base(blockColdCutHamSlices)
         .perspective(ItemDisplayContext.GUI, flatColdCutHamSlices)
         .perspective(ItemDisplayContext.FIXED, flatColdCutHamSlices);
      ItemModelBuilder teapotItem = (ItemModelBuilder)((ItemModelBuilder)new ItemModelBuilder(this.modLoc("teapot"), this.existingFileHelper)
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("item/teapot"));
      ItemModelBuilder teapotBlock = (ItemModelBuilder)new ItemModelBuilder(this.modLoc("teapot"), this.existingFileHelper)
         .parent(new UncheckedModelFile(this.modLoc("item/teapot_3d")));
      ((SeparateTransformsModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder("teapot")).guiLight(GuiLight.FRONT))
            .customLoader(SeparateTransformsModelBuilder::begin))
         .base(teapotBlock)
         .perspective(ItemDisplayContext.GUI, teapotItem)
         .perspective(ItemDisplayContext.FIXED, teapotItem)
         .perspective(ItemDisplayContext.GROUND, teapotItem);
   }

   public ItemModelBuilder handheldItem(Item item) {
      return this.handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
   }

   public ItemModelBuilder handheldItem(Identifier item) {
      return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(new UncheckedModelFile("item/handheld")))
         .texture("layer0", Identifier.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
   }
}
