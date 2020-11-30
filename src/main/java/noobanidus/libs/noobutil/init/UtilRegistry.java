package noobanidus.libs.noobutil.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import noobanidus.libs.noobutil.types.SupplierBlockStateProvider;

public class UtilRegistry {
  public static RegistryEntry<BlockStateProviderType<SupplierBlockStateProvider>> SUPPLIER_STATE_PROVIDER;

  public static void load (RegistryEntry<BlockStateProviderType<SupplierBlockStateProvider>> supplierStateProvider) {
    // TODO: Gosh, this is problematic
    if (SUPPLIER_STATE_PROVIDER == null) {
      SUPPLIER_STATE_PROVIDER = supplierStateProvider;
    }
  }
}
