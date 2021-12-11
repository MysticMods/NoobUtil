package noobanidus.libs.noobutil.recipe;

import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.List;

public interface ILargeRecipe<H extends LargeItemHandler, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IReferentialBlockEntity, W extends Crafting<H, C, T>> extends IRecipe<W> {
  List<Processor<W>> getProcessors ();

  void addProcessor (Processor<W> processor);

  @Override
  default boolean isSpecial() {
    return true;
  }
}
