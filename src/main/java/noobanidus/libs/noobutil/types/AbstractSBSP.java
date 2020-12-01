package noobanidus.libs.noobutil.types;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.NoobUtil;

import java.util.Random;

public abstract class AbstractSBSP extends BlockStateProvider {
  // public static final Codec<SupplierBlockStateProvider> CODEC = ResourceLocation.CODEC.fieldOf("key").xmap(SupplierBlockStateProvider::new, (provider) -> provider.key).codec();

  protected final ResourceLocation key;
  protected BlockState state = null;

  public AbstractSBSP(String namespace, String path) {
    this(new ResourceLocation(namespace, path));
  }

  public AbstractSBSP(ResourceLocation key) {
    this.key = key;
  }

  @Override
  protected abstract BlockStateProviderType<?> getProviderType();

  @Override
  public BlockState getBlockState(Random randomIn, BlockPos blockPosIn) {
    if (state == null) {
      Block block = ForgeRegistries.BLOCKS.getValue(key);
      if (block == null) {
        NoobUtil.logger.error("Block couldn't be located for key: " + key);
        state = Blocks.AIR.getDefaultState();
      } else {
        state = block.getDefaultState();
      }
    }

    return state;
  }
}
