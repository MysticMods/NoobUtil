package noobanidus.libs.noobutil.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.IItemHandlerModifiable;
import noobanidus.libs.noobutil.data.server.StoredInventoryData;

import javax.annotation.Nullable;

public interface ILargeInventory extends IItemHandlerModifiable {
  default int getSlots() {
    return this.size();
  }

  int size();

  long getCountInSlot(int slot);

  void resize(int size);

  CompoundTag serialize();

  void deserialize(CompoundTag result);

  default <T extends ILargeInventory> void setInventoryData(StoredInventoryData<T> data) {
  }

  @Nullable
  default <T extends ILargeInventory> StoredInventoryData<T> getInventoryData() {
    return null;
  }

  default void markDirty() {
    StoredInventoryData<?> data = getInventoryData();
    if (data != null) {
      data.setDirty();
    }
  }

  void addListener(IInventoryListener listener);
}
