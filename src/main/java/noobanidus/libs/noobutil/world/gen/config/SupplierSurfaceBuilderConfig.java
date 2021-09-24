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
    this(surface, surface, surface);
  }

  public SupplierSurfaceBuilderConfig (ResourceLocation top, ResourceLocation under, ResourceLocation underWater) {
    this(new LazyStateSupplier(top), new LazyStateSupplier(under), new LazyStateSupplier(underWater));
  }

  public SupplierSurfaceBuilderConfig(LazyStateSupplier topMaterial, LazyStateSupplier underMaterial, LazyStateSupplier underWaterMaterial) {
    this.topMaterial = topMaterial;
    this.underMaterial = underMaterial;
    this.underWaterMaterial = underWaterMaterial;
  }

  @Override
  public BlockState getTopMaterial() {
    return this.topMaterial.get();
  }

  @Override
  public BlockState getUnderMaterial() {
    return this.underMaterial.get();
  }

  public BlockState getUnderWaterMaterial() {
    return this.underWaterMaterial.get();
  }
}
