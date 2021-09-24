package noobanidus.libs.noobutil.world.gen.placer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class AbstractColumnBasePlacer extends BlockPlacer {
  public static <T extends AbstractColumnBasePlacer> Codec<T> codecBuilder(Builder<T> abstractBuilder) {
    return RecordCodecBuilder.create((builder) -> builder.group(
        Codec.INT.fieldOf("min_size").forGetter(o -> o.minSize),
        Codec.INT.fieldOf("extra_size").forGetter(o -> o.extraSize),
        Codec.INT.fieldOf("radius1").forGetter(o -> o.radius1),
        Codec.INT.fieldOf("radius2").forGetter(o -> o.radius2),
        LazyStateSupplier.CODEC.listOf().fieldOf("replacements").forGetter(o -> ImmutableList.copyOf(o.replace)),
        Codec.INT.fieldOf("peak").forGetter(o -> o.peak)).apply(builder, abstractBuilder::create));
  }

  protected final int minSize;
  protected final int extraSize;
  protected final int radius1;
  protected final int radius2;
  protected final Set<LazyStateSupplier> replace;
  protected final int peak;

  public interface Builder<T extends AbstractColumnBasePlacer> {
    T create(int minSize, int extraSize, int radius1, int radius2, List<LazyStateSupplier> replace, int peak);
  }

  public AbstractColumnBasePlacer(int minSize, int extraSize, int radius1, int radius2, List<LazyStateSupplier> replace, int peak) {
    this.minSize = minSize;
    this.extraSize = extraSize;
    this.radius1 = radius1;
    this.radius2 = radius2;
    this.replace = ImmutableSet.copyOf(replace);
    this.peak = peak;
  }

  @Override
  protected abstract BlockPlacerType<?> type();

  @Override
  public void place(IWorld world, BlockPos pos, BlockState state, Random random) {
    BlockPos.Mutable blockpos$mutable = pos.mutable();
    int i = this.minSize + random.nextInt(random.nextInt(this.extraSize + 1) + 1);

    for (int j = 0; j < i; ++j) {
      world.setBlock(blockpos$mutable, state, 2);
      blockpos$mutable.move(Direction.UP);
    }

    i = radius1 + random.nextInt(radius2);
    for (int j = pos.getX() - i; j <= pos.getX() + i; ++j) {
      for (int k = pos.getZ() - i; k <= pos.getZ() + i; ++k) {
        int l = j - pos.getX();
        int i1 = k - pos.getZ();
        if (l * l + i1 * i1 <= i * i) {
          int j1 = pos.getY() - 1;
          BlockPos blockpos = new BlockPos(j, j1, k);
          Block block = world.getBlockState(blockpos).getBlock();

          for (LazyStateSupplier blockstate : replace) {
            if (blockstate.get().is(block)) {

              world.setBlock(blockpos, state, 2);
              if (random.nextInt(peak) == 0) {
                boolean skip = false;
                outer: for (int x = -1; x <= 1; x++) {
                  for (int z = -1; z <= 1; z++) {
                    if (world.getBlockState(blockpos.offset(x, 1, z)).canOcclude()) {
                      skip = true;
                      break outer;
                    }
                  }
                }
                if (!skip) {
                  world.setBlock(blockpos.above(), state, 2);
                  if (random.nextInt(3) == 0) {
                    world.setBlock(blockpos.above().above(), state, 2);
                    if (random.nextInt(4) == 0) {
                      world.setBlock(blockpos.above().above().above(), state, 2);
                    }
                  }
                }
              }
              break;
            }
          }
        }
      }
    }
  }
}
