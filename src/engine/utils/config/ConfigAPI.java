package engine.utils.config;

import engine.utils.config.impl.Config;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The ConfigAPI class provides functionality for managing configuration settings.
 * It allows setting, getting, populating, and saving configuration settings to a file.
 * This code should not be touched unless you know what you are doing.
 */
public class ConfigAPI {

    /**
     * The map to store configuration key-value pairs.
     */
    private final Map<String, String> configMap;

    /**
     * The file path where the configuration settings are stored.
     */
    private final String filePath;

    /**
     * Constructs a ConfigAPI object with the specified file path.
     *
     * @param filePath The file path where the configuration settings are stored.
     */
        public ConfigAPI(String filePath) throws IOException {
            this.filePath = filePath;
            this.configMap = new HashMap<>();

            Path configFile = Path.of(filePath);
            Path parentDirectory = configFile.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            if (!Files.exists(configFile)) {
                Files.createFile(configFile);
            }

            loadConfig();
        }

    /**
     * Sets a configuration value for the specified key.
     *
     * @param key   The key of the configuration setting.
     * @param value The value of the configuration setting.
     * @throws IllegalArgumentException If the value is of an unsupported data type.
     */
    public void setValue(String key, Object value) throws IllegalArgumentException {
        if (value instanceof String || value instanceof Integer ||
                value instanceof Boolean || value instanceof Double) {
            configMap.put(key, value.toString());
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }

    /**
     * Gets the string value of the configuration setting associated with the specified key.
     *
     * @param key The key of the configuration setting.
     * @return The string value of the configuration setting.
     */
    public String getStringValue(String key) {
        return configMap.get(key);
    }

    /**
     * Gets the integer value of the configuration setting associated with the specified key.
     *
     * @param key The key of the configuration setting.
     * @return The integer value of the configuration setting.
     */
    public int getIntValue(String key) {
        return Integer.parseInt(configMap.get(key));
    }

    /**
     * Gets the boolean value of the configuration setting associated with the specified key.
     *
     * @param key The key of the configuration setting.
     * @return The boolean value of the configuration setting.
     */
    public boolean getBooleanValue(String key) {
        return Boolean.parseBoolean(configMap.get(key));
    }

    /**
     * Gets the double value of the configuration setting associated with the specified key.
     *
     * @param key The key of the configuration setting.
     * @return The double value of the configuration setting.
     */
    public double getDoubleValue(String key) {
        return Double.parseDouble(configMap.get(key));
    }

    /**
     * Populates the fields of a Config object with the configuration settings from the file.
     *
     * @param config The Config object to populate.
     * @throws IOException If an I/O error occurs while reading the configuration file.
     */
    public void populateConfig(Config config) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(input);

            Field[] fields = Config.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (properties.containsKey(fieldName)) {
                    try {
                        field.set(config, properties.getProperty(fieldName));
                    } catch (IllegalAccessException e) {
                        System.out.println("IllegalAccessException thrown during populateConfig: "+e);
                    }
                }
            }
        }
    }

    /**
     * Populates the configMap of the ConfigAPI with the values from the provided Config object.
     *
     * @param config The Config object from which to populate the configMap.
     */
    public void populateFromConfig(Config config) throws IllegalAccessException {
        Field[] fields = config.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(config);
            if (value != null) {
                setValue(field.getName(), value);
            }
        }
    }

    /**
     * Saves the configuration settings to the file.
     *
     * @throws IOException If an I/O error occurs while writing to the configuration file.
     */
    public void save() throws IOException {
        Path parentDirectory = Path.of(filePath).getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (OutputStream output = new FileOutputStream(filePath)) {
            Properties properties = new Properties();
            properties.putAll(configMap);
            properties.store(output, "Configuration file");
        }
    }

    /**
     * Loads the configuration settings from the file into the configMap.
     */
    private void loadConfig() {
        try (InputStream input = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(input);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                configMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            System.out.println("Configuration file not found. Creating a new one.");
        }
    }
}
