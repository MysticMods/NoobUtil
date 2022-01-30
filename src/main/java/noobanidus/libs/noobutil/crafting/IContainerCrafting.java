package noobanidus.libs.noobutil.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.NonNullList;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.List;

public interface IContainerCrafting<H extends ILargeInventory, C extends AbstractContainerMenu & IPartitionedPlayerContainer, T extends BlockEntity & IReferentialBlockEntity> extends ICrafting<H, T> {

  C getContainer();

  T getBlockEntity();

  Player getPlayer();

  default Inventory getPlayerInventory() {
    return getPlayer().inventory;
  }

  @Override
  H getHandler();

  List<Slot> getCombinedIngredientSlots();

  default NonNullList<ItemStack> getCombinedItems() {
    NonNullList<ItemStack> result = NonNullList.of(ItemStack.EMPTY);
    for (Slot slot : getCombinedIngredientSlots()) {
      if (slot.hasItem()) {
        result.add(slot.getItem());
      }
    }
    return result;
  }
}
