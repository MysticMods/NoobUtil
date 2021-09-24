package noobanidus.libs.noobutil.util;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileUtil {
  public static <T extends TileEntity> void updateViaState(T tile) {
    World world = tile.getLevel();
    if (world == null || world.isClientSide()) {
      return;
    }

    BlockState state = world.getBlockState(tile.getBlockPos());
    world.sendBlockUpdated(tile.getBlockPos(), state, state, 8);
  }
}
