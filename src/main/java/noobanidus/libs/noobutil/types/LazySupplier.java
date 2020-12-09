package noobanidus.libs.noobutil.types;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.LazyValue;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;

import java.util.function.Supplier;

public class LazySupplier<T> extends LazyValue<T> implements Supplier<T> {
  public LazySupplier(Supplier<T> supplier) {
    super(supplier);
  }

  @Override
  public T get() {
    return getValue();
  }

  public static class LazyStateSupplier extends LazySupplier<BlockState> {
   public static final Codec<LazyStateSupplier> CODEC = BlockState.CODEC.fieldOf("state").xmap(LazyStateSupplier::new, LazyValue::getValue).codec();

    public LazyStateSupplier(Supplier<BlockState> supplier) {
      super(supplier);
    }

    private LazyStateSupplier(BlockState state) {
      super(() -> state);
    }
  }
}
