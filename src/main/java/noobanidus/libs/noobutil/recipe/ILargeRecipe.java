package noobanidus.libs.noobutil.recipe;

import com.aranaira.arcanearchives.api.blockentities.IIdentifiedBlockEntity;
import com.aranaira.arcanearchives.api.container.IPartitionedPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;

import java.util.List;

public interface ILargeRecipe<H extends ArcaneItemHandler, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IIdentifiedBlockEntity, W extends ArcaneCrafting<H, C, T>> extends IRecipe<W> {
  List<Processor<W>> getProcessors ();

  void addProcessor (Processor<W> processor);

  @Override
  default boolean isSpecial() {
    return true;
  }
}
