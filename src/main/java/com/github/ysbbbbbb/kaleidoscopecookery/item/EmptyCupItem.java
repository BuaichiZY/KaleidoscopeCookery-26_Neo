package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class EmptyCupItem extends BlockItem {
   public EmptyCupItem() {
      super((Block)ModBlocks.EMPTY_CUP.get(), ModRegistrationProperties.itemProperties().stacksTo(16));
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      return player != null && !player.isSecondaryUseActive() ? InteractionResult.PASS : super.useOn(context);
   }
}
