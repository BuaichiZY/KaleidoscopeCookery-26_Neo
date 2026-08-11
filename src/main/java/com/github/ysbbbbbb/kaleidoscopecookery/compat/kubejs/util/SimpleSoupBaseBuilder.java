package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.util;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SimpleSoupBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

@Info("A builder for simple soup base, used in KubeJS. <br>\n简易汤底的构建器，用于 KubeJS\n")
public final class SimpleSoupBaseBuilder {
   private final Identifier name;
   private ItemStack displayStack = Items.APPLE.getDefaultInstance();
   private Identifier soupBaseTexture = StockpotVisuals.DEFAULT_COOKING_TEXTURE;
   private int bubbleColor = 16772291;
   private Predicate<ItemStack> soupBasePredicate = stack -> false;
   private Predicate<ItemStack> containerPredicate = stack -> false;
   private TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction = (level, entity, stack) -> ItemStack.EMPTY;
   private TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction = (level, entity, stack) -> ItemStack.EMPTY;

   private SimpleSoupBaseBuilder(Identifier name) {
      this.name = name;
   }

   @Info("Create a soup base, need to provide an id, this id will be used in subsequent stockpot recipes. <br>\n创建一个汤底，需要填写 id，这个 id 将用于后续汤锅配方。\n")
   public static SimpleSoupBaseBuilder create(Identifier name) {
      return new SimpleSoupBaseBuilder(name);
   }

   @Info("The item used to display the soup base in the JEI UI. <br>\n在 JEI 界面中用于显示汤底的物品。\n")
   public SimpleSoupBaseBuilder displayStack(ItemStack displayStack) {
      this.displayStack = displayStack;
      return this;
   }

   @Info(
      "The texture used to render the soup base in the soup pot. Supports dynamic textures. The texture needs to be registered in atlases/block.json. <br>\n在汤锅中用于渲染汤底的材质，支持动态材质。材质需要在 atlases/block.json 中注册。\n"
   )
   public SimpleSoupBaseBuilder soupBaseTexture(Identifier soupBaseTexture) {
      this.soupBaseTexture = soupBaseTexture;
      return this;
   }

   @Info("The color of the bubble particles when the soup base is first added to the soup pot. <br>\n刚放入汤底时，汤锅中气泡粒子的颜色。\n")
   public SimpleSoupBaseBuilder bubbleColor(int bubbleColor) {
      this.bubbleColor = bubbleColor;
      return this;
   }

   @Info("A predicate to test whether an item is the soup base. <br>\n当玩家将物品放入汤锅时，判断该物品是否为该汤底的物品。\n")
   public SimpleSoupBaseBuilder soupBasePredicate(Predicate<ItemStack> soupBasePredicate) {
      this.soupBasePredicate = soupBasePredicate;
      return this;
   }

   @Info("A predicate to test whether an item is a valid container to retrieve the soup base. <br>\n当玩家将物品放入汤锅时，判断该物品是否为取回汤底的有效容器。\n")
   public SimpleSoupBaseBuilder containerPredicate(Predicate<ItemStack> containerPredicate) {
      this.containerPredicate = containerPredicate;
      return this;
   }

   @Info(
      "When soupBasePredicate tests true, this function is called to get the item returned to the player. <br>\n当 soupBasePredicate 判断汤底物品符合时，调用该函数获取返回给玩家的物品。\n"
   )
   public SimpleSoupBaseBuilder returnContainerFunction(TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnContainerFunction) {
      this.returnContainerFunction = returnContainerFunction;
      return this;
   }

   @Info(
      "When containerPredicate tests true, this function is called to get the item returned to the player. <br>\n当 containerPredicate 判断容器物品符合时，调用该函数获取返回给玩家的物品。\n"
   )
   public SimpleSoupBaseBuilder returnSoupBaseFunction(TriFunction<Level, LivingEntity, ItemStack, ItemStack> returnSoupBaseFunction) {
      this.returnSoupBaseFunction = returnSoupBaseFunction;
      return this;
   }

   @HideFromJS
   public SimpleSoupBase build() {
      return new SimpleSoupBase(
         this.name,
         this.displayStack,
         this.soupBaseTexture,
         this.bubbleColor,
         this.soupBasePredicate,
         this.containerPredicate,
         this.returnContainerFunction,
         this.returnSoupBaseFunction
      );
   }
}
