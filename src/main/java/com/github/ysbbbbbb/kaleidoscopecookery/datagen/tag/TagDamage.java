package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagDamage extends DamageTypeTagsProvider {
   public TagDamage(PackOutput output, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
      super(output, lookupProvider, "kaleidoscope_cookery", fileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(TagMod.SATIATED_SHIELD_WEAKNESS)
         .add(
            new ResourceKey[]{
               DamageTypes.WITHER_SKULL, DamageTypes.WITHER, DamageTypes.SONIC_BOOM, DamageTypes.INDIRECT_MAGIC, DamageTypes.IN_WALL, DamageTypes.FREEZE
            }
         )
         .addTag(DamageTypeTags.IS_EXPLOSION)
         .addTag(DamageTypeTags.IS_LIGHTNING);
   }
}
