package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EquipmentSlot;
import noobanidus.libs.noobutil.item.WeaponType;
import noobanidus.libs.noobutil.material.MaterialType;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Tier;

public class ItemGenerator {
  @FunctionalInterface
  public interface ToolBuilder<V extends Item> {
    V apply(Tier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder);
  }

  @FunctionalInterface
  public interface ArmorBuilder<V extends Item> {
    V apply(ArmorMaterial materialIn, EquipmentSlot slot, Item.Properties builder);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> tool(ToolBuilder<T> builder, WeaponType matType, MaterialType material) {
    return (b) -> builder.apply(material.getItemMaterial(), material.getDamage(matType), material.getSpeed(matType), b);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> sword(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.SWORD, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> spear(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.SPEAR, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> pickaxe(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.PICKAXE, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> axe(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.AXE, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> shovel(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.SHOVEL, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> knife(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.KNIFE, material);
  }

  public static <T extends Item> NonNullFunction<Item.Properties, T> hoe(ToolBuilder<T> builder, MaterialType material) {
    return tool(builder, WeaponType.HOE, material);
  }

  public static <T extends ArmorItem> NonNullFunction<Item.Properties, T> armor(ArmorBuilder<T> builder, MaterialType material, EquipmentSlot slot) {
    return (b) -> builder.apply(material.getArmorMaterial(), slot, b);
  }

  public static NonNullFunction<Item.Properties, DyeItem> dyeItem(DyeColor color) {
    return (b) -> new DyeItem(color, b);
  }

  public static <T extends Block> NonNullFunction<Item.Properties, ItemNameBlockItem> blockNamedItem(RegistryEntry<T> block) {
    return (b) -> new ItemNameBlockItem(block.get(), b);
  }
}
