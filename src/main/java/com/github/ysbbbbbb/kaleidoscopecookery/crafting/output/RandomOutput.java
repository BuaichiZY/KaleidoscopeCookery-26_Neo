package com.github.ysbbbbbb.kaleidoscopecookery.crafting.output;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public record RandomOutput(ItemStackTemplate template, float chance) {
   public static final RandomOutput EMPTY = new RandomOutput(ItemStack.EMPTY, 1.0F);
   public static final Codec<RandomOutput> CODEC = Codec.lazyInitialized(
      () -> RecordCodecBuilder.create(
         instance -> instance.group(
               Item.CODEC.fieldOf("id").forGetter(r -> r.template.item()),
               ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(r -> r.template.count()),
               DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(r -> r.template.components()),
               Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(RandomOutput::chance)
            )
            .apply(instance, RandomOutput::new)
      )
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, RandomOutput> STREAM_CODEC = StreamCodec.composite(
      ItemStackTemplate.STREAM_CODEC, RandomOutput::template, ByteBufCodecs.FLOAT, RandomOutput::chance, RandomOutput::new
   );

   public RandomOutput(Holder<Item> tag, int count, DataComponentPatch components, float chance) {
      this(new ItemStackTemplate(tag, count, components), Mth.clamp(chance, 0.0F, 1.0F));
   }

   public RandomOutput(ItemStack stack, float chance) {
      this(stack.isEmpty() ? null : ItemStackTemplate.fromNonEmptyStack(stack), Mth.clamp(chance, 0.0F, 1.0F));
   }

   public ItemStack stack() {
      return this.template == null ? ItemStack.EMPTY : this.template.create();
   }

   public boolean isEmpty() {
      return this.template == null;
   }
}
