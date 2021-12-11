package noobanidus.libs.noobutil.block.entities;

import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;

public interface IInventoryBlockEntity<T extends ILargeInventory<?>> extends IReferentialBlockEntity {
  @Nullable
  T getBlockEntityInventory();

  T getEmptyInventory();
}
