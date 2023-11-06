/*
package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

public class CustomRegistrate extends AbstractRegistrate<CustomRegistrate> {
  protected CustomRegistrate(String modid) {
    super(modid);
  }

  public static CustomRegistrate create(String modid) {
    return new CustomRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
  }

  // Serializers

  public <T extends RecipeSerializer<?>> RecipeSerializerBuilder<T, CustomRegistrate> recipeSerializer(Supplier<? extends T> factory) {
    return recipeSerializer(this, factory);
  }

  public <T extends RecipeSerializer<?>> RecipeSerializerBuilder<T, CustomRegistrate> recipeSerializer(String name, Supplier<? extends T> factory) {
    return recipeSerializer(this, name, factory);
  }

  public <T extends RecipeSerializer<?>, P> RecipeSerializerBuilder<T, P> recipeSerializer(P parent, Supplier<? extends T> factory) {
    return recipeSerializer(parent, currentName(), factory);
  }

  public <T extends RecipeSerializer<?>, P> RecipeSerializerBuilder<T, P> recipeSerializer(P parent, String name, Supplier<? extends T> factory) {
    return entry(name, callback -> new RecipeSerializerBuilder<>(this, parent, name, callback, factory));
  }
}
*/
