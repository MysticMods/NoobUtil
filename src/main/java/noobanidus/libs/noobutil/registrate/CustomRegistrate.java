package noobanidus.libs.noobutil.registrate;

import com.mojang.datafixers.Dynamic;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomRegistrate extends AbstractRegistrate<CustomRegistrate> {
  protected CustomRegistrate(String modid) {
    super(modid);
  }

  public static CustomRegistrate create(String modid) {
    return new CustomRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
  }

  public <T extends Effect> EffectBuilder<T, CustomRegistrate> effect(Supplier<T> factory) {
    return effect(this, factory);
  }

  public <T extends Effect> EffectBuilder<T, CustomRegistrate> effect(String name, Supplier<T> factory) {
    return effect(this, name, factory);
  }

  public <T extends Effect, P> EffectBuilder<T, P> effect(P parent, Supplier<T> factory) {
    return effect(parent, currentName(), factory);
  }

  public <T extends Effect, P> EffectBuilder<T, P> effect(P parent, String name, Supplier<T> factory) {
    return entry(name, callback -> EffectBuilder.create(this, parent, name, callback, factory));
  }

  // Serializers

  public <T extends IRecipeSerializer<?>> RecipeSerializerBuilder<T, CustomRegistrate> recipeSerializer(Supplier<? extends T> factory) {
    return recipeSerializer(this, factory);
  }

  public <T extends IRecipeSerializer<?>> RecipeSerializerBuilder<T, CustomRegistrate> recipeSerializer(String name, Supplier<? extends T> factory) {
    return recipeSerializer(this, name, factory);
  }

  public <T extends IRecipeSerializer<?>, P> RecipeSerializerBuilder<T, P> recipeSerializer(P parent, Supplier<? extends T> factory) {
    return recipeSerializer(parent, currentName(), factory);
  }

  public <T extends IRecipeSerializer<?>, P> RecipeSerializerBuilder<T, P> recipeSerializer(P parent, String name, Supplier<? extends T> factory) {
    return entry(name, callback -> new RecipeSerializerBuilder<>(this, parent, name, callback, factory));
  }

  // Containers

  public <T extends Container> ContainerBuilder<T, CustomRegistrate> containerType(String name, ContainerType.IFactory<T> factory) {
    return containerType(this, name, factory);
  }

  public <T extends Container> ContainerBuilder<T, CustomRegistrate> containerType(ContainerType.IFactory<T> factory) {
    return containerType(this, factory);
  }

  public <T extends Container, P> ContainerBuilder<T, P> containerType(P parent, ContainerType.IFactory<T> factory) {
    return containerType(parent, currentName(), factory);
  }

  public <T extends Container, P> ContainerBuilder<T, P> containerType(P parent, String name, ContainerType.IFactory<T> factory) {
    return entry(name, callback -> new ContainerBuilder<>(this, parent, name, callback, factory));
  }

  // Features
  public <FC extends IFeatureConfig, T extends Feature<FC>> NotifyingFeatureBuilder<FC, T, CustomRegistrate> feature(String name, NotifyingFeatureBuilder.IFactoryNotify<FC, T> factory, Function<Dynamic<?>, FC> config, boolean doBlockNotify) {
    return feature(this, name, factory, config, doBlockNotify);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>> NotifyingFeatureBuilder<FC, T, CustomRegistrate> feature(NotifyingFeatureBuilder.IFactoryNotify<FC, T> factory, Function<Dynamic<?>, FC> config, boolean doBlockNotify) {
    return feature(this, factory, config, doBlockNotify);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>, P> NotifyingFeatureBuilder<FC, T, P> feature(P parent, NotifyingFeatureBuilder.IFactoryNotify<FC, T> factory, Function<Dynamic<?>, FC> config, boolean doBlockNotify)  {
    return feature(parent, currentName(), factory, config, doBlockNotify);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>, P> NotifyingFeatureBuilder<FC, T, P> feature(P parent, String name, NotifyingFeatureBuilder.IFactoryNotify<FC, T> factory, Function<Dynamic<?>, FC> config, boolean doBlockNotify) {
    return entry(name, callback -> new NotifyingFeatureBuilder<>(this, parent, name, callback, config, doBlockNotify, factory));
  }

  // Features
  public <FC extends IFeatureConfig, T extends Feature<FC>> FeatureBuilder<FC, T, CustomRegistrate> feature(String name, FeatureBuilder.IFactory<FC, T> factory, Function<Dynamic<?>, FC> config) {
    return feature(this, name, factory, config);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>> FeatureBuilder<FC, T, CustomRegistrate> feature(FeatureBuilder.IFactory<FC, T> factory, Function<Dynamic<?>, FC> config) {
    return feature(this, factory, config);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>, P> FeatureBuilder<FC, T, P> feature(P parent, FeatureBuilder.IFactory<FC, T> factory, Function<Dynamic<?>, FC> config)  {
    return feature(parent, currentName(), factory, config);
  }

  public <FC extends IFeatureConfig, T extends Feature<FC>, P> FeatureBuilder<FC, T, P> feature(P parent, String name, FeatureBuilder.IFactory<FC, T> factory, Function<Dynamic<?>, FC> config) {
    return entry(name, callback -> new FeatureBuilder<>(this, parent, name, callback, config, factory));
  }

  // Placement
  // Features
  public <DC extends IPlacementConfig, T extends Placement<DC>> PlacementBuilder<DC, T, CustomRegistrate> placement(String name, PlacementBuilder.IFactory<DC, T> factory, Function<Dynamic<?>, DC> config) {
    return placement(this, name, factory, config);
  }

  public <DC extends IPlacementConfig, T extends Placement<DC>> PlacementBuilder<DC, T, CustomRegistrate> placement(PlacementBuilder.IFactory<DC, T> factory, Function<Dynamic<?>, DC> config) {
    return placement(this, factory, config);
  }

  public <DC extends IPlacementConfig, T extends Placement<DC>, P> PlacementBuilder<DC, T, P> placement(P parent, PlacementBuilder.IFactory<DC, T> factory, Function<Dynamic<?>, DC> config)  {
    return placement(parent, currentName(), factory, config);
  }

  public <DC extends IPlacementConfig, T extends Placement<DC>, P> PlacementBuilder<DC, T, P> placement(P parent, String name, PlacementBuilder.IFactory<DC, T> factory, Function<Dynamic<?>, DC> config) {
    return entry(name, callback -> new PlacementBuilder<>(this, parent, name, callback, config, factory));
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

  public BlockBuilder<LogBlock, CustomRegistrate> log(String name, MaterialColor color) {
    return this.log(this.self(), name, (b) -> new LogBlock(color, b), Material.WOOD);
  }

  public <T extends LogBlock> BlockBuilder<T, CustomRegistrate> log(CustomRegistrate parent, String name, NonNullFunction<Block.Properties, T> factory, Material material) {
    return this.entry(name, (callback) -> BlockBuilder.create(this, parent, name, callback, factory, material));
  }
}
