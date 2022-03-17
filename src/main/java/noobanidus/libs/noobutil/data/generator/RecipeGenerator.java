/*
package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.item.BaseItems;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: Transition these to DataGenerators

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;

@SuppressWarnings({"Duplicates", "WeakerAccess", "unused"})
public class RecipeGenerator {
  private String modid;

  public RecipeGenerator(String modid) {
    this.modid = modid;
  }

  public ResourceLocation rl(String comp) {
    if (comp.contains(":")) {
      return new ResourceLocation(modid, comp.split(":")[1]);
    }
    return new ResourceLocation(modid, comp);
  }

  public ResourceLocation getId(Tag.Named<Item> tag) {
    return SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getId(tag);
  }

  public <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> storage(Supplier<RegistryEntry<Block>> block, Supplier<RegistryEntry<Item>> ingot, Tag.Named<Item> blockTag, Tag.Named<Item> ingotTag, @Nullable Tag.Named<Item> oreTag, @Nullable Supplier<RegistryEntry<Item>> nugget, @Nullable Tag.Named<Item> nuggetTag, @Nullable Tag.Named<Item> dustTag) {
    return (ctx, p) -> {

      // Ingot to block
      ShapedRecipeBuilder.shaped(block.get().get())
          .pattern("###")
          .pattern("###")
          .pattern("###")
          .define('#', ingotTag)
          .unlockedBy("has_at_least_9_" + safeName(getId(ingotTag)), DataIngredient.tag(ingotTag).getCritereon(p))
          .save(p, rl(safeName(getId(ingotTag)) + "_to_storage_block"));
      // Block to ingot
      ShapelessRecipeBuilder.shapeless(ingot.get().get(), 9)
          .requires(blockTag)
          .unlockedBy("has_block_" + safeName(getId(blockTag)), DataIngredient.tag(blockTag).getCritereon(p))
          .save(p, rl(safeName(getId(blockTag)) + "_to_9_ingots"));
      if (oreTag != null) {
        // Ore smelting
        ore(oreTag, ingot.get(), 0.125f, p);
      }
      if (nuggetTag != null) {
        ShapedRecipeBuilder.shaped(ingot.get().get())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', nuggetTag)
            .unlockedBy("has_at_least_9_" + safeName(getId(nuggetTag)), DataIngredient.tag(nuggetTag).getCritereon(p))
            .save(p, rl(safeName(getId(nuggetTag)) + "_to_ingot"));
        ShapelessRecipeBuilder.shapeless(Objects.requireNonNull(nugget).get().get(), 9)
            .requires(ingotTag)
            .unlockedBy("has_ingot_" + safeName(getId(ingotTag)), DataIngredient.tag(ingotTag).getCritereon(p))
            .save(p, rl(safeName(getId(ingotTag)) + "_to_9_nuggets"));
      }
      if (dustTag != null) {
        dust(dustTag, ingot.get(), 0.125f, p);
      }
    };
  }

  protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
    return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
  }

  protected static InventoryChangeTrigger.TriggerInstance has(Tag<Item> pTag) {
    return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
  }

  protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
    return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void ore(Tag.Named<Item> source, Supplier<T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, rl(safeId(result.get()) + "_from_smelting"));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, rl(safeId(result.get()) + "_from_blasting"));
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void dust(Tag.Named<Item> source, Supplier<T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, rl(safeId(result.get()) + "_from_smelting_dust"));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, rl(safeId(result.get()) + "_from_blasting_dust"));
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
        .save(consumer, safeId(result.get()) + "_from_smelting");
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
        .save(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, String namespace, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()),has(source.get()))
        .save(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_smelting_" + safeName(source.get())));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get()))
        .save(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_blasting_" + safeName(source.get())));
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void recycle(Tag.Named<Item> tag, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(tag), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(result.get().getRegistryName()), has(result.get()))
        .save(consumer, safeId(result.get()) + "_from_smelting");
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(tag), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(result.get().getRegistryName()), has(result.get()))
        .save(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200).unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get())).save(consumer);
    SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get())).save(consumer, safeId(result.get()) + "_from_smoker");
    SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get())).save(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void food(Tag.Named<Item> source, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer);
    SimpleCookingRecipeBuilder.cooking(Ingredient.of(source), result.get(), xp, 100, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, safeId(result.get()) + "_from_smoker");
    SimpleCookingRecipeBuilder.cooking(Ingredient.of(source), result.get(), xp, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_" + safeName(getId(source)), has(source)).save(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void smelting(Supplier<? extends T> source, Supplier<? extends T> result, float xp, boolean blast, Consumer<FinishedRecipe> consumer) {
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200).unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get())).save(consumer, safeId(result.get()) + "_from_smelting");
    if (blast) {
      SimpleCookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100).unlockedBy("has_" + safeName(source.get().getRegistryName()), has(source.get())).save(consumer, safeId(result.get()) + "_from_blasting");
    }
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(output.get()).pattern("###").pattern("###").pattern("###").define('#', input.get()).unlockedBy("has_at_least_9_" + safeName(input.get()), has(input.get())).save(consumer);
    ShapelessRecipeBuilder.shapeless(input.get(), 9).requires(output.get()).unlockedBy("has_" + safeName(output.get()), has(output.get())).save(consumer, safeId(input.get()) + "_from_" + safeName(output.get()));
  }

  public Item getModElement(Tag.Named<Item> input) {
    Item last = Items.AIR;
    for (Item item : input.getValues()) {
      last = item;
      if (Objects.requireNonNull(item.getRegistryName()).getNamespace().equals(modid)) {
        return item;
      }
    }
    return last;
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void storage(Tag.Named<Item> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(output.get())
        .pattern("###")
        .pattern("###")
        .pattern("###").define('#', input).unlockedBy("has_at_least_9_" + safeName(getId(input)), has(input)).save(consumer);
    ShapelessRecipeBuilder.shapeless(getModElement(input), 9).requires(output.get()).unlockedBy("has_" + safeName(output.get()), has(output.get())).save(consumer, new ResourceLocation(modid, safeName(getId(input)) + "_from_" + safeName(output.get())));
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
    return ShapelessRecipeBuilder.shapeless(result.get(), amount).requires(source.get(), required).unlockedBy("has_" + safeName(source.get()), has(source.get()));
  }

  public ResourceLocation safeId(ResourceLocation id) {
    return new ResourceLocation(id.getNamespace(), safeName(id));
  }

  public ResourceLocation safeId(IForgeRegistryEntry<?> registryEntry) {
    return safeId(registryEntry.getRegistryName());
  }

  public String safeName(ResourceLocation nameSource) {
    return nameSource.getPath().replace('/', '_');
  }

  public String safeName(IForgeRegistryEntry<?> registryEntry) {
    return safeName(registryEntry.getRegistryName());
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
    singleItemUnfinished(source, result, required, amount).save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
    singleItemUnfinished(source, result, required, amount).save(consumer);
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
    singleItemUnfinished(source, result, 1, 4)
        .group("planks")
        .save(consumer);
  }

  // TWO BY TWO: Items

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, int count, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), count)
        .pattern("XX")
        .pattern("XX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    twoByTwo(source, result, group, 4, consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 4)
        .pattern("X  ").pattern("XX ").pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
          .unlockedBy("has_" + safeName(source.get()), has(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlockedBy("has_" + safeName(source.get()), has(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void narrowPost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 4)
        .pattern("X")
        .pattern("X")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlockedBy("has_" + safeName(source.get()), has(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void widePost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("X")
        .pattern("X")
        .pattern("X")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlockedBy("has_" + safeName(source.get()), has(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 3)
        .pattern("W#W").pattern("W#W")
        .define('W', source.get())
        .define('#', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get())
        .pattern("#W#").pattern("#W#")
        .define('W', source.get())
        .define('#', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
  }

  public <T extends ItemLike & IForgeRegistryEntry<?>> void wall(Supplier<? extends T> source, Supplier<? extends T> result, boolean stone, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("XXX").pattern("XXX")
        .define('X', source.get())
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
          .unlockedBy("has_" + safeName(source.get()), has(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 3)
        .pattern("XX").pattern("XX").pattern("XX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 2)
        .pattern("XXX").pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), has(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void axe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern("XS ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void pickaxe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void shovel(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void sword(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("X")
        .pattern("S")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void knife(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern(" X")
        .pattern("S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void knife(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern(" X")
        .pattern("S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer, safeName(result.get()) + "_from_" + safeName(getId(material)));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void hoe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void boots(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void spear(Supplier<? extends Item> sword, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', sword.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(sword.get()), has(sword.get()))
        .save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void spear(Item sword, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', sword)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(sword), has(sword))
        .save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void legs(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void chest(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("XXX")
        .pattern("XXX")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void helmet(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), has(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void axe(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern("XS ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void pickaxe(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void shovel(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void sword(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("X")
        .pattern("S")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void hoe(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void boots(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void legs(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void chest(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("XXX")
        .pattern("XXX")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends ItemLike & IForgeRegistryEntry<?>> void helmet(Tag.Named<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), has(material))
        .save(consumer);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, ItemLike ingredient) {
    return cordial(cordial, () -> ingredient);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends ItemLike> ingredient) {
    return cordial(cordial, ingredient, 4);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends ItemLike> ingredient, int amount) {
    return (ctx, p) ->
        ShapedRecipeBuilder.shaped(cordial.get().get(), amount)
            .pattern("1S1")
            .pattern("BWB")
            .pattern("BSB")
            .define('1', ingredient.get())
            .define('S', Items.SUGAR)
            .define('B', Items.GLASS_BOTTLE)
            .define('W', Items.WATER_BUCKET)
            .unlockedBy("has_first", has(ingredient.get()))
            .unlockedBy("has_sugar", has(Items.SUGAR))
            .save(p);
  }
}

*/
