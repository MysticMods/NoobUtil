package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class EffectBuilder<T extends MobEffect, P> extends AbstractBuilder<MobEffect, T, P, EffectBuilder<T, P>> {
  private Supplier<T> factory;

  protected EffectBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Supplier<T> factory) {
    super(owner, parent, name, callback, MobEffect.class);
    this.factory = factory;
  }

  public static <T extends MobEffect, P> EffectBuilder<T, P> create(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Supplier<T> factory) {
    return new EffectBuilder<>(owner, parent, name, callback, factory).defaultLang();
  }

  @Override
  protected T createEntry() {
    return factory.get();
  }

  public EffectBuilder<T, P> defaultLang() {
    return this.lang(MobEffect::getDescriptionId);
  }

  public EffectBuilder<T, P> lang(String name) {
    return this.lang(MobEffect::getDescriptionId, name);
  }
}