package noobanidus.libs.noobutil.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import noobanidus.libs.noobutil.inventory.IInventoryListener;
import noobanidus.libs.noobutil.inventory.ILargeInventory;
import noobanidus.libs.noobutil.type.ItemStackEntry;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLargeItemHandler implements ILargeInventory {
  // TODO: THIS
  // TODO: Potentially separate slot info metadata from stacks
  protected NonNullList<ItemStackEntry> stacks;
  protected Set<IInventoryListener> listeners = new HashSet<>();

  public AbstractLargeItemHandler() {
    this(1);
  }

  public AbstractLargeItemHandler(int size) {
    stacks = NonNullList.withSize(size, ItemStackEntry.EMPTY);
  }

  public AbstractLargeItemHandler(NonNullList<ItemStackEntry> stacks) {
    this.stacks = stacks;
  }

  @Override
  public void resize(int size) {
    if (size < stacks.size()) {
      throw new RuntimeException("Cannot reduce the size of an AbstractLargeItemHandler, currently contains: " + stacks.size() + " slots, cannot reduce to " + size + " slots");
    } else if (size == stacks.size()) {
      return;
    } else {
      NonNullList<ItemStackEntry> newList = NonNullList.withSize(size, ItemStackEntry.EMPTY);
      for (int i = 0; i < stacks.size(); i++) {
        newList.set(i, stacks.get(i));
      }
      this.stacks = newList;
    }
  }

  public abstract int getSlotLimit(int slot);

  public int getSlotLimit(int slot, ItemStack stack) {
    return getSlotLimit(slot);
  }

  public int size() {
    return stacks.size();
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    validateSlotIndex(slot);
    ItemStack inSlot = this.stacks.get(slot).getStackOriginal();
    if (!ItemStack.isSame(inSlot, stack)) {
      this.stacks.set(slot, new ItemStackEntry(stack));
      onContentsChanged(slot);
    }
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return stacks.get(slot).getStackOriginal();
  }

  @Override
  public long getCountInSlot(int slot) {
    validateSlotIndex(slot);
    return stacks.get(slot).getCount();
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (!isItemValid(slot, stack)) {
      return stack;
    }

    validateSlotIndex(slot);

    ItemStackEntry entry = this.stacks.get(slot);
    ItemStack existing = entry.getStackOriginal();

    int limit = getSlotLimit(slot, stack);

    if (!existing.isEmpty()) {
      if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
        return stack;
      }

      limit -= existing.getCount();
    }

    if (limit <= 0)
      return stack;

    boolean reachedLimit = stack.getCount() > limit;

    if (!simulate) {
      if (existing.isEmpty()) {
        if (reachedLimit) {
          // TODO: MAINTAIN METADATA?
          entry = new ItemStackEntry(stack.copy());
          entry.setCount(limit);
          this.stacks.set(slot, entry);
        } else {
          // TODO: MAINTAIN METADATA?
          entry = new ItemStackEntry(stack);
          this.stacks.set(slot, entry);
        }
      } else {
        entry.grow(stack.getCount());
      }
      onContentsChanged(slot);
    }

    return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (amount == 0) {
      return ItemStack.EMPTY;
    }

    validateSlotIndex(slot);
    ItemStackEntry entry = this.stacks.get(slot);
    ItemStack existing = entry.getStackOriginal();
    if (existing.isEmpty()) {
      return ItemStack.EMPTY;
    }

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.getCount() <= toExtract) {
      if (!simulate) {
        // TODO: Metadata??
        this.stacks.set(slot, ItemStackEntry.EMPTY);
        onContentsChanged(slot);
        return existing;
      } else {
        return existing.copy();
      }
    } else {
      if (!simulate) {
        // TODO: Metdata?
        entry = new ItemStackEntry(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
        this.stacks.set(slot, entry);
        onContentsChanged(slot);
      }

      return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
    }
  }

  @Override
  public abstract boolean isItemValid(int slot, @Nonnull ItemStack stack);

  protected void validateSlotIndex(int slot) {
    if (slot < 0 || slot >= stacks.size())
      throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
  }

  public void onContentsChanged(int slot) {
    for (IInventoryListener listener : listeners) {
      listener.inventoryChanged(this, slot);
    }
  }

  @Override
  public void addListener(IInventoryListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public CompoundNBT serialize() {
    CompoundNBT result = new CompoundNBT();
    result.putInt("slots", size());
    for (int i = 0; i < size(); i++) {
      ItemStackEntry entry = stacks.get(i);
      if (!entry.isEmpty()) {
        result.put("" + i, entry.serialize());
      }
    }
    return result;
  }

  @Override
  public void deserialize(CompoundNBT result) {
    int size = result.getInt("slots");
    resize(size);
    for (int i = 0; i < size; i++) {
      ItemStackEntry entry = ItemStackEntry.deserialize(result.get("" + i));
      stacks.set(i, entry);
    }
  }
}
