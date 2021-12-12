package noobanidus.libs.noobutil.data.server;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import noobanidus.libs.noobutil.inventory.ILargeInventory;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.reference.NBTIdentifiers;

import java.io.File;
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
    this.id = nbt.getUUID(NBTIdentifiers.SavedInventoryData.Id);
    this.size = nbt.getInt(NBTIdentifiers.SavedInventoryData.Size);
    if (nbt.contains(NBTIdentifiers.SavedInventoryData.Inventory, Constants.NBT.TAG_COMPOUND)) {
      this.inventory.deserialize(nbt.getCompound(NBTIdentifiers.SavedInventoryData.Inventory));
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound.putInt(NBTIdentifiers.SavedInventoryData.Size, this.size);
    compound.putUUID(NBTIdentifiers.SavedInventoryData.Id, this.id);
    if (this.inventory != null) {
      compound.put(NBTIdentifiers.SavedInventoryData.Inventory, this.inventory.serialize());
    }
    return compound;
  }
}
