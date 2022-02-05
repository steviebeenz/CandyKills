package com.dan_owens.insurgencekills.listener;

import com.dan_owens.insurgencekills.InsurgenceKills;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(InsurgenceKills.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId()) != null){
            return;
        }

        InsurgenceKills.getInstance().getUserManager().registerUser(e.getPlayer().getUniqueId());

    }

}
