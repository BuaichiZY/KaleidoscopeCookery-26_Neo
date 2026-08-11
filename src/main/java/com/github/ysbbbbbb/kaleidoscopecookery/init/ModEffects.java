package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.effect.BaseEffect;
import com.github.ysbbbbbb.kaleidoscopecookery.effect.FlatulenceEffect;
import com.github.ysbbbbbb.kaleidoscopecookery.effect.SulfurEffect;
import com.github.ysbbbbbb.kaleidoscopecookery.effect.VigorEffect;
import com.github.ysbbbbbb.kaleidoscopecookery.effect.WarmthEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "kaleidoscope_cookery");
   public static final DeferredHolder<MobEffect, MobEffect> FLATULENCE = EFFECTS.register("flatulence", () -> new FlatulenceEffect(16762566));
   public static final DeferredHolder<MobEffect, MobEffect> TUNDRA_STRIDER = EFFECTS.register("tundra_strider", () -> new BaseEffect(10615036));
   public static final DeferredHolder<MobEffect, MobEffect> WARMTH = EFFECTS.register("warmth", () -> new WarmthEffect(16736014));
   public static final DeferredHolder<MobEffect, MobEffect> SATIATED_SHIELD = EFFECTS.register("satiated_shield", () -> new BaseEffect(16716563));
   public static final DeferredHolder<MobEffect, MobEffect> VIGOR = EFFECTS.register("vigor", () -> new VigorEffect(8700706));
   public static final DeferredHolder<MobEffect, MobEffect> SULFUR = EFFECTS.register("sulfur", () -> new SulfurEffect(15251294));
   public static final DeferredHolder<MobEffect, MobEffect> MUSTARD = EFFECTS.register("mustard", () -> new BaseEffect(5926153));
   public static final DeferredHolder<MobEffect, MobEffect> PRESERVATION = EFFECTS.register("preservation", () -> new BaseEffect(11454009));
   public static final DeferredHolder<MobEffect, MobEffect> HINDER = EFFECTS.register("hinder", () -> new BaseEffect(10387034));
   public static final DeferredHolder<MobEffect, MobEffect> PROJECTILE_DODGE = EFFECTS.register("projectile_dodge", () -> new BaseEffect(9316343));
   public static final DeferredHolder<MobEffect, MobEffect> INSTANT_SMELTING = EFFECTS.register("instant_smelting", () -> new BaseEffect(15760412));
   public static final DeferredHolder<MobEffect, MobEffect> VITALITY = EFFECTS.register("vitality", () -> new BaseEffect(6987342));
}
