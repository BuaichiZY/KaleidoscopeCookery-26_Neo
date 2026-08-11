package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagEntityType extends EntityTypeTagsProvider {
   public TagEntityType(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
      super(output, provider, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(TagMod.PIG_OIL_SOURCE)
         .add(new EntityType[]{EntityType.PIG, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.HOGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN});
      this.tag(TagMod.MILLSTONE_BINDABLE)
         .add(
            new EntityType[]{
               EntityType.MULE,
               EntityType.DONKEY,
               EntityType.HORSE,
               EntityType.ZOMBIE_HORSE,
               EntityType.SKELETON_HORSE,
               EntityType.LLAMA,
               EntityType.TRADER_LLAMA,
               EntityType.COW,
               EntityType.MOOSHROOM,
               EntityType.SHEEP,
               EntityType.GOAT,
               EntityType.VILLAGER
            }
         );
      this.tag(TagMod.RICE_GROWTH_BOOSTER)
         .add(new EntityType[]{EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.TADPOLE, EntityType.AXOLOTL});
   }
}
