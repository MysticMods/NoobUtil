package noobanidus.libs.noobutil.data.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.saveddata.SavedData;
import noobanidus.libs.noobutil.inventory.ILargeInventory;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.reference.NBTConstants;

import java.util.UUID;
import java.util.function.IntFunction;

public class StoredInventoryData<T extends ILargeInventory> extends SavedData {
  private UUID id;
  private final T inventory;
  private int size;

  public static String ID(UUID id) {
    return ModData.getModIdentifier() + "-Inventory-" + id.toString();
  }

  public StoredInventoryData(UUID id, int size, IntFunction<T> builder) {
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

  // TODO:
  public void load(CompoundTag nbt) {
    this.id = nbt.getUUID(NBTConstants.SavedInventoryData.Id);
    this.size = nbt.getInt(NBTConstants.SavedInventoryData.Size);
    if (nbt.contains(NBTConstants.SavedInventoryData.Inventory, Tag.TAG_COMPOUND)) {
      this.inventory.deserialize(nbt.getCompound(NBTConstants.SavedInventoryData.Inventory));
    }
  }

  @Override
  public CompoundTag save(CompoundTag compound) {
    compound.putInt(NBTConstants.SavedInventoryData.Size, this.size);
    compound.putUUID(NBTConstants.SavedInventoryData.Id, this.id);
    if (this.inventory != null) {
      compound.put(NBTConstants.SavedInventoryData.Inventory, this.inventory.serialize());
    }
    return compound;
  }
}
