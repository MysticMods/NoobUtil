package noobanidus.libs.noobutil.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

public class SandWithPatchesSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {

  private final double threshold;

  public SandWithPatchesSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec, double threshold) {
    super(codec);
    this.threshold = threshold;
  }

  @Override
  public void apply(Random random, ChunkAccess chunk, Biome biome, int int_1, int int_2, int int_3, double noise, BlockState blockState_1, BlockState blockState_2, int int_4, long long_1, SurfaceBuilderBaseConfiguration config) {
    if (noise > threshold) {
      SurfaceBuilder.DEFAULT.apply(random, chunk, biome, int_1, int_2, int_3, noise, blockState_1, blockState_2, int_4, long_1, config);
    } else {
      SurfaceBuilder.DEFAULT.apply(random, chunk, biome, int_1, int_2, int_3, noise, blockState_1, blockState_2, int_4, long_1, SurfaceBuilder.CONFIG_FULL_SAND);
    }
  }
}

