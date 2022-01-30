package noobanidus.libs.noobutil.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

@SuppressWarnings("NullableProblems")
public class DualSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
  private SurfaceBuilderBaseConfiguration one, two;
  private double treshold;

  public DualSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec, SurfaceBuilderBaseConfiguration one, SurfaceBuilderBaseConfiguration two, double treshold) {
    super(codec);
    this.one = one;
    this.two = two;
    this.treshold = treshold;
  }

  @Override
  public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
    if (noise > treshold)
      SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, one);
    else
      SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, two);
  }
}
