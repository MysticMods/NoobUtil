package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.item.BaseItems;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: Transition these to DataGenerators

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

  public ResourceLocation getId(ITag.INamedTag<Item> tag) {
    return TagCollectionManager.getInstance().getItems().getId(tag);
  }

  public <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> storage(Supplier<RegistryEntry<Block>> block, Supplier<RegistryEntry<Item>> ingot, ITag.INamedTag<Item> blockTag, ITag.INamedTag<Item> ingotTag, @Nullable ITag.INamedTag<Item> oreTag, @Nullable Supplier<RegistryEntry<Item>> nugget, @Nullable ITag.INamedTag<Item> nuggetTag, @Nullable ITag.INamedTag<Item> dustTag) {
    return (ctx, p) -> {

      // Ingot to block
      ShapedRecipeBuilder.shaped(block.get().get())
          .pattern("###")
          .pattern("###")
          .pattern("###")
          .define('#', ingotTag)
          .unlockedBy("has_at_least_9_" + safeName(getId(ingotTag)), p.hasItem(ingotTag))
          .save(p, rl(safeName(getId(ingotTag)) + "_to_storage_block"));
      // Block to ingot
      ShapelessRecipeBuilder.shapeless(ingot.get().get(), 9)
          .requires(blockTag)
          .unlockedBy("has_block_" + safeName(getId(blockTag)), p.hasItem(blockTag))
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
            .unlockedBy("has_at_least_9_" + safeName(getId(nuggetTag)), p.hasItem(nuggetTag))
            .save(p, rl(safeName(getId(nuggetTag)) + "_to_ingot"));
        ShapelessRecipeBuilder.shapeless(Objects.requireNonNull(nugget).get().get(), 9)
            .requires(ingotTag)
            .unlockedBy("has_ingot_" + safeName(getId(ingotTag)), p.hasItem(ingotTag))
            .save(p, rl(safeName(getId(ingotTag)) + "_to_9_nuggets"));
      }
      if (dustTag != null) {
        dust(dustTag, ingot.get(), 0.125f, p);
      }
    };
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void ore(ITag.INamedTag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, rl(safeId(result.get()) + "_from_smelting"));
    CookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, rl(safeId(result.get()) + "_from_blasting"));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void dust(ITag.INamedTag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, rl(safeId(result.get()) + "_from_smelting_dust"));
    CookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, rl(safeId(result.get()) + "_from_blasting_dust"));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer, safeId(result.get()) + "_from_smelting");
    CookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, String namespace, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_smelting_" + safeName(source.get())));
    CookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_blasting_" + safeName(source.get())));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(ITag.INamedTag<Item> tag, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(tag), result.get(), xp, 200)
        .unlockedBy("has_" + safeName(result.get().getRegistryName()), RegistrateRecipeProvider.hasItem(result.get()))
        .save(consumer, safeId(result.get()) + "_from_smelting");
    CookingRecipeBuilder.blasting(Ingredient.of(tag), result.get(), xp, 100)
        .unlockedBy("has_" + safeName(result.get().getRegistryName()), RegistrateRecipeProvider.hasItem(result.get()))
        .save(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200).unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).save(consumer);
    CookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, IRecipeSerializer.SMOKING_RECIPE).unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).save(consumer, safeId(result.get()) + "_from_smoker");
    CookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 600, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).save(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void food(ITag.INamedTag<Item> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 200).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer);
    CookingRecipeBuilder.cooking(Ingredient.of(source), result.get(), xp, 100, IRecipeSerializer.SMOKING_RECIPE).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, safeId(result.get()) + "_from_smoker");
    CookingRecipeBuilder.cooking(Ingredient.of(source), result.get(), xp, 600, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).save(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void smelting(Supplier<? extends T> source, Supplier<? extends T> result, float xp, boolean blast, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 200).unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).save(consumer, safeId(result.get()) + "_from_smelting");
    if (blast) {
      CookingRecipeBuilder.blasting(Ingredient.of(source.get()), result.get(), xp, 100).unlockedBy("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).save(consumer, safeId(result.get()) + "_from_blasting");
    }
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(output.get()).pattern("###").pattern("###").pattern("###").define('#', input.get()).unlockedBy("has_at_least_9_" + safeName(input.get()), RegistrateRecipeProvider.hasItem(input.get())).save(consumer);
    ShapelessRecipeBuilder.shapeless(input.get(), 9).requires(output.get()).unlockedBy("has_" + safeName(output.get()), RegistrateRecipeProvider.hasItem(output.get())).save(consumer, safeId(input.get()) + "_from_" + safeName(output.get()));
  }

  public Item getModElement(ITag.INamedTag<Item> input) {
    Item last = Items.AIR;
    for (Item item : input.getValues()) {
      last = item;
      if (Objects.requireNonNull(item.getRegistryName()).getNamespace().equals(modid)) {
        return item;
      }
    }
    return last;
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void storage(ITag.INamedTag<Item> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(output.get())
        .pattern("###")
        .pattern("###")
        .pattern("###").define('#', input).unlockedBy("has_at_least_9_" + safeName(getId(input)), RegistrateRecipeProvider.hasItem(input)).save(consumer);
    ShapelessRecipeBuilder.shapeless(getModElement(input), 9).requires(output.get()).unlockedBy("has_" + safeName(output.get()), RegistrateRecipeProvider.hasItem(output.get())).save(consumer, new ResourceLocation(modid, safeName(getId(input)) + "_from_" + safeName(output.get())));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
    return ShapelessRecipeBuilder.shapeless(result.get(), amount).requires(source.get(), required).unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()));
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

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
    singleItemUnfinished(source, result, required, amount).save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
    singleItemUnfinished(source, result, required, amount).save(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
    singleItemUnfinished(source, result, 1, 4)
        .group("planks")
        .save(consumer);
  }

  // TWO BY TWO: Items

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, int count, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), count)
        .pattern("XX")
        .pattern("XX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    twoByTwo(source, result, group, 4, consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 4)
        .pattern("X  ").pattern("XX ").pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
          .unlocks("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlocks("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void narrowPost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 4)
        .pattern("X")
        .pattern("X")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlocks("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void widePost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("X")
        .pattern("X")
        .pattern("X")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
          .unlocks("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 3)
        .pattern("W#W").pattern("W#W")
        .define('W', source.get())
        .define('#', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get())
        .pattern("#W#").pattern("#W#")
        .define('W', source.get())
        .define('#', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void wall(Supplier<? extends T> source, Supplier<? extends T> result, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 6)
        .pattern("XXX").pattern("XXX")
        .define('X', source.get())
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
          .unlocks("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .save(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 3)
        .pattern("XX").pattern("XX").pattern("XX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 2)
        .pattern("XXX").pattern("XXX")
        .define('X', source.get())
        .group(group)
        .unlockedBy("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void axe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern("XS ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void pickaxe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void shovel(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void sword(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("X")
        .pattern("S")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void knife(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern(" X")
        .pattern("S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void knife(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern(" X")
        .pattern("S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer, safeName(result.get()) + "_from_" + safeName(getId(material)));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void hoe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void boots(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void spear(Supplier<? extends Item> sword, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', sword.get())
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(sword.get()), RegistrateRecipeProvider.hasItem(sword.get()))
        .save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void spear(Item sword, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', sword)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(sword), RegistrateRecipeProvider.hasItem(sword))
        .save(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void legs(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void chest(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("XXX")
        .pattern("XXX")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void helmet(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .define('X', material.get())
        .group(group)
        .unlockedBy("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void axe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern("XS ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void pickaxe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void shovel(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("S")
        .pattern("S")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void sword(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X")
        .pattern("X")
        .pattern("S")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void hoe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XX ")
        .pattern(" S ")
        .pattern(" S ")
        .define('X', material)
        .define('S', Tags.Items.RODS_WOODEN)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void boots(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void legs(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void chest(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("X X")
        .pattern("XXX")
        .pattern("XXX")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void helmet(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(result.get(), 1)
        .pattern("XXX")
        .pattern("X X")
        .define('X', material)
        .group(group)
        .unlockedBy("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .save(consumer);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, IItemProvider ingredient) {
    return cordial(cordial, () -> ingredient);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends IItemProvider> ingredient) {
    return cordial(cordial, ingredient, 4);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends IItemProvider> ingredient, int amount) {
    return (ctx, p) ->
        ShapedRecipeBuilder.shaped(cordial.get().get(), amount)
            .pattern("1S1")
            .pattern("BWB")
            .pattern("BSB")
            .define('1', ingredient.get())
            .define('S', Items.SUGAR)
            .define('B', Items.GLASS_BOTTLE)
            .define('W', Items.WATER_BUCKET)
            .unlockedBy("has_first", RegistrateRecipeProvider.hasItem(ingredient.get()))
            .unlockedBy("has_sugar", RegistrateRecipeProvider.hasItem(Items.SUGAR))
            .save(p);
  }
}

