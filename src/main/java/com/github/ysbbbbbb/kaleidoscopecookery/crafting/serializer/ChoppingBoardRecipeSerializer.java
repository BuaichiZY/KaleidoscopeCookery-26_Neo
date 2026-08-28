package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ChoppingBoardRecipeSerializer {
   public static final Identifier EMPTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "empty");
   public static final MapCodec<ChoppingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ChoppingBoardRecipe::getIngredient),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(ChoppingBoardRecipe::getResultTemplate),
            Codec.INT.optionalFieldOf("cut_count", 3).forGetter(ChoppingBoardRecipe::getCutCount),
            Identifier.CODEC.optionalFieldOf("model_id", EMPTY).forGetter(ChoppingBoardRecipe::getModelId)
         )
         .apply(instance, ChoppingBoardRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, ChoppingBoardRecipe> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC,
      ChoppingBoardRecipe::getIngredient,
      ItemStackTemplate.STREAM_CODEC,
      ChoppingBoardRecipe::getResultTemplate,
      ByteBufCodecs.INT,
      ChoppingBoardRecipe::getCutCount,
      Identifier.STREAM_CODEC,
      ChoppingBoardRecipe::getModelId,
      ChoppingBoardRecipe::new
   );

   public MapCodec<ChoppingBoardRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, ChoppingBoardRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
