package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class QualityEvaluator {
   private static final int MAX_CAPACITY = 9;

   public static Quality evaluate(List<ItemStack> inputs, List<Ingredient> ingredients, Identifier recipeId, long worldSeed) {
      List<Ingredient> nonEmpty = Lists.newArrayList();
      ingredients.forEach(ingredient -> {
         if (!ingredient.isEmpty()) {
            nonEmpty.add(ingredient);
         }
      });
      if (!inputs.isEmpty() && !nonEmpty.isEmpty()) {
         if (nonEmpty.size() == 1) {
            return oneInputQuality(inputs, recipeId, worldSeed);
         } else {
            List<Pair<Ingredient, Integer>> recipeVector = randomVector(nonEmpty, recipeId, worldSeed);
            return evalQuality(inputs, recipeVector);
         }
      } else {
         return Quality.POOR;
      }
   }

   @NotNull
   private static Quality oneInputQuality(List<ItemStack> inputs, Identifier recipeId, long worldSeed) {
      long recipeSeed = worldSeed * 31L + recipeId.hashCode();
      Random random = new Random(recipeSeed);
      int count = inputs.size();
      if (count <= 1) {
         return Quality.POOR;
      } else {
         int standard = 1 + random.nextInt(2);
         if (count <= standard) {
            return Quality.STANDARD;
         } else {
            int excellent = 2 + random.nextInt(2);
            return count <= excellent ? Quality.EXCELLENT : Quality.SUPERB;
         }
      }
   }

   private static List<Pair<Ingredient, Integer>> randomVector(List<Ingredient> ingredients, Identifier id, long seed) {
      long recipeSeed = seed * 31L + id.hashCode();
      Random random = new Random(recipeSeed);
      int size = ingredients.size();
      IntList countPool = new IntArrayList(size);
      switch (size) {
         case 2:
            countPool.add(2 + random.nextInt(3));
            countPool.add(1 + random.nextInt(2));
            break;
         case 3:
            countPool.add(2 + random.nextInt(3));
            countPool.add(1 + random.nextInt(3));
            countPool.add(1 + random.nextInt(2));
            break;
         default:
            for (int i = 0; i < size; i++) {
               countPool.add(1 + random.nextInt(2));
            }
      }

      Collections.shuffle(countPool, random);
      List<Pair<Ingredient, Integer>> recipeVector = Lists.newArrayList();

      for (int i = 0; i < size; i++) {
         recipeVector.add(Pair.of(ingredients.get(i), countPool.getInt(i)));
      }

      return recipeVector;
   }

   private static Quality evalQuality(List<ItemStack> inputs, List<Pair<Ingredient, Integer>> recipes) {
      int size = recipes.size();
      int[] inputsVec = new int[size];
      int[] recipesVec = new int[size];

      for (int i = 0; i < size; i++) {
         Pair<Ingredient, Integer> pair = recipes.get(i);

         for (ItemStack stack : inputs) {
            if (((Ingredient)pair.left()).test(stack)) {
               inputsVec[i]++;
            }
         }

         recipesVec[i] = (Integer)pair.right();
      }

      double finalScore = getFinalScore(size, inputs.size(), inputsVec, recipesVec);

      for (Quality quality : Quality.values()) {
         if (finalScore >= quality.getScore()) {
            return quality;
         }
      }

      return Quality.STANDARD;
   }

   private static double getFinalScore(int size, int inputSize, int[] inputsVec, int[] recipesVec) {
      double dotProduct = 0.0;
      double normA = 0.0;
      double normB = 0.0;

      for (int i = 0; i < size; i++) {
         double a = inputsVec[i];
         double b = recipesVec[i];
         dotProduct += a * b;
         normA += a * a;
         normB += b * b;
      }

      double cosine = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
      double similarityScore = Math.pow(cosine, 4.0);
      double quantityFactor = 0.8 + 0.2 * (inputSize / 9.0);
      return Mth.clamp(similarityScore * quantityFactor, 0.0, 1.0);
   }
}
