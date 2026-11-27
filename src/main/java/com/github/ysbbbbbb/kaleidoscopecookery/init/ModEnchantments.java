package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ModEnchantments {
   public static final ResourceKey<Enchantment> QUICK_KNIFE = key("quick_knife");
   public static final ResourceKey<Enchantment> SWEEP = key("sweep");

   private ModEnchantments() {}

   private static ResourceKey<Enchantment> key(String name) {
      return ResourceKey.create(Registries.ENCHANTMENT,
         Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name));
   }
}
