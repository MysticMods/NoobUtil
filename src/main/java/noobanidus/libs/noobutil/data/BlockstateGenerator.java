package noobanidus.libs.noobutil.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.IForgeRegistryEntry;
import noobanidus.libs.noobutil.block.BaseBlocks;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BlockstateGenerator {

  // Helpers
  protected static String MODID;

  public static void setModid(String modid) {
    MODID = modid;
  }

  private static <T extends IForgeRegistryEntry<?>> String name(T block) {
    return Objects.requireNonNull(block.getRegistryName()).getPath();
  }

  // TODO: This is a little specific to Roots still
  public static <T extends Block> void cropBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    String prefix = ctx.getName().replace("crop", "");
    ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/crop"));
    p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((CropsBlock) state.getBlock()).getAgeProperty());
          ModelFile stage = p.models().getBuilder(file)
              .parent(crop)
              .texture("crop", p.modLoc("block/crops/" + file));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
  }

  public static <T extends Block> void crossBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    String prefix = ctx.getName().replace("crop", "");
    ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/cross"));
    p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((CropsBlock) state.getBlock()).getAgeProperty());
          ModelFile stage = p.models().getBuilder(file)
              .parent(crop)
              .texture("cross", p.modLoc("block/crops/" + file));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
  }

  public static <T extends Block> void simpleBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    p.simpleBlock(ctx.getEntry());
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleBlockstate(ResourceLocation parent) {
    return (ctx, p) -> p.simpleBlock(ctx.getEntry(), p.models().getExistingFile(parent));
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleBlockstate(String parent) {
    return simpleBlockstate(new ResourceLocation(MODID, parent));
  }

  public static <T extends StairsBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairs(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> p.stairsBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends StairsBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairs(Supplier<? extends Block> parent) {
    return (ctx, p) -> p.stairsBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(RegistryEntry<? extends T> parent) {
    return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(parent.get()));
  }

  public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(RegistryEntry<? extends Block> parent, Supplier<Block> visual) {
    return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(visual.get()));
  }

  public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(Supplier<? extends Block> parent) {
    return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(parent.get()));
  }

  public static <T extends FenceBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> fence(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> {
      p.fenceBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().fenceInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends FenceBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> fence(Supplier<? extends Block> parent) {
    return (ctx, p) -> {
      p.fenceBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().fenceInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends WallBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> wall(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> {
      p.wallBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().wallInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends WallBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> wall(Supplier<? extends Block> parent) {
    return (ctx, p) -> {
      p.wallBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().wallInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends FenceGateBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> gate(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> p.fenceGateBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends FenceGateBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> gate(Supplier<? extends Block> parent) {
    return (ctx, p) -> p.fenceGateBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends BaseBlocks.WidePostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> widePost(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> p.getVariantBuilder(ctx.getEntry())
        .partialState()
        .addModels(new ConfiguredModel(
            p.models()
                .getBuilder(name(ctx.getEntry()))
                .parent(
                    p.models()
                        .getExistingFile(new ResourceLocation(MODID, "wide_post")))
                .texture("wall", p.blockTexture(parent.get()))));
  }

  public static <T extends BaseBlocks.WidePostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> widePost(Supplier<? extends Block> parent) {
    return (ctx, p) -> p.getVariantBuilder(ctx.getEntry())
        .partialState()
        .addModels(new ConfiguredModel(
            p.models()
                .getBuilder(name(ctx.getEntry()))
                .parent(
                    p.models()
                        .getExistingFile(new ResourceLocation(MODID, "wide_post")))
                .texture("wall", p.blockTexture(parent.get()))));
  }

  public static <T extends BaseBlocks.NarrowPostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> narrowPost(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> p.getVariantBuilder(ctx.getEntry())
        .partialState()
        .addModels(new ConfiguredModel(
            p.models()
                .getBuilder(name(ctx.getEntry()))
                .parent(
                    p.models()
                        .getExistingFile(new ResourceLocation(MODID, "narrow_post")))
                .texture("wall", p.blockTexture(parent.get()))));
  }

  public static <T extends BaseBlocks.NarrowPostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> narrowPost(Supplier<? extends Block> parent) {
    return (ctx, p) ->
        p.getVariantBuilder(ctx.getEntry())
            .partialState()
            .addModels(new ConfiguredModel(
                p.models()
                    .getBuilder(name(ctx.getEntry()))
                    .parent(
                        p.models()
                            .getExistingFile(new ResourceLocation(MODID, "narrow_post")))
                    .texture("wall", p.blockTexture(parent.get()))));
  }
}
