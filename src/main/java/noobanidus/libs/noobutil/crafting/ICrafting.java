package noobanidus.libs.noobutil.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;

import javax.annotation.Nullable;

public interface ICrafting<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity> extends IInventory, IIInvWrapper<H> {

  T getBlockEntity();

  @Nullable
  PlayerEntity getPlayer();

  @Nullable
  default PlayerInventory getPlayerInventory() {
    PlayerEntity player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.inventory;
  }

  @Override
  H getHandler();
}
