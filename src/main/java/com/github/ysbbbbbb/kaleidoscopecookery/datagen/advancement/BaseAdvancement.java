package com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.advancements.criterion.ItemPredicate.Builder;
import net.minecraft.advancements.criterion.MinMaxBounds.Doubles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BaseAdvancement {
   public static void generate(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
      AdvancementHolder root = AdvancementTools.makeTask((ItemLike)ModItems.POT.get(), "root")
         .addCriterion("has_pot", TriggerInstance.hasItems(new ItemPredicate[]{Builder.item().of(TagMod.COOKERY_MOD_ITEMS).build()}))
         .save(saver, AdvancementTools.modLoc("root"), existingFileHelper);
      AdvancementHolder ironKnife = AdvancementTools.makeTask((ItemLike)ModItems.IRON_KITCHEN_KNIFE.get(), "iron_knife")
         .parent(root)
         .addCriterion("has_iron_knife", TriggerInstance.hasItems(new ItemPredicate[]{Builder.item().of(TagMod.KITCHEN_KNIFE).build()}))
         .save(saver, AdvancementTools.modLoc("iron_knife"), existingFileHelper);
      AdvancementHolder netheriteKnife = AdvancementTools.makeGoal((ItemLike)ModItems.NETHERITE_KITCHEN_KNIFE.get(), "netherite_knife")
         .parent(ironKnife)
         .addCriterion("has_netherite_knife", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.NETHERITE_KITCHEN_KNIFE.get()}))
         .save(saver, AdvancementTools.modLoc("netherite_knife"), existingFileHelper);
      AdvancementHolder oil = AdvancementTools.makeTask((ItemLike)ModItems.OIL.get(), "oil")
         .parent(ironKnife)
         .addCriterion(
            "kill_pig",
            net.minecraft.advancements.criterion.KilledTrigger.TriggerInstance.playerKilledEntity(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(TagMod.PIG_OIL_SOURCE),
               net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType()
                  .direct(
                     net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                        .equipment(
                           net.minecraft.advancements.criterion.EntityEquipmentPredicate.Builder.equipment().mainhand(Builder.item().of(TagMod.KITCHEN_KNIFE))
                        )
                  )
            )
         )
         .save(saver, AdvancementTools.modLoc("oil"), existingFileHelper);
      AdvancementHolder choppingBoard = AdvancementTools.makeTask((ItemLike)ModItems.CHOPPING_BOARD.get(), "chopping_board")
         .parent(ironKnife)
         .addCriterion("use_chopping_board", ModEventTrigger.create("use_chopping_board"))
         .save(saver, AdvancementTools.modLoc("chopping_board"), existingFileHelper);
      AdvancementHolder dangerousChef = AdvancementTools.makeChallenge((ItemLike)ModItems.OIL.get(), "dangerous_chef")
         .parent(oil)
         .addCriterion(
            "kill_piglin_brute",
            net.minecraft.advancements.criterion.KilledTrigger.TriggerInstance.playerKilledEntity(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().of(EntityType.PIGLIN_BRUTE),
               net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType()
                  .direct(
                     net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                        .equipment(
                           net.minecraft.advancements.criterion.EntityEquipmentPredicate.Builder.equipment().mainhand(Builder.item().of(TagMod.KITCHEN_KNIFE))
                        )
                  )
            )
         )
         .save(saver, AdvancementTools.modLoc("dangerous_chef"), existingFileHelper);
      AdvancementHolder strawHat = AdvancementTools.makeTask((ItemLike)ModItems.STRAW_HAT.get(), "straw_hat")
         .parent(root)
         .addCriterion("has_straw_hat", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.STRAW_HAT.get()}))
         .save(saver, AdvancementTools.modLoc("straw_hat"), existingFileHelper);
      AdvancementHolder modSeed = AdvancementTools.makeTask((ItemLike)ModItems.TOMATO_SEED.get(), "tomato_seed")
         .parent(strawHat)
         .addCriterion("has_tomato_seed", TriggerInstance.hasItems(new ItemPredicate[]{Builder.item().of(TagMod.COOKERY_MOD_SEEDS).build()}))
         .requirements(Strategy.OR)
         .save(saver, AdvancementTools.modLoc("tomato_seed"), existingFileHelper);
      AdvancementHolder flowerStrawHat = AdvancementTools.makeTask((ItemLike)ModItems.STRAW_HAT_FLOWER.get(), "flower_straw_hat")
         .parent(strawHat)
         .addCriterion("has_flower_straw_hat", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.STRAW_HAT_FLOWER.get()}))
         .save(saver, AdvancementTools.modLoc("flower_straw_hat"), existingFileHelper);
      AdvancementHolder farmerSet = AdvancementTools.makeTask((ItemLike)ModItems.FARMER_CHEST_PLATE.get(), "farmer_set")
         .parent(strawHat)
         .addCriterion("has_straw_hat", TriggerInstance.hasItems(new ItemPredicate[]{Builder.item().of(TagMod.STRAW_HAT).build()}))
         .addCriterion("has_farmer_chestplate", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.FARMER_CHEST_PLATE.get()}))
         .addCriterion("has_farmer_leggings", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.FARMER_LEGGINGS.get()}))
         .addCriterion("has_farmer_boots", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.FARMER_BOOTS.get()}))
         .requirements(Strategy.AND)
         .save(saver, AdvancementTools.modLoc("farmer_set"), existingFileHelper);
      AdvancementHolder ironHoe = AdvancementTools.makeTask(Items.IRON_HOE, "iron_hoe")
         .parent(modSeed)
         .addCriterion("use_hoe_on_water_field", ModEventTrigger.create("use_hoe_on_water_field"))
         .save(saver, AdvancementTools.modLoc("iron_hoe"), existingFileHelper);
      AdvancementHolder caterpillar = AdvancementTools.makeTask((ItemLike)ModItems.CATERPILLAR.get(), "caterpillar")
         .parent(modSeed)
         .addCriterion("has_caterpillar", TriggerInstance.hasItems(new ItemPredicate[]{Builder.item().of(TagMod.CATERPILLARS).build()}))
         .save(saver, AdvancementTools.modLoc("caterpillar"), existingFileHelper);
      AdvancementHolder dualChili = AdvancementTools.makeTask((ItemLike)ModItems.RED_CHILI.get(), "dual_chili")
         .parent(modSeed)
         .addCriterion("has_red_chili", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.RED_CHILI.get()}))
         .addCriterion("has_green_chili", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.GREEN_CHILI.get()}))
         .requirements(Strategy.AND)
         .save(saver, AdvancementTools.modLoc("dual_chili"), existingFileHelper);
      AdvancementHolder ricePanicle = AdvancementTools.makeTask((ItemLike)ModItems.RICE_PANICLE.get(), "rice_panicle")
         .parent(ironHoe)
         .addCriterion("has_rice_panicle", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.RICE_PANICLE.get()}))
         .save(saver, AdvancementTools.modLoc("rice_panicle"), existingFileHelper);
      AdvancementHolder feedChicken = AdvancementTools.makeTask((ItemLike)ModItems.CATERPILLAR.get(), "feed_chicken")
         .parent(caterpillar)
         .addCriterion("use_caterpillar_feed_chicken", ModEventTrigger.create("use_caterpillar_feed_chicken"))
         .save(saver, AdvancementTools.modLoc("feed_chicken"), existingFileHelper);
      AdvancementHolder fishRice = AdvancementTools.makeGoal(Items.SALMON, "fish_rice")
         .parent(ricePanicle)
         .addCriterion("place_fish_in_rice_field", ModEventTrigger.create("place_fish_in_rice_field"))
         .save(saver, AdvancementTools.modLoc("fish_rice"), existingFileHelper);
      AdvancementHolder stove = AdvancementTools.makeTask((ItemLike)ModItems.STOVE.get(), "stove")
         .parent(root)
         .addCriterion("lit_the_stove", ModEventTrigger.create("lit_the_stove"))
         .save(saver, AdvancementTools.modLoc("stove"), existingFileHelper);
      AdvancementHolder pot = AdvancementTools.makeTask((ItemLike)ModItems.POT.get(), "pot")
         .parent(stove)
         .addCriterion("place_pot_on_heat_source", ModEventTrigger.create("place_pot_on_heat_source"))
         .save(saver, AdvancementTools.modLoc("pot"), existingFileHelper);
      AdvancementHolder addOil = AdvancementTools.makeTask((ItemLike)ModItems.OIL.get(), "add_oil")
         .parent(pot)
         .addCriterion("put_oil_in_pot", ModEventTrigger.create("put_oil_in_pot"))
         .save(saver, AdvancementTools.modLoc("add_oil"), existingFileHelper);
      AdvancementHolder stirFry = AdvancementTools.makeTask((ItemLike)ModItems.KITCHEN_SHOVEL.get(), "stir_fry")
         .parent(addOil)
         .addCriterion("stir_fry_in_pot", ModEventTrigger.create("stir_fry_in_pot"))
         .save(saver, AdvancementTools.modLoc("stir_fry"), existingFileHelper);
      AdvancementHolder darkCuisine = AdvancementTools.makeTask(FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY), "dark_cuisine")
         .parent(stirFry)
         .addCriterion("has_suspicious_stew", TriggerInstance.hasItems(new ItemLike[]{FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY)}))
         .addCriterion("has_dark_cuisine", TriggerInstance.hasItems(new ItemLike[]{FoodBiteRegistry.getItem(FoodBiteRegistry.DARK_CUISINE)}))
         .requirements(Strategy.OR)
         .save(saver, AdvancementTools.modLoc("dark_cuisine"), existingFileHelper);
      AdvancementHolder burnHand = AdvancementTools.makeTask(Items.FLINT_AND_STEEL, "burn_hand")
         .parent(stirFry)
         .addCriterion("hurt_when_takeout_from_pot", ModEventTrigger.create("hurt_when_takeout_from_pot"))
         .save(saver, AdvancementTools.modLoc("burn_hand"), existingFileHelper);
      AdvancementHolder stockpot = AdvancementTools.makeTask((ItemLike)ModItems.STOCKPOT.get(), "stockpot")
         .parent(stove)
         .addCriterion("place_stockpot_on_heat_source", ModEventTrigger.create("place_stockpot_on_heat_source"))
         .save(saver, AdvancementTools.modLoc("stockpot"), existingFileHelper);
      AdvancementHolder addBroth = AdvancementTools.makeTask(Items.LAVA_BUCKET, "add_broth")
         .parent(stockpot)
         .addCriterion("put_soup_base_in_stockpot", ModEventTrigger.create("put_soup_base_in_stockpot"))
         .save(saver, AdvancementTools.modLoc("add_broth"), existingFileHelper);
      AdvancementHolder makeSoup = AdvancementTools.makeTask((ItemLike)ModItems.STOCKPOT_LID.get(), "make_soup")
         .parent(addBroth)
         .addCriterion("use_lid_on_stockpot", ModEventTrigger.create("use_lid_on_stockpot"))
         .save(saver, AdvancementTools.modLoc("make_soup"), existingFileHelper);
      AdvancementHolder burnHandSoup = AdvancementTools.makeTask(Items.FLINT_AND_STEEL, "burn_hand_soup")
         .parent(makeSoup)
         .addCriterion("hurt_when_takeout_from_stockpot", ModEventTrigger.create("hurt_when_takeout_from_stockpot"))
         .save(saver, AdvancementTools.modLoc("burn_hand_soup"), existingFileHelper);
      AdvancementHolder apprenticeChef = AdvancementTools.makeGoal((ItemLike)ModItems.BORSCHT.get(), "apprentice_chef")
         .parent(makeSoup)
         .addCriterion("has_borscht", TriggerInstance.hasItems(new ItemLike[]{(ItemLike)ModItems.BORSCHT.get()}))
         .save(saver, AdvancementTools.modLoc("apprentice_chef"), existingFileHelper);
      AdvancementHolder nitrogen100 = AdvancementTools.makeGoal(Items.FEATHER, "nitrogen_100")
         .parent(root)
         .addCriterion(
            "climb_100",
            AdvancementTools.flatulenceFlyHeight(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                  .located(net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atMost(320.0))),
               DistancePredicate.vertical(Doubles.atLeast(100.0)),
               net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atLeast(-60.0)).build()
            )
         )
         .save(saver, AdvancementTools.modLoc("nitrogen_100"), existingFileHelper);
      AdvancementHolder nitrogen300 = AdvancementTools.makeGoal(Items.FEATHER, "nitrogen_300")
         .parent(nitrogen100)
         .addCriterion(
            "climb_300",
            AdvancementTools.flatulenceFlyHeight(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                  .located(net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atMost(500.0))),
               DistancePredicate.vertical(Doubles.atLeast(300.0)),
               net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atLeast(-60.0)).build()
            )
         )
         .save(saver, AdvancementTools.modLoc("nitrogen_300"), existingFileHelper);
      AdvancementHolder nitrogen1000 = AdvancementTools.makeGoal(Items.FEATHER, "nitrogen_1000")
         .parent(nitrogen300)
         .addCriterion(
            "climb_1000",
            AdvancementTools.flatulenceFlyHeight(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                  .located(net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atMost(1200.0))),
               DistancePredicate.vertical(Doubles.atLeast(1000.0)),
               net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atLeast(-60.0)).build()
            )
         )
         .save(saver, AdvancementTools.modLoc("nitrogen_1000"), existingFileHelper);
      AdvancementHolder nitrogen3000 = AdvancementTools.makeChallenge(Items.FEATHER, "nitrogen_3000")
         .parent(nitrogen1000)
         .addCriterion(
            "climb_3000",
            AdvancementTools.flatulenceFlyHeight(
               net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                  .located(net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atMost(3200.0))),
               DistancePredicate.vertical(Doubles.atLeast(3000.0)),
               net.minecraft.advancements.criterion.LocationPredicate.Builder.atYLocation(Doubles.atLeast(-60.0)).build()
            )
         )
         .save(saver, AdvancementTools.modLoc("nitrogen_3000"), existingFileHelper);
      AdvancementHolder scarecrow = AdvancementTools.makeTask((ItemLike)ModItems.SCARECROW.get(), "scarecrow")
         .parent(farmerSet)
         .addCriterion("place_scarecrow", ModEventTrigger.create("place_scarecrow"))
         .save(saver, AdvancementTools.modLoc("scarecrow"), existingFileHelper);
      AdvancementHolder scarecrowHead = AdvancementTools.makeTask(Items.SKELETON_SKULL, "scarecrow_head")
         .parent(scarecrow)
         .addCriterion("place_head_on_scarecrow", ModEventTrigger.create("place_head_on_scarecrow"))
         .save(saver, AdvancementTools.modLoc("scarecrow_head"), existingFileHelper);
      AdvancementHolder lunchBag = AdvancementTools.makeGoal((ItemLike)ModItems.TRANSMUTATION_LUNCH_BAG.get(), "transmutation_lunch_bag")
         .parent(root)
         .addCriterion("use_transmutation_lunch_bag", ModEventTrigger.create("use_transmutation_lunch_bag"))
         .save(saver, AdvancementTools.modLoc("transmutation_lunch_bag"), existingFileHelper);
      AdvancementHolder millstone = AdvancementTools.makeTask((ItemLike)ModItems.MILLSTONE.get(), "millstone")
         .parent(root)
         .addCriterion("drive_the_millstone", ModEventTrigger.create("drive_the_millstone"))
         .save(saver, AdvancementTools.modLoc("millstone"), existingFileHelper);
      AdvancementHolder dough = AdvancementTools.makeTask((ItemLike)ModItems.RAW_DOUGH.get(), "dough")
         .parent(millstone)
         .addCriterion("pull_the_dough", ModEventTrigger.create("pull_the_dough"))
         .save(saver, AdvancementTools.modLoc("dough"), existingFileHelper);
      AdvancementHolder steamer = AdvancementTools.makeTask((ItemLike)ModItems.STEAMER.get(), "steamer")
         .parent(dough)
         .addCriterion("use_steamer", ModEventTrigger.create("use_steamer"))
         .save(saver, AdvancementTools.modLoc("steamer"), existingFileHelper);
      AdvancementHolder baozi = AdvancementTools.makeGoal((ItemLike)ModItems.BAOZI.get(), "baozi")
         .parent(steamer)
         .addCriterion("meat_buns_beat_dogs", ModEventTrigger.create("meat_buns_beat_dogs"))
         .save(saver, AdvancementTools.modLoc("baozi"), existingFileHelper);
   }
}
