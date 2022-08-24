package noobanidus.libs.noobutil.processor;

import noobanidus.libs.noobutil.crafting.ICrafting;

public abstract class Processor<T extends ICrafting<?, ?>> implements IProcessor<T> {
  public Processor() {
  }
}
