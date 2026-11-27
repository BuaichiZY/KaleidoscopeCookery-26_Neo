package com.github.ysbbbbbb.kaleidoscopecookery.init;
import com.github.ysbbbbbb.kaleidoscopecookery.loot.RecipeRandomlyFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class ModLootModifier {
    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, "kaleidoscope_cookery");
    static { FUNCTIONS.register("recipe_randomly", () -> RecipeRandomlyFunction.CODEC); }
}
