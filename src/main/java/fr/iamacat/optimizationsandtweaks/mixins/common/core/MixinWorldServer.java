package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import static net.minecraftforge.common.ChestGenHooks.BONUS_CHEST;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;

import fr.iamacat.optimizationsandtweaks.utils.concurrentlinkedhashmap.ConcurrentHashMapV8;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = WorldServer.class, priority = 999)
public abstract class MixinWorldServer extends World {
    @Unique
    private WorldServer optimizationsAndTweaks$worldServer;
    @Shadow
    private static final Logger logger = LogManager.getLogger();
    @Shadow
    private final MinecraftServer mcServer;
    @Shadow
    private final EntityTracker theEntityTracker;
    @Shadow
    private final PlayerManager thePlayerManager;
    @Unique
    private ConcurrentHashMapV8<NextTickListEntry, Boolean> optimizationsAndTweaks$pendingTickListEntriesHashSet = new ConcurrentHashMapV8<>();
    @Unique
    private NavigableSet<NextTickListEntry> optimizationsAndTweaks$pendingTickListEntriesTreeSet = new ConcurrentSkipListSet<>();
    @Shadow
    private final Teleporter worldTeleporter;
    @Shadow
    private List pendingTickListEntriesThisTick = new ArrayList();
    @Shadow
    private IntHashMap entityIdMap;

    public MixinWorldServer(MinecraftServer p_i45284_1_, ISaveHandler p_i45284_2_, String p_i45284_3_, int p_i45284_4_,
                            WorldSettings p_i45284_5_, Profiler p_i45284_6_) {
        super(p_i45284_2_, p_i45284_3_, p_i45284_5_, WorldProvider.getProviderForDimension(p_i45284_4_), p_i45284_6_);
        this.mcServer = p_i45284_1_;
        this.theEntityTracker = new EntityTracker(optimizationsAndTweaks$worldServer);
        this.thePlayerManager = new PlayerManager(optimizationsAndTweaks$worldServer);

        if (this.entityIdMap == null) {
            this.entityIdMap = new IntHashMap();
        }

        if (this.optimizationsAndTweaks$pendingTickListEntriesHashSet == null) {
            this.optimizationsAndTweaks$pendingTickListEntriesHashSet = new ConcurrentHashMapV8<>();
        }

        if (this.optimizationsAndTweaks$pendingTickListEntriesTreeSet == null) {
            this.optimizationsAndTweaks$pendingTickListEntriesTreeSet = new ConcurrentSkipListSet<>();
        }

        this.worldTeleporter = new Teleporter(optimizationsAndTweaks$worldServer);
        this.worldScoreboard = new ServerScoreboard(p_i45284_1_);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) this.mapStorage
            .loadData(ScoreboardSaveData.class, "scoreboard");

        if (scoreboardsavedata == null) {
            scoreboardsavedata = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", scoreboardsavedata);
        }

