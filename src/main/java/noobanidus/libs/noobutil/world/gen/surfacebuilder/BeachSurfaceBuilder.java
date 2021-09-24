package noobanidus.libs.noobutil.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import noobanidus.libs.noobutil.world.gen.config.ISurfaceWithUnderwaterBuilderConfig;

import java.util.Random;
import java.util.function.DoubleFunction;

public class BeachSurfaceBuilder extends SurfaceBuilder<ISurfaceWithUnderwaterBuilderConfig> {
  private static final BlockState STONE = Blocks.STONE.defaultBlockState();
  private final BlockState WATER;
  private final int seaLevel;
  private final DoubleFunction<BlockState> sand;

  public BeachSurfaceBuilder(Codec<ISurfaceWithUnderwaterBuilderConfig> codec, int seaLevel, DoubleFunction<BlockState> sand) {
    super(codec);
    this.WATER = Blocks.WATER.defaultBlockState();
    this.seaLevel = seaLevel;
    this.sand = sand;
  }

  @Override
  public void apply(Random rand, IChunk chunk, Biome biome, int x, int z, int height, double noiseVal, BlockState var9, BlockState var10, int var11, long seed, ISurfaceWithUnderwaterBuilderConfig config) {
    int localX = x & 15;
    int localZ = z & 15;
    BlockState chosenSand = this.sand.apply(noiseVal);
    int thickness = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);

    int run = 0;
    boolean beach = false;
    boolean underwater = false;

    Mutable pos = new Mutable(localX, 0, localZ);

    for (int y = height; y >= 0; --y) {
      pos.set(localX, y, localZ);
      BlockState chunkBlock = chunk.getBlockState(pos);
      if (chunkBlock == STONE && y < 255) {
        BlockState toSet = STONE;
        BlockState up = chunk.getBlockState(pos.above());
        //noinspection deprecation
        if (up.isAir(chunk, pos.above())) {
          beach = y < this.seaLevel + 3;
          toSet = beach ? chosenSand : config.getTopMaterial();
        } else if (chunk.getBlockState(pos.above()) == this.WATER || run < thickness && underwater) {
          underwater = true;
          if (y > this.seaLevel - 3) {
            beach = true;
            toSet = chosenSand;
          } else {
            toSet = config.getUnderWaterMaterial();
          }
        } else if (y > this.seaLevel - 3) {
          if (beach) {
            toSet = chosenSand;
          } else if (run < thickness) {
            toSet = config.getUnderMaterial();
          }
        }

        chunk.setBlockState(pos, toSet, false);
        ++run;
      } else {
        run = 0;
        beach = false;
        underwater = false;
      }
    }
  }
}

