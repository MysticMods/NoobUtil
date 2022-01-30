package noobanidus.libs.noobutil.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfiguredRegistry<T> {
  private final Map<String, T> entries = new ConcurrentHashMap<>();
  private final Registry<T> registry;
  private boolean registered = false;
  private final String modid;

  public ConfiguredRegistry(String modid, Registry<T> registry) {
    this.registry = registry;
    this.modid = modid;
  }

  public T register(String name, T value) {
    T current = entries.get(name);
    if (current != null) {
      throw new IllegalArgumentException("Key '" + name + "' already exists for this registry.");
    }
    entries.put(name, value);
    return value;
  }

  public void registration() {
    if (registered) {
      throw new IllegalStateException("Attempted to register configured registry " + this + " when it has already been marked as registered.");
    }
    for (Map.Entry<String, T> entry : entries.entrySet()) {
      Registry.register(this.registry, new ResourceLocation(modid, entry.getKey()), entry.getValue());
    }
    registered = true;
  }
}
