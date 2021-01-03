package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.TwoBlockStateRadiusFeatureConfig;

import java.util.Random;

public class RadiusTwoBlockBlobFeature extends Feature<TwoBlockStateRadiusFeatureConfig> {
  public RadiusTwoBlockBlobFeature(Codec<TwoBlockStateRadiusFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, TwoBlockStateRadiusFeatureConfig config) {
    while (true) {
      if (pos.getY() > 3) {
        if (reader.isAirBlock(pos.down())) {
          pos = pos.down();
          continue;
        }

        Block block = reader.getBlockState(pos.down()).getBlock();
        if (!isDirt(block) && !isStone(block)) {
          pos = pos.down();
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
        double f3 = ((f / 2.0) * (f / 2.0));

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-j, -k, -l), pos.add(j, k, l))) {
          if (blockpos.distanceSq(pos) <= f3) {
            reader.setBlockState(blockpos, config.provider2.getBlockState(rand, blockpos), 4);
          } else if (blockpos.distanceSq(pos) <= f2) {
            reader.setBlockState(blockpos, config.provider1.getBlockState(rand, blockpos), 4);
          }
        }

        pos = pos.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
      }

      return true;
    }
  }
}
