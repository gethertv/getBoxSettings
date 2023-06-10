package dev.gether.getboxsettings.data.change.coins;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemCoinsChange implements Comparable<ItemCoinsChange> {


    private int priority;
    private ItemStack fromItem;
    private ItemStack toItem;
    private boolean convToMoney;
    private double price;

    public ItemCoinsChange(int priority, ItemStack fromItem, ItemStack toItem) {
        this.priority = priority;
        this.fromItem = fromItem;
        this.toItem = toItem;
        this.convToMoney = false;
        this.price = 0;
    }

    public ItemCoinsChange(int priority, ItemStack fromItem, ItemStack toItem, boolean convToMoney, double price) {
        this(priority, fromItem, toItem);
        this.convToMoney = convToMoney;
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isConvToMoney() {
        return convToMoney;
    }

    public double getPrice() {
        return price;
    }

    public ItemStack getFromItem() {
        return fromItem;
    }

    public ItemStack getToItem() {
        return toItem;
    }

    @Override
    public int compareTo(@NotNull ItemCoinsChange other) {
        return Integer.compare(this.priority, other.priority);
    }
}
