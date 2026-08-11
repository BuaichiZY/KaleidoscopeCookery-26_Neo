package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PathPackResources.PathResourcesSupplier;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.Pack.Metadata;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.locating.IModFile;

public class LegacyPackRepositorySource implements RepositorySource {
   private static final String LEGACY_PACK_DIR_NAME = "legacy_pack";
   private static final String PACK_NAME = "kaleidoscope_cookery_legacy_resources_pack";
   private final Pack legacyPack;

   public LegacyPackRepositorySource() {
      ResourcesSupplier supplier = this.getLegacyPack();
      MutableComponent title = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.title");
      MutableComponent desc = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.desc");
      PackLocationInfo info = new PackLocationInfo("kaleidoscope_cookery_legacy_resources_pack", title, PackSource.BUILT_IN, Optional.empty());
      Metadata metadata = new Metadata(desc, PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), Collections.emptyList(), false);
      PackSelectionConfig config = new PackSelectionConfig(false, Position.TOP, false);
      this.legacyPack = new Pack(info, supplier, metadata, config);
   }

   private PathResourcesSupplier getLegacyPack() {
      IModFile file = ModList.get().getModFileById("kaleidoscope_cookery").getFile();
      return new PathResourcesSupplier(file.getSecureJar().getRootPath().resolve("legacy_pack"));
   }

   public void loadPacks(Consumer<Pack> consumer) {
      consumer.accept(this.legacyPack);
   }
}
