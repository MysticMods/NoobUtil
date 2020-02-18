package noobanidus.libs.noobutil.util;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileUtil {
  public static <T extends TileEntity> void updateViaState (T tile) {
    World world = tile.getWorld();
    if (world == null || world.isRemote()) {
      return;
    }

    BlockState state = world.getBlockState(tile.getPos());
    world.notifyBlockUpdate(tile.getPos(), state, state, 8);
  }
}
