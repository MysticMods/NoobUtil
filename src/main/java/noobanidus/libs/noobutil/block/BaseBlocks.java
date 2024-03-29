package noobanidus.libs.noobutil.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BaseBlocks {
  public static class CropsBlock extends net.minecraft.block.CropsBlock {
    public CropsBlock(Properties builder) {
      super(builder);
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
      return Block.box(0, 0, 0, 16.0D, 2.0D * (state.getValue(AGE) + 1), 16.0D);
    }
  }

  public static class SeededCropsBlock extends CropsBlock {
    private final Supplier<Supplier<? extends IItemProvider>> seedProvider;

    public SeededCropsBlock(Properties builder, Supplier<Supplier<? extends IItemProvider>> seedProvider) {
      super(builder);
      this.seedProvider = seedProvider;
    }

    @Override
    protected IItemProvider getBaseSeedId() {
      return seedProvider.get().get();
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
    protected int xpOnDrop(Random rand) {
      return MathHelper.nextInt(rand, minXP, maxXP);
    }
  }

  public static class PressurePlateBlock extends net.minecraft.block.PressurePlateBlock {
    public PressurePlateBlock(Sensitivity sensitivityIn, Properties propertiesIn) {
      super(sensitivityIn, propertiesIn);
    }
  }

  public static class SaplingBlock extends net.minecraft.block.SaplingBlock {
    public SaplingBlock(Tree tree, Properties proeprties) {
      super(tree, proeprties);
    }
  }

  public static class StoneButtonBlock extends net.minecraft.block.StoneButtonBlock {
    public StoneButtonBlock(Properties properties) {
      super(properties);
    }
  }

  public static class TrapDoorBlock extends net.minecraft.block.TrapDoorBlock {
    public TrapDoorBlock(Properties properties) {
      super(properties);
    }
  }

  public static class WeightedPressurePlateBlock extends net.minecraft.block.WeightedPressurePlateBlock {
    public WeightedPressurePlateBlock(int weight, Properties properties) {
      super(weight, properties);
    }
  }

  public static class WoodButtonBlock extends net.minecraft.block.WoodButtonBlock {
    public WoodButtonBlock(Properties properties) {
      super(properties);
    }
  }

  public static class NarrowPostBlock extends WaterloggedBlock {
    public static VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);

    public NarrowPostBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return SHAPE;
    }
  }

  public static class WidePostBlock extends WaterloggedBlock {
    public static VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

    public WidePostBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return SHAPE;
    }
  }

  public static class HorizontalBlock extends net.minecraft.block.HorizontalBlock {
    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected HorizontalBlock(Properties props) {
      super(props);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
      return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
      super.createBlockStateDefinition(pBuilder);

      pBuilder.add(FACING);
    }
  }
}
