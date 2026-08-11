package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeacupRegistry {
   public static final Map<Identifier, TeacupRegistry.TeacupData> TEACUP_DATA_MAP = Maps.newLinkedHashMap();
   public static Identifier BARLEY_TEA;
   public static Identifier TIEGUANYIN;
   public static Identifier BILUOCHUN;
   public static Identifier OOLONG;
   public static Identifier SAKURA_FUBUKI;
   public static Identifier FLOWER_TEA;

   public static void init() {
      TeacupRegistry registry = new TeacupRegistry();
      BARLEY_TEA = registry.registerTeacupData(
         "barley_tea", TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(ModEffects.VITALITY, 9600))
      );
      TIEGUANYIN = registry.registerTeacupData(
         "tieguanyin", TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(ModEffects.INSTANT_SMELTING, 2400))
      );
      BILUOCHUN = registry.registerTeacupData(
         "biluochun", TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(ModEffects.PROJECTILE_DODGE, 2400))
      );
      OOLONG = registry.registerTeacupData(
         "oolong",
         TeacupRegistry.TeacupData.create(4)
            .addEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 7200))
            .addEffect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 7200))
      );
      SAKURA_FUBUKI = registry.registerTeacupData(
         "sakura_fubuki", TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(ModEffects.HINDER, 7200))
      );
      FLOWER_TEA = registry.registerTeacupData(
         "flower_tea", TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400))
      );
   }

   public Identifier registerTeacupData(Identifier id, TeacupRegistry.TeacupData data) {
      TEACUP_DATA_MAP.put(id, data);
      return id;
   }

   public Identifier registerTeacupData(String name, TeacupRegistry.TeacupData data) {
      return this.registerTeacupData(Identifier.fromNamespaceAndPath("kaleidoscope_cookery", name), data);
   }

   public static Item getItem(Identifier name) {
      return (Item)BuiltInRegistries.ITEM.getValue(name);
   }

   public static Block getBlock(Identifier name) {
      return (Block)BuiltInRegistries.BLOCK.getValue(name);
   }

   public static final class TeacupData {
      private final int maxCount;
      private final List<Pair<Supplier<MobEffectInstance>, Float>> effects = Lists.newArrayList();
      @Nullable
      private VoxelShape aabb = null;

      private TeacupData(int maxCount) {
         this.maxCount = maxCount;
      }

      public static TeacupRegistry.TeacupData create(int maxCount) {
         return new TeacupRegistry.TeacupData(maxCount);
      }

      public TeacupRegistry.TeacupData addEffect(Supplier<MobEffectInstance> effect, float probability) {
         this.effects.add(Pair.of(effect, probability));
         return this;
      }

      public TeacupRegistry.TeacupData addEffect(Supplier<MobEffectInstance> effect) {
         return this.addEffect(effect, 1.0F);
      }

      public TeacupRegistry.TeacupData setAABB(VoxelShape aabb) {
         this.aabb = aabb;
         return this;
      }

      public int getMaxCount() {
         return this.maxCount;
      }

      @Nullable
      public VoxelShape getAABB() {
         return this.aabb;
      }

      public List<Pair<Supplier<MobEffectInstance>, Float>> getEffects() {
         return this.effects;
      }
   }
}
