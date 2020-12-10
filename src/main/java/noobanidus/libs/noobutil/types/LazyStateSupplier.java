package noobanidus.libs.noobutil.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class LazyStateSupplier extends LazySupplier<BlockState> {
  public static Codec<LazyStateSupplier> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("location").forGetter(o -> o.location),
      BlockState.CODEC.optionalFieldOf("state").forGetter(o -> o.state)).apply(instance, (loc, state) -> state.map(LazyStateSupplier::new).orElseGet(() -> new LazyStateSupplier(loc))));

  private final ResourceLocation location;
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<BlockState> state;

  public LazyStateSupplier(ResourceLocation location) {
    super();
    this.supplier = () -> {
      Block block = ForgeRegistries.BLOCKS.getValue(location);
      if (block != null) {
        return block.getDefaultState();
      }
      return Blocks.AIR.getDefaultState();
    };
    this.location = location;
    this.state = Optional.empty();
  }

  public LazyStateSupplier(BlockState stateIn) {
    super();
    this.state = Optional.of(stateIn);
    this.supplier = () -> this.state.orElse(Blocks.AIR.getDefaultState());
    this.location = stateIn.getBlock().getRegistryName();
  }
}
