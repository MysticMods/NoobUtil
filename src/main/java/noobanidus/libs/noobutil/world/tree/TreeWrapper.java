package noobanidus.libs.noobutil.world.tree;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class TreeWrapper extends Tree {
  private final Supplier<ConfiguredFeature<BaseTreeFeatureConfig, ?>> tree;

  public TreeWrapper(Supplier<ConfiguredFeature<BaseTreeFeatureConfig, ?>> tree) {
    this.tree = tree;
  }

  @Nullable
  @Override
  protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
    return this.tree.get();
  }
}
