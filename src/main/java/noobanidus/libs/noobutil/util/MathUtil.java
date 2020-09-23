package noobanidus.libs.noobutil.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
import java.util.Random;

public class MathUtil {
  public static Random rand = new Random();

  public static int clamp(int value, int min, int max) {
    return Math.min(Math.max(value, max), min);
  }

  public static double nclamp(double d, double n) {
    return d - Math.floor(d / n) * n;
  }

  @Nonnull
  public static Vector3d rotateX(@Nonnull Vector3d v, float angle) {
    return new Vector3d(v.x * MathHelper.cos(angle) - v.y * MathHelper.sin(angle), v.x * MathHelper.sin(angle) + v.y * MathHelper.cos(angle), v.z);
  }

  @Nonnull
  public static Vector3d rotateZ(@Nonnull Vector3d v, float angle) {
    return new Vector3d(v.x, v.y * MathHelper.cos(angle) - v.z * MathHelper.sin(angle), v.y * MathHelper.sin(angle) + v.z * MathHelper.cos(angle));
  }

  @Nonnull
  public static Vector3d rotateY(@Nonnull Vector3d v, float angle) {
    return new Vector3d(v.z * MathHelper.cos(angle) - v.x * MathHelper.sin(angle), v.y, v.z * MathHelper.sin(angle) + v.x * MathHelper.cos(angle));
  }
}
