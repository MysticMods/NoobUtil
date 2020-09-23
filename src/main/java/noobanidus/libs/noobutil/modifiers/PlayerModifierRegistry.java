package noobanidus.libs.noobutil.modifiers;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerModifierRegistry {
  private static PlayerModifierRegistry INSTANCE = new PlayerModifierRegistry();

  public static PlayerModifierRegistry getInstance() {
    return INSTANCE;
  }

  private List<Attribute> attributes;

  public PlayerModifierRegistry() {
    this.attributes = new ArrayList<>();
  }

  public Attribute registerAttribute(Attribute attribute) {
    this.attributes.add(attribute);
    return attribute;
  }

  public void onEntityConstructed(EntityEvent.EntityConstructing event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntity();

      AttributeModifierManager map = player.getAttributeManager();

      for (Attribute attrib : attributes) {
        map.createInstanceIfAbsent(attrib);
      }
    }
  }
}
