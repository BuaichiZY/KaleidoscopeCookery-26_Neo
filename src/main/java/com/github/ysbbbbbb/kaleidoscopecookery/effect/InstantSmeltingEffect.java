package com.github.ysbbbbbb.kaleidoscopecookery.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags.Blocks;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class InstantSmeltingEffect {
   public static void onGetDrops(BlockState state, ServerLevel level, Entity entity, CallbackInfoReturnable<List<ItemStack>> cir) {
      if (state.is(Blocks.ORES) && entity instanceof LivingEntity living) {
         MobEffectInstance effect = living.getEffect(ModEffects.INSTANT_SMELTING);
         if (effect != null) {
            List<ItemStack> orgDrops = (List<ItemStack>)cir.getReturnValue();
            if (!orgDrops.isEmpty()) {
               int[] count = new int[]{effect.getAmplifier() + 1};
               List<ItemStack> newDrops = Lists.newArrayList();

               for (ItemStack stack : orgDrops) {
                  if (count[0] <= 0) {
                     break;
                  }

                  SingleRecipeInput input = new SingleRecipeInput(stack);
                  level.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level).ifPresent(recipe -> {
                     ItemStack result = ((SmeltingRecipe)recipe.value()).assemble(input);
                     int splitCount = stack.split(count[0]).getCount();
                     newDrops.add(result.copyWithCount(result.getCount() * splitCount));
                     count[0] -= splitCount;
                  });
               }

               if (!newDrops.isEmpty()) {
                  orgDrops.stream().filter(stackx -> !stackx.isEmpty()).forEach(newDrops::add);
                  cir.setReturnValue(newDrops);
               }
            }
         }
      }
   }
}
