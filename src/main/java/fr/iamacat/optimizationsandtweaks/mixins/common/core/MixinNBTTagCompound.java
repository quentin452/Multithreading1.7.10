package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import fr.iamacat.optimizationsandtweaks.utils.concurrentlinkedhashmap.ConcurrentHashMapV8;

@Mixin(NBTTagCompound.class)
public abstract class MixinNBTTagCompound extends NBTBase {

    @Shadow
    private Map tagMap = new ConcurrentHashMapV8();

    @Shadow
    public boolean hasKey(String key) {
        return this.tagMap.containsKey(key);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean hasKey(String key, int type) {
        byte tagType = this.func_150299_b(key);

        if (type == 99) {
            return tagType == 1 || tagType == 2 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6;
        }

        return tagType == type;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public byte func_150299_b(String key) {
        NBTBase nbtbase = (NBTBase) this.tagMap.get(key);
        return nbtbase != null ? nbtbase.getId() : 0;
    }

    /**
     * @author
     * @reason
     */
    @Override
    public NBTBase copy() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        for (Map.Entry<String, NBTBase> entry : (Iterable<Map.Entry<String, NBTBase>>) this.tagMap.entrySet()) {
            String key = entry.getKey();
            NBTBase value = entry.getValue();
            nbttagcompound.setTag(key, value.copy());
        }

        return nbttagcompound;
    }
}
