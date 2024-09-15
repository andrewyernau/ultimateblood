package ub.andrewyernau;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BloodEffect implements Listener {

    UltimateBlood plugin;
    private Map<EntityType, String> entityBloodMap = new HashMap<>();

    public BloodEffect(UltimateBlood plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        loadBloodTypes();
    }


    private void loadBloodTypes() {
        if (plugin.getConfig().getBoolean("player.Enabled")) {
            entityBloodMap.put(EntityType.PLAYER, "player.BloodType");
        }

        if (plugin.getConfig().getBoolean("passive.Enabled")) {
            safelyAddEntityBloodType("ALLAY", "passive-mob.Wither");
            safelyAddEntityBloodType("BAT", "passive-mobs.Bat");
            safelyAddEntityBloodType("CAT", "passive-mobs.Cat");
            safelyAddEntityBloodType("CHICKEN", "passive-mobs.Chicken");
            safelyAddEntityBloodType("COD", "passive-mobs.Cod");
            safelyAddEntityBloodType("COW", "passive-mobs.Cow");
            safelyAddEntityBloodType("DONKEY", "passive-mobs.Donkey");
            safelyAddEntityBloodType("FOX", "passive-mobs.Fox");
            safelyAddEntityBloodType("GLOW_SQUID", "passive-mobs.GlowingSquid");
            safelyAddEntityBloodType("HORSE", "passive-mobs.Horse");
            safelyAddEntityBloodType("MULE", "passive-mobs.Mule");
            safelyAddEntityBloodType("OCELOT", "passive-mobs.Ocelot");
            safelyAddEntityBloodType("PARROT", "passive-mobs.Parrot");
            safelyAddEntityBloodType("PIG", "passive-mobs.Pig");
            safelyAddEntityBloodType("POLAR_BEAR", "passive-mobs.PolarBear");
            safelyAddEntityBloodType("PUFFERFISH", "passive-mobs.Pufferfish");
            safelyAddEntityBloodType("RABBIT", "passive-mobs.Rabbit");
            safelyAddEntityBloodType("SALMON", "passive-mobs.Salmon");
            safelyAddEntityBloodType("SHEEP", "passive-mobs.Sheep");
            safelyAddEntityBloodType("SQUID", "passive-mobs.Squid");
            safelyAddEntityBloodType("STRIDER", "passive-mobs.Strider");
            safelyAddEntityBloodType("TROPICAL_FISH", "passive-mobs.TropicalFish");
            safelyAddEntityBloodType("TURTLE", "passive-mobs.Turtle");
            safelyAddEntityBloodType("VILLAGER", "passive-mobs.Villager");
            safelyAddEntityBloodType("WANDERING_TRADER", "passive-mobs.WanderingTrader");
        }

        if (plugin.getConfig().getBoolean("neutral.Enabled")) {
            safelyAddEntityBloodType("BEE", "neutral-mobs.Bee");
            safelyAddEntityBloodType("DOLPHIN", "neutral-mobs.Dolphin");
            safelyAddEntityBloodType("ENDERMAN", "neutral-mobs.Enderman");
            safelyAddEntityBloodType("GOAT", "neutral-mobs.Goat");
            safelyAddEntityBloodType("IRON_GOLEM", "neutral-mobs.IronGolem");
            safelyAddEntityBloodType("LLAMA", "neutral-mobs.Llama");
            safelyAddEntityBloodType("PANDA", "neutral-mobs.Panda");
            safelyAddEntityBloodType("POLAR_BEAR", "neutral-mobs.PolarBear");
            safelyAddEntityBloodType("TRADER_LLAMA", "neutral-mobs.TraderLlama");
            safelyAddEntityBloodType("WOLF", "neutral-mobs.Wolf");
            safelyAddEntityBloodType("ZOMBIFIED_PIGLIN", "neutral-mobs.ZombifiedPiglin");
        }

        if (plugin.getConfig().getBoolean("hostile.Enabled")) {
            safelyAddEntityBloodType("BLAZE", "hostile-mobs.Blaze");
            safelyAddEntityBloodType("BREEZE", "hostile-mobs.Breeze");
            safelyAddEntityBloodType("CREEPER", "hostile-mobs.Creeper");
            safelyAddEntityBloodType("DROWNED", "hostile-mobs.Drowned");
            safelyAddEntityBloodType("CAVE_SPIDER", "hostile-mobs.CaveSpider");
            safelyAddEntityBloodType("ENDERMITE", "hostile-mobs.Endermite");
            safelyAddEntityBloodType("EVOKER", "hostile-mobs.Evoker");
            safelyAddEntityBloodType("GHAST", "hostile-mobs.Ghast");
            safelyAddEntityBloodType("GUARDIAN", "hostile-mobs.Guardian");
            safelyAddEntityBloodType("HOGLIN", "hostile-mobs.Hoglin");
            safelyAddEntityBloodType("HUSK", "hostile-mobs.Husk");
            safelyAddEntityBloodType("MAGMA_CUBE", "hostile-mobs.MagmaCube");
            safelyAddEntityBloodType("PHANTOM", "hostile-mobs.Phantom");
            safelyAddEntityBloodType("PIGLIN", "hostile-mobs.Piglin");
            safelyAddEntityBloodType("PILLAGER", "hostile-mobs.Pillager");
            safelyAddEntityBloodType("RAVAGER", "hostile-mobs.Ravager");
            safelyAddEntityBloodType("SHULKER", "hostile-mobs.Shulker");
            safelyAddEntityBloodType("SILVERFISH", "hostile-mobs.Silverfish");
            safelyAddEntityBloodType("SKELETON", "hostile-mobs.Skeleton");
            safelyAddEntityBloodType("SLIME", "hostile-mobs.Slime");
            safelyAddEntityBloodType("SPIDER", "hostile-mobs.Spider");
            safelyAddEntityBloodType("STRAY", "hostile-mobs.Stray");
            safelyAddEntityBloodType("VEX", "hostile-mobs.Vex");
            safelyAddEntityBloodType("VINDICATOR", "hostile-mobs.Vindicator");
            safelyAddEntityBloodType("WITCH", "hostile-mobs.Witch");
            safelyAddEntityBloodType("WITHER_SKELETON", "hostile-mobs.WitherSkeleton");
            safelyAddEntityBloodType("ZOMBIE", "hostile-mobs.Zombie");
            safelyAddEntityBloodType("ZOMBIE_VILLAGER", "hostile-mobs.ZombieVillager");
        }

        if (plugin.getConfig().getBoolean("boss.Enabled")) {
            safelyAddEntityBloodType("ENDER_DRAGON", "boss-mobs.EnderDragon");
            safelyAddEntityBloodType("ELDER_GUARDIAN", "boss-mobs.ElderGuardian");
            safelyAddEntityBloodType("WARDEN", "boss-mobs.Warden");
            safelyAddEntityBloodType("WITHER", "boss-mobs.Wither");
        }
    }

    private void safelyAddEntityBloodType(String entityTypeName, String configPath) {
        try {
            EntityType entityType = EntityType.valueOf(entityTypeName);
            entityBloodMap.put(entityType, configPath);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("EntityType " + entityTypeName + " is not available in this Minecraft version. Skipping...");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        try {
            Entity entity = e.getEntity();
            EntityType entityType = entity.getType();
            if (this.plugin.getConfig().getBoolean("enable-blood-particles") && entityBloodMap.containsKey(entityType)) {
                String configPath = entityBloodMap.get(entityType);
                Material bloodMaterial = Material.getMaterial(this.plugin.getConfig().getString(configPath));
                if (bloodMaterial != null) {
                    entity.getLocation().getWorld().playEffect(entity.getLocation().add(0.0D, 0.5D, 0.0D),
                            Effect.STEP_SOUND, bloodMaterial);
                } else {
                    plugin.getLogger().warning("Blood material for " + entityType + " is null or not configured properly.");
                }
            }
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().warning("Some EntityTypes are not available in this Minecraft version. This might be fine if you aren't using root version");
        } catch (NullPointerException ex) {
            plugin.getLogger().warning("An entity or material could not be found or is null.");
        } catch (Exception ex) {
            plugin.getLogger().severe("An unexpected error o2ccurred while handling entity damage: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}