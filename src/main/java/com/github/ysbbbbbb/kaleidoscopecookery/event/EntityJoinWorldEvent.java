package com.github.ysbbbbbb.kaleidoscopecookery.event;

import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ai.CatLieOnBlockGoal;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class EntityJoinWorldEvent {
   private static final Set<String> SKEWER_TARGETS = Set.of("minecraft:bee", "minecraft:fox", "minecraft:chicken", "minecraft:enderman");

   @SubscribeEvent
   public static void registerTemptationRange(EntityAttributeModificationEvent event) {
      for (var type : event.getTypes()) {
         if (SKEWER_TARGETS.contains(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())
            && !event.has(type, Attributes.TEMPT_RANGE)) {
            event.add(type, Attributes.TEMPT_RANGE, 10.0);
         }
      }
   }

   @SubscribeEvent
   public static void onCatJoinWorld(EntityJoinLevelEvent event) {
      if (event.getLevel().isClientSide()) return;
      Entity entity = event.getEntity();
      if (entity instanceof Cat cat) {
         cat.goalSelector.addGoal(5, new CatLieOnBlockGoal(cat, 1.1, 8));
      } else if (entity instanceof Creeper creeper) {
         creeper.goalSelector.addGoal(
            3, new AvoidEntityGoal<LivingEntity>(creeper, LivingEntity.class, 6.0F, 1.0, 1.2, e -> e.hasEffect(ModEffects.MUSTARD))
         );
      } else if (entity instanceof net.minecraft.world.entity.animal.bee.Bee mob) {
         mob.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(
            mob, 1.25, net.minecraft.world.item.crafting.Ingredient.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.CANDIED_APPLE_SKEWER.get()), false));
      } else if (entity instanceof net.minecraft.world.entity.animal.fox.Fox mob) {
         mob.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(
            mob, 1.0, net.minecraft.world.item.crafting.Ingredient.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.CANDIED_SWEET_BERRY_SKEWER.get()), false));
      } else if (entity instanceof net.minecraft.world.entity.animal.chicken.Chicken mob) {
         mob.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(
            mob, 1.0, net.minecraft.world.item.crafting.Ingredient.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.CANDIED_CATERPILLAR_SKEWER.get()), false));
      } else if (entity instanceof net.minecraft.world.entity.monster.Enderman mob) {
         mob.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(
            mob, 1.0, net.minecraft.world.item.crafting.Ingredient.of(com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get()), false));
      }
   }
}
