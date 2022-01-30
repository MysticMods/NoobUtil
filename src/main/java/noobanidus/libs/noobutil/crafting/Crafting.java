package noobanidus.libs.noobutil.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.inventory.IInvWrapper;

import javax.annotation.Nullable;

public abstract class Crafting<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity> extends IInvWrapper<H> implements ICrafting<H, T> {
  protected final T blockentity;
  protected final Player player;

  public Crafting(T blockentity, H handler, @Nullable Player player) {
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
  public Player getPlayer() {
    return player;
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
