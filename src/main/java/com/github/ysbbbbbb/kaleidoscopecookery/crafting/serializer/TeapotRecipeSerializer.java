package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
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

public final class TeapotRecipeSerializer {
   public static final int DEFAULT_TIME = 2400;
   public static final int DEFAULT_INGREDIENT_COUNT = 12;
   public static final Identifier EMPTY_TEA_FLUID = Identifier.withDefaultNamespace("empty");
   public static final MapCodec<TeapotRecipe> CODEC = RecordCodecBuilder.mapCodec(
      inst -> inst.group(
            Identifier.CODEC.optionalFieldOf("tea_fluid", EMPTY_TEA_FLUID).forGetter(TeapotRecipe::teaFluid),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(TeapotRecipe::ingredient),
            Codec.INT.optionalFieldOf("ingredient_count", 12).forGetter(TeapotRecipe::ingredientCount),
            Codec.INT.optionalFieldOf("time", 2400).forGetter(TeapotRecipe::time),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(TeapotRecipe::resultTemplate)
         )
         .apply(inst, TeapotRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, TeapotRecipe> STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC,
      TeapotRecipe::teaFluid,
      Ingredient.CONTENTS_STREAM_CODEC,
      TeapotRecipe::ingredient,
      ByteBufCodecs.VAR_INT,
      TeapotRecipe::ingredientCount,
      ByteBufCodecs.VAR_INT,
      TeapotRecipe::time,
      ItemStackTemplate.STREAM_CODEC,
      TeapotRecipe::resultTemplate,
      TeapotRecipe::new
   );

   public MapCodec<TeapotRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, TeapotRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
