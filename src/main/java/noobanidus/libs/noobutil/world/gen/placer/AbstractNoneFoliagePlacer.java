package noobanidus.libs.noobutil.world.gen.placer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer.Foliage;

public abstract class AbstractNoneFoliagePlacer extends FoliagePlacer {
  public static <T extends AbstractNoneFoliagePlacer> Codec<T> codecBuilder(BiFunction<FeatureSpread, FeatureSpread, T> builder) {
    return RecordCodecBuilder.create((instance) -> foliagePlacerParts(instance).apply(instance, builder));
  }

  protected AbstractNoneFoliagePlacer(FeatureSpread p_i241999_1_, FeatureSpread p_i241999_2_) {
    super(p_i241999_1_, p_i241999_2_);
  }

  public AbstractNoneFoliagePlacer() {
    super(FeatureSpread.of(0, 0), FeatureSpread.of(0, 0));
  }

  @Override
  protected abstract FoliagePlacerType<?> type();

  @Override
  protected void createFoliage(IWorldGenerationReader p_230372_1_, Random p_230372_2_, BaseTreeFeatureConfig p_230372_3_, int p_230372_4_, Foliage p_230372_5_, int p_230372_6_, int p_230372_7_, Set<BlockPos> p_230372_8_, int p_230372_9_, MutableBoundingBox p_230372_10_) {

  }

  @Override
  public int foliageHeight(Random pRandom, int pHeight, BaseTreeFeatureConfig pConfig) {
    return 0;
  }

  @Override
  protected boolean shouldSkipLocation(Random pRandom, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
    return false;
  }
}
