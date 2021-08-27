package noobanidus.libs.noobutil.events;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Remapper {
  private static final Logger LOG = LogManager.getLogger();

  private final String modid;

  private final List<Remap<?>> remapper = new ArrayList<>();

  private final Map<IForgeRegistry<?>, Map<ResourceLocation, Remap<?>>> cache = new HashMap<>();

  private Map<ResourceLocation, Remap<?>> getRemap(IForgeRegistry<?> registry) {
    return cache.computeIfAbsent(registry, (k) -> remapper.stream().filter(o -> o.getRegistry() == k || (o.getRegistry() == null && o.ignored())).collect(Collectors.toMap(Remap::getOldKey, o -> o)));
  }

  public Remapper(String modid) {
    this.modid = modid;
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SafeVarargs
  public final <T extends IForgeRegistryEntry<T>> void remap(String modid, T... entries) {
    for (T entry : entries) {
      ResourceLocation old_id = new ResourceLocation(modid, Objects.requireNonNull(entry.getRegistryName()).getPath());
      remap(old_id, entry);
    }
  }

  @SafeVarargs
  public final <T extends IForgeRegistryEntry<T>> void remap(String modid, RegistryEntry<T>... entries) {
    for (RegistryEntry<T> entry : entries) {
      ResourceLocation old_id = new ResourceLocation(modid, entry.getId().getPath());
      remap(old_id, entry);
    }
  }

  public <T extends IForgeRegistryEntry<T>> void remap(ResourceLocation old_id, T entry) {
    remapper.add(Remap.of(old_id, entry));
  }

  public <T extends IForgeRegistryEntry<T>> void remap(ResourceLocation old_id, RegistryEntry<T> entry) {
    remapper.add(Remap.of(old_id, entry));
  }

  public <T extends IForgeRegistryEntry<T>> void ignore (ResourceLocation old_id) {
    remapper.add(Remap.ignore(old_id));
  }

  @SubscribeEvent
  public <T extends IForgeRegistryEntry<T>> void onRemapEvent(RegistryEvent.MissingMappings<T> event) {
    Map<ResourceLocation, Remap<?>> remaps = getRemap(event.getRegistry());
    if (remaps.isEmpty()) {
      return;
    }
    ImmutableList<? extends RegistryEvent.MissingMappings.Mapping<T>> mappings = event.getAllMappings();

    if (mappings != null) {
      for (RegistryEvent.MissingMappings.Mapping<T> map : mappings) {
        Remap<?> remap = remaps.get(map.key);
        String registry = modid + ":" + event.getRegistry().getRegistryName();
        if (remap == null) {
          LOG.debug("Remapper for " + registry + " skipped missing mapping: " + map.key);
        } else if (remap.ignored()) {
          LOG.debug("Remapper for " + registry + " ignored mapping: " + map.key);
          map.ignore();
        } else {
          LOG.debug("Remapper for " + registry + " remapped: " + map.key + " to: " + remap.getKey());
          try {
            //noinspection unchecked
            map.remap((T) remap.getEntry());
          } catch (Exception e) {
            LOG.debug("Remapper for " + registry + " failed on mapping: " + map.key + ", remapping to " + remap.getKey() + " failed with exception.", e);
          }
        }
      }
    }
  }

  public static class Remap<T extends IForgeRegistryEntry<T>> {
    public static ResourceLocation IGNORE = new ResourceLocation("noobutil", "ignore");

    private final T entry;
    private final RegistryEntry<T> registryEntry;
    private final ResourceLocation resourceLocation;
    private final ResourceLocation oldResourceLocation;

    protected Remap(ResourceLocation oldResourceLocation) {
      this.entry = null;
      this.registryEntry = null;
      this.resourceLocation = IGNORE;
      this.oldResourceLocation = oldResourceLocation;
    }

    protected Remap(ResourceLocation oldResourceLocation, T entry) {
      this.entry = entry;
      this.registryEntry = null;
      this.resourceLocation = entry.getRegistryName();
      this.oldResourceLocation = oldResourceLocation;
    }

    protected Remap(ResourceLocation oldResourceLocation, RegistryEntry<T> registryEntry) {
      this.entry = null;
      this.registryEntry = registryEntry;
      this.resourceLocation = registryEntry.getId();
      this.oldResourceLocation = oldResourceLocation;
    }

    public T getEntry() {
      if (this.entry != null) {
        return this.entry;
      }

      if (this.registryEntry != null) {
        return registryEntry.get();
      }

      throw new IllegalArgumentException("Both entry and registry entry are null for Remap: " + resourceLocation);
    }

    public ResourceLocation getKey() {
      return resourceLocation;
    }

    public ResourceLocation getOldKey() {
      return oldResourceLocation;
    }

    public boolean ignored () {
      return resourceLocation.equals(IGNORE);
    }

    @Nullable
    public IForgeRegistry<T> getRegistry () {
      T entry = getEntry();
      if (entry == null) {
        if (!ignored()) {
          Remapper.LOG.error("Remap from " + oldResourceLocation + " to " + resourceLocation + " failed: entry is null.");
        }
        return null;
      }

      return RegistryManager.ACTIVE.getRegistry(entry.getRegistryType());
    }

    public static <T extends IForgeRegistryEntry<T>> Remap<T> of(ResourceLocation oldKey, T entry) {
      return new Remap<>(oldKey, entry);
    }

    public static <T extends IForgeRegistryEntry<T>> Remap<T> of(ResourceLocation oldKey, RegistryEntry<T> entry) {
      return new Remap<>(oldKey, entry);
    }

    public static Remap<?> ignore (ResourceLocation old_key) {
      return new Remap<>(old_key);
    }
  }
}
