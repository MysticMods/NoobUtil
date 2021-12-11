package noobanidus.libs.noobutil.reference;

public class Modid {
  private static String modid = null;

  public static String getModid() {
    if (modid == null) {
      throw new IllegalStateException("Modid cannot be null at this time");
    }
    return modid;
  }

  public static void setModid(String modid) {
    Modid.modid = modid;
  }
}
