package noobanidus.libs.noobutil.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.List;
import java.util.UUID;

public interface ICrafter<H extends ILargeInventory, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IReferentialBlockEntity> extends IInventory, IIInvWrapper<H> {

  C getContainer();

  T getBlockEntity();

  PlayerEntity getPlayer();

  default PlayerInventory getPlayerInventory() {
    return getPlayer().inventory;
  }

  @Override
  H getHandler();

  List<Slot> getCombinedIngredientSlots();

  default NonNullList<ItemStack> getCombinedItems() {
    NonNullList<ItemStack> result = NonNullList.of(ItemStack.EMPTY);
    for (Slot slot : getCombinedIngredientSlots()) {
      if (slot.hasItem()) {
        result.add(slot.getItem());
      }
    }
    return result;
  }
}
