package noobanidus.libs.noobutil.ingredient;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import noobanidus.libs.noobutil.NoobUtil;
import noobanidus.libs.noobutil.reference.NBTConstants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IngredientStack {
  public static final IngredientStack EMPTY = new IngredientStack(Ingredient.EMPTY, 0, null);

  private final Ingredient ingredient;
  // TODO: Consider finality of this field
  private int count;
  private final CompoundNBT nbt;

  public IngredientStack(ItemStack stack) {
    this(stack.getItem(), stack.getCount(), stack.getTag());
  }

  public IngredientStack(IItemProvider item) {
    this(item, 1);
  }

  public IngredientStack(IItemProvider item, int count) {
    this(item, count, null);
  }

  public IngredientStack(IItemProvider item, int count, CompoundNBT nbt) {
    this.ingredient = Ingredient.of(item.asItem());
    this.count = count;
    this.nbt = nbt;
  }

  public IngredientStack(Ingredient ingredient) {
    this(ingredient, 1);
  }

  public IngredientStack(Ingredient ingredient, int count) {
    this(ingredient, count, null);
  }

  public IngredientStack(Ingredient ingredient, int count, CompoundNBT nbt) {
    this.ingredient = ingredient;
    this.count = count;
    this.nbt = nbt;
  }

  public IngredientStack(ITag<Item> tag) {
    this(tag, 1);
  }

  public IngredientStack(ITag<Item> tag, int count) {
    this(tag, count, null);
  }

  public IngredientStack(ITag<Item> tag, int count, CompoundNBT nbt) {
    this.ingredient = Ingredient.of(tag);
    this.count = count;
    this.nbt = nbt;
  }


  public ItemStack[] getMatchingStacks() {
    return ingredient.getItems();
  }

  public ItemStack getFirstStack () {
    ItemStack[] stacks = getMatchingStacks();
    if (stacks.length > 0) {
      return stacks[0];
    } else {
      return ItemStack.EMPTY;
    }
  }

  public boolean apply(@Nullable ItemStack p_apply_1_) {
    boolean res = ingredient.test(p_apply_1_);
    if (nbt != null && p_apply_1_ != null) {
      return res && nbt.equals(p_apply_1_.getTag());
    }

    return res;
  }

  public IntList getValidItemStacksPacked() {
    return ingredient.getStackingIds();
  }

  public boolean isSimple() {
    return ingredient.isSimple();
  }

  public int getCount() {
    return count;
  }

  public void shrink(int amount) {
    this.count -= amount;
  }

  public void grow(int amount) {
    this.count += amount;
  }

  public void shrink() {
    shrink(1);
  }

  public void grow() {
    grow(1);
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public boolean isEmpty() {
    return this == EMPTY;
  }

  @Nullable
  public CompoundNBT getNBT() {
    return nbt;
  }

  public List<ItemStack> getMatchingStacksWithSizes() {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack stack : getMatchingStacks()) {
      ItemStack copy = stack.copy();
      copy.setCount(getCount());
      result.add(copy);
    }
    return result;
  }

  public JsonObject serialize () {
    JsonObject result = new JsonObject();
    result.add(NBTConstants.Ingredient, ingredient.toJson());
    result.addProperty(NBTConstants.Count, this.count);
    CompoundNBT.CODEC.encodeStart(JsonOps.INSTANCE, this.nbt).resultOrPartial(NoobUtil.logger::error).ifPresent((i) -> result.add(NBTConstants.NBT, i));
    return result;
  }

  public static IngredientStack deserialize (@Nullable JsonObject object) {
    if (object == null || object.isJsonNull()) {
      // TODO: Null or empty?
      return EMPTY;
    }

    Ingredient ing = Ingredient.fromJson(object.getAsJsonObject(NBTConstants.Ingredient));
    int count = object.get(NBTConstants.Count).getAsInt();
    CompoundNBT tag = object.has(NBTConstants.NBT) ? CompoundNBT.CODEC.parse(JsonOps.INSTANCE, object.getAsJsonObject(NBTConstants.NBT)).resultOrPartial(NoobUtil.logger::error).orElse(null) : null;
    return new IngredientStack(ing, count, tag);
  }

  public void write (PacketBuffer buffer) {
    buffer.writeVarInt(count);
    ingredient.toNetwork(buffer);
  }

  public static IngredientStack read (PacketBuffer buffer) {
    int count = buffer.readVarInt();
    Ingredient ingredient = Ingredient.fromNetwork(buffer);
    return new IngredientStack(ingredient, count);
  }

  public CountableIngredientStack countable () {
    return new CountableIngredientStack(this);
  }
}
