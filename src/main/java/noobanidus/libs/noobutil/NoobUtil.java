package noobanidus.libs.noobutil;

import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoobUtil {
  public static CustomRegistrate REGISTRATE;

  public static Logger logger = LogManager.getLogger();

  public NoobUtil() {
    REGISTRATE = CustomRegistrate.create("noobutil");
  }
}
