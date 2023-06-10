package dev.gether.getboxsettings.listeners;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.change.ChangeManager;
import dev.gether.getboxsettings.data.user.User;
import dev.gether.getboxsettings.data.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClickInv(InventoryClickEvent event)
    {
        if(event.getClickedInventory()==null)
            return;

        Player player = (Player) event.getWhoClicked();

        ChangeManager changeManager = GetBoxSettings.getInstance().getChangeManager();
        Inventory inventory = changeManager.getAdminChangeInv().get(player.getUniqueId());
        if(inventory!=null)
        {
            if(event.getInventory().equals(inventory))
            {
                if(event.getClickedInventory().equals(inventory))
                {
                    if(event.getSlot()==12 || event.getSlot()==14)
                        return;

                    event.setCancelled(true);
                    if(event.getSlot()==26)
                    {
                        if(event.getInventory().getItem(12)==null || event.getInventory().getItem(14)==null)
                            return;

                        ItemStack fromItem = event.getInventory().getItem(12);
                        ItemStack toItem = event.getInventory().getItem(14);
                        changeManager.createItemsTrade(fromItem, toItem, player);
                        return;
                    }
                }
                return;
            }
        }
        Inventory adminMoneyInv = changeManager.getAdminMoneyCreateInv().get(player.getUniqueId());
        if(adminMoneyInv!=null)
        {
            if(event.getInventory().equals(adminMoneyInv))
            {
                if(event.getClickedInventory().equals(adminMoneyInv))
                {
                    if(event.getSlot()==13)
                        return;

                    event.setCancelled(true);
                    if(event.getSlot()==26)
                    {
                        if(event.getInventory().getItem(13)==null)
                            return;

                        ItemStack fromItem = event.getInventory().getItem(13);
                        changeManager.createMoneyTrade(fromItem, player);
                        return;
                    }
                }
                return;
            }
        }

        User user = GetBoxSettings.getInstance().getUserManager().getUserData().get(player.getUniqueId());
        if(user==null)
            return;

        if(event.getInventory().equals(user.getSettingsInv()))
        {
            event.setCancelled(true);
            if(event.getClickedInventory().equals(user.getSettingsInv()))
            {
                UserManager userManager = GetBoxSettings.getInstance().getUserManager();
                if(userManager.BLOCK_CONV_ENABLE)
                {
                    if(userManager.BLOCK_CONV_SLOT==event.getSlot())
                    {
                        user.changeStatusBlockConv();
                        userManager.updateBlockConv(user.getSettingsInv(), event.getSlot(), user.isEnableBlockConv());
                        return;
                    }
                }
                if(userManager.MONEY_CONV_ENABLE)
                {
                    if(userManager.MONEY_CONV_SLOT==event.getSlot())
                    {
                        user.changeStatusMoneyConv();
                        userManager.updateBlockConv(user.getSettingsInv(), event.getSlot(), user.isEnableMoneyConv());
                        return;
                    }
                }
                if(userManager.SELL_MONEY_ENABLE)
                {
                    if(userManager.SELL_MONEY_SLOT==event.getSlot())
                    {
                        user.changeStatusSellMoney();
                        userManager.updateBlockConv(user.getSettingsInv(), event.getSlot(), user.isEnableSellMoney());
                        return;
                    }
                }

                if(userManager.ACTION_BAR_ENABLE)
                {
                    if(userManager.ACTION_BAR_SLOT==event.getSlot())
                    {
                        user.changeActionBarStatus();
                        userManager.updateBlockConv(user.getSettingsInv(), event.getSlot(), user.isActionBarEnable());
                        return;
                    }
                }
            }
        }
    }
}
