package noobanidus.libs.noobutil.tracking;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nullable;
import java.util.*;

public class ItemTracking<T> {
  private static final Set<String> DAMAGE_SET = Sets.newHashSet("Damage");
  private final Map<Item, TrackingEntry<T>> trackingMap = new HashMap<>();

  public ItemTracking() {
  }

  public boolean track(ItemStack item, T blockId, int slotIndex) {
    return trackingMap.computeIfAbsent(item.getItem(), k -> new TrackingEntry<>(item.getItem())).trackItem(item, blockId, slotIndex);
  }

  @Nullable
  public TrackingEntry<T> trackingFor (ItemStack item) {
    return trackingFor(item.getItem());
  }

  @Nullable
  public TrackingEntry<T> trackingFor (Item item) {
    return trackingMap.get(item);
  }

  public void clear () {
    trackingMap.clear();
  }

  public void clear (ItemStack item) {
    clear(item.getItem());
  }

  public void clear (Item item) {
    TrackingEntry<T> entry = trackingMap.get(item);
    if (entry != null) {
      entry.clear();
    }
  }

  public static class TrackingEntry<T> implements Iterable<ItemEntry<T>> {
    private final ResourceLocation location;
    private final Item canonicalItem;
    private final ItemStack canonicalStack;

    private final List<ItemEntry<T>> base = new ArrayList<>();
    private final Int2ObjectMap<ItemEntry<T>> damageMap = new Int2ObjectOpenHashMap<>();
    private final List<NBTEntry<T>> nbtMap = new ArrayList<>();

    public TrackingEntry(Item canonicalItem) {
      this.canonicalItem = canonicalItem;
      this.canonicalStack = new ItemStack(canonicalItem);
      this.location = canonicalItem.getRegistryName();
    }

    public void clear () {
      base.clear();
      damageMap.clear();
      nbtMap.clear();
    }

    public Iterator<ItemEntry<T>> iterator () {
      Queue<Iterator<ItemEntry<T>>> queue = new ArrayDeque<>();
      queue.add(base.iterator());
      queue.add(damageMap.values().iterator());
      nbtMap.forEach(o -> queue.add(o.getEntries().iterator()));
      return Iterators.concat(new AbstractIterator<Iterator<ItemEntry<T>>> () {
        @Override
        protected Iterator<ItemEntry<T>> computeNext() {
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

    public boolean trackItem(ItemStack stack, T blockId, int slotIndex) {
      if (!stack.sameItem(canonicalStack)) {
        return false;
      }

      CompoundNBT tag = stack.getTag();

      if (!stack.hasTag() || tag == null) {
        base.add(new ItemEntry<>(blockId, stack, stack.getCount(), slotIndex));
      } else {
        if (DAMAGE_SET.containsAll(tag.getAllKeys())) {
          damageMap.put(stack.getDamageValue(), new ItemEntry<>(blockId, stack, stack.getCount(), slotIndex));
        } else {
          for (NBTEntry<T> entry : nbtMap) {
            ItemStack nbtStack = entry.getCanonicalStack();
            if (ItemUtil.equalWithoutSize(stack, nbtStack)) {
              entry.addEntry(new ItemEntry<>(blockId, stack, stack.getCount(), slotIndex));
              return true;
            }
          }
          NBTEntry<T> newEntry = new NBTEntry<>(stack, new ArrayList<>());
          newEntry.addEntry(new ItemEntry<>(blockId, stack, stack.getCount(), slotIndex));
          nbtMap.add(newEntry);
        }
      }
      return true;
    }
  }

  public static class ItemEntry<UUID> {
    private final UUID identifier;
    private final ItemStack canonicalStack;
    private final int count;
    private final int slot;

    public ItemEntry(UUID identifier, ItemStack canonicalStack, int count, int slot) {
      this.identifier = identifier;
      this.canonicalStack = canonicalStack.copy();
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
  }

  public static class NBTEntry<T> {
    private final ItemStack canonicalStack;
    private final List<ItemEntry<T>> entries;

    public NBTEntry(ItemStack canonicalStack, List<ItemEntry<T>> entries) {
      this.canonicalStack = canonicalStack.copy();
      this.entries = entries;
    }

    public ItemStack getCanonicalStack() {
      return canonicalStack;
    }

    public List<ItemEntry<T>> getEntries() {
      return entries;
    }

    public void addEntry(ItemEntry<T> entry) {
      this.entries.add(entry);
    }
  }
}
