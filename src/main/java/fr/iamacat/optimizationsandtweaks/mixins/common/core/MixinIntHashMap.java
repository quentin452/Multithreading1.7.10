package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.IntHashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.mixins.Classers;

@Mixin(IntHashMap.class)
public class MixinIntHashMap {

    /** An array of HashEntries representing the heads of hash slot lists */
    @Unique
    private transient Classers.EntryIntHashMap[] optimizationsAndTweaks$slots = new Classers.EntryIntHashMap[16];
    /** The number of items stored in this map */
    @Shadow
    private transient int count;
    /** The grow threshold */
    @Shadow
    private int threshold = 12;

    /** The scale factor used to determine when to grow the table */
    @Shadow
    private final float growFactor = 0.75F;
    /** A serial stamp used to mark changes */
    @Shadow
    private transient volatile int versionStamp;
    /** The set of all the keys stored in this MCHash object */
    @Shadow
    private Set keySet = new HashSet();

    /**
     * Computes the index of the slot for the hash and slot count passed in.
     */
    @Shadow
    private static int getSlotIndex(int p_76043_0_, int p_76043_1_) {
        return p_76043_0_ & p_76043_1_ - 1;
    }

    /**
     * Returns the object associated to a key
     */
    @Overwrite
    public Object lookup(int p_76041_1_) {
        int j = Classers.EntryIntHashMap.computeHash(p_76041_1_);

        for (Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$slots[getSlotIndex(
            j,
            this.optimizationsAndTweaks$slots.length)]; entry != null; entry = entry.nextEntry) {
            if (entry.hashEntry == p_76041_1_) {
                return entry.valueEntry;
            }
        }

        return null;
    }

    /**
     * Return true if an object is associated with the given key
     */
    @Shadow
    public boolean containsItem(int p_76037_1_) {
        return this.optimizationsAndTweaks$lookupEntry(p_76037_1_) != null;
    }

    /**
     * Returns the key/object mapping for a given key as a MCHashEntry
     */
    @Unique
    final Classers.EntryIntHashMap optimizationsAndTweaks$lookupEntry(int p_76045_1_) {
        int j = Classers.EntryIntHashMap.computeHash(p_76045_1_);

        for (Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$slots[getSlotIndex(
            j,
            this.optimizationsAndTweaks$slots.length)]; entry != null; entry = entry.nextEntry) {
            if (entry.hashEntry == p_76045_1_) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Adds a key and associated value to this map
     */
    @Overwrite
    public void addKey(int p_76038_1_, Object p_76038_2_) {
        this.keySet.add(p_76038_1_);
        int j = Classers.EntryIntHashMap.computeHash(p_76038_1_);
        int k = getSlotIndex(j, this.optimizationsAndTweaks$slots.length);

        for (Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$slots[k]; entry
            != null; entry = entry.nextEntry) {
            if (entry.hashEntry == p_76038_1_) {
                entry.valueEntry = p_76038_2_;
                return;
            }
        }

        ++this.versionStamp;
        this.insert(j, p_76038_1_, p_76038_2_, k);
    }

    /**
     * Increases the number of hash slots
     */
    @Overwrite
    private void grow(int p_76047_1_) {
        Classers.EntryIntHashMap[] aentry = this.optimizationsAndTweaks$slots;
        int j = aentry.length;

        if (j == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            Classers.EntryIntHashMap[] aentry1 = new Classers.EntryIntHashMap[p_76047_1_];
            this.optimizationsAndTweaks$copyTo(aentry1);
            this.optimizationsAndTweaks$slots = aentry1;
            this.threshold = (int) (p_76047_1_ * this.growFactor);
        }
    }

    /**
     * Copies the hash slots to a new array
     */
    @Unique
    private void optimizationsAndTweaks$copyTo(Classers.EntryIntHashMap[] p_76048_1_) {
        Classers.EntryIntHashMap[] aentry = this.optimizationsAndTweaks$slots;
        int i = p_76048_1_.length;

        for (int j = 0; j < aentry.length; ++j) {
            Classers.EntryIntHashMap entry = aentry[j];

            if (entry != null) {
                aentry[j] = null;
                Classers.EntryIntHashMap entry1;

                do {
                    entry1 = entry.nextEntry;
                    int k = getSlotIndex(entry.slotHash, i);
                    entry.nextEntry = p_76048_1_[k];
                    p_76048_1_[k] = entry;
                    entry = entry1;
                } while (entry1 != null);
            }
        }
    }

    /**
     * Removes the specified object from the map and returns it
     */
    @Overwrite
    public Object removeObject(int p_76049_1_) {
        this.keySet.remove(Integer.valueOf(p_76049_1_));
        Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$removeEntry(p_76049_1_);
        return entry == null ? null : entry.valueEntry;
    }

    /**
     * Removes the specified entry from the map and returns it
     */
    @Unique
    final Classers.EntryIntHashMap optimizationsAndTweaks$removeEntry(int p_76036_1_) {
        int j = Classers.EntryIntHashMap.computeHash(p_76036_1_);
        int k = getSlotIndex(j, this.optimizationsAndTweaks$slots.length);
        Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$slots[k];
        Classers.EntryIntHashMap entry1;
        Classers.EntryIntHashMap entry2;

        for (entry1 = entry; entry1 != null; entry1 = entry2) {
            entry2 = entry1.nextEntry;

            if (entry1.hashEntry == p_76036_1_) {
                ++this.versionStamp;
                --this.count;

                if (entry == entry1) {
                    this.optimizationsAndTweaks$slots[k] = entry2;
                } else {
                    entry.nextEntry = entry2;
                }

                return entry1;
            }

            entry = entry1;
        }

        return entry1;
    }

    /**
     * Removes all entries from the map
     */
    @Overwrite
    public void clearMap() {
        ++this.versionStamp;
        Classers.EntryIntHashMap[] aentry = this.optimizationsAndTweaks$slots;

        Arrays.fill(aentry, null);

        this.count = 0;
    }

    /**
     * Adds an object to a slot
     */
    @Overwrite
    private void insert(int p_76040_1_, int p_76040_2_, Object p_76040_3_, int p_76040_4_) {
        Classers.EntryIntHashMap entry = this.optimizationsAndTweaks$slots[p_76040_4_];
        this.optimizationsAndTweaks$slots[p_76040_4_] = new Classers.EntryIntHashMap(
            p_76040_1_,
            p_76040_2_,
            p_76040_3_,
            entry);

        if (this.count++ >= this.threshold) {
            this.grow(2 * this.optimizationsAndTweaks$slots.length);
        }
    }
}
