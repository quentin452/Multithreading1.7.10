package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.world.gen.layer.IntCache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IntCache.class)
public class MixinIntCache {

    @Shadow
    private static int intCacheSize = 256;
    /** A list of pre-allocated int[256] arrays that are currently unused and can be returned by getIntCache() */
    @Unique
    private static Queue<int[]> optimizationsAndTweaks$freeSmallArrays = new ConcurrentLinkedQueue<>();
    /**
     * A list of pre-allocated int[256] arrays that were previously returned by getIntCache() and which will not be re-
     * used again until resetIntCache() is called.
     */
    @Unique
    private static Queue<int[]> optimizationsAndTweaks$inUseSmallArrays = new ConcurrentLinkedQueue<>();
    /** A list of pre-allocated int[cacheSize] arrays that are currently unused and can be returned by getIntCache() */
    @Unique
    private static Queue<int[]> optimizationsAndTweaks$freeLargeArrays = new ConcurrentLinkedQueue<>();
    /**
     * A list of pre-allocated int[cacheSize] arrays that were previously returned by getIntCache() and which will not
     * be re-used again until resetIntCache() is called.
     */
    @Unique
    private static Queue<int[]> optimizationsAndTweaks$inUseLargeArrays = new ConcurrentLinkedQueue<>();

    @Overwrite
    public static synchronized int[] getIntCache(int p_76445_0_) {
        int[] aint;

        if (p_76445_0_ <= 256) {
            if (optimizationsAndTweaks$freeSmallArrays.isEmpty()) {
                aint = new int[256];
                optimizationsAndTweaks$inUseSmallArrays.add(aint);
                return aint;
            } else {
                aint = optimizationsAndTweaks$freeSmallArrays.poll();
                if (aint == null) {
                    aint = new int[256];
                    optimizationsAndTweaks$inUseSmallArrays.add(aint);
                }
                return aint;
            }
        } else if (p_76445_0_ > intCacheSize) {
            intCacheSize = p_76445_0_;
            optimizationsAndTweaks$freeLargeArrays.clear();
            optimizationsAndTweaks$inUseLargeArrays.clear();
            aint = new int[intCacheSize];
            optimizationsAndTweaks$inUseLargeArrays.add(aint);
            return aint;
        } else if (optimizationsAndTweaks$freeLargeArrays.isEmpty()) {
            aint = new int[intCacheSize];
            optimizationsAndTweaks$inUseLargeArrays.add(aint);
            return aint;
        } else {
            aint = optimizationsAndTweaks$freeLargeArrays.poll();
            if (aint == null) {
                aint = new int[intCacheSize];
                optimizationsAndTweaks$inUseLargeArrays.add(aint);
            }
            return aint;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static synchronized void resetIntCache() {
        optimizationsAndTweaks$freeLargeArrays.addAll(optimizationsAndTweaks$inUseLargeArrays);
        optimizationsAndTweaks$inUseLargeArrays.clear();

        optimizationsAndTweaks$freeSmallArrays.addAll(optimizationsAndTweaks$inUseSmallArrays);
        optimizationsAndTweaks$inUseSmallArrays.clear();
    }
}
