package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WeightedBlockStateFeatureConfig implements IFeatureConfig {
  public static final Codec<WeightedBlockStateFeatureConfig> CODEC = WeightedBlockStateProvider.CODEC.fieldOf("states").xmap(WeightedBlockStateFeatureConfig::new, (o) -> o.states).codec();
  private final WeightedBlockStateProvider states;
  private final Set<BlockState> containedStates = new HashSet<>();

  public WeightedBlockStateFeatureConfig(WeightedBlockStateProvider states) {
    this.states = states;
    this.states.weightedList.entries.forEach(o -> containedStates.add(o.getData()));
  }

  public BlockState getBlockState (Random random) {
    return this.states.getState(random, BlockPos.ZERO);
  }

  public boolean containsState (BlockState state) {
    return containedStates.contains(state);
  }
}
