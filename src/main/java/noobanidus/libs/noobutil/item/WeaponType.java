package noobanidus.libs.noobutil.item;

import javax.annotation.Nullable;

public enum WeaponType {
  SWORD("sword"),
  KNIFE("knife"),
  PICKAXE("pickaxe"),
  AXE("axe"),
  SHOVEL("shovel"),
  HOE("hoe"),
  SPEAR("spear");

  private final String name;

  WeaponType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Nullable
  public static WeaponType byName(String name) {
    for (WeaponType i : values()) {
      if (i.getName().equals(name)) {
        return i;
      }
    }

    return null;
  }
}
