package me.hsgamer.bettergui.skull;

import me.hsgamer.bettergui.builder.PropertyBuilder;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public boolean onLoad() {
    PropertyBuilder.registerItemProperty("head", SkullItemProperty.class);
    return true;
  }
}
