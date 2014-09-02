package pw.ollie.lootprotect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public final class LPListener implements Listener {
    private final List<Protection> protections = new ArrayList<>();
    private final long protectionLengthMillis;

    LPListener(LootProtect plugin) {
        FileConfiguration config = plugin.getConfig();
        protectionLengthMillis = config.getInt("protection-length", 3) * 1000;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        EntityDamageEvent cause = event.getEntity().getLastDamageCause();

        if (cause instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evt = (EntityDamageByEntityEvent) cause;
            Entity killer = evt.getDamager();

            if (killer instanceof Player) {
                Player playerKiller = (Player) killer;
                UUID killerId = playerKiller.getUniqueId();

                if (playerKiller.hasPermission("lootprotect.protect")) {
                    for (ItemStack stack : event.getDrops()) {
                        Protection prot = new Protection(stack, killerId, System.currentTimeMillis(), protectionLengthMillis);
                        protections.add(prot);
                    }
                    playerKiller.sendMessage(ChatColor.GRAY + "[LootProtect] Your loot is protected!");
                } else {
                    playerKiller.sendMessage(ChatColor.GRAY + "[LootProtect] Your loot isn't protected!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        ItemStack stack = item.getItemStack();

        for (Protection prot : protections) {
            if (System.currentTimeMillis() > prot.start + prot.length) {
                protections.remove(prot);
                continue;
            }

            if (prot.stack.equals(stack)) {
                if (!prot.owner.equals(event.getPlayer().getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
