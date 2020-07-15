package noobanidus.libs.noobutil.registrate;

import com.mojang.datafixers.Dynamic;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.function.Function;

public class PlacementBuilder<DC extends IPlacementConfig, T extends Placement<DC>, P> extends AbstractBuilder<Placement<?>, T, P, PlacementBuilder<DC, T, P>> {
  private IFactory<DC, T> factory;
  private Function<Dynamic<?>, DC> configFactory;

  public PlacementBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Function<Dynamic<?>, DC> configfactory, IFactory<DC, T> factory) {
    super(owner, parent, name, callback, Placement.class);
    this.factory = factory;
    this.configFactory = configfactory;
  }

  @Override
  protected T createEntry() {
    return factory.create(configFactory);
  }

  @FunctionalInterface
  public interface IFactory<DC extends IPlacementConfig, T extends Placement<DC>> {
    T create(Function<Dynamic<?>, DC> factory);
  }
}
