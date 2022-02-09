package tech.candy_dev.candykills.listener;

import tech.candy_dev.candykills.CandyKills;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(CandyKills.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId()) != null){
            return;
        }

        CandyKills.getInstance().getUserManager().registerUser(e.getPlayer().getUniqueId());

    }

}
