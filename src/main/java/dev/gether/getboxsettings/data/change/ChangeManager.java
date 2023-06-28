package dev.gether.getboxsettings.data.change;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.change.block.BlockChange;
import dev.gether.getboxsettings.data.change.coins.ItemCoinsChange;
import dev.gether.getboxsettings.data.change.money.MoneyChange;
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
    private HashMap<UUID, Inventory> adminMoneyCreateInv = new HashMap<>();
    private List<ItemCoinsChange> changeList = new ArrayList<>();
    private List<BlockChange> blockChanges = new ArrayList<>();
    private List<MoneyChange> moneyChanges = new ArrayList<>();

    private ChangeFeatures changeFeatures;


    public ChangeManager()
    {
        injectData();
        changeFeatures = new ChangeFeatures();
    }

    public void injectData()
    {
        injectChangeItems();
        injectBlockChanges();
        injectMoneyChanges();
    }


    public void changeToCoin(Player player)
    {
        for(ItemCoinsChange itemCoinsChange : getChangeList())
        {
            int amount = changeFeatures.calcItem(player, itemCoinsChange.getFromItem());
            int finalAmount = amount/itemCoinsChange.getFromItem().getAmount();
            if(finalAmount<=0)
                continue;

            int getAmount = finalAmount*itemCoinsChange.getToItem().getAmount();
            changeFeatures.removeItem(player, itemCoinsChange.getFromItem().clone(), finalAmount*itemCoinsChange.getFromItem().getAmount());
            changeFeatures.addItem(player, itemCoinsChange.getToItem().clone(), getAmount);
        }
    }

    public void changeToMoney(Player player)
    {
        for(MoneyChange moneyChange : getMoneyChanges())
        {
            int amount = changeFeatures.calcItem(player, moneyChange.getItemStack());

            int finalAmount = amount/moneyChange.getItemStack().getAmount();
            if(finalAmount<=0)
                continue;

            int getAmount = finalAmount*moneyChange.getItemStack().getAmount();
            changeFeatures.removeItem(player, moneyChange.getItemStack().clone(), finalAmount*moneyChange.getItemStack().getAmount());
            GetBoxSettings.getEcon().depositPlayer(player, getAmount*moneyChange.getPrice());
            player.updateInventory();
        }
    }
    public void changeToBlock(Player player)
    {
        for(BlockChange blockChange : getBlockChanges())
        {
            int amount = changeFeatures.calcItem(player, blockChange.getFromItemstack());

            int finalAmount = amount/blockChange.getFromItemstack().getAmount();
            if(finalAmount<=0)
                continue;

            int getAmount = finalAmount*blockChange.getToItemstack().getAmount();
            changeFeatures.removeItem(player, blockChange.getFromItemstack().clone(), finalAmount*blockChange.getFromItemstack().getAmount());
            changeFeatures.addItem(player, blockChange.getToItemstack().clone(), getAmount);
        }
    }
    private void injectBlockChanges() {
        blockChanges.clear();
        FileConfiguration config = ChangeFile.getConfig();
        for(String key : config.getConfigurationSection("change-block").getKeys(false))
        {
            Material fromMaterial = Material.valueOf(config.getString("change-block."+key+".from.material"));
            int fromAmount = config.getInt("change-block."+key+".from.amount");

            Material toMaterial = Material.valueOf(config.getString("change-block."+key+".to.material"));
            int toAmount = config.getInt("change-block."+key+".to.amount");

            ItemStack fromItem = new ItemStack(fromMaterial);
            fromItem.setAmount(fromAmount);

            ItemStack toItem = new ItemStack(toMaterial);
            toItem.setAmount(toAmount);

            blockChanges.add(new BlockChange(fromItem, toItem));
        }
    }

    private void injectMoneyChanges() {
        moneyChanges.clear();
        FileConfiguration config = ChangeFile.getConfig();
        for(String key : config.getConfigurationSection("change-money").getKeys(false))
        {
            ItemStack fromItem = config.getItemStack("change-money."+key+".from-item");
            double price = config.getDouble("change-money."+key+".price");

            moneyChanges.add(new MoneyChange(fromItem, price));
        }
    }
    private void injectChangeItems() {
        changeList.clear();
        FileConfiguration config = ChangeFile.getConfig();
        for(String key : config.getConfigurationSection("change-item").getKeys(false))
        {
            int priority = config.getInt("change-item."+key+".priority");
            ItemStack fromItem = config.getItemStack("change-item."+key+".from-item");
            ItemStack toItem = config.getItemStack("change-item."+key+".to-item");

            changeList.add(new ItemCoinsChange(priority, fromItem, toItem));
        }
        Collections.sort(changeList);
    }

    public void openAdminCoinsInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ColorFixer.addColors("&0Tworzenie... - Coins"));
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

    public void openAdminMoneyInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ColorFixer.addColors("&0Tworzenie... - Money"));
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ColorFixer.addColors("&7"));
        itemStack.setItemMeta(itemMeta);
        for(int i = 0; i < 27; i++)
        {
            if(i == 13)
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

        adminMoneyCreateInv.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    public void createMoneyTrade(ItemStack fromItem, Player player) {
        int index = ThreadLocalRandom.current().nextInt(1, 9999 + 1);
        FileConfiguration config = ChangeFile.getConfig();
        if(config.isSet("change-money."+index))
        {
            createMoneyTrade(fromItem, player);
            return;
        }

        config.set("change-money."+index+".from-item", fromItem);
        config.set("change-money."+index+".price", 0);
        ChangeFile.save();

        moneyChanges.add(new MoneyChange(fromItem, 0));
        player.sendMessage(ColorFixer.addColors("&aPomyslnie stworzono trade money!"));
        player.closeInventory();
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

        changeList.add(new ItemCoinsChange(1, fromItem, toItem));
        player.sendMessage(ColorFixer.addColors("&aPomyslnie stworzono trade coins!"));
        player.closeInventory();
    }

    public HashMap<UUID, Inventory> getAdminChangeInv() {
        return adminChangeInv;
    }

    public List<BlockChange> getBlockChanges() {
        return blockChanges;
    }

    public List<MoneyChange> getMoneyChanges() {
        return moneyChanges;
    }

    public List<ItemCoinsChange> getChangeList() {
        return changeList;
    }

    public HashMap<UUID, Inventory> getAdminMoneyCreateInv() {
        return adminMoneyCreateInv;
    }


}
