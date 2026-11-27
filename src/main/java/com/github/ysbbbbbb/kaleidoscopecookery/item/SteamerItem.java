package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

public class SteamerItem extends BlockItem {
   public static final Identifier HAS_ITEMS = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_items");
   private static final int NONE = 0;
   private static final int HAS = 1;

   public SteamerItem() {
      super((Block)ModBlocks.STEAMER.get(), ModRegistrationProperties.blockItemProperties());
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

   @Override
   public java.util.Optional<net.minecraft.world.inventory.tooltip.TooltipComponent> getTooltipImage(ItemStack stack) {
      var data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
      var level = net.minecraft.client.Minecraft.getInstance().level;
      if (data == null || level == null) return java.util.Optional.empty();
      var tag = data.copyTagWithoutId();
      if (!tag.contains(SteamerBlockEntity.ITEMS_TAG)) return java.util.Optional.empty();
      var items = net.minecraft.core.NonNullList.withSize(4, ItemStack.EMPTY);
      net.minecraft.world.ContainerHelper.loadAllItems(net.minecraft.world.level.storage.TagValueInput.create(
         net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), tag), items);
      return java.util.Optional.of(new com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip(new com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.ItemStackHandler(items)));
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.steamer").withStyle(ChatFormatting.GRAY));
   }
}
