package noobanidus.libs.noobutil.recipe;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.crafting.ContainerCrafting;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.List;

public interface ILargeRecipe<H extends LargeItemHandler, C extends AbstractContainerMenu & IPartitionedPlayerContainer, T extends BlockEntity & IReferentialBlockEntity, W extends ContainerCrafting<H, C, T>> extends Recipe<W> {
  List<Processor<W>> getProcessors ();

  void addProcessor (Processor<W> processor);

  @Override
  default boolean isSpecial() {
    return true;
  }
}
