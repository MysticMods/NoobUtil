package noobanidus.libs.noobutil.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;

import javax.annotation.Nullable;

public interface ILargeInventory extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  void enlarge(int size);

  CompoundNBT serialize();

  void deserialize(CompoundNBT result);

  default void setInventoryData(StoredInventoryData<ILargeInventory> data) {
  }

  @Nullable
  default StoredInventoryData<ILargeInventory> getInventoryData() {
    return null;
  }

  default void markDirty() {
    StoredInventoryData<ILargeInventory> data = getInventoryData();
    if (data != null) {
      data.setDirty();
    }
  }

  void addListener(IInventoryListener listener);
}
