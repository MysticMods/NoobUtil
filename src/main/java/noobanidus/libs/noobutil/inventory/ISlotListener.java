package noobanidus.libs.noobutil.inventory;

import net.minecraft.world.Container;

@FunctionalInterface
public interface ISlotListener {
  void slotChanged (Container inventory, int slot);
}
