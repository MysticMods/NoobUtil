package noobanidus.libs.noobutil.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.NoobUtil;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class LazyStateSupplier extends LazySupplier<BlockState> implements INBTSerializable<CompoundNBT> {
  public static Codec<LazyStateSupplier> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("location").forGetter(o -> o.location),
      PropertyPair.CODEC.listOf().fieldOf("properties").forGetter(o -> o.properties),
      BlockState.CODEC.optionalFieldOf("state").forGetter(o -> o.state)).apply(instance, (loc, props, state) -> state.map(o -> new LazyStateSupplier(o, props)).orElseGet(() -> new LazyStateSupplier(loc, props))));

  private final ResourceLocation location;
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<BlockState> state;

  private final List<PropertyPair> properties = new ArrayList<>();

  public LazyStateSupplier(String namespace, String name) {
    this(new ResourceLocation(namespace, name));
  }

  public LazyStateSupplier(ResourceLocation location) {
    this(location, null);
  }

  public LazyStateSupplier(ResourceLocation location, @Nullable List<PropertyPair> pairs) {
    super();
    if (pairs != null) {
      this.properties.addAll(pairs);
    }
    this.supplier = () -> {
      Block block = ForgeRegistries.BLOCKS.getValue(location);
      if (block != null) {
        return this.apply(block.defaultBlockState());
      }
      return Blocks.AIR.defaultBlockState();
    };
    this.location = location;
    this.state = Optional.empty();
  }

  public LazyStateSupplier(BlockState stateIn) {
    this(stateIn, null);
  }

  public LazyStateSupplier(BlockState stateIn, @Nullable List<PropertyPair> pairs) {
    super();
    this.state = Optional.of(stateIn);
    if (pairs != null) {
      this.properties.addAll(pairs);
    }
    this.supplier = () -> this.apply(this.state.orElse(Blocks.AIR.defaultBlockState()));
    this.location = stateIn.getBlock().getRegistryName();
  }

  public BlockState apply (BlockState state) {
    for (PropertyPair pair : properties) {
      state = pair.apply(state);
    }
    return state;
  }

  public LazyStateSupplier addPair (String name, boolean value) {
    this.properties.add(new PropertyPair(name, value ? "true" : "false", "boolean"));
    return this;
  }

  public LazyStateSupplier addPair (String name, int value) {
    this.properties.add(new PropertyPair(name, String.valueOf(value), "integer"));
    return this;
  }

  @Override
  public CompoundNBT serializeNBT() {
    final CompoundNBT tag = new CompoundNBT();
    tag.put("lazy_state", CODEC.encodeStart(NBTDynamicOps.INSTANCE, this).getOrThrow(false, NoobUtil.logger::debug));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    throw new NotImplementedException("deserializeNBT not implemented, use fromNBT static method");
  }

  public static LazyStateSupplier fromNBT (CompoundNBT nbt) {
    return CODEC.decode(NBTDynamicOps.INSTANCE, nbt.get("lazy_state")).getOrThrow(false, NoobUtil.logger::debug).getFirst();
  }

  public static class PropertyPair {
    private final String propertyName;
    private final String value;
    private final String type;

    public static Codec<PropertyPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("name").forGetter(o -> o.propertyName),
        Codec.STRING.fieldOf("value").forGetter(o -> o.value),
        Codec.STRING.fieldOf("type").forGetter(o -> o.type)
    ).apply(instance, PropertyPair::new));

    public PropertyPair(String propertyName, String value, String type) {
      this.propertyName = propertyName;
      this.value = value;
      this.type = type;
    }

    @Nullable
    protected Property<?> getProperty(Block block) {
      BlockState state = block.defaultBlockState();
      Collection<Property<?>> props = state.getProperties();
      for (Property<?> prop : props) {
        if (prop.getName().equals(propertyName)) {
          return prop;
        }
      }
      return null;
    }

    public BlockState apply(BlockState state) {
      Property<?> prop = getProperty(state.getBlock());
      if (prop == null) {
        return state;
      }

      switch (this.type) {
        case "boolean":
          return state.setValue((BooleanProperty) prop, Boolean.valueOf(this.value));
        case "int":
          return state.setValue((IntegerProperty) prop, Integer.valueOf(this.value));
        default:
        case "enum":
          throw new NotImplementedException("enum isn't serialized soz");
      }
    }
  }
}
