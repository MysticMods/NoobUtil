package noobanidus.libs.noobutil.processor;

import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.crafting.Crafting;

public abstract class Processor<T extends Crafting<?, ?, ?>> extends ForgeRegistryEntry<IProcessor<?>> implements IProcessor<T> {
  public Processor() {
  }
}
