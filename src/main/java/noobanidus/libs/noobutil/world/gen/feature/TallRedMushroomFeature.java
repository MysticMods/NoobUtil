package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;
import net.minecraft.world.gen.feature.BigRedMushroomFeature;

import java.util.Random;

@Deprecated
/**
 * Use ConfigurableHeightBigMushroomFeature.
 *
 * Default BiggerMushroomFeatureConfig uses the same values as this but allows for configuration.
 *
 */
public class TallRedMushroomFeature extends BigRedMushroomFeature {
  public TallRedMushroomFeature(Codec<BigMushroomFeatureConfig> codec) {
    super(codec);
  }

  @Override
  protected int func_227211_a_(Random p_227211_1_) {
    return ((p_227211_1_.nextInt(3) + 4) * 2) - (2 + p_227211_1_.nextInt(2));
  }
}
