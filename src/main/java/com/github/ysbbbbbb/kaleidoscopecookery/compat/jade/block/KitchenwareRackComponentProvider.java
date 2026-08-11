package com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.jade.ModPlugin;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

public enum KitchenwareRackComponentProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
   INSTANCE;

   public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
      return ClientViewGroup.map(list, ItemView::new, null);
   }

   @Nullable
   public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
      if (accessor.getTarget() instanceof KitchenwareRacksBlockEntity kitchenwareRacks) {
         List<ItemStack> list = Lists.newArrayList();
         if (!kitchenwareRacks.getItemLeft().isEmpty()) {
            list.add(kitchenwareRacks.getItemLeft());
         }

         if (!kitchenwareRacks.getItemRight().isEmpty()) {
            list.add(kitchenwareRacks.getItemRight());
         }

         return List.of(new ViewGroup(list));
      } else {
         return null;
      }
   }

   public Identifier getUid() {
      return ModPlugin.KITCHENWARE_RACK;
   }
}
