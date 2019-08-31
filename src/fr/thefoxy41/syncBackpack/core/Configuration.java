package fr.thefoxy41.syncBackpack.core;

import fr.thefoxy41.syncBackpack.core.managers.FileManager;
import fr.thefoxy41.syncBackpack.utils.UUIDUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Configuration {
    public static final String FILE_NAME = "config.yml";
    public static String FILE_PATH;

    public static String SERVER_NAME;

    public static String CHESTS_TABLE;
    public static String BACKPACKS_TABLE;
    public static String BACKPACKS_ITEMS_TABLE;

    public static void init(Plugin plugin) throws InvalidConfigurationException {
        FILE_PATH = plugin.getDataFolder().toString();

        try {
            checkDefaultConfig(plugin);
        } catch (IOException | org.apache.logging.log4j.core.config.ConfigurationException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        YamlConfiguration configuration = FileManager.getConfiguration(new File(FILE_PATH, FILE_NAME));
        if (configuration == null) throw new InvalidConfigurationException("Unable to load plugin configuration file");

        SERVER_NAME = configuration.getString("server_id");
        CHESTS_TABLE = configuration.getString("chests_table");
        BACKPACKS_TABLE = configuration.getString("backpacks_table");
        BACKPACKS_ITEMS_TABLE = configuration.getString("items_table");
    }

    private static void checkDefaultConfig(Plugin plugin) throws InvalidConfigurationException, IOException {
        File configFile = new File(FILE_PATH, FILE_NAME);
        if (!configFile.exists()) {
            plugin.getLogger().info("Default configuration file has been moved to " + FILE_PATH + "/" + FILE_NAME);

            InputStream stream = Configuration.class.getClassLoader().getResourceAsStream(FILE_NAME);
            YamlConfiguration configuration = new YamlConfiguration();

            if (stream != null) {
                configuration.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
                configuration.set("server_id", UUIDUtils.randomToString());
                stream.close();
            }

            FileManager.save(configuration, configFile);
        }
    }
}
