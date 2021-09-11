package noobanidus.libs.noobutil.shaded.component.deferred;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.shaded.component.ShadedComponent;
import noobanidus.libs.noobutil.shaded.registry.deferred.DeferredShadedRegistry;

public abstract class DeferredShadedComponent<T extends IForgeRegistryEntry<T>> extends ShadedComponent<T> {
  protected final DeferredShadedRegistry<T> registry;

  public DeferredShadedComponent(Class<T> clazz, DeferredShadedRegistry<T> registry) {
    super(clazz);
    this.registry = registry;
  }
}
