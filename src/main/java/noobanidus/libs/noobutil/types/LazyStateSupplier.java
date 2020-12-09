package noobanidus.libs.noobutil.types;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class LazyStateSupplier extends LazySupplier<BlockState> {
  public static Codec<LazyStateSupplier> CODEC = ResourceLocation.CODEC.xmap(LazyStateSupplier::new, o -> o.location);

  private final ResourceLocation location;

  public LazyStateSupplier(ResourceLocation location) {
    super(() -> {
      Block block = ForgeRegistries.BLOCKS.getValue(location);
      if (block != null) {
        return block.getDefaultState();
      }
      return Blocks.AIR.getDefaultState();
    });
    this.location = location;
  }

  public LazyStateSupplier(String namespace, String identifier) {
    this(new ResourceLocation(namespace, identifier));
  }
}
