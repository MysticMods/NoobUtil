package noobanidus.libs.noobutil.world.gen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

// Biome Builder adapted from the MysticalBiomes & Terraform Projects
// for the MysticalBiomes Reforged port to Forge:
// http://github.com/MysticMods/Traverse

public final class BiomeBuilder implements Cloneable {
  public static final BiomeTemplate BIOME_TEMPLATE = new BiomeTemplate(create());

  private ConfiguredSurfaceBuilder<?> surfaceBuilder;
  private Biome.Precipitation precipitation;
  private Biome.BiomeCategory category;
  private BiomeSpecialEffects.Builder effects;
  private Biome.TemperatureModifier temperatureModifier;
  private Float depth;
  private Float scale;
  private Float temperature;
  private Float downfall;

  private final ArrayList<Consumer<BiomeGenerationSettings.Builder>> defaultFeatureFunctions = new ArrayList<>();
  private final ArrayList<Pair<GenerationStep.Decoration, ConfiguredFeature<?, ?>>> features = new ArrayList<>();
  private final ArrayList<ConfiguredStructureFeature<? extends FeatureConfiguration, ? extends StructureFeature<? extends FeatureConfiguration>>> structureFeatures = new ArrayList<>();
  private final ArrayList<MobSpawnSettings.SpawnerData> spawnEntries = new ArrayList<>();
  private final ArrayList<Consumer<MobSpawnSettings.Builder>> spawnFunctions = new ArrayList<>();
  private float spawnChance = -1.0F;
  private boolean template = false;
  private boolean playerSpawnFriendly = false;

  private BiomeBuilder() {
    super();

  }

  public BiomeBuilder(BiomeBuilder existing) { // Template copy code
    if (existing.surfaceBuilder != null) {
      this.surfaceBuilder(existing.surfaceBuilder);
    }

    if (existing.precipitation != null) {
      this.precipitation(existing.precipitation);
    }

    if (existing.category != null) {
      this.category(existing.category);
    }

    if (existing.depth != null) {
      this.depth(existing.depth);
    }

    if (existing.scale != null) {
      this.scale(existing.scale);
    }

    if (existing.temperature != null) {
      this.temperature(existing.temperature);
    }

    if (existing.downfall != null) {
      this.downfall(existing.downfall);
    }

    if (existing.effects != null) {
      this.effects(existing.effects);
    }

    if (existing.temperatureModifier != null) {
      this.temperatureModifier(existing.temperatureModifier);
    }

    this.defaultFeatureFunctions.addAll(existing.defaultFeatureFunctions);
    this.features.addAll(existing.features);
    this.structureFeatures.addAll(existing.structureFeatures);
    this.spawnEntries.addAll(existing.spawnEntries);
    this.spawnFunctions.addAll(existing.spawnFunctions);
    this.spawnChance = existing.spawnChance;
    this.playerSpawnFriendly = existing.playerSpawnFriendly;
  }

  public static BiomeSpecialEffects.Builder createDefaultBiomeAmbience() {
    return new BiomeSpecialEffects.Builder()
        .waterColor(0x3F76E4)
        .waterFogColor(0x50533)
        .skyColor(ColorConstants.getSkyColor(0.2F))
        .fogColor(0xC0D8FF);
  }

