package com.saturn_dev.insurgencekills.command;

import com.saturn_dev.insurgencekills.InsurgenceKills;
import com.saturn_dev.insurgencekills.user.User;
import com.saturn_dev.insurgencekills.util.Util;
import com.saturn_dev.insurgencekills.util.command.CommandContext;
import com.saturn_dev.insurgencekills.util.command.InsurgenceCommand;
import com.saturn_dev.insurgencekills.util.message.Message;
import com.saturn_dev.insurgencekills.util.message.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class StatsCommand extends InsurgenceCommand {
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

            user = InsurgenceKills.getInstance().getUserManager().getUser(player.getUniqueId());
        } else {
            Player target = Bukkit.getPlayer(commandContext.getArgs()[0]);
            if (target == null) {
                Message.PLAYER_NOT_FOUND.send(commandContext.getCommandSender(), new Placeholder("{player}", commandContext.getArgs()[0]));
                return;
            }

            user = InsurgenceKills.getInstance().getUserManager().getUser(target.getUniqueId());
        }

        List<Placeholder> placeholders = Arrays.asList(
            new Placeholder("{kills}", Util.getFormattedNumber(user.getKills())), new Placeholder("{deaths}", Util.getFormattedNumber(user.getDeaths())), new Placeholder("{killstreak}", Util.getFormattedNumber(user.getKillStreak())), new Placeholder("{player}", Bukkit.getPlayer(user.getUUID()).getName()));


        Message.STATS.sendList(commandContext.getCommandSender(), placeholders.toArray(new Placeholder[0]));

    }
}
