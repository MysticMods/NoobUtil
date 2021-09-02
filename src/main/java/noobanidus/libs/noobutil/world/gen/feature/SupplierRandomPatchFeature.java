package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import noobanidus.libs.noobutil.world.gen.config.SupplierBlockClusterFeatureConfig;

import java.util.Random;

public class SupplierRandomPatchFeature extends Feature<SupplierBlockClusterFeatureConfig> {
  public SupplierRandomPatchFeature(Codec<SupplierBlockClusterFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, SupplierBlockClusterFeatureConfig config) {
    BlockState blockstate = config.stateProvider.get();
    BlockPos blockpos;
    if (config.project) {
      blockpos = reader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
    } else {
      blockpos = pos;
    }

    int i = 0;
    BlockPos.Mutable pos2 = new BlockPos.Mutable();

    for (int j = 0; j < config.tryCount; ++j) {
      pos2.setAndOffset(blockpos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
      BlockPos blockpos1 = pos2.down();
      BlockState blockstate1 = reader.getBlockState(blockpos1);
      if ((reader.isAirBlock(pos2) || config.isReplaceable && reader.getBlockState(pos2).getMaterial().isReplaceable()) && blockstate.isValidPosition(reader, pos2) && (config.whitelist.isEmpty() || config.getWhitelist().contains(blockstate1.getBlock())) && !config.getBlacklist().contains(blockstate1) && (!config.requiresWater || reader.getFluidState(blockpos1.west()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.east()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.north()).isTagged(FluidTags.WATER) || reader.getFluidState(blockpos1.south()).isTagged(FluidTags.WATER))) {
        config.blockPlacer.place(reader, pos2, blockstate, rand);
        ++i;
      }
    }

    return i > 0;
  }
}
