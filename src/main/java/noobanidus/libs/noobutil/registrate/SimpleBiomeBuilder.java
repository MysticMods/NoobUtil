package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.world.biome.Biome;

import java.util.function.Supplier;

public class SimpleBiomeBuilder<T extends Biome, P> extends AbstractBuilder<Biome, T, P, SimpleBiomeBuilder<T, P>> {
  private Supplier<T> factory;

  protected SimpleBiomeBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Supplier<T> factory) {
    super(owner, parent, name, callback, Biome.class);
    this.factory = factory;
  }

  public static <T extends Biome, P> SimpleBiomeBuilder<T, P> create(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Supplier<T> factory) {
    return new SimpleBiomeBuilder<>(owner, parent, name, callback, factory).defaultLang();
  }

  @Override
  protected T createEntry() {
    return factory.get();
  }

  public SimpleBiomeBuilder<T, P> defaultLang() {
    return this.lang(Biome::getTranslationKey);
  }

  public SimpleBiomeBuilder<T, P> lang(String name) {
    return this.lang(Biome::getTranslationKey, name);
  }
}
