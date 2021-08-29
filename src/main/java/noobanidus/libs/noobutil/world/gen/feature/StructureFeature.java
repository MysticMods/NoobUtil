package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.*;
import noobanidus.libs.noobutil.types.IntPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StructureFeature extends Feature<NoFeatureConfig> {
  private final WeightedList<ResourceLocation> structures;
  private final List<StructureProcessor> processors = new ArrayList<>();

  public StructureFeature(Codec<NoFeatureConfig> codec, ResourceLocation structure) {
    this(codec, 1, structure);
  }

  public StructureFeature(Codec<NoFeatureConfig> codec, int weight, ResourceLocation structure) {
    this(codec, new IntPair<>(weight, structure));
  }

  protected void addProcessor (StructureProcessor processor) {
    this.processors.add(processor);
  }

  @SafeVarargs
  public StructureFeature(Codec<NoFeatureConfig> codec, IntPair<ResourceLocation>... structures) {
    super(codec);
    this.structures = new WeightedList<>();
    for (IntPair<ResourceLocation> structure : structures) {
      this.structures.func_226313_a_(structure.getValue(), structure.getInt());
    }
  }

  @Override
  public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
    Rotation rotation = Rotation.randomRotation(rand);

    ResourceLocation structure = this.structures.func_226318_b_(rand);
    TemplateManager manager = reader.getWorld().getServer().getTemplateManager();
    Template template = manager.getTemplate(structure);
    if (template == null) {
      throw new IllegalArgumentException("Invalid structure: " + structure);
    }
    ChunkPos chunkpos = new ChunkPos(pos);

    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
    BlockPos blockpos = template.transformedSize(rotation);
    int j = rand.nextInt(16 - blockpos.getX());
    int k = rand.nextInt(16 - blockpos.getZ());
    int l = 256;

    for (int i1 = 0; i1 < blockpos.getX(); ++i1) {
      for (int j1 = 0; j1 < blockpos.getZ(); ++j1) {
        l = Math.min(l, reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
      }
    }

    BlockPos pos2 = new BlockPos(pos.getX() + j, l - 1, pos.getZ() + k);
    BlockPos blockpos1 = template.getZeroPositionWithTransform(pos2, Mirror.NONE, rotation);
    placementsettings.clearProcessors();
    for (StructureProcessor proc : processors) {
      placementsettings.getProcessors().add(proc);
    }
    return template.func_237146_a_(reader, blockpos1, blockpos1, placementsettings, rand, 4);
  }
}
