package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.PlateBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.StackableFoodBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.TeacupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteOneByTwoBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.EnamelBasinBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.OilPotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrungMushroomsBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import java.util.function.Function;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BlockStateGenerator extends BlockStateProvider {
   public BlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
      super(output, "kaleidoscope_cookery", exFileHelper);
   }

   protected void registerStatesAndModels() {
      this.horizontalBlock(
         (Block)ModBlocks.STOVE.get(),
         blockState -> blockState.getValue(StoveBlock.LIT)
            ? new UncheckedModelFile(this.modLoc("block/stove_lit"))
            : new UncheckedModelFile(this.modLoc("block/stove"))
      );
      this.horizontalBlock(
         (Block)ModBlocks.POT.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(PotBlock.HAS_OIL) && (Boolean)blockState.getValue(PotBlock.SHOW_OIL)) {
               return blockState.getValue(PotBlock.HAS_BASE)
                  ? new UncheckedModelFile(this.modLoc("block/pot_base_has_oil"))
                  : new UncheckedModelFile(this.modLoc("block/pot_has_oil"));
            } else {
               return blockState.getValue(PotBlock.HAS_BASE)
                  ? new UncheckedModelFile(this.modLoc("block/pot_base"))
                  : new UncheckedModelFile(this.modLoc("block/pot"));
            }
         }
      );
      this.horizontalBlock(
         (Block)ModBlocks.STOCKPOT.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(StockpotBlock.HAS_LID)) {
               if ((Boolean)blockState.getValue(StockpotBlock.HAS_BASE)) {
                  return new UncheckedModelFile(this.modLoc("block/stockpot_base_has_lid"));
               } else {
                  return blockState.getValue(StockpotBlock.HAS_CHAINS)
                     ? new UncheckedModelFile(this.modLoc("block/stockpot_chains_has_lid"))
                     : new UncheckedModelFile(this.modLoc("block/stockpot_has_lid"));
               }
            } else if ((Boolean)blockState.getValue(StockpotBlock.HAS_BASE)) {
               return new UncheckedModelFile(this.modLoc("block/stockpot_base"));
            } else {
               return blockState.getValue(StockpotBlock.HAS_CHAINS)
                  ? new UncheckedModelFile(this.modLoc("block/stockpot_chains"))
                  : new UncheckedModelFile(this.modLoc("block/stockpot"));
            }
         }
      );
      this.horizontalBlock(
         (Block)ModBlocks.SHAWARMA_SPIT.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(ShawarmaSpitBlock.POWERED)) {
               return blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER
                  ? new UncheckedModelFile(this.modLoc("block/shawarma_spit_powered_lower"))
                  : new UncheckedModelFile(this.modLoc("block/shawarma_spit_powered_upper"));
            } else {
               return blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER
                  ? new UncheckedModelFile(this.modLoc("block/shawarma_spit_lower"))
                  : new UncheckedModelFile(this.modLoc("block/shawarma_spit_upper"));
            }
         }
      );
      this.horizontalBlock(
         (Block)ModBlocks.OIL_POT.get(),
         blockState -> blockState.getValue(OilPotBlock.HAS_OIL)
            ? new UncheckedModelFile(this.modLoc("block/oil_pot_with_oil"))
            : new UncheckedModelFile(this.modLoc("block/oil_pot"))
      );
      this.horizontalBlock((Block)ModBlocks.STEAMER.get(), blockState -> {
         boolean hasLid = (Boolean)blockState.getValue(SteamerBlock.HAS_LID);
         boolean hasBase = (Boolean)blockState.getValue(SteamerBlock.HAS_BASE);
         boolean half = (Boolean)blockState.getValue(SteamerBlock.HALF);
         if (hasLid && hasBase && half) {
            return new UncheckedModelFile(this.modLoc("block/steamer_half_lid_base"));
         } else if (hasLid && hasBase) {
            return new UncheckedModelFile(this.modLoc("block/steamer_full_lid_base"));
         } else if (hasLid && half) {
            return new UncheckedModelFile(this.modLoc("block/steamer_half_lid"));
         } else if (hasLid) {
            return new UncheckedModelFile(this.modLoc("block/steamer_full_lid"));
         } else if (hasBase && half) {
            return new UncheckedModelFile(this.modLoc("block/steamer_half_base"));
         } else if (hasBase) {
            return new UncheckedModelFile(this.modLoc("block/steamer_full_base"));
         } else {
            return half ? new UncheckedModelFile(this.modLoc("block/steamer_half")) : new UncheckedModelFile(this.modLoc("block/steamer_full"));
         }
      });
      FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
         Block block = (Block)BuiltInRegistries.BLOCK.getValue(key);
         if (value.blockType() == FoodBiteRegistry.BlockType.ONE_BY_TWO) {
            this.addOneByTwoFoodBiteBlock(block, key);
         } else {
            this.addFoodBiteBlock(block, key);
         }
      });
      PlateRegistry.PLATE_DATA_MAP.forEach((key, value) -> {
         Block block = (Block)BuiltInRegistries.BLOCK.getValue(key);
         this.addPlateBlock(block, key);
      });
      this.horizontalBlock((Block)ModBlocks.EMPTY_CUP.get(), blockState -> {
         int count = (Integer)blockState.getValue(EmptyCupBlock.CUP_COUNT);
         Identifier model = this.modLoc("block/teacup/empty_cup/count%d".formatted(count));
         return new UncheckedModelFile(model);
      });
      TeacupRegistry.TEACUP_DATA_MAP.forEach((key, value) -> {
         Block block = (Block)BuiltInRegistries.BLOCK.getValue(key);
         this.addTeacupBlock(block, key);
      });
      this.addStackableFoodBlock((Block)ModBlocks.BAMBOO_TUBE_RICE.get(), this.modLoc("bamboo_tube_rice"));
      this.horizontalBlock((Block)ModBlocks.FRUIT_BASKET.get(), new UncheckedModelFile(this.modLoc("block/fruit_basket")));
      this.horizontalBlock((Block)ModBlocks.CHOPPING_BOARD.get(), new UncheckedModelFile(this.modLoc("block/chopping_board")));
      this.horizontalBlock((Block)ModBlocks.KITCHENWARE_RACKS.get(), new UncheckedModelFile(this.modLoc("block/kitchenware_racks")));
      this.cookStool(ModBlocks.COOK_STOOL_OAK, "oak");
      this.cookStool(ModBlocks.COOK_STOOL_SPRUCE, "spruce");
      this.cookStool(ModBlocks.COOK_STOOL_ACACIA, "acacia");
      this.cookStool(ModBlocks.COOK_STOOL_BAMBOO, "bamboo");
      this.cookStool(ModBlocks.COOK_STOOL_BIRCH, "birch");
      this.cookStool(ModBlocks.COOK_STOOL_CHERRY, "cherry");
      this.cookStool(ModBlocks.COOK_STOOL_CRIMSON, "crimson");
      this.cookStool(ModBlocks.COOK_STOOL_DARK_OAK, "dark_oak");
      this.cookStool(ModBlocks.COOK_STOOL_JUNGLE, "jungle");
      this.cookStool(ModBlocks.COOK_STOOL_MANGROVE, "mangrove");
      this.cookStool(ModBlocks.COOK_STOOL_WARPED, "warped");
      this.chair(ModBlocks.CHAIR_OAK, "oak");
      this.chair(ModBlocks.CHAIR_SPRUCE, "spruce");
      this.chair(ModBlocks.CHAIR_ACACIA, "acacia");
      this.chair(ModBlocks.CHAIR_BAMBOO, "bamboo");
      this.chair(ModBlocks.CHAIR_BIRCH, "birch");
      this.chair(ModBlocks.CHAIR_CHERRY, "cherry");
      this.chair(ModBlocks.CHAIR_CRIMSON, "crimson");
      this.chair(ModBlocks.CHAIR_DARK_OAK, "dark_oak");
      this.chair(ModBlocks.CHAIR_JUNGLE, "jungle");
      this.chair(ModBlocks.CHAIR_MANGROVE, "mangrove");
      this.chair(ModBlocks.CHAIR_WARPED, "warped");
      this.table(ModBlocks.TABLE_OAK, "oak");
      this.table(ModBlocks.TABLE_SPRUCE, "spruce");
      this.table(ModBlocks.TABLE_ACACIA, "acacia");
      this.table(ModBlocks.TABLE_BAMBOO, "bamboo");
      this.table(ModBlocks.TABLE_BIRCH, "birch");
      this.table(ModBlocks.TABLE_CHERRY, "cherry");
      this.table(ModBlocks.TABLE_CRIMSON, "crimson");
      this.table(ModBlocks.TABLE_DARK_OAK, "dark_oak");
      this.table(ModBlocks.TABLE_JUNGLE, "jungle");
      this.table(ModBlocks.TABLE_MANGROVE, "mangrove");
      this.table(ModBlocks.TABLE_WARPED, "warped");
      this.simpleBlock((Block)ModBlocks.OIL_BLOCK.get());
      this.crop(ModBlocks.TOMATO_CROP, "tomato");
      this.crop(ModBlocks.CHILI_CROP, "chili");
      this.crop(ModBlocks.LETTUCE_CROP, "lettuce");
      this.axisBlock((RotatedPillarBlock)ModBlocks.STRAW_BLOCK.get());
      this.horizontalFaceBlock((Block)ModBlocks.RECIPE_BLOCK.get(), new UncheckedModelFile(this.modLoc("block/recipe_block")));
      this.riceCrop();
      this.variantBlock(
         (Block)ModBlocks.ENAMEL_BASIN.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(EnamelBasinBlock.HAS_LID)) {
               return new UncheckedModelFile(this.modLoc("block/enamel_basin/base"));
            } else {
               int oilCount = (Integer)blockState.getValue(EnamelBasinBlock.OIL_COUNT);
               if (oilCount <= 0) {
                  return new UncheckedModelFile(this.modLoc("block/enamel_basin/empty"));
               } else if (oilCount <= 10) {
                  return new UncheckedModelFile(this.modLoc("block/enamel_basin/low"));
               } else {
                  return oilCount <= 20
                     ? new UncheckedModelFile(this.modLoc("block/enamel_basin/middle"))
                     : new UncheckedModelFile(this.modLoc("block/enamel_basin/high"));
               }
            }
         }
      );
      this.variantBlock(
         (Block)ModBlocks.CHILI_RISTRA.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(ChiliRistraBlock.IS_HEAD)) {
               return blockState.getValue(ChiliRistraBlock.SHEARED)
                  ? new UncheckedModelFile(this.modLoc("block/chili_ristra/head_sheared"))
                  : new UncheckedModelFile(this.modLoc("block/chili_ristra/head"));
            } else {
               return blockState.getValue(ChiliRistraBlock.SHEARED)
                  ? new UncheckedModelFile(this.modLoc("block/chili_ristra/body_sheared"))
                  : new UncheckedModelFile(this.modLoc("block/chili_ristra/body"));
            }
         }
      );
      this.variantBlock(
         (Block)ModBlocks.STRUNG_MUSHROOMS.get(),
         blockState -> {
            if ((Boolean)blockState.getValue(StrungMushroomsBlock.IS_HEAD)) {
               return blockState.getValue(StrungMushroomsBlock.SHEARED)
                  ? new UncheckedModelFile(this.modLoc("block/strung_mushrooms/head_sheared"))
                  : new UncheckedModelFile(this.modLoc("block/strung_mushrooms/head"));
            } else {
               return blockState.getValue(StrungMushroomsBlock.SHEARED)
                  ? new UncheckedModelFile(this.modLoc("block/strung_mushrooms/body_sheared"))
                  : new UncheckedModelFile(this.modLoc("block/strung_mushrooms/body"));
            }
         }
      );
   }

   public void variantBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
      this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).build());
   }

   public void crop(DeferredBlock<Block> block, String name) {
      this.getVariantBuilder((Block)block.get()).forAllStates(state -> {
         int age = (Integer)state.getValue(CropBlock.AGE);
         Identifier file = this.modLoc("block/crop/%s/stage%d".formatted(name, age));
         return ConfiguredModel.builder().modelFile(new UncheckedModelFile(file)).build();
      });
   }

   public void riceCrop() {
      this.getVariantBuilder((Block)ModBlocks.RICE_CROP.get()).forAllStates(state -> {
         int age = (Integer)state.getValue(CropBlock.AGE);
         int location = (Integer)state.getValue(RiceCropBlock.LOCATION);
         Identifier file;
         if (location == 0) {
            file = this.modLoc("block/crop/rice/stage%d_down".formatted(age));
         } else if (location == 1) {
            file = this.modLoc("block/crop/rice/stage%d_middle".formatted(age));
         } else {
            file = this.modLoc("block/crop/rice/stage%d_up".formatted(age));
         }

         return ConfiguredModel.builder().modelFile(new UncheckedModelFile(file)).build();
      });
   }

   public void cookStool(DeferredBlock<Block> block, String name) {
      this.horizontalBlock((Block)block.get(), new UncheckedModelFile(this.modLoc("block/cook_stool/" + name)));
   }

   public void chair(DeferredBlock<Block> block, String name) {
      this.horizontalBlock((Block)block.get(), new UncheckedModelFile(this.modLoc("block/chair/" + name)));
   }

   private void table(DeferredBlock<Block> block, String name) {
      UncheckedModelFile leftModel = new UncheckedModelFile(this.modLoc("block/table/%s_left".formatted(name)));
      UncheckedModelFile rightModel = new UncheckedModelFile(this.modLoc("block/table/%s_right".formatted(name)));
      UncheckedModelFile middleModel = new UncheckedModelFile(this.modLoc("block/table/%s_middle".formatted(name)));
      UncheckedModelFile leftModelRot = new UncheckedModelFile(this.modLoc("block/table/%s_left_rot".formatted(name)));
      UncheckedModelFile rightModelRot = new UncheckedModelFile(this.modLoc("block/table/%s_right_rot".formatted(name)));
      UncheckedModelFile middleModelRot = new UncheckedModelFile(this.modLoc("block/table/%s_middle_rot".formatted(name)));
      this.getVariantBuilder((Block)block.get()).forAllStates(blockState -> {
         int position = (Integer)blockState.getValue(TableBlock.POSITION);
         if (position == 0) {
            return ConfiguredModel.builder().modelFile(new UncheckedModelFile(this.modLoc("block/table/%s_single".formatted(name)))).build();
         } else {
            boolean isRotation = blockState.getValue(TableBlock.AXIS) == Axis.Z;
            if (position == 1) {
               return isRotation ? ConfiguredModel.builder().modelFile(rightModelRot).build() : ConfiguredModel.builder().modelFile(rightModel).build();
            } else if (position == 3) {
               return isRotation ? ConfiguredModel.builder().modelFile(leftModelRot).build() : ConfiguredModel.builder().modelFile(leftModel).build();
            } else {
               return isRotation ? ConfiguredModel.builder().modelFile(middleModelRot).build() : ConfiguredModel.builder().modelFile(middleModel).build();
            }
         }
      });
   }

   public void addTeacupBlock(Block block, Identifier id) {
      this.horizontalBlock(block, blockState -> {
         if (blockState.getBlock() instanceof TeacupBlock teacupBlock) {
            int var8 = (Integer)blockState.getValue(teacupBlock.getCupCountProperty());
            int teaCount = (Integer)blockState.getValue(teacupBlock.getTeaCountProperty());
            if (teaCount > var8) {
               teaCount = var8;
            }

            String modelName = "count%d_%d".formatted(var8, teaCount);
            String modelPath = "block/teacup/%s/%s".formatted(id.getPath(), modelName);
            Identifier modelLoc = Identifier.fromNamespaceAndPath(id.getNamespace(), modelPath);
            return new UncheckedModelFile(modelLoc);
         } else {
            throw new IllegalArgumentException("Block must be an instance of TeacupBlock");
         }
      });
   }

   public void addFoodBiteBlock(Block block, Identifier id) {
      this.horizontalBlock(
         block,
         blockState -> {
            if (blockState.getBlock() instanceof FoodBiteBlock foodBiteBlock) {
               int var5 = (Integer)blockState.getValue(foodBiteBlock.getBites());
               Identifier model = Identifier.fromNamespaceAndPath(
                  id.getNamespace(), "block/food/%s/%s_%d".formatted(id.getPath(), id.getPath(), var5)
               );
               return new UncheckedModelFile(model);
            } else {
               throw new IllegalArgumentException("Block must be an instance of FoodBiteBlock");
            }
         }
      );
   }

   public void addOneByTwoFoodBiteBlock(Block block, Identifier id) {
      this.horizontalBlock(block, blockState -> {
         if (blockState.getBlock() instanceof FoodBiteOneByTwoBlock foodBiteBlock) {
            int var6 = (Integer)blockState.getValue(foodBiteBlock.getBites());
            int position = (Integer)blockState.getValue(FoodBiteOneByTwoBlock.POSITION);
            Identifier model;
            if (position == 0) {
               model = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/food/%s/%s_left_%d".formatted(id.getPath(), id.getPath(), var6));
            } else {
               model = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/food/%s/%s_right_%d".formatted(id.getPath(), id.getPath(), var6));
            }

            return new UncheckedModelFile(model);
         } else {
            throw new IllegalArgumentException("Block must be an instance of OneByTwoFoodBiteBlock");
         }
      });
   }

   public void addStackableFoodBlock(Block block, Identifier id) {
      this.horizontalBlock(
         block,
         blockState -> {
            if (blockState.getBlock() instanceof StackableFoodBlock stackableFoodBlock) {
               int var5 = (Integer)blockState.getValue(stackableFoodBlock.getCountProperty());
               Identifier model = Identifier.fromNamespaceAndPath(
                  id.getNamespace(), "block/food/%s/%s_%d".formatted(id.getPath(), id.getPath(), var5)
               );
               return new UncheckedModelFile(model);
            } else {
               throw new IllegalArgumentException("Block must be an instance of StackableFoodBlock");
            }
         }
      );
   }

   public void addPlateBlock(Block block, Identifier id) {
      this.horizontalBlock(
         block,
         blockState -> {
            if (blockState.getBlock() instanceof PlateBlock plateBlock) {
               int var5 = (Integer)blockState.getValue(plateBlock.getServingsProperty());
               Identifier model = Identifier.fromNamespaceAndPath(
                  id.getNamespace(), "block/plate/%s/%s_%d".formatted(id.getPath(), id.getPath(), var5)
               );
               return new UncheckedModelFile(model);
            } else {
               throw new IllegalArgumentException("Block must be an instance of PlateBlock");
            }
         }
      );
   }
}
