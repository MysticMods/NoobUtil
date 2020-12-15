package noobanidus.libs.noobutil.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;

public class BiomeConfig {
  private String name;
  private double weight;

  private ForgeConfigSpec.DoubleValue configWeight;

  private BiomeConfig (String name, double weight, Collection<BiomeConfig> list) {
    this.name = name;
    this.weight = weight;
    list.add(this);
  }

  private BiomeConfig(String name, int weight, Collection<BiomeConfig> list) {
    this(name, (double) weight, list);
  }

  public String name() {
    return name;
  }

  public double weight() {
    return configWeight.get();
  }

  public boolean shouldSpawn() {
    return weight() > 0;
  }

  public void apply(ForgeConfigSpec.Builder builder) {
    builder.comment(name + " biome generation").push(name + "_biome");
    configWeight = builder.comment("spawn weight of the biome (0 to disable generation)").defineInRange("weight", weight, 0.0, Double.MAX_VALUE);
    builder.pop();
  }

  public static class Builder {
    private final Collection<BiomeConfig> list;

    public Builder(Collection<BiomeConfig> list) {
      this.list = list;
    }

    public BiomeConfig build(String name, int weight) {
      return new BiomeConfig(name, weight, this.list);
    }

    public BiomeConfig build (String name, double weight) {
      return new BiomeConfig(name, weight, this.list);
    }
  }
}
