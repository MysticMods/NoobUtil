package noobanidus.libs.noobutil.container;

import net.minecraft.world.inventory.Slot;

import java.util.List;

public interface IPartitionedPlayerContainer extends IPlayerContainer {
  List<Slot> getIngredientSlots();
  List<Slot> getCombinedIngredientSlots();
}
