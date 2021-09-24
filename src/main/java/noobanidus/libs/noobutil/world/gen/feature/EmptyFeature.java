package noobanidus.libs.noobutil.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class EmptyFeature extends Feature<NoFeatureConfig> {
  public EmptyFeature() {
    super(NoFeatureConfig.CODEC);
  }

  @Override
  public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
    return true;
  }
}
