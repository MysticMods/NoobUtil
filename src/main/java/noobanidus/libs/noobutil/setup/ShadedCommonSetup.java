package noobanidus.libs.noobutil.setup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.libs.noobutil.modifiers.PlayerModifierRegistry;

public class ShadedCommonSetup {
  public static void init (FMLCommonSetupEvent event) {
    MinecraftForge.EVENT_BUS.addListener(PlayerModifierRegistry.getInstance()::onEntityConstructed);
  }
}
