package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class KitchenShelfItemHandler extends ItemStackHandler {
    public static final int CAPACITY = 256;
    private final KitchenShelfBlockEntity blockEntity;

    public KitchenShelfItemHandler(KitchenShelfBlockEntity blockEntity) {
        super(1);
        this.blockEntity = blockEntity;
    }

    @Override
    public int getSlotLimit(int slot) {
        return CAPACITY;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return CAPACITY;
    }

    public void saveShelf(net.minecraft.world.level.storage.ValueOutput output) {
        ItemStack stack = getStackInSlot(0);
        output.store("item", ItemStack.OPTIONAL_CODEC, stack.copyWithCount(1));
        output.putInt("count", stack.getCount());
    }

    public void loadShelf(net.minecraft.world.level.storage.ValueInput input) {
        ItemStack item = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.stacks.set(0, item.copyWithCount(Math.clamp(input.getIntOr("count", 0), 0, CAPACITY)));
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.blockEntity.refresh();
    }
}
