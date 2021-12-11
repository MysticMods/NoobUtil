package noobanidus.libs.noobutil.world.gen.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.NoobUtil;
import noobanidus.libs.noobutil.type.LazyStateSupplier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public abstract class AbstractSupplierBlockStateProvider extends BlockStateProvider {
  public static <T extends AbstractSupplierBlockStateProvider> Codec<T> codecBuilder(BiFunction<ResourceLocation, List<LazyStateSupplier.PropertyPair>, T> builder) {
    return RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("key").forGetter(o -> o.key),
        LazyStateSupplier.PropertyPair.CODEC.listOf().fieldOf("properties").forGetter(o -> o.properties)).apply(instance, builder));
  }

  protected final ResourceLocation key;
  protected BlockState state = null;

  protected final List<LazyStateSupplier.PropertyPair> properties = new ArrayList<>();

  public AbstractSupplierBlockStateProvider(String namespace, String path) {
    this(new ResourceLocation(namespace, path));
  }

  public AbstractSupplierBlockStateProvider(String namespace, String path, @Nullable List<LazyStateSupplier.PropertyPair> pairs) {
    this(new ResourceLocation(namespace, path), pairs);
  }

  public AbstractSupplierBlockStateProvider(ResourceLocation key) {
    this(key, null);
  }

  public AbstractSupplierBlockStateProvider(ResourceLocation key, @Nullable List<LazyStateSupplier.PropertyPair> pairs) {
    this.key = key;
    if (pairs != null) {
      this.properties.addAll(pairs);
    }
  }

  @Override
  protected abstract BlockStateProviderType<?> type();

  @Override
  public BlockState getState(Random randomIn, BlockPos blockPosIn) {
    if (state == null) {
      Block block = ForgeRegistries.BLOCKS.getValue(key);
      if (block == null) {
        NoobUtil.logger.error("Block couldn't be located for key: " + key);
        state = Blocks.AIR.defaultBlockState();
      } else {
        state = apply(block.defaultBlockState());
      }
    }

    return state;
  }

  public BlockState apply (BlockState state) {
    for (LazyStateSupplier.PropertyPair pair : properties) {
      state = pair.apply(state);
    }
    return state;
  }

  public AbstractSupplierBlockStateProvider addPair(String name, boolean value) {
    this.properties.add(new LazyStateSupplier.PropertyPair(name, value ? "true" : "false", "boolean"));
    return this;
  }

  public AbstractSupplierBlockStateProvider addPair(String name, int value) {
    this.properties.add(new LazyStateSupplier.PropertyPair(name, String.valueOf(value), "integer"));
    return this;
  }
}
