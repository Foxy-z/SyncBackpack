package fr.thefoxy41.syncBackpack.core.managers;

import org.apache.logging.log4j.core.config.ConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {

    /**
     * Get YamlConfiguration from file
     * @param file File
     * @return YamlConfiguration
     */
    public static YamlConfiguration getConfiguration(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            return configuration;
        } catch (IOException | ConfigurationException | org.bukkit.configuration.InvalidConfigurationException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save configuration
     * @param configuration YamlConfiguration
     * @param file File
     */
    public static void save(YamlConfiguration configuration, File file) {
        try {
            createIfEmpty(file);
            configuration.save(file);
        } catch (ConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file file
     * @noinspection ResultOfMethodCallIgnored
     * @throws IOException exception
     */
    private static void createIfEmpty(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }
}
