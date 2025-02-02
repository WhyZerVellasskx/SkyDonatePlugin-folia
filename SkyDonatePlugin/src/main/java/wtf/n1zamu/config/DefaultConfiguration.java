package wtf.n1zamu.config;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import wtf.n1zamu.SkyDonatePlugin;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfiguration {
    private final SkyDonatePlugin plugin;

    public DefaultConfiguration(SkyDonatePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SkyDonate] Список товаров: " + getActionsByPurchase().toString());
    }

    public Map<Integer, List<String>> getActionsByPurchase() {
        Map<Integer, List<String>> actionsMap = new HashMap<>();
        if (!plugin.getConfig().contains("products")) {
            plugin.getConfig().set("products", new ArrayList<>());
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("products");
        if (section == null) {
            return null;
        }
        section.getKeys(false).forEach(key -> actionsMap.put(Integer.parseInt(key.split("id")[1]), section.getStringList(key + ".actions")));
        return actionsMap;
    }

    public Map<String, Integer> getGroupId() {
        Map<String, Integer> idGroupMap = new HashMap<>();
        if (!plugin.getConfig().contains("group")) {
            plugin.getConfig().set("group", new ArrayList<>());
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("group");
        if (section == null) {
            return null;
        }
        section.getKeys(false).forEach(key -> {
            String value = section.getString(key);
            if (value == null) {
                return;
            }
            idGroupMap.put("group." + key, Integer.parseInt(value.split("id")[1]));
        });
        return idGroupMap;
    }

    @SneakyThrows
    public void saveConfigWithCommentaries(String store_id, String server, String secretKey) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        List<String> configLines = new ArrayList<>();
        if (!configFile.exists()) {
            return;
        }
        FileReader reader = new FileReader(configFile);
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        String[] lines = sb.toString().split("\n");

        for (String line : lines) {
            if (line.startsWith("SERVER_ID:")) {
                configLines.add("SERVER_ID: " + server);
                continue;
            }
            if (line.startsWith("STORE_ID:")) {
                configLines.add("STORE_ID: " + store_id);
                continue;
            }
            if (line.startsWith("SECRET_KEY:")) {
                configLines.add("SECRET_KEY: " + secretKey);
                continue;
            }
            configLines.add(line);
        }

        Files.write(configFile.toPath(), configLines);
        if (plugin.getConfig().getBoolean("debug")) {
            Bukkit.getLogger().info("[SkyDonate] Конфиг был обновлен!");
        }
    }
}
