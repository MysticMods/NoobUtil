package noobanidus.libs.noobutil.tracking;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import noobanidus.libs.noobutil.network.AbstractNetworkObject;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ItemTracking extends AbstractNetworkObject<PacketBuffer> {
  private static final Set<String> DAMAGE_SET = Sets.newHashSet("Damage");
  private final Map<Item, TrackingEntry> trackingMap = new HashMap<>();

  public ItemTracking() {
  }

  public boolean track(ItemStack item, UUID blockId, int slotIndex) {
    return trackingMap.computeIfAbsent(item.getItem(), k -> new TrackingEntry(item.getItem())).trackItem(item, blockId, slotIndex);
  }

  @Nullable
  public TrackingEntry trackingFor(ItemStack item) {
    return trackingFor(item.getItem());
  }

  @Nullable
  public TrackingEntry trackingFor(Item item) {
    return trackingMap.get(item);
  }

  public void clear() {
    trackingMap.clear();
  }

  public void clear(ItemStack item) {
    clear(item.getItem());
  }

  public void clear(Item item) {
    TrackingEntry entry = trackingMap.get(item);
    if (entry != null) {
      entry.clear();
    }
  }

  @Override
  public void serialize(PacketBuffer buffer) {
    buffer.writeVarInt(trackingMap.size());
    for (TrackingEntry entry : trackingMap.values()) {
      entry.serialize(buffer);
    }
  }

  public static ItemTracking deserialize (PacketBuffer buffer) {
    ItemTracking tracking = new ItemTracking();
    int entryCount = buffer.readVarInt();
    for (int i = 0; i < entryCount; i++){
      TrackingEntry thisEntry = TrackingEntry.deserialize(buffer);
      TrackingEntry existing = tracking.trackingMap.get(thisEntry.getCanonicalItem());
      if (existing != null) {
        existing.combine(thisEntry);
      } else {
        tracking.trackingMap.put(thisEntry.getCanonicalItem(), thisEntry);
      }
    }
    return tracking;
  }

  public static class TrackingEntry extends AbstractNetworkObject<PacketBuffer> implements Iterable<ItemEntry> {
    private final ResourceLocation location;
    private final Item canonicalItem;
    private final ItemStack canonicalStack;

    private final Set<ItemEntry> base = new HashSet<>();
    private final Int2ObjectMap<Set<ItemEntry>> damageMap = new Int2ObjectOpenHashMap<>();
    private final List<NBTEntry> nbtMap = new ArrayList<>();

    public TrackingEntry(Item canonicalItem) {
      this.canonicalItem = canonicalItem;
      this.canonicalStack = new ItemStack(canonicalItem);
      this.location = canonicalItem.getRegistryName();
    }

    public void clear() {
      base.clear();
      damageMap.clear();
      nbtMap.clear();
    }

    public Iterator<ItemEntry> iterator() {
      Queue<Iterator<ItemEntry>> queue = new ArrayDeque<>();
      queue.add(base.iterator());
      damageMap.values().forEach(o -> queue.add(o.iterator()));
      nbtMap.forEach(o -> queue.add(o.getEntries().iterator()));
      return Iterators.concat(new AbstractIterator<Iterator<ItemEntry>>() {
        @Override
        protected Iterator<ItemEntry> computeNext() {
          return queue.isEmpty() ? endOfData() : queue.remove();
        }
      });
    }

    public ResourceLocation getLocation() {
      return location;
    }

    public Item getCanonicalItem() {
      return canonicalItem;
    }

    public ItemStack getCanonicalStack() {
      return canonicalStack;
    }

    public boolean trackItem(ItemStack stack, UUID blockId, int slotIndex) {
      if (!stack.sameItem(canonicalStack)) {
        return false;
      }

      CompoundNBT tag = stack.getTag();

      if (!stack.hasTag() || tag == null) {
        base.add(new ItemEntry(blockId, getCanonicalStack(), stack.getCount(), slotIndex));
      } else {
        if (DAMAGE_SET.containsAll(tag.getAllKeys())) {
          damageMap.computeIfAbsent(stack.getDamageValue(), (k) -> new HashSet<>()).add(new ItemEntry(blockId, stack.copy(), stack.getCount(), slotIndex));
        } else {
          for (NBTEntry entry : nbtMap) {
            ItemStack nbtStack = entry.getCanonicalStack();
            if (ItemUtil.equalWithoutSize(stack, nbtStack)) {
              entry.addEntry(new ItemEntry(blockId, entry.getCanonicalStack(), stack.getCount(), slotIndex));
              return true;
            }
          }
          NBTEntry newEntry = new NBTEntry(stack.copy(), new HashSet<>());
          newEntry.addEntry(new ItemEntry(blockId, newEntry.getCanonicalStack(), stack.getCount(), slotIndex));
          nbtMap.add(newEntry);
        }
      }
      return true;
    }

    @Override
    public void serialize(PacketBuffer buffer) {
      buffer.writeVarInt(Item.getId(canonicalItem));
      buffer.writeVarInt(base.size());
      ItemEntry.lastUUID = null;
      List<ItemEntry> sorted = base.stream().sorted(Comparator.comparing(ItemEntry::getIdentifier, UUID::compareTo)).collect(Collectors.toList());
      sorted.forEach(item -> item.serialize(buffer));
      buffer.writeVarInt(damageMap.size());
      for (Int2ObjectMap.Entry<Set<ItemEntry>> entryMap : damageMap.int2ObjectEntrySet()) {
        buffer.writeVarInt(entryMap.getIntKey());
        buffer.writeVarInt(entryMap.getValue().size());
        ItemEntry.lastUUID = null;
        sorted = entryMap.getValue().stream().sorted(Comparator.comparing(ItemEntry::getIdentifier, UUID::compareTo)).collect(Collectors.toList());
        sorted.forEach(item -> item.serialize(buffer));
      }
      buffer.writeVarInt(nbtMap.size());
      for (NBTEntry nbtEntry : nbtMap) {
        buffer.writeItem(nbtEntry.getCanonicalStack());
        buffer.writeVarInt(nbtEntry.getEntries().size());
        ItemEntry.lastUUID = null;
        sorted = nbtEntry.getEntries().stream().sorted(Comparator.comparing(ItemEntry::getIdentifier, UUID::compareTo)).collect(Collectors.toList());
        sorted.forEach(item -> item.serialize(buffer));
      }
    }

    public static TrackingEntry deserialize(PacketBuffer buffer) {
      TrackingEntry entry = new TrackingEntry(Item.byId(buffer.readVarInt()));
      int baseCount = buffer.readVarInt();
      for (int i = 0; i < baseCount; i++) {
        entry.base.add(ItemEntry.deserialize(buffer, entry.getCanonicalStack()));
      }
      int damageCount = buffer.readVarInt();
      for (int i = 0; i < damageCount; i++) {
        int damage = buffer.readVarInt();
        int damagedCount = buffer.readVarInt();
        ItemStack canonical = entry.getCanonicalStack().copy();
        canonical.setDamageValue(damage);
        for (int j = 0; j < damagedCount; j++) {
          entry.damageMap.computeIfAbsent(damage, (k) -> new HashSet<>()).add(ItemEntry.deserialize(buffer, canonical));
        }
      }
      int nbtCount = buffer.readVarInt();
      for (int i = 0; i < nbtCount; i++) {
        ItemStack nbtItem = buffer.readItem();
        NBTEntry nbtEntry = new NBTEntry(nbtItem, new HashSet<>());
        int nbtEntryCount = buffer.readVarInt();
        for (int j = 0; j < nbtEntryCount; j++) {
          nbtEntry.entries.add(ItemEntry.deserialize(buffer, nbtItem));
        }
      }
      return entry;
    }

    public void combine(TrackingEntry other) {
      this.base.addAll(other.base);
      for (Int2ObjectMap.Entry<Set<ItemEntry>> entry : other.damageMap.int2ObjectEntrySet()) {
        damageMap.computeIfAbsent(entry.getIntKey(), (k) -> new HashSet<>()).addAll(entry.getValue());
      }
      for (NBTEntry otherEntry : other.nbtMap) {
        for (NBTEntry myEntry : nbtMap) {
          myEntry.combine(otherEntry);
        }
      }
    }
  }

  public static class ItemEntry extends AbstractNetworkObject<PacketBuffer> {
    private final UUID identifier;
    private final ItemStack canonicalStack;
    private final int count;
    private final int slot;

    public ItemEntry(UUID identifier, ItemStack canonicalStack, int count, int slot) {
      this.identifier = identifier;
      this.canonicalStack = canonicalStack;
      this.count = count;
      this.slot = slot;
    }

    public UUID getIdentifier() {
      return identifier;
    }

    public ItemStack getCanonicalStack() {
      return canonicalStack;
    }

    public int getCount() {
      return count;
    }

    public int getSlot() {
      return slot;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ItemEntry itemEntry = (ItemEntry) o;

      if (count != itemEntry.count) return false;
      if (slot != itemEntry.slot) return false;
      return identifier.equals(itemEntry.identifier);
    }

    @Override
    public int hashCode() {
      int result = identifier.hashCode();
      result = 31 * result + count;
      result = 31 * result + slot;
      return result;
    }

    public static UUID lastUUID = null;

    @Override
    public void serialize(PacketBuffer buffer) {
      if (lastUUID != null && lastUUID.equals(getIdentifier())) {
        buffer.writeBoolean(true);
        lastUUID = getIdentifier();
      } else {
        buffer.writeBoolean(false);
        buffer.writeUUID(getIdentifier());
      }
      buffer.writeVarInt(getCount());
      buffer.writeVarInt(getSlot());
    }

    public static ItemEntry deserialize(PacketBuffer buffer, ItemStack canonicalStack) {
      if (!buffer.readBoolean()) {
        lastUUID = buffer.readUUID();
      }
      return new ItemEntry(lastUUID, canonicalStack, buffer.readVarInt(), buffer.readVarInt());
    }
  }

  public static class NBTEntry {
    private final ItemStack canonicalStack;
    private final Set<ItemEntry> entries;

    public NBTEntry(ItemStack canonicalStack, Set<ItemEntry> entries) {
      this.canonicalStack = canonicalStack;
      this.entries = entries;
    }

    public ItemStack getCanonicalStack() {
      return canonicalStack;
    }

    public Set<ItemEntry> getEntries() {
      return entries;
    }

    public void addEntry(ItemEntry entry) {
      this.entries.add(entry);
    }

    public void combine(NBTEntry entry) {
      if (ItemUtil.equalWithoutSize(getCanonicalStack(), entry.getCanonicalStack())) {
        this.entries.addAll(entry.getEntries());
      }
    }
  }
}
