package noobanidus.libs.noobutil.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.util.ItemUtil;

import net.minecraft.item.Item.Properties;

public abstract class MultiReturnItem extends Item {
  public MultiReturnItem(Properties properties) {
    super(properties);
  }

  @Override
  public abstract UseAction getUseAnimation(ItemStack stack);

  protected abstract Item getReturnItem(ItemStack stack);

  @Override
  public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
    ItemStack returned = new ItemStack(getReturnItem(stack));
    ItemStack result = super.finishUsingItem(stack, world, entity);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      if (!player.addItem(returned)) {
        ItemUtil.Spawn.spawnItem(world, player.blockPosition(), returned);
      }
    }
    return result;
  }
}
