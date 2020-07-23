/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Crypto Morin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.hsgamer.bettergui.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.lib.xseries.XMaterial;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {

  private static final Pattern BASE64 = Pattern
      .compile("(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?");

  private SkullUtils() {

  }

  @SuppressWarnings("deprecation")
  public static ItemMeta parseSkull(ItemMeta itemMeta, String skullOwner) {
    if (itemMeta instanceof SkullMeta) {
      if (skullOwner.contains("textures.minecraft.net")) {
        setSkullWithURL((SkullMeta) itemMeta, skullOwner);
      } else if (BASE64.matcher(skullOwner).matches()) {
        setSkullWithBase64((SkullMeta) itemMeta, skullOwner);
      } else if (isUUID(skullOwner)) {
        UUID uuid = UUID.fromString(skullOwner);
        if (XMaterial.supports(12)) {
          ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        } else {
          ((SkullMeta) itemMeta).setOwner(NameFetcher.nameFromUuid(uuid));
        }
      } else {
        ((SkullMeta) itemMeta).setOwner(skullOwner);
      }
    }
    return itemMeta;
  }

  private static boolean isUUID(String id) {
    return id.length() == 36 && StringUtils.countMatches(id, "-") == 4;
  }

  public static void setSkullWithURL(SkullMeta skullMeta, String url) {
    byte[] encodedData = Base64.getEncoder()
        .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    setSkullWithBase64(skullMeta, new String(encodedData));
  }

  public static void setSkullWithBase64(SkullMeta skullMeta, String base64) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", base64));
    try {
      Field profileField;
      profileField = skullMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(skullMeta, profile);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      BetterGUI.getInstance().getLogger()
          .log(Level.WARNING, "Unexpected error when getting skull", e);
    }
  }
}