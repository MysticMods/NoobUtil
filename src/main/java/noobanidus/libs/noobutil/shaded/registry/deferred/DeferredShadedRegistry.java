package noobanidus.libs.noobutil.shaded.registry.deferred;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.shaded.registry.ShadedRegistry;

import java.util.HashMap;
import java.util.Map;

public class DeferredShadedRegistry<T extends IForgeRegistryEntry<T>> extends ShadedRegistry<T> {
  public static Map<String, Map<Class<? extends IForgeRegistryEntry<?>>, DeferredRegister<?>>> registerCache = new HashMap<>();
  public static Map<String, Map<Class<? extends IForgeRegistryEntry<?>>, DeferredShadedRegistry<?>>> registryCache = new HashMap<>();

  protected DeferredShadedRegistry(Class<T> type, String modid) {
    super(type, modid);
  }

  @Override
  public void init() {
    super.init();
  }

  @SuppressWarnings("unchecked")
  public DeferredRegister<T> getRegistry() {
    return (DeferredRegister<T>) registerCache.computeIfAbsent(this.modid, (k) -> new HashMap<>()).computeIfAbsent(this.type, (k) -> DeferredRegister.create(this.type, this.modid));
  }

  @Override
  public void register(IEventBus bus) {
    for (Map<Class<? extends IForgeRegistryEntry<?>>, DeferredRegister<?>> entry1 : registerCache.values()) {
      for (DeferredRegister<?> register : entry1.values()) {
        register.register(bus);
      }
    }
    super.register(bus);

  }

  public static <T extends IForgeRegistryEntry<T>> DeferredShadedRegistry<T> getOrCreate(Class<T> type, String modid) {
    //noinspection unchecked
    return (DeferredShadedRegistry<T>) registryCache.computeIfAbsent(modid, (k) -> new HashMap<>()).computeIfAbsent(type, (k) -> new DeferredShadedRegistry<>(type, modid));
  }
}
