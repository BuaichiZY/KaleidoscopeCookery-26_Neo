package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record StockpotVisuals(Identifier cookingTexture, Identifier finishedTexture, int cookingBubbleColor, int finishedBubbleColor) {
   public static final Identifier DEFAULT_COOKING_TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot/default_cooking");
   public static final Identifier DEFAULT_FINISHED_TEXTURE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot/default_finished");
   public static final int DEFAULT_COOKING_BUBBLE_COLOR = 16772291;
   public static final int DEFAULT_FINISHED_BUBBLE_COLOR = 16034443;
   public static final StockpotVisuals DEFAULT = new StockpotVisuals(DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE, 16772291, 16034443);
   public static final MapCodec<StockpotVisuals> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("cooking_texture", DEFAULT_COOKING_TEXTURE).forGetter(StockpotVisuals::cookingTexture),
            Identifier.CODEC.optionalFieldOf("finished_texture", DEFAULT_FINISHED_TEXTURE).forGetter(StockpotVisuals::finishedTexture),
            Codec.INT.optionalFieldOf("cooking_bubble_color", 16772291).forGetter(StockpotVisuals::cookingBubbleColor),
            Codec.INT.optionalFieldOf("finished_bubble_color", 16034443).forGetter(StockpotVisuals::finishedBubbleColor)
         )
         .apply(instance, StockpotVisuals::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, StockpotVisuals> STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC,
      StockpotVisuals::cookingTexture,
      Identifier.STREAM_CODEC,
      StockpotVisuals::finishedTexture,
      ByteBufCodecs.INT,
      StockpotVisuals::cookingBubbleColor,
      ByteBufCodecs.INT,
      StockpotVisuals::finishedBubbleColor,
      StockpotVisuals::new
   );
}
