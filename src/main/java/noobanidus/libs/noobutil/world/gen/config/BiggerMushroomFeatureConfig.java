package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;

public class BiggerMushroomFeatureConfig extends BigMushroomFeatureConfig {
  public static final Codec<BiggerMushroomFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(
          BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter((o) -> o.capProvider),
          BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter((o) -> o.stemProvider),
          Codec.INT.fieldOf("foliage_radius").orElse(2).forGetter((o) -> o.foliageRadius),
          Codec.INT.fieldOf("a").orElse(4).forGetter(o -> o.a),
          Codec.INT.fieldOf("b").orElse(2).forGetter(o -> o.b)
      ).apply(instance, BiggerMushroomFeatureConfig::new));


  private final int a;
  private final int b;

  public BiggerMushroomFeatureConfig(BlockStateProvider p_i225832_1_, BlockStateProvider p_i225832_2_, int p_i225832_3_, int a, int b) {
    super(p_i225832_1_, p_i225832_2_, p_i225832_3_);
    this.a = a;
    this.b = b;
  }

  public int getA() {
    return a;
  }

  public int getB() {
    return b;
  }
}
