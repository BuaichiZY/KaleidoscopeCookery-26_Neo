package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class BowlFoodBlockItem extends BlockItem implements IHasContainer {
   private final List<MobEffectInstance> effectInstances = Lists.newArrayList();
   private final Function<Quality, List<MobEffectInstance>> effectCache = Util.memoize(quality -> QualityUtils.modifyEffects(this.effectInstances, quality));
   private final Optional<Item> usingConvertsTo;

   public BowlFoodBlockItem(Block block, FoodProperties properties, @Nullable ItemLike usingConvertsTo) {
      super(block, createProperties(properties, usingConvertsTo));
      // Item components are bound only after the item registry event finishes.
      // Keep the item reference here instead of constructing an ItemStack while
      // entries are still being registered.
      this.usingConvertsTo = usingConvertsTo == null ? Optional.empty() : Optional.of(usingConvertsTo.asItem());
      ModFoods.effectsFor(properties).forEach(effect -> {
         if (effect.probability() >= 1.0F) {
            this.effectInstances.add(effect.effect());
         }
      });
   }

   private static Properties createProperties(FoodProperties properties, @Nullable ItemLike usingConvertsTo) {
      Properties result = ModRegistrationProperties.itemProperties().stacksTo(16).food(properties, ModFoods.consumableFor(properties));
      if (usingConvertsTo != null) {
         result.usingConvertsTo(usingConvertsTo.asItem());
      }
      return result;
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (level instanceof ServerLevel serverLevel && this.getBlock() instanceof FoodBiteBlock foodBiteBlock) {
         Builder builder = new Builder(serverLevel)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(entity.blockPosition()))
            .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
            .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
         BlockState state = (BlockState)foodBiteBlock.defaultBlockState().setValue(foodBiteBlock.getBites(), foodBiteBlock.getMaxBites());
         List<ItemStack> drops = this.getDrops(state, builder);
         drops.forEach(itemStack -> {
            if (!itemStack.isEmpty()) {
                if (!this.usingConvertsTo.isPresent() || itemStack.getItem() != this.usingConvertsTo.get()) {
                  if (entity instanceof Player player) {
                     ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                  } else {
                     ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), itemStack);
                     level.addFreshEntity(itemEntity);
                  }
               }
            }
         });
      }

      return super.finishUsingItem(stack, level, entity);
   }

   private List<ItemStack> getDrops(BlockState state, Builder params) {
      Optional<ResourceKey<LootTable>> resourcekey = state.getBlock().getLootTable();
      if (resourcekey.isEmpty()) {
         return Collections.emptyList();
      } else {
         LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
         ServerLevel serverLevel = lootParams.getLevel();
         LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(resourcekey.orElseThrow());
         return lootTable.getRandomItems(lootParams);
      }
   }

   @Override
   public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      TooltipDisplay display,
      Consumer<Component> tooltip,
      TooltipFlag flag
   ) {
      tooltip.accept(Component.translatable("item_group.kaleidoscope_cookery.cookery_food.name").withStyle(ChatFormatting.BLUE));
      Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
      if (id != null) {
         String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
         MutableComponent full = Component.translatable(key).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC});
         String text = full.getString();

         for (String line : text.split("\n")) {
            if (!line.isEmpty()) {
               tooltip.accept(Component.literal(line).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
            } else {
               tooltip.accept(CommonComponents.EMPTY);
            }
         }
      }

      boolean showEffect = !this.effectInstances.isEmpty()
         && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS
         && (Boolean)ClientConfig.SHOW_FOOD_EFFECT_TOOLTIPS.get();
      if (QualityUtils.hasQuality(stack)) {
         Quality quality = QualityUtils.getQuality(stack);
         tooltip.accept(quality.getTooltip());
         if (showEffect) {
            tooltip.accept(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectCache.apply(quality), tooltip, 1.0F, context.tickRate());
         }
      } else {
         if (showEffect) {
            tooltip.accept(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectInstances, tooltip, 1.0F, context.tickRate());
         }
      }
   }

   @Override
   public Item getContainerItem() {
      return Items.BOWL;
   }
}
