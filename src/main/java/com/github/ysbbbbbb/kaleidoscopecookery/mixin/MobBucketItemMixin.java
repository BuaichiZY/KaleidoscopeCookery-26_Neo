package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.advancements.critereon.ModEventTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModTrigger;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin {
   @Shadow
   @Final
   private EntityType<? extends Mob> type;

   @Inject(method = "checkExtraContent", at = @At("RETURN"))
   private void onCheckExtraContent(LivingEntity user, Level level, ItemStack containerStack, BlockPos pos, CallbackInfo ci) {
      if (!level.isClientSide() && user instanceof Player player) {
         if (this.type.builtInRegistryHolder().is(TagMod.RICE_GROWTH_BOOSTER)) {
            MutableBlockPos mutable = pos.mutable();

            for (int x = -1; x <= 1; x++) {
               for (int z = -1; z <= 1; z++) {
                  if (level.getBlockState(mutable.offset(x, 0, z)).is((Block)ModBlocks.RICE_CROP.get())) {
                     ((ModEventTrigger)ModTrigger.EVENT.get()).trigger(player, "place_fish_in_rice_field");
                     return;
                  }
               }
            }
         }
      }
   }
}
