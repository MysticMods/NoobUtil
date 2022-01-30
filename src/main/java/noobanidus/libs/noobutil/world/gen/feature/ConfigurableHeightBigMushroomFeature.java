package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.BiggerMushroomFeatureConfig;

import java.util.Random;

@SuppressWarnings({"unused", "NullableProblems"})
public class ConfigurableHeightBigMushroomFeature extends Feature<BiggerMushroomFeatureConfig> {
  public ConfigurableHeightBigMushroomFeature(Codec<BiggerMushroomFeatureConfig> codec) {
    super(codec);
  }

  protected void placeTrunk(LevelAccessor pLevel, Random pRandom, BlockPos pPos, HugeMushroomFeatureConfiguration pConfig, int pMaxHeight, BlockPos.MutableBlockPos pMutablePos) {
    for (int i = 0; i < pMaxHeight; ++i) {
      pMutablePos.set(pPos).move(Direction.UP, i);
      if (pLevel.getBlockState(pMutablePos).canBeReplacedByLogs(pLevel, pMutablePos)) {
        this.setBlock(pLevel, pMutablePos, pConfig.stemProvider.getState(pRandom, pPos));
      }
    }
  }

  protected boolean isValidPosition(LevelAccessor pLevel, BlockPos pPos, int pMaxHeight, BlockPos.MutableBlockPos pMutablePos, BiggerMushroomFeatureConfig pConfig) {
    int i = pPos.getY();
    if (i >= 1 && i + pMaxHeight + 1 < 256) {
      Block block = pLevel.getBlockState(pPos.below()).getBlock();
      if (!isDirt(block) && !block.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
        return false;
      } else {
        for (int j = 0; j <= pMaxHeight; ++j) {
          int k = this.getTreeRadiusForHeight(pConfig.foliageRadius, j);

          for (int l = -k; l <= k; ++l) {
            for (int i1 = -k; i1 <= k; ++i1) {
              BlockState blockstate = pLevel.getBlockState(pMutablePos.setWithOffset(pPos, l, j, i1));
              //noinspection deprecation
              if (!blockstate.isAir(pLevel, pMutablePos.setWithOffset(pPos, l, j, i1)) && !blockstate.is(BlockTags.LEAVES)) {
                return false;
              }
            }
          }
        }

        return true;
      }
    } else {
      return false;
    }
  }

  public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, BiggerMushroomFeatureConfig config) {
    int i = this.getTreeHeight(rand, config.getA(), config.getB());
    BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
    if (!this.isValidPosition(reader, pos, i, blockpos$mutable, config)) {
      return false;
    } else {
      this.makeCap(reader, rand, pos, i, blockpos$mutable, config);
      this.placeTrunk(reader, rand, pos, config, i, blockpos$mutable);
      return true;
    }
  }

  protected void makeCap(LevelAccessor pLevel, Random pRandom, BlockPos pPos, int pTreeHeight, BlockPos.MutableBlockPos pMutablePos, HugeMushroomFeatureConfiguration pConfig) {
    for (int i = pTreeHeight - 3; i <= pTreeHeight; ++i) {
      int j = i < pTreeHeight ? pConfig.foliageRadius : pConfig.foliageRadius - 1;
      int k = pConfig.foliageRadius - 2;

      for (int l = -j; l <= j; ++l) {
        for (int i1 = -j; i1 <= j; ++i1) {
          boolean flag = l == -j;
          boolean flag1 = l == j;
          boolean flag2 = i1 == -j;
          boolean flag3 = i1 == j;
          boolean flag4 = flag || flag1;
          boolean flag5 = flag2 || flag3;
          if (i >= pTreeHeight || flag4 != flag5) {
            pMutablePos.setWithOffset(pPos, l, i, i1);
            if (pLevel.getBlockState(pMutablePos).canBeReplacedByLeaves(pLevel, pMutablePos)) {
              this.setBlock(pLevel, pMutablePos, pConfig.capProvider.getState(pRandom, pPos).setValue(HugeMushroomBlock.UP, i >= pTreeHeight - 1).setValue(HugeMushroomBlock.WEST, l < -k).setValue(HugeMushroomBlock.EAST, l > k).setValue(HugeMushroomBlock.NORTH, i1 < -k).setValue(HugeMushroomBlock.SOUTH, i1 > k));
            }
          }
        }
      }
    }

  }

  protected int getTreeRadiusForHeight(int pFoliageRadius, int pY) {
    int i = 0;
    if (pY < -1 && pY >= -1 - 3) {
      i = pFoliageRadius;
    } else if (pY == -1) {
      i = pFoliageRadius;
    }

    return i;
  }

  protected int getTreeHeight(Random pRandom, int a, int b) {
    return ((pRandom.nextInt(3) + a) * 2) - (2 + pRandom.nextInt(b));
  }
}
