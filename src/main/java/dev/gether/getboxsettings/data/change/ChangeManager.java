package dev.gether.getboxsettings.data.changeitem;

import dev.gether.getboxsettings.file.ChangeFile;
import dev.gether.getboxsettings.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChangeManager {


    private HashMap<UUID, Inventory> adminChangeInv = new HashMap<>();
    private List<ItemChange> changeList = new ArrayList<>();

    public ChangeManager()
    {
        FileConfiguration config = ChangeFile.getConfig();
        for(String key : ChangeFile.getConfig().getConfigurationSection("change-item").getKeys(false))
        {
            int priority = config.getInt("change-item."+key+".priority");
            ItemStack fromItem = config.getItemStack("change-item."+key+".from-item");
            ItemStack toItem = config.getItemStack("change-item."+key+".to-item");

            changeList.add(new ItemChange(priority, fromItem, toItem));
        }
        Collections.sort(changeList);
    }

    public void openInv(Player player)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, ColorFixer.addColors("&0Tworzenie..."));
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ColorFixer.addColors("&7"));
        itemStack.setItemMeta(itemMeta);
        for(int i = 0; i < 27; i++)
        {
            if(i == 12 || i == 14)
                continue;

            inventory.setItem(i, itemStack);
        }

        {
            ItemStack accept = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta acceptMeta = accept.getItemMeta();
            acceptMeta.setDisplayName(ColorFixer.addColors("&a&lSTWORZ"));
            accept.setItemMeta(acceptMeta);
            inventory.setItem(26, accept);
        }

        adminChangeInv.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    public void createItemsTrade(ItemStack fromItem, ItemStack toItem, Player player)
    {
        int index = ThreadLocalRandom.current().nextInt(1, 9999 + 1);
        FileConfiguration config = ChangeFile.getConfig();
        if(config.isSet("change-item."+index))
        {
            createItemsTrade(fromItem, toItem, player);
            return;
        }

        config.set("change-item."+index+".priority", 1);
        config.set("change-item."+index+".from-item", fromItem);
        config.set("change-item."+index+".to-item", toItem);
        ChangeFile.save();

        changeList.add(new ItemChange(1, fromItem, toItem));
        player.sendMessage(ColorFixer.addColors("&aPomyslnie stworzono trade!"));
    }

    public HashMap<UUID, Inventory> getAdminChangeInv() {
        return adminChangeInv;
    }
}
