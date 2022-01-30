package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import noobanidus.libs.noobutil.type.LazyStateSupplier;

public class SupplierBlockStateFeatureConfig implements FeatureConfiguration {
  public static final Codec<SupplierBlockStateFeatureConfig> CODEC = LazyStateSupplier.CODEC.fieldOf("state").xmap(SupplierBlockStateFeatureConfig::new, (o) -> o.state).codec();

  private final LazyStateSupplier state;

  public SupplierBlockStateFeatureConfig (ResourceLocation location) {
    this(new LazyStateSupplier(location));
  }

  public SupplierBlockStateFeatureConfig(String namespace, String key) {
    this(new LazyStateSupplier(namespace, key));
  }

  private SupplierBlockStateFeatureConfig(LazyStateSupplier supplier) {
    this.state = supplier;
  }

  public BlockState get () {
    return state.get();
  }
}
