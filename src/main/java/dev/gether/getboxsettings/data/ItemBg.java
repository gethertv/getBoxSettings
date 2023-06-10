package dev.gether.getboxsettings.data;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemBg {

    private ItemStack itemStack;
    private List<Integer> slots;

    public ItemBg(ItemStack itemStack, List<Integer> slots) {
        this.itemStack = itemStack;
        this.slots = slots;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<Integer> getSlots() {
        return slots;
    }
}
