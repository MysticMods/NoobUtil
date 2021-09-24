package noobanidus.libs.noobutil.block;

import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.util.ResourceLocation;

import net.minecraft.block.AbstractBlock.Properties;

public class ModdedStandingSignBlock extends StandingSignBlock implements IModdedSign {
  private final ResourceLocation texture;
  public ModdedStandingSignBlock(Properties properties, ResourceLocation texture) {
    super(properties, WoodType.OAK);
    this.texture = texture;
  }

  @Override
  public ResourceLocation getTexture() {
    return texture;
  }
}
