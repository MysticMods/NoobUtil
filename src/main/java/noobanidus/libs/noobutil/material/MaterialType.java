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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import noobanidus.libs.noobutil.item.WeaponType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
  private Supplier<? extends Ingredient> repairMaterial = null;

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
    return this;
  }

  public MaterialType itemMaterial(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, @Nonnull Supplier<? extends Ingredient> repairMaterial) {
    this.repairMaterial = repairMaterial;
    return itemMaterial(maxUses, efficiency, attackDamage, harvestLevel, enchantability);
  }

  public MaterialType armorMaterial(int maxDamageFactor, int[] damageReductionAmountArray, SoundEvent soundEvent, float toughness) {
    this.maxDamageFactor = maxDamageFactor;
    this.damageReductionAmountArray = damageReductionAmountArray;
    this.soundEvent = soundEvent;
    this.toughness = toughness;
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

  public MaterialType repairMaterial (Supplier<Supplier<? extends Ingredient>> ingredient) {
    this.repairMaterial = ingredient.get();
    return this;
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
    return props.hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1);
  }

  public Supplier<Block.Properties> getBlockProps() {
    return () -> getBlockProps(Block.Properties.create(Material.IRON));
  }

  public Block.Properties getOreBlockProperties (Block.Properties props) {
    return props.hardnessAndResistance(3.0f, 3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(getHarvestLevel() - 1);
  }

  public Supplier<Block.Properties> getOreBlockProperties() {
    return () -> getOreBlockProperties(Block.Properties.create(Material.ROCK));
  }

  public int getHarvestLevel() {
    return tier == null ? harvestLevel : tier.getHarvestLevel();
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

  public Ingredient getActualRepairMaterial () {
    if (repairMaterial == null) {
      repairMaterial = () -> Ingredient.fromItems(item.get().get());
    }
    return repairMaterial.get();
  }

  public class ArmorMaterial implements IArmorMaterial {
    @Override
    public int getDurability(EquipmentSlotType slotIn) {
      return material == null ? MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor : material.getDurability(slotIn);
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
      return material == null ? damageReductionAmountArray[slotIn.getIndex()] : material.getDamageReductionAmount(slotIn);
    }

    @Override
    public SoundEvent getSoundEvent() {
      return material == null ? soundEvent : material.getSoundEvent();
    }

    @Override
    public float getToughness() {
      return material == null ? toughness : material.getToughness();
    }

    @Override
    @Nonnull
    public Ingredient getRepairMaterial() {
      return tier == null ? getActualRepairMaterial() : tier.getRepairMaterial();
    }

    @Override
    public int getEnchantability() {
      return tier == null ? enchantability : tier.getEnchantability();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
      return getModId() + ":" + getInternalName();
    }
  }

  public class ItemMaterial implements IItemTier {
    @Override
    public int getMaxUses() {
      return tier == null ? maxUses : tier.getMaxUses();
    }

    @Override
    public float getEfficiency() {
      return tier == null ? efficiency : tier.getEfficiency();
    }

    @Override
    public float getAttackDamage() {
      return tier == null ? attackDamage : tier.getAttackDamage();
    }

    @Override
    public int getHarvestLevel() {
      return MaterialType.this.getHarvestLevel();
    }

    @Override
    public int getEnchantability() {
      return tier == null ? enchantability : tier.getEnchantability();
    }

    @Override
    @Nonnull
    public Ingredient getRepairMaterial() {
      return tier == null ? getActualRepairMaterial() : tier.getRepairMaterial();
    }
  }
}
