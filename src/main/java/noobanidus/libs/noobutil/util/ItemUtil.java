package noobanidus.libs.noobutil.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

  public static class Spawn {
    public static ItemEntity spawnItem(World world, BlockPos pos, ItemStack stack) {
      return spawnItem(world, pos, stack, -1);
    }

    public static ItemEntity spawnItem(World world, BlockPos pos, ItemStack stack, float hoverStart) {
      return spawnItem(world, pos, stack, true, -1, hoverStart);
    }

    public static ItemEntity spawnItem(World world, BlockPos pos, ItemStack stack, int ticks) {
      return spawnItem(world, pos, stack, true, ticks, -1);
    }

    public static ItemEntity spawnItem(World world, BlockPos pos, ItemStack stack, boolean offset) {
      return spawnItem(world, pos, stack, offset, -1, -1);
    }

    public static ItemEntity spawnItem(World world, BlockPos pos, ItemStack stack, boolean offset, int ticks, float hoverStart) {
      return spawnItem(world, pos.getX(), pos.getY(), pos.getZ(), offset, stack, ticks, hoverStart);
    }

    public static ItemEntity spawnItem(World world, double x, double y, double z, boolean offset, ItemStack stack, int ticks, float hoverStart) {
      if (offset) {
        x += 0.5;
        y += 0.5;
        z += 0.5;
      }
      ItemEntity item = new ItemEntity(world, x, y, z, stack);
      if (ticks != -1) {
        item.setPickupDelay(ticks);
      }
      if (hoverStart != -1) {
        item.hoverStart = hoverStart;
      }
      return spawnItem(world, item);
    }

    public static ItemEntity spawnItem(World world, ItemEntity item) {
      item.setMotion(0, 0, 0);
      world.addEntity(item);
      return item;
    }
  }
}
