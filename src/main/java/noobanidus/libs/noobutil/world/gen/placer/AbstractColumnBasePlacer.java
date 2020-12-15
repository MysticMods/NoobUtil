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

  protected abstract BlockPlacerType<?> getBlockPlacerType();

  public void place(IWorld world, BlockPos pos, BlockState state, Random random) {
    int i = radius1 + random.nextInt(radius2);
    for (int j = pos.getX() - i; j <= pos.getX() + i; ++j) {
      for (int k = pos.getZ() - i; k <= pos.getZ() + i; ++k) {
        int l = j - pos.getX();
        int i1 = k - pos.getZ();
        if (l * l + i1 * i1 <= i * i) {
          int j1 = pos.getY() - 1;
          BlockPos blockpos = new BlockPos(j, j1, k);
          Block block = world.getBlockState(blockpos).getBlock();

          for (LazyStateSupplier blockstate : replace) {
            if (blockstate.get().isIn(block)) {
              world.setBlockState(blockpos, state, 2);
              if (random.nextInt(peak) == 0) {
                world.setBlockState(blockpos.up(), state, 2);
                if (random.nextInt(Math.max(1, peak / 2)) == 0) {
                  world.setBlockState(blockpos.up().up(), state, 2);
                  if (random.nextInt(Math.max(1, (peak / 3))) == 0) {
                    world.setBlockState(blockpos.up().up().up(), state, 2);
                  }
                }
              }
              break;
            }
          }
        }
      }
    }

    BlockPos.Mutable blockpos$mutable = pos.toMutable();
    i = this.minSize + random.nextInt(random.nextInt(this.extraSize + 1) + 1);

    for (
        int j = 0;
        j < i; ++j) {
      world.setBlockState(blockpos$mutable, state, 2);
      blockpos$mutable.move(Direction.UP);
    }
  }
}