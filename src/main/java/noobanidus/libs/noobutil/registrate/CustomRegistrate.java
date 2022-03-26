package noobanidus.libs.noobutil.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.material.Material;
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

  public <T extends MobEffect> EffectBuilder<T, CustomRegistrate> effect(Supplier<T> factory) {
    return effect(this, factory);
  }

  public <T extends MobEffect> EffectBuilder<T, CustomRegistrate> effect(String name, Supplier<T> factory) {
    return effect(this, name, factory);
  }

  public <T extends MobEffect, P> EffectBuilder<T, P> effect(P parent, Supplier<T> factory) {
    return effect(parent, currentName(), factory);
  }

  public <T extends MobEffect, P> EffectBuilder<T, P> effect(P parent, String name, Supplier<T> factory) {
    return entry(name, callback -> EffectBuilder.create(this, parent, name, callback, factory));
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

  // Containers

  public <T extends AbstractContainerMenu> ContainerBuilder<T, CustomRegistrate> containerType(String name, MenuType.MenuSupplier<T> factory) {
    return containerType(this, name, factory);
  }

  public <T extends AbstractContainerMenu> ContainerBuilder<T, CustomRegistrate> containerType(MenuType.MenuSupplier<T> factory) {
    return containerType(this, factory);
  }

  public <T extends AbstractContainerMenu, P> ContainerBuilder<T, P> containerType(P parent, MenuType.MenuSupplier<T> factory) {
    return containerType(parent, currentName(), factory);
  }

  public <T extends AbstractContainerMenu, P> ContainerBuilder<T, P> containerType(P parent, String name, MenuType.MenuSupplier<T> factory) {
    return entry(name, callback -> new ContainerBuilder<>(this, parent, name, callback, factory));
  }

  public SoundEventBuilder<SoundEvent, CustomRegistrate> soundEvent() {
    return soundEvent(currentName());
  }

  public SoundEventBuilder<SoundEvent, CustomRegistrate> soundEvent(String name) {
    ResourceLocation rl = new ResourceLocation(this.getModid(), name);
    Supplier<SoundEvent> factory = () -> new SoundEvent(rl); // TODO ???
    return soundEvent(this, name, factory);
  }

  public SoundEventBuilder<SoundEvent, CustomRegistrate> soundEvent(String name, String fullName) {
    ResourceLocation fullNameRL = new ResourceLocation(this.getModid(), fullName);
    Supplier<SoundEvent> factory = () -> new SoundEvent(fullNameRL);
    return soundEvent(this, name, factory);
  }

  public <P> SoundEventBuilder<SoundEvent, P> soundEvent(P parent, String name, Supplier<SoundEvent> factory) {
    return entry(name, callback -> new SoundEventBuilder<>(this, parent, name, callback, factory));
  }

  public BlockBuilder<RotatedPillarBlock, CustomRegistrate> log(String name/*, MaterialColor color*/) {
    return this.log(this.self(), name, RotatedPillarBlock::new, Material.WOOD/*, color*/);
  }

  public <T extends RotatedPillarBlock> BlockBuilder<T, CustomRegistrate> log(CustomRegistrate parent, String name, NonNullFunction<Block.Properties, T> factory, Material material/*, MaterialColor color*/) {
    return this.entry(name, (callback) -> BlockBuilder.create(this, parent, name, callback, factory, material));
  }
}
