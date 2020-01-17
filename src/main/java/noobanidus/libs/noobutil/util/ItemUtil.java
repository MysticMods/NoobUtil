package noobanidus.mods.wishingforsunshine.util;

import net.minecraft.item.ItemStack;

public class ItemUtil {
  public static boolean equalWithoutSize(ItemStack item1, ItemStack item2) {
    if (item1.getItem() != item2.getItem()) {
      return false;
    } else if (item1.getTag() == null && item2.getTag() != null) {
      return false;
    } else {
      return (item1.getTag() == null || item1.getTag().equals(item2.getTag())) && item1.areCapsCompatible(item2);
    }
  }
}
