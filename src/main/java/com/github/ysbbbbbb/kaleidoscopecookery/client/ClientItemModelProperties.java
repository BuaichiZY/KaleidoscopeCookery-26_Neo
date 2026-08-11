package com.github.ysbbbbbb.kaleidoscopecookery.client;

import com.github.ysbbbbbb.kaleidoscopecookery.item.KitchenShovelItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.OilPotItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.SteamerItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import org.jspecify.annotations.Nullable;

/**
 * Bridges the mod's legacy item predicates to Minecraft 26's item model system.
 */
@EventBusSubscriber(modid = "kaleidoscope_cookery", value = Dist.CLIENT)
public final class ClientItemModelProperties {
   private ClientItemModelProperties() {
   }

   @SubscribeEvent
   public static void register(RegisterConditionalItemModelPropertyEvent event) {
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_oil"), HasOil.MAP_CODEC);
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_recipe"), HasRecipe.MAP_CODEC);
      event.register(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_items"), HasItems.MAP_CODEC);
   }

   private static final class HasOil implements ConditionalItemModelProperty {
      private static final HasOil INSTANCE = new HasOil();
      private static final MapCodec<HasOil> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         if (stack.getItem() instanceof KitchenShovelItem) {
            return KitchenShovelItem.hasOil(stack);
         }
         return stack.getItem() instanceof OilPotItem && OilPotItem.hasOil(stack);
      }

      @Override
      public MapCodec<HasOil> type() {
         return MAP_CODEC;
      }
   }

   private static final class HasRecipe implements ConditionalItemModelProperty {
      private static final HasRecipe INSTANCE = new HasRecipe();
      private static final MapCodec<HasRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         return RecipeItem.hasRecipe(stack);
      }

      @Override
      public MapCodec<HasRecipe> type() {
         return MAP_CODEC;
      }
   }

   private static final class HasItems implements ConditionalItemModelProperty {
      private static final HasItems INSTANCE = new HasItems();
      private static final MapCodec<HasItems> MAP_CODEC = MapCodec.unit(INSTANCE);

      @Override
      public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
         if (stack.getItem() instanceof TransmutationLunchBagItem) {
            return TransmutationLunchBagItem.hasItems(stack);
         }
         return stack.getItem() instanceof SteamerItem && stack.has(DataComponents.BLOCK_ENTITY_DATA);
      }

      @Override
      public MapCodec<HasItems> type() {
         return MAP_CODEC;
      }
   }
}
