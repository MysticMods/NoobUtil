package noobanidus.libs.noobutil.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IPlayerContainer {
  PlayerEntity getPlayer();

  @Nullable
  default World getPlayerWorld () {
    if (getPlayer() == null) {
      return null;
    }
    return getPlayer().level;
  }

  List<Slot> getPlayerSlots();
}
