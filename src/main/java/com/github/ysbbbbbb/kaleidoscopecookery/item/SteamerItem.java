package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SteamerItem extends BlockItem {
   public static final Identifier HAS_ITEMS = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_items");
   private static final int NONE = 0;
   private static final int HAS = 1;

   public SteamerItem() {
      super((Block)ModBlocks.STEAMER.get(), ModRegistrationProperties.itemProperties());
   }

   @OnlyIn(Dist.CLIENT)
   public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
      return stack.has(DataComponents.BLOCK_ENTITY_DATA) ? 1.0F : 0.0F;
   }

   protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
      Level level = context.getLevel();
      Direction face = context.getClickedFace();
      BlockPos clickedPos = context.getClickedPos();
      if (face != Direction.UP) {
         return false;
      } else {
         BlockEntity blockEntity = level.getBlockEntity(clickedPos);
         ItemStack stack = context.getItemInHand();
         boolean hasData = stack.has(DataComponents.BLOCK_ENTITY_DATA);
         if (blockEntity instanceof SteamerBlockEntity steamer && stack.is(this) && hasData && stack.getCount() == 1) {
            steamer.mergeItem(stack, context.getLevel());
         }

         return super.placeBlock(context, state);
      }
   }

   public int getMaxStackSize(ItemStack stack) {
      return stack.has(DataComponents.BLOCK_ENTITY_DATA) ? 1 : super.getMaxStackSize(stack);
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.steamer").withStyle(ChatFormatting.GRAY));
   }
}
