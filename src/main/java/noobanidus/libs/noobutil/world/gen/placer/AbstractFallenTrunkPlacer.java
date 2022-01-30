package noobanidus.libs.noobutil.world.gen.placer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings({"NullableProblems", "UnusedReturnValue", "WeakerAccess"})
public abstract class AbstractFallenTrunkPlacer extends StraightTrunkPlacer {
  public static <T extends AbstractFallenTrunkPlacer> Codec<T> codecBuilder(Builder<T> builder) {
    return RecordCodecBuilder.create((instance) -> trunkPlacerParts(instance).apply(instance, builder::create));
  }

  @FunctionalInterface
  public interface Builder<T extends AbstractFallenTrunkPlacer> {
    T create(int baseHeight, int firstRandomHeight, int secondRandomHeight);
  }

  public AbstractFallenTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
    super(baseHeight, firstRandomHeight, secondRandomHeight);
  }

  @Override
  protected abstract TrunkPlacerType<?> type();

  @Override
  public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> set, BoundingBox blockBox, TreeConfiguration treeFeatureConfig) {
    setDirtAt(world, pos.below());

    // Axis
    Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
    Direction direction = Direction.fromAxisAndDirection(axis, random.nextBoolean() ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);

    for (int i = 0; i < trunkHeight; ++i) {
      placeTrunkBlock(world, random, pos.relative(direction, i), set, blockBox, treeFeatureConfig, axis);
    }

    return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos, 0, false));
  }

  protected static boolean placeTrunkBlock(LevelSimulatedRW modifiableTestableWorld, Random random, BlockPos blockPos, Set<BlockPos> set, BoundingBox blockBox, TreeConfiguration treeFeatureConfig, Direction.Axis axis) {
    if (TreeFeature.validTreePos(modifiableTestableWorld, blockPos)) {
      setBlock(modifiableTestableWorld, blockPos, treeFeatureConfig.trunkProvider.getState(random, blockPos).setValue(RotatedPillarBlock.AXIS, axis), blockBox);
      set.add(blockPos.immutable());
      return true;
    } else {
      return false;
    }
  }
}
