package noobanidus.libs.noobutil.item;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;

public class BaseItems {
  public static class BowlItem extends MultiReturnItem {
    public BowlItem(Properties properties) {
      super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
    }

    @Override
    protected Item getReturnItem(ItemStack stack) {
      return Items.BOWL;
    }
  }

  @SuppressWarnings("NullableProblems")
  public static class DrinkItem extends MultiReturnItem {
    public DrinkItem(Properties properties) {
      super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
    }

    @Override
    protected Item getReturnItem(ItemStack stack) {
      return Items.GLASS_BOTTLE;
    }
  }

  public static class EffectItem extends Item {

    public EffectItem(Properties properties) {
      super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isFoil(ItemStack stack) {
      return true;
    }
  }

  public static class FastFoodItem extends Item {
    public FastFoodItem(Properties properties) {
      super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
      if (stack.getItem().isEdible()) {
        return this.getFoodProperties().isFastFood() ? 6 : 32;
      } else {
        return 0;
      }
    }
  }

  public static class KnifeItem extends DiggerItem {
    public static Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.SPRUCE_LOG);

    // TODO rework knives to strip logs of bark with right click, or drop bark by mining it
    public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties props) {
      super(attackDamage, attackSpeed, tier, EFFECTIVE_BLOCKS, props);
    }
  }

  public static class SpearItem extends SwordItem {
    public SpearItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
      super(tier, attackDamageIn, attackSpeedIn, builder);
    }
  }
}
