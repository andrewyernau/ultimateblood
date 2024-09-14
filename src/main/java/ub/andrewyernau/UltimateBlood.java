package ub.andrewyernau;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class UltimateBlood extends JavaPlugin implements Listener {
    private static UltimateBlood instance;
    private FileConfiguration messagesConfig;
    private String language;
    File messagesFile ;
    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        saveResource("lang/en.yml", false);
        saveResource("lang/es.yml", false);
        saveResource("lang/de.yml", false);
        saveResource("lang/fr.yml", false);
        saveResource("lang/ru.yml", false);

        getConfig().options().copyDefaults(true);

        FileConfiguration config = getConfig();
        language = config.getString("language", "en");

        messagesFile = new File(getDataFolder(), "lang/" + language + ".yml");

        if (!messagesFile.exists()) {
            saveResource("lang/" + language + ".yml", true);
        }
        getLogger().info("Using language: " + language);
        getLogger().info("Supported languages: de, en, es, fr, ru. Modify your language in the config.yml file and reload the plugin");
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);




        new BloodEffect(this);
        new GUIMenu(this);
        new BleedingEffectManagement(this);
        loadBandageRecipe();
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public void loadBandageRecipe() {
        ItemStack bandage = createBandage();
        String[] shape = getConfig().getStringList("bandage-recipe.shape").toArray(new String[0]);
        NamespacedKey recipeKey = new NamespacedKey(this, "bandage");
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, bandage);
        recipe.shape(shape[0], shape[1], shape[2]);

        getConfig().getConfigurationSection("bandage-recipe.ingredients").getKeys(false).forEach(charKey -> {
            Material material = Material.matchMaterial(getConfig().getString("bandage-recipe.ingredients." + charKey));
            if (material != null) {
                recipe.setIngredient(charKey.charAt(0), material);
            }
        });
        Bukkit.addRecipe(recipe);
        getLogger().info(this.getMessagesConfig().getString("messages.bandage_recipe_loaded", "Bandage recipe loaded successfully!"));
    }

    public ItemStack createBandage() {
        ItemStack bandage = new ItemStack(Material.PAPER);
        ItemMeta meta = bandage.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(this.getMessagesConfig().getString("messages.bandage","§aBandage"));
            NamespacedKey key = new NamespacedKey(this, "venda_autentica");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "venda_verdadera");
            bandage.setItemMeta(meta);
        }
        return bandage;
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLable, String[] args) {
        if (args.length == 1) {
            List<String> args1 = new ArrayList<String>();
            args1.add("help");
            args1.add("reload");
            args1.add("gui");
            return args1;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ultimatebleeding") || command.getName().equalsIgnoreCase("ub")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("ub.reload")) {
                    this.reloadConfig();
                    FileConfiguration messagesConfig = getMessagesConfig();
                    try {
                        messagesConfig.load(messagesFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage(this.getMessagesConfig().getString("messages.config_reloaded", "UltimateBlood configuration reloaded"));
                    return true;
                } else {
                    sender.sendMessage(this.getMessagesConfig().getString("messages.no_permission", "§cYou do not have permission to use this command."));
                    return false;
                }
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("ub.toggle")) {
                        GUIMenu.openGUI(player);
                        return true;
                    } else {
                        player.sendMessage(this.getMessagesConfig().getString("messages.no_permission", "§cYou do not have permission to use this command."));
                        return false;
                    }
                } else {
                    sender.sendMessage(this.getMessagesConfig().getString("messages.not_player", "§cThis command can only be used by players."));
                    return false;
                }
            }
        }
        return false;
    }
}
