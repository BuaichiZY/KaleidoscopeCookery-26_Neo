package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record ItemRenderReplacer(
   Map<Identifier, Object> pot,
   Map<Identifier, Object> stockpotCooking,
   Map<Identifier, Object> stockpotFinished,
   Map<Identifier, Object> millstone,
   Map<Identifier, Object> steamer
) {
   public static final Codec<Object> RL_CODEC = Codec.STRING.comapFlatMap(ItemRenderReplacer::toLocation, ItemRenderReplacer::fromLocation).stable();
   public static final Codec<ItemRenderReplacer> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("pot").forGetter(ItemRenderReplacer::pot),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("stockpot_cooking").forGetter(ItemRenderReplacer::stockpotCooking),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("stockpot_finished").forGetter(ItemRenderReplacer::stockpotFinished),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("millstone").forGetter(ItemRenderReplacer::millstone),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("steamer").forGetter(ItemRenderReplacer::steamer)
         )
         .apply(instance, ItemRenderReplacer::new)
   );
   private static Function<Object, BakedModel> CACHE = createNewCache();

   public ItemRenderReplacer() {
      this(Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
   }

   private static Function<Object, BakedModel> createNewCache() {
      return Util.memoize(id -> {
         ModelManager modelManager = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager();
         if (id instanceof ModelIdentifier modelRl) {
            return modelManager.getModel(modelRl);
         } else {
            return id instanceof Identifier rl ? modelManager.getModel(ModelIdentifier.standalone(rl)) : modelManager.getMissingModel();
         }
      });
   }

   public static void resetCache() {
      CACHE = createNewCache();
   }

   public static BakedModel getModel(@Nullable Level level, ItemStack stack, Map<Identifier, Object> models) {
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
      Object location = models.get(key);
      return location == null ? itemRenderer.getModel(stack, level, null, 0) : CACHE.apply(location);
   }

   private static DataResult<Object> toLocation(String input) {
      String[] split = input.split("#");
      return split.length > 1
         ? DataResult.success(new ModelIdentifier(Identifier.parse(split[0]), split[1]))
         : DataResult.success(Identifier.parse(input));
   }

   private static String fromLocation(Object input) {
      return input.toString();
   }

   public void addAll(ItemRenderReplacer other) {
      this.pot.putAll(other.pot);
      this.stockpotCooking.putAll(other.stockpotCooking);
      this.stockpotFinished.putAll(other.stockpotFinished);
      this.millstone.putAll(other.millstone);
      this.steamer.putAll(other.steamer);
   }
}
