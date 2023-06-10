package dev.gether.getboxsettings.data.change.block;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockChange {

    private ItemStack fromItemstack;
    private ItemStack toItemstack;

    public BlockChange(ItemStack fromItemstack, ItemStack toItemstack) {
        this.fromItemstack = fromItemstack;
        this.toItemstack = toItemstack;
    }

    public ItemStack getFromItemstack() {
        return fromItemstack;
    }

    public ItemStack getToItemstack() {
        return toItemstack;
    }
}
