package noobanidus.libs.noobutil.block;

import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
