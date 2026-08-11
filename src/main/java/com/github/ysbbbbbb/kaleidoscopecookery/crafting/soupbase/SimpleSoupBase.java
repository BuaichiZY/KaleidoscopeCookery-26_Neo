package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

public class SimpleSoupBase implements ISoupBase {
   protected final Identifier name;
   protected final ItemStack displayStack;
   protected final Identifier soupBaseTexture;
   protected final int bubbleColor;
   protected final Predicate<ItemStack> soupBasePredicate;
   protected final Predicate<ItemStack> containerPredicate;
   protected final TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction;
   protected final TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction;

   public SimpleSoupBase(
      Identifier name,
      ItemStack displayStack,
      Identifier soupBaseTexture,
      int bubbleColor,
      Predicate<ItemStack> soupBasePredicate,
      Predicate<ItemStack> containerPredicate,
      TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction,
      TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction
   ) {
      this.name = name;
      this.displayStack = displayStack;
      this.soupBaseTexture = soupBaseTexture;
      this.bubbleColor = bubbleColor;
      this.soupBasePredicate = soupBasePredicate;
      this.containerPredicate = containerPredicate;
      this.returnContainerFunction = returnContainerFunction;
      this.returnSoupBaseFunction = returnSoupBaseFunction;
   }

   @Override
   public Identifier getName() {
      return this.name;
   }

   @Override
   public int getBubbleColor() {
      return this.bubbleColor;
   }

   @Override
   public ItemStack getDisplayStack() {
      return this.displayStack;
   }

   public Identifier getSoupBaseTexture() {
      return this.soupBaseTexture;
   }

   @Override
   public boolean isSoupBase(ItemStack stack) {
      return this.soupBasePredicate.test(stack);
   }

   @Override
   public ItemStack getReturnContainer(Level level, LivingEntity user, ItemStack soupBase) {
      return (ItemStack)this.returnContainerFunction.apply(level, user, soupBase);
   }

   @Override
   public boolean isContainer(ItemStack stack) {
      return this.containerPredicate.test(stack);
   }

   @Override
   public ItemStack getReturnSoupBase(Level level, LivingEntity user, ItemStack container) {
      return (ItemStack)this.returnSoupBaseFunction.apply(level, user, container);
   }
}
