package noobanidus.libs.noobutil.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.IInvWrapper;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import java.util.List;

public abstract class ContainerCrafting<H extends ILargeInventory, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IReferentialBlockEntity> extends IInvWrapper<H> implements IContainerCrafting<H, C, T> {
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
  public PlayerEntity getPlayer() {
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
