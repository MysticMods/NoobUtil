package noobanidus.libs.noobutil.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import noobanidus.libs.noobutil.type.ItemStackEntry;

import javax.annotation.Nonnull;

public class LargeItemHandler extends AbstractLargeItemHandler {
  public LargeItemHandler() {
  }

  public LargeItemHandler(int size) {
    super(size);
  }

  public LargeItemHandler(NonNullList<ItemStackEntry> stacks) {
    super(stacks);
  }

  @Override
  public int getSlotLimit(int slot) {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return true;
  }
}
