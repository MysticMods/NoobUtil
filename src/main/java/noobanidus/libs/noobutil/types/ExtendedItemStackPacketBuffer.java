package noobanidus.libs.noobutil.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.io.IOException;

/*
  Contains code from the public domain mod Dank Storage
  Used with permission from Tfarecnim
  Original code: https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/utils/PacketBufferEX.java
 */

public class ExtendedItemStackPacketBuffer extends PacketBuffer {
  public ExtendedItemStackPacketBuffer(ByteBuf wrapped) {
    super(wrapped);
  }

  public void writeExtendedItemStack(ItemStack stack) {
    if (stack.isEmpty()) {
      writeInt(-1);
    } else {
      writeInt(Item.getId(stack.getItem()));
      writeInt(stack.getCount());
      CompoundNBT nbttagcompound = null;

      if (stack.getItem().getShareTag(stack) != null) {
        nbttagcompound = stack.getItem().getShareTag(stack);
      }

      writeNBT(nbttagcompound);
    }
  }

  public void writeNBT(@Nullable CompoundNBT nbt) {
    if (nbt == null) {
      writeByte(0);
    } else {
      try {
        CompressedStreamTools.write(nbt, new ByteBufOutputStream(this.getBuffer()));
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

  public CompoundNBT readNBT() {
    int i = readerIndex();
    byte b0 = readByte();

    if (b0 == 0) {
      return null;
    } else {
      readerIndex(i);
      try {
        return CompressedStreamTools.read(new ByteBufInputStream(getBuffer()), new NBTSizeTracker(2097152L));
      } catch (IOException ioexception) {
        throw new EncoderException(ioexception);
      }
    }
  }
}
