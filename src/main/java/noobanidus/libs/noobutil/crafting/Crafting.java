package noobanidus.libs.noobutil.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.inventory.IInvWrapper;

import javax.annotation.Nullable;

public abstract class Crafting<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity> extends IInvWrapper<H> implements ICrafting<H, T> {
  protected final T blockentity;
  protected final PlayerEntity player;

  public Crafting(T blockentity, H handler, @Nullable PlayerEntity player) {
    super(handler);
    this.blockentity = blockentity;
    this.player = player;
  }

  @Override
  public T getBlockEntity() {
    return blockentity;
  }

  @Override
  @Nullable
  public PlayerEntity getPlayer() {
    return player;
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
