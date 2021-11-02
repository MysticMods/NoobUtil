package noobanidus.libs.noobutil.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.block.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
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

  public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(NonNullSupplier<? extends Block> parent, NonNullSupplier<Block> visual) {
    return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(visual.get()));
  }

  public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(parent.get()));
  }

  public static <T extends FenceBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> fence(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      p.fenceBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().fenceInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends WallBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> wall(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      p.wallBlock(ctx.getEntry(), p.blockTexture(parent.get()));
      p.models().wallInventory(name(ctx.getEntry()) + "_inventory", p.blockTexture(parent.get()));
    };
  }

  public static <T extends FenceGateBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> gate(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> p.fenceGateBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends BaseBlocks.WidePostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> widePost(NonNullSupplier<? extends Block> parent) {
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

  public static <T extends BaseBlocks.NarrowPostBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> narrowPost(NonNullSupplier<? extends Block> parent) {
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

  public static <T extends AbstractButtonBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> button(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      ModelFile button = p.models().singleTexture(name(ctx.getEntry()) + "_button", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button"), p.blockTexture(parent.get()));
      ModelFile buttonPressed = p.models().singleTexture(name(ctx.getEntry()) + "_button_pressed", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button_pressed"), p.blockTexture(parent.get()));
      p.models().singleTexture(name(ctx.getEntry()) + "_button_inventory", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button_inventory"), p.blockTexture(parent.get()));
      p.getVariantBuilder(ctx.getEntry())
          .forAllStates(state -> {
            int x = 0;
            int y = 0;
            switch (state.getValue(AbstractButtonBlock.FACE)) {
              case CEILING:
                switch (state.getValue(AbstractButtonBlock.FACING)) {
                  case EAST:
                    y = 270;
                    x = 180;
                    break;
                  case NORTH:
                    y = 180;
                    x = 180;
                    break;
                  case SOUTH:
                    x = 180;
                    break;
                  case WEST:
                    y = 90;
                    x = 180;
                    break;
                }
                break;
              case FLOOR:
                switch (state.getValue(AbstractButtonBlock.FACING)) {
                  case EAST:
                    y = 90;
                    break;
                  case NORTH:
                    break;
                  case SOUTH:
                    y = 180;
                    break;
                  case WEST:
                    y = 270;
                    break;
                }
                break;
              case WALL:
                switch (state.getValue(AbstractButtonBlock.FACING)) {
                  case EAST:
                    y = 90;
                    x = 90;
                    break;
                  case NORTH:
                    x = 90;
                    break;
                  case SOUTH:
                    y = 180;
                    x = 90;
                    break;
                  case WEST:
                    y = 270;
                    x = 90;
                    break;
                }
                break;
            }
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(state.getValue(AbstractButtonBlock.POWERED) ? buttonPressed : button);
            if (y != 0) {
              builder.rotationY(y);
            }
            if (x != 0) {
              builder.rotationX(x);
            }
            return builder.build();
          });
    };
  }

  public static <T extends AbstractPressurePlateBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pressurePlate(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      ModelFile plate = p.models().singleTexture(name(ctx.getEntry()) + "_pressure_plate", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/pressure_plate_up"), p.blockTexture(parent.get()));
      ModelFile platePowered = p.models().singleTexture(name(ctx.getEntry()) + "_pressure_plate_down", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/pressure_plate_down"), p.blockTexture(parent.get()));
      p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(PressurePlateBlock.POWERED) ? platePowered : plate).build());
    };
  }
}

