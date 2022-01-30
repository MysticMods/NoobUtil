package noobanidus.libs.noobutil.world.tree;

import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class TreeWrapper extends AbstractTreeGrower {
  private final Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree;

  public TreeWrapper(Supplier<ConfiguredFeature<TreeConfiguration, ?>> tree) {
    this.tree = tree;
  }

  @Nullable
  @Override
  protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
    return this.tree.get();
  }
}
