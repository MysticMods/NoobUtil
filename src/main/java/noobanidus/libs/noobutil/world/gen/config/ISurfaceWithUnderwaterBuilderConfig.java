package noobanidus.libs.noobutil.world.gen.config;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;

public interface ISurfaceWithUnderwaterBuilderConfig extends SurfaceBuilderConfiguration {
  BlockState getUnderWaterMaterial();
}
