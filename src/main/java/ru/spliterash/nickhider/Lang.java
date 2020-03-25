package ru.spliterash.nickhider;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public enum Lang {
    NO_PEX("no_pex", "&cYou dont have permissions to do that"),
    HIDE_HELP("hide_help", Arrays.asList(
            "&b/hide &6- Hide/Show my nickname to other",
            "&b/hide [player]&6- Hide/Show Other player nickname"
    )),
    UNHIDE("unhide", "&6You are not hide nickname now"),
    HIDE("hide", "&6Now players not see your nickname"),
    NO_CONSOLE("no_console", "&6Sry, but it command only for players"),
    NO_PLAYER("no_player", "&6Player is not in server"),
    UNHIDE_OTHER("unhide_other", "&6You show player nickname"),
    HIDE_OTHER("hide_other", "&6You hide player nickname"),
    HIDE_ALL("hide_all_mode", "&6You can use this command if hide all mode enable, sry maybe in next update");

    private static final NickHider plugin;

    static {
        plugin = JavaPlugin.getPlugin(NickHider.class);
        loadLang();
    }

    private final langType type;

    private List<String> list;

    private String text;

    public String[] toArray() {
        return toList().toArray(new String[0]);
    }

    private enum langType {
        text, list
    }

    private static YamlConfiguration LANG;
    private String path;

    /**
     * Lang enum constructor.
     *
     * @param path The string path.
     * @param text The default string.
     */
    Lang(String path, String text) {
        this.path = path;
        this.text = text;
        type = langType.text;
    }

    Lang(String path, List<String> list) {
        this.path = path;
        type = langType.list;
        this.list = list;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     *
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    public static void loadLang() {
        File langFile = new File(plugin.getDataFolder(), "locale.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
            } catch (Exception ignore) {
                Bukkit.getLogger().warning("unable to save locale.yml file");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
        for (Lang item : values()) {
            if (!conf.contains(item.getPath())) {
                conf.set(item.getPath(), item.getDefault());
            }

        }
        Lang.setFile(conf);
        try {
            conf.save(langFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save locale.yml.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case text:
                return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, text));
            case list:
                return String.join("|", toList());
            default:
                return "null";
        }
    }

    public String toStripString() {
        return ChatColor.stripColor(toString());
    }

    public List<String> toList() {
        switch (type) {
            case list:
                return LANG.getStringList(getPath())
                        .stream()
                        .map(s -> s = ChatColor.translateAlternateColorCodes('&', s))
                        .collect(Collectors.toCollection(ArrayList::new));
            case text:
                return Collections.singletonList(toString());
            default:
                return Collections.singletonList("&cError");
        }
    }

    /**
     * Get the default value of the path.
     *
     * @return The default value of the path.
     */
    public Object getDefault() {
        switch (type) {
            case text:
                return text;
            case list:
                return list;
            default:
                return "empty";
        }
    }

    /**
     * Get the path to the string.
     *
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }

}