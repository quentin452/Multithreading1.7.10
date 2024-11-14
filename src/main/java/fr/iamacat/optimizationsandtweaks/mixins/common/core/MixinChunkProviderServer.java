package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.chunkio.ChunkIOExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer implements IChunkProvider {

    @Shadow
    private static final Logger logger = LogManager.getLogger();
    /**
     * used by unload100OldestChunks to iterate the loadedChunkHashMap for unload (underlying assumption, first in,
     * first out)
     */
    @Shadow
    private Set chunksToUnload = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @Shadow
    private Chunk defaultEmptyChunk;
    @Shadow
    public IChunkProvider currentChunkProvider;
    @Shadow
    public IChunkLoader currentChunkLoader;
    /** if this is false, the defaultEmptyChunk will be returned by the provider */
    @Shadow
    public boolean loadChunkOnProvideRequest = true;
    @Shadow
    public LongHashMap loadedChunkHashMap = new LongHashMap();
    @Shadow
    public List loadedChunks = new ArrayList();
    @Shadow
    public WorldServer worldObj;
    @Shadow
    private final Set<Long> loadingChunks = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Shadow
    private Chunk safeLoadChunk(int p_73239_1_, int p_73239_2_) {
        if (this.currentChunkLoader == null) {
            return null;
        } else {
            try {
                Chunk chunk = this.currentChunkLoader.loadChunk(this.worldObj, p_73239_1_, p_73239_2_);

                if (chunk != null) {
                    chunk.lastSaveTime = this.worldObj.getTotalWorldTime();

                    if (this.currentChunkProvider != null) {
                        this.currentChunkProvider.recreateStructures(p_73239_1_, p_73239_2_);
                    }
                }

                return chunk;
            } catch (Exception exception) {
                logger.error("Couldn't load chunk", exception);
                return null;
            }
        }
    }

    @Shadow
    public Chunk originalLoadChunk(int p_73158_1_, int p_73158_2_) {
        long k = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.chunksToUnload.remove(k);
        Chunk chunk = (Chunk) this.loadedChunkHashMap.getValueByKey(k);

        if (chunk == null) {
            boolean added = loadingChunks.add(k);
            if (!added) {
                cpw.mods.fml.common.FMLLog.bigWarning(
                    "There is an attempt to load a chunk (%d,%d) in dimension %d that is already being loaded. This will cause weird chunk breakages.",
                    p_73158_1_,
                    p_73158_2_,
                    worldObj.provider.dimensionId);
            }
            chunk = ForgeChunkManager.fetchDormantChunk(k, this.worldObj);
            if (chunk == null) {
                chunk = this.safeLoadChunk(p_73158_1_, p_73158_2_);
            }

            if (chunk == null) {
                if (this.currentChunkProvider == null) {
                    chunk = this.defaultEmptyChunk;
                } else {
                    try {
                        chunk = this.currentChunkProvider.provideChunk(p_73158_1_, p_73158_2_);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport
                            .makeCrashReport(throwable, "Exception generating new chunk");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                        crashreportcategory.addCrashSection("Location", String.format("%d,%d", p_73158_1_, p_73158_2_));
                        crashreportcategory.addCrashSection("Position hash", k);
                        crashreportcategory.addCrashSection("Generator", this.currentChunkProvider.makeString());
                        throw new ReportedException(crashreport);
                    }
                }
            }

            this.loadedChunkHashMap.add(k, chunk);
            this.loadedChunks.add(chunk);
            loadingChunks.remove(k);
            chunk.onChunkLoad();
            chunk.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }

        return chunk;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        Chunk chunk = (Chunk) this.loadedChunkHashMap
            .getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return chunk == null
            ? (!this.worldObj.findingSpawnPoint && !this.loadChunkOnProvideRequest ? this.defaultEmptyChunk
                : this.loadChunk(p_73154_1_, p_73154_2_))
            : chunk;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public synchronized Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return loadChunk(p_73158_1_, p_73158_2_, null);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public Chunk loadChunk(int par1, int par2, Runnable runnable) {
        long k = ChunkCoordIntPair.chunkXZ2Int(par1, par2);
        this.chunksToUnload.remove(k);
        Chunk chunk = (Chunk) this.loadedChunkHashMap.getValueByKey(k);
        AnvilChunkLoader loader = null;

        if (this.currentChunkLoader instanceof AnvilChunkLoader) {
            loader = (AnvilChunkLoader) this.currentChunkLoader;
        }

        // We can only use the queue for already generated chunks
        if (chunk == null && loader != null && loader.chunkExists(this.worldObj, par1, par2)) {
            if (runnable != null) {
                ChunkIOExecutor
                    .queueChunkLoad(this.worldObj, loader, (ChunkProviderServer) (Object) this, par1, par2, runnable);
                return null;
            } else {
                chunk = ChunkIOExecutor
                    .syncChunkLoad(this.worldObj, loader, (ChunkProviderServer) (Object) this, par1, par2);
            }
        } else if (chunk == null) {
            chunk = this.originalLoadChunk(par1, par2);
        }

        // If we didn't load the chunk async and have a callback run it now
        if (runnable != null) {
            runnable.run();
        }

        return chunk;
    }
}
