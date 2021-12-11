package noobanidus.libs.noobutil.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;

import javax.annotation.Nullable;

public interface ILargeInventory<T extends StoredInventoryData<?>> extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  void enlarge(int size);

  CompoundNBT serialize();

  void deserialize(CompoundNBT result);

  default void setInventoryData(T data) {
  }

  @Nullable
  default T getInventoryData() {
    return null;
  }

  default void markDirty() {
    T data = getInventoryData();
    if (data != null) {
      data.setDirty();
    }
  }

  void addListener(IInventoryListener listener);
}
