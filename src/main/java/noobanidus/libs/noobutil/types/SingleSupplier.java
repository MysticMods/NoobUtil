package noobanidus.libs.noobutil.types;

import java.util.function.Supplier;

public class SingleSupplier<T> implements Supplier<T> {
  private T value = null;
  private final Supplier<T> creator;

  public SingleSupplier(Supplier<T> creator) {
    this.creator = creator;
  }

  @Override
  public T get() {
    if (value == null) {
      value = creator.get();
    }
    return value;
  }
}
