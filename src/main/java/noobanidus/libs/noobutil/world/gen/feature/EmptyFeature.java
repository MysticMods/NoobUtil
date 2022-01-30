package noobanidus.libs.noobutil.world.gen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class EmptyFeature extends Feature<NoneFeatureConfiguration> {
  public EmptyFeature() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, NoneFeatureConfiguration config) {
    return true;
  }
}
