package noobanidus.libs.noobutil.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;

public class IInvWrapper<T extends IItemHandler> implements Container, IIInvWrapper<T> {
  private final T handler;

  public IInvWrapper(T handler) {
    this.handler = handler;
  }

  @Override
  public T getHandler() {
    return handler;
  }

  @Override
  public int getContainerSize() {
    return handler.getSlots();
  }

  @Override
  public boolean isEmpty() {
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack inSlot = handler.getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getItem(int index) {
    return handler.getStackInSlot(index);
  }

  @Override
  public ItemStack removeItem(int index, int count) {
    return handler.extractItem(index, count, false);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return handler.extractItem(index, handler.getSlotLimit(index), false);
  }

  @Override
  public void setItem(int index, ItemStack stack) {
    handler.extractItem(index, handler.getSlotLimit(index), false);
    handler.insertItem(index, stack, false);
  }

  @Override
  public void setChanged() {
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public void clearContent() {
    for (int i = 0; i < handler.getSlots(); i++) {
      handler.extractItem(i, handler.getSlotLimit(i), false);
    }
  }
}
