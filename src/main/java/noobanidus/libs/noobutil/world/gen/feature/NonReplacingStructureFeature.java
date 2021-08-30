package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import noobanidus.libs.noobutil.types.IntPair;
import noobanidus.libs.noobutil.world.gen.config.StructureFeatureConfig;

import java.util.List;
import java.util.Random;

public class NonReplacingStructureFeature extends StructureFeature {
  public NonReplacingStructureFeature(Codec<StructureFeatureConfig> codec, ResourceLocation structure) {
    super(codec, structure);
  }

  public NonReplacingStructureFeature(Codec<StructureFeatureConfig> codec, ResourceLocation... structures) {
    super(codec, structures);
  }

  public NonReplacingStructureFeature(Codec<StructureFeatureConfig> codec, int weight, ResourceLocation structure) {
    super(codec, weight, structure);
  }

  public NonReplacingStructureFeature(Codec<StructureFeatureConfig> codec, IntPair<ResourceLocation>... structures) {
    super(codec, structures);
  }

  public NonReplacingStructureFeature(Codec<StructureFeatureConfig> codec, List<IntPair<ResourceLocation>> structures) {
    super(codec, structures);
  }

  @Override
  protected boolean shouldContinue(IServerWorld world, BlockPos pos1, BlockPos pos2, PlacementSettings placement, Random random, int props) {
    MutableBoundingBox box = placement.getBoundingBox();
    if (box == null) {
      return true;
    }

    BlockPos.Mutable pos = new BlockPos.Mutable();
    for (int x = box.minX; x < box.maxX; x++) {
      pos.setX(x);
      for (int z = box.minZ; z < box.maxZ; z++) {
        pos.setZ(z);
        for (int y = box.minY; y < box.maxY; y++) {
          pos.setY(y);
          BlockState state = world.getBlockState(pos);
          if (!state.isAir(world, pos)) {
            return false;
          }
          if (!state.getMaterial().isReplaceable()) {
            return false;
          }
        }
      }
    }

    return true;
  }
}
