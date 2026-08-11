package com.github.ysbbbbbb.kaleidoscopecookery.datagen.advancement;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import java.util.Optional;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.DistanceTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.DistanceTrigger.TriggerInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;

public class AdvancementTools {
   public static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/advancement/background.png");

   public static Builder makeTask(ItemLike item, String key) {
      MutableComponent title = Component.translatable("advancements.kaleidoscope_cookery.%s.title".formatted(key));
      MutableComponent desc = Component.translatable("advancements.kaleidoscope_cookery.%s.description".formatted(key));
      return Builder.advancement().display(item, title, desc, BG, AdvancementType.TASK, true, true, false);
   }

   public static Builder makeChallenge(ItemLike item, String key) {
      MutableComponent title = Component.translatable("advancements.kaleidoscope_cookery.%s.title".formatted(key));
      MutableComponent desc = Component.translatable("advancements.kaleidoscope_cookery.%s.description".formatted(key));
      return Builder.advancement().display(item, title, desc, BG, AdvancementType.CHALLENGE, true, true, true);
   }

   public static Builder makeGoal(ItemLike item, String key) {
      MutableComponent title = Component.translatable("advancements.kaleidoscope_cookery.%s.title".formatted(key));
      MutableComponent desc = Component.translatable("advancements.kaleidoscope_cookery.%s.description".formatted(key));
      return Builder.advancement().display(item, title, desc, BG, AdvancementType.GOAL, true, true, false);
   }

   public static Criterion<TriggerInstance> flatulenceFlyHeight(
      net.minecraft.advancements.criterion.EntityPredicate.Builder player, DistancePredicate distance, LocationPredicate startPosition
   ) {
      return ((DistanceTrigger)ModTrigger.FLATULENCE_FLY_HEIGHT.get())
         .createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(startPosition), Optional.of(distance)));
   }

   public static Identifier modLoc(String id) {
      return Identifier.fromNamespaceAndPath("kaleidoscope_cookery", id);
   }
}
