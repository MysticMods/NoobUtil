package noobanidus.libs.noobutil.util;

public class ClassUtil {
  @SuppressWarnings("unchecked")
  public static <T> Class<T> convertClass(Class<?> cls) {
    return (Class<T>) cls;
  }
}
