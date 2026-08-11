package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags.Items;

public class ShapedRecipeProvider extends ModRecipeProvider {
   public ShapedRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, (ItemLike)ModItems.STOVE.get())
         .pattern("###")
         .pattern("#F#")
         .pattern("###")
         .define('#', Items.COBBLESTONES)
         .define('F', net.minecraft.world.item.Items.CAMPFIRE)
         .unlockedBy("has_campfire", has(net.minecraft.world.item.Items.CAMPFIRE))
         .save(consumer, "kaleidoscope_cookery:stove_campfire");
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, (ItemLike)ModItems.STOVE.get())
         .pattern("###")
         .pattern("#F#")
         .pattern("###")
         .define('#', Items.COBBLESTONES)
         .define('F', net.minecraft.world.item.Items.SOUL_CAMPFIRE)
         .unlockedBy("has_soul_campfire", has(net.minecraft.world.item.Items.SOUL_CAMPFIRE))
         .save(consumer, "kaleidoscope_cookery:stove_soul_campfire");
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.FRUIT_BASKET.get())
         .pattern(" S ")
         .pattern("#C#")
         .pattern("###")
         .define('S', net.minecraft.world.item.Items.STICK)
         .define('#', ItemTags.PLANKS)
         .define('C', net.minecraft.world.item.Items.CHEST)
         .unlockedBy("has_chest", has(net.minecraft.world.item.Items.CHEST))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.SCARECROW.get())
         .pattern(" H ")
         .pattern("SPS")
         .pattern(" # ")
         .define('H', TagMod.STRAW_HAT)
         .define('S', net.minecraft.world.item.Items.STICK)
         .define('P', net.minecraft.world.item.Items.PUMPKIN)
         .define('#', TagMod.STRAW_BALE)
         .unlockedBy("has_pumpkin", has(net.minecraft.world.item.Items.PUMPKIN))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, (ItemLike)ModItems.POT.get())
         .pattern("###")
         .pattern("###")
         .pattern(" # ")
         .define('#', Items.INGOTS_IRON)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.IRON_KITCHEN_KNIFE.get())
         .pattern("##")
         .pattern("#S")
         .define('#', Items.INGOTS_IRON)
         .define('S', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.GOLD_KITCHEN_KNIFE.get())
         .pattern("##")
         .pattern("#S")
         .define('#', Items.INGOTS_GOLD)
         .define('S', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.DIAMOND_KITCHEN_KNIFE.get())
         .pattern("##")
         .pattern("#S")
         .define('#', Items.GEMS_DIAMOND)
         .define('S', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.KITCHEN_SHOVEL.get())
         .pattern("I  ")
         .pattern(" N ")
         .pattern("  S")
         .define('I', Items.INGOTS_IRON)
         .define('N', Items.NUGGETS_IRON)
         .define('S', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.STRAW_HAT.get())
         .pattern(" W ")
         .pattern(" S ")
         .pattern("WWW")
         .define('W', net.minecraft.world.item.Items.WHEAT)
         .define('S', net.minecraft.world.item.Items.STRING)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.STRAW_HAT_FLOWER.get())
         .pattern("FFF")
         .pattern("FHF")
         .pattern("FFF")
         .define('F', ItemTags.FLOWERS)
         .define('H', (ItemLike)ModItems.STRAW_HAT.get())
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.OIL_BLOCK.get())
         .pattern("OOO")
         .pattern("OOO")
         .pattern("OOO")
         .define('O', (ItemLike)ModItems.OIL.get())
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.CHOPPING_BOARD.get())
         .pattern("PPP")
         .pattern("PPP")
         .define('P', ItemTags.WOODEN_PRESSURE_PLATES)
         .unlockedBy("has_wood", has(net.minecraft.world.item.Items.OAK_PLANKS))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.STOCKPOT.get())
         .pattern("B B")
         .pattern("I I")
         .pattern("III")
         .define('B', net.minecraft.world.item.Items.BRICK)
         .define('I', Items.INGOTS_IRON)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.STOCKPOT_LID.get())
         .pattern(" B ")
         .pattern("III")
         .define('B', net.minecraft.world.item.Items.BRICK)
         .define('I', Items.INGOTS_IRON)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.ENAMEL_BASIN.get())
         .pattern("O")
         .pattern("I")
         .pattern("B")
         .define('B', net.minecraft.world.item.Items.BUCKET)
         .define('I', net.minecraft.world.item.Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
         .define('O', net.minecraft.world.item.Items.STONE_BUTTON)
         .unlockedBy("has_bucket", has(net.minecraft.world.item.Items.BUCKET))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.KITCHENWARE_RACKS.get())
         .pattern("SSS")
         .pattern("INI")
         .define('S', net.minecraft.world.item.Items.STICK)
         .define('I', Items.INGOTS_IRON)
         .define('N', Items.NUGGETS_IRON)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.CHILI_RISTRA.get())
         .pattern("CC")
         .pattern("CC")
         .pattern("CC")
         .define('C', (ItemLike)ModItems.RED_CHILI.get())
         .unlockedBy("has_red_chili", has((ItemLike)ModItems.RED_CHILI.get()))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.STRUNG_MUSHROOMS.get())
         .pattern("MM")
         .pattern("MM")
         .pattern("MM")
         .define('M', net.minecraft.world.item.Items.BROWN_MUSHROOM)
         .unlockedBy("has_brown_mushroom", has(net.minecraft.world.item.Items.BROWN_MUSHROOM))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.STRAW_BLOCK.get())
         .pattern("RRR")
         .pattern("RRR")
         .pattern("RRR")
         .define('R', (ItemLike)ModItems.RICE_PANICLE.get())
         .unlockedBy("has_rice_panicle", has((ItemLike)ModItems.RICE_PANICLE.get()))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.FARMER_CHEST_PLATE.get())
         .pattern("I I")
         .pattern("LLL")
         .pattern("LLL")
         .define('I', Items.INGOTS_IRON)
         .define('L', net.minecraft.world.item.Items.LEATHER)
         .unlockedBy("has_leather", has(net.minecraft.world.item.Items.LEATHER))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.FARMER_LEGGINGS.get())
         .pattern("LIL")
         .pattern("L L")
         .pattern("L L")
         .define('I', Items.INGOTS_IRON)
         .define('L', net.minecraft.world.item.Items.LEATHER)
         .unlockedBy("has_leather", has(net.minecraft.world.item.Items.LEATHER))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.FARMER_BOOTS.get())
         .pattern("I I")
         .pattern("L L")
         .define('I', Items.INGOTS_IRON)
         .define('L', net.minecraft.world.item.Items.LEATHER)
         .unlockedBy("has_leather", has(net.minecraft.world.item.Items.LEATHER))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.SHAWARMA_SPIT.get())
         .pattern("ICI")
         .pattern("ICI")
         .define('I', net.minecraft.world.item.Items.CHAIN)
         .define('C', net.minecraft.world.item.Items.CAMPFIRE)
         .unlockedBy("has_campfire", has(net.minecraft.world.item.Items.CAMPFIRE))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.MILLSTONE.get())
         .pattern(" F ")
         .pattern("SG ")
         .pattern("TTT")
         .define('F', Items.FENCES_WOODEN)
         .define('S', net.minecraft.world.item.Items.STICK)
         .define('G', net.minecraft.world.item.Items.GRINDSTONE)
         .define('T', net.minecraft.world.item.Items.SMOOTH_STONE)
         .unlockedBy("has_smooth_stone", has(net.minecraft.world.item.Items.SMOOTH_STONE))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)ModItems.RECIPE_ITEM.get())
         .pattern("PP")
         .pattern("PP")
         .define('P', net.minecraft.world.item.Items.PAPER)
         .unlockedBy("has_paper", has(net.minecraft.world.item.Items.PAPER))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.STEAMER.get())
         .pattern("TTT")
         .pattern("BBB")
         .define('T', net.minecraft.world.item.Items.BAMBOO_TRAPDOOR)
         .define('B', net.minecraft.world.item.Items.BAMBOO_BLOCK)
         .unlockedBy("has_bamboo", has(net.minecraft.world.item.Items.BAMBOO))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.TRANSMUTATION_LUNCH_BAG.get())
         .pattern(" L ")
         .pattern("LSL")
         .pattern("LLL")
         .define('L', net.minecraft.world.item.Items.LEATHER)
         .define('S', net.minecraft.world.item.Items.NETHER_STAR)
         .unlockedBy("has_nether_star", has(net.minecraft.world.item.Items.NETHER_STAR))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.OIL_POT.get())
         .pattern("P ")
         .pattern("BS")
         .define('P', net.minecraft.world.item.Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
         .define('B', net.minecraft.world.item.Items.BUCKET)
         .define('S', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_bucket", has(net.minecraft.world.item.Items.BUCKET))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)ModItems.SICKLE.get())
         .pattern("AAB")
         .pattern(" CA")
         .pattern("C  ")
         .define('A', net.minecraft.world.item.Items.FLINT)
         .define('B', net.minecraft.world.item.Items.STRING)
         .define('C', net.minecraft.world.item.Items.STICK)
         .unlockedBy("has_flint", has(net.minecraft.world.item.Items.FLINT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, (ItemLike)ModItems.TEAPOT.get())
         .pattern(" I ")
         .pattern("IBI")
         .pattern("III")
         .define('I', Items.INGOTS_COPPER)
         .define('B', net.minecraft.world.item.Items.BUCKET)
         .unlockedBy("has_ingot_copper", has(net.minecraft.world.item.Items.COPPER_INGOT))
         .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike)ModItems.TRASH_CAN.get())
         .pattern("III")
         .pattern("ICI")
         .pattern("III")
         .define('I', Items.INGOTS_IRON)
         .define('C', net.minecraft.world.item.Items.COMPOSTER)
         .unlockedBy("has_ingot_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
         .save(consumer);
   }
}
