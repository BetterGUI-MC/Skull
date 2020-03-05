package me.hsgamer.bettergui.skull;

import com.cryptomorin.xseries.SkullUtils;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.property.item.ItemProperty;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullItemProperty extends ItemProperty<String, String> {

  public SkullItemProperty(Icon icon) {
    super(icon);
  }

  @Override
  public String getParsed(Player player) {
    Icon icon = getIcon();
    String value = getValue();
    return icon.hasVariables(value) ? icon.setVariables(value, player) : value;
  }

  @Override
  public ItemStack parse(Player player, ItemStack itemStack) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta instanceof SkullMeta) {
      itemStack.setItemMeta(SkullUtils.applySkin(itemMeta, getParsed(player)));
    }
    return itemStack;
  }

  @Override
  public boolean compareWithItemStack(Player player, ItemStack itemStack) {
    throw new UnsupportedOperationException("Cannot compare using the new method");
  }
}