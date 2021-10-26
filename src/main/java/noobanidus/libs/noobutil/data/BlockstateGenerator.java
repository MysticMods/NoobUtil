package noobanidus.libs.noobutil.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import noobanidus.libs.noobutil.block.BaseBlocks;

public class BlockstateGenerator {
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
}
