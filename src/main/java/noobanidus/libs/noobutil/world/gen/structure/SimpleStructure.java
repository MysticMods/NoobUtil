package noobanidus.libs.noobutil.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import net.minecraft.world.gen.feature.structure.Structure.IStartFactory;

@SuppressWarnings("NullableProblems")
public abstract class SimpleStructure extends Structure<NoFeatureConfig> {
  public SimpleStructure(Codec<NoFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public abstract IStartFactory<NoFeatureConfig> getStartFactory();

  @Override
  public GenerationStage.Decoration step() {
    return GenerationStage.Decoration.SURFACE_STRUCTURES;
  }

  @SuppressWarnings("WeakerAccess")
  public static abstract class SimpleStart extends StructureStart<NoFeatureConfig> {

    public SimpleStart(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
      super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
    }

    protected abstract ResourceLocation getPoolLocation ();
    protected abstract int getPoolSize ();

    protected abstract void modifyStructure (DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, Biome biomeIn, NoFeatureConfig config, BlockPos pos);

    @Override
    public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
      BlockPos blockpos = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);

      JigsawManager.addPieces(
          dynamicRegistryManager,
          new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
              .get(getPoolLocation()),
              getPoolSize()),
          AbstractVillagePiece::new,
          chunkGenerator,
          templateManagerIn,
          blockpos,
          this.pieces,
          this.random,
          true,
          true);

      this.modifyStructure(dynamicRegistryManager, chunkGenerator, templateManagerIn, biomeIn, config, blockpos);
      this.calculateBoundingBox();
    }
  }
}
