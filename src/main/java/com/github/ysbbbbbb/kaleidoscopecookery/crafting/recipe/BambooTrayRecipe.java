package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class BambooTrayRecipe implements BaseRecipe<SingleRecipeInput> {
   private final Ingredient ingredient;
   private final ItemStackTemplate resultTemplate;
   private final Subtype subtype;
   private final int duration;

   public BambooTrayRecipe(Ingredient ingredient, ItemStackTemplate resultTemplate, Subtype subtype, int duration) {
      this.ingredient = ingredient;
      this.resultTemplate = resultTemplate;
      this.subtype = subtype;
      this.duration = Math.max(duration, 1);
   }

   public boolean matches(SingleRecipeInput input, Level level) { return this.ingredient.test(input.getItem(0)); }
   public boolean isSpecial() { return true; }
   public Ingredient getIngredient() { return this.ingredient; }
   public ItemStackTemplate getResultTemplate() { return this.resultTemplate; }
   public ItemStack getResult() { return this.resultTemplate.create(); }
   public ItemStack getResultItem(Provider registries) { return this.resultTemplate.create(); }
   public Subtype getSubtype() { return this.subtype; }
   public int getDuration() { return this.duration; }

   @SuppressWarnings("unchecked")
   public RecipeSerializer<BambooTrayRecipe> getSerializer() {
      return (RecipeSerializer<BambooTrayRecipe>)(RecipeSerializer<?>)ModRecipes.BAMBOO_TRAY_SERIALIZER.get();
   }
   public RecipeType<BambooTrayRecipe> getType() { return ModRecipes.BAMBOO_TRAY_RECIPE; }

   public enum Subtype implements StringRepresentable {
      WETTING(0, "wetting"), DRYING(1, "drying");
      private static final IntFunction<Subtype> BY_ID = ByIdMap.continuous(Subtype::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
      public static final Codec<Subtype> CODEC = StringRepresentable.fromEnum(Subtype::values);
      public static final StreamCodec<ByteBuf, Subtype> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Subtype::getId);
      private final int id;
      private final String serializedName;
      Subtype(int id, String serializedName) { this.id = id; this.serializedName = serializedName; }
      private int getId() { return this.id; }
      public String getSerializedName() { return this.serializedName; }
   }
}
