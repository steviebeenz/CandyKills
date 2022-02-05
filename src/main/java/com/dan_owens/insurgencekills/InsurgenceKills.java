package com.dan_owens.insurgencekills;

import com.dan_owens.insurgencekills.util.file.yaml.YamlFile;
import com.dan_owens.insurgencekills.command.StatsCommand;
import com.dan_owens.insurgencekills.listener.PlayerDeathListener;
import com.dan_owens.insurgencekills.listener.PlayerJoinListener;
import com.dan_owens.insurgencekills.user.manager.UserManager;
import com.dan_owens.insurgencekills.util.Util;
import com.dan_owens.insurgencekills.util.command.CommandManager;
import com.dan_owens.insurgencekills.util.command.InsurgenceCommand;
import com.dan_owens.insurgencekills.util.config.Config;
import com.dan_owens.insurgencekills.util.database.Database;
import com.dan_owens.insurgencekills.util.message.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InsurgenceKills extends JavaPlugin {

    private static InsurgenceKills instance;

    private UserManager userManager;

    private Database database;

    private Map<String, YamlFile> yamlFileMap;

    private CommandManager commandManager;

    private Economy econ;

    @Override
    public void onEnable() {
        instance = this;
        reload();
        if (Config.SQL_ENABLED.getBoolean()) {
            this.database = new Database(Config.SQL_USERNAME.getString(), Config.SQL_PASSWORD.getString(), Config.SQL_HOST.getString(), Config.SQL_DATABASE.getString(), Config.SQL_PORT.getInt());
            if(!this.database.isConnected()){
                sendConsoleMessage("Could not connect to the MySQL Database, disabling...");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
            }
            sendConsoleMessage("MySQL Connection Established!");
        }else{
            sendConsoleMessage("MySQL disabled, using FlatFile storage!");
        }
        this.userManager = new UserManager();
        registerListeners(new PlayerJoinListener(), new PlayerDeathListener());
        this.commandManager = new CommandManager();
        new StatsCommand();
        if(!setupEconomy()){
            sendConsoleMessage("Vault not found! Kill Rewards and Death Fines will not work!");
        }else{
            sendConsoleMessage("Vault Found!");
        }
    }

    @Override
    public void onDisable() {
        userManager.save(false);
    }

    public void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(Util.c(Message.PREFIX.getString() + message));
    }

    public void registerCommand(String alias, InsurgenceCommand insurgenceCommand) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(alias, insurgenceCommand);

            commandManager.registerCommand(insurgenceCommand);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public static InsurgenceKills getInstance() {
        return instance;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    private void loadYamlFiles(){
        this.yamlFileMap = new HashMap<>();
        List<String> files = Arrays.asList("config", "messages");
        for(String file: files){
            yamlFileMap.put(file, new YamlFile(file + ".yml", this.getDataFolder().getAbsolutePath(), null));
        }
    }

    public void reload(){
        loadYamlFiles();
        Config.loadConfig(yamlFileMap.get("config"));
        Message.loadMessages(yamlFileMap.get("messages"));
    }

    public Database getDatabase() {
        return database;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon() {
        return econ;
    }
}
