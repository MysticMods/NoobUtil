package noobanidus.libs.noobutil.ingredient;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import noobanidus.libs.noobutil.type.LazySupplier;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LazyIngredient extends Ingredient {
  private final LazySupplier<Ingredient> ingredient;

  public LazyIngredient(Supplier<Ingredient> ingredient) {
    super(Stream.empty());
    this.ingredient = new LazySupplier<>(ingredient);
  }

  @Override
  public ItemStack[] getItems() {
    return ingredient.get().getItems();
  }

  @Override
  public boolean test(@Nullable ItemStack p_test_1_) {
    return ingredient.get().test(p_test_1_);
  }

  @Override
  public IntList getStackingIds() {
    return ingredient.get().getStackingIds();
  }

  @Override
  public JsonElement toJson() {
    return ingredient.get().toJson();
  }

  @Override
  public boolean isEmpty() {
    return ingredient.get().isEmpty();
  }

  @Override
  public boolean isSimple() {
    return ingredient.get().isSimple();
  }

  @Override
  public IIngredientSerializer<? extends Ingredient> getSerializer() {
    return ingredient.get().getSerializer();
  }
}
