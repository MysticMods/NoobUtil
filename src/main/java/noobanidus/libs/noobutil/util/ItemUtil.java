package noobanidus.libs.noobutil.util;

import net.minecraft.item.ItemStack;

/**
 * Utility functions for interacting with items.
 */
@SuppressWarnings("unused")
public class ItemUtil {
  /**
   * Compares two itemstacks without considering their size.
   *
   * @param item1 First item to compare.
   * @param item2 Second item to compare.
   * @return True if the items, tags, and capabilities between `item1` and `item2` match, disregarding size.
   */
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
