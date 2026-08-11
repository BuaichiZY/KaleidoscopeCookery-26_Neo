package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrungMushroomsBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceBlockMatchTool;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BlockLootTables extends BlockLootSubProvider {
   public final Set<Block> knownBlocks = new HashSet<>();
   public final RegistryLookup<Enchantment> enchantment = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

   public BlockLootTables(Provider registries) {
      super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
   }

   public void generate() {
      this.dropSelf((Block)ModBlocks.STOVE.get());
      this.dropSelf((Block)ModBlocks.POT.get());
      this.dropSelf((Block)ModBlocks.CHOPPING_BOARD.get());
      this.dropSelf((Block)ModBlocks.OIL_POT.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_OAK.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_SPRUCE.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_ACACIA.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_BAMBOO.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_BIRCH.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_CHERRY.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_CRIMSON.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_DARK_OAK.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_JUNGLE.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_MANGROVE.get());
      this.dropSelf((Block)ModBlocks.COOK_STOOL_WARPED.get());
      this.dropSelf((Block)ModBlocks.CHAIR_OAK.get());
      this.dropSelf((Block)ModBlocks.CHAIR_SPRUCE.get());
      this.dropSelf((Block)ModBlocks.CHAIR_ACACIA.get());
      this.dropSelf((Block)ModBlocks.CHAIR_BAMBOO.get());
      this.dropSelf((Block)ModBlocks.CHAIR_BIRCH.get());
      this.dropSelf((Block)ModBlocks.CHAIR_CHERRY.get());
      this.dropSelf((Block)ModBlocks.CHAIR_CRIMSON.get());
      this.dropSelf((Block)ModBlocks.CHAIR_DARK_OAK.get());
      this.dropSelf((Block)ModBlocks.CHAIR_JUNGLE.get());
      this.dropSelf((Block)ModBlocks.CHAIR_MANGROVE.get());
      this.dropSelf((Block)ModBlocks.CHAIR_WARPED.get());
      this.dropSelf((Block)ModBlocks.TABLE_OAK.get());
      this.dropSelf((Block)ModBlocks.TABLE_SPRUCE.get());
      this.dropSelf((Block)ModBlocks.TABLE_ACACIA.get());
      this.dropSelf((Block)ModBlocks.TABLE_BAMBOO.get());
      this.dropSelf((Block)ModBlocks.TABLE_BIRCH.get());
      this.dropSelf((Block)ModBlocks.TABLE_CHERRY.get());
      this.dropSelf((Block)ModBlocks.TABLE_CRIMSON.get());
      this.dropSelf((Block)ModBlocks.TABLE_DARK_OAK.get());
      this.dropSelf((Block)ModBlocks.TABLE_JUNGLE.get());
      this.dropSelf((Block)ModBlocks.TABLE_MANGROVE.get());
      this.dropSelf((Block)ModBlocks.TABLE_WARPED.get());
      this.dropSelf((Block)ModBlocks.STOCKPOT.get());
      this.dropSelf((Block)ModBlocks.FRUIT_BASKET.get());
      this.dropSelf((Block)ModBlocks.KITCHENWARE_RACKS.get());
      this.dropSelf((Block)ModBlocks.STRAW_BLOCK.get());
      this.dropSelf((Block)ModBlocks.SHAWARMA_SPIT.get());
      this.dropSelf((Block)ModBlocks.OIL_BLOCK.get());
      this.dropSelf((Block)ModBlocks.ENAMEL_BASIN.get());
      this.dropSelf((Block)ModBlocks.TRASH_CAN.get());
      this.add(
         (Block)ModBlocks.TOMATO_CROP.get(),
         this.createCropDrops(
            (Block)ModBlocks.TOMATO_CROP.get(),
            (Item)ModItems.TOMATO.get(),
            (Item)ModItems.TOMATO_SEED.get(),
            this.createCropBuilder((Block)ModBlocks.TOMATO_CROP.get())
         )
      );
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder chiliBuilder = this.createCropBuilder((Block)ModBlocks.CHILI_CROP.get());
      Builder<?> greenChili = (Builder<?>)LootItem.lootTableItem((ItemLike)ModItems.GREEN_CHILI.get()).when(LootItemRandomChanceCondition.randomChance(0.2F));
      this.add(
         (Block)ModBlocks.CHILI_CROP.get(),
         this.createCropDrops((Block)ModBlocks.CHILI_CROP.get(), (Item)ModItems.RED_CHILI.get(), (Item)ModItems.CHILI_SEED.get(), chiliBuilder)
            .withPool(LootPool.lootPool().when(chiliBuilder).add(greenChili))
      );
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder lettuceBuilder = this.createCropBuilder((Block)ModBlocks.LETTUCE_CROP.get());
      Builder<?> caterpillar = (Builder<?>)LootItem.lootTableItem((ItemLike)ModItems.CATERPILLAR.get()).when(LootItemRandomChanceCondition.randomChance(0.1F));
      this.add(
         (Block)ModBlocks.LETTUCE_CROP.get(),
         this.createCropDrops((Block)ModBlocks.LETTUCE_CROP.get(), (Item)ModItems.LETTUCE.get(), (Item)ModItems.LETTUCE_SEED.get(), lettuceBuilder)
            .withPool(LootPool.lootPool().when(lettuceBuilder).add(caterpillar))
      );
      Item riceSeed = (Item)ModItems.WILD_RICE_SEED.get();
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder riceCropBuilder = this.createRiceCropBuilder();
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<? extends net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?>> countFunction = SetItemCountFunction.setCount(
         UniformGenerator.between(2.0F, 4.0F)
      );
      net.minecraft.world.level.storage.loot.LootPool.Builder ricePanicle = LootPool.lootPool()
         .add(
            ((Builder)LootItem.lootTableItem((ItemLike)ModItems.RICE_PANICLE.get()).when(riceCropBuilder))
               .apply(countFunction)
               .otherwise(LootItem.lootTableItem(riceSeed))
         );
      net.minecraft.world.level.storage.loot.LootPool.Builder extraRiceSeeds = LootPool.lootPool()
         .add(LootItem.lootTableItem(riceSeed))
         .when(riceCropBuilder)
         .apply(ApplyBonusCount.addBonusBinomialDistributionCount(this.enchantment.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3));
      this.add(
         (Block)ModBlocks.RICE_CROP.get(),
         (net.minecraft.world.level.storage.loot.LootTable.Builder)this.applyExplosionDecay(
            (ItemLike)ModBlocks.RICE_CROP.get(), LootTable.lootTable().withPool(ricePanicle).withPool(extraRiceSeeds)
         )
      );
      FoodBiteRegistry.FOOD_DATA_MAP.forEach(this::dropFoodBite);
      this.dropFoodBite((Block)ModBlocks.COLD_CUT_HAM_SLICES.get(), (Item)ModItems.COLD_CUT_HAM_SLICES.get(), Items.BOWL);
      PlateRegistry.PLATE_DATA_MAP.forEach(this::dropPlate);
      this.add((Block)ModBlocks.CHILI_RISTRA.get(), this.createChiliRistraLootTable());
      this.add((Block)ModBlocks.STRUNG_MUSHROOMS.get(), this.createStrungMushroomsLootTable());
   }

   private net.minecraft.world.level.storage.loot.LootTable.Builder createChiliRistraLootTable() {
      net.minecraft.world.level.storage.loot.LootPool.Builder builder = LootPool.lootPool();
      net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder isSheared = net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
         .hasProperty(ChiliRistraBlock.SHEARED, true);
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
            (Block)ModBlocks.CHILI_RISTRA.get()
         )
         .setProperties(isSheared);
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> normalDrop = SetItemCountFunction.setCount(
         ConstantValue.exactly(6.0F)
      );
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> shearedDrop = SetItemCountFunction.setCount(
         ConstantValue.exactly(3.0F)
      );
      Builder<?> normalLoot = LootItem.lootTableItem((ItemLike)ModItems.RED_CHILI.get()).apply(normalDrop);
      Builder<?> shearedLoot = LootItem.lootTableItem((ItemLike)ModItems.RED_CHILI.get()).apply(shearedDrop);
      builder.add(((Builder)shearedLoot.when(condition)).otherwise(normalLoot));
      return LootTable.lootTable().withPool(builder.when(ExplosionCondition.survivesExplosion()));
   }

   private net.minecraft.world.level.storage.loot.LootTable.Builder createStrungMushroomsLootTable() {
      net.minecraft.world.level.storage.loot.LootPool.Builder builder = LootPool.lootPool();
      net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder isSheared = net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
         .hasProperty(StrungMushroomsBlock.SHEARED, true);
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
            (Block)ModBlocks.STRUNG_MUSHROOMS.get()
         )
         .setProperties(isSheared);
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> normalDrop = SetItemCountFunction.setCount(
         ConstantValue.exactly(6.0F)
      );
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> shearedDrop = SetItemCountFunction.setCount(
         ConstantValue.exactly(3.0F)
      );
      Builder<?> normalLoot = LootItem.lootTableItem(Items.BROWN_MUSHROOM).apply(normalDrop);
      Builder<?> shearedLoot = LootItem.lootTableItem(Items.BROWN_MUSHROOM).apply(shearedDrop);
      builder.add(((Builder)shearedLoot.when(condition)).otherwise(normalLoot));
      return LootTable.lootTable().withPool(builder.when(ExplosionCondition.survivesExplosion()));
   }

   private net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder createCropBuilder(Block cropBlock) {
      net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder property = net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
         .hasProperty(CropBlock.AGE, 7);
      return LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock).setProperties(property);
   }

   private net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder createRiceCropBuilder() {
      net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder property = net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
         .hasProperty(CropBlock.AGE, 7)
         .hasProperty(RiceCropBlock.LOCATION, 0);
      return LootItemBlockStatePropertyCondition.hasBlockStateProperties((Block)ModBlocks.RICE_CROP.get()).setProperties(property);
   }

   public void generate(BiConsumer<ResourceKey<LootTable>, net.minecraft.world.level.storage.loot.LootTable.Builder> output) {
      super.generate(output);
      Builder<? extends Builder<?>> tomato = (Builder<? extends Builder<?>>)this.getSeed((ItemLike)ModItems.TOMATO_SEED.get());
      Builder<? extends Builder<?>> chili = (Builder<? extends Builder<?>>)this.getSeed((ItemLike)ModItems.CHILI_SEED.get());
      Builder<? extends Builder<?>> lettuce = (Builder<? extends Builder<?>>)this.getSeed((ItemLike)ModItems.LETTUCE_SEED.get());
      Builder<? extends Builder<?>> rice = (Builder<? extends Builder<?>>)this.getSeed((ItemLike)ModItems.WILD_RICE_SEED.get());
      Builder<? extends Builder<?>> beetRootSeed = (Builder<? extends Builder<?>>)this.getSeed(Items.BEETROOT_SEEDS, 0.02F);
      Builder<? extends Builder<?>> pumpkinSeed = (Builder<? extends Builder<?>>)this.getSeed(Items.PUMPKIN_SEEDS, 0.02F);
      Builder<? extends Builder<?>> melonSeed = (Builder<? extends Builder<?>>)this.getSeed(Items.MELON_SEEDS, 0.02F);
      net.minecraft.world.level.storage.loot.LootTable.Builder dropSeed = LootTable.lootTable()
         .withPool(
            LootPool.lootPool()
               .setRolls(ConstantValue.exactly(1.0F))
               .add(tomato)
               .add(chili)
               .add(lettuce)
               .add(rice)
               .add(beetRootSeed)
               .add(pumpkinSeed)
               .add(melonSeed)
         );
      ResourceKey<LootTable> id = ResourceKey.create(Registries.LOOT_TABLE, this.modLoc("straw_hat_seed_drop"));
      output.accept(id, dropSeed);
   }

   private Builder<?> getSeed(ItemLike item) {
      return this.getSeed(item, 0.125F);
   }

   private Builder<?> getSeed(ItemLike item, float probability) {
      ItemPredicate hasHat = net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(TagMod.STRAW_HAT).build();
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder hatMatches = AdvanceBlockMatchTool.toolMatches(EquipmentSlot.HEAD, hasHat);
      return ((Builder)((Builder)LootItem.lootTableItem(item).when(LootItemRandomChanceCondition.randomChance(probability))).when(hatMatches))
         .apply(ApplyBonusCount.addUniformBonusCount(this.enchantment.getOrThrow(Enchantments.FORTUNE), 2));
   }

   private void dropFoodBite(Identifier id, FoodBiteRegistry.FoodData data) {
      Block block = (Block)BuiltInRegistries.BLOCK.getValue(id);
      Item food = (Item)BuiltInRegistries.ITEM.getValue(id);
      ItemLike[] lootItems = data.getLootItems().toArray(new ItemLike[0]);
      this.dropFoodBite(block, food, lootItems);
   }

   private void dropFoodBite(Block block, Item food, ItemLike... lootItems) {
      if (block instanceof FoodBiteBlock foodBiteBlock) {
         ConstantValue exactly = ConstantValue.exactly(1.0F);
         net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder notBite = net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
            .hasProperty(foodBiteBlock.getBites(), 0);
         net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.Builder builder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
               foodBiteBlock
            )
            .setProperties(notBite);
         net.minecraft.world.level.storage.loot.LootTable.Builder lootTable = LootTable.lootTable();

         for (int i = 0; i < lootItems.length; i++) {
            ItemLike itemLike = lootItems[i];
            net.minecraft.world.level.storage.loot.LootPool.Builder rolls = LootPool.lootPool().setRolls(exactly).when(ExplosionCondition.survivesExplosion());
            if (i == 0) {
               rolls.add(((Builder)LootItem.lootTableItem(food).when(builder)).otherwise(LootItem.lootTableItem(itemLike)));
            } else {
               rolls.add(((Builder)EmptyLootItem.emptyItem().when(builder)).otherwise(LootItem.lootTableItem(itemLike)));
            }

            lootTable.withPool(rolls);
         }

         this.add(block, lootTable);
      }
   }

   private void dropPlate(Identifier id, PlateRegistry.PlateData data) {
      Block block = (Block)BuiltInRegistries.BLOCK.getValue(id);
      ItemLike[] lootItems = data.getLootItems().toArray(new ItemLike[0]);
      this.dropOthers(block, lootItems);
   }

   private void dropOthers(Block block, ItemLike... lootItems) {
      ConstantValue exactly = ConstantValue.exactly(1.0F);
      net.minecraft.world.level.storage.loot.LootTable.Builder lootTable = LootTable.lootTable();

      for (ItemLike itemLike : lootItems) {
         net.minecraft.world.level.storage.loot.LootPool.Builder rolls = LootPool.lootPool()
            .setRolls(exactly)
            .when(ExplosionCondition.survivesExplosion())
            .add(LootItem.lootTableItem(itemLike));
         lootTable.withPool(rolls);
      }

      this.add(block, lootTable);
   }

   public void add(Block block, net.minecraft.world.level.storage.loot.LootTable.Builder builder) {
      this.knownBlocks.add(block);
      super.add(block, builder);
   }

   public Iterable<Block> getKnownBlocks() {
      return this.knownBlocks;
   }

   public Identifier modLoc(String name) {
      return Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name);
   }
}
