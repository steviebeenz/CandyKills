package tech.candy_dev.candykills.util.command;

import java.util.List;

public abstract class SubCommand {

    private final List<String> aliasList;

    private final String usage;

    private String permission;
    
    private boolean player;

    public SubCommand(String usage, List<String> aliases) {
        this.usage = usage;
        this.aliasList = aliases;
    }

    public SubCommand(String usage, String permission, List<String> aliases){
        this(usage, aliases);
        this.permission = permission;
    }

    public abstract void perform(CommandContext commandContext);

    public List<String> getAliasList() {
        return aliasList;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }
}
