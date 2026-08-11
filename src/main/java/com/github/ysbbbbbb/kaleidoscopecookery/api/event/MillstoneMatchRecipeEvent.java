package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public abstract class MillstoneMatchRecipeEvent extends Event {
   private final Level level;
   private final MillstoneBlockEntity millstone;
   private final SimpleInput input;
   @Nullable
   private RecipeHolder<MillstoneRecipe> output = null;

   public MillstoneMatchRecipeEvent(Level level, MillstoneBlockEntity millstone, SimpleInput input) {
      this.level = level;
      this.millstone = millstone;
      this.input = input;
   }

   public Level getLevel() {
      return this.level;
   }

   public MillstoneBlockEntity getMillstone() {
      return this.millstone;
   }

   public SimpleInput getInput() {
      return this.input;
   }

   @Nullable
   public RecipeHolder<MillstoneRecipe> getOutput() {
      return this.output;
   }

   public void setOutput(@Nullable RecipeHolder<MillstoneRecipe> output) {
      this.output = output;
   }

   public static class Post extends MillstoneMatchRecipeEvent {
      private final RecipeHolder<MillstoneRecipe> rawOutput;

      public Post(Level level, MillstoneBlockEntity millstone, SimpleInput container, RecipeHolder<MillstoneRecipe> rawOutput) {
         super(level, millstone, container);
         this.rawOutput = rawOutput;
      }

      public RecipeHolder<MillstoneRecipe> getRawOutput() {
         return this.rawOutput;
      }
   }

   public static class Pre extends MillstoneMatchRecipeEvent {
      public Pre(Level level, MillstoneBlockEntity millstone, SimpleInput container) {
         super(level, millstone, container);
      }
   }
}
