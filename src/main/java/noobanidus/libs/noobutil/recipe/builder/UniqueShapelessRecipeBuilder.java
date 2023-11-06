/*
package noobanidus.libs.noobutil.recipe.builder;

import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import noobanidus.libs.noobutil.recipe.UniqueShapelessRecipe;

import java.util.List;
import java.util.function.Consumer;

public class UniqueShapelessRecipeBuilder {
  private final Item result;
  private final int count;
  private final List<Ingredient> ingredients = Lists.newArrayList();
  private final Advancement.Builder advancement = Advancement.Builder.advancement();
  private String group;

  public UniqueShapelessRecipeBuilder(ItemLike pResult, int pCount) {
    this.result = pResult.asItem();
    this.count = pCount;
  }

  public static UniqueShapelessRecipeBuilder shapeless(ItemLike pResult) {
    return new UniqueShapelessRecipeBuilder(pResult, 1);
  }

  public static UniqueShapelessRecipeBuilder shapeless(ItemLike pResult, int pCount) {
    return new UniqueShapelessRecipeBuilder(pResult, pCount);
  }

  public UniqueShapelessRecipeBuilder requires(TagKey<Item> pTag) {
    return this.requires(Ingredient.of(pTag));
  }

  public UniqueShapelessRecipeBuilder requires(ItemLike pItem) {
    return this.requires(pItem, 1);
  }

  public UniqueShapelessRecipeBuilder requires(ItemLike pItem, int pQuantity) {
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

  public UniqueShapelessRecipeBuilder unlockedBy(String p_200483_1_, CriterionTriggerInstance p_200483_2_) {
    this.advancement.addCriterion(p_200483_1_, p_200483_2_);
    return this;
  }

  public UniqueShapelessRecipeBuilder group(String p_200490_1_) {
    this.group = p_200490_1_;
    return this;
  }

  public void save(Consumer<FinishedRecipe> p_200482_1_) {
    this.save(p_200482_1_, BuiltInRegistries.ITEM.getKey(this.result));
  }

  public void save(Consumer<FinishedRecipe> p_200484_1_, String p_200484_2_) {
    ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
    if ((new ResourceLocation(p_200484_2_)).equals(resourcelocation)) {
      throw new IllegalStateException("Unique Shapeless Recipe " + p_200484_2_ + " should remove its 'save' argument");
    } else {
      this.save(p_200484_1_, new ResourceLocation(p_200484_2_));
    }
  }

  public void save(Consumer<FinishedRecipe> p_200485_1_, ResourceLocation p_200485_2_) {
    this.ensureValid(p_200485_2_);
    this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_200485_2_)).rewards(AdvancementRewards.Builder.recipe(p_200485_2_)).requirements(RequirementsStrategy.OR);
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
    public RecipeSerializer<?> getType() {
      if (UniqueShapelessRecipe.getStoredSerializer() == null) {
        throw new NullPointerException("UniqueShapelessRecipe serializer has not been registered");
      }
      return UniqueShapelessRecipe.getStoredSerializer();
    }
  }
}
*/
