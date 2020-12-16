package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

public class SupplierBlockStateFeatureConfig implements IFeatureConfig {
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
