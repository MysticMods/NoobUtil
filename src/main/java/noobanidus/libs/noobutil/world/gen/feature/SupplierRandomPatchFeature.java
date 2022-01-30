package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.SupplierBlockClusterFeatureConfig;

import java.util.Random;

public class SupplierRandomPatchFeature extends Feature<SupplierBlockClusterFeatureConfig> {
  public SupplierRandomPatchFeature(Codec<SupplierBlockClusterFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, SupplierBlockClusterFeatureConfig config) {
    BlockState blockstate = config.stateProvider.get();
    BlockPos blockpos;
    if (config.project) {
      blockpos = reader.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
    } else {
      blockpos = pos;
    }

    int i = 0;
    BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos();

    for (int j = 0; j < config.tryCount; ++j) {
      pos2.setWithOffset(blockpos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
      BlockPos blockpos1 = pos2.below();
      BlockState blockstate1 = reader.getBlockState(blockpos1);
      if ((reader.isEmptyBlock(pos2) || config.isReplaceable && reader.getBlockState(pos2).getMaterial().isReplaceable()) && blockstate.canSurvive(reader, pos2) && (config.whitelist.isEmpty() || config.getWhitelist().contains(blockstate1.getBlock())) && !config.getBlacklist().contains(blockstate1) && (!config.requiresWater || reader.getFluidState(blockpos1.west()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.east()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.north()).is(FluidTags.WATER) || reader.getFluidState(blockpos1.south()).is(FluidTags.WATER))) {
        config.blockPlacer.place(reader, pos2, blockstate, rand);
        ++i;
      }
    }

    return i > 0;
  }
}
