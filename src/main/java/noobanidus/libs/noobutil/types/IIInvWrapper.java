package noobanidus.libs.noobutil.types;

import net.minecraftforge.items.IItemHandler;

public interface IIInvWrapper<T extends IItemHandler> {
  T getHandler();
}
