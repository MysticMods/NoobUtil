package noobanidus.libs.noobutil.util;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * Functions for VoxelShape creation and manipulation
 */
@SuppressWarnings("unused")
public class VoxelUtil {
  /**
   * @param base The base voxel shape that all additional shapes will be {@link VoxelShapes::or VoxelShapes::or'd} into.
   * @param shapes Additional shapes to be combined into the main shape.
   * @return The combination of base and each item contained within `shapes`.
   */
  public static VoxelShape multiOr(VoxelShape base, VoxelShape... shapes) {
    VoxelShape result = base;

    for (VoxelShape shape : shapes) {
      result = VoxelShapes.or(result, shape);
    }

    return result;
  }

  /**
   * @param base 6 doubles defining the uppermost and lowermost points of a cube, that all additional shapes will
   *             be {@link VoxelShapes::or VoxelShapes::or'd} into.
   * @param shapes Additional shapes defined as an array of 6 doubles, to be combined into the main shape.
   * @return The combination of the base and each subsequent shape.
   */
  public static VoxelShape multiOr (double[] base, double[] ... shapes) {
    VoxelShape result = makeCube(base);

    for (double[] shape : shapes) {
      result = VoxelShapes.or(result, makeCube(shape));
    }

    return result;
  }

  /**
   * @param p 6 doubles consisting of the uppermost and lowermost points of a cube.
   * @return The VoxelShape describing that cube.
   */
  @SuppressWarnings("WeakerAccess")
  public static VoxelShape makeCube (double ... p) {
    return VoxelShapes.create(p[0] / 16.0D, p[1] / 16.0D, p[2] / 16.0D, p[3] / 16.0D, p[4] / 16.0D, p[5] / 16.0D);
  }
}
