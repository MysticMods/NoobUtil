package noobanidus.libs.noobutil.modifiers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import noobanidus.libs.noobutil.util.NoobUtil;

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
    if (attributes.isEmpty() || activated) {
      return;
    }

    AttributeModifierMap player = GlobalEntityTypeAttributes.getAttributesForEntity(EntityType.PLAYER);
    Map<Attribute, ModifiableAttributeInstance> map = new HashMap<>(player.attributeMap);
    for (Supplier<? extends Attribute> modifier : attributes) {
      Attribute attr = modifier.get();
      map.put(attr, new ModifiableAttributeInstance(attr, (instance) -> {
      }));
    }
    player.attributeMap = map;
    NoobUtil.logger.info("Added " + attributes.size() + " additional attributes to PlayerEntity.");
  }
}
