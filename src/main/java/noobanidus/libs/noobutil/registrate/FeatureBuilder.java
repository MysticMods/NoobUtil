package noobanidus.libs.noobutil.registrate;

import com.mojang.datafixers.Dynamic;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.function.Function;

public class FeatureBuilder<FC extends IFeatureConfig, T extends Feature<FC>, P> extends AbstractBuilder<Feature<?>, T, P, FeatureBuilder<FC, T, P>> {
  private IFactory<FC, T> factory;
  private Function<Dynamic<?>, FC> configFactory;
  private boolean doBlockNotify;

  public FeatureBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Function<Dynamic<?>, FC> configfactory, boolean doBlockNotify, IFactory<FC, T> factory) {
    super(owner, parent, name, callback, Feature.class);
    this.factory = factory;
    this.configFactory = configfactory;
    this.doBlockNotify = doBlockNotify;
  }

  @Override
  protected T createEntry() {
    return factory.create(configFactory, doBlockNotify);
  }

  @FunctionalInterface
  public interface IFactory<FC extends IFeatureConfig, T extends Feature<FC>> {
    T create(Function<Dynamic<?>, FC> factory, boolean doBlockNotify);
  }
}
