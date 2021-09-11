package noobanidus.libs.noobutil.shaded.component.registrate.state_provider;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.noobutil.shaded.component.registrate.RegistrateShadedComponent;
import noobanidus.libs.noobutil.util.ClassUtil;
import noobanidus.libs.noobutil.world.gen.provider.SupplierBlockStateProvider;

public class ShadedSupplierBlockStateProviderComponent extends RegistrateShadedComponent<BlockStateProviderType<?>> {
  public static RegistryEntry<BlockStateProviderType<SupplierBlockStateProvider>> SUPPLIER_BLOCK_STATE_PROVIDER = null;

  public ShadedSupplierBlockStateProviderComponent(CustomRegistrate registrate) {
    super(ClassUtil.convertClass(BlockStateProviderType.class), registrate);
  }

  @Override
  public void init() {
    if (SUPPLIER_BLOCK_STATE_PROVIDER == null) {
      SUPPLIER_BLOCK_STATE_PROVIDER = registrate.simple("supplier_block_state_provider", BlockStateProviderType.class, () -> new BlockStateProviderType<>(SupplierBlockStateProvider.CODEC));
    }
  }

  @Override
  public void register() {
    SupplierBlockStateProvider.type = SUPPLIER_BLOCK_STATE_PROVIDER.get();
  }
}
