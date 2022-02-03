package com.saturn_dev.insurgencekills.util.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

    private final CommandSender commandSender;
    private final String[] args;
    private final Player player;
    private final String label;

    public CommandContext(CommandSender commandSender, String label, String[] args){
        this.commandSender = commandSender;
        this.label = label;
        this.args =args;
        if(commandSender instanceof Player){
            this.player = ((Player) commandSender);
        }else{
            this.player = null;
        }
    }

    public Player getOnlinePlayer(String argument){
        return Bukkit.getPlayer(argument);
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public String[] getArgs() {
        return args;
    }

    public Player getPlayer() {
        return player;
    }

    public String getLabel() {
        return label;
    }
}
