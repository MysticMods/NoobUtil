package noobanidus.libs.noobutil.client;

import net.minecraft.client.resources.model.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SignSprites {
  private static final List<Material> sprites = new ArrayList<>();

  public static void addRenderMaterial(Material material) {
    sprites.add(material);
  }

  public static Collection<Material> getSprites() {
    return Collections.unmodifiableCollection(sprites);
  }
}
