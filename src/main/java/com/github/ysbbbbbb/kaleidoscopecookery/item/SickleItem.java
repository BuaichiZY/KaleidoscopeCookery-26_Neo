package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.SickleHarvestEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.NeoForge;

public class SickleItem extends Item {
   private static final ToolMaterial SICKLE_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_STONE_TOOL, 2000, 4.0F, 1.0F, 5, net.minecraft.tags.ItemTags.STONE_TOOL_MATERIALS
   );

   public SickleItem(ToolMaterial material, Properties properties) {
      super(material.applySwordProperties(properties, 0.0F, -2.0F));
   }

   public SickleItem(ToolMaterial material, Properties properties, Tool toolComponentData) {
      this(material, properties.component(net.minecraft.core.component.DataComponents.TOOL, toolComponentData));
   }

   public SickleItem() {
      this(SICKLE_MATERIAL, ModRegistrationProperties.itemProperties());
   }

   public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
      return true;
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      if (player == null) {
         return super.useOn(context);
      } else {
         Level level = context.getLevel();
         if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
         } else {
            BlockPos pos = context.getClickedPos();
            ItemStack stack = context.getItemInHand();
            int sweepLevel = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
               .get(ModEnchantments.SWEEP).map(stack::getEnchantmentLevel).orElse(0);
            int radius = 2 + sweepLevel;

            for (int x = -radius; x <= radius; x++) {
               for (int y = 0; y <= 1; y++) {
                  for (int z = -radius; z <= radius; z++) {
                     this.harvest(pos, x, y, z, level, player, stack);
                  }
               }
            }

            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            int durabilityCost = switch (sweepLevel) {
               case 1 -> 2;
               case 2 -> 4;
               case 3 -> 8;
               default -> 1;
            };
            stack.hurtAndBreak(durabilityCost, player, EquipmentSlot.MAINHAND);
            player.getCooldowns().addCooldown(stack, 10);
            return InteractionResult.SUCCESS;
         }
      }
   }

   private boolean harvest(BlockPos pos, int x, int y, int z, Level level, Player player, ItemStack stack) {
      BlockPos newPos = pos.offset(x, y, z);
      if (!level.mayInteract(player, newPos)) {
         return false;
      } else {
         BlockState blockState = level.getBlockState(newPos);
         if (blockState.isAir()) {
            return false;
         } else if (blockState.is(TagMod.SICKLE_HARVEST_BLACKLIST)) {
            return false;
         } else {
            Block block = blockState.getBlock();
            SickleHarvestEvent event = new SickleHarvestEvent(player, stack, newPos, blockState);
            if (((SickleHarvestEvent)NeoForge.EVENT_BUS.post(event)).isCanceled()) {
               return event.isCostDurability();
            } else if (block instanceof TeaTreeBlock teaTreeBlock) {
               if (!teaTreeBlock.isMaxAge(blockState)) return false;
               teaTreeBlock.playerDestroy(level, player, newPos, blockState, null, ItemStack.EMPTY);
               level.setBlock(newPos, teaTreeBlock.getStateForAge(0), 3);
               level.levelEvent(null, 2001, newPos, Block.getId(blockState));
               return true;
            } else if (block instanceof CropBlock cropBlock) {
               if (block instanceof RiceCropBlock) {
                  int position = (Integer)blockState.getValue(RiceCropBlock.LOCATION);
                  newPos = newPos.below(position);
                  blockState = level.getBlockState(newPos);
               }

               if (cropBlock.isMaxAge(blockState)) {
                  cropBlock.playerDestroy(level, player, newPos, blockState, null, ItemStack.EMPTY);
                  BlockState stateForAge = cropBlock.getStateForAge(0);
                  BooleanProperty waterlogged = BlockStateProperties.WATERLOGGED;
                  if (stateForAge.hasProperty(waterlogged)) {
                     stateForAge = (BlockState)stateForAge.setValue(waterlogged, (Boolean)blockState.getValue(waterlogged));
                  }

                  level.setBlock(newPos, stateForAge, 3);
                  level.levelEvent(null, 2001, newPos, Block.getId(blockState));
                  return true;
               } else {
                  return false;
               }
            } else if (block instanceof VegetationBlock && player instanceof ServerPlayer serverPlayer) {
               serverPlayer.gameMode.destroyBlock(newPos);
               level.levelEvent(null, 2001, newPos, Block.getId(blockState));
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("tooltip.kaleidoscope_cookery.sickle").withStyle(ChatFormatting.GRAY));
   }
}
