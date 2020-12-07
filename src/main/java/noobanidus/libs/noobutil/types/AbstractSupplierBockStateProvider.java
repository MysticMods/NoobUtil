package noobanidus.libs.noobutil.types;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.NoobUtil;

import java.util.Random;
import java.util.function.Function;

public abstract class AbstractSupplierBockStateProvider extends BlockStateProvider {
  public static <T extends AbstractSupplierBockStateProvider> Codec<T> codecBuilder (Function<ResourceLocation, T> builder) {
    return ResourceLocation.CODEC.fieldOf("key").xmap(builder, (provider) -> provider.key).codec();
  }

  protected final ResourceLocation key;
  protected BlockState state = null;

  public AbstractSupplierBockStateProvider(String namespace, String path) {
    this(new ResourceLocation(namespace, path));
  }

  public AbstractSupplierBockStateProvider(ResourceLocation key) {
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
