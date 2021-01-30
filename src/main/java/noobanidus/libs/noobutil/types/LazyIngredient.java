package noobanidus.libs.noobutil.types;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

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
  public ItemStack[] getMatchingStacks() {
    return ingredient.get().getMatchingStacks();
  }

  @Override
  public boolean test(@Nullable ItemStack p_test_1_) {
    return ingredient.get().test(p_test_1_);
  }

  @Override
  public IntList getValidItemStacksPacked() {
    return ingredient.get().getValidItemStacksPacked();
  }

  @Override
  public JsonElement serialize() {
    return ingredient.get().serialize();
  }

  @Override
  public boolean hasNoMatchingItems() {
    return ingredient.get().hasNoMatchingItems();
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
