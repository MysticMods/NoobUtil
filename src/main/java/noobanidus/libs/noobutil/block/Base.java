package noobanidus.libs.noobutil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Random;

public class Base {
  public static class CropsBlock extends net.minecraft.block.CropsBlock {
    public CropsBlock(Properties builder) {
      super(builder);
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
      return Block.makeCuboidShape(0, 0, 0, 16.0D, 2.0D * (state.get(AGE) + 1), 16.0D);
    }
  }

  public static class DoorBlock extends net.minecraft.block.DoorBlock {
    public DoorBlock(Properties builder) {
      super(builder);
    }
  }

  public static class OreBlock extends net.minecraft.block.OreBlock {
    private final int minXP;
    private final int maxXP;

    public OreBlock(Properties props, int minXP, int maxXP) {
      super(props);
      this.minXP = minXP;
      this.maxXP = maxXP;
    }

    @Override
    protected int getExperience(Random rand) {
      return MathHelper.nextInt(rand, minXP, maxXP);
    }
  }

  private static class PressurePlateBlock extends net.minecraft.block.PressurePlateBlock {
    public PressurePlateBlock(Sensitivity sensitivityIn, Properties propertiesIn) {
      super(sensitivityIn, propertiesIn);
    }
  }

}
