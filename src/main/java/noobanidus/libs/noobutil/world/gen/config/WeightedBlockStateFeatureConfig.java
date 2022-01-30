package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WeightedBlockStateFeatureConfig implements FeatureConfiguration {
  public static final Codec<WeightedBlockStateFeatureConfig> CODEC = WeightedStateProvider.CODEC.fieldOf("states").xmap(WeightedBlockStateFeatureConfig::new, (o) -> o.states).codec();
  private final WeightedStateProvider states;
  private final Set<BlockState> containedStates = new HashSet<>();

  public WeightedBlockStateFeatureConfig(WeightedStateProvider states) {
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
