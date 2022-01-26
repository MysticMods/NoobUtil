package noobanidus.libs.noobutil.processor;

import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.crafting.ContainerCrafting;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.crafting.ICrafting;

public abstract class Processor<T extends ICrafting<?, ?>> extends ForgeRegistryEntry<IProcessor<?>> implements IProcessor<T> {
  public Processor() {
  }
}
