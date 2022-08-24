package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.reference.ModData;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BlockstateGenerator {
  private static <T extends Block> String name(T block) {
    return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
  }

  // TODO: This is a little specific to Roots still
  public static <T extends Block> void cropBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    String prefix = ctx.getName().replace("crop", "");
    ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/crop"));
    p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((CropBlock) state.getBlock()).getAgeProperty());
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
          String file = prefix + state.getValue(((CropBlock) state.getBlock()).getAgeProperty());
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
    return simpleBlockstate(new ResourceLocation(ModData.getModid(), parent));
  }

  public static <T extends StairBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairs(RegistryEntry<? extends Block> parent) {
    return (ctx, p) -> p.stairsBlock(ctx.getEntry(), p.blockTexture(parent.get()));
  }

  public static <T extends StairBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairs(Supplier<? extends Block> parent) {
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
                        .getExistingFile(new ResourceLocation(ModData.getModid(), "wide_post")))
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
                            .getExistingFile(new ResourceLocation(ModData.getModid(), "narrow_post")))
                    .texture("wall", p.blockTexture(parent.get()))));
  }

  public static <T extends ButtonBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> button(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      ModelFile button = p.models().singleTexture(name(ctx.getEntry()), new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button"), p.blockTexture(parent.get()));
      ModelFile buttonPressed = p.models().singleTexture(name(ctx.getEntry()) + "_pressed", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button_pressed"), p.blockTexture(parent.get()));
      p.models().singleTexture(name(ctx.getEntry()) + "_inventory", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/button_inventory"), p.blockTexture(parent.get()));
      p.getVariantBuilder(ctx.getEntry())
          .forAllStates(state -> {
            int x = 0;
            int y = 0;
            switch (state.getValue(ButtonBlock.FACE)) {
              case CEILING:
                switch (state.getValue(ButtonBlock.FACING)) {
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
                switch (state.getValue(ButtonBlock.FACING)) {
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
                switch (state.getValue(ButtonBlock.FACING)) {
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
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(state.getValue(ButtonBlock.POWERED) ? buttonPressed : button);
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

  public static <T extends BasePressurePlateBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pressurePlate(NonNullSupplier<? extends Block> parent) {
    return (ctx, p) -> {
      ModelFile plate = p.models().singleTexture(name(ctx.getEntry()), new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/pressure_plate_up"), p.blockTexture(parent.get()));
      ModelFile platePowered = p.models().singleTexture(name(ctx.getEntry()) + "_down", new ResourceLocation("minecraft", ModelProvider.BLOCK_FOLDER + "/pressure_plate_down"), p.blockTexture(parent.get()));
      p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(PressurePlateBlock.POWERED) ? platePowered : plate).build());
    };
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pillar(String sideName, String topName) {
    return (ctx, p) -> {
      ResourceLocation side = new ResourceLocation(ModData.getModid(), sideName);
      ResourceLocation top = new ResourceLocation(ModData.getModid(), topName);
      ModelFile pillar = p.models().cubeColumn(name(ctx.getEntry()), side, top);
      p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(pillar).build());
    };
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> existingNoRotation (String existingModel) {
    return (ctx, p) -> {
      ResourceLocation rl = p.modLoc(existingModel);
      ModelFile existing = p.models().getExistingFile(rl);
      p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(existing).build());
    };
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> existingRotation (String existingModel) {
    return (ctx, p) -> {
      ResourceLocation rl = p.modLoc(existingModel);
      ModelFile existing = p.models().getExistingFile(rl);
      p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
        Direction dir;
        int xRot = 0;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
          dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        } else if (state.hasProperty(BlockStateProperties.FACING)) {
          dir = state.getValue(BlockStateProperties.FACING);
/*          if (dir == Direction.DOWN) {
            xRot = 180;
          } else if (dir.getAxis().isHorizontal()) {
            xRot = 90;
          } else {
            xRot = 0;
          }*/
        } else {
          dir = Direction.EAST;
        }
        return ConfiguredModel.builder()
            .modelFile(existing)
            .rotationX(xRot)
            .rotationY(dir.getAxis().isVertical() ? 0 : (int) (dir.toYRot() % 360))
            .build();
      });
    };
  }
}

