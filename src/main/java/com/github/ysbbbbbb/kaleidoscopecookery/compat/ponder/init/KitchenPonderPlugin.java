package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init;

import javax.annotation.ParametersAreNonnullByDefault;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import org.jspecify.annotations.NullMarked;
import net.minecraft.resources.Identifier;

@ParametersAreNonnullByDefault
@NullMarked
public class KitchenPonderPlugin implements PonderPlugin {
   public String getModId() {
      return "kaleidoscope_cookery";
   }

   public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
      KitchenBlockPonderScreen.register(helper);
   }

   public void registerTags(PonderTagRegistrationHelper<Identifier> helper) {
      KitchenBlockPonderTag.register(helper);
   }

   public static void init() {
      PonderIndex.addPlugin(new KitchenPonderPlugin());
   }
}
