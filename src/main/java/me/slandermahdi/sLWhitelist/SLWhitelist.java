package me.slandermahdi.sLWhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SLWhitelist extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private boolean whitelistEnabled; // Boolean whitelist enabled bray true bodan ya false bodan whitelist

    @Override
    public void onEnable() {
        saveDefaultConfig(); // save kardan file config
        config = getConfig(); // grftan file config
        whitelistEnabled = config.getBoolean("whitelist-enabled", true); // Grftan True Ya False bodan whitelist az config
        Bukkit.getPluginManager().registerEvents(this, this); // Register kardan event hay Hamin File
        getLogger().info("Plugin Whitelist Roshan Shod"); // payam roshan shodan plugin
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Whitelist Khamosh Shod"); // payam khamosh shodan plugin
    }

    @Override
    //register kardan command avalih
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("slwhitelist")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /slwhitelist <add|remove|list|reload|on|off> [player]");
                return true;
            }

            String subCommand = args[0].toLowerCase();
            // Sub Coomand Ha Command Hay Kh Bad Az command slwhitelist myan

            switch (subCommand) {
                // sub command add baray add kardan player bh whitelist
                case "add":
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /slwhitelist add <player>");
                        return true;
                    }
                    String playerToAdd = args[1];
                    addPlayerToWhitelist(playerToAdd, sender);
                    break;
                // Sub Command Remove Baray Remove Kardan Player Az Whitelist
                case "remove":
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /slwhitelist remove <player>");
                        return true;
                    }
                    String playerToRemove = args[1];
                    removePlayerFromWhitelist(playerToRemove, sender);
                    break;
                // sub command list baray didan list player hay whitelist
                case "list":
                    listWhitelistedPlayers(sender);
                    break;
                // sub command reload baray reload kardan config
                case "reload":
                    reloadConfig();
                    config = getConfig();
                    whitelistEnabled = config.getBoolean("whitelist-enabled", true);
                    sender.sendMessage(ChatColor.GREEN + "Whitelist Settings Reloaded");
                    break;
                // sub command on baray on kardan whitelist
                case "on":
                    whitelistEnabled = true;
                    config.set("whitelist-enabled", true);
                    saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Whitelist Turn On");
                    break;
                // sub command off baray off kardan whitelist
                case "off":
                    whitelistEnabled = false;
                    config.set("whitelist-enabled", false);
                    saveConfig();
                    sender.sendMessage(ChatColor.RED + "Whitelist Turn OFF!");
                    break;
                // payam kh sub command yaft nashod
                default:
                    sender.sendMessage(ChatColor.RED + "Cmd Not Found: /slwhitelist <add|remove|list|reload|on|off> [player]");
                    break;
            }
            return true;
        }
        return false;
    }

    @EventHandler
    // vaghti kh player join server mish
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!whitelistEnabled) { // dar sorati kh whitelist on bot
            return;
        }

        Player player = event.getPlayer(); // grftan player
        List<String> whitelist = config.getStringList("whitelist"); // grftan list whitelist
        if (!whitelist.contains(player.getName())) { // dar sorati kh player whitelist nabod
            String kickMessage = ChatColor.translateAlternateColorCodes('&', config.getString("kick-message", "&cYou are not whitelisted on this server.")); // grftan payam az config
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, kickMessage); // kick kardan player agar whitelist nabod bh dalil string kickMassage
        }
    }

    private void addPlayerToWhitelist(String playerName, CommandSender sender) { // Add Kardan Player Bh Whitelist
        List<String> whitelist = config.getStringList("whitelist"); // grftan list whitelist
        // dar sorat whitelist bodan player
        if (whitelist.contains(playerName)) {
            sender.sendMessage(ChatColor.YELLOW + playerName + " Alredy In Whitelist"); // arsal payam bh sender
            return;
        }
        whitelist.add(playerName); // dar sorat nabod alredy dakhl whitelist add mishavad player
        config.set("whitelist", whitelist);
        saveConfig(); // save kardan config
        sender.sendMessage(ChatColor.GREEN + playerName + " Added To Whitelist."); // arsal payam movafaghyat bh sender
    }

    private void removePlayerFromWhitelist(String playerName, CommandSender sender) { // remove kardan player az whitelist
        List<String> whitelist = config.getStringList("whitelist");
        if (!whitelist.contains(playerName)) {
            sender.sendMessage(ChatColor.RED + playerName + " Is Not Whitelist.");
            return;
        }
        whitelist.remove(playerName); // remove shodan player az whitelist
        config.set("whitelist", whitelist);
        saveConfig();
        sender.sendMessage(ChatColor.GREEN + playerName + " Removed Whitelist."); // payam bh sender
    }

    private void listWhitelistedPlayers(CommandSender sender) {
        List<String> whitelist = config.getStringList("whitelist");
        sender.sendMessage(ChatColor.GOLD + "Whitelist List :");
        for (String player : whitelist) {
            sender.sendMessage(ChatColor.AQUA + "- " + player);
        }
    }
}
