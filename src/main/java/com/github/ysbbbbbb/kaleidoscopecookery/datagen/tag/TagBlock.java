package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagBlock extends BlockTagsProvider {
   public TagBlock(PackOutput output, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
      super(output, lookupProvider, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(new Block[]{(Block)ModBlocks.STOVE.get(), (Block)ModBlocks.POT.get(), (Block)ModBlocks.MILLSTONE.get()});
      this.tag(BlockTags.MINEABLE_WITH_AXE)
         .add(
            new Block[]{
               (Block)ModBlocks.COOK_STOOL_OAK.get(),
               (Block)ModBlocks.COOK_STOOL_SPRUCE.get(),
               (Block)ModBlocks.COOK_STOOL_ACACIA.get(),
               (Block)ModBlocks.COOK_STOOL_BAMBOO.get(),
               (Block)ModBlocks.COOK_STOOL_BIRCH.get(),
               (Block)ModBlocks.COOK_STOOL_CHERRY.get(),
               (Block)ModBlocks.COOK_STOOL_CRIMSON.get(),
               (Block)ModBlocks.COOK_STOOL_DARK_OAK.get(),
               (Block)ModBlocks.COOK_STOOL_JUNGLE.get(),
               (Block)ModBlocks.COOK_STOOL_MANGROVE.get(),
               (Block)ModBlocks.COOK_STOOL_WARPED.get(),
               (Block)ModBlocks.CHAIR_OAK.get(),
               (Block)ModBlocks.CHAIR_SPRUCE.get(),
               (Block)ModBlocks.CHAIR_ACACIA.get(),
               (Block)ModBlocks.CHAIR_BAMBOO.get(),
               (Block)ModBlocks.CHAIR_BIRCH.get(),
               (Block)ModBlocks.CHAIR_CHERRY.get(),
               (Block)ModBlocks.CHAIR_CRIMSON.get(),
               (Block)ModBlocks.CHAIR_DARK_OAK.get(),
               (Block)ModBlocks.CHAIR_JUNGLE.get(),
               (Block)ModBlocks.CHAIR_MANGROVE.get(),
               (Block)ModBlocks.CHAIR_WARPED.get(),
               (Block)ModBlocks.TABLE_OAK.get(),
               (Block)ModBlocks.TABLE_SPRUCE.get(),
               (Block)ModBlocks.TABLE_ACACIA.get(),
               (Block)ModBlocks.TABLE_BAMBOO.get(),
               (Block)ModBlocks.TABLE_BIRCH.get(),
               (Block)ModBlocks.TABLE_CHERRY.get(),
               (Block)ModBlocks.TABLE_CRIMSON.get(),
               (Block)ModBlocks.TABLE_DARK_OAK.get(),
               (Block)ModBlocks.TABLE_JUNGLE.get(),
               (Block)ModBlocks.TABLE_MANGROVE.get(),
               (Block)ModBlocks.TABLE_WARPED.get(),
               (Block)ModBlocks.KITCHENWARE_RACKS.get(),
               (Block)ModBlocks.CHOPPING_BOARD.get()
            }
         );
      this.tag(BlockTags.MINEABLE_WITH_HOE).add((Block)ModBlocks.STRAW_BLOCK.get());
      this.tag(TagMod.TUNDRA_STRIDER_SPEED_BLOCKS)
         .add(new Block[]{Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.PACKED_ICE, Blocks.ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE});
      this.tag(TagMod.WARMTH_HEAT_SOURCE_BLOCKS).add(new Block[]{Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.LAVA, Blocks.MAGMA_BLOCK});
      this.tag(TagMod.CAT_LIE_ON_BLOCKS)
         .add(
            new Block[]{
               (Block)ModBlocks.FRUIT_BASKET.get(),
               (Block)ModBlocks.CHAIR_OAK.get(),
               (Block)ModBlocks.CHAIR_SPRUCE.get(),
               (Block)ModBlocks.CHAIR_ACACIA.get(),
               (Block)ModBlocks.CHAIR_BAMBOO.get(),
               (Block)ModBlocks.CHAIR_BIRCH.get(),
               (Block)ModBlocks.CHAIR_CHERRY.get(),
               (Block)ModBlocks.CHAIR_CRIMSON.get(),
               (Block)ModBlocks.CHAIR_DARK_OAK.get(),
               (Block)ModBlocks.CHAIR_JUNGLE.get(),
               (Block)ModBlocks.CHAIR_MANGROVE.get(),
               (Block)ModBlocks.CHAIR_WARPED.get()
            }
         );
      this.tag(TagCommon.FD_HEAT_SOURCES).add((Block)ModBlocks.STOVE.get());
      this.tag(TagMod.HEAT_SOURCE_BLOCKS_WITHOUT_LIT)
         .add(new Block[]{Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.LAVA, Blocks.MAGMA_BLOCK})
         .addTag(TagCommon.FD_HEAT_SOURCES);
      this.tag(BlockTags.CROPS)
         .add(
            new Block[]{
               (Block)ModBlocks.TOMATO_CROP.get(), (Block)ModBlocks.RICE_CROP.get(), (Block)ModBlocks.CHILI_CROP.get(), (Block)ModBlocks.LETTUCE_CROP.get()
            }
         );
      this.tag(TagMod.COOK_STOOL)
         .add(
            new Block[]{
               (Block)ModBlocks.COOK_STOOL_OAK.get(),
               (Block)ModBlocks.COOK_STOOL_SPRUCE.get(),
               (Block)ModBlocks.COOK_STOOL_ACACIA.get(),
               (Block)ModBlocks.COOK_STOOL_BAMBOO.get(),
               (Block)ModBlocks.COOK_STOOL_BIRCH.get(),
               (Block)ModBlocks.COOK_STOOL_CHERRY.get(),
               (Block)ModBlocks.COOK_STOOL_CRIMSON.get(),
               (Block)ModBlocks.COOK_STOOL_DARK_OAK.get(),
               (Block)ModBlocks.COOK_STOOL_JUNGLE.get(),
               (Block)ModBlocks.COOK_STOOL_MANGROVE.get(),
               (Block)ModBlocks.COOK_STOOL_WARPED.get()
            }
         );
      this.tag(TagMod.CHAIR)
         .add(
            new Block[]{
               (Block)ModBlocks.CHAIR_OAK.get(),
               (Block)ModBlocks.CHAIR_SPRUCE.get(),
               (Block)ModBlocks.CHAIR_ACACIA.get(),
               (Block)ModBlocks.CHAIR_BAMBOO.get(),
               (Block)ModBlocks.CHAIR_BIRCH.get(),
               (Block)ModBlocks.CHAIR_CHERRY.get(),
               (Block)ModBlocks.CHAIR_CRIMSON.get(),
               (Block)ModBlocks.CHAIR_DARK_OAK.get(),
               (Block)ModBlocks.CHAIR_JUNGLE.get(),
               (Block)ModBlocks.CHAIR_MANGROVE.get(),
               (Block)ModBlocks.CHAIR_WARPED.get()
            }
         );
      this.tag(TagMod.TABLE)
         .add(
            new Block[]{
               (Block)ModBlocks.TABLE_OAK.get(),
               (Block)ModBlocks.TABLE_SPRUCE.get(),
               (Block)ModBlocks.TABLE_ACACIA.get(),
               (Block)ModBlocks.TABLE_BAMBOO.get(),
               (Block)ModBlocks.TABLE_BIRCH.get(),
               (Block)ModBlocks.TABLE_CHERRY.get(),
               (Block)ModBlocks.TABLE_CRIMSON.get(),
               (Block)ModBlocks.TABLE_DARK_OAK.get(),
               (Block)ModBlocks.TABLE_JUNGLE.get(),
               (Block)ModBlocks.TABLE_MANGROVE.get(),
               (Block)ModBlocks.TABLE_WARPED.get()
            }
         );
      this.tag(TagMod.SITTABLE).addTag(TagMod.COOK_STOOL).addTag(TagMod.CHAIR).add((Block)ModBlocks.TRASH_CAN.get());
      this.tag(TagMod.RICE_PLANTABLE).addOptional(Identifier.parse("farmersdelight:rich_soil_farmland"));
      this.tag(TagMod.SICKLE_HARVEST_BLACKLIST)
         .add(new Block[]{Blocks.MELON_STEM, Blocks.PUMPKIN_STEM})
         .add(new Block[]{Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM});
      this.tag(TagCommon.SPRING_CROPS_BLOCK).add((Block)ModBlocks.LETTUCE_CROP.get());
      this.tag(TagCommon.SUMMER_CROPS_BLOCK)
         .add(new Block[]{(Block)ModBlocks.TOMATO_CROP.get(), (Block)ModBlocks.RICE_CROP.get(), (Block)ModBlocks.CHILI_CROP.get()});
      this.tag(TagCommon.AUTUMN_CROPS_BLOCK)
         .add(
            new Block[]{
               (Block)ModBlocks.TOMATO_CROP.get(), (Block)ModBlocks.RICE_CROP.get(), (Block)ModBlocks.LETTUCE_CROP.get(), (Block)ModBlocks.CHILI_CROP.get()
            }
         );
      this.tag(TagCommon.DRY_AVERAGE).add(new Block[]{(Block)ModBlocks.TOMATO_CROP.get(), (Block)ModBlocks.CHILI_CROP.get()});
      this.tag(TagCommon.AVERAGE_MOIST)
         .add(new Block[]{(Block)ModBlocks.TOMATO_CROP.get(), (Block)ModBlocks.LETTUCE_CROP.get(), (Block)ModBlocks.CHILI_CROP.get()});
      this.tag(TagCommon.MOIST_HUMID).add(new Block[]{(Block)ModBlocks.LETTUCE_CROP.get(), (Block)ModBlocks.RICE_CROP.get()});
      this.tag(TagCommon.HUMID_HUMID).add((Block)ModBlocks.RICE_CROP.get());
      IntrinsicTagAppender<Block> carryOn = this.tag(TagCommon.CARRYON_BLOCK_BLACKLIST);
      BuiltInRegistries.BLOCK
         .keySet()
         .stream()
         .filter(id -> id.getNamespace().equals("kaleidoscope_cookery"))
         .forEach(id -> carryOn.add((Block)BuiltInRegistries.BLOCK.getValue(id)));
      this.tag(TagCommon.FTB_SINGLE_CROP_HARVESTING_BLACKLIST).add((Block)ModBlocks.RICE_CROP.get());
      this.tag(TagCommon.FTB_EXCLUDED_BLOCKS).add((Block)ModBlocks.RICE_CROP.get());
      this.tag(BlockTags.BEE_GROWABLES).remove((Block)ModBlocks.RICE_CROP.get());
   }
}
