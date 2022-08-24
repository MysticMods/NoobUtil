package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nonnull;

public class ContainerBuilder<T extends AbstractContainerMenu, P> extends AbstractBuilder<MenuType<?>, MenuType<T>, P, ContainerBuilder<T, P>> {
  private final MenuType.MenuSupplier<T> factory;

  public ContainerBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, MenuType.MenuSupplier<T> factory) {
    super(owner, parent, name, callback, Registry.MENU_REGISTRY);
    this.factory = factory;
  }

  @Override
  @Nonnull
  protected MenuType<T> createEntry() {
    return new MenuType<>(factory);
  }
}
