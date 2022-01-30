package noobanidus.libs.noobutil.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PacketHandler {

  private final String PROTOCOL_VERSION = Integer.toString(2);
  private short index = 0;

  public final SimpleChannel HANDLER;

  public PacketHandler(String modid) {
    this.HANDLER = NetworkRegistry.ChannelBuilder
        .named(new ResourceLocation(modid, "main_network_channel"))
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .simpleChannel();
  }

  private int id = 0;

  public abstract void registerMessages();

  public void sendToInternal(Object msg, ServerPlayer player) {
    if (!(player instanceof FakePlayer))
      HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }

  public void sendToServerInternal(Object msg) {
    HANDLER.sendToServer(msg);
  }

  public <MSG> void sendInternal(PacketDistributor.PacketTarget target, MSG message) {
    HANDLER.send(target, message);
  }

  public <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
    HANDLER.registerMessage(index, messageType, encoder, decoder, messageConsumer);
    index++;
    if (index > 0xFF)
      throw new RuntimeException("Too many messages!");
  }
}