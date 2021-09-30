package noobanidus.libs.noobutil.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.vector.Vector3d;

public class Codecs {
  public static Codec<Vector3d> VECTOR_3D = RecordCodecBuilder.create(i -> i.group(
      Codec.DOUBLE.fieldOf("x").forGetter(v -> v.x),
      Codec.DOUBLE.fieldOf("y").forGetter(v -> v.y),
      Codec.DOUBLE.fieldOf("z").forGetter(v -> v.z)
  ).apply(i, Vector3d::new));
}
