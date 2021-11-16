package noobanidus.libs.noobutil.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class TagBuilder {
  protected abstract static class Builder<T, V> {
    protected final RegistrateTagsProvider<T> provider;

    public Builder(RegistrateTagsProvider<T> provider) {
      this.provider = provider;
    }

    public abstract Builder<T, V> add(Tags.IOptionalNamedTag<T> tag, NonNullSupplier<? extends V>... items);

    public abstract Builder<T, V> add(Tags.IOptionalNamedTag<T> tag, V... items);

    public Builder<T, V> add(Tags.IOptionalNamedTag<T> tag, Tags.IOptionalNamedTag<T>... tags) {
      provider.tag(tag).addTags(tags);
      return this;
    }
  }

  public static class BlocksBuilder extends Builder<Block, Block> {
    public BlocksBuilder(RegistrateTagsProvider<Block> provider) {
      super(provider);
    }

    @Override
    public Builder<Block, Block> add(Tags.IOptionalNamedTag<Block> tag, NonNullSupplier<? extends Block>... items) {
      provider.tag(tag).add(Stream.of(items).map(NonNullSupplier::get).toArray(Block[]::new));
      return this;
    }

    @Override
    public Builder<Block, Block> add(Tags.IOptionalNamedTag<Block> tag, Block... items) {
      provider.tag(tag).add(items);
      return this;
    }
  }

  public static class ItemsBuilder extends Builder<Item, IItemProvider> {
    public ItemsBuilder(RegistrateTagsProvider<Item> provider) {
      super(provider);
    }

    @Override
    public Builder<Item, IItemProvider> add(Tags.IOptionalNamedTag<Item> tag, NonNullSupplier<? extends IItemProvider>... items) {
      provider.tag(tag).add(Stream.of(items).map(Supplier::get).map(IItemProvider::asItem).toArray(Item[]::new));
      return this;
    }

    @Override
    public Builder<Item, IItemProvider> add(Tags.IOptionalNamedTag<Item> tag, IItemProvider... items) {
      provider.tag(tag).add(Stream.of(items).map(IItemProvider::asItem).toArray(Item[]::new));
      return this;
    }
  }
}
