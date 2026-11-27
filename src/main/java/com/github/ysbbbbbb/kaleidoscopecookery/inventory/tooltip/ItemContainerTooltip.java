package com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler.IItemHandler;

public record ItemContainerTooltip(IItemHandler handler) implements TooltipComponent {
}
