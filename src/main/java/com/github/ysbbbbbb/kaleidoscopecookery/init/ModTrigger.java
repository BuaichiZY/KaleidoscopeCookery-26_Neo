package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.advancements.triggers.DistanceTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTrigger {
   public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, "kaleidoscope_cookery");
   public static DeferredHolder<CriterionTrigger<?>, ModEventTrigger> EVENT = TRIGGERS.register("mod_event", ModEventTrigger::new);
   public static DeferredHolder<CriterionTrigger<?>, DistanceTrigger> FLATULENCE_FLY_HEIGHT = TRIGGERS.register("flatulence_fly_height", DistanceTrigger::new);
}
