package com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.AdvanceEntityMatchTool;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityLootTables extends EntityLootSubProvider {
   public final Set<EntityType<?>> knownEntities = Sets.newHashSet();

   public EntityLootTables(Provider registries) {
      super(FeatureFlags.REGISTRY.allFlags(), registries);
   }

   public void generate(BiConsumer<ResourceKey<LootTable>, Builder> output) {
      super.generate(output);
      ItemPredicate hasKnife = net.minecraft.advancements.criterion.ItemPredicate.Builder.item().of(TagMod.KITCHEN_KNIFE).build();
      net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder toolMatches = AdvanceEntityMatchTool.toolMatches(
         EquipmentSlot.MAINHAND, hasKnife
      );
      net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<? extends net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?>> count = SetItemCountFunction.setCount(
         UniformGenerator.between(1.0F, 2.0F)
      );
      net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.Builder looting = EnchantedCountIncreaseFunction.lootingMultiplier(
         this.registries, UniformGenerator.between(0.0F, 1.0F)
      );
      net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> oil = LootItem.lootTableItem((ItemLike)ModItems.OIL.get())
         .apply(count)
         .apply(looting);
      Builder lessOil = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(oil).when(toolMatches));
      Builder moreOil = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2.0F)).add(oil).when(toolMatches));
      output.accept(this.modLoc("pig"), lessOil);
      output.accept(this.modLoc("zombified_piglin"), lessOil);
      output.accept(this.modLoc("piglin"), moreOil);
      output.accept(this.modLoc("piglin_brute"), moreOil);
      output.accept(this.modLoc("hoglin"), moreOil);
      output.accept(this.modLoc("zoglin"), moreOil);
   }

   public void generate() {
   }

   protected boolean canHaveLootTable(EntityType<?> type) {
      return true;
   }

   protected Stream<EntityType<?>> getKnownEntityTypes() {
      return this.knownEntities.stream();
   }

   protected void add(EntityType<?> type, Builder builder) {
      this.add(type, type.getDefaultLootTable(), builder);
   }

   protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, Builder builder) {
      super.add(type, lootTable, builder);
      this.knownEntities.add(type);
   }

   public ResourceKey<LootTable> modLoc(String name) {
      return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name));
   }
}
