package noobanidus.libs.noobutil.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ConfiguredRegistry<T> {
  private final Map<ResourceLocation, Entry<T>> entries = new ConcurrentHashMap<>();
  private final Registry<T> registry;
  private boolean registered = false;

  public ConfiguredRegistry(Registry<T> registry) {
    this.registry = registry;
  }

  public Entry<T> register(String name, Supplier<T> value) {
    synchronized (entries) {
      ResourceLocation rl = new ResourceLocation(ModData.getModid(), name);
      if (entries.containsKey(rl)) {
        throw new IllegalArgumentException("Key '" + rl + "' already exists for '" + registry + "'");
      }
      Entry<T> result = new Entry<>(value, new ResourceLocation(ModData.getModid(), name));
      entries.put(rl, result);
      return result;
    }
  }

  public void register() {
    synchronized (entries) {
      if (registered) {
        throw new IllegalStateException("Attempted to register configured registry " + this + " when it has already been marked as registered.");
      }
      for (Map.Entry<ResourceLocation, Entry<T>> entry : entries.entrySet()) {
        entry.getValue().registered = true;
        T value = entry.getValue().get();
        Registry.register(this.registry, entry.getKey(), value);
      }
      registered = true;
    }
  }

  public static class Entry<T> extends LazySupplier<T> {
    private boolean registered = false;
    private final ResourceLocation rl;

    public Entry(Supplier<T> supplierIn, ResourceLocation rl) {
      super(supplierIn);
      this.rl = rl;
    }

    @Override
    public T get() {
      if (!isRegistered()) {
        throw new IllegalStateException("Tried to access entry '" + rl + "' before initialization");
      }
      return super.get();
    }

    public boolean isRegistered() {
      return registered;
    }

    public ResourceLocation getId () {
      return rl;
    }
  }
}
