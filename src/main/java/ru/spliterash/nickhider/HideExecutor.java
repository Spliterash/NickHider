package ru.spliterash.nickhider;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class HideExecutor implements CommandExecutor {
    private final NickHider plugin;

    public HideExecutor(NickHider plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.isHideAll()) {
            sender.sendMessage(Lang.HIDE_ALL.toString());
            return true;
        }
        HideManager manager = HideManager.inst;
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("nickhider.self")) {
                    if (manager.isHided(player)) {
                        manager.unHidePlayer(player);
                        player.sendMessage(Lang.UNHIDE.toString());
                    } else {
                        manager.hidePlayer(player);
                        player.sendMessage(Lang.HIDE.toString());
                    }
                } else {
                    player.sendMessage(Lang.NO_PEX.toString());
                }
            } else {
                sender.sendMessage(Lang.NO_CONSOLE.toString());
            }
            return true;
        } else if (args.length == 1) {
            if (sender.hasPermission("nickhider.other")) {
                Player otherPlayer = Bukkit.getPlayer(args[0]);
                if (otherPlayer == null) {
                    sender.sendMessage(Lang.NO_PLAYER.toString());
                } else {
                    if (manager.isHided(otherPlayer)) {
                        manager.unHidePlayer(otherPlayer);
                        sender.sendMessage(Lang.UNHIDE_OTHER.toString());
                    } else {
                        manager.hidePlayer(otherPlayer);
                        sender.sendMessage(Lang.HIDE_OTHER.toString());
                    }
                }
            } else {
                sender.sendMessage(Lang.NO_PEX.toString());
            }
        }
        return true;
    }
}
