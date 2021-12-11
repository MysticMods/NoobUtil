package noobanidus.libs.noobutil.inventory;

import net.minecraft.inventory.IInventory;

@FunctionalInterface
public interface ISlotListener {
  void slotChanged (IInventory inventory, int slot);
}
