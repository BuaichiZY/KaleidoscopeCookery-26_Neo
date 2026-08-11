package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.OilPotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum OilPotComponentProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
   INSTANCE;

   public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
      return ClientViewGroup.map(list, ItemView::new, null);
   }

   @Nullable
   public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
      if (accessor.getTarget() instanceof OilPotBlockEntity oilPot) {
         int oilCount = oilPot.getOilCount();
         if (oilCount > 0) {
            ItemStack stack = new ItemStack((ItemLike)ModItems.OIL.get(), oilCount);
            return List.of(new ViewGroup(Collections.singletonList(stack)));
         }
      }

      return null;
   }

   public Identifier getUid() {
      return ModPlugin.OIL_POT;
   }
}
