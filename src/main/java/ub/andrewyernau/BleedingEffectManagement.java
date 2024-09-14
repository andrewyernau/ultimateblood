package ub.andrewyernau;

import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BleedingEffectManagement implements Listener {

    private WorldBorderApi worldBorderApi;
    private final Random random = new Random();
    UltimateBlood plugin;
    private final NamespacedKey healingKey;
    private final Map<Player, BukkitTask> bleedingTasks = new HashMap<>();


    public BleedingEffectManagement(UltimateBlood javaPlugin) {
        this.plugin = javaPlugin;
        this.healingKey = new NamespacedKey(plugin, "venda_autentica");
        RegisteredServiceProvider<WorldBorderApi> provider = javaPlugin.getServer().getServicesManager().getRegistration(WorldBorderApi.class);
        if (provider != null) {
            worldBorderApi = provider.getProvider();
        } else {
            javaPlugin.getLogger().warning(plugin.getMessagesConfig().getString("messages.worldborderapi_not_found", "Default message"));
            javaPlugin.getServer().getPluginManager().disablePlugin(javaPlugin);
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void applyRedScreenEffect(Player player, int durationInSeconds) {
        if (worldBorderApi != null) {
            worldBorderApi.sendRedScreenForSeconds(player, durationInSeconds, plugin);
        } else {
            plugin.getLogger().warning(plugin.getMessagesConfig().getString("messages.red_screen_effect_error", "Default message"));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        String worldName = player.getWorld().getName();
        List<String> blacklistedWorlds = plugin.getConfig().getStringList("disable-world");

        if (blacklistedWorlds.contains(worldName)) {
            return;
        }

        double health = player.getHealth();
        double maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthPercentage = (health / maxHealth) * 100;

        if (plugin.getConfig().getBoolean("bleeding")) {
            double bleedChance = calculateBleedChance(healthPercentage);
            if (random.nextDouble() <= bleedChance) {
                startBleeding(player, false);
            }

            if (plugin.getConfig().getBoolean("heavy-bleeding") && random.nextDouble() <= bleedChance / 2) {
                startBleeding(player, true);
            }
        }
    }

    private double calculateBleedChance(double healthPercentage) {
        if (healthPercentage <= 10) {
            return 0.9;
        } else if (healthPercentage <= 25) {
            return 0.5;
        } else if (healthPercentage <= 50) {
            return 0.25;
        } else {
            return 0.1;
        }
    }

    private void startBleeding(Player player, boolean isHeavyBleed) {
        double damage = isHeavyBleed ? plugin.getConfig().getDouble("heavy-bleeding-damage")
                : plugin.getConfig().getDouble("bleeding-damage");
        int duration = plugin.getConfig().getInt("bleeding-duration");
        String message = isHeavyBleed ? plugin.getMessagesConfig().getString("messages.severe_bleeding", "Default message") : plugin.getMessagesConfig().getString("messages.bleeding_message", "Default message");
        player.sendMessage("§c" + message);
        applyRedScreenEffect(player, duration / 20);

        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration || player.isDead() || !player.isOnline()) {
                    cancel();
                    bleedingTasks.remove(player);
                    return;
                }
                if(ticks%80==0){
                    player.damage(damage);
                }

                ticks ++;
            }
        }.runTaskTimer(plugin, 0, 1);

        bleedingTasks.put(player, task);
    }

    @EventHandler
    public void onPlayerUseBandage(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (item != null && item.getType() == Material.PAPER) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (meta.getPersistentDataContainer().has(healingKey, PersistentDataType.STRING)) {
                    if (bleedingTasks.containsKey(player)) {
                        e.setCancelled(true);
                        player.sendMessage(plugin.getMessagesConfig().getString("messages.bandage_used", "Default message"));
                        removeBleeding(player);
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.sendMessage(plugin.getMessagesConfig().getString("messages.not_bleeding", "Default message"));
                    }
                }
            }
        }
    }

    private void removeBleeding(Player player) {
        BukkitTask task = bleedingTasks.remove(player);
        if (task != null) {
            task.cancel();
            player.sendMessage("§cSangrado detenido");
        } else {
            player.sendMessage("§cNo había sangrado para detener");
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        plugin.getLogger().info("Jugador resucitado: " + player.getName());
        removeBleeding(player);
    }

    @EventHandler
    public void onPlayerResurrect(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player player) {
            removeBleeding(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        plugin.getLogger().info("Jugador murió: " + player.getName());
        removeBleeding(player);
    }
}