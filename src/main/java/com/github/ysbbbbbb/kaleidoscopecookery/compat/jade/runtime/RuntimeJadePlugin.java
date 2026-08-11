package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.runtime;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import net.minecraft.resources.Identifier;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class RuntimeJadePlugin implements IWailaPlugin {
   public static final Identifier MILLSTONE = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "millstone");
   public static final Identifier SHAWARMA_SPIT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "shawarma_spit");

   @Override
   public void registerClient(IWailaClientRegistration registration) {
      registration.registerBlockComponent(RuntimeMillstoneComponentProvider.INSTANCE, MillstoneBlock.class);
      registration.registerBlockComponent(RuntimeShawarmaSpitComponentProvider.INSTANCE, ShawarmaSpitBlock.class);
   }
}
