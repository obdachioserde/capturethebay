package de.obdachioser.capturethebay.projectlisteners;

import de.obdachioser.capturethebay.CaptureTheBay;
import de.obdachioser.capturethebay.api.DefinedTeam;
import de.obdachioser.capturethebay.api.Team;
import de.obdachioser.capturethebay.cache.api.PlayerCache;
import de.obdachioser.capturethebay.events.BayFoundEvent;
import de.obdachioser.capturethebay.utils.ItemStackCreator;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ObdachIoser at 14:31 on 27.08.2017.
 *
 * @TODO
 * @Finished: false
 */
public class BayFoundListener implements Listener {

    @EventHandler
    public void bayFound(BayFoundEvent event) {

        PlayerCache playerCache = CaptureTheBay.getGameSession().getPlayerCacheCacheHandler().get(event.getPlayer().getUniqueId());
        DefinedTeam definedTeam = (DefinedTeam) playerCache.getCurrentTeam();

        try {

            Executors.newCachedThreadPool().execute(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureTheBay.getInstance(),
                    () -> this.d(event.getBiObject().getValue0().getLocation().clone(), definedTeam)));

        } catch (Exception exc) {}

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getEyeLocation(), Sound.WITHER_HURT, 1F, 1F));

        Bukkit.broadcastMessage(CaptureTheBay.getPrefix() + "Die Bucht §e" + event.getBayName() + " §7wurde vom Team " + definedTeam.getTeamDisplayName() + " §7gefunden!");
    }

    private void d(Location location, DefinedTeam definedTeam) {

        location.setY(location.getY()+4);
        ItemStack itemStack = ItemStackCreator.d(Material.WOOL, definedTeam.getTeamColor());

        try {

            for(Integer i = 0; i != 9; i++) {

                TimeUnit.MILLISECONDS.sleep(100L);

                Bukkit.getWorld("world").playEffect(location, Effect.TILE_BREAK, 1);

                location.setY(location.getY()+1);
                location.getBlock().setType(itemStack.getType());
                location.getBlock().setData(itemStack.getData().getData());
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}