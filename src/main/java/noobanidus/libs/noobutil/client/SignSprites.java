package noobanidus.libs.noobutil.client;

import net.minecraft.client.renderer.model.RenderMaterial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SignSprites {
  private static final List<RenderMaterial> sprites = new ArrayList<>();

  public static void addRenderMaterial(RenderMaterial material) {
    sprites.add(material);
  }

  public static Collection<RenderMaterial> getSprites() {
    return Collections.unmodifiableCollection(sprites);
  }
}
