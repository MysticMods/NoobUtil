package noobanidus.libs.noobutil.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.libs.noobutil.modifiers.PlayerModifierRegistry;

public class ShadedCommonSetup {
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(PlayerModifierRegistry::init);
  }
}
