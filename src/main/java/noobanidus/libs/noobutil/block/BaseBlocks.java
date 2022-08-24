package noobanidus.libs.noobutil.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BaseBlocks {
  public static class CropsBlock extends net.minecraft.world.level.block.CropBlock {
    public CropsBlock(Properties builder) {
      super(builder);
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext selectionContext) {
      return Block.box(0, 0, 0, 16.0D, 2.0D * (state.getValue(AGE) + 1), 16.0D);
    }
  }

  public static class SeededCropsBlock extends CropsBlock {
    private final Supplier<Supplier<? extends ItemLike>> seedProvider;

    public SeededCropsBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
      super(builder);
      this.seedProvider = seedProvider;
    }

    @Override
    protected ItemLike getBaseSeedId() {
      return seedProvider.get().get();
    }
  }

  public static class WildCropBlock extends BushBlock {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final TagKey<Block> supporter;

    public WildCropBlock(BlockBehaviour.Properties builder, TagKey<Block> tag) {
      super(builder);
      this.supporter = tag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
      return state.is(this.supporter);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
      return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return this.mayPlaceOn(worldIn.getBlockState(pos.below()), worldIn, pos.below());
    }
  }

  public static class DoorBlock extends net.minecraft.world.level.block.DoorBlock {
    public DoorBlock(Properties builder) {
      super(builder);
    }
  }

  public static class OreBlock extends DropExperienceBlock {
    private final int minXP;
    private final int maxXP;

    public OreBlock(Properties props, UniformInt xp) {
      super(props, xp);
      minXP = xp.getMinValue();
      maxXP = xp.getMaxValue();
    }

    public OreBlock(Properties props, int minXP, int maxXP) {
      super(props, UniformInt.of(minXP, maxXP));
      this.minXP = minXP;
      this.maxXP = maxXP;
    }
  }

  public static class PressurePlateBlock extends net.minecraft.world.level.block.PressurePlateBlock {
    public PressurePlateBlock(Sensitivity sensitivityIn, Properties propertiesIn) {
      super(sensitivityIn, propertiesIn);
    }
  }

  public static class SaplingBlock extends net.minecraft.world.level.block.SaplingBlock {
    public SaplingBlock(AbstractTreeGrower tree, Properties proeprties) {
      super(tree, proeprties);
    }
  }

  public static class StoneButtonBlock extends net.minecraft.world.level.block.StoneButtonBlock {
    public StoneButtonBlock(Properties properties) {
      super(properties);
    }
  }

  public static class TrapDoorBlock extends net.minecraft.world.level.block.TrapDoorBlock {
    public TrapDoorBlock(Properties properties) {
      super(properties);
    }
  }

  public static class WeightedPressurePlateBlock extends net.minecraft.world.level.block.WeightedPressurePlateBlock {
    public WeightedPressurePlateBlock(int weight, Properties properties) {
      super(weight, properties);
    }
  }

  public static class WoodButtonBlock extends net.minecraft.world.level.block.WoodButtonBlock {
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
    }
  }

  public static class HorizontalBlock extends net.minecraft.world.level.block.HorizontalDirectionalBlock {
    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected HorizontalBlock(Properties props) {
      super(props);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
      super.createBlockStateDefinition(pBuilder);

      pBuilder.add(FACING);
    }
  }
}
