package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FoodBiteRegistry {
   public static final Map<Identifier, FoodBiteRegistry.FoodData> FOOD_DATA_MAP = Maps.newLinkedHashMap();
   public static Identifier DARK_CUISINE;
   public static Identifier SUSPICIOUS_STIR_FRY;
   public static Identifier SLIME_BALL_MEAL;
   public static Identifier FONDANT_PIE;
   public static Identifier DONGPO_PORK;
   public static Identifier FONDANT_SPIDER_EYE;
   public static Identifier CHORUS_FRIED_EGG;
   public static Identifier GOLDEN_SALAD;
   public static Identifier SPICY_CHICKEN;
   public static Identifier PAN_SEARED_KNIGHT_STEAK;
   public static Identifier STARGAZY_PIE;
   public static Identifier SWEET_AND_SOUR_ENDER_PEARLS;
   public static Identifier CRYSTAL_LAMB_CHOP;
   public static Identifier BLAZE_LAMB_CHOP;
   public static Identifier FROST_LAMB_CHOP;
   public static Identifier NETHER_STYLE_SASHIMI;
   public static Identifier END_STYLE_SASHIMI;
   public static Identifier DESERT_STYLE_SASHIMI;
   public static Identifier TUNDRA_STYLE_SASHIMI;
   public static Identifier COLD_STYLE_SASHIMI;
   public static Identifier CANDIED_POTATO;
   public static Identifier DOUGH_DROP_SOUP;
   public static Identifier STUFFED_TIGER_SKIN_PEPPER;
   public static Identifier SPICY_RABBIT_HEAD;
   public static Identifier FOUR_JOY_MEATBALL_SOUP;
   public static Identifier NUMBING_SPICY_CHICKEN;
   public static Identifier FRIED_CATERPILLAR;
   public static Identifier FRIED_SPRING_ROLL;
   public static Identifier SPICY_BLOOD_STEW;
   public static Identifier BRAISED_PORK_RIBS;
   public static Identifier COLD_ROASTED_MEAT;
   public static Identifier OIL_SPLASHED_FISH;
   public static Identifier BROWN_MUSHROOM_POT_SOUP;
   public static Identifier RED_MUSHROOM_POT_SOUP;
   public static Identifier WARPED_FUNGUS_POT_SOUP;
   public static Identifier CRIMSON_FUNGUS_POT_SOUP;
   public static Identifier BUDDHA_JUMPS_OVER_THE_WALL;

   public static void init() {
      FoodBiteRegistry registry = new FoodBiteRegistry();
      DARK_CUISINE = registry.registerFoodData(
         "dark_cuisine",
         FoodBiteRegistry.FoodData.create(3, ModFoods.DARK_CUISINE_BLOCK, ModFoods.DARK_CUISINE_ITEM)
            .setAnimateTick(FoodBiteAnimateTicks.DARK_CUISINE_ANIMATE_TICK)
      );
      SUSPICIOUS_STIR_FRY = registry.registerFoodData(
         "suspicious_stir_fry",
         FoodBiteRegistry.FoodData.create(1, ModFoods.SUSPICIOUS_STIR_FRY_BLOCK, ModFoods.SUSPICIOUS_STIR_FRY_ITEM)
            .setAnimateTick(FoodBiteAnimateTicks.SUSPICIOUS_STIR_FRY_ANIMATE_TICK)
      );
      SLIME_BALL_MEAL = registry.registerFoodData(
         "slime_ball_meal", FoodBiteRegistry.FoodData.create(3, ModFoods.SLIME_BALL_MEAL_BLOCK, ModFoods.SLIME_BALL_MEAL_ITEM)
      );
      FONDANT_PIE = registry.registerFoodData("fondant_pie", FoodBiteRegistry.FoodData.create(4, ModFoods.FONDANT_PIE_BLOCK, ModFoods.FONDANT_PIE_ITEM));
      DONGPO_PORK = registry.registerFoodData(
         "dongpo_pork", FoodBiteRegistry.FoodData.create(3, ModFoods.DONGPO_PORK_BLOCK, ModFoods.DONGPO_PORK_ITEM).addLootItems(Items.BAMBOO)
      );
      FONDANT_SPIDER_EYE = registry.registerFoodData(
         "fondant_spider_eye", FoodBiteRegistry.FoodData.create(4, ModFoods.FONDANT_SPIDER_EYE_BLOCK, ModFoods.FONDANT_SPIDER_EYE_ITEM)
      );
      CHORUS_FRIED_EGG = registry.registerFoodData(
         "chorus_fried_egg", FoodBiteRegistry.FoodData.create(3, ModFoods.CHORUS_FRIED_EGG_BLOCK, ModFoods.CHORUS_FRIED_EGG_ITEM)
      );
      SPICY_CHICKEN = registry.registerFoodData("spicy_chicken", FoodBiteRegistry.FoodData.create(4, ModFoods.SPICY_CHICKEN_BLOCK, ModFoods.SPICY_CHICKEN_ITEM));
      PAN_SEARED_KNIGHT_STEAK = registry.registerFoodData(
         "pan_seared_knight_steak",
         FoodBiteRegistry.FoodData.create(4, ModFoods.PAN_SEARED_KNIGHT_STEAK_BLOCK, ModFoods.PAN_SEARED_KNIGHT_STEAK_ITEM)
            .addLootItems(Items.BONE, Items.BONE_MEAL)
      );
      STARGAZY_PIE = registry.registerFoodData("stargazy_pie", FoodBiteRegistry.FoodData.create(4, ModFoods.STARGAZY_PIE_BLOCK, ModFoods.STARGAZY_PIE_ITEM));
      SWEET_AND_SOUR_ENDER_PEARLS = registry.registerFoodData(
         "sweet_and_sour_ender_pearls",
         FoodBiteRegistry.FoodData.create(3, ModFoods.SWEET_AND_SOUR_ENDER_PEARLS_BLOCK, ModFoods.SWEET_AND_SOUR_ENDER_PEARLS_ITEM)
      );
      CRYSTAL_LAMB_CHOP = registry.registerFoodData(
         "crystal_lamb_chop",
         FoodBiteRegistry.FoodData.create(3, ModFoods.CRYSTAL_LAMB_CHOP_BLOCK, ModFoods.CRYSTAL_LAMB_CHOP_ITEM).addLootItems(Items.AMETHYST_SHARD)
      );
      BLAZE_LAMB_CHOP = registry.registerFoodData(
         "blaze_lamb_chop", FoodBiteRegistry.FoodData.create(3, ModFoods.BLAZE_LAMB_CHOP_BLOCK, ModFoods.BLAZE_LAMB_CHOP_ITEM).addLootItems(Items.BLAZE_ROD)
      );
      FROST_LAMB_CHOP = registry.registerFoodData(
         "frost_lamb_chop", FoodBiteRegistry.FoodData.create(3, ModFoods.FROST_LAMB_CHOP_BLOCK, ModFoods.FROST_LAMB_CHOP_ITEM).addLootItems(Items.BLUE_ICE)
      );
      NETHER_STYLE_SASHIMI = registry.registerFoodData(
         "nether_style_sashimi",
         FoodBiteRegistry.FoodData.create(4, ModFoods.NETHER_STYLE_SASHIMI_BLOCK, ModFoods.NETHER_STYLE_SASHIMI_ITEM)
            .addLootItems(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS)
      );
      END_STYLE_SASHIMI = registry.registerFoodData(
         "end_style_sashimi",
         FoodBiteRegistry.FoodData.create(4, ModFoods.END_STYLE_SASHIMI_BLOCK, ModFoods.END_STYLE_SASHIMI_ITEM).addLootItems(Items.CHORUS_FRUIT)
      );
      DESERT_STYLE_SASHIMI = registry.registerFoodData(
         "desert_style_sashimi",
         FoodBiteRegistry.FoodData.create(4, ModFoods.DESERT_STYLE_SASHIMI_BLOCK, ModFoods.DESERT_STYLE_SASHIMI_ITEM).addLootItems(Items.CACTUS)
      );
      TUNDRA_STYLE_SASHIMI = registry.registerFoodData(
         "tundra_style_sashimi", FoodBiteRegistry.FoodData.create(4, ModFoods.TUNDRA_STYLE_SASHIMI_BLOCK, ModFoods.TUNDRA_STYLE_SASHIMI_ITEM)
      );
      COLD_STYLE_SASHIMI = registry.registerFoodData(
         "cold_style_sashimi",
         FoodBiteRegistry.FoodData.create(4, ModFoods.COLD_STYLE_SASHIMI_BLOCK, ModFoods.COLD_STYLE_SASHIMI_ITEM).addLootItems(Items.SNOWBALL, Items.SNOWBALL)
      );
      CANDIED_POTATO = registry.registerFoodData(
         "candied_potato", FoodBiteRegistry.FoodData.create(3, ModFoods.CANDIED_POTATO_BLOCK, ModFoods.CANDIED_POTATO_ITEM)
      );
      STUFFED_TIGER_SKIN_PEPPER = registry.registerFoodData(
         "stuffed_tiger_skin_pepper", FoodBiteRegistry.FoodData.create(5, ModFoods.STUFFED_TIGER_SKIN_PEPPER_BLOCK, ModFoods.STUFFED_TIGER_SKIN_PEPPER_ITEM)
      );
      SPICY_RABBIT_HEAD = registry.registerFoodData(
         "spicy_rabbit_head", FoodBiteRegistry.FoodData.create(3, ModFoods.SPICY_RABBIT_HEAD_BLOCK, ModFoods.SPICY_RABBIT_HEAD_ITEM)
      );
      FRIED_CATERPILLAR = registry.registerFoodData(
         "fried_caterpillar",
         FoodBiteRegistry.FoodData.create(3, ModFoods.FRIED_CATERPILLAR_BLOCK, ModFoods.FRIED_CATERPILLAR_ITEM)
            .setAABB(Block.box(1.0, 0.0, 3.0, 15.0, 4.0, 13.0))
      );
      FRIED_SPRING_ROLL = registry.registerFoodData(
         "fried_spring_roll", FoodBiteRegistry.FoodData.create(3, ModFoods.FRIED_SPRING_ROLL_BLOCK, ModFoods.FRIED_SPRING_ROLL_ITEM)
      );
      BRAISED_PORK_RIBS = registry.registerFoodData(
         "braised_pork_ribs",
         FoodBiteRegistry.FoodData.createOneByTwo(4, ModFoods.BRAISED_PORK_RIBS_BLOCK, ModFoods.BRAISED_PORK_RIBS_ITEM).addLootItems(Items.BONE)
      );
      COLD_ROASTED_MEAT = registry.registerFoodData(
         "cold_roasted_meat", FoodBiteRegistry.FoodData.createOneByTwo(3, ModFoods.COLD_ROASTED_MEAT_BLOCK, ModFoods.COLD_ROASTED_MEAT_ITEM)
      );
      OIL_SPLASHED_FISH = registry.registerFoodData(
         "oil_splashed_fish",
         FoodBiteRegistry.FoodData.createOneByTwo(5, ModFoods.OIL_SPLASHED_FISH_BLOCK, ModFoods.OIL_SPLASHED_FISH_ITEM).addLootItems(Items.BONE_MEAL)
      );
      DOUGH_DROP_SOUP = registry.registerFoodData(
         "dough_drop_soup", FoodBiteRegistry.FoodData.create(3, ModFoods.DOUGH_DROP_SOUP_BLOCK, ModFoods.DOUGH_DROP_SOUP_ITEM).bowlAABB()
      );
      FOUR_JOY_MEATBALL_SOUP = registry.registerFoodData(
         "four_joy_meatball_soup", FoodBiteRegistry.FoodData.create(4, ModFoods.FOUR_JOY_MEATBALL_SOUP_BLOCK, ModFoods.FOUR_JOY_MEATBALL_SOUP_ITEM).bowlAABB()
      );
      NUMBING_SPICY_CHICKEN = registry.registerFoodData(
         "numbing_spicy_chicken", FoodBiteRegistry.FoodData.create(3, ModFoods.NUMBING_SPICY_CHICKEN_BLOCK, ModFoods.NUMBING_SPICY_CHICKEN_ITEM).bowlAABB()
      );
      SPICY_BLOOD_STEW = registry.registerFoodData(
         "spicy_blood_stew", FoodBiteRegistry.FoodData.create(3, ModFoods.SPICY_BLOOD_STEW_BLOCK, ModFoods.SPICY_BLOOD_STEW_ITEM).bowlAABB()
      );
      BROWN_MUSHROOM_POT_SOUP = registry.registerFoodData(
         "brown_mushroom_pot_soup",
         FoodBiteRegistry.FoodData.create(2, ModFoods.BROWN_MUSHROOM_POT_SOUP_BLOCK, ModFoods.BROWN_MUSHROOM_POT_SOUP_ITEM)
            .setLootItem(Items.FLOWER_POT)
            .soupPotAABB()
            .potSoupAnimateTick()
      );
      RED_MUSHROOM_POT_SOUP = registry.registerFoodData(
         "red_mushroom_pot_soup",
         FoodBiteRegistry.FoodData.create(2, ModFoods.RED_MUSHROOM_POT_SOUP_BLOCK, ModFoods.RED_MUSHROOM_POT_SOUP_ITEM)
            .setLootItem(Items.FLOWER_POT)
            .soupPotAABB()
            .potSoupAnimateTick()
      );
      WARPED_FUNGUS_POT_SOUP = registry.registerFoodData(
         "warped_fungus_pot_soup",
         FoodBiteRegistry.FoodData.create(2, ModFoods.WARPED_FUNGUS_POT_SOUP_BLOCK, ModFoods.WARPED_FUNGUS_POT_SOUP_ITEM)
            .setLootItem(Items.FLOWER_POT)
            .soupPotAABB()
            .potSoupAnimateTick()
      );
      CRIMSON_FUNGUS_POT_SOUP = registry.registerFoodData(
         "crimson_fungus_pot_soup",
         FoodBiteRegistry.FoodData.create(2, ModFoods.CRIMSON_FUNGUS_POT_SOUP_BLOCK, ModFoods.CRIMSON_FUNGUS_POT_SOUP_ITEM)
            .setLootItem(Items.FLOWER_POT)
            .soupPotAABB()
            .potSoupAnimateTick()
      );
      BUDDHA_JUMPS_OVER_THE_WALL = registry.registerFoodData(
         "buddha_jumps_over_the_wall",
         FoodBiteRegistry.FoodData.create(2, ModFoods.BUDDHA_JUMPS_OVER_THE_WALL_BLOCK, ModFoods.BUDDHA_JUMPS_OVER_THE_WALL_ITEM)
            .setLootItem(Items.FLOWER_POT)
            .soupPotAABB()
            .potSoupAnimateTick()
      );
      GOLDEN_SALAD = registry.registerFoodData("golden_salad", FoodBiteRegistry.FoodData.create(6, ModFoods.GOLDEN_SALAD_BLOCK, ModFoods.GOLDEN_SALAD_ITEM));
   }

   public Identifier registerFoodData(Identifier foodName, FoodBiteRegistry.FoodData data) {
      FOOD_DATA_MAP.put(foodName, data);
      return foodName;
   }

   public Identifier registerFoodData(String foodName, FoodBiteRegistry.FoodData data) {
      Identifier id = mcLoc(foodName);
      FOOD_DATA_MAP.put(id, data);
      return id;
   }

   public static Identifier mcLoc(String name) {
      return Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name);
   }

   public static Item getItem(Identifier name) {
      return (Item)BuiltInRegistries.ITEM.getValue(name);
   }

   public static Block getBlock(Identifier name) {
      return (Block)BuiltInRegistries.BLOCK.getValue(name);
   }

   public static enum BlockType {
      SINGLE,
      ONE_BY_TWO;
   }

   public static final class FoodData {
      private final FoodBiteRegistry.BlockType blockType;
      private final int maxBites;
      private final List<ItemLike> lootItems = Lists.newArrayList();
      private final FoodProperties blockFood;
      private final FoodProperties itemFood;
      @Nullable
      private FoodBiteAnimateTicks.AnimateTick animateTick = null;
      @Nullable
      private VoxelShape aabb = null;

      private FoodData(FoodBiteRegistry.BlockType blockType, int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
         this.blockType = blockType;
         this.maxBites = maxBites;
         this.lootItems.add(Items.BOWL);
         this.blockFood = blockFood;
         this.itemFood = itemFood;
      }

      public static FoodBiteRegistry.FoodData create(int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
         return new FoodBiteRegistry.FoodData(FoodBiteRegistry.BlockType.SINGLE, maxBites, blockFood, itemFood);
      }

      public static FoodBiteRegistry.FoodData createOneByTwo(int maxBites, FoodProperties blockFood, FoodProperties itemFood) {
         return new FoodBiteRegistry.FoodData(FoodBiteRegistry.BlockType.ONE_BY_TWO, maxBites, blockFood, itemFood);
      }

      public FoodBiteRegistry.FoodData setAnimateTick(FoodBiteAnimateTicks.AnimateTick animateTick) {
         this.animateTick = animateTick;
         return this;
      }

      public FoodBiteRegistry.FoodData potSoupAnimateTick() {
         this.animateTick = FoodBiteAnimateTicks.POT_SOUP_ANIMATE_TICK;
         return this;
      }

      public FoodBiteRegistry.FoodData addLootItems(ItemLike... lootItems) {
         this.lootItems.addAll(Arrays.stream(lootItems).toList());
         return this;
      }

      public FoodBiteRegistry.FoodData setLootItem(ItemLike lootItem) {
         this.lootItems.clear();
         this.lootItems.add(lootItem);
         return this;
      }

      public FoodBiteRegistry.FoodData setAABB(VoxelShape aabb) {
         this.aabb = aabb;
         return this;
      }

      public FoodBiteRegistry.FoodData bowlAABB() {
         this.aabb = Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);
         return this;
      }

      public FoodBiteRegistry.FoodData soupPotAABB() {
         this.aabb = Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0), Block.box(4.0, 1.0, 4.0, 12.0, 7.0, 12.0));
         return this;
      }

      public FoodBiteRegistry.BlockType blockType() {
         return this.blockType;
      }

      public int maxBites() {
         return this.maxBites;
      }

      @Nullable
      public VoxelShape getAABB() {
         return this.aabb;
      }

      @Nullable
      public FoodBiteAnimateTicks.AnimateTick animateTick() {
         return this.animateTick;
      }

      public List<ItemLike> getLootItems() {
         return this.lootItems;
      }

      public FoodProperties blockFood() {
         return this.blockFood;
      }

      public FoodProperties itemFood() {
         return this.itemFood;
      }
   }
}
