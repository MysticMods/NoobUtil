package noobanidus.libs.noobutil.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;

import javax.annotation.Nullable;

public interface ICrafting<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity> extends Container, IIInvWrapper<H> {

  T getBlockEntity();

  @Nullable
  Player getPlayer();

  @Nullable
  default Inventory getPlayerInventory() {
    Player player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.getInventory();
  }

  @Override
  H getHandler();
}
