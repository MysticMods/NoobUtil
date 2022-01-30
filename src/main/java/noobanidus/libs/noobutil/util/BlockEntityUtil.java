package noobanidus.libs.noobutil.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;

public class BlockEntityUtil {
  public static <T extends BlockEntity> void updateViaState(T tile) {
    Level world = tile.getLevel();
    if (world == null || world.isClientSide()) {
      return;
    }

    BlockState state = world.getBlockState(tile.getBlockPos());
    world.sendBlockUpdated(tile.getBlockPos(), state, state, 8);
  }
}
