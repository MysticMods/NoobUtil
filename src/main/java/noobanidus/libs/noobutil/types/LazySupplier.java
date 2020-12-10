package noobanidus.libs.noobutil.types;

import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public class LazySupplier<T> implements Supplier<T> {
   protected Supplier<T> supplier;
   protected T value;

   public LazySupplier () {
      this.supplier = () -> null;
   }

   public LazySupplier(Supplier<T> supplierIn) {
      this.supplier = supplierIn;
   }

   @Override
   public T get() {
      Supplier<T> supplier = this.supplier;
      if (supplier != null) {
         this.value = supplier.get();
         this.supplier = null;
      }

      return this.value;
   }
}
