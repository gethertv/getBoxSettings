package dev.gether.getboxsettings.data.change;

import dev.gether.getboxsettings.data.change.ChangeManager;
import dev.gether.getboxsettings.data.change.coins.ItemCoinsChange;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChangeFeatures {

    public void addItem(Player player, ItemStack itemStack, int amount)
    {
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
    }

    public void removeItem(Player player, ItemStack fromItem, int amount)
    {
        int removeItem = amount;


        for(ItemStack itemStack : player.getInventory()) {
            if(itemStack==null || itemStack.getType()== Material.AIR)
                continue;

            if(itemStack.isSimilar(fromItem))
            {
                if(removeItem<=0)
                    break;

                if(itemStack.getAmount()<=removeItem)
                {
                    removeItem-=itemStack.getAmount();
                    itemStack.setAmount(0);
                } else {
                    itemStack.setAmount(itemStack.getAmount()-removeItem);
                    removeItem=0;
                }
            }
        }
    }

    public int calcItem(Player player, ItemStack fromItem)
    {
        int amount = 0;
        for(ItemStack itemStack : player.getInventory())
        {
            if(itemStack==null || itemStack.getType()== Material.AIR)
                continue;

            if(itemStack.isSimilar(fromItem))
                amount+=itemStack.getAmount();
        }

        return amount;
    }
}
