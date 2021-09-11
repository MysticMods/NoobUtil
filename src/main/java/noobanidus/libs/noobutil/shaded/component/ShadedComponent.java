package noobanidus.libs.noobutil.shaded.component;

import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class ShadedComponent<T extends IForgeRegistryEntry<T>> {
  protected final Class<T> clazz;

  public ShadedComponent(Class<T> clazz) {
    this.clazz = clazz;
  }

  public abstract void init();

  public abstract void register();
}
