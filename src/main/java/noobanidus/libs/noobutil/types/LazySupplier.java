package noobanidus.libs.noobutil.types;

import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public class LazySupplier<T> extends LazyValue<T> implements Supplier<T> {
  public LazySupplier(Supplier<T> supplier) {
    super(supplier);
  }

  @Override
  public T get() {
    return getValue();
  }
}
