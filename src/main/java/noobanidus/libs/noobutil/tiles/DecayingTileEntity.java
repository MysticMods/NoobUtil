package noobanidus.libs.noobutil.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

public class DecayingTileEntity extends TileEntity implements ITickableTileEntity {
  private int decay;
  private LazyStateSupplier state;

  public DecayingTileEntity(TileEntityType<?> type) {
    this(type, new LazyStateSupplier(Blocks.AIR.defaultBlockState()), 35);
  }

  public DecayingTileEntity(TileEntityType<?> type, LazyStateSupplier block, int decay) {
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
  public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
    this.decay = p_230337_2_.getInt("decay");
    this.state = LazyStateSupplier.fromNBT(p_230337_2_.getCompound("state"));
    super.load(p_230337_1_, p_230337_2_);
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    CompoundNBT result = super.save(pCompound);
    result.putInt("decay", this.decay);
    result.put("state", this.state.serializeNBT());
    return result;
  }
}
