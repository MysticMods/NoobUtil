package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

public class StructureFeatureConfig implements IFeatureConfig {
  public static final Codec<StructureFeatureConfig> CODEC = Codec.INT.fieldOf("state").xmap(StructureFeatureConfig::new, (o) -> o.offset).codec();

  private final int offset;

  public StructureFeatureConfig(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }
}
