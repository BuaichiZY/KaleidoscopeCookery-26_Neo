package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class FlexStockpotRecipeSerializer {
   public static final MapCodec<FlexStockpotRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Ingredient.CODEC
               .listOf()
               .fieldOf("ingredients")
               .forGetter(FlexStockpotRecipe::getIngredients),
            Identifier.CODEC.optionalFieldOf("soup_base", StockpotRecipeSerializer.DEFAULT_SOUP_BASE).forGetter(FlexStockpotRecipe::soupBase),
            ItemStack.CODEC.fieldOf("result").forGetter(FlexStockpotRecipe::result),
            Codec.INT.optionalFieldOf("time", 300).forGetter(FlexStockpotRecipe::time),
            Ingredient.CODEC.optionalFieldOf("carrier", StockpotRecipeSerializer.DEFAULT_CARRIER).forGetter(FlexStockpotRecipe::carrier),
            StockpotVisuals.CODEC.forGetter(FlexStockpotRecipe::visuals)
         )
         .apply(instance, FlexStockpotRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, FlexStockpotRecipe> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
      FlexStockpotRecipe::getIngredients,
      Identifier.STREAM_CODEC,
      FlexStockpotRecipe::soupBase,
      ItemStack.STREAM_CODEC,
      FlexStockpotRecipe::result,
      ByteBufCodecs.INT,
      FlexStockpotRecipe::time,
      Ingredient.CONTENTS_STREAM_CODEC,
      FlexStockpotRecipe::carrier,
      StockpotVisuals.STREAM_CODEC,
      FlexStockpotRecipe::visuals,
      FlexStockpotRecipe::new
   );

   public MapCodec<FlexStockpotRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, FlexStockpotRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
