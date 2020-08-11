package noobanidus.libs.noobutil.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.util.ItemUtil;

public abstract class MultiReturnItem extends Item {
  public MultiReturnItem(Properties properties) {
    super(properties);
  }

  @Override
  public abstract UseAction getUseAction(ItemStack stack);

  protected abstract Item getReturnItem(ItemStack stack);

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
    ItemStack returned = new ItemStack(getReturnItem(stack));
    ItemStack result = super.onItemUseFinish(stack, world, entity);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      if (!player.addItemStackToInventory(returned)) {
        ItemUtil.Spawn.spawnItem(world, player.getPosition(), returned);
      }
    }
    return result;
  }
}
