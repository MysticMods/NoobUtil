package noobanidus.libs.noobutil.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Stream;

/**
 * Functions for iterating over block positions, primarily involving AxisAlignedBB
 */
@SuppressWarnings("unused")
public class BlockPosUtil {
  /**
   * Note that this function coerces floating point values into integers.
   *
   * @param box AxisAlignedBB defining the area you wish to iterate over
   * @return A *mutable* iterable consisting of each block position within `box`.
   */
  public static Iterable<BlockPos> getAllInBoxMutable (AxisAlignedBB box) {
    return BlockPos.betweenClosed((int)box.maxX, (int)box.maxY, (int)box.maxZ, (int)box.minX, (int)box.minY, (int)box.minZ);
  }

  /**
   * Note that this function coerces floating point values into integers.
   *
   * @param box AxisAlignedBB defining the area you wish to iterate over
   * @return A stream of BlockPos (non-mutable) consisting of each block position within `box`
   */
  public static Stream<BlockPos> getAllInBox (AxisAlignedBB box) {
    return BlockPos.betweenClosedStream((int)box.maxX, (int)box.maxY, (int)box.maxZ, (int)box.minX, (int)box.minY, (int)box.minZ);
  }
}
