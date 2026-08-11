package com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender;
import net.minecraft.data.tags.TagsProvider.TagLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagItem extends ItemTagsProvider {
   public TagItem(
      PackOutput pOutput,
      CompletableFuture<Provider> pLookupProvider,
      CompletableFuture<TagLookup<Block>> pBlockTags,
      @Nullable ExistingFileHelper existingFileHelper
   ) {
      super(pOutput, pLookupProvider, pBlockTags, "kaleidoscope_cookery", existingFileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(TagMod.OIL).add((Item)ModItems.OIL.get());
      this.tag(TagMod.LIT_STOVE).add(new Item[]{Items.FLINT_AND_STEEL, Items.FIRE_CHARGE});
      this.tag(TagMod.STRAW_HAT).add(new Item[]{(Item)ModItems.STRAW_HAT.get(), (Item)ModItems.STRAW_HAT_FLOWER.get()});
      this.tag(TagMod.KITCHEN_KNIFE)
         .add(
            new Item[]{
               (Item)ModItems.IRON_KITCHEN_KNIFE.get(),
               (Item)ModItems.GOLD_KITCHEN_KNIFE.get(),
               (Item)ModItems.DIAMOND_KITCHEN_KNIFE.get(),
               (Item)ModItems.NETHERITE_KITCHEN_KNIFE.get()
            }
         )
         .addOptionalTag(Identifier.parse("farmersdelight:tools/knives"));
      this.tag(TagMod.KITCHEN_SHOVEL).add((Item)ModItems.KITCHEN_SHOVEL.get());
      this.tag(TagMod.CATERPILLARS).add((Item)ModItems.CATERPILLAR.get());
      this.tag(TagCommon.FD_KNIVES)
         .add(
            new Item[]{
               (Item)ModItems.IRON_KITCHEN_KNIFE.get(),
               (Item)ModItems.GOLD_KITCHEN_KNIFE.get(),
               (Item)ModItems.DIAMOND_KITCHEN_KNIFE.get(),
               (Item)ModItems.NETHERITE_KITCHEN_KNIFE.get()
            }
         );
      this.tag(TagMod.FARMER_ARMOR)
         .add(new Item[]{(Item)ModItems.FARMER_CHEST_PLATE.get(), (Item)ModItems.FARMER_LEGGINGS.get(), (Item)ModItems.FARMER_BOOTS.get()})
         .addTag(TagMod.STRAW_HAT);
      this.tag(TagMod.STRAW_BALE).add(new Item[]{Items.HAY_BLOCK, (Item)ModItems.STRAW_BLOCK.get()});
      this.tag(TagMod.COOKERY_MOD_SEEDS)
         .add(
            new Item[]{
               (Item)ModItems.TOMATO_SEED.get(), (Item)ModItems.CHILI_SEED.get(), (Item)ModItems.WILD_RICE_SEED.get(), (Item)ModItems.LETTUCE_SEED.get()
            }
         );
      this.tag(TagMod.INGREDIENT_CONTAINER).add(new Item[]{Items.BUCKET, Items.BOWL, Items.GLASS_BOTTLE});
      this.tag(TagMod.GLASS_BOTTLE_CONTAINER).add(Items.HONEY_BOTTLE);
      this.tag(TagMod.BUCKET_CONTAINER)
         .add(
            new Item[]{
               Items.WATER_BUCKET,
               Items.LAVA_BUCKET,
               Items.MILK_BUCKET,
               Items.SALMON_BUCKET,
               Items.COD_BUCKET,
               Items.TROPICAL_FISH_BUCKET,
               Items.PUFFERFISH_BUCKET,
               Items.AXOLOTL_BUCKET,
               Items.TADPOLE_BUCKET,
               Items.POWDER_SNOW_BUCKET
            }
         );
      IntrinsicTagAppender<Item> meal = this.tag(TagMod.MEALS);
      IntrinsicTagAppender<Item> feasts = this.tag(TagMod.FEASTS);
      BuiltInRegistries.ITEM.keySet().stream().filter(id -> id.getNamespace().equals("kaleidoscope_cookery")).forEach(id -> {
         Item item = (Item)BuiltInRegistries.ITEM.getValue(id);
         if (item != null) {
            ItemStack stack = item.getDefaultInstance();
            if (item.getFoodProperties(stack, null) != null) {
               meal.add(item);
               if (item instanceof BowlFoodBlockItem) {
                  feasts.add(item);
               }
            }
         }
      });
      this.addModItems();
      this.tag(TagMod.INGREDIENT_BLOCKLIST)
         .addTags(new TagKey[]{net.neoforged.neoforge.common.Tags.Items.TOOLS, net.neoforged.neoforge.common.Tags.Items.ARMORS})
         .add(
            new Item[]{
               Items.SHULKER_BOX,
               Items.WHITE_SHULKER_BOX,
               Items.ORANGE_SHULKER_BOX,
               Items.MAGENTA_SHULKER_BOX,
               Items.LIGHT_BLUE_SHULKER_BOX,
               Items.YELLOW_SHULKER_BOX,
               Items.LIME_SHULKER_BOX,
               Items.PINK_SHULKER_BOX,
               Items.GRAY_SHULKER_BOX,
               Items.LIGHT_GRAY_SHULKER_BOX,
               Items.CYAN_SHULKER_BOX,
               Items.PURPLE_SHULKER_BOX,
               Items.BLUE_SHULKER_BOX,
               Items.BROWN_SHULKER_BOX,
               Items.GREEN_SHULKER_BOX,
               Items.RED_SHULKER_BOX,
               Items.BLACK_SHULKER_BOX,
               Items.BUNDLE,
               Items.BUCKET,
               Items.MILK_BUCKET,
               Items.WATER_BUCKET,
               Items.LAVA_BUCKET,
               Items.POWDER_SNOW_BUCKET,
               Items.PUFFERFISH_BUCKET,
               Items.SALMON_BUCKET,
               Items.COD_BUCKET,
               Items.TROPICAL_FISH_BUCKET,
               Items.AXOLOTL_BUCKET,
               Items.TADPOLE_BUCKET,
               Items.BOWL,
               Items.GLASS_BOTTLE,
               (Item)ModItems.RECIPE_ITEM.get()
            }
         );
      this.tag(ItemTags.SHOVELS).add((Item)ModItems.KITCHEN_SHOVEL.get());
      this.tag(ItemTags.SWORDS).addTag(TagMod.KITCHEN_KNIFE).add((Item)ModItems.SICKLE.get());
      this.tag(TagMod.EXTINGUISH_STOVE).addTag(ItemTags.SHOVELS);
      this.tag(ItemTags.VILLAGER_PLANTABLE_SEEDS)
         .add(new Item[]{(Item)ModItems.TOMATO_SEED.get(), (Item)ModItems.CHILI_SEED.get(), (Item)ModItems.LETTUCE_SEED.get()});
      this.tag(net.neoforged.neoforge.common.Tags.Items.SEEDS)
         .add(
            new Item[]{
               (Item)ModItems.CHILI_SEED.get(),
               (Item)ModItems.TOMATO_SEED.get(),
               (Item)ModItems.LETTUCE_SEED.get(),
               (Item)ModItems.WILD_RICE_SEED.get(),
               (Item)ModItems.RICE_SEED.get()
            }
         );
      this.tag(net.neoforged.neoforge.common.Tags.Items.EGGS).add((Item)ModItems.FRIED_EGG.get());
      this.tag(TagCommon.CROPS_CHILI_PEPPER).add(new Item[]{(Item)ModItems.RED_CHILI.get(), (Item)ModItems.GREEN_CHILI.get()});
      this.tag(TagCommon.CROPS_TOMATO).add((Item)ModItems.TOMATO.get());
      this.tag(TagCommon.CROPS_LETTUCE).add((Item)ModItems.LETTUCE.get());
      this.tag(TagCommon.CROPS_RICE).add((Item)ModItems.RICE_SEED.get());
      this.tag(TagCommon.CROPS)
         .addTag(TagCommon.CROPS_CHILI_PEPPER)
         .addTag(TagCommon.CROPS_TOMATO)
         .addTag(TagCommon.CROPS_LETTUCE)
         .addTag(TagCommon.CROPS_RICE);
      this.tag(TagCommon.VEGETABLES_CHILI_PEPPER).add(new Item[]{(Item)ModItems.RED_CHILI.get(), (Item)ModItems.GREEN_CHILI.get()});
      this.tag(TagCommon.VEGETABLES_TOMATO).add((Item)ModItems.TOMATO.get());
      this.tag(TagCommon.VEGETABLES_LETTUCE).add((Item)ModItems.LETTUCE.get());
      this.tag(TagCommon.VEGETABLES)
         .addTag(TagCommon.VEGETABLES_CHILI_PEPPER)
         .addTag(TagCommon.VEGETABLES_TOMATO)
         .addTag(TagCommon.VEGETABLES_LETTUCE)
         .addOptionalTag(TagCommon.CROPS_CABBAGE);
      this.tag(TagCommon.SEEDS_CHILI_PEPPER).add((Item)ModItems.CHILI_SEED.get());
      this.tag(TagCommon.SEEDS_TOMATO).add((Item)ModItems.TOMATO_SEED.get());
      this.tag(TagCommon.SEEDS_LETTUCE).add((Item)ModItems.LETTUCE_SEED.get());
      this.tag(TagCommon.SEEDS_RICE).add((Item)ModItems.RICE_SEED.get());
      this.tag(TagCommon.GRAIN_RICE).add((Item)ModItems.RICE_SEED.get());
      this.tag(TagCommon.COOKED_BEEF).add(new Item[]{(Item)ModItems.COOKED_COW_OFFAL.get(), Items.COOKED_BEEF});
      this.tag(TagCommon.COOKED_PORK).add(new Item[]{(Item)ModItems.COOKED_PORK_BELLY.get(), Items.COOKED_PORKCHOP});
      this.tag(TagCommon.COOKED_MUTTON).add(new Item[]{(Item)ModItems.COOKED_LAMB_CHOPS.get(), Items.COOKED_MUTTON});
      this.tag(TagCommon.COOKED_EGGS).add((Item)ModItems.FRIED_EGG.get());
      this.tag(TagCommon.COOKED_RICE).add((Item)ModItems.COOKED_RICE.get()).addOptional(Identifier.parse("farmersdelight:cooked_rice"));
      this.tag(TagCommon.RAW_BEEF).add(new Item[]{(Item)ModItems.RAW_COW_OFFAL.get(), Items.BEEF});
      this.tag(TagCommon.RAW_CHICKEN).add(Items.CHICKEN);
      this.tag(TagCommon.RAW_PORK).add(new Item[]{(Item)ModItems.RAW_PORK_BELLY.get(), Items.PORKCHOP});
      this.tag(TagCommon.RAW_MUTTON).add(new Item[]{(Item)ModItems.RAW_LAMB_CHOPS.get(), Items.MUTTON});
      this.tag(net.neoforged.neoforge.common.Tags.Items.EGGS).add(new Item[]{Items.EGG, Items.TURTLE_EGG});
      this.tag(TagCommon.RAW_FISHES_TROPICAL).add((Item)ModItems.SASHIMI.get());
      this.tag(TagCommon.RAW_FISHES_COD).add(Items.COD);
      this.tag(TagCommon.RAW_FISHES_SALMON).add(Items.SALMON);
      this.tag(TagCommon.RAW_FISHES).addTag(TagCommon.RAW_FISHES_COD).addTag(TagCommon.RAW_FISHES_SALMON).addTag(TagCommon.RAW_FISHES_TROPICAL);
      this.tag(TagCommon.RAW_MEATS)
         .addTag(TagCommon.RAW_BEEF)
         .addTag(TagCommon.RAW_CHICKEN)
         .addTag(TagCommon.RAW_PORK)
         .addTag(TagCommon.RAW_MUTTON)
         .addTag(TagCommon.RAW_FISHES_COD)
         .addTag(TagCommon.RAW_FISHES_SALMON)
         .addTag(TagCommon.RAW_FISHES_TROPICAL)
         .add((Item)ModItems.RAW_CUT_SMALL_MEATS.get());
      this.tag(TagCommon.FLOUR).add((Item)ModItems.FLOUR.get());
      this.tag(TagCommon.DOUGHS);
      this.tag(TagCommon.FOODS_DOUGH);
      this.tag(TagCommon.DOUGH).add((Item)ModItems.RAW_DOUGH.get()).addTags(new TagKey[]{TagCommon.DOUGHS, TagCommon.FOODS_DOUGH});
      this.tag(TagCommon.GRAINS).add(new Item[]{(Item)ModItems.RICE_SEED.get(), (Item)ModItems.RICE_PANICLE.get()});
      this.tag(TagCommon.PROTEINS).add((Item)ModItems.RAW_CUT_SMALL_MEATS.get()).addTag(TagMod.CATERPILLARS);
      this.tag(TagCommon.DIET_VEGETABLES).addTag(TagCommon.VEGETABLES);
      this.tag(TagCommon.SPRING_CROPS).add((Item)ModItems.LETTUCE_SEED.get());
      this.tag(TagCommon.SUMMER_CROPS)
         .add(
            new Item[]{(Item)ModItems.TOMATO_SEED.get(), (Item)ModItems.CHILI_SEED.get(), (Item)ModItems.RICE_SEED.get(), (Item)ModItems.WILD_RICE_SEED.get()}
         );
      this.tag(TagCommon.AUTUMN_CROPS)
         .add(
            new Item[]{
               (Item)ModItems.TOMATO_SEED.get(),
               (Item)ModItems.CHILI_SEED.get(),
               (Item)ModItems.LETTUCE_SEED.get(),
               (Item)ModItems.RICE_SEED.get(),
               (Item)ModItems.WILD_RICE_SEED.get()
            }
         );
   }

   private void addModItems() {
      IntrinsicTagAppender<Item> modTags = this.tag(TagMod.COOKERY_MOD_ITEMS);

      for (Item item : BuiltInRegistries.ITEM) {
         Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
         if (itemId.getNamespace().equals("kaleidoscope_cookery")) {
            modTags.add(item);
         }
      }
   }
}
