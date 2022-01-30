package noobanidus.libs.noobutil.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.io.IOException;

/*
  Contains code from the public domain mod Dank Storage
  Used with permission from Tfarecnim
  Original code: https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/utils/PacketBufferEX.java
 */

public class ExtendedItemStackPacketBuffer extends FriendlyByteBuf {
  public ExtendedItemStackPacketBuffer(ByteBuf wrapped) {
    super(wrapped);
  }

  public void writeExtendedItemStack(ItemStack stack) {
    if (stack.isEmpty()) {
      writeInt(-1);
    } else {
      writeInt(Item.getId(stack.getItem()));
      writeInt(stack.getCount());
      CompoundTag nbttagcompound = null;

      if (stack.getItem().getShareTag(stack) != null) {
        nbttagcompound = stack.getItem().getShareTag(stack);
      }

      writeNBT(nbttagcompound);
    }
  }

  public void writeNBT(@Nullable CompoundTag nbt) {
    if (nbt == null) {
      writeByte(0);
    } else {
      try {
        NbtIo.write(nbt, new ByteBufOutputStream(this.getBuffer()));
      } catch (IOException ioexception) {
        throw new EncoderException(ioexception);
      }
    }
  }

  public ItemStack readExtendedItemStack() {
    int i = readInt();

    if (i < 0) {
      return ItemStack.EMPTY;
    } else {
      int j = readInt();
      ItemStack itemstack = new ItemStack(Item.byId(i), j);
      itemstack.setTag(readNBT());
      return itemstack;
    }
  }

  public CompoundTag readNBT() {
    int i = readerIndex();
    byte b0 = readByte();

    if (b0 == 0) {
      return null;
    } else {
      readerIndex(i);
      try {
        return NbtIo.read(new ByteBufInputStream(getBuffer()), new NbtAccounter(2097152L));
      } catch (IOException ioexception) {
        throw new EncoderException(ioexception);
      }
    }
  }
}
