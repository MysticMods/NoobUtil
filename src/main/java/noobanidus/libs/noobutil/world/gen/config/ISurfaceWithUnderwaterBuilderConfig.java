package noobanidus.libs.noobutil.world.gen.config;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;

public interface ISurfaceWithUnderwaterBuilderConfig extends ISurfaceBuilderConfig {
  BlockState getUnderWaterMaterial();
}
