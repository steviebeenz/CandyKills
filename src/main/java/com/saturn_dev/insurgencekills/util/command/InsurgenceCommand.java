package com.saturn_dev.insurgencekills.util.command;

import com.saturn_dev.insurgencekills.InsurgenceKills;
import com.saturn_dev.insurgencekills.util.Util;
import com.saturn_dev.insurgencekills.util.message.Message;
import com.saturn_dev.insurgencekills.util.message.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class InsurgenceCommand extends org.bukkit.command.Command {

    private final List<SubCommand> subCommandList;

    private String permission = null;
    private boolean requiresPlayer = false;

    public InsurgenceCommand(String name) {
        super(name);
        this.subCommandList = new ArrayList<>();
        this.setup();
        InsurgenceKills.getInstance().registerCommand(this.getName(), this);
    }

    public abstract void setup();

    public abstract void perform(CommandContext commandContext);

    public boolean execute(CommandContext commandContext) {
        if (commandContext.getArgs().length > 0) {
            for (final SubCommand subCommand : this.subCommandList) {
                if (!subCommand.getAliasList().contains(commandContext.getArgs()[0].toLowerCase())) {
                    continue;
                }
                if (subCommand.getPermission() != null) {
                    if (!commandContext.getCommandSender().hasPermission(subCommand.getPermission())) {
                        Message.NO_PERMISSION.send(commandContext.getPlayer(), new Placeholder("{node}", subCommand.getPermission()));
                        return false;
                    }
                }
                if (subCommand.isPlayer()) {
                    if (commandContext.getPlayer() == null) {
                        Message.PLAYERS_ONLY.send(commandContext.getCommandSender());
                        return false;
                    }
                }

                int args = 0;

                if(subCommand.getUsage().contains(" ")){
                    args = subCommand.getUsage().split(" ").length-1;
                }

                if(commandContext.getArgs().length < args){
                    Message.USAGE.send(commandContext.getCommandSender(), new Placeholder("{usage}", subCommand.getUsage()));
                    return false;
                }

                subCommand.perform(commandContext);
                return true;
            }
        }

        if (this.requiresPlayer && commandContext.getPlayer() == null) {
            Message.PLAYERS_ONLY.send(commandContext.getCommandSender());
            return false;
        }
        if (this.permission != null && !commandContext.getCommandSender().hasPermission(this.permission)) {
            Message.NO_PERMISSION.send(commandContext.getPlayer(), new Placeholder("{node}", this.permission));
            return false;
        }

        int args = 0;

        if(getUsage().contains(" ")){
            args = getUsage().split(" ").length-1;
        }

        if(commandContext.getArgs().length < args){
            Message.USAGE.send(commandContext.getCommandSender(), new Placeholder("{usage}", getUsage()));
            return false;
        }

        this.perform(commandContext);
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        return this.execute(new CommandContext(commandSender, commandLabel, args));
    }

    public void addSubCommands(SubCommand... subCommands) {
        this.subCommandList.addAll(Arrays.asList(subCommands));
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    public boolean isRequiresPlayer() {
        return this.requiresPlayer;
    }

    public List<SubCommand> getSubCommandList() {
        return this.subCommandList;
    }

    public void setRequiresPlayer(boolean requiresPlayer) {
        this.requiresPlayer = requiresPlayer;
    }


}
