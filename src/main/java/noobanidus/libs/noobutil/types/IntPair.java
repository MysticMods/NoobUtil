package noobanidus.libs.noobutil.types;

public class IntPair<T> {
  private final int integer;
  private final T value;

  public IntPair(int integer, T value) {
    this.integer = integer;
    this.value = value;
  }

  public int getInt() {
    return integer;
  }

  public T getValue() {
    return value;
  }
}
