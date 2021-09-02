package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.BiggerMushroomFeatureConfig;

import java.util.Random;

@SuppressWarnings({"unused", "NullableProblems"})
public class ConfigurableHeightBigMushroomFeature extends Feature<BiggerMushroomFeatureConfig> {
  public ConfigurableHeightBigMushroomFeature(Codec<BiggerMushroomFeatureConfig> codec) {
    super(codec);
  }

  protected void func_227210_a_(IWorld p_227210_1_, Random p_227210_2_, BlockPos p_227210_3_, BigMushroomFeatureConfig p_227210_4_, int p_227210_5_, BlockPos.Mutable p_227210_6_) {
    for (int i = 0; i < p_227210_5_; ++i) {
      p_227210_6_.setPos(p_227210_3_).move(Direction.UP, i);
      if (p_227210_1_.getBlockState(p_227210_6_).canBeReplacedByLogs(p_227210_1_, p_227210_6_)) {
        this.setBlockState(p_227210_1_, p_227210_6_, p_227210_4_.stemProvider.getBlockState(p_227210_2_, p_227210_3_));
      }
    }
  }

  protected boolean func_227209_a_(IWorld p_227209_1_, BlockPos p_227209_2_, int p_227209_3_, BlockPos.Mutable p_227209_4_, BiggerMushroomFeatureConfig p_227209_5_) {
    int i = p_227209_2_.getY();
    if (i >= 1 && i + p_227209_3_ + 1 < 256) {
      Block block = p_227209_1_.getBlockState(p_227209_2_.down()).getBlock();
      if (!isDirt(block) && !block.isIn(BlockTags.MUSHROOM_GROW_BLOCK)) {
        return false;
      } else {
        for (int j = 0; j <= p_227209_3_; ++j) {
          int k = this.func_225563_a_(p_227209_5_.foliageRadius, j);

          for (int l = -k; l <= k; ++l) {
            for (int i1 = -k; i1 <= k; ++i1) {
              BlockState blockstate = p_227209_1_.getBlockState(p_227209_4_.setAndOffset(p_227209_2_, l, j, i1));
              //noinspection deprecation
              if (!blockstate.isAir(p_227209_1_, p_227209_4_.setAndOffset(p_227209_2_, l, j, i1)) && !blockstate.isIn(BlockTags.LEAVES)) {
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

  public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BiggerMushroomFeatureConfig config) {
    int i = this.func_227211_a_(rand, config.getA(), config.getB());
    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
    if (!this.func_227209_a_(reader, pos, i, blockpos$mutable, config)) {
      return false;
    } else {
      this.func_225564_a_(reader, rand, pos, i, blockpos$mutable, config);
      this.func_227210_a_(reader, rand, pos, config, i, blockpos$mutable);
      return true;
    }
  }

  protected void func_225564_a_(IWorld p_225564_1_, Random p_225564_2_, BlockPos p_225564_3_, int p_225564_4_, BlockPos.Mutable p_225564_5_, BigMushroomFeatureConfig p_225564_6_) {
    for (int i = p_225564_4_ - 3; i <= p_225564_4_; ++i) {
      int j = i < p_225564_4_ ? p_225564_6_.foliageRadius : p_225564_6_.foliageRadius - 1;
      int k = p_225564_6_.foliageRadius - 2;

      for (int l = -j; l <= j; ++l) {
        for (int i1 = -j; i1 <= j; ++i1) {
          boolean flag = l == -j;
          boolean flag1 = l == j;
          boolean flag2 = i1 == -j;
          boolean flag3 = i1 == j;
          boolean flag4 = flag || flag1;
          boolean flag5 = flag2 || flag3;
          if (i >= p_225564_4_ || flag4 != flag5) {
            p_225564_5_.setAndOffset(p_225564_3_, l, i, i1);
            if (p_225564_1_.getBlockState(p_225564_5_).canBeReplacedByLeaves(p_225564_1_, p_225564_5_)) {
              this.setBlockState(p_225564_1_, p_225564_5_, p_225564_6_.capProvider.getBlockState(p_225564_2_, p_225564_3_).with(HugeMushroomBlock.UP, i >= p_225564_4_ - 1).with(HugeMushroomBlock.WEST, l < -k).with(HugeMushroomBlock.EAST, l > k).with(HugeMushroomBlock.NORTH, i1 < -k).with(HugeMushroomBlock.SOUTH, i1 > k));
            }
          }
        }
      }
    }

  }

  protected int func_225563_a_(int p_225563_3_, int p_225563_4_) {
    int i = 0;
    if (p_225563_4_ < -1 && p_225563_4_ >= -1 - 3) {
      i = p_225563_3_;
    } else if (p_225563_4_ == -1) {
      i = p_225563_3_;
    }

    return i;
  }

  protected int func_227211_a_(Random p_227211_1_, int a, int b) {
    return ((p_227211_1_.nextInt(3) + a) * 2) - (2 + p_227211_1_.nextInt(b));
  }
}
