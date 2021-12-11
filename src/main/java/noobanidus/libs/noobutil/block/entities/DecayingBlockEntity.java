package noobanidus.libs.noobutil.block.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.reference.NBTIdentifiers;
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
    this.decay = tag.getInt(NBTIdentifiers.DecayingBlockEntity.decay);
    this.state = LazyStateSupplier.fromNBT(tag.getCompound(NBTIdentifiers.DecayingBlockEntity.state));
    super.load(state, tag);
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    CompoundNBT result = super.save(pCompound);
    result.putInt(NBTIdentifiers.DecayingBlockEntity.decay, this.decay);
    result.put(NBTIdentifiers.DecayingBlockEntity.state, this.state.serializeNBT());
    return result;
  }
}
