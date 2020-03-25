package ru.spliterash.nickhider;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class HideManager {
    public static final HideManager inst = new HideManager();
    private final NickHider plugin;
    public final Set<Player> hidedPlayer = new HashSet<>();

    private HideManager() {
        this.plugin = JavaPlugin.getPlugin(NickHider.class);
    }

    public void hideNick(Player player, String hide) {
        Team t = getHideTeam(player);
        if (!t.hasEntry(hide)) {
            t.addEntry(hide);
        }
    }

    public void hidePlayer(Player player) {
        hidedPlayer.add(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            hideNick(onlinePlayer, player.getName());
        }
    }

    public boolean isHided(Player player) {
        return hidedPlayer.contains(player);
    }

    public void unHidePlayer(Player player) {
        if (hidedPlayer.remove(player)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                showNick(onlinePlayer, player.getName());
            }
        }
    }

    public void showNick(Player player, String show) {
        Team t = getHideTeam(player);
        if (t.hasEntry(show))
            t.removeEntry(show);
    }

    public void showAll(Player player) {
        Team t = getHideTeam(player);
        Set<String> entry = new HashSet<>(t.getEntries());
        for (String s : entry) {
            t.removeEntry(s);
        }
    }

    private Team getHideTeam(Player player) {
        Scoreboard s = player.getPlayer().getScoreboard();
        Team t = s.getTeam("hideTeam");
        if (t == null) {
            t = s.registerNewTeam("hideTeam");
            t.setNameTagVisibility(NameTagVisibility.NEVER);
        }
        return t;
    }
}
