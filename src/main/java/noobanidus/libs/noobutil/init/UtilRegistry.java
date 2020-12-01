package noobanidus.libs.noobutil.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import noobanidus.libs.noobutil.types.SupplierBlockStateProvider;

public class UtilRegistry {
  public static RegistryEntry<BlockStateProviderType<SupplierBlockStateProvider>> SUPPLIER_STATE_PROVIDER;

  public static <T extends Registrate> void load (T registry) {
    SUPPLIER_STATE_PROVIDER = registry.simple("supplier_state_provider", BlockStateProviderType.class, () -> new BlockStateProviderType<>(SupplierBlockStateProvider.CODEC));
  }
}
