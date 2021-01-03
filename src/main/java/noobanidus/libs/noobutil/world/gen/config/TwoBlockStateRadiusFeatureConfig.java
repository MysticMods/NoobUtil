package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TwoBlockStateRadiusFeatureConfig implements IFeatureConfig {
  public static final Codec<TwoBlockStateRadiusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BlockStateProvider.CODEC.fieldOf("provider1").forGetter((s) -> s.provider1), BlockStateProvider.CODEC.fieldOf("provider2").forGetter((s) -> s.provider2), Codec.INT.fieldOf("startRadius").forGetter((s) -> s.startRadius)).apply(instance, TwoBlockStateRadiusFeatureConfig::new));
  public final BlockStateProvider provider1;
  public final BlockStateProvider provider2;
  public final int startRadius;

  public TwoBlockStateRadiusFeatureConfig(BlockStateProvider provider1, BlockStateProvider provider2, int startRadius) {
    this.provider1 = provider1;
    this.provider2 = provider2;
    this.startRadius = startRadius;
  }
}
