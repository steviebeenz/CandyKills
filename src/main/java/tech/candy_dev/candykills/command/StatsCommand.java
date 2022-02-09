package tech.candy_dev.candykills.command;

import tech.candy_dev.candykills.CandyKills;
import tech.candy_dev.candykills.user.User;
import tech.candy_dev.candykills.util.Util;
import tech.candy_dev.candykills.util.command.CommandContext;
import tech.candy_dev.candykills.util.command.XCommand;
import tech.candy_dev.candykills.util.message.Message;
import tech.candy_dev.candykills.util.message.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class StatsCommand extends XCommand {
    public StatsCommand() {
        super("stats");
    }

    @Override
    public void setup() {
        setPermission("insurgencekills.stats");
        setDescription("Stats Command");
        setUsage("/stats");
    }

    @Override
    public void perform(CommandContext commandContext) {
        User user;
        if (commandContext.getArgs().length == 0) {
            if (!(commandContext.getCommandSender() instanceof Player)) {
                Message.PLAYERS_ONLY.send(commandContext.getCommandSender());
                return;
            }

            Player player = commandContext.getPlayer();

            user = CandyKills.getInstance().getUserManager().getUser(player.getUniqueId());
        } else {
            Player target = Bukkit.getPlayer(commandContext.getArgs()[0]);
            if (target == null) {
                Message.PLAYER_NOT_FOUND.send(commandContext.getCommandSender(), new Placeholder("{player}", commandContext.getArgs()[0]));
                return;
            }

            user = CandyKills.getInstance().getUserManager().getUser(target.getUniqueId());
        }

        List<Placeholder> placeholders = Arrays.asList(
            new Placeholder("{kills}", Util.getFormattedNumber(user.getKills())), new Placeholder("{deaths}", Util.getFormattedNumber(user.getDeaths())), new Placeholder("{killstreak}", Util.getFormattedNumber(user.getKillStreak())), new Placeholder("{player}", Bukkit.getPlayer(user.getUUID()).getName()));


        Message.STATS.sendList(commandContext.getCommandSender(), placeholders.toArray(new Placeholder[0]));

    }
}
