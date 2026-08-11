package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class MillstoneRecipeSerializer {
   private static final MapCodec<List<RandomOutput>> RESULTS_MAP_CODEC = new MapCodec<List<RandomOutput>>() {
      public <T> DataResult<List<RandomOutput>> decode(DynamicOps<T> ops, MapLike<T> input) {
         T resultsNode = (T)input.get("results");
         if (resultsNode != null) {
            return RandomOutput.CODEC.listOf().parse(ops, resultsNode);
         } else {
            T resultNode = (T)input.get("result");
            return resultNode != null
               ? RandomOutput.CODEC.parse(ops, resultNode).map(List::of)
               : DataResult.error(() -> "Missing both 'results' and 'result' fields!");
         }
      }

      public <T> RecordBuilder<T> encode(List<RandomOutput> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
         return input.size() == 1
            ? prefix.add("result", RandomOutput.CODEC.encodeStart(ops, input.getFirst()))
            : prefix.add("results", RandomOutput.CODEC.listOf().encodeStart(ops, input));
      }

      public <T> Stream<T> keys(DynamicOps<T> ops) {
         return Stream.of((T[])(new Object[]{ops.createString("results"), ops.createString("result")}));
      }
   };
   public static final MapCodec<MillstoneRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(MillstoneRecipe::ingredient), RESULTS_MAP_CODEC.forGetter(MillstoneRecipe::results)
         )
         .apply(instance, MillstoneRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, MillstoneRecipe> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC,
      MillstoneRecipe::ingredient,
      RandomOutput.STREAM_CODEC.apply(ByteBufCodecs.list(4)),
      MillstoneRecipe::results,
      MillstoneRecipe::new
   );
   public static final Identifier EMPTY_ID = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone/empty");

   public static RecipeHolder<MillstoneRecipe> getEmptyRecipe() {
      MillstoneRecipe recipe = new MillstoneRecipe(com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes.EMPTY_INGREDIENT, NonNullList.withSize(4, RandomOutput.EMPTY));
      return new RecipeHolder(ResourceKey.create(Registries.RECIPE, EMPTY_ID), recipe);
   }

   public MapCodec<MillstoneRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, MillstoneRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
