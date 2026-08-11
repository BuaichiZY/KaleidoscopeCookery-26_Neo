package com.github.ysbbbbbb.kaleidoscopecookery.api.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public abstract class StockpotMatchRecipeEvent extends Event {
   private final Level level;
   private final StockpotBlockEntity stockpot;
   private final StockpotInput input;
   @Nullable
   private RecipeHolder<StockpotRecipe> output = null;

   public StockpotMatchRecipeEvent(Level level, StockpotBlockEntity stockpot, StockpotInput input) {
      this.level = level;
      this.stockpot = stockpot;
      this.input = input;
   }

   public Level getLevel() {
      return this.level;
   }

   public StockpotBlockEntity getStockpot() {
      return this.stockpot;
   }

   public StockpotInput getInput() {
      return this.input;
   }

   @Nullable
   public RecipeHolder<StockpotRecipe> getOutput() {
      return this.output;
   }

   public void setOutput(@Nullable RecipeHolder<StockpotRecipe> output) {
      this.output = output;
   }

   public static class Post extends StockpotMatchRecipeEvent {
      private final Identifier rawOutput;

      public Post(Level level, StockpotBlockEntity stockpot, StockpotInput container, Identifier rawOutput) {
         super(level, stockpot, container);
         this.rawOutput = rawOutput;
      }

      public Identifier getRawOutput() {
         return this.rawOutput;
      }
   }

   public static class Pre extends StockpotMatchRecipeEvent {
      public Pre(Level level, StockpotBlockEntity stockpot, StockpotInput container) {
         super(level, stockpot, container);
      }
   }
}
