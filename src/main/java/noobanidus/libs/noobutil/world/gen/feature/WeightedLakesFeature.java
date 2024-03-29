package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import noobanidus.libs.noobutil.world.gen.config.WeightedBlockStateFeatureConfig;

import java.util.Random;

public class WeightedLakesFeature extends Feature<WeightedBlockStateFeatureConfig> {
  private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

  public WeightedLakesFeature(Codec<WeightedBlockStateFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, WeightedBlockStateFeatureConfig config) {
    while (pos.getY() > 5 && reader.isEmptyBlock(pos)) {
      pos = pos.below();
    }

    if (pos.getY() <= 4) {
      return false;
    } else {
      pos = pos.below(4);
      if (reader.startsForFeature(SectionPos.of(pos), Structure.VILLAGE).findAny().isPresent()) {
        return false;
      } else {
        boolean[] aboolean = new boolean[2048];
        int i = rand.nextInt(4) + 4;

        for (int j = 0; j < i; ++j) {
          double d0 = rand.nextDouble() * 6.0D + 3.0D;
          double d1 = rand.nextDouble() * 4.0D + 2.0D;
          double d2 = rand.nextDouble() * 6.0D + 3.0D;
          double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
          double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
          double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

          for (int l = 1; l < 15; ++l) {
            for (int i1 = 1; i1 < 15; ++i1) {
              for (int j1 = 1; j1 < 7; ++j1) {
                double d6 = ((double) l - d3) / (d0 / 2.0D);
                double d7 = ((double) j1 - d4) / (d1 / 2.0D);
                double d8 = ((double) i1 - d5) / (d2 / 2.0D);
                double d9 = d6 * d6 + d7 * d7 + d8 * d8;
                if (d9 < 1.0D) {
                  aboolean[(l * 16 + i1) * 8 + j1] = true;
                }
              }
            }
          }
        }

        for (int k1 = 0; k1 < 16; ++k1) {
          for (int l2 = 0; l2 < 16; ++l2) {
            for (int k = 0; k < 8; ++k) {
              boolean flag = !aboolean[(k1 * 16 + l2) * 8 + k] && (k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + k] || k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + k] || l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + k] || l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + k] || k < 7 && aboolean[(k1 * 16 + l2) * 8 + k + 1] || k > 0 && aboolean[(k1 * 16 + l2) * 8 + (k - 1)]);
              if (flag) {
                Material material = reader.getBlockState(pos.offset(k1, k, l2)).getMaterial();
                if (k >= 4 && material.isLiquid()) {
                  return false;
                }

                if (k < 4 && !material.isSolid() && !config.containsState(reader.getBlockState(pos.offset(k1, k, l2)))) {
                  return false;
                }
              }
            }
          }
        }

        for (int l1 = 0; l1 < 16; ++l1) {
          for (int i3 = 0; i3 < 16; ++i3) {
            for (int i4 = 0; i4 < 8; ++i4) {
              if (aboolean[(l1 * 16 + i3) * 8 + i4]) {
                reader.setBlock(pos.offset(l1, i4, i3), i4 >= 4 ? AIR : config.getBlockState(rand), 2);
              }
            }
          }
        }

        for (int i2 = 0; i2 < 16; ++i2) {
          for (int j3 = 0; j3 < 16; ++j3) {
            for (int j4 = 4; j4 < 8; ++j4) {
              if (aboolean[(i2 * 16 + j3) * 8 + j4]) {
                BlockPos blockpos = pos.offset(i2, j4 - 1, j3);
                if (isDirt(reader.getBlockState(blockpos).getBlock()) && reader.getBrightness(LightType.SKY, pos.offset(i2, j4, j3)) > 0) {
                  Biome biome = reader.getBiome(blockpos);
                  if (biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial().is(Blocks.MYCELIUM)) {
                    reader.setBlock(blockpos, Blocks.MYCELIUM.defaultBlockState(), 2);
                  } else {
                    reader.setBlock(blockpos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                  }
                }
              }
            }
          }
        }

        return true;
      }
    }
  }
}
