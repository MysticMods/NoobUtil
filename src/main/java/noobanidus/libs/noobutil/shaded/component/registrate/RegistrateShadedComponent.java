package noobanidus.libs.noobutil.shaded.component.registrate;

import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.noobutil.shaded.component.ShadedComponent;

public abstract class RegistrateShadedComponent<T extends IForgeRegistryEntry<T>> extends ShadedComponent<T> {
  protected final CustomRegistrate registrate;

  public RegistrateShadedComponent(Class<T> clazz, CustomRegistrate registrate) {
    super(clazz);
    this.registrate = registrate;
  }
}
