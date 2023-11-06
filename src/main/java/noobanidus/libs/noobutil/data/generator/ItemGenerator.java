package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EquipmentSlot;
import noobanidus.libs.noobutil.item.WeaponType;

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

  public static NonNullFunction<Item.Properties, DyeItem> dyeItem(DyeColor color) {
    return (b) -> new DyeItem(color, b);
  }

  public static <T extends Block> NonNullFunction<Item.Properties, ItemNameBlockItem> blockNamedItem(RegistryEntry<T> block) {
    return (b) -> new ItemNameBlockItem(block.get(), b);
  }
}
