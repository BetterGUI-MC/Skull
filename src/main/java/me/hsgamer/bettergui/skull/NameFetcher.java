package me.hsgamer.bettergui.skull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;

public class NameFetcher {

  private static final JsonParser parser = new JsonParser();
  private static final Map<UUID, String> cache = new HashMap<>();

  private NameFetcher() {
    // EMPTY
  }

  public static String nameFromUuid(UUID uuid) {
    if (cache.containsKey(uuid)) {
      return cache.get(uuid);
    }
    try {
      URL url = new URL("https://api.mojang.com/user/profiles/"
          + uuid.toString().replace("-", "").toLowerCase() + "/names"
      );
      InputStreamReader readId = new InputStreamReader(url.openStream());
      JsonObject jObject = parser.parse(readId).getAsJsonObject();
      if (mojangError(jObject)) {
        return null;
      }

      String finalName = null;
      JsonArray array = jObject.getAsJsonArray();
      for (JsonElement jsonElement : array) {
        JsonObject namePack = jsonElement.getAsJsonObject();
        finalName = namePack.getAsJsonObject("name").getAsString();
      }
      cache.put(uuid, finalName);
      return finalName;
    } catch (Exception e) {
      // EMPTY
    }
    return null;
  }

  public static void invalidateCache() {
    cache.clear();
  }

  private static boolean mojangError(JsonObject jsonObject) {
    if (!jsonObject.has("error")) {
      return false;
    }

    String err = jsonObject.get("error").getAsString();
    String msg = jsonObject.get("errorMessage").getAsString();
    Bukkit.getLogger().warning(() -> "Mojang Error: " + err + ", Msg: " + msg);
    return true;
  }
}
