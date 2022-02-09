package tech.candy_dev.candykills.util.message;

import tech.candy_dev.candykills.util.Util;
import tech.candy_dev.candykills.util.file.yaml.YamlFile;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.List;

public enum Message {

    PREFIX("prefix", "&5&lCandy&f&lKills &8» &d"),
    USAGE("errors.usage", "{prefix}&4&LERROR: &cIncorrect Usage! Use: {usage}"),
    NO_PERMISSION("errors.no-permission", "{prefix}&4&lERROR: &cYou need the permission {node} to do this!"),
    PLAYERS_ONLY("errors.players-only", "{prefix}&4&lERROR: &cOnly players can perform this command!"),
    PLAYER_NOT_FOUND("errors.player-not-found", "{prefix}&4&lERROR: &cCould not find player {player}!"),
    DEATH_MESSAGE_PLAYER("death-message.player", "{prefix} &b{dead} &3has been killed by &b{killer}!"),
    DEATH_MESSAGE_OTHER("death-message.other", "{prefix} &b{dead} &3died of natural causes!"),
    DEATH_MESSAGE_FINE("death-message.fine", "{prefix} &bYou lost ${amount} because you died!"),
    DEATH_MESSAGE_REWARD("death-message.reward", "{prefix} &bYou received ${amount} for getting a kill!"),
    STATS("stats",
          Arrays.asList(
              "&8&l======================",
              " ",
              "&dStats for &b{player}",
              " ",
              " &dKills: &b{kills}",
              " &dDeaths: &b{deaths}",
              " &dKill Streak: &b{killstreak}",
              " ",
              "&8&l======================"
          ));

    private final String path;
    private Object value;

    Message(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public static void loadMessages(YamlFile iConfig) {

        boolean saveConfig = false;

        YamlConfiguration config = iConfig.getConfig();

        for (Message message : values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.value);
                saveConfig = true;
                continue;
            }

            message.value = config.get(message.path);
        }

        if (saveConfig) {
            iConfig.saveConfig();
        }
    }

    public Object getValue() {
        return this.value;
    }

    /**
     * Gets string of value.
     *
     * @return the string
     */
    public String getString() {
        return (String) this.value;
    }

    /**
     * Gets string list.
     *
     * @return the string list
     */
    public List<String> getStringList() {
        return (List<String>) this.value;
    }

    /**
     * Send a message with placeholders as well.
     *
     * @param player       the player
     * @param placeholders the placeholders
     */
    public void send(CommandSender player, Placeholder... placeholders) {

        if (player == null) {
            return;
        }

        String text = this.getString();

        text = Placeholder.apply(text, placeholders);

        player.sendMessage(Util.c(text.replace("{prefix}", (String) PREFIX.value)));
    }

    /**
     * Send a StringList into a messsage to a valid sender.
     *
     * @param player       the player
     * @param placeholders the placeholders
     */
    public void sendList(CommandSender player, Placeholder... placeholders) {
        StringBuilder text = new StringBuilder();
        for (String message : this.getStringList()) {
            text.append(Placeholder.apply(message, placeholders)).append("\n");
        }

        player.sendMessage(Util.c(text.toString().replace("{prefix}", (String) PREFIX.value)));
    }

}
