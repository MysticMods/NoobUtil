package noobanidus.libs.noobutil.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ResolvingRecipeType<C extends Container, T extends Recipe<C>> extends SimpleJsonResourceReloadListener {
  protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  protected final Supplier<RecipeType<T>> type;
  protected List<T> cache = null;
  protected final Comparator<? super T> comparator;
  protected final Object2IntOpenHashMap<ResourceLocation> reverseLookup = new Object2IntOpenHashMap<>();

  public ResolvingRecipeType(Supplier<RecipeType<T>> type, Comparator<? super T> comparator) {
    super(GSON, "recipes");
    this.type = type;
    this.comparator = comparator;
  }

  protected abstract List<T> getRecipesList();

  public List<T> getRecipes() {
    if (cache == null) {
      cache = getRecipesList(); // ArcaneArchivesAPI.getInstance().getRecipeManager().getAllRecipesFor(type.get());
      cache.sort(comparator);
      reverseLookup.clear();
      for (int i = 0; i < cache.size(); i++) {
        reverseLookup.put(cache.get(i).getId(), i);
      }
    }

    return cache;
  }

  @Nullable
  public T getRecipe(ResourceLocation location) {
    int index = lookup(location);
    if (index == -1) {
      return null;
    }
    return getRecipe(index);
  }

  public int size() {
    return getRecipes().size();
  }

  public T getRecipe(int index) {
    if (index < 0 || index >= getRecipes().size()) {
      throw new RuntimeException("Index " + index + " not in valid range for recipe type " + type + " [0," + getRecipes().size() + ")");
    }

    return getRecipes().get(index);
  }

  public boolean hasRecipe(int index) {
    return index < getRecipes().size();
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    this.cache = null;
  }

  public int lookup(ResourceLocation recipeId) {
    getRecipes();
    return reverseLookup.getOrDefault(recipeId, -1);
  }
}
