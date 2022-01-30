package noobanidus.libs.noobutil.getter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import noobanidus.libs.noobutil.client.ClientGetter;

import javax.annotation.Nullable;

public interface Getter {
  @Nullable
  Player getterGetPlayer();

  @Nullable
  Level getterGetWorld();

  @Nullable
  AbstractContainerMenu getterGetContainer();

  @Nullable
  MinecraftServer getterGetServer();

  @Nullable
  static Player getPlayer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetPlayer, () -> ServerGetter.INSTANCE::getterGetPlayer);
  }

  @Nullable
  static Level getWorld() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetWorld, () -> ServerGetter.INSTANCE::getterGetWorld);
  }

  @Nullable
  static AbstractContainerMenu getContainer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetContainer, () -> ServerGetter.INSTANCE::getterGetContainer);
  }

  @Nullable
  static MinecraftServer getServer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetServer, () -> ServerGetter.INSTANCE::getterGetServer);
  }
}
