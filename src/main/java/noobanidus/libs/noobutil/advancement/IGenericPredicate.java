package noobanidus.libs.noobutil.advancement;

import com.google.gson.JsonElement;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public interface IGenericPredicate<T> {
  boolean test(ServerPlayer player, T condition);

  IGenericPredicate<T> deserialize(@Nullable JsonElement element);
}
