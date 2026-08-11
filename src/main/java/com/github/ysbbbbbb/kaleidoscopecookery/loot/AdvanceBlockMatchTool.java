package com.github.ysbbbbbb.kaleidoscopecookery.loot;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModLootModifier;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder;

public class AdvanceBlockMatchTool implements LootItemCondition {
   public static final Identifier ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "advance_block_match_tool");
   public static final MapCodec<AdvanceBlockMatchTool> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            EquipmentSlot.CODEC.fieldOf("slot").forGetter(tool -> tool.slot), ItemPredicate.CODEC.fieldOf("predicate").forGetter(tool -> tool.predicate)
         )
         .apply(instance, AdvanceBlockMatchTool::new)
   );
   private final EquipmentSlot slot;
   private final ItemPredicate predicate;

   public AdvanceBlockMatchTool(EquipmentSlot slot, ItemPredicate predicate) {
      this.slot = slot;
      this.predicate = predicate;
   }

   public LootItemConditionType getType() {
      return ModLootModifier.ADVANCE_BLOCK_MATCH_TOOL;
   }

   public Set<LootContextParam<?>> getReferencedContextParams() {
      return ImmutableSet.of(LootContextParams.THIS_ENTITY);
   }

   public boolean test(LootContext context) {
      if (context.hasParam(LootContextParams.THIS_ENTITY)) {
         Entity entity = (Entity)context.getParam(LootContextParams.THIS_ENTITY);
         if (entity instanceof LivingEntity livingEntity) {
            ItemStack stack = livingEntity.getItemBySlot(this.slot);
            return this.predicate.test(stack);
         }
      }

      return false;
   }

   public static Builder toolMatches(EquipmentSlot slot, ItemPredicate builder) {
      return () -> new AdvanceBlockMatchTool(slot, builder);
   }
}
