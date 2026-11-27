package cookery.regression;

import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.ChoppingBoardDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.item.TransmutationLunchBagItem;
import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.util.FakePlayer;

public final class Version160Regression {
    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError("1.6.0: " + message);
    }
    private static final class Dispenser extends ChoppingBoardDispenseBehavior {
        Dispenser(boolean knife) { super(knife); }
        ItemStack run(BlockSource source, ItemStack stack) { return execute(source, stack); }
    }
    public static void verify(ServerLevel level, FakePlayer player) {
        player.setGameMode(GameType.SURVIVAL);
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        var pos = new BlockPos(5, 120, 5);
        level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 3);
        var state = ModBlocks.CHOPPING_BOARD.get().defaultBlockState();
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(pos, state, 3);
        var board = (ChoppingBoardBlockEntity) level.getBlockEntity(pos);
        var food = new ItemStack(Items.CHICKEN, 4);
        var dispenserPos = pos.west();
        var dispenserState = Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, Direction.EAST);
        level.setBlock(dispenserPos, dispenserState, 3);
        var source = new BlockSource(level, dispenserPos, dispenserState, (DispenserBlockEntity) level.getBlockEntity(dispenserPos));
        new Dispenser(false).run(source, food);
        check(food.getCount() == 3 && !board.getCurrentCutStack().isEmpty(), "dispenser must consume exactly one chicken");
        new Dispenser(false).run(source, food);
        check(food.getCount() == 3, "occupied board must not consume ingredient");
        var knife = new ItemStack(ModItems.IRON_KITCHEN_KNIFE.get());
        knife.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.QUICK_KNIFE), 2);
        new Dispenser(true).run(source, knife);
        check(board.getCurrentCutCount() == 4 && board.getMaxCutCount() == 4, "quick knife II must finish four cuts in one action");
        var snapshot = board.saveWithoutMetadata(level.registryAccess());
        var restored = new ChoppingBoardBlockEntity(pos, state);
        restored.setLevel(level);
        restored.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), snapshot));
        var area = new AABB(pos).inflate(1);
        level.getEntitiesOfClass(ItemEntity.class, area).forEach(e -> e.discard());
        check(restored.onCutItem(level, null, knife), "restored multi-output board failed");
        int bones = 0, meats = 0;
        for (var entity : level.getEntitiesOfClass(ItemEntity.class, area)) {
            var stack = entity.getItem();
            if (stack.is(Items.BONE)) bones += stack.getCount();
            if (stack.is(ModItems.RAW_CUT_SMALL_MEATS.get())) meats += stack.getCount();
        }
        check(bones == 1 && meats == 3, "all chopping outputs must survive save/load: " + bones + "/" + meats);
        var old = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
        old.putInt("MaxCutCount", 4); old.putInt("CurrentCutCount", 4);
        old.store("ResultItem", ItemStack.OPTIONAL_CODEC, new ItemStack(Items.BONE, 2));
        restored.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), old.buildResult()));
        check(restored.onCutItem(level, null, knife), "legacy single-output board save must load");
        level.getEntitiesOfClass(ItemEntity.class, area).forEach(e -> e.discard());

        var bag = new ItemStack(ModItems.TRANSMUTATION_LUNCH_BAG.get());
        var items = TransmutationLunchBagItem.getItems(bag);
        check(items.getSlots() == 24, "lunch bag must have 24 slots");
        items.setStackInSlot(0, new ItemStack(Items.BREAD, 5));
        var potion = new ItemStack(Items.POTION);
        potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.SWIFTNESS));
        items.setStackInSlot(1, potion);
        TransmutationLunchBagItem.setItems(bag, items);
        player.getFoodData().setFoodLevel(10);
        bag.getItem().finishUsingItem(bag, level, player);
        var after = TransmutationLunchBagItem.getItems(bag);
        check(player.getFoodData().getFoodLevel() == 20 && after.getStackInSlot(0).getCount() == 3,
            "lunch bag must eat sequentially until full");
        check(after.getStackInSlot(1).isEmpty() && player.hasEffect(net.minecraft.world.effect.MobEffects.SPEED),
            "lunch bag must actually consume potion and apply its effect");
        player.removeAllEffects();
        var ops = RegistryOps.create(JsonOps.INSTANCE, level.registryAccess());
        var legacy = new ArrayList<ItemStack>();
        for (int i = 0; i < 16; i++) legacy.add(i == 15 ? new ItemStack(Items.APPLE, 7) : ItemStack.EMPTY);
        var json = ItemStack.OPTIONAL_CODEC.listOf().encodeStart(ops, legacy).getOrThrow();
        var migrated = TransmutationLunchBagItem.ItemContainer.CODEC.parse(ops, json).getOrThrow().items();
        check(migrated.getSlots() == 24 && migrated.getStackInSlot(15).getCount() == 7, "legacy bag inventory migration");

        check(ModBlocks.MILLSTONE.get().defaultBlockState().is(net.minecraft.tags.TagKey.create(Registries.BLOCK,
            net.minecraft.resources.Identifier.parse("carryon:block_blacklist"))), "Carry On blacklist must load without optional furniture mods");
        var millPos = pos.offset(0, 0, 5);
        level.setBlock(millPos, ModBlocks.MILLSTONE.get().defaultBlockState(), 3);
        var mill = (MillstoneBlockEntity) level.getBlockEntity(millPos);
        check(mill != null && !mill.canBindEntity(player), "default millstone blacklist must reject players");
        var cow = (net.minecraft.world.entity.LivingEntity) net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.parse("minecraft:cow")).create(level, EntitySpawnReason.COMMAND);
        cow.setPos(Vec3.atCenterOf(millPos));
        level.addFreshEntity(cow);
        check(mill.canBindEntity(cow), "untamed adult animals must be bindable");
        mill.bindEntity(cow);
        check(mill.isBoundTo(cow), "millstone bind");
        cow.hurtServer(level, level.damageSources().generic(), 1);
        check(!mill.isBoundTo(cow), "damage must unbind millstone animal");
        cow.discard();
        var chicken = (net.minecraft.world.entity.LivingEntity) net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.parse("minecraft:chicken")).create(level, EntitySpawnReason.COMMAND);
        check(!mill.canBindEntity(chicken), "short animal must not bind");
        check(com.github.ysbbbbbb.kaleidoscopecookery.datamap.MillstoneBindableData.DEFAULT.rotSpeedTick() == 500,
            "default millstone period");

        var origin = pos.offset(25, 0, 0);
        for (int distance : new int[] {0, 5, 6}) {
            var target = origin.east(distance);
            level.setBlock(target.below(), Blocks.DIRT.defaultBlockState(), 3);
            level.setBlock(target, Blocks.SHORT_GRASS.defaultBlockState(), 3);
        }
        var sickle = new ItemStack(ModItems.SICKLE.get());
        sickle.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.SWEEP), 3);
        player.setPos(Vec3.atCenterOf(origin));
        player.setItemSlot(EquipmentSlot.MAINHAND, sickle);
        sickle.getItem().useOn(new UseOnContext(player, InteractionHand.MAIN_HAND,
            new BlockHitResult(Vec3.atCenterOf(origin), Direction.UP, origin, false)));
        check(level.getBlockState(origin.east(5)).isAir() && !level.getBlockState(origin.east(6)).isAir(), "sweep III radius must be 5");
        check(sickle.getDamageValue() == 8, "sweep III durability must cost 8");
        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }
}
