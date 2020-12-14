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

public abstract class AbstractNoneFoliagePlacer extends FoliagePlacer {
  public static <T extends AbstractNoneFoliagePlacer> Codec<T> codecBuilder(BiFunction<FeatureSpread, FeatureSpread, T> builder) {
    return RecordCodecBuilder.create((instance) -> func_242830_b(instance).apply(instance, builder));
  }

  private AbstractNoneFoliagePlacer(FeatureSpread p_i241999_1_, FeatureSpread p_i241999_2_) {
    super(p_i241999_1_, p_i241999_2_);
  }

  public AbstractNoneFoliagePlacer() {
    super(FeatureSpread.func_242253_a(0, 0), FeatureSpread.func_242253_a(0, 0));
  }

  @Override
  protected abstract FoliagePlacerType<?> func_230371_a_();

  @Override
  protected void func_230372_a_(IWorldGenerationReader p_230372_1_, Random p_230372_2_, BaseTreeFeatureConfig p_230372_3_, int p_230372_4_, Foliage p_230372_5_, int p_230372_6_, int p_230372_7_, Set<BlockPos> p_230372_8_, int p_230372_9_, MutableBoundingBox p_230372_10_) {

  }

  @Override
  public int func_230374_a_(Random p_230374_1_, int p_230374_2_, BaseTreeFeatureConfig p_230374_3_) {
    return 0;
  }

  @Override
  protected boolean func_230373_a_(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
    return false;
  }
}
