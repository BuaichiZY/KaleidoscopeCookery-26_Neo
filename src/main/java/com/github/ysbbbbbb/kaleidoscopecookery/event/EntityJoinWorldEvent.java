package com.github.ysbbbbbb.kaleidoscopecookery.event;

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
   @SubscribeEvent
   public static void onCatJoinWorld(EntityJoinLevelEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof Cat cat) {
         cat.goalSelector.addGoal(5, new CatLieOnBlockGoal(cat, 1.1, 8));
      } else if (entity instanceof Creeper creeper) {
         creeper.goalSelector.addGoal(
            3, new AvoidEntityGoal<LivingEntity>(creeper, LivingEntity.class, 6.0F, 1.0, 1.2, e -> e.hasEffect(ModEffects.MUSTARD))
         );
      }
   }
}
