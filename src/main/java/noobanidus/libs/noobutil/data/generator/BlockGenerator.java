package noobanidus.libs.noobutil.data.generator;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.material.MaterialType;

public class BlockGenerator {
  @SuppressWarnings("unchecked")
  public static NonNullFunction<Block.Properties, BaseBlocks.OreBlock> oreBlock(MaterialType material) {
    return (o) -> new BaseBlocks.OreBlock(o, material.getMinXP(), material.getMaxXP());
  }
}
