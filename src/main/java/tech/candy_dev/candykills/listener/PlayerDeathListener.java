package tech.candy_dev.candykills.listener;

import tech.candy_dev.candykills.CandyKills;
import tech.candy_dev.candykills.user.User;
import tech.candy_dev.candykills.util.Util;
import tech.candy_dev.candykills.util.config.Config;
import tech.candy_dev.candykills.util.message.Message;
import tech.candy_dev.candykills.util.message.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player player = e.getEntity();

        User user = CandyKills.getInstance().getUserManager().getUser(player.getUniqueId());

        user.setDeaths(user.getDeaths() + 1, true);
        user.setKillStreak(0, true);

        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            User kUser = CandyKills.getInstance().getUserManager().getUser(killer.getUniqueId());

            kUser.setKills(kUser.getKills() + 1, true);
            kUser.setKillStreak(kUser.getKillStreak() + 1, true);

            e.setDeathMessage(Util.c(Placeholder.apply(Message.DEATH_MESSAGE_PLAYER.getString(), new Placeholder("{prefix}", Message.PREFIX.getString()), new Placeholder("{dead}", player.getName()), new Placeholder("{killer}", killer.getName()))));

            double killReward = Config.KILL_REWARD.getDouble();
            if (killReward > 0 && CandyKills.getInstance().getEcon() != null) {
                CandyKills.getInstance().getEcon().depositPlayer(killer, killReward);
                Message.DEATH_MESSAGE_REWARD.sendList(killer, new Placeholder("{amount}", Util.getFormattedNumber(killReward)));
            }
        } else {
            e.setDeathMessage(Util.c(Placeholder.apply(Message.DEATH_MESSAGE_OTHER.getString(), new Placeholder("{prefix}", Message.PREFIX.getString()), new Placeholder("{dead}", player.getName()))));
        }

        double deathFine = Config.DEATH_FINE.getDouble();

        if (deathFine > 0 && CandyKills.getInstance().getEcon() != null) {
            if (CandyKills.getInstance().getEcon().getBalance(player) < deathFine) {
                return;
            }

            CandyKills.getInstance().getEcon().withdrawPlayer(player, deathFine);

            Message.DEATH_MESSAGE_FINE.send(player, new Placeholder("{amount}", Util.getFormattedNumber(deathFine)));

        }

    }

}
