package noobanidus.libs.noobutil.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.IInvWrapper;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.List;

public abstract class ContainerCrafting<H extends ILargeInventory, C extends AbstractContainerMenu & IPartitionedPlayerContainer, T extends BlockEntity & IReferentialBlockEntity> extends IInvWrapper<H> implements IContainerCrafting<H, C, T> {
  private final C container;
  private final T blockentity;

  public ContainerCrafting(C container, T blockentity, H handler) {
    super(handler);
    this.container = container;
    this.blockentity = blockentity;
  }

  @Override
  public C getContainer() {
    return container;
  }

  @Override
  public T getBlockEntity() {
    return blockentity;
  }

  @Override
  public Player getPlayer() {
    return container.getPlayer();
  }

  @Override
  public List<Slot> getCombinedIngredientSlots() {
    return container.getCombinedIngredientSlots();
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
