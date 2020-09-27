package noobanidus.libs.noobutil.world;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class GenericTree extends Tree {
  private final Supplier<TreeFeatureConfig> config;
  private boolean fancy;

  public GenericTree(Supplier<TreeFeatureConfig> config, boolean fancy) {
    this.config = config;
    this.fancy = fancy;
  }

  @Nullable
  @Override
  protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean beeHive) {
    return fancy ? Feature.FANCY_TREE.withConfiguration(config.get()) : Feature.NORMAL_TREE.withConfiguration(config.get());
  }
}
