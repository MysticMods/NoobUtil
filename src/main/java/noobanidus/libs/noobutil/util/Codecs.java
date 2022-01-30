package noobanidus.libs.noobutil.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

public class Codecs {
  public static Codec<Vec3> VECTOR_3D = RecordCodecBuilder.create(i -> i.group(
      Codec.DOUBLE.fieldOf("x").forGetter(v -> v.x),
      Codec.DOUBLE.fieldOf("y").forGetter(v -> v.y),
      Codec.DOUBLE.fieldOf("z").forGetter(v -> v.z)
  ).apply(i, Vec3::new));
}
