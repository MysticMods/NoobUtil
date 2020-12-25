package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import noobanidus.libs.noobutil.types.LazyStateSupplier;
import noobanidus.libs.noobutil.world.gen.config.SupplierSphereReplaceConfig;

import java.util.Random;

public class SupplierSphereReplaceFeature extends Feature<SupplierSphereReplaceConfig> {
  public SupplierSphereReplaceFeature(Codec<SupplierSphereReplaceConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, SupplierSphereReplaceConfig config) {
    boolean flag = false;
    int i = config.radius.func_242259_a(rand);

    for (int j = pos.getX() - i; j <= pos.getX() + i; ++j) {
      for (int k = pos.getZ() - i; k <= pos.getZ() + i; ++k) {
        int l = j - pos.getX();
        int i1 = k - pos.getZ();
        if (l * l + i1 * i1 <= i * i) {
          for (int j1 = pos.getY() - config.half_height; j1 <= pos.getY() + config.half_height; ++j1) {
            BlockPos blockpos = new BlockPos(j, j1, k);
            Block block = reader.getBlockState(blockpos).getBlock();

            for (LazyStateSupplier blockstate : config.targets) {
              if (blockstate.get().isIn(block)) {
                reader.setBlockState(blockpos, config.state, 2);
                flag = true;
                break;
              }
            }
          }
        }
      }
    }

    return flag;
  }
}
