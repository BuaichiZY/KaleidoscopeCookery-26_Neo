package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei;

import java.util.Arrays;
import java.util.List;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ReiUtil {
   public static me.shedaniel.rei.api.client.gui.widgets.Widget translate(me.shedaniel.rei.api.client.gui.widgets.Widget widget, int x, int y, double ignoredZ) {
      return me.shedaniel.rei.api.client.gui.widgets.Widgets.withTranslate(widget, unused -> new org.joml.Matrix3x2f().translation(x, y));
   }

   public static EntryIngredient ofIngredient(Ingredient ingredient) {
      if (com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.isEmptyIngredient(ingredient)) {
         return EntryIngredient.empty();
      }
      return EntryIngredient.of(ingredient.items().map(holder -> new ItemStack(holder.value())).map(EntryStacks::of).toList());
   }

   public static EntryIngredient ofItem(Item item) {
      return EntryIngredient.of(EntryStacks.of(item));
   }

   public static EntryIngredient ofItemStack(ItemStack itemStack) {
      return EntryIngredient.of(EntryStacks.of(itemStack));
   }

   public static List<EntryIngredient> ofItems(Item... items) {
      return Arrays.stream(items).map(ReiUtil::ofItem).toList();
   }

   public static List<EntryIngredient> ofItemStacks(ItemStack... items) {
      return Arrays.stream(items).map(ReiUtil::ofItemStack).toList();
   }

   public static List<EntryIngredient> ofItems(List<Item> items) {
      return items.stream().map(ReiUtil::ofItem).toList();
   }

   public static List<EntryIngredient> ofIngredients(Ingredient... ingredients) {
      return Arrays.stream(ingredients).map(ReiUtil::ofIngredient).toList();
   }

   public static List<EntryIngredient> ofIngredients(List<Ingredient> ingredients) {
      return ingredients.stream().map(ReiUtil::ofIngredient).toList();
   }
}
