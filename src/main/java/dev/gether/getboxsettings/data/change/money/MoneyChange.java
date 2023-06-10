package dev.gether.getboxsettings.data.change.money;

import org.bukkit.inventory.ItemStack;

public class MoneyChange {
    private ItemStack itemStack;
    private double price;

    public MoneyChange(ItemStack itemStack, double price) {
        this.itemStack = itemStack;
        this.price = price;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getPrice() {
        return price;
    }
}
