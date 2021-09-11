package noobanidus.libs.noobutil.shaded.component.deferred.state_provider;

import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraftforge.fml.RegistryObject;
import noobanidus.libs.noobutil.shaded.component.deferred.DeferredShadedComponent;
import noobanidus.libs.noobutil.shaded.registry.deferred.DeferredShadedRegistry;
import noobanidus.libs.noobutil.util.ClassUtil;
import noobanidus.libs.noobutil.world.gen.provider.SupplierBlockStateProvider;

public class ShadedSupplierBlockStateProviderComponent extends DeferredShadedComponent<BlockStateProviderType<?>> {
  public static RegistryObject<BlockStateProviderType<?>> SUPPLIER_BLOCK_STATE_PROVIDER = null;

  public ShadedSupplierBlockStateProviderComponent(String modid) {
    super(ClassUtil.convertClass(BlockStateProviderType.class), DeferredShadedRegistry.getOrCreate(ClassUtil.convertClass(BlockStateProviderType.class), modid));
  }

  @Override
  public void init() {
    if (SUPPLIER_BLOCK_STATE_PROVIDER == null) {
      SUPPLIER_BLOCK_STATE_PROVIDER = this.registry.getRegistry().register("supplier_block_state_provider", () -> new BlockStateProviderType<>(SupplierBlockStateProvider.CODEC));
    }
  }

  @Override
  public void register() {
    SupplierBlockStateProvider.type = SUPPLIER_BLOCK_STATE_PROVIDER.get();
  }
}
