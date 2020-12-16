package noobanidus.libs.noobutil.world.gen.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;
import noobanidus.libs.noobutil.types.LazyStateSupplier;

import java.util.List;

public class SupplierSphereReplaceConfig implements IFeatureConfig {
   public static final Codec<SupplierSphereReplaceConfig> CODEC = RecordCodecBuilder.create((p_236518_0_) -> p_236518_0_.group(BlockState.CODEC.fieldOf("state").forGetter((p_236521_0_) -> p_236521_0_.state), FeatureSpread.func_242254_a(0, 4, 4).fieldOf("radius").forGetter((p_236520_0_) -> p_236520_0_.radius), Codec.intRange(0, 4).fieldOf("half_height").forGetter((p_236519_0_) -> p_236519_0_.half_height), LazyStateSupplier.CODEC.listOf().fieldOf("targets").forGetter((p_236517_0_) -> p_236517_0_.targets)).apply(p_236518_0_, SupplierSphereReplaceConfig::new));
   public final BlockState state;
   public final FeatureSpread radius;
   public final int half_height;
   public final List<LazyStateSupplier> targets;

   public SupplierSphereReplaceConfig(BlockState p_i241986_1_, FeatureSpread p_i241986_2_, int p_i241986_3_, List<LazyStateSupplier> p_i241986_4_) {
      this.state = p_i241986_1_;
      this.radius = p_i241986_2_;
      this.half_height = p_i241986_3_;
      this.targets = p_i241986_4_;
   }
}
