package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade;

import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.RecipeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.ChoppingBoardComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.EnamelBasinComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.FoodBiteBlockComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.FruitBasketComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.KitchenwareRackComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.MillstoneComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.OilPotComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.PotComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.RecipeBlockComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.ShawarmaSpitComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.SteamerComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.StockpotComponentProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block.TableComponentProvider;
import net.minecraft.resources.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModPlugin implements IWailaPlugin {
   public static final Identifier SHAWARMA_SPIT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "shawarma_spit");
   public static final Identifier POT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot");
   public static final Identifier STOCKPOT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot");
   public static final Identifier CHOPPING_BOARD = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "chopping_board");
   public static final Identifier MILLSTONE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone");
   public static final Identifier ENAMEL_BASIN = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "enamel_basin");
   public static final Identifier FOOD_BITE_BLOCK = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "food_bite_block");
   public static final Identifier TABLE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "table");
   public static final Identifier FRUIT_BASKET = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "fruit_basket");
   public static final Identifier KITCHENWARE_RACK = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "kitchenware_rack");
   public static final Identifier RECIPE_BLOCK = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "recipe_block");
   public static final Identifier STEAMER = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "steamer");
   public static final Identifier OIL_POT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "oil_pot");

   public void register(IWailaCommonRegistration registration) {
      registration.registerItemStorage(FruitBasketComponentProvider.INSTANCE, FruitBasketBlockEntity.class);
      registration.registerItemStorage(KitchenwareRackComponentProvider.INSTANCE, KitchenwareRacksBlockEntity.class);
      registration.registerItemStorage(TableComponentProvider.INSTANCE, TableBlockEntity.class);
      registration.registerItemStorage(PotComponentProvider.INSTANCE, PotBlockEntity.class);
      registration.registerItemStorage(StockpotComponentProvider.INSTANCE, StockpotBlockEntity.class);
      registration.registerItemStorage(SteamerComponentProvider.INSTANCE, SteamerBlockEntity.class);
      registration.registerItemStorage(OilPotComponentProvider.INSTANCE, OilPotBlockEntity.class);
   }

   public void registerClient(IWailaClientRegistration registration) {
      registration.registerBlockComponent(ShawarmaSpitComponentProvider.INSTANCE, ShawarmaSpitBlock.class);
      registration.registerBlockComponent(ChoppingBoardComponentProvider.INSTANCE, ChoppingBoardBlock.class);
      registration.registerBlockComponent(EnamelBasinComponentProvider.INSTANCE, EnamelBasinBlock.class);
      registration.registerBlockComponent(FoodBiteBlockComponentProvider.INSTANCE, FoodBiteBlock.class);
      registration.registerItemStorageClient(FruitBasketComponentProvider.INSTANCE);
      registration.registerItemStorageClient(KitchenwareRackComponentProvider.INSTANCE);
      registration.registerItemStorageClient(TableComponentProvider.INSTANCE);
      registration.registerItemStorageClient(PotComponentProvider.INSTANCE);
      registration.registerItemStorageClient(StockpotComponentProvider.INSTANCE);
      registration.registerItemStorageClient(SteamerComponentProvider.INSTANCE);
      registration.registerItemStorageClient(OilPotComponentProvider.INSTANCE);
      registration.registerBlockComponent(MillstoneComponentProvider.INSTANCE, MillstoneBlock.class);
      registration.registerBlockComponent(RecipeBlockComponentProvider.INSTANCE, RecipeBlock.class);
   }
}
