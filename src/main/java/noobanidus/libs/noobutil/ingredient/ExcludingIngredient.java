package noobanidus.libs.noobutil.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import noobanidus.libs.noobutil.reference.JsonConstants;
import noobanidus.libs.noobutil.reference.ModData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Derived from Mekanism's ExcludingIngredient: https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/recipe/ingredient/ExcludingIngredient.java
// MIT licensed.
import net.minecraft.world.item.crafting.Ingredient.Value;

public class ExcludingIngredient extends Ingredient {

  //Helper methods for the two most common types we may be creating
  public static ExcludingIngredient create(TagKey<Item> base, ItemLike without) {
    return new ExcludingIngredient(Ingredient.of(base), Ingredient.of(without));
  }

  public static ExcludingIngredient create(TagKey<Item> base, TagKey<Item> without) {
    return new ExcludingIngredient(Ingredient.of(base), Ingredient.of(without));
  }

  private final ExcludingItemList excludingItemList;

  private ExcludingIngredient(Ingredient base, Ingredient without) {
    this(new ExcludingItemList(base, without));
  }

  private ExcludingIngredient(ExcludingItemList excludingItemList) {
    super(Stream.of(excludingItemList));
    this.excludingItemList = excludingItemList;
  }

  @Override
  public boolean test(@Nullable ItemStack stack) {
    if (stack == null || stack.isEmpty()) {
      return false;
    }
    return excludingItemList.base.test(stack) && !excludingItemList.without.test(stack);
  }

  @Override
  public boolean isEmpty() {
    return getItems().length == 0;
  }

  @Override
  public boolean isSimple() {
    return excludingItemList.base.isSimple() && excludingItemList.without.isSimple();
  }

  @Override
  public IIngredientSerializer<ExcludingIngredient> getSerializer() {
    return Serializer.INSTANCE;
  }

  private static class ExcludingItemList implements Value {

    private final Ingredient base;
    private final Ingredient without;

    public ExcludingItemList(Ingredient base, Ingredient without) {
      this.base = base;
      this.without = without;
    }

    @Override
    public Collection<ItemStack> getItems() {
      return Arrays.stream(base.getItems())
          .filter(stack -> !without.test(stack))
          .collect(Collectors.toList());
    }

    @Override
    public JsonObject serialize() {
      JsonObject json = new JsonObject();
      json.addProperty(JsonConstants.Type, ModData.getResourceLocation("excluding_ingredient").toString());
      json.add(JsonConstants.Base, base.toJson());
      json.add(JsonConstants.Without, without.toJson());
      return json;
    }
  }

  public static class Serializer implements IIngredientSerializer<ExcludingIngredient> {

    public static final IIngredientSerializer<ExcludingIngredient> INSTANCE = new Serializer();

    private Serializer() {
    }

    @Override
    public ExcludingIngredient parse(@Nonnull JsonObject json) {
      if (json.has(JsonConstants.Base) && json.has(JsonConstants.Without)) {
        return new ExcludingIngredient(Ingredient.fromJson(json.getAsJsonObject(JsonConstants.Base)),
            Ingredient.fromJson(json.getAsJsonObject(JsonConstants.Without)));
      }
      throw new JsonParseException("A without ingredient must have both a base ingredient and a negation ingredient.");
    }

    @Override
    public ExcludingIngredient parse(@Nonnull FriendlyByteBuf buffer) {
      Ingredient base = Ingredient.fromNetwork(buffer);
      Ingredient without = Ingredient.fromNetwork(buffer);
      return new ExcludingIngredient(base, without);
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf buffer, ExcludingIngredient ingredient) {
      CraftingHelper.write(buffer, ingredient.excludingItemList.base);
      CraftingHelper.write(buffer, ingredient.excludingItemList.without);
    }
  }
}
