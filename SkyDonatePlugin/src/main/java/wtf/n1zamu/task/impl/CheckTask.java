package wtf.n1zamu.task.impl;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wtf.n1zamu.SkyDonatePlugin;
import wtf.n1zamu.api.dto.Purchase;
import wtf.n1zamu.http.Request;
import wtf.n1zamu.task.ITask;

import java.util.*;
import java.util.stream.Collectors;

public class CheckTask implements ITask {
    @Setter
    private Request request;
    private final SkyDonatePlugin skyDonatePlugin;
    private final Map<Integer, String> codeErrors = new HashMap<>();

    public CheckTask(SkyDonatePlugin skyDonatePlugin) {
        Bukkit.getLogger().info("[SkyDonate] Задача успешно запущена!");
        this.skyDonatePlugin = skyDonatePlugin;
        this.request = new Request(String.format(
                "https://api.skydonate.ru/api/v2/method/orders.received?server_id=%s&store_id=%s&secret_key=%s",
                skyDonatePlugin.getConfig().getString("SERVER_ID"),
                skyDonatePlugin.getConfig().getString("STORE_ID"),
                skyDonatePlugin.getConfig().getString("SECRET_KEY")));
        this.codeErrors.put(7001, "Неверный формат ServerID!");
        this.codeErrors.put(7002, "Неверный формат StoreID!");
        this.codeErrors.put(7003, "Неверный формат SecretKey!");
        this.codeErrors.put(7004, "Неверные данные или несуществующий магазин!");
    }

    @Override
    public Runnable getTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                JSONObject response = request.getResponse();
                if (response == null) {
                    return;
                }
                boolean success = (boolean) response.get("success");

                if (!success) {
                    if (isDebugged()) {
                        JSONObject error = (JSONObject) response.get("error");
                        Long errorCode = (Long) error.get("error_code");
                        Bukkit.getLogger().info("[SkyDonate] Не удалось получить ответ! Причина: " + codeErrors.get(errorCode.intValue()));
                    }
                    return;
                }
                JSONArray responseArray = (JSONArray) response.get("response");

                if (responseArray.isEmpty()) {
                    return;
                }

                for (Object resp : responseArray) {
                    JSONObject product = (JSONObject) resp;
                    String username = (String) product.get("username");
                    Long productId = (Long) product.get("product_id");
                    Purchase purchase = new Purchase(username, productId.toString(), null, null);
                    if (username == null) {
                        return;
                    }
                    JSONObject storage = null;
                    if (product.get("storage") != null) {
                        JSONParser parser = new JSONParser();
                        try {
                            storage = (JSONObject) parser.parse(product.get("storage").toString());
                        } catch (ParseException e) {
                            if (isDebugged()) {
                                Bukkit.getLogger().warning("[SkyDonate] Ошибка декодирования storage для продукта ID " + productId + ": " + e.getMessage());
                            }
                        } catch (ClassCastException e) {
                            if (isDebugged()) {
                                Bukkit.getLogger().info("[SkyDonate] Покупка доната, storage пуст!");
                            }
                        }
                    }
                    if (isDebugged()) {
                        Bukkit.getLogger().info("[SkyDonate] Найдена покупка для игрока " + username + " ее айди " + productId.intValue());
                    }
                    JSONObject finalStorage = storage;
                    skyDonatePlugin.getDefaultConfiguration().getActionsByPurchase().forEach((key, value) -> {
                        if (key != productId.intValue()) {
                            return;
                        }
                        List<String> commandsToExecute = value.stream()
                                .map(line -> {
                                    String finalLine = line.replace("{player}", username);
                                    if (finalStorage != null) {
                                        Map<String, String> placeholders = new HashMap<>();
                                        for (Object stKey : finalStorage.keySet()) {
                                            placeholders.put("{" + stKey + "}", finalStorage.get(stKey.toString()).toString());
                                            purchase.setPlaceholders(placeholders);
                                            finalLine = finalLine.replace("{" + stKey + "}", finalStorage.get(stKey.toString()).toString());
                                        }
                                    }
                                    return finalLine;
                                })
                                .collect(Collectors.toList());
                        purchase.setUsedCommands(commandsToExecute);
                        getPurchases().add(purchase);
                    });
                }
            }
        };
    }
}
