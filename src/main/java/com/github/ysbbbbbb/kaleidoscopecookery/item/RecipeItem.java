package com.github.ysbbbbbb.kaleidoscopecookery.item;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRegistrationProperties;
import com.github.ysbbbbbb.kaleidoscopecookery.api.event.RecipeItemEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexPotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityEvaluator;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeItem extends BlockItem {
   public static final Identifier HAS_RECIPE_PROPERTY = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "has_recipe");
   public static final Identifier POT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "pot");
   public static final Identifier STOCKPOT = Identifier.fromNamespaceAndPath("kaleidoscope_cookery", "stockpot");
   private static final int NO_RECIPE = 0;
   private static final int HAS_RECIPE = 1;

   public RecipeItem() {
      super((Block)ModBlocks.RECIPE_BLOCK.get(), ModRegistrationProperties.itemProperties());
   }

   public static void setRecipe(ItemStack stack, RecipeItem.RecipeRecord record) {
      stack.set(ModDataComponents.RECIPE_RECORD, record);
   }

   @Nullable
   public static RecipeItem.RecipeRecord getRecipe(ItemStack stack) {
      return !stack.isEmpty() && stack.getItem() instanceof RecipeItem ? (RecipeItem.RecipeRecord)stack.get(ModDataComponents.RECIPE_RECORD) : null;
   }

   public static boolean hasRecipe(ItemStack stack) {
      return stack.has(ModDataComponents.RECIPE_RECORD);
   }

   @OnlyIn(Dist.CLIENT)
   public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
      return hasRecipe(stack) ? 1.0F : 0.0F;
   }

   public Component getName(ItemStack pStack) {
      if (hasRecipe(pStack)) {
         RecipeItem.RecipeRecord recipe = getRecipe(pStack);
         if (recipe != null) {
            Component result = recipe.output().getHoverName();
            Component type;
            if (recipe.type().equals(POT)) {
               type = Component.translatable("block.kaleidoscope_cookery.pot");
            } else if (recipe.type().equals(STOCKPOT)) {
               type = Component.translatable("block.kaleidoscope_cookery.stockpot");
            } else {
               type = Component.empty();
            }

            return Component.translatable("block.kaleidoscope_cookery.recipe_block.has_record", new Object[]{result, type});
         }
      }

      return super.getName(pStack);
   }

   public InteractionResult useOn(UseOnContext context) {
      if (!(context.getLevel() instanceof ServerLevel serverLevel)) {
         return InteractionResult.SUCCESS;
      }

      ItemStack itemInHand = context.getItemInHand();
      BlockPos clickedPos = context.getClickedPos();
      BlockEntity blockEntity = context.getLevel().getBlockEntity(clickedPos);
      RecipeManager recipeManager = serverLevel.recipeAccess();
      if (blockEntity == null) {
         return super.useOn(context);
      } else {
         Player player = context.getPlayer();
         if (player == null) {
            return super.useOn(context);
         } else {
            return hasRecipe(itemInHand)
               ? this.onPutRecipe(blockEntity, player, itemInHand)
               : this.onRecordRecipe(serverLevel, player, blockEntity, recipeManager, itemInHand);
         }
      }
   }

   private InteractionResult onPutRecipe(BlockEntity blockEntity, Player player, ItemStack itemInHand) {
      RecipeItem.RecipeRecord record = getRecipe(itemInHand);
      if (record == null) {
         return InteractionResult.PASS;
      } else if (blockEntity instanceof PotBlockEntity pot
         && pot.getStatus() == 0
         && (Boolean)pot.getBlockState().getValue(PotBlock.HAS_OIL)
         && record.type().equals(POT)) {
         List<ItemStack> inputs = pot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
         return !inputs.isEmpty() ? InteractionResult.PASS : this.handlePutRecipe(player, record, () -> pot.addAllIngredients(record.input(), player));
      } else if (blockEntity instanceof StockpotBlockEntity stockpot && stockpot.getStatus() == 1 && record.type().equals(STOCKPOT)) {
         List<ItemStack> inputs = stockpot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
         return !inputs.isEmpty() ? InteractionResult.PASS : this.handlePutRecipe(player, record, () -> stockpot.addAllIngredients(record.input(), player));
      } else {
         return InteractionResult.PASS;
      }
   }

   @NotNull
   private InteractionResult handlePutRecipe(Player player, RecipeItem.RecipeRecord record, Runnable success) {
      Reference2IntMap<Item> need = new Reference2IntOpenHashMap();

      for (ItemStack s : record.input()) {
         if (!s.isEmpty()) {
            Item item = s.getItem();
            need.put(item, need.getInt(item) + 1);
         }
      }

      IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
      Reference2IntMap<Item> supply = new Reference2IntOpenHashMap();

      for (int slot = 0; slot < inventory.getSlots(); slot++) {
         ItemStack sx = inventory.getStackInSlot(slot);
         if (!sx.isEmpty()) {
            RecipeItemEvent.CheckItem event = new RecipeItemEvent.CheckItem(sx, supply);
            NeoForge.EVENT_BUS.post(event);
            Item item = sx.getItem();
            supply.put(item, supply.getInt(item) + sx.getCount());
         }
      }

      Reference2IntMap<Item> missing = new Reference2IntOpenHashMap();
      ObjectIterator var19 = need.keySet().iterator();

      while (var19.hasNext()) {
         Item item = (Item)var19.next();
         if (supply.getInt(item) < need.getInt(item)) {
            missing.put(item, need.getInt(item) - supply.getInt(item));
         }
      }

      if (!missing.isEmpty()) {
         MutableComponent component = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.missing");
         int i = 0;

         for (ObjectIterator var26 = missing.keySet().iterator(); var26.hasNext(); i++) {
            Item sx = (Item)var26.next();
            Component hoverName = sx.getDefaultInstance().getHoverName();
            MutableComponent count = Component.literal("×%d".formatted(missing.getInt(sx)));
            if (i != 0) {
               component = component.append(CommonComponents.SPACE);
            }

            component.append(CommonComponents.SPACE).append(hoverName).append(count);
         }

         if (!player.level().isClientSide()) {
            player.sendSystemMessage(component);
         }

         return InteractionResult.FAIL;
      } else {
         var19 = need.keySet().iterator();

         while (var19.hasNext()) {
            Item item = (Item)var19.next();
            int needCount = need.getInt(item);

            for (int i = 0; i < inventory.getSlots(); i++) {
               ItemStack inSlot = inventory.getStackInSlot(i);
               if (!inSlot.isEmpty()) {
                  RecipeItemEvent.DeductItem event = new RecipeItemEvent.DeductItem(inSlot, item, new int[]{needCount});
                  NeoForge.EVENT_BUS.post(event);
                  needCount = event.getNeedCount();
                  if (needCount <= 0) {
                     break;
                  }

                  if (inSlot.is(item)) {
                     int extracted = Math.min(needCount, inSlot.getCount());
                     inventory.extractItem(i, extracted, false);
                     needCount -= extracted;
                     if (needCount <= 0) {
                        break;
                     }
                  }
               }
            }
         }

         success.run();
         return InteractionResult.SUCCESS;
      }
   }

   private InteractionResult onRecordRecipe(Level level, Player player, BlockEntity blockEntity, RecipeManager recipeManager, ItemStack itemInHand) {
      if (blockEntity instanceof PotBlockEntity pot && pot.getStatus() == 0) {
         List<ItemStack> inputs = pot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
         if (inputs.isEmpty()) {
            return InteractionResult.PASS;
         } else {
            ItemStack recordStack = itemInHand.split(1);
            RecipeItem.RecipeResult recipeResult = this.getPotRecipeResult(level, recipeManager, pot, inputs, recordStack);
            setRecipe(recordStack, new RecipeItem.RecipeRecord(inputs, recipeResult.output(), POT, recipeResult.flexRecipe()));
            ItemUtils.getItemToLivingEntity(player, recordStack);
            return InteractionResult.SUCCESS;
         }
      } else if (blockEntity instanceof StockpotBlockEntity stockpot && stockpot.getStatus() == 1) {
         List<ItemStack> inputs = stockpot.getInputs().stream().filter(s -> !s.isEmpty()).toList();
         if (inputs.isEmpty()) {
            return InteractionResult.PASS;
         } else {
            ItemStack recordStack = itemInHand.split(1);
            RecipeItem.RecipeResult recipeResult = this.getStockpotRecipeResult(level, recipeManager, stockpot, inputs, recordStack);
            setRecipe(recordStack, new RecipeItem.RecipeRecord(inputs, recipeResult.output(), STOCKPOT, recipeResult.flexRecipe()));
            ItemUtils.getItemToLivingEntity(player, recordStack);
            return InteractionResult.SUCCESS;
         }
      } else {
         return InteractionResult.PASS;
      }
   }

   private RecipeItem.RecipeResult getPotRecipeResult(
      Level level, RecipeManager recipeManager, PotBlockEntity pot, List<ItemStack> inputs, ItemStack recordStack
   ) {
      SimpleInput container = pot.getContainer();
      Optional<RecipeHolder<PotRecipe>> potRecipe = recipeManager.getRecipeFor(ModRecipes.POT_RECIPE, container, level);
      if (potRecipe.isPresent()) {
         ItemStack assemble = ((PotRecipe)potRecipe.get().value()).assemble(container);
         return new RecipeItem.RecipeResult(assemble, false);
      } else {
         Optional<RecipeHolder<FlexPotRecipe>> flexPotRecipe = recipeManager.getRecipeFor(ModRecipes.FLEX_POT_RECIPE, container, level);
         if (flexPotRecipe.isPresent()) {
            RecipeHolder<FlexPotRecipe> recipe = flexPotRecipe.get();
            ItemStack result = ((FlexPotRecipe)recipe.value()).assemble(container);
            this.setQuality(level, inputs, ((FlexPotRecipe)recipe.value()).ingredients(), recipe.id().identifier(), result, recordStack);
            return new RecipeItem.RecipeResult(result, true);
         } else {
            ItemStack instance = FoodBiteRegistry.getItem(FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
            return new RecipeItem.RecipeResult(instance, false);
         }
      }
   }

   private RecipeItem.RecipeResult getStockpotRecipeResult(
      Level level, RecipeManager recipeManager, StockpotBlockEntity stockpot, List<ItemStack> inputs, ItemStack recordStack
   ) {
      StockpotInput container = stockpot.getInput();
      Optional<RecipeHolder<StockpotRecipe>> stockpotRecipe = recipeManager.getRecipeFor(ModRecipes.STOCKPOT_RECIPE, container, level);
      if (stockpotRecipe.isPresent()) {
         ItemStack assemble = ((StockpotRecipe)stockpotRecipe.get().value()).assemble(container);
         return new RecipeItem.RecipeResult(assemble, false);
      } else {
         Optional<RecipeHolder<FlexStockpotRecipe>> flexStockpotRecipe = recipeManager.getRecipeFor(ModRecipes.FLEX_STOCKPOT_RECIPE, container, level);
         if (flexStockpotRecipe.isPresent()) {
            RecipeHolder<FlexStockpotRecipe> recipe = flexStockpotRecipe.get();
            ItemStack result = ((FlexStockpotRecipe)recipe.value()).assemble(container);
            this.setQuality(level, inputs, ((FlexStockpotRecipe)recipe.value()).ingredients(), recipe.id().identifier(), result, recordStack);
            return new RecipeItem.RecipeResult(result, true);
         } else {
            return new RecipeItem.RecipeResult(Items.SUSPICIOUS_STEW.getDefaultInstance(), false);
         }
      }
   }

   private void setQuality(
      Level level, List<ItemStack> inputs, List<Ingredient> ingredients, Identifier recipeId, ItemStack result, ItemStack recordStack
   ) {
      if (level instanceof ServerLevel serverLevel) {
         Quality quality = QualityEvaluator.evaluate(inputs, ingredients, recipeId, serverLevel.getSeed());
         QualityUtils.setQuality(result, quality);
         QualityUtils.setQuality(recordStack, quality);
      }
   }

   public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
      if (hasRecipe(stack)) {
         RecipeItem.RecipeRecord recipe = getRecipe(stack);
         if (recipe == null) {
            return Optional.empty();
         } else {
            Quality quality = null;
            if (QualityUtils.hasQuality(stack)) {
               quality = QualityUtils.getQuality(stack);
            }

            return Optional.of(new RecipeItemTooltip(recipe, quality));
         }
      } else {
         return Optional.empty();
      }
   }

   public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.translatable("tooltip.kaleidoscope_cookery.recipe_item").withStyle(ChatFormatting.GRAY));
   }

   public record RecipeRecord(List<ItemStack> input, ItemStack output, Identifier type, boolean flexRecipe) {
      public static final Codec<RecipeItem.RecipeRecord> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ItemStack.OPTIONAL_CODEC.listOf().fieldOf("input").forGetter(RecipeItem.RecipeRecord::input),
               ItemStack.CODEC.fieldOf("output").forGetter(RecipeItem.RecipeRecord::output),
               Identifier.CODEC.fieldOf("type").forGetter(RecipeItem.RecipeRecord::type),
               Codec.BOOL.fieldOf("flex_recipe").forGetter(RecipeItem.RecipeRecord::flexRecipe)
            )
            .apply(instance, RecipeItem.RecipeRecord::new)
      );
      public static final StreamCodec<RegistryFriendlyByteBuf, RecipeItem.RecipeRecord> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, RecipeItem.RecipeRecord>() {
         public RecipeItem.RecipeRecord decode(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            List<ItemStack> inputs = Lists.newArrayList();

            for (int i = 0; i < size; i++) {
               inputs.add((ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
            }

            ItemStack output = (ItemStack)ItemStack.STREAM_CODEC.decode(buffer);
            Identifier type = buffer.readIdentifier();
            boolean flexRecipe = buffer.readBoolean();
            return new RecipeItem.RecipeRecord(inputs, output, type, flexRecipe);
         }

         public void encode(RegistryFriendlyByteBuf buffer, RecipeItem.RecipeRecord value) {
            buffer.writeVarInt(value.input().size());

            for (ItemStack s : value.input()) {
               ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, s);
            }

            ItemStack.STREAM_CODEC.encode(buffer, value.output());
            buffer.writeIdentifier(value.type());
            buffer.writeBoolean(value.flexRecipe());
         }
      };

      public static RecipeItem.RecipeRecord pot(ItemLike output, ItemLike... input) {
         List<ItemStack> inputList = Arrays.stream(input).<ItemStack>map(ItemStack::new).toList();
         return new RecipeItem.RecipeRecord(inputList, new ItemStack(output), RecipeItem.POT, false);
      }

      @SafeVarargs
      public static RecipeItem.RecipeRecord pot(DeferredItem<Item> output, DeferredItem<Item>... input) {
         List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack((ItemLike)s.get())).toList();
         return new RecipeItem.RecipeRecord(inputList, new ItemStack((ItemLike)output.get()), RecipeItem.POT, false);
      }

      public static RecipeItem.RecipeRecord stockpot(ItemLike output, ItemLike... input) {
         List<ItemStack> inputList = Arrays.stream(input).<ItemStack>map(ItemStack::new).toList();
         return new RecipeItem.RecipeRecord(inputList, new ItemStack(output), RecipeItem.STOCKPOT, false);
      }

      @SafeVarargs
      public static RecipeItem.RecipeRecord stockpot(DeferredItem<Item> output, DeferredItem<Item>... input) {
         List<ItemStack> inputList = Arrays.stream(input).map(s -> new ItemStack((ItemLike)s.get())).toList();
         return new RecipeItem.RecipeRecord(inputList, new ItemStack((ItemLike)output.get()), RecipeItem.STOCKPOT, false);
      }
   }

   private record RecipeResult(ItemStack output, boolean flexRecipe) {
   }
}
