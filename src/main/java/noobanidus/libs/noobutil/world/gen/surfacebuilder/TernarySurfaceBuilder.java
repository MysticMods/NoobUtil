package noobanidus.libs.noobutil.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

@SuppressWarnings("NullableProblems")
public class TernarySurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
  private final SurfaceBuilderBaseConfiguration one;
  private final SurfaceBuilderBaseConfiguration two;
  private final SurfaceBuilderBaseConfiguration three;

  public TernarySurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec, SurfaceBuilderBaseConfiguration one, SurfaceBuilderBaseConfiguration two, SurfaceBuilderBaseConfiguration three) {
    super(codec);
    this.one = one;
    this.two = two;
    this.three = three;
  }

  @Override
  public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
    if (!(noise < -1.0D) && !(noise > 2.0D)) {
      if (noise > 1.0D)
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, one);
      else
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, two);
    } else
      SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, three);
  }
}
