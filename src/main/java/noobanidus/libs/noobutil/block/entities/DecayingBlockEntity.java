package noobanidus.libs.noobutil.block.entities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import noobanidus.libs.noobutil.reference.NBTConstants;
import noobanidus.libs.noobutil.type.LazyStateSupplier;

public class DecayingBlockEntity extends BlockEntity implements TickableBlockEntity {
  private int decay;
  private LazyStateSupplier state;

  public DecayingBlockEntity(BlockEntityType<?> type) {
    this(type, new LazyStateSupplier(Blocks.AIR.defaultBlockState()), 35);
  }

  public DecayingBlockEntity(BlockEntityType<?> type, LazyStateSupplier block, int decay) {
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
  public void load(BlockState state, CompoundTag tag) {
    this.decay = tag.getInt(NBTConstants.DecayingBlockEntity.Decay);
    this.state = LazyStateSupplier.fromNBT(tag.getCompound(NBTConstants.DecayingBlockEntity.State));
    super.load(state, tag);
  }

  @Override
  public CompoundTag save(CompoundTag pCompound) {
    CompoundTag result = super.save(pCompound);
    result.putInt(NBTConstants.DecayingBlockEntity.Decay, this.decay);
    result.put(NBTConstants.DecayingBlockEntity.State, this.state.serializeNBT());
    return result;
  }
}
