package noobanidus.libs.noobutil.world.gen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class CenteredWaterPlacement extends Placement<ChanceConfig> {
  public CenteredWaterPlacement(Codec<ChanceConfig> codec) {
    super(codec);
  }

  public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, ChanceConfig config, BlockPos pos) {
    if (rand.nextInt(config.chance) == 0) {
      int i = pos.getX() - 8;
      int j = pos.getZ() - 8;
      int k = rand.nextInt(helper.func_242891_a());
      return Stream.of(new BlockPos(i, k, j));
    } else {
      return Stream.empty();
    }
  }
}
