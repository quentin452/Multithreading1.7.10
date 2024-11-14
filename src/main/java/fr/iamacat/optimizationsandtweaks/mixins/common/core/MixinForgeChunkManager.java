package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.cache.Cache;
import com.google.common.collect.MapMaker;

@Mixin(ForgeChunkManager.class)
public class MixinForgeChunkManager {

    @Shadow
    private static Map<World, Cache<Long, Chunk>> dormantChunkCache = new MapMaker().weakKeys()
        .makeMap();

    @Overwrite

    @SuppressWarnings("unchecked")
    public synchronized static Chunk fetchDormantChunk(long coords, World world) {
        Cache<Long, Chunk> cache = dormantChunkCache.get(world);
        if (cache == null) {
            return null;
        }
        Chunk chunk = cache.getIfPresent(coords);
        if (chunk != null) {
            for (List<Entity> eList : chunk.entityLists) {
                for (Entity e : eList) {
                    e.resetEntityId();
                }
            }
        }
        return chunk;
    }
}
