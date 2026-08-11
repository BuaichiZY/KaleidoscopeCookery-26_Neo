package com.github.ysbbbbbb.kaleidoscopecookery.init;

import java.util.function.Supplier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachmentType {
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
      NeoForgeRegistries.ATTACHMENT_TYPES, "kaleidoscope_cookery"
   );
   public static final Supplier<AttachmentType<Vec3>> FLATULENCE_EFFECT_STARTING_POSITION = ATTACHMENT_TYPES.register(
      "flatulence_effect_starting_position", () -> AttachmentType.builder(() -> Vec3.ZERO).build()
   );
}
