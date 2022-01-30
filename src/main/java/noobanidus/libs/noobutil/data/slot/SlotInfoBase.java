package noobanidus.libs.noobutil.data.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import noobanidus.libs.noobutil.reference.NBTConstants;

public abstract class SlotInfoBase<V> {
  protected final String key;
  protected CompoundTag tag;

  public SlotInfoBase(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  public abstract Class<?> getType();

  public abstract CompoundTag getTag();

  protected CompoundTag getBaseTag() {
    if (tag == null) {
      tag = new CompoundTag();
      tag.putString(NBTConstants.Key, key);
    }
    return tag;
  }

  public String asString() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a string.");
  }

  public int asInt() {
    throw new InvalidTypeException(getKey() + " cannot be converted into an integer.");
  }

  public float asFloat() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a float.");
  }

  public boolean asBoolean() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a boolean.");
  }

  public abstract V asObject();

  public static SlotInfoBase<?> fromNBT(CompoundTag tag) {
    String key = tag.getString(NBTConstants.Key);
    Tag value = tag.get(NBTConstants.Value);
    byte id = value.getId();
    if (id == Tag.TAG_STRING) {
      return new StringDataEntry(key, tag.getString(NBTConstants.Value));
    } else if (id == Tag.TAG_INT) {
      return new IntegerDataEntry(key, tag.getInt(NBTConstants.Value));
    } else if (id == Tag.TAG_FLOAT) {
      return new FloatDataEntry(key, tag.getFloat(NBTConstants.Value));
    } else if (id == Tag.TAG_BYTE) {
      return new BooleanDataEntry(key, tag.getBoolean(NBTConstants.Value));
    } else {
      throw new RuntimeException("Invalid NBT tag stored in data entry: " + tag);
    }
  }

  public static class StringDataEntry extends SlotInfoBase<String> {
    private String value;

    public StringDataEntry(String key, String value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return String.class;
    }

    @Override
    public CompoundTag getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putString(NBTConstants.Value, value);
      }
      return tag;
    }

    @Override
    public String asString() {
      return value;
    }

    @Override
    public String asObject() {
      return asString();
    }
  }

  public static class IntegerDataEntry extends SlotInfoBase<Integer> {
    private int value;

    public IntegerDataEntry(String key, int value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Integer.class;
    }

    @Override
    public CompoundTag getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putInt(NBTConstants.Value, value);
      }
      return tag;
    }

    @Override
    public int asInt() {
      return value;
    }

    @Override
    public Integer asObject() {
      return asInt();
    }
  }

  public static class FloatDataEntry extends SlotInfoBase<Float> {
    private float value;

    public FloatDataEntry(String key, float value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Float.class;
    }

    @Override
    public CompoundTag getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putFloat(NBTConstants.Value, value);
      }
      return tag;
    }

    @Override
    public float asFloat() {
      return value;
    }

    @Override
    public Float asObject() {
      return asFloat();
    }
  }

  public static class BooleanDataEntry extends SlotInfoBase<Boolean> {
    private boolean value;

    public BooleanDataEntry(String key, boolean value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Boolean.class;
    }

    @Override
    public CompoundTag getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putBoolean(NBTConstants.Value, value);
      }
      return tag;
    }

    @Override
    public boolean asBoolean() {
      return this.value;
    }

    @Override
    public Boolean asObject() {
      return asBoolean();
    }
  }
}
