package noobanidus.libs.noobutil.block;

import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.util.ResourceLocation;

public class ModdedWallSignBlock extends WallSignBlock implements IModdedSign {
  private final ResourceLocation texture;

  public ModdedWallSignBlock(Properties properties, ResourceLocation texture) {
    super(properties, WoodType.OAK);
    this.texture = texture;
  }

  @Override
  public ResourceLocation getTexture() {
    return texture;
  }
}
