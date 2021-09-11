package noobanidus.libs.noobutil.shaded.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.shaded.component.ShadedComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class ShadedRegistry<T extends IForgeRegistryEntry<T>> {
  protected final Class<T> type;
  protected final String modid;
  protected final List<ShadedComponent<T>> components = new ArrayList<>();

  public ShadedRegistry(Class<T> type, String modid) {
    this.type = type;
    this.modid = modid;
  }

  public void addComponent (ShadedComponent<T> component) {
    this.components.add(component);
  }

  public void init () {
    this.components.forEach(ShadedComponent::init);
  }

  public void register (IEventBus bus) {
    this.components.forEach(ShadedComponent::register);
  }
}
