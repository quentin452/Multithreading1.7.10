package fr.iamacat.optimizationsandtweaks.eventshandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.falsepattern.lib.compat.ChunkPos;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.iamacat.optimizationsandtweaks.utils.agrona.collections.Object2ObjectHashMap;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.tidychunkbackport.TidyChunkBackportWorldContext;

public class TidyChunkBackportEventHandler {
    // Remove almost all EntityItem during initial chunk generation.
    // To prevent lags caused by a large amount of EntityItem on mod packs.
    // Backport of Tidy Chunk mod from 1.12.2 to 1.7.10.
    // Tidy Chunk Backport Version V1.0 (stable)
    // todo Need bugfixes/optimizations/redundant code remover/Map Changer
    // todo prevent EntityItem to be dropped from the player if removeOldContext is not terminated

    // Map to store world contexts
    public static final Object2ObjectHashMap<Integer, TidyChunkBackportWorldContext> worldData = new Object2ObjectHashMap<>();

    // Event handler for world unload
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldUnload(WorldEvent.Unload evt) {
        World w = evt.world;
        if (!w.isRemote) {
            getWorldContext(w).searchAndDestroy(w);
            worldData.remove(w.provider.dimensionId);
        }
    }

    // Event handler for chunk population
    @SubscribeEvent
    public void onChunkPopulate(PopulateChunkEvent.Pre evt) {
        TidyChunkBackportWorldContext ctx = getWorldContext(evt.world);
        ChunkPos pos = new ChunkPos(evt.chunkX, evt.chunkZ);
        ctx.add(pos, evt.world);
    }

    // Event handler for entity join world
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent evt) {
        Entity entity = evt.entity;
        World world = entity.worldObj;

        if (world == null || world.isRemote || !TidyChunkBackportWorldContext.isTargetEntity(entity)) {
            return;
        }

        EntityItem itemEntity = (EntityItem) entity;

        final TidyChunkBackportWorldContext ctx = getWorldContext(world);

        if (ctx.isContained(itemEntity)) {
            ctx.removeEntity(entity, world);
            evt.setCanceled(true);
        }
    }

    // Create a new world context if not exists
    private static TidyChunkBackportWorldContext createWorldContext(World w) {
        TidyChunkBackportWorldContext ctx;
        worldData.put(w.provider.dimensionId, ctx = new TidyChunkBackportWorldContext());
        return ctx;
    }

    // Get the world context for a given world
    private static TidyChunkBackportWorldContext getWorldContext(World w) {
        return worldData.computeIfAbsent(w.provider.dimensionId, k -> createWorldContext(w));
    }

    // Inject the tidy chunk logic in the world tick
    public static void injectInWorldTick(World world) {
        TidyChunkBackportWorldContext ctx = getWorldContext(world);

        if (ctx != null) {
            ctx.searchAndDestroy(world);
            ctx.removeOldContext(world);
        }
    }
}
