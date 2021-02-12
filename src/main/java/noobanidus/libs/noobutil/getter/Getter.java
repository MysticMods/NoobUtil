package noobanidus.libs.noobutil.getter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import noobanidus.libs.noobutil.client.ClientGetter;

import javax.annotation.Nullable;

public interface Getter {
  @Nullable
  PlayerEntity getterGetPlayer();

  @Nullable
  World getterGetWorld();

  @Nullable
  Container getterGetContainer();

  @Nullable
  MinecraftServer getterGetServer();

  @Nullable
  static PlayerEntity getPlayer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetPlayer, () -> ServerGetter.INSTANCE::getterGetPlayer);
  }

  @Nullable
  static World getWorld() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetWorld, () -> ServerGetter.INSTANCE::getterGetWorld);
  }

  @Nullable
  static Container getContainer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetContainer, () -> ServerGetter.INSTANCE::getterGetContainer);
  }

  @Nullable
  static MinecraftServer getServer() {
    return DistExecutor.safeRunForDist(() -> ClientGetter.INSTANCE::getterGetServer, () -> ServerGetter.INSTANCE::getterGetServer);
  }
}
