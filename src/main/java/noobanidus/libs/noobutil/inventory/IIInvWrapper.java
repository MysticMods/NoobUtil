package noobanidus.libs.noobutil.inventory;

import net.minecraftforge.items.IItemHandler;

public interface IIInvWrapper<T extends IItemHandler> {
  T getHandler();
}
