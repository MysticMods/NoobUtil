package noobanidus.libs.noobutil.getter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class ServerGetter implements Getter {
  public static ServerGetter INSTANCE = new ServerGetter();

  @Nullable
  @Override
  public PlayerEntity getterGetPlayer() {
    return null;
  }

  @Nullable
  @Override
  public World getterGetWorld() {
    MinecraftServer server = getterGetServer();
    if (server != null) {
      return server.getLevel(World.OVERWORLD);
    }
    return null;
  }

  @Nullable
  @Override
  public Container getterGetContainer() {
    return null;
  }

  @Nullable
  @Override
  public MinecraftServer getterGetServer() {
    return ServerLifecycleHooks.getCurrentServer();
  }
}
