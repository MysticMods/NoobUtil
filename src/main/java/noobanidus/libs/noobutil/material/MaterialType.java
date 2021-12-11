package noobanidus.libs.noobutil.material;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import noobanidus.libs.noobutil.config.IArmorConfig;
import noobanidus.libs.noobutil.item.WeaponType;
import noobanidus.libs.noobutil.ingredient.LazyIngredient;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class MaterialType {
  public static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
  public static final UUID MAIN_HAND_MODIFIER = UUID.fromString("0e2c39ce-5247-4095-abf7-d99bd7387a95");
  public static final UUID OFF_HAND_MODIFIER = UUID.fromString("28ad5d13-618f-4c80-8f60-0e0469c1a046");

  private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

  private final String name;
  private IItemTier tier = null;
  private IArmorMaterial material = null;
  private int maxUses;
  private float efficiency;
  private float attackDamage;
  private int harvestLevel;
  private int enchantability;
  private float knockbackResistance;
  private LazyIngredient repairMaterial;

  private int maxDamageFactor;
  private int[] damageReductionAmountArray;
  private SoundEvent soundEvent;
  private float toughness;

  private Supplier<Supplier<? extends Item>> item;
  private Supplier<Supplier<? extends Item>> dust;
  private Supplier<Supplier<? extends Item>> nugget;
  private Supplier<Supplier<? extends Block>> block;
  private Supplier<Supplier<? extends Block>> ore;

  private int maxXP = 0;
  private int minXP = 0;

  private String modId;

  private Object2FloatOpenHashMap<WeaponType> damage = new Object2FloatOpenHashMap<>();
  private Object2FloatOpenHashMap<WeaponType> speed = new Object2FloatOpenHashMap<>();

  private List<WeaponType> itemTypes;

  private ArmorMaterial armorMaterial = new ArmorMaterial();
  private ItemMaterial itemMaterial = new ItemMaterial();

  private Function<String, IArmorConfig> configProvider = null;

  public MaterialType(String name) {
    this.name = name;
    this.putDamageSpeed(WeaponType.SWORD, 3.0f, -2.4f, WeaponType.SHOVEL, 1.5f, -3.0f, WeaponType.PICKAXE, 1.0f, -2.8f, WeaponType.HOE, 1.0f, -1.0f);
  }

  public ArmorMaterial getArmorMaterial() {
    return armorMaterial;
  }

  public ItemMaterial getItemMaterial() {
    return itemMaterial;
  }

  public MaterialType setModId(String modId) {
    this.modId = modId;
    return this;
  }

  public String getModId() {
    return modId;
  }

  public MaterialType itemMaterial(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability) {
    this.maxUses = maxUses;
    this.efficiency = efficiency;
    this.attackDamage = attackDamage;
    this.harvestLevel = harvestLevel;
    this.enchantability = enchantability;
    this.repairMaterial = new LazyIngredient(() -> Ingredient.of(item.get().get()));
    return this;
  }

  public MaterialType itemMaterial(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<ITag.INamedTag<Item>> repairTag) {
    itemMaterial(maxUses, efficiency, attackDamage, harvestLevel, enchantability);
    this.repairMaterial = new LazyIngredient(() -> Ingredient.of(repairTag.get()));
    return this;
  }

  public MaterialType itemMaterial(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Ingredient repairMaterial) {
    itemMaterial(maxUses, efficiency, attackDamage, harvestLevel, enchantability);
    if (repairMaterial instanceof LazyIngredient) {
      this.repairMaterial = (LazyIngredient) repairMaterial;
    } else {
      this.repairMaterial = new LazyIngredient(() -> repairMaterial);
    }
    return this;
  }

  public MaterialType armorMaterial(int maxDamageFactor, int[] damageReductionAmountArray, SoundEvent soundEvent, float toughness, float knockbackResistance) {
    this.maxDamageFactor = maxDamageFactor;
    this.damageReductionAmountArray = damageReductionAmountArray;
    this.soundEvent = soundEvent;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
    return this;
  }

  public MaterialType setItemTier(IItemTier tier) {
    this.tier = tier;
    return this;
  }

  public MaterialType setArmorMaterial(IArmorMaterial material) {
    this.material = material;
    return this;
  }

  public MaterialType setConfigProvider(Function<String, IArmorConfig> configProvider) {
    this.configProvider = configProvider;
    return this;
  }

  public int getMaxXP() {
    return maxXP;
  }

  public MaterialType setMaxXP(int maxXP) {
    this.maxXP = maxXP;
    return this;
  }

  public int getMinXP() {
    return minXP;
  }

  public MaterialType setMinXP(int minXP) {
    this.minXP = minXP;
    return this;
  }

  public MaterialType putDamageSpeed(Object... entries) {
    if (entries.length % 3 != 0) {
      throw new IllegalArgumentException("Invalid number of arguments to putDamageSpeed");
    }

    for (int i = 0; i < entries.length; i += 3) {
      WeaponType type = (WeaponType) entries[i];
      float damage = (float) entries[i + 1];
      float speed = (float) entries[i + 2];

      this.damage.put(type, damage);
      this.speed.put(type, speed);
    }

    return this;
  }

  public int getDamage(WeaponType type) {
    return (int) damage.getOrDefault(type, 1.0f);
  }

  public int getDamage(String type) {
    return getDamage(WeaponType.byName(type));
  }

  public float getSpeed(WeaponType type) {
    return speed.getOrDefault(type, -1.0f);
  }

  public float getSpeed(String type) {
    return getSpeed(WeaponType.byName(type));
  }

  public List<WeaponType> getItemTypes() {
    return itemTypes;
  }

  public MaterialType setItemTypes(WeaponType... types) {
    this.itemTypes = Arrays.asList(types);
    return this;
  }

  public String getInternalName() {
    return name;
  }

  public String getIngotName () {
    return name + "_ingot";
  }

  public Supplier<? extends Item> getItem() {
    return item.get();
  }

  public Supplier<? extends Item> getDust() {
    return dust.get();
  }

  public Supplier<? extends Item> getNugget() {
    return nugget.get();
  }

  public Supplier<? extends Block> getBlock() {
    return block.get();
  }

  public Supplier<? extends Block> getOre() {
    return ore.get();
  }

  public MaterialType item(Supplier<Supplier<? extends Item>> ingot) {
    this.item = ingot;
    return this;
  }

  public MaterialType dust(Supplier<Supplier<? extends Item>> dust) {
    this.dust = dust;
    return this;
  }

  public MaterialType nugget(Supplier<Supplier<? extends Item>> nugget) {
    this.nugget = nugget;
    return this;
  }

  public MaterialType block(Supplier<Supplier<? extends Block>> block) {
    this.block = block;
    return this;
  }

  public MaterialType ore(Supplier<Supplier<? extends Block>> ore) {
    this.ore = ore;
    return this;
  }

  public Block.Properties getBlockProps (Block.Properties props) {
    return props.strength(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1);
  }

  public Supplier<Block.Properties> getBlockProps() {
    return () -> getBlockProps(Block.Properties.of(Material.METAL));
  }

  public Block.Properties getOreBlockProperties (Block.Properties props) {
    return props.strength(3.0f, 3.0f).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(getHarvestLevel() - 1);
  }

  public Supplier<Block.Properties> getOreBlockProperties() {
    return () -> getOreBlockProperties(Block.Properties.of(Material.STONE));
  }

  public int getHarvestLevel() {
    return tier == null ? harvestLevel : tier.getLevel();
  }

  public String gemName() {
    return name + "_gem";
  }

  public String ingotName() {
    return name + "_ingot";
  }

  public String dustName() {
    return name + "_dust";
  }

  public String blockName() {
    return name + "_block";
  }

  public String oreName() {
    return name + "_ore";
  }

  public String nuggetName() {
    return name + "_nugget";
  }

  public class ArmorMaterial implements IArmorMaterial {
    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
      return material == null ? MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor : material.getDurabilityForSlot(slotIn);
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
      if (configProvider != null) {
        IArmorConfig config = configProvider.apply(name);
        if (config != null) {
          if (slotIn == EquipmentSlotType.HEAD) {
            return config.getHead();
          } else if (slotIn == EquipmentSlotType.FEET) {
            return config.getFeet();
          } else if (slotIn == EquipmentSlotType.CHEST) {
            return config.getChest();
          } else if (slotIn == EquipmentSlotType.LEGS) {
            return config.getLegs();
          }
        }
      }
      return material == null ? damageReductionAmountArray[slotIn.getIndex()] : material.getDefenseForSlot(slotIn);
    }

    @Override
    public SoundEvent getEquipSound() {
      return material == null ? soundEvent : material.getEquipSound();
    }

    @Override
    public float getToughness() {
      if (configProvider != null) {
        IArmorConfig config = configProvider.apply(name);
        if (config != null) {
          return config.getToughness();
        }
      }
      return material == null ? toughness : material.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
      return material == null ? knockbackResistance : material.getKnockbackResistance();
    }

    @Override
    @Nonnull
    public Ingredient getRepairIngredient() {
      return tier == null ? repairMaterial : tier.getRepairIngredient();
    }

    @Override
    public int getEnchantmentValue() {
      return tier == null ? enchantability : tier.getEnchantmentValue();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
      return getModId() + ":" + getInternalName();
    }
  }

  public class ItemMaterial implements IItemTier {
    @Override
    public int getUses() {
      return tier == null ? maxUses : tier.getUses();
    }

    @Override
    public float getSpeed() {
      return tier == null ? efficiency : tier.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
      return tier == null ? attackDamage : tier.getAttackDamageBonus();
    }

    @Override
    public int getLevel() {
      return MaterialType.this.getHarvestLevel();
    }

    @Override
    public int getEnchantmentValue() {
      return tier == null ? enchantability : tier.getEnchantmentValue();
    }

    @Override
    @Nonnull
    public Ingredient getRepairIngredient() {
      return tier == null ? repairMaterial : tier.getRepairIngredient();
    }
  }
}
