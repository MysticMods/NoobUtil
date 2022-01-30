package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.BlockStateRadiusFeatureConfig;

import java.util.Random;

public class RadiusBlockBlobFeature extends Feature<BlockStateRadiusFeatureConfig> {
  public RadiusBlockBlobFeature(Codec<BlockStateRadiusFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateRadiusFeatureConfig config) {
    while (true) {
      if (pos.getY() > 3) {
        if (reader.isEmptyBlock(pos.below())) {
          pos = pos.below();
          continue;
        }

        Block block = reader.getBlockState(pos.below()).getBlock();
        if (!isDirt(block) && !isStone(block)) {
          pos = pos.below();
          continue;
        }
      }

      if (pos.getY() <= 3) {
        return false;
      }

      int i1 = config.startRadius;

      for (int i = 0; i1 >= 0 && i < 3; ++i) {
        int j = i1 + rand.nextInt(2);
        int k = i1 + rand.nextInt(2);
        int l = i1 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        double f2 = (double) (f * f);

        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-j, -k, -l), pos.offset(j, k, l))) {
          if (blockpos.distSqr(pos) <= f2) {
            reader.setBlock(blockpos, config.provider.getState(rand, blockpos), 4);
          }
        }

        pos = pos.offset(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
      }

      return true;
    }
  }
}
