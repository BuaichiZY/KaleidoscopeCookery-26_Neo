package com.github.ysbbbbbb.kaleidoscopecookery.event;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModVillager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = "kaleidoscope_cookery")
public class ModTradesEvent {
   @SubscribeEvent
   public static void onTradesEvent(VillagerTradesEvent event) {
      if (event.getType() == ModVillager.CHEF.get()) {
         addNoviceTrades(event);
         addApprenticeTrades(event);
         addJourneymanTrades(event);
         addExpertTrades(event);
         addMasterTrades(event);
      }
   }

   private static void addNoviceTrades(VillagerTradesEvent event) {
      addTrade(event, 1, ModItems.TOMATO.get(), 12, Items.EMERALD, 1, 16, 2, 0.05F);
      addTrade(event, 1, ModItems.LETTUCE.get(), 16, Items.EMERALD, 1, 16, 2, 0.05F);
      addTrade(event, 1, ModItems.RICE_SEED.get(), 20, Items.EMERALD, 1, 16, 2, 0.05F);
      addTrade(event, 1, ModItems.RED_CHILI.get(), 12, Items.EMERALD, 1, 16, 2, 0.05F);
      addTrade(event, 1, ModItems.GREEN_CHILI.get(), 8, Items.EMERALD, 1, 12, 2, 0.1F);
      addTrade(event, 1, ModItems.CATERPILLAR.get(), 1, Items.EMERALD, 1, 12, 2, 0.1F);
   }

