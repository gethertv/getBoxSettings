package dev.gether.getboxsettings.data.changeitem;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemChange implements Comparable<ItemChange> {


    private int priority;
    private ItemStack fromItem;
    private ItemStack toItem;
    private boolean convToMoney;

    public ItemChange(int priority, ItemStack fromItem, ItemStack toItem) {
        this.priority = priority;
        this.fromItem = fromItem;
        this.toItem = toItem;
    }

    public int getPriority() {
        return priority;
    }

    public ItemStack getFromItem() {
        return fromItem;
    }

    public ItemStack getToItem() {
        return toItem;
    }

    @Override
    public int compareTo(@NotNull ItemChange other) {
        return Integer.compare(this.priority, other.priority);
    }
}
