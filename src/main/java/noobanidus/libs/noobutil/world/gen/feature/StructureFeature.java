package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.*;
import noobanidus.libs.noobutil.types.IntPair;
import noobanidus.libs.noobutil.world.gen.config.StructureFeatureConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureFeature extends Feature<StructureFeatureConfig> {
  private final WeightedList<ResourceLocation> structures;
  private final List<StructureProcessor> processors = new ArrayList<>();

  public StructureFeature(Codec<StructureFeatureConfig> codec, ResourceLocation structure) {
    this(codec, 1, structure);
  }

  public StructureFeature(Codec<StructureFeatureConfig> codec, ResourceLocation ... structures) {
    this(codec, Stream.of(structures).map(o -> new IntPair<>(1, o)).collect(Collectors.toList()));
  }

  public StructureFeature(Codec<StructureFeatureConfig> codec, int weight, ResourceLocation structure) {
    this(codec, new IntPair<>(weight, structure));
  }

  public StructureFeature addProcessor (StructureProcessor processor) {
    this.processors.add(processor);
    return this;
  }

  @SafeVarargs
  public StructureFeature(Codec<StructureFeatureConfig> codec, IntPair<ResourceLocation>... structures) {
    this(codec, Arrays.asList(structures));
  }

  public StructureFeature(Codec<StructureFeatureConfig> codec, List<IntPair<ResourceLocation>> structures) {
    super(codec);
    this.structures = new WeightedList<>();
    for (IntPair<ResourceLocation> structure : structures) {
      this.structures.add(structure.getValue(), structure.getInt());
    }
  }

  @Override
  public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, StructureFeatureConfig config) {
    Rotation rotation = Rotation.getRandom(rand);

    ResourceLocation structure = this.structures.getOne(rand);
    TemplateManager manager = reader.getLevel().getServer().getStructureManager();
    Template template = manager.get(structure);
    if (template == null) {
      throw new IllegalArgumentException("Invalid structure: " + structure);
    }
    ChunkPos chunkpos = new ChunkPos(pos);

    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR);
    BlockPos blockpos = template.getSize(rotation);
    int j = rand.nextInt(16 - blockpos.getX());
    int k = rand.nextInt(16 - blockpos.getZ());
    int l = 256;

    for (int i1 = 0; i1 < blockpos.getX(); ++i1) {
      for (int j1 = 0; j1 < blockpos.getZ(); ++j1) {
        l = Math.min(l, reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
      }
    }

    int offset = -1 + config.getOffset();

    BlockPos pos2 = new BlockPos(pos.getX() + j, l + offset, pos.getZ() + k);
    BlockPos blockpos1 = template.getZeroPositionWithTransform(pos2, Mirror.NONE, rotation);
    placementsettings.clearProcessors();
    for (StructureProcessor proc : processors) {
      placementsettings.getProcessors().add(proc);
    }
    prePlaceCallback(reader, blockpos1, blockpos1, placementsettings, rand, 4);
    boolean result = template.placeInWorld(reader, blockpos1, blockpos1, placementsettings, rand, 4);
    postPlaceCallback(reader, blockpos1, blockpos1, placementsettings, rand, 4);
    return result;
  }

  protected void prePlaceCallback (IServerWorld pServerLevel, BlockPos p_237146_2_, BlockPos p_237146_3_, PlacementSettings pSettings, Random pRandom, int pFlags) {

  }

  protected void postPlaceCallback (IServerWorld pServerLevel, BlockPos p_237146_2_, BlockPos p_237146_3_, PlacementSettings pSettings, Random pRandom, int pFlags) {

  }
}
