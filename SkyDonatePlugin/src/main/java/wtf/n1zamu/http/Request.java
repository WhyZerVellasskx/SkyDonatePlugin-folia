package wtf.n1zamu.http;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wtf.n1zamu.SkyDonatePlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String requestURL;
    private final Map<Integer, String> errorCodes;

    public Request(String URL) {
        this.requestURL = URL;
        this.errorCodes = new HashMap<>();
        this.errorCodes.put(7104, "Продукт не найден!");
        this.errorCodes.put(7105, "Продукт найден, но не прикреплен к серверу!");
        this.errorCodes.put(7106, "Тип продукта - не привилегия!");
        this.errorCodes.put(7107, "Продукт дешевле, чем последний, приобретенный игроком!");
        this.errorCodes.put(7110, "У игрока уже есть данная привилегия!");
    }

    @SneakyThrows
    public JSONObject getResponse() {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(this.requestURL).openConnection();
        } catch (UnknownHostException e) {
            Bukkit.getLogger().info("[SkyDonate] Вы не подключены к интернету!");
        }
        if (connection == null) {
            return null;
        }
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        if (connection.getResponseCode() == 401) {
            if (SkyDonatePlugin.getPlugin(SkyDonatePlugin.class).getConfig().getBoolean("debug")) {
                Bukkit.getLogger().info("[SkyDonate] Запрос был заблокирован!");
            }
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        bufferedReader.lines().forEach(response::append);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response.toString());
    }

    @SneakyThrows
    public void sendData(Map<String, Integer> cache, String server, String store, String secretKey) {
        JSONObject jsonData = new JSONObject();

        JSONObject connect = new JSONObject();
        connect.put("store_id", store);
        connect.put("server_id", server);
        connect.put("secret_key", secretKey);

        jsonData.put("connect", connect);

        JSONArray dataArray = new JSONArray();

        cache.forEach((key, value) -> {
            JSONObject dataItem = new JSONObject();
            dataItem.put("username", key);
            dataItem.put("product_id", value);
            dataArray.add(dataItem);
        });

        jsonData.put("data", dataArray);

        HttpURLConnection connection = (HttpURLConnection) new URL(this.requestURL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream output = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
        writer.write(jsonData.toJSONString());
        writer.flush();
        if (!SkyDonatePlugin.getPlugin(SkyDonatePlugin.class).getConfig().getBoolean("debug")) {
            return;
        }
        StringBuilder response = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        bufferedReader.lines().forEach(response::append);

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());

        JSONObject responseCategory = (JSONObject) jsonResponse.get("response");
        if (responseCategory == null) {
            return;
        }

        for (Object key : responseCategory.keySet()) {
            int errorCode = 0;
            try {
                errorCode = Integer.parseInt((String) key);
            } catch (NumberFormatException ignored) {

            }
            if (!errorCodes.containsKey(errorCode)) {
                continue;
            }
            Bukkit.getLogger().info("[SkyDonate] Возникла ошибка: " + errorCodes.get(errorCode));
        }
    }
}


