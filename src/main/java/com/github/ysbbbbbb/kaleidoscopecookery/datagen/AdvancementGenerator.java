package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement.BaseAdvancement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AdvancementGenerator extends AdvancementProvider {
   public AdvancementGenerator(PackOutput packOutput, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
      super(packOutput, provider, helper, List.of(new AdvancementGenerator.ModAdvancement()));
   }

   private static final class ModAdvancement implements net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator {
      public void generate(Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
         BaseAdvancement.generate(saver, existingFileHelper);
      }
   }
}
