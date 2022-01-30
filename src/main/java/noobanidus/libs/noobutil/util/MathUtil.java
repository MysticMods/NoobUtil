package noobanidus.libs.noobutil.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

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
  public static Vec3 rotateX(@Nonnull Vec3 v, float angle) {
    return new Vec3(v.x * Mth.cos(angle) - v.y * Mth.sin(angle), v.x * Mth.sin(angle) + v.y * Mth.cos(angle), v.z);
  }

  @Nonnull
  public static Vec3 rotateZ(@Nonnull Vec3 v, float angle) {
    return new Vec3(v.x, v.y * Mth.cos(angle) - v.z * Mth.sin(angle), v.y * Mth.sin(angle) + v.z * Mth.cos(angle));
  }

  @Nonnull
  public static Vec3 rotateY(@Nonnull Vec3 v, float angle) {
    return new Vec3(v.z * Mth.cos(angle) - v.x * Mth.sin(angle), v.y, v.z * Mth.sin(angle) + v.x * Mth.cos(angle));
  }
}
