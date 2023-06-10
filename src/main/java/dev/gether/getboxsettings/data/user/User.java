package dev.gether.getboxsettings.data.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class User {
    private Player player;
    private Inventory settingsInv;
    private boolean enableBlockConv;
    private boolean enableMoneyConv;
    private boolean enableSellMoney;

    private boolean actionBarEnable;

    public User(Player player, boolean enableBlockConv, boolean enableMoneyConv, boolean enableSellMoney, boolean actionBarEnable) {
        this.player = player;
        this.enableBlockConv = enableBlockConv;
        this.enableMoneyConv = enableMoneyConv;
        this.enableSellMoney = enableSellMoney;
        this.actionBarEnable = actionBarEnable;
    }

    public User(Player player)
    {
        this(player, false, false, false, false);
    }

    public void setSettingsInv(Inventory settingsInv) {
        this.settingsInv = settingsInv;
    }



    public void changeStatusBlockConv() {
        this.enableBlockConv = !enableBlockConv;
    }

    public void changeStatusMoneyConv() {
        this.enableMoneyConv = !enableMoneyConv;
    }

    public void changeStatusSellMoney() {
        this.enableSellMoney = !enableSellMoney;
    }
    public void changeActionBarStatus() {
        this.actionBarEnable = !actionBarEnable;
    }

    public Inventory getSettingsInv() {
        return settingsInv;
    }

    public boolean isEnableBlockConv() {
        return enableBlockConv;
    }

    public boolean isEnableMoneyConv() {
        return enableMoneyConv;
    }

    public boolean isActionBarEnable() {
        return actionBarEnable;
    }

    public boolean isEnableSellMoney() {
        return enableSellMoney;
    }
}
