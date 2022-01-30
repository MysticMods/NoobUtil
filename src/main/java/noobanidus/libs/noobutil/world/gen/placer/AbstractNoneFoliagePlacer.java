package noobanidus.libs.noobutil.world.gen.placer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

public abstract class AbstractNoneFoliagePlacer extends FoliagePlacer {
  public static <T extends AbstractNoneFoliagePlacer> Codec<T> codecBuilder(BiFunction<UniformInt, UniformInt, T> builder) {
    return RecordCodecBuilder.create((instance) -> foliagePlacerParts(instance).apply(instance, builder));
  }

  protected AbstractNoneFoliagePlacer(UniformInt p_i241999_1_, UniformInt p_i241999_2_) {
    super(p_i241999_1_, p_i241999_2_);
  }

  public AbstractNoneFoliagePlacer() {
    super(UniformInt.of(0, 0), UniformInt.of(0, 0));
  }

  @Override
  protected abstract FoliagePlacerType<?> type();

  @Override
  protected void createFoliage(LevelSimulatedRW p_230372_1_, Random p_230372_2_, TreeConfiguration p_230372_3_, int p_230372_4_, FoliageAttachment p_230372_5_, int p_230372_6_, int p_230372_7_, Set<BlockPos> p_230372_8_, int p_230372_9_, BoundingBox p_230372_10_) {

  }

  @Override
  public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
    return 0;
  }

  @Override
  protected boolean shouldSkipLocation(Random pRandom, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
    return false;
  }
}
