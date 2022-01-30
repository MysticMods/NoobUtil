package noobanidus.libs.noobutil.getter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class ServerGetter implements Getter {
  public static ServerGetter INSTANCE = new ServerGetter();

  @Nullable
  @Override
  public Player getterGetPlayer() {
    return null;
  }

  @Nullable
  @Override
  public Level getterGetWorld() {
    MinecraftServer server = getterGetServer();
    if (server != null) {
      return server.getLevel(Level.OVERWORLD);
    }
    return null;
  }

  @Nullable
  @Override
  public AbstractContainerMenu getterGetContainer() {
    return null;
  }

  @Nullable
  @Override
  public MinecraftServer getterGetServer() {
    return ServerLifecycleHooks.getCurrentServer();
  }
}
