package noobanidus.libs.noobutil.data.server;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import noobanidus.libs.noobutil.inventory.ILargeInventory;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.reference.NBTConstants;

import java.util.UUID;
import java.util.function.IntFunction;

public class StoredInventoryData<T extends ILargeInventory> extends WorldSavedData {
  private UUID id;
  private final T inventory;
  private int size;

  public static String ID(UUID id) {
    return ModData.getModIdentifier() + "-Inventory-" + id.toString();
  }

  public StoredInventoryData(UUID id, int size, IntFunction<T> builder) {
    super(ID(id));
    this.id = id;
    this.size = size;
    this.inventory = builder.apply(this.size);
  }

  public T getInventory() {
    return inventory;
  }

  public UUID getUUID() {
    return id;
  }

  @Override
  public void load(CompoundNBT nbt) {
    this.id = nbt.getUUID(NBTConstants.SavedInventoryData.Id);
    this.size = nbt.getInt(NBTConstants.SavedInventoryData.Size);
    if (nbt.contains(NBTConstants.SavedInventoryData.Inventory, Constants.NBT.TAG_COMPOUND)) {
      this.inventory.deserialize(nbt.getCompound(NBTConstants.SavedInventoryData.Inventory));
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound.putInt(NBTConstants.SavedInventoryData.Size, this.size);
    compound.putUUID(NBTConstants.SavedInventoryData.Id, this.id);
    if (this.inventory != null) {
      compound.put(NBTConstants.SavedInventoryData.Inventory, this.inventory.serialize());
    }
    return compound;
  }
}
