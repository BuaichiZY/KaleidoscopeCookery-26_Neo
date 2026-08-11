package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.RiceBowlRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class RiceBowlRecipeSerializer {
   public static final MapCodec<RiceBowlRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CustomRecipe::category),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(RiceBowlRecipe::getIngredient),
            ItemStack.CODEC.fieldOf("result").forGetter(RiceBowlRecipe::getResult)
         )
         .apply(instance, RiceBowlRecipe::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, RiceBowlRecipe> STREAM_CODEC = StreamCodec.composite(
      CraftingBookCategory.STREAM_CODEC,
      CustomRecipe::category,
      Ingredient.CONTENTS_STREAM_CODEC,
      RiceBowlRecipe::getIngredient,
      ItemStack.STREAM_CODEC,
      RiceBowlRecipe::getResult,
      RiceBowlRecipe::new
   );

   public MapCodec<RiceBowlRecipe> codec() {
      return CODEC;
   }

   public StreamCodec<RegistryFriendlyByteBuf, RiceBowlRecipe> streamCodec() {
      return STREAM_CODEC;
   }
}
