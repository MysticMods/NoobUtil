package noobanidus.libs.noobutil.types;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackStore extends LazySupplier<ItemStack> {
  public ItemStackStore(Supplier<Item> itemCreator, int itemQuantity) {
    super(() -> new ItemStack(itemCreator.get(), itemQuantity));
  }
}
