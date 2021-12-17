package noobanidus.libs.noobutil.block.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.reference.NBTConstants;
import noobanidus.libs.noobutil.type.LazyStateSupplier;

public class DecayingBlockEntity extends TileEntity implements ITickableTileEntity {
  private int decay;
  private LazyStateSupplier state;

  public DecayingBlockEntity(TileEntityType<?> type) {
    this(type, new LazyStateSupplier(Blocks.AIR.defaultBlockState()), 35);
  }

  public DecayingBlockEntity(TileEntityType<?> type, LazyStateSupplier block, int decay) {
    super(type);
    this.decay = decay;
    this.state = block;
  }

  @Override
  public void tick() {
    if (level != null && !level.isClientSide && decay-- <= 0) {
      level.setBlockAndUpdate(worldPosition, state.get());
    }
  }

  @Override
  public void load(BlockState state, CompoundNBT tag) {
    this.decay = tag.getInt(NBTConstants.DecayingBlockEntity.Decay);
    this.state = LazyStateSupplier.fromNBT(tag.getCompound(NBTConstants.DecayingBlockEntity.State));
    super.load(state, tag);
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    CompoundNBT result = super.save(pCompound);
    result.putInt(NBTConstants.DecayingBlockEntity.Decay, this.decay);
    result.put(NBTConstants.DecayingBlockEntity.State, this.state.serializeNBT());
    return result;
  }
}
