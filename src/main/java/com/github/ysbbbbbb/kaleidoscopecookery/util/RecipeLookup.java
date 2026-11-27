package com.github.ysbbbbbb.kaleidoscopecookery.util;
import java.util.List;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
public final class RecipeLookup {
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeHolder<T>> byType(RecipeManager manager, RecipeType<T> type) {
        return manager.getRecipes().stream().filter(holder -> holder.value().getType() == type)
            .map(holder -> (RecipeHolder<T>) holder).toList();
    }
}
