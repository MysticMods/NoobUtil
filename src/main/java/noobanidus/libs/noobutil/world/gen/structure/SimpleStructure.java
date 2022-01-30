package noobanidus.libs.noobutil.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

@SuppressWarnings("NullableProblems")
public abstract class SimpleStructure extends StructureFeature<NoneFeatureConfiguration> {
  public SimpleStructure(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public abstract StructureStartFactory<NoneFeatureConfiguration> getStartFactory();

  @Override
  public GenerationStep.Decoration step() {
    return GenerationStep.Decoration.SURFACE_STRUCTURES;
  }

  @SuppressWarnings("WeakerAccess")
  public static abstract class SimpleStart extends StructureStart<NoneFeatureConfiguration> {

    public SimpleStart(StructureFeature<NoneFeatureConfiguration> structureIn, int chunkX, int chunkZ, BoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
      super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
    }

    protected abstract ResourceLocation getPoolLocation ();
    protected abstract int getPoolSize ();

    protected abstract void modifyStructure (RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, Biome biomeIn, NoneFeatureConfiguration config, BlockPos pos);

    @Override
    public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoneFeatureConfiguration config) {
      BlockPos blockpos = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);

      JigsawPlacement.addPieces(
          dynamicRegistryManager,
          new JigsawConfiguration(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
              .get(getPoolLocation()),
              getPoolSize()),
          PoolElementStructurePiece::new,
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
