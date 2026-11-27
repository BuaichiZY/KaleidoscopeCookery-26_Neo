package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.lootable.GiftLootTables;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.common.data.DataMapProvider.Builder;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;

public class DataMapGenerator extends DataMapProvider {
   private final Builder<RaidHeroGift, VillagerProfession> heroGiftBuilder = this.builder(NeoForgeDataMaps.RAID_HERO_GIFTS);

   public DataMapGenerator(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
      super(packOutput, lookupProvider);
   }

   protected void gather(Provider provider) {
      RaidHeroGift gift = new RaidHeroGift(ResourceKey.create(Registries.LOOT_TABLE, GiftLootTables.CHEF_GIFT));
      this.heroGiftBuilder.add(ModVillager.CHEF, gift, false, new ICondition[0]);
   }
}
