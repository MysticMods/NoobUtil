package epicsquid.mysticalworld.init;

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
    return TagCollectionManager.getManager().getItemTags().getDirectIdFromTag(tag);
  }

  public <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> storage(Supplier<RegistryEntry<Block>> block, Supplier<RegistryEntry<Item>> ingot, ITag.INamedTag<Item> blockTag, ITag.INamedTag<Item> ingotTag, @Nullable ITag.INamedTag<Item> oreTag, @Nullable Supplier<RegistryEntry<Item>> nugget, @Nullable ITag.INamedTag<Item> nuggetTag, @Nullable ITag.INamedTag<Item> dustTag) {
    return (ctx, p) -> {

      // Ingot to block
      ShapedRecipeBuilder.shapedRecipe(block.get().get())
          .patternLine("###")
          .patternLine("###")
          .patternLine("###")
          .key('#', ingotTag)
          .addCriterion("has_at_least_9_" + safeName(getId(ingotTag)), p.hasItem(ingotTag))
          .build(p, rl(safeName(getId(ingotTag)) + "_to_storage_block"));
      // Block to ingot
      ShapelessRecipeBuilder.shapelessRecipe(ingot.get().get(), 9)
          .addIngredient(blockTag)
          .addCriterion("has_block_" + safeName(getId(blockTag)), p.hasItem(blockTag))
          .build(p, rl(safeName(getId(blockTag)) + "_to_9_ingots"));
      if (oreTag != null) {
        // Ore smelting
        ore(oreTag, ingot.get(), 0.125f, p);
      }
      if (nuggetTag != null) {
        ShapedRecipeBuilder.shapedRecipe(ingot.get().get())
            .patternLine("###")
            .patternLine("###")
            .patternLine("###")
            .key('#', nuggetTag)
            .addCriterion("has_at_least_9_" + safeName(getId(nuggetTag)), p.hasItem(nuggetTag))
            .build(p, rl(safeName(getId(nuggetTag)) + "_to_ingot"));
        ShapelessRecipeBuilder.shapelessRecipe(Objects.requireNonNull(nugget).get().get(), 9)
            .addIngredient(ingotTag)
            .addCriterion("has_ingot_" + safeName(getId(ingotTag)), p.hasItem(ingotTag))
            .build(p, rl(safeName(getId(ingotTag)) + "_to_9_nuggets"));
      }
      if (dustTag != null) {
        dust(dustTag, ingot.get(), 0.125f, p);
      }
    };
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void ore(ITag.INamedTag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(source), result.get(), xp, 200).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, rl(safeId(result.get()) + "_from_smelting"));
    CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(source), result.get(), xp, 100).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, rl(safeId(result.get()) + "_from_blasting"));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void dust(ITag.INamedTag<Item> source, Supplier<T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(source), result.get(), xp, 200).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, rl(safeId(result.get()) + "_from_smelting_dust"));
    CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(source), result.get(), xp, 100).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, rl(safeId(result.get()) + "_from_blasting_dust"));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 200)
        .addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer, safeId(result.get()) + "_from_smelting");
    CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100)
        .addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(Supplier<? extends T> source, Supplier<? extends T> result, float xp, String namespace, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 200)
        .addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_smelting_" + safeName(source.get())));
    CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100)
        .addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer, new ResourceLocation(namespace, safeName(result.get()) + "_from_blasting_" + safeName(source.get())));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void recycle(ITag.INamedTag<Item> tag, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(tag), result.get(), xp, 200)
        .addCriterion("has_" + safeName(result.get().getRegistryName()), RegistrateRecipeProvider.hasItem(result.get()))
        .build(consumer, safeId(result.get()) + "_from_smelting");
    CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(tag), result.get(), xp, 100)
        .addCriterion("has_" + safeName(result.get().getRegistryName()), RegistrateRecipeProvider.hasItem(result.get()))
        .build(consumer, safeId(result.get()) + "_from_blasting");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 200).addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).build(consumer);
    CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100, IRecipeSerializer.SMOKING).addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).build(consumer, safeId(result.get()) + "_from_smoker");
    CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 600, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).build(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void food(ITag.INamedTag<Item> source, Supplier<? extends T> result, float xp, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(source), result.get(), xp, 200).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer);
    CookingRecipeBuilder.cookingRecipe(Ingredient.fromTag(source), result.get(), xp, 100, IRecipeSerializer.SMOKING).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, safeId(result.get()) + "_from_smoker");
    CookingRecipeBuilder.cookingRecipe(Ingredient.fromTag(source), result.get(), xp, 600, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion("has_" + safeName(getId(source)), RegistrateRecipeProvider.hasItem(source)).build(consumer, safeId(result.get()) + "_from_campfire");
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void smelting(Supplier<? extends T> source, Supplier<? extends T> result, float xp, boolean blast, Consumer<IFinishedRecipe> consumer) {
    CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 200).addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).build(consumer, safeId(result.get()) + "_from_smelting");
    if (blast) {
      CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(source.get()), result.get(), xp, 100).addCriterion("has_" + safeName(source.get().getRegistryName()), RegistrateRecipeProvider.hasItem(source.get())).build(consumer, safeId(result.get()) + "_from_blasting");
    }
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(output.get()).patternLine("###").patternLine("###").patternLine("###").key('#', input.get()).addCriterion("has_at_least_9_" + safeName(input.get()), RegistrateRecipeProvider.hasItem(input.get())).build(consumer);
    ShapelessRecipeBuilder.shapelessRecipe(input.get(), 9).addIngredient(output.get()).addCriterion("has_" + safeName(output.get()), RegistrateRecipeProvider.hasItem(output.get())).build(consumer, safeId(input.get()) + "_from_" + safeName(output.get()));
  }

  public Item getModElement(ITag.INamedTag<Item> input) {
    Item last = Items.AIR;
    for (Item item : input.getAllElements()) {
      last = item;
      if (Objects.requireNonNull(item.getRegistryName()).getNamespace().equals(modid)) {
        return item;
      }
    }
    return last;
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void storage(ITag.INamedTag<Item> input, Supplier<? extends T> output, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(output.get())
        .patternLine("###")
        .patternLine("###")
        .patternLine("###").key('#', input).addCriterion("has_at_least_9_" + safeName(getId(input)), RegistrateRecipeProvider.hasItem(input)).build(consumer);
    ShapelessRecipeBuilder.shapelessRecipe(getModElement(input), 9).addIngredient(output.get()).addCriterion("has_" + safeName(output.get()), RegistrateRecipeProvider.hasItem(output.get())).build(consumer, new ResourceLocation(modid, safeName(getId(input)) + "_from_" + safeName(output.get())));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
    return ShapelessRecipeBuilder.shapelessRecipe(result.get(), amount).addIngredient(source.get(), required).addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()));
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
    singleItemUnfinished(source, result, required, amount).build(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<IFinishedRecipe> consumer) {
    singleItemUnfinished(source, result, required, amount).build(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<IFinishedRecipe> consumer) {
    singleItemUnfinished(source, result, 1, 4)
        .setGroup("planks")
        .build(consumer);
  }

  // TWO BY TWO: Items

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, int count, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), count)
        .patternLine("XX")
        .patternLine("XX")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void twoByTwo(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    twoByTwo(source, result, group, 4, consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
        .patternLine("X  ").patternLine("XX ").patternLine("XXX")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get())
          .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
        .patternLine("XXX")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get(), 2)
          .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void narrowPost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
        .patternLine("X")
        .patternLine("X")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get(), 2)
          .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void widePost(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
        .patternLine("X")
        .patternLine("X")
        .patternLine("X")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get(), 2)
          .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
        .patternLine("W#W").patternLine("W#W")
        .key('W', source.get())
        .key('#', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get())
        .patternLine("#W#").patternLine("#W#")
        .key('W', source.get())
        .key('#', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
  }

  public <T extends IItemProvider & IForgeRegistryEntry<?>> void wall(Supplier<? extends T> source, Supplier<? extends T> result, boolean stone, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
        .patternLine("XXX").patternLine("XXX")
        .key('X', source.get())
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
    if (stone) {
      SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source.get()), result.get())
          .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
          .build(consumer, safeId(result.get()) + "_from_" + safeName(source.get()) + "_stonecutting");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
        .patternLine("XX").patternLine("XX").patternLine("XX")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 2)
        .patternLine("XXX").patternLine("XXX")
        .key('X', source.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(source.get()), RegistrateRecipeProvider.hasItem(source.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void axe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XX ")
        .patternLine("XS ")
        .patternLine(" S ")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void pickaxe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine(" S ")
        .patternLine(" S ")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void shovel(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("S")
        .patternLine("S")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void sword(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("X")
        .patternLine("S")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void knife(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine(" X")
        .patternLine("S ")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void knife(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine(" X")
        .patternLine("S ")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer, safeName(result.get()) + "_from_" + safeName(getId(material)));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void hoe(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XX ")
        .patternLine(" S ")
        .patternLine(" S ")
        .key('X', material.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void boots(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X X")
        .patternLine("X X")
        .key('X', material.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void spear(Supplier<? extends Item> sword, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("S")
        .patternLine("S")
        .key('X', sword.get())
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(sword.get()), RegistrateRecipeProvider.hasItem(sword.get()))
        .build(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void spear(Item sword, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("S")
        .patternLine("S")
        .key('X', sword)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(sword), RegistrateRecipeProvider.hasItem(sword))
        .build(consumer, new ResourceLocation(modid, Objects.requireNonNull(result.get().getRegistryName()).getPath()));
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void legs(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine("X X")
        .patternLine("X X")
        .key('X', material.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void chest(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X X")
        .patternLine("XXX")
        .patternLine("XXX")
        .key('X', material.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void helmet(Supplier<? extends T> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine("X X")
        .key('X', material.get())
        .setGroup(group)
        .addCriterion("has_" + safeName(material.get()), RegistrateRecipeProvider.hasItem(material.get()))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void axe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XX ")
        .patternLine("XS ")
        .patternLine(" S ")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void pickaxe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine(" S ")
        .patternLine(" S ")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void shovel(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("S")
        .patternLine("S")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void sword(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X")
        .patternLine("X")
        .patternLine("S")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void hoe(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XX ")
        .patternLine(" S ")
        .patternLine(" S ")
        .key('X', material)
        .key('S', Tags.Items.RODS_WOODEN)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void boots(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X X")
        .patternLine("X X")
        .key('X', material)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void legs(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine("X X")
        .patternLine("X X")
        .key('X', material)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void chest(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("X X")
        .patternLine("XXX")
        .patternLine("XXX")
        .key('X', material)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  @SuppressWarnings("ConstantConditions")
  public <T extends IItemProvider & IForgeRegistryEntry<?>> void helmet(ITag.INamedTag<Item> material, Supplier<? extends T> result, @Nullable String group, Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(result.get(), 1)
        .patternLine("XXX")
        .patternLine("X X")
        .key('X', material)
        .setGroup(group)
        .addCriterion("has_" + safeName(getId(material)), RegistrateRecipeProvider.hasItem(material))
        .build(consumer);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, IItemProvider ingredient) {
    return cordial(cordial, () -> ingredient);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends IItemProvider> ingredient) {
    return cordial(cordial, ingredient, 4);
  }

  public <T extends BaseItems.DrinkItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> cordial(Supplier<RegistryEntry<T>> cordial, Supplier<? extends IItemProvider> ingredient, int amount) {
    return (ctx, p) ->
        ShapedRecipeBuilder.shapedRecipe(cordial.get().get(), amount)
            .patternLine("1S1")
            .patternLine("BWB")
            .patternLine("BSB")
            .key('1', ingredient.get())
            .key('S', Items.SUGAR)
            .key('B', Items.GLASS_BOTTLE)
            .key('W', Items.WATER_BUCKET)
            .addCriterion("has_first", RegistrateRecipeProvider.hasItem(ingredient.get()))
            .addCriterion("has_sugar", RegistrateRecipeProvider.hasItem(Items.SUGAR))
            .build(p);
  }
}

