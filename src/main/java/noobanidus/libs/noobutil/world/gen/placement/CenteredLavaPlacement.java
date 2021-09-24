package noobanidus.libs.noobutil.world.gen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class CenteredLavaPlacement extends Placement<ChanceConfig> {
  public CenteredLavaPlacement(Codec<ChanceConfig> codec) {
    super(codec);
  }

  public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, ChanceConfig config, BlockPos pos) {
    if (rand.nextInt(config.chance / 10) == 0) {
      int i = pos.getX();
      int j = pos.getZ();
      int k = rand.nextInt(rand.nextInt(helper.getGenDepth() - 8) + 8);
      if (k < helper.getSeaLevel() || rand.nextInt(config.chance / 8) == 0) {
        return Stream.of(new BlockPos(i, k, j));
      }
    }

    return Stream.empty();
  }
}
