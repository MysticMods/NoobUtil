package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RecipeSerializerBuilder<T extends RecipeSerializer<?>, P> extends AbstractBuilder<RecipeSerializer<?>, T, P, RecipeSerializerBuilder<T, P>> {
  private final Supplier<? extends T> factory;

  public RecipeSerializerBuilder(CustomRegistrate owner, P parent, String name, BuilderCallback callback, Supplier<? extends T> factory) {
    super(owner, parent, name, callback, Registry.RECIPE_SERIALIZER_REGISTRY);
    this.factory = factory;
  }

  @Override
  @Nonnull
  protected T createEntry() {
    return factory.get();
  }
}
