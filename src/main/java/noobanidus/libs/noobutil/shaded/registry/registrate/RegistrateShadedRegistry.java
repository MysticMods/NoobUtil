package noobanidus.libs.noobutil.shaded.registry.registrate;

import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.noobutil.shaded.registry.ShadedRegistry;

public class RegistrateShadedRegistry<T extends IForgeRegistryEntry<T>> extends ShadedRegistry<T> {
  protected final CustomRegistrate registrate;

  public RegistrateShadedRegistry(Class<T> type, CustomRegistrate registrate) {
    super(type, registrate.getModid());
    this.registrate = registrate;
  }
}
