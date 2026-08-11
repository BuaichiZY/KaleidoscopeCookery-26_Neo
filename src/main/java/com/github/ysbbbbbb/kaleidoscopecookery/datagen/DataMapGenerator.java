package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.common.data.DataMapProvider.Builder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;

public class DataMapGenerator extends DataMapProvider {
   private final Builder<Compostable, Item> compostableBuilder = this.builder(NeoForgeDataMaps.COMPOSTABLES);
   private final Builder<RaidHeroGift, VillagerProfession> heroGiftBuilder = this.builder(NeoForgeDataMaps.RAID_HERO_GIFTS);

   public DataMapGenerator(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
      super(packOutput, lookupProvider);
   }

   protected void gather(Provider provider) {
      this.addCompostable(ModItems.TOMATO_SEED, 0.3F);
      this.addCompostable(ModItems.CHILI_SEED, 0.3F);
      this.addCompostable(ModItems.LETTUCE_SEED, 0.3F);
      this.addCompostable(ModItems.WILD_RICE_SEED, 0.3F);
      this.addCompostable(ModItems.RICE_SEED, 0.3F);
      this.addCompostable(ModItems.TOMATO, 0.65F);
      this.addCompostable(ModItems.RED_CHILI, 0.65F);
      this.addCompostable(ModItems.GREEN_CHILI, 0.65F);
      this.addCompostable(ModItems.LETTUCE, 0.65F);
      this.addCompostable(ModItems.RICE_PANICLE, 0.65F);
      this.addCompostable(ModItems.CATERPILLAR, 1.0F);
      RaidHeroGift gift = new RaidHeroGift(ResourceKey.create(Registries.LOOT_TABLE, GiftLootTables.CHEF_GIFT));
      this.heroGiftBuilder.add(ModVillager.CHEF, gift, false, new ICondition[0]);
   }

   private void addCompostable(DeferredItem<Item> item, float chance) {
      this.compostableBuilder.add(item, new Compostable(chance, true), false, new ICondition[0]);
   }
}
