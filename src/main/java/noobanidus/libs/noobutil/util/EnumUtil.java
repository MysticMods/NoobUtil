package noobanidus.libs.noobutil.util;

public class EnumUtil {
  @SuppressWarnings("unchecked")
  public static <T extends Enum<T>> T fromOrdinal (T i, int ordinal) {
    T[] values = ((Class<T>) i.getClass()).getEnumConstants();
    return ordinal >= 0 && ordinal < values.length ? values[ordinal] : null;
  }

  public static <T extends Enum<T>> T fromOrdinal (Class<T> i, int ordinal) {
    T[] values = i.getEnumConstants();
    return ordinal >= 0 && ordinal < values.length ? values[ordinal] : null;
  }
}
