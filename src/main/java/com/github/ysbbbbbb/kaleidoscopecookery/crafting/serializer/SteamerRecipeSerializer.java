package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class SteamerRecipeSerializer {
   public static final MapCodec<SteamerRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SteamerRecipe::getIngredient),
            ItemStack.CODEC.fieldOf("result").forGetter(SteamerRecipe::getResult),
            Codec.INT.optionalFieldOf("cook_tick", 1200).forGetter(SteamerRecipe::getCookTick)
         )
         .apply(instance, SteamerRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SteamerRecipe> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC,
      SteamerRecipe::getIngredient,
      ItemStack.STREAM_CODEC,
      SteamerRecipe::getResult,
      ByteBufCodecs.INT,
      SteamerRecipe::getCookTick,
      SteamerRecipe::new
   );

   public MapCodec<SteamerRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, SteamerRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