   private static void addApprenticeTrades(VillagerTradesEvent event) {
      addTrade(event, 2, Items.EMERALD, 4, ModItems.KITCHEN_SHOVEL.get(), 1, 12, 4, 0.1F);
      addTrade(event, 2, Items.EMERALD, 8, ModItems.IRON_KITCHEN_KNIFE.get(), 1, 12, 4, 0.2F);
      addTrade(event, 2, Items.EMERALD, 3, ModItems.STOCKPOT_LID.get(), 1, 16, 4, 0.1F);
      addTrade(
         event,
         2,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.pot(
            ModItems.SCRAMBLE_EGG_WITH_TOMATOES, ModItems.FRIED_EGG, ModItems.FRIED_EGG, ModItems.FRIED_EGG, ModItems.TOMATO, ModItems.TOMATO, ModItems.TOMATO
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         2,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.pot(ModItems.BRAISED_BEEF, ModItems.RAW_COW_OFFAL, ModItems.RAW_COW_OFFAL, ModItems.GREEN_CHILI, ModItems.GREEN_CHILI),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         2,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.pot(
            (ItemLike)ModItems.SWEET_AND_SOUR_PORK.get(), Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.PORKCHOP, Items.PORKCHOP, Items.PORKCHOP
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         2,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.pot(
            (ItemLike)ModItems.FISH_FLAVORED_SHREDDED_PORK.get(),
            Items.BROWN_MUSHROOM,
            Items.BROWN_MUSHROOM,
            Items.PORKCHOP,
            Items.PORKCHOP,
            Items.PORKCHOP,
            (ItemLike)ModItems.GREEN_CHILI.get()
         ),
         16,
         4,
         0.1F
      );
   }

   private static void addJourneymanTrades(VillagerTradesEvent event) {
      addTrade(event, 3, FoodBiteRegistry.getItem(FoodBiteRegistry.DARK_CUISINE), 5, Items.EMERALD, 2, 16, 5, 0.1F);
      addTrade(
         event,
         3,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.stockpot(
            (ItemLike)ModItems.PUFFERFISH_SOUP.get(), Items.PUFFERFISH, Items.PUFFERFISH, Items.PUFFERFISH, Items.SEAGRASS, Items.SEAGRASS
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         3,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.stockpot(
            (ItemLike)ModItems.BORSCHT.get(),
            Items.BEEF,
            Items.BEEF,
            (ItemLike)ModItems.TOMATO.get(),
            (ItemLike)ModItems.TOMATO.get(),
            (ItemLike)ModItems.LETTUCE.get()
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         3,
         Items.EMERALD,
         3,
         RecipeItem.RecipeRecord.stockpot(
            (ItemLike)ModItems.BRAISED_BEEF_WITH_POTATOES.get(), Items.BEEF, Items.BEEF, Items.BEEF, Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO
         ),
         16,
         4,
         0.1F
      );
   }

   private static void addExpertTrades(VillagerTradesEvent event) {
      addTrade(event, 4, ModItems.PORK_BONE_SOUP.get(), 1, Items.EMERALD, 1, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.PUFFERFISH_SOUP.get(), 1, Items.EMERALD, 4, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.SEAFOOD_MISO_SOUP.get(), 1, Items.EMERALD, 1, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.LAMB_AND_RADISH_SOUP.get(), 1, Items.EMERALD, 2, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.BRAISED_BEEF_WITH_POTATOES.get(), 1, Items.EMERALD, 2, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.WILD_MUSHROOM_RABBIT_SOUP.get(), 1, Items.EMERALD, 3, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.BORSCHT.get(), 1, Items.EMERALD, 2, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.BEEF_MEATBALL_SOUP.get(), 1, Items.EMERALD, 3, 16, 10, 0.1F);
      addTrade(event, 4, ModItems.FEARSOME_THICK_SOUP.get(), 1, Items.EMERALD, 5, 16, 10, 0.1F);
      addTrade(
         event,
         4,
         Items.EMERALD,
         5,
         RecipeItem.RecipeRecord.pot(
            FoodBiteRegistry.getItem(FoodBiteRegistry.DONGPO_PORK),
            Items.BAMBOO,
            Items.BAMBOO,
            Items.PORKCHOP,
            Items.PORKCHOP,
            Items.PORKCHOP,
            Items.PORKCHOP,
            Items.PORKCHOP,
            Items.PORKCHOP
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         4,
         Items.EMERALD,
         5,
         RecipeItem.RecipeRecord.pot(
            FoodBiteRegistry.getItem(FoodBiteRegistry.STARGAZY_PIE), Items.COD, Items.COD, Items.COD, Items.COD, Items.COD, Items.PUMPKIN_PIE
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         4,
         Items.EMERALD,
         5,
         RecipeItem.RecipeRecord.pot(
            FoodBiteRegistry.getItem(FoodBiteRegistry.NETHER_STYLE_SASHIMI),
            Items.CRIMSON_FUNGUS,
            Items.CRIMSON_FUNGUS,
            Items.WARPED_FUNGUS,
            Items.WARPED_FUNGUS,
            Items.TROPICAL_FISH,
            Items.TROPICAL_FISH,
            Items.TROPICAL_FISH,
            Items.TROPICAL_FISH
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         4,
         Items.EMERALD,
         5,
         RecipeItem.RecipeRecord.pot(
            FoodBiteRegistry.getItem(FoodBiteRegistry.SLIME_BALL_MEAL), Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL, Items.SLIME_BALL
         ),
         16,
         4,
         0.1F
      );
      addTrade(
         event,
         4,
         Items.EMERALD,
         5,
         RecipeItem.RecipeRecord.pot(
            FoodBiteRegistry.getItem(FoodBiteRegistry.SPICY_CHICKEN),
            (ItemLike)ModItems.RED_CHILI.get(),
            (ItemLike)ModItems.RED_CHILI.get(),
            (ItemLike)ModItems.RED_CHILI.get(),
            (ItemLike)ModItems.RED_CHILI.get(),
            (ItemLike)ModItems.RED_CHILI.get(),
            Items.CHICKEN,
            Items.CHICKEN,
            Items.CHICKEN,
            Items.CHICKEN
         ),
         16,
         4,
         0.1F
      );
   }

   private static void addMasterTrades(VillagerTradesEvent event) {
      ((List)event.getTrades().get(5)).add(new ModTradesEvent.EnchantedItemForEmeralds((Item)ModItems.DIAMOND_KITCHEN_KNIFE.get(), 8, 3, 30, 0.2F));
   }

   private static void addTrade(
      VillagerTradesEvent event,
      int level,
      Object inputItem,
      int inputCount,
      Object outputItem,
      int outputCount,
      int maxUses,
      int villagerXp,
      float priceMultiplier
   ) {
      ItemStack input = createItemStack(inputItem, inputCount);
      ItemStack output = createItemStack(outputItem, outputCount);
      BasicItemListing listing = new BasicItemListing(input, output, maxUses, villagerXp, priceMultiplier);
      ((List)event.getTrades().get(level)).add(listing);
   }

   private static void addTrade(
      VillagerTradesEvent event,
      int level,
      Object inputItem,
      int inputCount,
      RecipeItem.RecipeRecord record,
      int maxUses,
      int villagerXp,
      float priceMultiplier
   ) {
      ItemStack input = createItemStack(inputItem, inputCount);
      ItemStack output = ((Item)ModItems.RECIPE_ITEM.get()).getDefaultInstance();
      RecipeItem.setRecipe(output, record);
      BasicItemListing listing = new BasicItemListing(input, output, maxUses, villagerXp, priceMultiplier);
      ((List)event.getTrades().get(level)).add(listing);
   }

   private static ItemStack createItemStack(Object item, int count) {
      if (item instanceof Item itemInstance) {
         return new ItemStack(itemInstance, count);
      } else if (item instanceof DeferredItem<?> registryObject) {
         return new ItemStack((ItemLike)registryObject.get(), count);
      } else if (item instanceof ItemStack itemStack) {
         return itemStack.copyWithCount(count);
      } else {
         throw new IllegalArgumentException("Unsupported item type: " + item.getClass());
      }
   }

   private static class EnchantedItemForEmeralds implements ItemListing {
      private final ItemStack itemStack;
      private final int baseEmeraldCost;
      private final int maxUses;
      private final int villagerXp;
      private final float priceMultiplier;

      public EnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier) {
         this.itemStack = new ItemStack(item);
         this.baseEmeraldCost = baseEmeraldCost;
         this.maxUses = maxUses;
         this.villagerXp = villagerXp;
         this.priceMultiplier = priceMultiplier;
      }

      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i = 5 + random.nextInt(15);
         RegistryAccess registryaccess = trader.level().registryAccess();
         Optional<Named<Enchantment>> optional = registryaccess.registryOrThrow(Registries.ENCHANTMENT).getTag(EnchantmentTags.ON_TRADED_EQUIPMENT);
         ItemStack itemstack = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), i, registryaccess, optional);
         int j = Math.min(this.baseEmeraldCost + i, 64);
         ItemCost itemcost = new ItemCost(Items.EMERALD, j);
         return new MerchantOffer(itemcost, itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
      }
   }
}