  @SuppressWarnings("unchecked")
  public Biome build() {
    if (template) {
      throw new IllegalStateException("Tried to call build() on a frozen Builder instance!");
    }

    Biome.BiomeBuilder builder = new Biome.BiomeBuilder();
    builder.biomeCategory(this.category);
    builder.depth(this.depth);
    builder.scale(this.scale);
    builder.downfall(this.downfall);
    builder.precipitation(this.precipitation);
    builder.temperature(this.temperature);

    this.effects.skyColor(ColorConstants.getSkyColor(this.temperature));
    builder.specialEffects(this.effects.build());

    BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder().surfaceBuilder(this.surfaceBuilder);

    MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
    if (this.spawnChance != -1.0F) {
      spawnSettings.creatureGenerationProbability(this.spawnChance);
    }

    for (MobSpawnSettings.SpawnerData spawnEntry : spawnEntries) {
      spawnSettings.addSpawn(spawnEntry.type.getCategory(), spawnEntry);
    }

    for (Consumer<MobSpawnSettings.Builder> mobFunction : spawnFunctions) {
      mobFunction.accept(spawnSettings);
    }

    if (playerSpawnFriendly) {
      spawnSettings.setPlayerCanSpawn();
    }

    for (ConfiguredStructureFeature<? extends FeatureConfiguration, ? extends StructureFeature<? extends FeatureConfiguration>> structure : structureFeatures) {
      generationSettings.addStructureStart(structure);
    }

    for (Consumer<BiomeGenerationSettings.Builder> featureFunction : defaultFeatureFunctions) {
      featureFunction.accept(generationSettings);
    }

    for (Pair<GenerationStep.Decoration, ConfiguredFeature<?, ?>> feature : features) {
      generationSettings.addFeature(feature.getFirst(), feature.getSecond());
    }

    builder.generationSettings(generationSettings.build());
    builder.mobSpawnSettings(spawnSettings.build());
    return builder.build();
  }

  public BiomeBuilder surfaceBuilder(ConfiguredSurfaceBuilder<?> surfaceBuilder) {
    this.surfaceBuilder = surfaceBuilder;
    return this;
  }

  public BiomeBuilder precipitation(Biome.Precipitation precipitation) {
    this.precipitation = precipitation;
    return this;
  }

  public BiomeBuilder category(Biome.BiomeCategory category) {
    this.category = category;
    return this;
  }

  public BiomeBuilder depth(float depth) {
    this.depth = depth;
    return this;
  }

  public BiomeBuilder scale(float scale) {
    this.scale = scale;
    return this;
  }

  public BiomeBuilder temperature(float temperature) {
    this.temperature = temperature;
    return this;
  }

  public BiomeBuilder downfall(float downfall) {
    this.downfall = downfall;
    return this;
  }

  public BiomeBuilder effects(BiomeSpecialEffects.Builder effects) {
    this.effects = effects;
    return this;
  }

  public BiomeBuilder addFeature(GenerationStep.Decoration step, ConfiguredFeature<?, ?> feature) {
    this.features.add(Pair.of(step, feature));
    return this;
  }

  public BiomeBuilder addSpawnEntry(MobSpawnSettings.SpawnerData entry) {
    this.spawnEntries.add(entry);
    return this;
  }

  public BiomeBuilder addSpawnFunction(Consumer<MobSpawnSettings.Builder> function) {
    this.spawnFunctions.add(function);
    return this;
  }

  @SafeVarargs
  public final BiomeBuilder addSpawnFunctions(Consumer<MobSpawnSettings.Builder>... functions) {
    this.spawnFunctions.addAll(Arrays.asList(functions));
    return this;
  }

  public BiomeBuilder addStructureFeature(ConfiguredStructureFeature<? extends FeatureConfiguration, ? extends StructureFeature<? extends FeatureConfiguration>> stucture) {
    this.structureFeatures.add(stucture);
    return this;
  }

  public BiomeBuilder addDefaultFeatureFunction(Consumer<BiomeGenerationSettings.Builder> feature) {
    defaultFeatureFunctions.add(feature);
    return this;
  }

  @SafeVarargs
  public final BiomeBuilder addDefaultFeatureFunctions(Consumer<BiomeGenerationSettings.Builder>... feature) {
    defaultFeatureFunctions.addAll(Arrays.asList(feature));
    return this;
  }

  public BiomeBuilder playerSpawnFriendly() {
    playerSpawnFriendly = true;
    return this;
  }

  private void markTemplate() {
    this.template = true;
  }

  public static BiomeBuilder create() {
    return new BiomeBuilder();
  }

  public BiomeBuilder temperatureModifier(Biome.TemperatureModifier temperatureModifier) {
    this.temperatureModifier = temperatureModifier;
    return this;
  }

  public static final class BiomeTemplate {
    private final BiomeBuilder builder;

    public BiomeTemplate(BiomeBuilder builder) {
      this.builder = builder;
      builder.markTemplate();
    }

    public BiomeBuilder builder() {
      return new BiomeBuilder(this.builder);
    }
  }
}
