package dev.gether.getboxsettings.data.change.coins;

import dev.gether.getboxsettings.data.change.ChangeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CoinsManager {

    private final ChangeManager changeManager;
    public CoinsManager(ChangeManager changeManager)
    {
        this.changeManager = changeManager;
    }

    public void changeToCoin(Player player)
    {
        for(ItemCoinsChange itemCoinsChange : changeManager.getChangeList())
        {
            int amount = calcItem(player, itemCoinsChange.getFromItem());

            int finalAmount = amount/itemCoinsChange.getFromItem().getAmount();
            if(finalAmount<=0)
                continue;

            int getAmount = finalAmount*itemCoinsChange.getToItem().getAmount();
            removeItem(player, itemCoinsChange.getFromItem().clone(), finalAmount*itemCoinsChange.getFromItem().getAmount());
            addItem(player, itemCoinsChange.getToItem().clone(), getAmount);
        }
    }

    private void addItem(Player player, ItemStack itemStack, int amount)
    {
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
    }

    private void removeItem(Player player, ItemStack fromItem, int amount)
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

    private int calcItem(Player player, ItemStack fromItem)
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
