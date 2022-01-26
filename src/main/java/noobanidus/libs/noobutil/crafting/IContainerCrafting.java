package noobanidus.libs.noobutil.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.List;

public interface IContainerCrafting<H extends ILargeInventory, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IReferentialBlockEntity> extends ICrafting<H, T> {

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
