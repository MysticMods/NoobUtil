package noobanidus.libs.noobutil.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;

public class ItemModelGenerator {
  public static <T extends Item> ItemModelBuilder itemModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.blockItem(ctx::getEntry);
  }

  public static <T extends Item> ItemModelBuilder inventoryModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.blockWithInventoryModel(ctx::getEntry);
  }

  public static <T extends Item> ItemModelBuilder generated(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.generated(ctx::getEntry, p.modLoc("block/" + p.name(ctx::getEntry)));
  }

  public static <T extends Item> ItemModelBuilder complexItemModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.withExistingParent(p.name(ctx::getEntry), new ResourceLocation(p.modid(ctx::getEntry), "block/complex/" + p.name(ctx::getEntry)));
  }
}
