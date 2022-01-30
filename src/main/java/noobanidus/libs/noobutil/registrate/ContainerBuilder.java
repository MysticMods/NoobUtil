package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ContainerBuilder<T extends AbstractContainerMenu, P> extends AbstractBuilder<MenuType<?>, MenuType<T>, P, ContainerBuilder<T, P>> {
  private final MenuType.MenuSupplier<T> factory;

  public ContainerBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, MenuType.MenuSupplier<T> factory) {
    super(owner, parent, name, callback, MenuType.class);
    this.factory = factory;
  }

  @Override
  protected MenuType<T> createEntry() {
    return new MenuType<>(factory);
  }
}
