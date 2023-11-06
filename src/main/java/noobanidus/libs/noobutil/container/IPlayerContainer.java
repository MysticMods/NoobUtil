package noobanidus.libs.noobutil.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface IPlayerContainer {
  Player getPlayer();

  @Nullable
  default Level getPlayerWorld () {
    if (getPlayer() == null) {
      return null;
    }
    return getPlayer().level();
  }

  List<Slot> getPlayerSlots();
}