        if (!(optimizationsAndTweaks$worldServer instanceof WorldServerMulti)) // Forge: We fix the global mapStorage, which causes us to share
        // scoreboards early. So don't associate the save data with the
        // temporary scoreboard
        {
            scoreboardsavedata.func_96499_a(this.worldScoreboard);
        }
        ((ServerScoreboard) this.worldScoreboard).func_96547_a(scoreboardsavedata);
        DimensionManager.setWorld(p_i45284_4_, optimizationsAndTweaks$worldServer);
    }

    /**
     * @author iamacatfr
     * @reason optimize func_147456_g/updateBlocks method
     */
    @Overwrite
    public void func_147456_g() {
        super.func_147456_g();

        for (Object o : this.activeChunkSet) {
            optimizationsAndTweaks$processChunk((ChunkCoordIntPair) o);
        }
    }

    @Unique
    private void optimizationsAndTweaks$processChunk(ChunkCoordIntPair chunkcoordintpair) {
        int k = chunkcoordintpair.chunkXPos * 16;
        int l = chunkcoordintpair.chunkZPos * 16;
        Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);

        optimizationsAndTweaks$processChunkLightning(chunk, k, l);
        optimizationsAndTweaks$processChunkIceAndSnow(chunk, k, l);
        optimizationsAndTweaks$processChunkTickBlocks(chunk, k, l);
    }

    @Unique
    private void optimizationsAndTweaks$processChunkLightning(Chunk chunk, int k, int l) {
        this.func_147467_a(k, l, chunk);
        this.theProfiler.endStartSection("tickChunk");
        chunk.func_150804_b(false);
        this.theProfiler.endStartSection("thunder");

        if (provider.canDoLightning(chunk) && optimizationsAndTweaks$shouldGenerateLightning()) {
            optimizationsAndTweaks$generateLightning(k, l);
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$shouldGenerateLightning() {
        return this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering();
    }

    @Unique
    private void optimizationsAndTweaks$generateLightning(int k, int l) {
        int randValue = this.rand.nextInt(100000);
        this.updateLCG = this.updateLCG * 3 + 1013904223;
        int i1 = this.updateLCG >> 2;
        int j1 = k + (i1 & 15);
        int k1 = l + (i1 >> 8 & 15);
        int l1 = this.getPrecipitationHeight(j1, k1);

        if (randValue == 0 && this.canLightningStrikeAt(j1, l1, k1)) {
            this.addWeatherEffect(new EntityLightningBolt(this, j1, l1, k1));
        }
    }

    @Unique
    private void optimizationsAndTweaks$processChunkIceAndSnow(Chunk chunk, int k, int l) {
        this.theProfiler.endStartSection("iceandsnow");

        if (provider.canDoRainSnowIce(chunk) && this.rand.nextInt(16) == 0) {
            optimizationsAndTweaks$generateIceAndSnow(k, l);
        }
    }

    @Unique
    private void optimizationsAndTweaks$generateIceAndSnow(int k, int l) {
        this.updateLCG = this.updateLCG * 3 + 1013904223;
        int i1 = this.updateLCG >> 2;
        int j1 = i1 & 15;
        int k1 = i1 >> 8 & 15;
        int l1 = this.getPrecipitationHeight(j1 + k, k1 + l);
        Block blockBelow = this.getBlock(j1 + k, l1 - 1, k1 + l);

        if (blockBelow == Blocks.water && this.isBlockFreezableNaturally(j1 + k, l1 - 1, k1 + l)) {
            this.setBlock(j1 + k, l1 - 1, k1 + l, Blocks.ice);
        }

        if (this.isRaining() && this.func_147478_e(j1 + k, l1, k1 + l, true)) {
            this.setBlock(j1 + k, l1, k1 + l, Blocks.snow_layer);
        }

        if (this.isRaining()) {
            BiomeGenBase biomegenbase = this.getBiomeGenForCoords(j1 + k, k1 + l);

            if (biomegenbase.canSpawnLightningBolt()) {
                blockBelow.fillWithRain(this, j1 + k, l1 - 1, k1 + l);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processChunkTickBlocks(Chunk chunk, int k, int l) {
        this.theProfiler.endStartSection("tickBlocks");
        optimizationsAndTweaks$updateTickBlocks(chunk, k, l);
    }

    @Unique
    private void optimizationsAndTweaks$updateTickBlocks(Chunk chunk, int k, int l) {
        for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
            if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
                optimizationsAndTweaks$updateSingleBlockTick(extendedblockstorage, k, l);
            }
        }
    }

    @Unique
    private Random optimizationsAndTweaks$random = new Random();

    @Unique
    private void optimizationsAndTweaks$updateSingleBlockTick(ExtendedBlockStorage extendedblockstorage, int k, int l) {
        int randomValue = ThreadLocalRandom.current().nextInt(1013904223);
        int j2 = randomValue & 15;
        int k2 = randomValue >> 8 & 15;
        int l2 = randomValue >> 16 & 15;

        Block block = extendedblockstorage.getBlockByExtId(j2, l2, k2);
        if (block.getTickRandomly()) {
            optimizationsAndTweaks$updateBlockTick(block, j2 + k, l2 + extendedblockstorage.getYLocation(), k2 + l);
        }
    }

    @Unique
    private void optimizationsAndTweaks$updateBlockTick(Block block, int x, int y, int z) {
        block.updateTick(this, x, y, z, optimizationsAndTweaks$random);
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void scheduleBlockUpdateWithPriority(int xCoord, int yCoord, int zCoord, Block block, int priority, int scheduledTime) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(xCoord, yCoord, zCoord, block);
        byte radius = 0;

        if (scheduledUpdatesAreImmediate && block.getMaterial() != Material.air) {
            if (block.func_149698_L()) {
                radius = 8;

                if (checkChunksExist(
                    nextTickListEntry.xCoord - radius,
                    nextTickListEntry.yCoord - radius,
                    nextTickListEntry.zCoord - radius,
                    nextTickListEntry.xCoord + radius,
                    nextTickListEntry.yCoord + radius,
                    nextTickListEntry.zCoord + radius)) {
                    Block existingBlock = getBlock(nextTickListEntry.xCoord, nextTickListEntry.yCoord, nextTickListEntry.zCoord);

                    if (existingBlock.getMaterial() != Material.air && existingBlock == nextTickListEntry.func_151351_a()) {
                        existingBlock.updateTick(this, nextTickListEntry.xCoord, nextTickListEntry.yCoord, nextTickListEntry.zCoord, rand);
                    }
                }

                return;
            }

            scheduledTime = 1;
        }

        if (checkChunksExist(
            xCoord - radius,
            yCoord - radius,
            zCoord - radius,
            xCoord + radius,
            yCoord + radius,
            zCoord + radius)) {
            if (block.getMaterial() != Material.air) {
                nextTickListEntry.setScheduledTime((long) scheduledTime + worldInfo.getWorldTotalTime());
                nextTickListEntry.setPriority(priority);
            }

            ConcurrentHashMapV8<NextTickListEntry, Boolean> pendingEntries = optimizationsAndTweaks$pendingTickListEntriesHashSet;

            if (!pendingEntries.containsKey(nextTickListEntry)) {
                pendingEntries.put(nextTickListEntry, Boolean.TRUE);
                optimizationsAndTweaks$pendingTickListEntriesTreeSet.add(nextTickListEntry);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_,
                              int p_147446_6_) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(p_147446_1_, p_147446_2_, p_147446_3_, p_147446_4_);
        nextticklistentry.setPriority(p_147446_6_);

        if (p_147446_4_.getMaterial() != Material.air) {
            nextticklistentry.setScheduledTime((long) p_147446_5_ + this.worldInfo.getWorldTotalTime());
        }

        if (!this.optimizationsAndTweaks$pendingTickListEntriesHashSet.containsKey(nextticklistentry)) {
            this.optimizationsAndTweaks$pendingTickListEntriesHashSet.put(nextticklistentry, Boolean.TRUE);
            this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean tickUpdates(boolean p_72955_1_) {
        int i = this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.size();

        if (i != this.optimizationsAndTweaks$pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (i > 1000) {
                i = 1000;
            }

            this.theProfiler.startSection("cleaning");
            NextTickListEntry nextticklistentry;

            for (int j = 0; j < i; ++j) {
                nextticklistentry = this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.first();

                if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                    break;
                }

                this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.remove(nextticklistentry);
                this.optimizationsAndTweaks$pendingTickListEntriesHashSet.remove(nextticklistentry);
                this.pendingTickListEntriesThisTick.add(nextticklistentry);
            }

            this.theProfiler.endSection();
            this.theProfiler.startSection("ticking");
            Iterator iterator = this.pendingTickListEntriesThisTick.iterator();

            while (iterator.hasNext()) {
                nextticklistentry = (NextTickListEntry) iterator.next();
                iterator.remove();
                // Keeping here as a note for future when it may be restored.
                // boolean isForced = getPersistentChunks().containsKey(new ChunkCoordIntPair(nextticklistentry.xCoord
                // >> 4, nextticklistentry.zCoord >> 4));
                // byte b0 = isForced ? 0 : 8;
                byte b0 = 0;

                if (this.checkChunksExist(
                    nextticklistentry.xCoord - b0,
                    nextticklistentry.yCoord - b0,
                    nextticklistentry.zCoord - b0,
                    nextticklistentry.xCoord + b0,
                    nextticklistentry.yCoord + b0,
                    nextticklistentry.zCoord + b0)) {
                    Block block = this
                        .getBlock(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);

                    if (block.getMaterial() != Material.air
                        && Block.isEqualTo(block, nextticklistentry.func_151351_a())) {
                        try {
                            block.updateTick(
                                this,
                                nextticklistentry.xCoord,
                                nextticklistentry.yCoord,
                                nextticklistentry.zCoord,
                                this.rand);
                        } catch (Throwable throwable1) {
                            CrashReport crashreport = CrashReport
                                .makeCrashReport(throwable1, "Exception while ticking a block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                            int k;

                            try {
                                k = this.getBlockMetadata(
                                    nextticklistentry.xCoord,
                                    nextticklistentry.yCoord,
                                    nextticklistentry.zCoord);
                            } catch (Throwable throwable) {
                                k = -1;
                            }

                            CrashReportCategory.func_147153_a(
                                crashreportcategory,
                                nextticklistentry.xCoord,
                                nextticklistentry.yCoord,
                                nextticklistentry.zCoord,
                                block,
                                k);
                            throw new ReportedException(crashreport);
                        }
                    }
                } else {
                    this.scheduleBlockUpdate(
                        nextticklistentry.xCoord,
                        nextticklistentry.yCoord,
                        nextticklistentry.zCoord,
                        nextticklistentry.func_151351_a(),
                        0);
                }
            }

            this.theProfiler.endSection();
            this.pendingTickListEntriesThisTick.clear();
            return !this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.isEmpty();
        }
    }

    /**
     * @author iamacatfr
     * @reason optimize getPendingBlockUpdates from WorldServer to reduce Tps lags
     */
    @Overwrite
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
        List<NextTickListEntry> pendingBlockUpdates = optimizationsAndTweaks$collectPendingBlockUpdates(p_72920_1_, p_72920_2_);
        optimizationsAndTweaks$removeProcessedEntries(p_72920_2_);
        return pendingBlockUpdates;
    }

    @Unique
    private List<NextTickListEntry> optimizationsAndTweaks$collectPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
        List<NextTickListEntry> pendingBlockUpdates = new ArrayList<>();
        ChunkCoordIntPair chunkcoordintpair = p_72920_1_.getChunkCoordIntPair();
        int minX = (chunkcoordintpair.chunkXPos << 4) - 2;
        int maxX = minX + 16 + 2;
        int minZ = (chunkcoordintpair.chunkZPos << 4) - 2;
        int maxZ = minZ + 16 + 2;

        for (int i = 0; i < 2; ++i) {
            Collection<NextTickListEntry> tickListEntries = (i == 0) ? this.optimizationsAndTweaks$pendingTickListEntriesTreeSet
                : this.pendingTickListEntriesThisTick;

            if (i == 1 && !this.pendingTickListEntriesThisTick.isEmpty()) {
                logger.debug("toBeTicked = " + this.pendingTickListEntriesThisTick.size());
            }

            Collection<NextTickListEntry> tickListEntriesCopy = new ArrayList<>(tickListEntries);
            Iterator<NextTickListEntry> iterator = tickListEntriesCopy.iterator();
            while (iterator.hasNext()) {
                NextTickListEntry nextticklistentry = iterator.next();
                if (optimizationsAndTweaks$isWithinBounds(nextticklistentry, minX, maxX, minZ, maxZ)) {
                    if (p_72920_2_) {
                        iterator.remove();
                    }

                    pendingBlockUpdates.add(nextticklistentry);
                }
            }
        }

        return pendingBlockUpdates;
    }

    @Unique
    private boolean optimizationsAndTweaks$isWithinBounds(NextTickListEntry entry, int minX, int maxX, int minZ, int maxZ) {
        return entry.xCoord >= minX && entry.xCoord < maxX &&
            entry.zCoord >= minZ && entry.zCoord < maxZ;
    }

    @Unique
    private void optimizationsAndTweaks$removeProcessedEntries(boolean p_72920_2_) {
        if (p_72920_2_) {
            this.pendingTickListEntriesThisTick.clear();
            this.optimizationsAndTweaks$pendingTickListEntriesTreeSet.clear();
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void initialize(WorldSettings p_72963_1_) {
        if (this.entityIdMap == null) {
            this.entityIdMap = new IntHashMap();
        }

        if (this.optimizationsAndTweaks$pendingTickListEntriesHashSet == null) {
            this.optimizationsAndTweaks$pendingTickListEntriesHashSet = new ConcurrentHashMapV8<>();
        }

        if (this.optimizationsAndTweaks$pendingTickListEntriesTreeSet == null) {
            this.optimizationsAndTweaks$pendingTickListEntriesTreeSet = new ConcurrentSkipListSet<>();
        }

        this.createSpawnPosition(p_72963_1_);
        super.initialize(p_72963_1_);
    }

    @Shadow
    protected void createSpawnPosition(WorldSettings p_73052_1_) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawnPosition(0, this.provider.getAverageGroundLevel(), 0);
        } else {
            if (net.minecraftforge.event.ForgeEventFactory.onCreateWorldSpawn(this, p_73052_1_)) return;
            this.findingSpawnPoint = true;
            WorldChunkManager worldchunkmanager = this.provider.worldChunkMgr;
            List list = worldchunkmanager.getBiomesToSpawnIn();
            Random random = new Random(this.getSeed());
            ChunkPosition chunkposition = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
            int i = 0;
            int j = this.provider.getAverageGroundLevel();
            int k = 0;

            if (chunkposition != null) {
                i = chunkposition.chunkPosX;
                k = chunkposition.chunkPosZ;
            } else {
                logger.warn("Unable to find spawn biome");
            }

            int l = 0;

            while (!this.provider.canCoordinateBeSpawn(i, k)) {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++l;

                if (l == 1000) {
                    break;
                }
            }

            this.worldInfo.setSpawnPosition(i, j, k);
            this.findingSpawnPoint = false;

            if (p_73052_1_.isBonusChestEnabled()) {
                this.createBonusChest();
            }
        }
    }

    @Shadow
    protected void createBonusChest() {
        WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(
            ChestGenHooks.getItems(BONUS_CHEST, rand),
            ChestGenHooks.getCount(BONUS_CHEST, rand));

        for (int i = 0; i < 10; ++i) {
            int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            int l = this.getTopSolidOrLiquidBlock(j, k) + 1;

            if (worldgeneratorbonuschest.generate(this, this.rand, j, l, k)) {
                break;
            }
        }
    }

    @Shadow
    protected int func_152379_p() {
        return this.mcServer.getConfigurationManager()
            .getViewDistance();
    }

    @Shadow
    public MinecraftServer func_73046_m() {
        return this.mcServer;
    }

    @Overwrite
    public void updateWeather()
    {
        boolean flag = this.isRaining();
        super.updateWeather();

        if (this.prevRainingStrength != this.rainingStrength)
        {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.dimensionId);
        }

        if (this.prevThunderingStrength != this.thunderingStrength)
        {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.dimensionId);
        }

        if (flag != this.isRaining())
        {
            if (flag)
            {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(2, 0.0F), this.provider.dimensionId);
            }
            else
            {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(1, 0.0F), this.provider.dimensionId);
            }

            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.dimensionId);
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.dimensionId);
        }
    }
}
