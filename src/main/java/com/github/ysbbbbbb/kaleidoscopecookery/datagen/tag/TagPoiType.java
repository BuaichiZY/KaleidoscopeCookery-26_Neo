package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModPoi;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagPoiType extends PoiTypeTagsProvider {
   public TagPoiType(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
      super(output, provider, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(ModPoi.STOVE.getKey());
   }
}
