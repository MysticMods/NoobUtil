package noobanidus.libs.noobutil.inventory;

@FunctionalInterface
public interface IInventoryListener {
  void inventoryChanged (ILargeInventory<?> inventory, int slot);
}
