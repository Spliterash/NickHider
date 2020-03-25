package ru.spliterash.nickhider;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Handler implements Listener {
    private final NickHider plugin;

    public Handler(NickHider plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Stream<String> stream = Stream.concat(
                plugin.getList().stream(),
                HideManager.inst.hidedPlayer
                        .stream()
                        .map(HumanEntity::getName)
        );
        if (plugin.isHideAll()) {
            stream = Stream.concat(stream, Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName));
        }
        Set<String> list = stream.collect(Collectors.toSet());
        if (list.size() > 0) {
            for (String s : list) {
                HideManager.inst.hideNick(p, s);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        HideManager.inst.unHidePlayer(e.getPlayer());
    }
}
