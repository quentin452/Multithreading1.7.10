package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExtendedBlockStorage.class)
public class MixinExtendedBlockStorage {

    @Shadow
    private NibbleArray blockMSBArray;
    @Shadow
    private byte[] blockLSBArray;
    // the map introduce TPS overhead so for now not enabled

    // @Unique
    // private Object2ObjectHashMap<Long, Block> optimizationsAndTweaks$blockIdCache = new Object2ObjectHashMap<>();

    /**
     * @author
     * @reason
     */
    // @Overwrite
    /*
     * public Block getBlockByExtId(int x, int y, int z) {
     * long cacheKey = (long) x << 32 | (long) y << 16 | z;
     * if (optimizationsAndTweaks$blockIdCache.containsKey(cacheKey)) {
     * return optimizationsAndTweaks$blockIdCache.get(cacheKey);
     * }
     * int l = this.blockLSBArray[y << 8 | z << 4 | x] & 255;
     * if (this.blockMSBArray != null) {
     * l |= this.blockMSBArray.get(x, y, z) << 8;
     * }
     * Block block = Block.getBlockById(l);
     * optimizationsAndTweaks$blockIdCache.put(cacheKey, block);
     * return block;
     * }
     */
}
