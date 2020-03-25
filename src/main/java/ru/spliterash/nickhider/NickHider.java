package ru.spliterash.nickhider;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class NickHider extends JavaPlugin {
    @Getter
    private List<String> list;
    @Getter
    private boolean hideAll;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        list = getConfig().getStringList("nick");
        hideAll = getConfig().getBoolean("hide_all", false);
        getCommand("hide").setExecutor(new HideExecutor(this));
        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            HideManager.inst.showAll(player);
        }
    }
}
