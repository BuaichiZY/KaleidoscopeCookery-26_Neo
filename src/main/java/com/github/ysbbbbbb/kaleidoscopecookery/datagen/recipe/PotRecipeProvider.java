package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.FlexPotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PotRecipeProvider extends ModRecipeProvider {
   public PotRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries);
   }

   @Override
   public void buildRecipes(RecipeOutput consumer) {
      PotRecipeBuilder.builder()
         .addInput(
            TagCommon.CROPS_CHILI_PEPPER,
            TagCommon.CROPS_CHILI_PEPPER,
            TagCommon.DOUGH,
            TagCommon.DOUGH,
            ModItems.RAW_CUT_SMALL_MEATS.get(),
            ModItems.RAW_CUT_SMALL_MEATS.get()
         )
         .setResult((Item)ModItems.DONKEY_BURGER.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.COOKED_RICE)
         .setBowlCarrier()
         .setResult((Item)ModItems.EGG_FRIED_RICE.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(TagCommon.EGGS, TagCommon.EGGS, TagCommon.EGGS, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO, TagCommon.CROPS_TOMATO)
         .setBowlCarrier()
         .setResult((Item)ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, TagCommon.RAW_BEEF, ModItems.RED_CHILI.get(), ModItems.RED_CHILI.get(), ModItems.RED_CHILI.get())
         .setBowlCarrier()
         .setResult((Item)ModItems.BRAISED_BEEF.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get(), ModItems.GREEN_CHILI.get())
         .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
         .setBowlCarrier()
         .setResult((Item)ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(Items.SUGAR, Items.SUGAR, Items.SUGAR)
         .addInput(TagCommon.RAW_PORK, TagCommon.RAW_PORK, TagCommon.RAW_PORK)
         .setBowlCarrier()
         .setResult((Item)ModItems.SWEET_AND_SOUR_PORK.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(
            net.neoforged.neoforge.common.Tags.Items.MUSHROOMS,
            net.neoforged.neoforge.common.Tags.Items.MUSHROOMS,
            TagCommon.RAW_PORK,
            TagCommon.RAW_PORK,
            TagCommon.CROPS_CHILI_PEPPER,
            TagCommon.CROPS_CHILI_PEPPER
         )
         .setBowlCarrier()
         .setResult((Item)ModItems.FISH_FLAVORED_SHREDDED_PORK.get())
         .save(consumer);
      PotRecipeBuilder.builder()
         .addInput(Items.LEATHER, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR)
         .setResult((Item)ModItems.STICKY_CANDY.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.CROPS_CHILI_PEPPER, TagCommon.DOUGH, ModItems.RAW_CUT_SMALL_MEATS.get())
         .setResult((Item)ModItems.DONKEY_BURGER.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.EGGS, TagCommon.COOKED_RICE)
         .setBowlCarrier()
         .setResult((Item)ModItems.EGG_FRIED_RICE.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.EGGS, TagCommon.CROPS_TOMATO)
         .setBowlCarrier()
         .setResult((Item)ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.RAW_BEEF, TagCommon.CROPS_CHILI_PEPPER)
         .setBowlCarrier()
         .setResult((Item)ModItems.BRAISED_BEEF.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER)
         .setBowlCarrier()
         .setResult((Item)ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.RAW_PORK, Items.SUGAR)
         .setBowlCarrier()
         .setResult((Item)ModItems.SWEET_AND_SOUR_PORK.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder()
         .addInput(TagCommon.RAW_PORK, TagCommon.CROPS_CHILI_PEPPER, net.neoforged.neoforge.common.Tags.Items.MUSHROOMS)
         .setBowlCarrier()
         .setResult((Item)ModItems.FISH_FLAVORED_SHREDDED_PORK.get())
         .save(consumer);
      FlexPotRecipeBuilder.builder().addInput(Items.LEATHER, Items.SUGAR).setResult((Item)ModItems.STICKY_CANDY.get()).save(consumer);
   }
}
