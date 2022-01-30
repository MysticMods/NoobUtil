package noobanidus.libs.noobutil.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import noobanidus.libs.noobutil.getter.Getter;

import javax.annotation.Nullable;

public class ClientGetter implements Getter {
  public static ClientGetter INSTANCE = new ClientGetter();

  @Nullable
  public Player getterGetPlayer() {
    return Minecraft.getInstance().player;
  }

  @Nullable
  public Level getterGetWorld() {
    return Minecraft.getInstance().level;
  }

  @Nullable
  public AbstractContainerMenu getterGetContainer() {
    Player player = getterGetPlayer();
    if (player != null) {
      return player.containerMenu;
    }
    return null;
  }

  @Nullable
  @Override
  public MinecraftServer getterGetServer() {
    return Minecraft.getInstance().getSingleplayerServer();
  }
}
