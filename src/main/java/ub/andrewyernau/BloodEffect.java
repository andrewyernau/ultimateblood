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
            entityBloodMap.put(EntityType.ALLAY, "passive-mob.Wither");
            entityBloodMap.put(EntityType.BAT, "passive-mobs.Bat");
            entityBloodMap.put(EntityType.CAT, "passive-mobs.Cat");
            entityBloodMap.put(EntityType.CHICKEN, "passive-mobs.Chicken");
            entityBloodMap.put(EntityType.COD, "passive-mobs.Cod");
            entityBloodMap.put(EntityType.COW, "passive-mobs.Cow");
            entityBloodMap.put(EntityType.DONKEY, "passive-mobs.Donkey");
            entityBloodMap.put(EntityType.FOX, "passive-mobs.Fox");
            entityBloodMap.put(EntityType.GLOW_SQUID, "passive-mobs.GlowingSquid");
            entityBloodMap.put(EntityType.HORSE, "passive-mobs.Horse");
            entityBloodMap.put(EntityType.MULE, "passive-mobs.Mule");
            entityBloodMap.put(EntityType.OCELOT, "passive-mobs.Ocelot");
            entityBloodMap.put(EntityType.PARROT, "passive-mobs.Parrot");
            entityBloodMap.put(EntityType.PIG, "passive-mobs.Pig");
            entityBloodMap.put(EntityType.POLAR_BEAR, "passive-mobs.PolarBear");
            entityBloodMap.put(EntityType.PUFFERFISH, "passive-mobs.Pufferfish");
            entityBloodMap.put(EntityType.RABBIT, "passive-mobs.Rabbit");
            entityBloodMap.put(EntityType.SALMON, "passive-mobs.Salmon");
            entityBloodMap.put(EntityType.SHEEP, "passive-mobs.Sheep");
            entityBloodMap.put(EntityType.SQUID, "passive-mobs.Squid");
            entityBloodMap.put(EntityType.STRIDER, "passive-mobs.Strider");
            entityBloodMap.put(EntityType.TROPICAL_FISH, "passive-mobs.TropicalFish");
            entityBloodMap.put(EntityType.TURTLE, "passive-mobs.Turtle");
            entityBloodMap.put(EntityType.VILLAGER, "passive-mobs.Villager");
            entityBloodMap.put(EntityType.WANDERING_TRADER, "passive-mobs.WanderingTrader");
        }
        if(plugin.getConfig().getBoolean("neutral.Enabled")){
            entityBloodMap.put(EntityType.BEE, "neutral-mobs.Bee");
            entityBloodMap.put(EntityType.DOLPHIN, "neutral-mobs.Dolphin");
            entityBloodMap.put(EntityType.ENDERMAN, "neutral-mobs.Enderman");
            entityBloodMap.put(EntityType.GOAT, "neutral-mobs.Goat");
            entityBloodMap.put(EntityType.IRON_GOLEM,"neutral-mobs.IronGolem");
            entityBloodMap.put(EntityType.LLAMA, "neutral-mobs.Llama");
            entityBloodMap.put(EntityType.PANDA, "neutral-mobs.Panda");
            entityBloodMap.put(EntityType.POLAR_BEAR,"neutral-mobs.PolarBear");
            entityBloodMap.put(EntityType.TRADER_LLAMA, "neutral-mobs.TraderLlama");
            entityBloodMap.put(EntityType.WOLF, "neutral-mobs.Wolf");
            entityBloodMap.put(EntityType.ZOMBIFIED_PIGLIN, "neutral-mobs.ZombifiedPiglin");
        }
        if(plugin.getConfig().getBoolean("hostile.Enabled")){
            entityBloodMap.put(EntityType.BLAZE, "hostile-mobs.Blaze");
            entityBloodMap.put(EntityType.BREEZE, "hostile-mobs.Breeze");
            entityBloodMap.put(EntityType.BOGGED, "hostile-mobs.Bogged");
            entityBloodMap.put(EntityType.CREEPER, "hostile-mobs.Creeper");
            entityBloodMap.put(EntityType.DROWNED, "hostile-mobs.Drowned");
            entityBloodMap.put(EntityType.CAVE_SPIDER, "hostile-mobs.CaveSpider");
            entityBloodMap.put(EntityType.ENDERMITE, "hostile-mobs.Endermite");
            entityBloodMap.put(EntityType.EVOKER, "hostile-mobs.Evoker");
            entityBloodMap.put(EntityType.GHAST, "hostile-mobs.Ghast");
            entityBloodMap.put(EntityType.GUARDIAN, "hostile-mobs.Guardian");
            entityBloodMap.put(EntityType.HOGLIN, "hostile-mobs.Hoglin");
            entityBloodMap.put(EntityType.HUSK, "hostile-mobs.Husk");
            entityBloodMap.put(EntityType.MAGMA_CUBE, "hostile-mobs.MagmaCube");
            entityBloodMap.put(EntityType.PHANTOM, "hostile-mobs.Phantom");
            entityBloodMap.put(EntityType.PIGLIN, "hostile-mobs.Piglin");
            entityBloodMap.put(EntityType.PILLAGER, "hostile-mobs.Pillager");
            entityBloodMap.put(EntityType.RAVAGER, "hostile-mobs.Ravager");
            entityBloodMap.put(EntityType.SHULKER, "hostile-mobs.Shulker");
            entityBloodMap.put(EntityType.SILVERFISH, "hostile-mobs.Silverfish");
            entityBloodMap.put(EntityType.SKELETON, "hostile-mobs.Skeleton");
            entityBloodMap.put(EntityType.SLIME, "hostile-mobs.Slime");
            entityBloodMap.put(EntityType.SPIDER, "hostile-mobs.Spider");
            entityBloodMap.put(EntityType.STRAY, "hostile-mobs.Stray");
            entityBloodMap.put(EntityType.VEX, "hostile-mobs.Vex");
            entityBloodMap.put(EntityType.VINDICATOR, "hostile-mobs.Vindicator");
            entityBloodMap.put(EntityType.WITCH, "hostile-mobs.Witch");
            entityBloodMap.put(EntityType.WITHER_SKELETON, "hostile-mobs.WitherSkeleton");
            entityBloodMap.put(EntityType.ZOMBIE, "hostile-mobs.Zombie");
            entityBloodMap.put(EntityType.ZOMBIE_VILLAGER, "hostile-mobs.ZombieVillager");
        }
        if(plugin.getConfig().getBoolean("boss.Enabled")){
            entityBloodMap.put(EntityType.ENDER_DRAGON, "boss-mobs.EnderDragon");
            entityBloodMap.put(EntityType.ELDER_GUARDIAN, "boss-mobs.ElderGuardian");
            entityBloodMap.put(EntityType.WARDEN, "boss-mobs.Warden");
            entityBloodMap.put(EntityType.WITHER, "boss-mobs.Wither");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        EntityType entityType = entity.getType();
        if (this.plugin.getConfig().getBoolean("enable-blood-particles") && entityBloodMap.containsKey(entityType)) {
            String configPath = entityBloodMap.get(entityType);
            Material bloodMaterial = Material.getMaterial(this.plugin.getConfig().getString(configPath));

            if (bloodMaterial != null) {
                entity.getLocation().getWorld().playEffect(entity.getLocation().add(0.0D, 0.5D, 0.0D),
                        Effect.STEP_SOUND, bloodMaterial);
            }
        }
    }
}