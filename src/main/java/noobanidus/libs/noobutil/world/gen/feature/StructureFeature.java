package noobanidus.libs.noobutil.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.gen.feature.template.*;
import noobanidus.libs.noobutil.type.IntPair;
import noobanidus.libs.noobutil.world.gen.config.StructureFeatureConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

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
  public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, StructureFeatureConfig config) {
    Rotation rotation = Rotation.getRandom(rand);

    ResourceLocation structure = this.structures.getOne(rand);
    StructureManager manager = reader.getLevel().getServer().getStructureManager();
    StructureTemplate template = manager.get(structure);
    if (template == null) {
      throw new IllegalArgumentException("Invalid structure: " + structure);
    }
    ChunkPos chunkpos = new ChunkPos(pos);

    BoundingBox mutableboundingbox = new BoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
    StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
    BlockPos blockpos = template.getSize(rotation);
    int j = rand.nextInt(16 - blockpos.getX());
    int k = rand.nextInt(16 - blockpos.getZ());
    int l = 256;

    for (int i1 = 0; i1 < blockpos.getX(); ++i1) {
      for (int j1 = 0; j1 < blockpos.getZ(); ++j1) {
        l = Math.min(l, reader.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
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

  protected void prePlaceCallback (ServerLevelAccessor pServerLevel, BlockPos p_237146_2_, BlockPos p_237146_3_, StructurePlaceSettings pSettings, Random pRandom, int pFlags) {

  }

  protected void postPlaceCallback (ServerLevelAccessor pServerLevel, BlockPos p_237146_2_, BlockPos p_237146_3_, StructurePlaceSettings pSettings, Random pRandom, int pFlags) {

  }
}
