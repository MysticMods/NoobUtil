package noobanidus.libs.noobutil.recipe;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.List;

public class UniqueShapelessRecipe extends ShapelessRecipe {
  private static RecipeSerializer<?> storedSerializer = null;

  public UniqueShapelessRecipe(ResourceLocation pId, String pGroup, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
    super(pId, pGroup, pResult, pIngredients);
  }

  public static RecipeSerializer<?> getStoredSerializer() {
    return storedSerializer;
  }

  public static void setStoredSerializer(RecipeSerializer<?> storedSerializer) {
    UniqueShapelessRecipe.storedSerializer = storedSerializer;
  }

  public RecipeSerializer<?> getSerializer() {
    if (getStoredSerializer() == null) {
      throw new NullPointerException("serializer has not been registered for UniqueShapelessRecipe");
    }
    return getStoredSerializer();
  }

  public boolean matches(CraftingContainer pInv, Level pLevel) {
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
