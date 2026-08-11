package com.github.ysbbbbbb.kaleidoscopecookery.compat.emi.category;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

public class EmiTeapotRecipe extends BasicEmiRecipe {
   public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
      Identifier.parse(ModRecipes.TEAPOT_RECIPE.toString()), EmiIngredient.of(Ingredient.of(new ItemLike[]{(ItemLike)ModItems.TEAPOT.get()}))
   );
   private static final Identifier BG = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "textures/gui/jei/teapot.png");
   public static final int WIDTH = 176;
   public static final int HEIGHT = 78;
   private final Item fluidBucket;
   private final int brewTime;

   public EmiTeapotRecipe(Identifier id, List<EmiIngredient> inputs, List<EmiStack> outputs, Item fluidBucket, int brewTime) {
      super(CATEGORY, id, 176, 78);
      this.inputs = inputs;
      this.outputs = outputs;
      this.fluidBucket = fluidBucket;
      this.brewTime = brewTime;
   }

   public static void register(EmiRegistry registry) {
      registry.addCategory(CATEGORY);
      registry.addWorkstation(CATEGORY, EmiStack.of((ItemLike)ModItems.TEAPOT.get()));
      registry.getRecipeManager()
         .getAllRecipesFor(ModRecipes.TEAPOT_RECIPE)
         .forEach(
            r -> {
               TeapotRecipe value = (TeapotRecipe)r.value();
               Fluid fluid = (Fluid)BuiltInRegistries.FLUID.getValue(value.teaFluid());
               Item bucket = fluid.getBucket();
               List<EmiIngredient> inputs = List.of(
                  EmiIngredient.of(
                     Arrays.stream(value.ingredient().getItems()).map(stack -> EmiStack.of(stack.copyWithCount(value.ingredientCount()))).toList()
                  )
               );
               List<EmiStack> outputs = List.of(EmiStack.of(value.result().copyWithCount(12)));
               registry.addRecipe(new EmiTeapotRecipe(r.id(), inputs, outputs, bucket, value.time()));
            }
         );
   }

   public void addWidgets(WidgetHolder widgets) {
      widgets.addTexture(BG, 1, 1, 176, 78, 0, 0);
      widgets.addSlot(EmiStack.of(this.fluidBucket), 65, 3);
      widgets.addSlot((EmiIngredient)this.inputs.getFirst(), 83, 3);
      widgets.addSlot((EmiIngredient)this.outputs.getFirst(), 128, 30).drawBack(false).recipeContext(this);
      Component brewTimeText = Component.translatable("jei.kaleidoscope_cookery.teapot.time", new Object[]{this.brewTime / 20});
      int x = 88 - Minecraft.getInstance().font.width(brewTimeText) / 2;
      widgets.addText(brewTimeText, x, 70, 5592405, false);
   }
}
