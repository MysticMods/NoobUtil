package noobanidus.libs.noobutil.world.gen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.Random;
import java.util.stream.Stream;

public class CenteredWaterPlacement extends FeatureDecorator<ChanceDecoratorConfiguration> {
  public CenteredWaterPlacement(Codec<ChanceDecoratorConfiguration> codec) {
    super(codec);
  }

  public Stream<BlockPos> getPositions(DecorationContext helper, Random rand, ChanceDecoratorConfiguration config, BlockPos pos) {
    if (rand.nextInt(config.chance) == 0) {
      int i = pos.getX();
      int j = pos.getZ();
      int k = rand.nextInt(helper.getGenDepth());
      return Stream.of(new BlockPos(i, k, j));
    } else {
      return Stream.empty();
    }
  }
}
