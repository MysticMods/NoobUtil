package noobanidus.libs.noobutil.modifier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import noobanidus.libs.noobutil.NoobUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PlayerModifierRegistry {
  private static boolean activated = false;
  private final static Set<Supplier<? extends Attribute>> attributes = new HashSet<>();

  public static void addModifier(Supplier<? extends Attribute> modifier) {
    if (activated) {
      NoobUtil.logger.error("Attempted to add a new Player modifier after modifiers have already been added.");
    } else {
      attributes.add(modifier);
    }
  }

  public static void init() {
    if (/*attributes.isEmpty() || */activated) {
      return;
    }

    Field modifiers;
    Field instances = ObfuscationReflectionHelper.findField(AttributeSupplier.class, "f_22241_");    instances.setAccessible(true);
    try {
      modifiers = Field.class.getDeclaredField("modifiers");
    } catch (NoSuchFieldException e) {
      return;
    }

    modifiers.setAccessible(true);
    try {
      modifiers.setInt(instances, instances.getModifiers() & ~Modifier.FINAL);
    } catch (IllegalAccessException e) {
      return;
    }

    AttributeSupplier player = DefaultAttributes.getSupplier(EntityType.PLAYER);
    Map<Attribute, AttributeInstance> map = null;
    try {
      map = new HashMap<>((Map<Attribute, AttributeInstance>)instances.get(player));
    } catch (IllegalAccessException e) {
      return;
    }
    for (Supplier<? extends Attribute> modifier : attributes) {
      Attribute attr = modifier.get();
      map.put(attr, new AttributeInstance(attr, (instance) -> {
      }));
    }
    try {
      instances.set(player, map);
    } catch (IllegalAccessException e) {
      return;
    }
    NoobUtil.logger.info("Added " + attributes.size() + " additional attributes to PlayerEntity.");
    activated = true;
  }
}
