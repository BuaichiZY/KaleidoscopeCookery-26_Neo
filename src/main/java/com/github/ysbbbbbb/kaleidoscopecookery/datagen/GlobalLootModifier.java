package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

public class GlobalLootModifier extends GlobalLootModifierProvider {
   public GlobalLootModifier(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries, "kaleidoscope_cookery");
   }

   public void start() {
      this.addEntityLootModifier("pig", EntityType.PIG);
      this.addEntityLootModifier("zombified_piglin", EntityType.ZOMBIFIED_PIGLIN);
      this.addEntityLootModifier("piglin", EntityType.PIGLIN);
      this.addEntityLootModifier("piglin_brute", EntityType.PIGLIN_BRUTE);
      this.addEntityLootModifier("hoglin", EntityType.HOGLIN);
      this.addEntityLootModifier("zoglin", EntityType.ZOGLIN);
      this.addEntityLootModifier("donkey", EntityType.DONKEY);
      this.addBlockLootModifier("straw_hat_seed_drop", Blocks.SHORT_GRASS);
   }

   private void addEntityLootModifier(String name, EntityType<?> type) {
      LootItemCondition condition = LootTableIdCondition.builder(type.getDefaultLootTable().location()).build();
      LootItemCondition[] conditions = new LootItemCondition[]{condition};
      this.add(name, new AddTableLootModifier(conditions, this.modLoc(name)), new ICondition[0]);
   }

   private void addBlockLootModifier(String name, Block block) {
      LootItemCondition condition = LootTableIdCondition.builder(block.getLootTable().location()).build();
      LootItemCondition[] conditions = new LootItemCondition[]{condition};
      this.add(name, new AddTableLootModifier(conditions, this.modLoc(name)), new ICondition[0]);
   }

   public ResourceKey<LootTable> modLoc(String name) {
      return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }
}
