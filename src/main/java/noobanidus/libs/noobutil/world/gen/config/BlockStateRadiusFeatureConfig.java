package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BlockStateRadiusFeatureConfig implements FeatureConfiguration {
  public static final Codec<BlockStateRadiusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BlockStateProvider.CODEC.fieldOf("provider1").forGetter((s) -> s.provider), Codec.INT.fieldOf("startRadius").forGetter((s) -> s.startRadius)).apply(instance, BlockStateRadiusFeatureConfig::new));
  public final BlockStateProvider provider;
  public final int startRadius;

  public BlockStateRadiusFeatureConfig(BlockStateProvider provider, int startRadius) {
    this.provider = provider;
    this.startRadius = startRadius;
  }
}
