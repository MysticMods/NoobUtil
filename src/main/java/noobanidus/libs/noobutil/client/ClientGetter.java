package noobanidus.libs.noobutil.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.getter.Getter;

import javax.annotation.Nullable;

public class ClientGetter implements Getter {
  public static ClientGetter INSTANCE = new ClientGetter();

  @Nullable
  public PlayerEntity getterGetPlayer() {
    return Minecraft.getInstance().player;
  }

  @Nullable
  public World getterGetWorld() {
    return Minecraft.getInstance().world;
  }

  @Nullable
  public Container getterGetContainer() {
    PlayerEntity player = getterGetPlayer();
    if (player != null) {
      return player.openContainer;
    }
    return null;
  }

  @Nullable
  @Override
  public MinecraftServer getterGetServer() {
    return Minecraft.getInstance().getIntegratedServer();
  }
}
