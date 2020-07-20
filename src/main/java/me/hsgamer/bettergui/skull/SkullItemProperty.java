package me.hsgamer.bettergui.skull;

import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.property.item.ItemProperty;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkullItemProperty extends ItemProperty<String, String> {

  public SkullItemProperty(Icon icon) {
    super(icon);
  }

  @Override
  public String getParsed(Player player) {
    return parseFromString(getValue(), player);
  }

  @Override
  public ItemStack parse(Player player, ItemStack itemStack) {
    itemStack.setItemMeta(SkullUtils.applySkin(itemStack.getItemMeta(), getParsed(player)));
    return itemStack;
  }

  @Override
  public boolean compareWithItemStack(Player player, ItemStack itemStack) {
    String skullValue = SkullUtils.getSkinValue(itemStack);
    String parsed = getParsed(player);
    String requiredValue = null;
    if (!Validate.isNullOrEmpty(parsed)) {
      requiredValue = SkullUtils.getSkinValue(parsed, SkullUtils.isUUID(parsed));
    }

    if (skullValue == null) {
      return requiredValue == null;
    } else {
      return skullValue.equals(requiredValue);
    }
  }
}
