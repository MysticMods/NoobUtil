package noobanidus.libs.noobutil.reference;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModData {
  public static Logger logger = LogManager.getLogger();
  private static String modid = null;
  private static String modIdentifier = null;

  public static String getModid() {
    if (modid == null) {
      throw new IllegalStateException("Modid cannot be null at this time");
    }
    return modid;
  }

  public static void setModid(String modid) {
    ModData.modid = modid;
  }

  public static String getModIdentifier () {
    if (modIdentifier == null) {
      throw new IllegalStateException("ModIdentifier cannot be null at this time");
    }
    return modIdentifier;
  }

  public static void setModIdentifier(String modIdentifier) {
    ModData.modIdentifier = modIdentifier;
  }

  public static void setIdAndIdentifier (String modid, String modIdentifier) {
    setModid(modid);
    setModIdentifier(modIdentifier);
  }

  public static ResourceLocation getResourceLocation(String path) {
    return new ResourceLocation(getModid(), path);
  }
}
