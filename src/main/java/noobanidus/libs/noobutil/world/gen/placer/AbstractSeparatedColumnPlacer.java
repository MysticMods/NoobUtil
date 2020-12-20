/*package noobanidus.libs.noobutil.world.gen.placer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraft.world.gen.blockplacer.ColumnBlockPlacer;

import java.util.Random;

public abstract class AbstractSeparatedColumnPlacer extends BlockPlacer {
  public static <T extends AbstractSeparatedColumnPlacer> Codec<AbstractSeparatedColumnPlacer> codecBuilder (Builder<T> builder) {
    return RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("minSize").forGetter(o -> o.minSize), Codec.INT.fieldOf("extraSize").forGetter(o -> o.extraSize)).apply(instance, builder::create));
  }
  private final int minSize;
  private final int extraSize;

  @FunctionalInterface
  public interface Builder<T extends AbstractSeparatedColumnPlacer> {
    T create(int minSize, int extraSize);
  }

  public AbstractSeparatedColumnPlacer(int minSize, int extraSize) {
    this.minSize = minSize;
    this.extraSize = extraSize;
  }

  @Override
  public void place(IWorld world, BlockPos pos, BlockState state, Random random) {
    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        if (world.getBlockState(
      }
    }

    BlockPos.Mutable blockpos$mutable = pos.toMutable();
    int i = this.minSize + random.nextInt(random.nextInt(this.extraSize + 1) + 1);

    for (int j = 0; j < i; ++j) {
      world.setBlockState(blockpos$mutable, state, 2);
      blockpos$mutable.move(Direction.UP);
    }
  }

  @Override
  protected abstract BlockPlacerType<?> getBlockPlacerType();
}*/
