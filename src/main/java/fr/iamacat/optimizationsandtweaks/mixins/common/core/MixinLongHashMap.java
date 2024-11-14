package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.LongHashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.mixins.Classers;

@Mixin(LongHashMap.class)
public class MixinLongHashMap {

    @Unique
    private transient Classers.Entry[] optimizationsAndTweaks$hashArray = new Classers.Entry[16];
    @Unique
    private transient final AtomicInteger optimizationsAndTweaks$modCount = new AtomicInteger();
    @Shadow
    private transient int numHashElements;
    @Shadow
    private int capacity = 12;
    @Shadow
    private final float percentUseable = 0.75F;

    @Overwrite
    private static int hash(int p_76157_0_) {
        p_76157_0_ ^= p_76157_0_ >>> 20 ^ p_76157_0_ >>> 12;
        return p_76157_0_ ^ p_76157_0_ >>> 7 ^ p_76157_0_ >>> 4;
    }

    @Overwrite
    private static int getHashIndex(int p_76158_0_, int p_76158_1_) {
        return p_76158_0_ & p_76158_1_ - 1;
    }

    @Shadow
    public int getNumHashElements() {
        return this.numHashElements;
    }

    @Overwrite
    public Object getValueByKey(long p_76164_1_) {
        int index = getHashIndex(Classers.Entry.getHashedKey(p_76164_1_), this.optimizationsAndTweaks$hashArray.length);
        Classers.Entry entry = this.optimizationsAndTweaks$hashArray[index];

        while (entry != null) {
            if (entry.key == p_76164_1_) {
                return entry.value;
            }
            entry = entry.nextEntry;
        }

        return null;
    }

    @Overwrite
    public boolean containsItem(long p_76161_1_) {
        int index = getHashIndex(Classers.Entry.getHashedKey(p_76161_1_), this.optimizationsAndTweaks$hashArray.length);
        return optimizationsAndTweaks$getEntry(index, p_76161_1_) != null;
    }

    @Unique
    final Classers.Entry optimizationsAndTweaks$getEntry(int index, long key) {
        Classers.Entry entry = this.optimizationsAndTweaks$hashArray[index];

        while (entry != null) {
            if (entry.key == key) {
                return entry;
            }
            entry = entry.nextEntry;
        }

        return null;
    }

    @Overwrite
    public void add(long p_76163_1_, Object p_76163_3_) {
        int index = getHashIndex(Classers.Entry.getHashedKey(p_76163_1_), this.optimizationsAndTweaks$hashArray.length);
        Classers.Entry entry = this.optimizationsAndTweaks$hashArray[index];

        while (entry != null) {
            if (entry.key == p_76163_1_) {
                entry.value = p_76163_3_;
                return;
            }
            entry = entry.nextEntry;
        }

        this.optimizationsAndTweaks$modCount.incrementAndGet();
        this.createKey(Classers.Entry.getHashedKey(p_76163_1_), p_76163_1_, p_76163_3_, index);
    }

    @Overwrite
    private void resizeTable(int p_76153_1_) {
        Classers.Entry[] oldArray = this.optimizationsAndTweaks$hashArray;
        int oldCapacity = oldArray.length;

        if (oldCapacity == 1073741824) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            Classers.Entry[] newArray = new Classers.Entry[p_76153_1_];
            this.optimizationsAndTweaks$copyHashTableTo(newArray);
            this.optimizationsAndTweaks$hashArray = newArray;
            this.capacity = (int) (p_76153_1_ * this.percentUseable);
        }
    }

    @Unique
    private void optimizationsAndTweaks$copyHashTableTo(Classers.Entry[] p_76154_1_) {
        Classers.Entry[] oldArray = this.optimizationsAndTweaks$hashArray;
        int oldCapacity = oldArray.length;

        for (int i = 0; i < oldCapacity; ++i) {
            Classers.Entry entry = oldArray[i];

            if (entry != null) {
                oldArray[i] = null;
                Classers.Entry nextEntry;

                do {
                    nextEntry = entry.nextEntry;
                    int index = getHashIndex(entry.hash, p_76154_1_.length);
                    entry.nextEntry = p_76154_1_[index];
                    p_76154_1_[index] = entry;
                    entry = nextEntry;
                } while (nextEntry != null);
            }
        }
    }

    @Overwrite
    public Object remove(long p_76159_1_) {
        Classers.Entry entry = this.optimizationsAndTweaks$removeKey(p_76159_1_);
        return entry == null ? null : entry.value;
    }

    @Unique
    final Classers.Entry optimizationsAndTweaks$removeKey(long p_76152_1_) {
        int index = getHashIndex(Classers.Entry.getHashedKey(p_76152_1_), this.optimizationsAndTweaks$hashArray.length);
        Classers.Entry entry = this.optimizationsAndTweaks$hashArray[index];
        Classers.Entry prevEntry = null;

        while (entry != null) {
            Classers.Entry nextEntry = entry.nextEntry;

            if (entry.key == p_76152_1_) {
                this.optimizationsAndTweaks$modCount.incrementAndGet();
                --this.numHashElements;

                if (prevEntry == null) {
                    this.optimizationsAndTweaks$hashArray[index] = nextEntry;
                } else {
                    prevEntry.nextEntry = nextEntry;
                }

                return entry;
            }

            prevEntry = entry;
            entry = nextEntry;
        }

        return null;
    }

    @Overwrite
    private void createKey(int p_76156_1_, long p_76156_2_, Object p_76156_4_, int p_76156_5_) {
        Classers.Entry entry = this.optimizationsAndTweaks$hashArray[p_76156_5_];
        this.optimizationsAndTweaks$hashArray[p_76156_5_] = new Classers.Entry(
            p_76156_1_,
            p_76156_2_,
            p_76156_4_,
            entry);

        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.optimizationsAndTweaks$hashArray.length);
        }
    }
}
