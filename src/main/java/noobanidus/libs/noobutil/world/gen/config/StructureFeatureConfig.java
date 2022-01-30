package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StructureFeatureConfig implements FeatureConfiguration {
  public static final Codec<StructureFeatureConfig> CODEC = Codec.INT.fieldOf("state").xmap(StructureFeatureConfig::new, (o) -> o.offset).codec();

  private final int offset;

  public StructureFeatureConfig(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }
}
