package noobanidus.libs.noobutil.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.List;

public class UniqueShapelessRecipe extends ShapelessRecipe {
  private static IRecipeSerializer<?> storedSerializer = null;

  public UniqueShapelessRecipe(ResourceLocation pId, String pGroup, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
    super(pId, pGroup, pResult, pIngredients);
  }

  public static IRecipeSerializer<?> getStoredSerializer() {
    return storedSerializer;
  }

  public static void setStoredSerializer(IRecipeSerializer<?> storedSerializer) {
    UniqueShapelessRecipe.storedSerializer = storedSerializer;
  }

  public IRecipeSerializer<?> getSerializer() {
    if (getStoredSerializer() == null) {
      throw new NullPointerException("serializer has not been registered for UniqueShapelessRecipe");
    }
    return getStoredSerializer();
  }

  public boolean matches(CraftingInventory pInv, World pLevel) {
    List<ItemStack> inputs = new java.util.ArrayList<>();
    int count = 0;

    for (int j = 0; j < pInv.getContainerSize(); ++j) {
      ItemStack itemstack = pInv.getItem(j);
      if (!itemstack.isEmpty()) {
        for (ItemStack existingInput : inputs) {
          if (ItemUtil.equalWithoutSize(existingInput, itemstack)) {
            return false;
          }
        }
        ++count;
        inputs.add(itemstack);
      }
    }

    return count == this.getIngredients().size() && RecipeMatcher.findMatches(inputs, this.getIngredients()) != null;
  }
}
