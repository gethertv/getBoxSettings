package dev.gether.getboxsettings.data.user;

import dev.gether.getboxsettings.GetBoxSettings;
import dev.gether.getboxsettings.data.ItemBg;
import dev.gether.getboxsettings.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private GetBoxSettings plugin;
    private HashMap<UUID, User> userData = new HashMap<>();
    private List<ItemBg> bgItems = new ArrayList<>();
    public int SIZE_INV;
    public String TITLE_INV;

    public boolean BLOCK_CONV_ENABLE;
    public ItemStack BLOCK_CONV_ITEM;
    public String BLOCK_CONV_FORMAT_ACTION_BAR;
    public int BLOCK_CONV_SLOT;

    public boolean MONEY_CONV_ENABLE;
    public ItemStack MONEY_CONV_ITEM;
    public String MONEY_CONV_FORMAT_ACTION_BAR;
    public int MONEY_CONV_SLOT;

    public boolean SELL_MONEY_ENABLE;
    public ItemStack SELL_MONEY_ITEM;
    public String SELL_MONEY_FORMAT_ACTION_BAR;
    public int SELL_MONEY_SLOT;

    public boolean ACTION_BAR_ENABLE;
    public ItemStack ACTION_BAR_ITEM;
    public int ACTION_BAR_SLOT;

    public String STATUS_ON;
    public String STATUS_OFF;

    public String STATUS_ON_ACTION_BAR;
    public String STATUS_OFF_ACTION_BAR;
    public String FORMAT_ACTION_BAR;
    public UserManager(GetBoxSettings plugin)
    {
        this.plugin = plugin;
        injectConfig();
    }

    public void injectConfig() {
        bgItems.clear();
        for(String key : plugin.getConfig().getConfigurationSection("backgrounds").getKeys(false))
        {
            ItemStack itemStack = getItemFromConfig("backgrounds."+key);

            List<Integer> slots = new ArrayList<>();
            slots.addAll(plugin.getConfig().getIntegerList("backgrounds."+key+".slots"));

            bgItems.add(new ItemBg(itemStack, slots));
        }

        SIZE_INV = plugin.getConfig().getInt("inv.size");
        TITLE_INV = plugin.getConfig().getString("inv.title");

        BLOCK_CONV_ENABLE = plugin.getConfig().getBoolean("items.BLOCK_CONV.enable");
        BLOCK_CONV_ITEM = getItemFromConfig("items.BLOCK_CONV");
        BLOCK_CONV_SLOT = plugin.getConfig().getInt("items.BLOCK_CONV.slot");
        BLOCK_CONV_FORMAT_ACTION_BAR = plugin.getConfig().getString("lang.block-conv");

        MONEY_CONV_ENABLE = plugin.getConfig().getBoolean("items.MONEY_CONV.enable");
        MONEY_CONV_ITEM = getItemFromConfig("items.MONEY_CONV");
        MONEY_CONV_SLOT = plugin.getConfig().getInt("items.MONEY_CONV.slot");
        MONEY_CONV_FORMAT_ACTION_BAR = plugin.getConfig().getString("lang.money_conv");

        SELL_MONEY_ENABLE = plugin.getConfig().getBoolean("items.SELL_MONEY.enable");
        SELL_MONEY_ITEM = getItemFromConfig("items.SELL_MONEY");
        SELL_MONEY_SLOT=  plugin.getConfig().getInt("items.SELL_MONEY.slot");
        SELL_MONEY_FORMAT_ACTION_BAR=  plugin.getConfig().getString("lang.sell_money");

        ACTION_BAR_ENABLE = plugin.getConfig().getBoolean("items.ACTION_BAR.enable");
        ACTION_BAR_ITEM = getItemFromConfig("items.ACTION_BAR");
        ACTION_BAR_SLOT=  plugin.getConfig().getInt("items.ACTION_BAR.slot");

        STATUS_ON = plugin.getConfig().getString("lang.status-on");
        STATUS_OFF = plugin.getConfig().getString("lang.status-off");

        STATUS_ON_ACTION_BAR = plugin.getConfig().getString("lang.status-action-on");
        STATUS_OFF_ACTION_BAR = plugin.getConfig().getString("lang.status-action-off");
        FORMAT_ACTION_BAR = plugin.getConfig().getString("action-bar");
    }

    private ItemStack getItemFromConfig(String path)
    {
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getConfig().getString(path+".material").toUpperCase()));
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors(plugin.getConfig().getString(path+".displayname")));
        List<String> lore = new ArrayList<>();
        lore.addAll(plugin.getConfig().getStringList(path+".lore"));
        itemMeta.setLore(ColorFixer.addColors(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public void openSettings(Player player) {
        User user = getUserData().get(player.getUniqueId());
        if(user==null)
            return;

        Inventory inventory = Bukkit.createInventory(null, SIZE_INV, ColorFixer.addColors(TITLE_INV));
        getBgItems().forEach(bgItems -> {
            bgItems.getSlots().forEach(slot -> inventory.setItem(slot, bgItems.getItemStack()));
        });

        if(SELL_MONEY_ENABLE)
            inventory.setItem(SELL_MONEY_SLOT, getItemStatus(SELL_MONEY_ITEM ,user.isEnableSellMoney()));

        if(MONEY_CONV_ENABLE)
            inventory.setItem(MONEY_CONV_SLOT, getItemStatus(MONEY_CONV_ITEM ,user.isEnableMoneyConv()));

        if(BLOCK_CONV_ENABLE)
            inventory.setItem(BLOCK_CONV_SLOT, getItemStatus(BLOCK_CONV_ITEM ,user.isEnableBlockConv()));

        if(ACTION_BAR_ENABLE)
            inventory.setItem(ACTION_BAR_SLOT, getItemStatus(ACTION_BAR_ITEM , user.isActionBarEnable()));

        user.setSettingsInv(inventory);

        player.openInventory(inventory);
    }

    private ItemStack getItemStatus(ItemStack itemStack, boolean status)
    {
        ItemStack item = itemStack.clone();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.getLore()==null)
            return item;

        List<String> lore = new ArrayList<>();
        lore.addAll(itemMeta.getLore());
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("{status}", status ? STATUS_ON : STATUS_OFF));
        }
        itemMeta.setLore(ColorFixer.addColors(lore));

        item.setItemMeta(itemMeta);

        return item;
    }
    public void updateBlockConv(Inventory settingsInv, int slot, boolean status) {
        if(slot==SELL_MONEY_SLOT)
        {
            settingsInv.setItem(SELL_MONEY_SLOT, getItemStatus(SELL_MONEY_ITEM, status));
            return;
        }
        if(slot==BLOCK_CONV_SLOT)
        {
            settingsInv.setItem(BLOCK_CONV_SLOT, getItemStatus(BLOCK_CONV_ITEM, status));
            return;
        }
        if(slot==MONEY_CONV_SLOT)
        {
            settingsInv.setItem(MONEY_CONV_SLOT, getItemStatus(MONEY_CONV_ITEM, status));
            return;
        }
        if(slot==ACTION_BAR_SLOT)
        {
            settingsInv.setItem(ACTION_BAR_SLOT, getItemStatus(ACTION_BAR_ITEM, status));
            return;
        }
    }
    public HashMap<UUID, User> getUserData() {
        return userData;
    }


    public List<ItemBg> getBgItems() {
        return bgItems;
    }


}
