package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.BaseCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.ChiliCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.LettuceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.ChairBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.CookStoolBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.FruitBasketBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.StackableFoodBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.EightImmortalsTableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.LongBenchBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.RedLanternBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TeaBannerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.ClayPotMilkTeaBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteThreeByThreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.KitchenwareRacksBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.TeapotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.BambooTrayBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.OilBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.RecipeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrawBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrungMushroomsBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.FoodBiteThreeByThreeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.TeapotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Blocks;

public class ModBlocks {
   public static final Blocks BLOCKS = DeferredRegister.createBlocks("kaleidoscope_cookery");
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
      BuiltInRegistries.BLOCK_ENTITY_TYPE, "kaleidoscope_cookery"
   );
   public static DeferredBlock<Block> STOVE = registerBlock("stove", StoveBlock::new);
   public static DeferredBlock<Block> POT = registerBlock("pot", PotBlock::new);
   public static DeferredBlock<Block> STOCKPOT = registerBlock("stockpot", StockpotBlock::new);
   public static DeferredBlock<Block> FRUIT_BASKET = registerBlock("fruit_basket", FruitBasketBlock::new);
   public static DeferredBlock<Block> CHOPPING_BOARD = registerBlock("chopping_board", ChoppingBoardBlock::new);
   public static DeferredBlock<Block> OIL_BLOCK = registerBlock("oil_block", OilBlock::new);
   public static DeferredBlock<Block> ENAMEL_BASIN = registerBlock("enamel_basin", EnamelBasinBlock::new);
   public static DeferredBlock<Block> KITCHENWARE_RACKS = registerBlock("kitchenware_racks", KitchenwareRacksBlock::new);
   public static DeferredBlock<Block> BAMBOO_TRAY = registerBlock("bamboo_tray", BambooTrayBlock::new);
   public static DeferredBlock<Block> CHILI_RISTRA = registerBlock("chili_ristra", ChiliRistraBlock::new);
   public static DeferredBlock<Block> STRUNG_MUSHROOMS = registerBlock("strung_mushrooms", StrungMushroomsBlock::new);
   public static DeferredBlock<Block> STRAW_BLOCK = registerBlock("straw_block", StrawBlocks::new);
   public static DeferredBlock<Block> SHAWARMA_SPIT = registerBlock("shawarma_spit", ShawarmaSpitBlock::new);
   public static DeferredBlock<Block> MILLSTONE = registerBlock("millstone", MillstoneBlock::new);
   public static DeferredBlock<Block> STEAMER = registerBlock("steamer", SteamerBlock::new);
   public static DeferredBlock<Block> TEAPOT = registerBlock("teapot", TeapotBlock::new);
   public static DeferredBlock<Block> EMPTY_CUP = registerBlock("empty_cup", EmptyCupBlock::new);
   public static DeferredBlock<Block> CLAY_POT_MILK_TEA = registerBlock("clay_pot_milk_tea", ClayPotMilkTeaBlock::new);
   public static DeferredBlock<Block> TRASH_CAN = registerBlock("trash_can", TrashCanBlock::new);
   public static DeferredBlock<Block> OIL_POT = registerBlock("oil_pot", OilPotBlock::new);
   public static DeferredBlock<Block> RECIPE_BLOCK = registerBlock("recipe_block", RecipeBlock::new);
   public static DeferredBlock<Block> TEA_BANNER = registerBlock("tea_banner", TeaBannerBlock::new);
   public static DeferredBlock<Block> TOMATO_CROP = registerBlock("tomato_crop", () -> new BaseCropBlock(ModItems.TOMATO, ModItems.TOMATO_SEED));
   public static DeferredBlock<Block> CHILI_CROP = registerBlock("chili_crop", ChiliCropBlock::new);
   public static DeferredBlock<Block> LETTUCE_CROP = registerBlock("lettuce_crop", LettuceCropBlock::new);
   public static DeferredBlock<Block> RICE_CROP = registerBlock("rice_crop", RiceCropBlock::new);
   public static DeferredBlock<Block> TEA_TREE = registerBlock("tea_tree", TeaTreeBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_OAK = registerBlock("cook_stool_oak", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_SPRUCE = registerBlock("cook_stool_spruce", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_ACACIA = registerBlock("cook_stool_acacia", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_BAMBOO = registerBlock("cook_stool_bamboo", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_BIRCH = registerBlock("cook_stool_birch", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_CHERRY = registerBlock("cook_stool_cherry", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_CRIMSON = registerBlock("cook_stool_crimson", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_DARK_OAK = registerBlock("cook_stool_dark_oak", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_JUNGLE = registerBlock("cook_stool_jungle", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_MANGROVE = registerBlock("cook_stool_mangrove", CookStoolBlock::new);
   public static DeferredBlock<Block> COOK_STOOL_WARPED = registerBlock("cook_stool_warped", CookStoolBlock::new);
   public static DeferredBlock<Block> CHAIR_OAK = registerBlock("chair_oak", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_SPRUCE = registerBlock("chair_spruce", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_ACACIA = registerBlock("chair_acacia", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_BAMBOO = registerBlock("chair_bamboo", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_BIRCH = registerBlock("chair_birch", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_CHERRY = registerBlock("chair_cherry", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_CRIMSON = registerBlock("chair_crimson", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_DARK_OAK = registerBlock("chair_dark_oak", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_JUNGLE = registerBlock("chair_jungle", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_MANGROVE = registerBlock("chair_mangrove", ChairBlock::new);
   public static DeferredBlock<Block> CHAIR_WARPED = registerBlock("chair_warped", ChairBlock::new);
   public static DeferredBlock<Block> TABLE_OAK = registerBlock("table_oak", TableBlock::new);
   public static DeferredBlock<Block> TABLE_SPRUCE = registerBlock("table_spruce", TableBlock::new);
   public static DeferredBlock<Block> TABLE_ACACIA = registerBlock("table_acacia", TableBlock::new);
   public static DeferredBlock<Block> TABLE_BAMBOO = registerBlock("table_bamboo", TableBlock::new);
   public static DeferredBlock<Block> TABLE_BIRCH = registerBlock("table_birch", TableBlock::new);
   public static DeferredBlock<Block> TABLE_CHERRY = registerBlock("table_cherry", TableBlock::new);
   public static DeferredBlock<Block> TABLE_CRIMSON = registerBlock("table_crimson", TableBlock::new);
   public static DeferredBlock<Block> TABLE_DARK_OAK = registerBlock("table_dark_oak", TableBlock::new);
   public static DeferredBlock<Block> TABLE_JUNGLE = registerBlock("table_jungle", TableBlock::new);
   public static DeferredBlock<Block> TABLE_MANGROVE = registerBlock("table_mangrove", TableBlock::new);
   public static DeferredBlock<Block> TABLE_WARPED = registerBlock("table_warped", TableBlock::new);
   public static DeferredBlock<Block> LONG_BENCH = registerBlock("long_bench", LongBenchBlock::new);
   public static DeferredBlock<Block> RED_LANTERN = registerBlock("red_lantern", RedLanternBlock::new);
   public static DeferredBlock<Block> EIGHT_IMMORTALS_TABLE = registerBlock("eight_immortals_table", EightImmortalsTableBlock::new);
   public static final DeferredBlock<Block> KITCHEN_SHELF = registerBlock("kitchen_shelf", com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.KitchenShelfBlock::new);
    public static DeferredBlock<Block> CANDIED_GLOW_BERRY_SKEWER_UPRIGHT = candiedSkewer(
            "candied_glow_berry_skewer_upright", 12, true,
            () -> ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), 8
    );

    public static DeferredBlock<Block> CANDIED_GLOW_BERRY_SKEWER_SIDE = candiedSkewer(
            "candied_glow_berry_skewer_side", 5, false,
            () -> ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), 8
    );

    public static DeferredBlock<Block> CANDIED_SWEET_BERRY_SKEWER_UPRIGHT = candiedSkewer(
            "candied_sweet_berry_skewer_upright", 12, true,
            () -> ModItems.CANDIED_SWEET_BERRY_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_SWEET_BERRY_SKEWER_SIDE = candiedSkewer(
            "candied_sweet_berry_skewer_side", 5, false,
            () -> ModItems.CANDIED_SWEET_BERRY_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CHORUS_FRUIT_SKEWER_UPRIGHT = candiedSkewer(
            "candied_chorus_fruit_skewer_upright", 5, true,
            () -> ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CHORUS_FRUIT_SKEWER_SIDE = candiedSkewer(
            "candied_chorus_fruit_skewer_side", 4, false,
            () -> ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_APPLE_SKEWER_UPRIGHT = candiedSkewer(
            "candied_apple_skewer_upright", 5, true,
            () -> ModItems.CANDIED_APPLE_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_APPLE_SKEWER_SIDE = candiedSkewer(
            "candied_apple_skewer_side", 4, false,
            () -> ModItems.CANDIED_APPLE_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CATERPILLAR_SKEWER_UPRIGHT = candiedSkewer(
            "candied_caterpillar_skewer_upright", 12, true,
            () -> ModItems.CANDIED_CATERPILLAR_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CATERPILLAR_SKEWER_SIDE = candiedSkewer(
            "candied_caterpillar_skewer_side", 5, false,
            () -> ModItems.CANDIED_CATERPILLAR_SKEWER.get(), 0
    );

   public static DeferredBlock<Block> COLD_CUT_HAM_SLICES = registerBlock(
      "cold_cut_ham_slices", () -> new FoodBiteThreeByThreeBlock(ModFoods.COLD_CUT_HAM_SLICES_BLOCK, 8, null)
   );
   public static DeferredBlock<Block> BAMBOO_TUBE_RICE = registerBlock(
      "bamboo_tube_rice",
      () -> StackableFoodBlock.create()
         .maxCount(4)
         .item(() -> (Item)ModItems.BAMBOO_TUBE_RICE.get())
         .shapes(
            Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0),
            Shapes.or(Block.box(7.0, 0.0, 1.0, 15.0, 10.0, 9.0), Block.box(1.0, 0.0, 7.0, 9.0, 10.0, 15.0)),
            Shapes.or(Block.box(0.0, 0.0, 6.0, 16.0, 10.0, 15.0), Block.box(4.0, 0.0, 0.0, 12.0, 10.0, 15.0)),
            Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
         )
         .build()
         .get()
   );
   public static Supplier<BlockEntityType<PotBlockEntity>> POT_BE = BLOCK_ENTITIES.register(
      "pot", () -> new BlockEntityType<>(PotBlockEntity::new, (Block)POT.get())
   );
   public static Supplier<BlockEntityType<StockpotBlockEntity>> STOCKPOT_BE = BLOCK_ENTITIES.register(
      "stockpot", () -> new BlockEntityType<>(StockpotBlockEntity::new, (Block)STOCKPOT.get())
   );
   public static Supplier<BlockEntityType<FruitBasketBlockEntity>> FRUIT_BASKET_BE = BLOCK_ENTITIES.register(
      "fruit_basket", () -> new BlockEntityType<>(FruitBasketBlockEntity::new, (Block)FRUIT_BASKET.get())
   );
   public static Supplier<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD_BE = BLOCK_ENTITIES.register(
      "chopping_board", () -> new BlockEntityType<>(ChoppingBoardBlockEntity::new, (Block)CHOPPING_BOARD.get())
   );
   public static Supplier<BlockEntityType<KitchenwareRacksBlockEntity>> KITCHENWARE_RACKS_BE = BLOCK_ENTITIES.register(
      "kitchenware_racks", () -> new BlockEntityType<>(KitchenwareRacksBlockEntity::new, (Block)KITCHENWARE_RACKS.get())
   );
   public static Supplier<BlockEntityType<BambooTrayBlockEntity>> BAMBOO_TRAY_BE = BLOCK_ENTITIES.register(
      "bamboo_tray", () -> new BlockEntityType<>(BambooTrayBlockEntity::new, (Block)BAMBOO_TRAY.get())
   );
   public static Supplier<BlockEntityType<ShawarmaSpitBlockEntity>> SHAWARMA_SPIT_BE = BLOCK_ENTITIES.register(
      "shawarma_spit", () -> new BlockEntityType<>(ShawarmaSpitBlockEntity::new, (Block)SHAWARMA_SPIT.get())
   );
   public static Supplier<BlockEntityType<MillstoneBlockEntity>> MILLSTONE_BE = BLOCK_ENTITIES.register(
      "millstone", () -> new BlockEntityType<>(MillstoneBlockEntity::new, (Block)MILLSTONE.get())
   );
   public static Supplier<BlockEntityType<RecipeBlockEntity>> RECIPE_BLOCK_BE = BLOCK_ENTITIES.register(
      "recipe_block", () -> new BlockEntityType<>(RecipeBlockEntity::new, (Block)RECIPE_BLOCK.get())
   );
   public static Supplier<BlockEntityType<TeaBannerBlockEntity>> TEA_BANNER_BE = BLOCK_ENTITIES.register(
      "tea_banner", () -> new BlockEntityType<>(TeaBannerBlockEntity::new, (Block)TEA_BANNER.get())
   );
   public static Supplier<BlockEntityType<SteamerBlockEntity>> STEAMER_BE = BLOCK_ENTITIES.register(
      "steamer", () -> new BlockEntityType<>(SteamerBlockEntity::new, (Block)STEAMER.get())
   );
   public static Supplier<BlockEntityType<TeapotBlockEntity>> TEAPOT_BE = BLOCK_ENTITIES.register(
      "teapot", () -> new BlockEntityType<>(TeapotBlockEntity::new, (Block)TEAPOT.get())
   );
   public static Supplier<BlockEntityType<OilPotBlockEntity>> OIL_POT_BE = BLOCK_ENTITIES.register(
      "oil_pot", () -> new BlockEntityType<>(OilPotBlockEntity::new, (Block)OIL_POT.get())
   );
   public static Supplier<BlockEntityType<TrashCanBlockEntity>> TRASH_CAN_BE = BLOCK_ENTITIES.register(
      "trash_can", () -> new BlockEntityType<>(TrashCanBlockEntity::new, (Block)TRASH_CAN.get())
   );
   public static Supplier<BlockEntityType<FoodBiteThreeByThreeBlockEntity>> FOOD_BITE_THREE_BY_THREE_BE = BLOCK_ENTITIES.register(
      "food_bite_three_by_three", () -> new BlockEntityType<>(FoodBiteThreeByThreeBlockEntity::new, (Block)COLD_CUT_HAM_SLICES.get())
   );
   public static Supplier<BlockEntityType<ChairBlockEntity>> CHAIR_BE = BLOCK_ENTITIES.register(
      "chair",
      () -> new BlockEntityType<>(ChairBlockEntity::new, 
               (Block)CHAIR_OAK.get(),
               (Block)CHAIR_SPRUCE.get(),
               (Block)CHAIR_ACACIA.get(),
               (Block)CHAIR_BAMBOO.get(),
               (Block)CHAIR_BIRCH.get(),
               (Block)CHAIR_CHERRY.get(),
               (Block)CHAIR_CRIMSON.get(),
               (Block)CHAIR_DARK_OAK.get(),
               (Block)CHAIR_JUNGLE.get(),
               (Block)CHAIR_MANGROVE.get(),
               (Block)CHAIR_WARPED.get()
            )
   );
   public static Supplier<BlockEntityType<TableBlockEntity>> TABLE_BE = BLOCK_ENTITIES.register(
      "table",
      () -> new BlockEntityType<>(TableBlockEntity::new, 
               (Block)TABLE_OAK.get(),
               (Block)TABLE_SPRUCE.get(),
               (Block)TABLE_ACACIA.get(),
               (Block)TABLE_BAMBOO.get(),
               (Block)TABLE_BIRCH.get(),
               (Block)TABLE_CHERRY.get(),
               (Block)TABLE_CRIMSON.get(),
               (Block)TABLE_DARK_OAK.get(),
               (Block)TABLE_JUNGLE.get(),
               (Block)TABLE_MANGROVE.get(),
               (Block)TABLE_WARPED.get()
            )
   );

   public static final Supplier<BlockEntityType<KitchenShelfBlockEntity>> KITCHEN_SHELF_BE =
      BLOCK_ENTITIES.register("kitchen_shelf", () -> new BlockEntityType<>(KitchenShelfBlockEntity::new, KITCHEN_SHELF.get()));

   private static DeferredBlock<Block> candiedSkewer(String name, int count, boolean upright,
      Supplier<net.minecraft.world.item.Item> item, int light) {
      return registerBlock(name, () -> com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.CandiedSkewerBlock.create(count, item, light, upright));
   }

   private static <B extends Block> DeferredBlock<B> registerBlock(String name, Supplier<? extends B> factory) {
      return BLOCKS.register(name, id -> ModRegistrationProperties.withBlockId(id, factory));
   }
}
