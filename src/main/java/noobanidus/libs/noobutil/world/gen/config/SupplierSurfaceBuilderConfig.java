package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

public class SupplierSurfaceBuilderConfig implements ISurfaceBuilderConfig {
  public static final Codec<SupplierSurfaceBuilderConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(LazyStateSupplier.CODEC.fieldOf("top_material").forGetter((get) -> get.topMaterial), LazyStateSupplier.CODEC.fieldOf("under_material").forGetter((get) -> get.underMaterial), LazyStateSupplier.CODEC.fieldOf("underwater_material").forGetter((get) -> get.underWaterMaterial)).apply(instance, SupplierSurfaceBuilderConfig::new));
  private final LazyStateSupplier topMaterial;
  private final LazyStateSupplier underMaterial;
  private final LazyStateSupplier underWaterMaterial;

  public SupplierSurfaceBuilderConfig (ResourceLocation surface) {
    this.topMaterial = new LazyStateSupplier(surface);
    this.underMaterial = new LazyStateSupplier(surface);
    this.underWaterMaterial = new LazyStateSupplier(surface);
  }

  public SupplierSurfaceBuilderConfig(LazyStateSupplier topMaterial, LazyStateSupplier underMaterial, LazyStateSupplier underWaterMaterial) {
    this.topMaterial = topMaterial;
    this.underMaterial = underMaterial;
    this.underWaterMaterial = underWaterMaterial;
  }

  public BlockState getTop() {
    return this.topMaterial.get();
  }

  public BlockState getUnder() {
    return this.underMaterial.get();
  }

  public BlockState getUnderWaterMaterial() {
    return this.underWaterMaterial.get();
  }
}
