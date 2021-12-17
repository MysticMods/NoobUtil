package noobanidus.libs.noobutil.recipe.builder;

import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import noobanidus.libs.noobutil.recipe.UniqueShapelessRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class UniqueShapelessRecipeBuilder {
  private final Item result;
  private final int count;
  private final List<Ingredient> ingredients = Lists.newArrayList();
  private final Advancement.Builder advancement = Advancement.Builder.advancement();
  private String group;

  public UniqueShapelessRecipeBuilder(IItemProvider pResult, int pCount) {
    this.result = pResult.asItem();
    this.count = pCount;
  }

  public static UniqueShapelessRecipeBuilder shapeless(IItemProvider pResult) {
    return new UniqueShapelessRecipeBuilder(pResult, 1);
  }

  public static UniqueShapelessRecipeBuilder shapeless(IItemProvider pResult, int pCount) {
    return new UniqueShapelessRecipeBuilder(pResult, pCount);
  }

  public UniqueShapelessRecipeBuilder requires(ITag<Item> pTag) {
    return this.requires(Ingredient.of(pTag));
  }

  public UniqueShapelessRecipeBuilder requires(IItemProvider pItem) {
    return this.requires(pItem, 1);
  }

  public UniqueShapelessRecipeBuilder requires(IItemProvider pItem, int pQuantity) {
    for (int i = 0; i < pQuantity; ++i) {
      this.requires(Ingredient.of(pItem));
    }

    return this;
  }

  public UniqueShapelessRecipeBuilder requires(Ingredient pIngredient) {
    return this.requires(pIngredient, 1);
  }

  public UniqueShapelessRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
    for (int i = 0; i < pQuantity; ++i) {
      this.ingredients.add(pIngredient);
    }

    return this;
  }

  public UniqueShapelessRecipeBuilder unlockedBy(String p_200483_1_, ICriterionInstance p_200483_2_) {
    this.advancement.addCriterion(p_200483_1_, p_200483_2_);
    return this;
  }

  public UniqueShapelessRecipeBuilder group(String p_200490_1_) {
    this.group = p_200490_1_;
    return this;
  }

  public void save(Consumer<IFinishedRecipe> p_200482_1_) {
    this.save(p_200482_1_, Registry.ITEM.getKey(this.result));
  }

  public void save(Consumer<IFinishedRecipe> p_200484_1_, String p_200484_2_) {
    ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
    if ((new ResourceLocation(p_200484_2_)).equals(resourcelocation)) {
      throw new IllegalStateException("Unique Shapeless Recipe " + p_200484_2_ + " should remove its 'save' argument");
    } else {
      this.save(p_200484_1_, new ResourceLocation(p_200484_2_));
    }
  }

  public void save(Consumer<IFinishedRecipe> p_200485_1_, ResourceLocation p_200485_2_) {
    this.ensureValid(p_200485_2_);
    this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_200485_2_)).rewards(AdvancementRewards.Builder.recipe(p_200485_2_)).requirements(IRequirementsStrategy.OR);
    p_200485_1_.accept(new Result(p_200485_2_, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(p_200485_2_.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + p_200485_2_.getPath())));
  }

  private void ensureValid(ResourceLocation pId) {
    if (this.advancement.getCriteria().isEmpty()) {
      throw new IllegalStateException("No way of obtaining recipe " + pId);
    }
  }

  public static class Result extends ShapelessRecipeBuilder.Result {
    public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<Ingredient> pIngredients, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
      super(pId, pResult, pCount, pGroup, pIngredients, pAdvancement, pAdvancementId);
    }

    @Override
    public IRecipeSerializer<?> getType() {
      if (UniqueShapelessRecipe.getStoredSerializer() == null) {
        throw new NullPointerException("UniqueShapelessRecipe serializer has not been registered");
      }
      return UniqueShapelessRecipe.getStoredSerializer();
    }
  }
}
