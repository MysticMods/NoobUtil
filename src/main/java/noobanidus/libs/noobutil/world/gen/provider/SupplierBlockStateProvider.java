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
import noobanidus.libs.noobutil.types.LazyStateSupplier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class SupplierBlockStateProvider extends BlockStateProvider {
  public static Codec<SupplierBlockStateProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("key").forGetter(o -> o.key),
        LazyStateSupplier.PropertyPair.CODEC.listOf().fieldOf("properties").forGetter(o -> o.properties)).apply(instance, SupplierBlockStateProvider::new));

  public static BlockStateProviderType<?> type = null;

  protected final ResourceLocation key;
  protected BlockState state = null;

  protected final List<LazyStateSupplier.PropertyPair> properties = new ArrayList<>();

  public SupplierBlockStateProvider(String namespace, String path) {
    this(new ResourceLocation(namespace, path));
  }

  public SupplierBlockStateProvider(String namespace, String path, @Nullable List<LazyStateSupplier.PropertyPair> pairs) {
    this(new ResourceLocation(namespace, path), pairs);
  }

  public SupplierBlockStateProvider(ResourceLocation key) {
    this(key, null);
  }

  public SupplierBlockStateProvider(ResourceLocation key, @Nullable List<LazyStateSupplier.PropertyPair> pairs) {
    this.key = key;
    if (pairs != null) {
      this.properties.addAll(pairs);
    }
  }

  @Override
  protected BlockStateProviderType<?> getProviderType() {
    if (type == null) {
      throw new IllegalArgumentException("SupplierBlockStateProvider hasn't been properly registered");
    }

    return type;
  }

  @Override
  public BlockState getBlockState(Random randomIn, BlockPos blockPosIn) {
    if (state == null) {
      Block block = ForgeRegistries.BLOCKS.getValue(key);
      if (block == null) {
        NoobUtil.logger.error("Block couldn't be located for key: " + key);
        state = Blocks.AIR.getDefaultState();
      } else {
        state = apply(block.getDefaultState());
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

  public SupplierBlockStateProvider addPair(String name, boolean value) {
    this.properties.add(new LazyStateSupplier.PropertyPair(name, value ? "true" : "false", "boolean"));
    return this;
  }

  public SupplierBlockStateProvider addPair(String name, int value) {
    this.properties.add(new LazyStateSupplier.PropertyPair(name, String.valueOf(value), "integer"));
    return this;
  }
}
