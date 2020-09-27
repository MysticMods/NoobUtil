package noobanidus.libs.noobutil.types;

import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public class LazySupplier<T> extends LazyValue<T> implements Supplier<T> {
  public LazySupplier(Supplier<T> supplierIn) {
    super(supplierIn);
  }

  @Override
  public T get() {
    return super.getValue();
  }
}
