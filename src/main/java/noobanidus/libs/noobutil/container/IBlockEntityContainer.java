package noobanidus.libs.noobutil.container;

import net.minecraft.world.Container;
import noobanidus.libs.noobutil.block.entities.IInventoryBlockEntity;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;

public interface IBlockEntityContainer<V extends ILargeInventory, T extends IInventoryBlockEntity<V>> {
  @Nullable
  T getBlockEntity();

  @Nullable
  default V getBlockEntityInventory() {
    if (getBlockEntity() == null) {
      return null;
    }
    return getBlockEntity().getBlockEntityInventory();
  }

  @Nullable
  V getEmptyInventory();

  void inventoryChanged(ILargeInventory inventory, int slot);

  default void inventoryChanged(Container inventory, int slot) {

  }
}
