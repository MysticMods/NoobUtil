package noobanidus.libs.noobutil.type;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class LazyItemStack extends LazySupplier<ItemStack> {
  public LazyItemStack (Supplier<Item> itemCreator, int itemQuantity) {
    super(() -> new ItemStack(itemCreator.get(), itemQuantity));
  }
}
