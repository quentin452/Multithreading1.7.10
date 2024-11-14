package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import static fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.WorldServerTwo.*;
import static net.minecraftforge.common.ChestGenHooks.BONUS_CHEST;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.*;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
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

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.WorldServerTwo;

@Mixin(value = WorldServer.class, priority = 999)
public abstract class MixinWorldServer extends World {

    @Unique
    private WorldServer optimizationsAndTweaks$worldServer;
    @Unique
    private int optimizationsAndTweaks$tickCounter = 0;
    @Shadow
    private static final Logger logger = LogManager.getLogger();
    @Shadow
    public boolean allPlayersSleeping;
    @Shadow
    private final MinecraftServer mcServer;
    @Shadow
    private final EntityTracker theEntityTracker;
    @Shadow
    private final PlayerManager thePlayerManager;
    @Shadow
    private final Teleporter worldTeleporter;
    @Shadow
    private IntHashMap entityIdMap;
    @Shadow
    public final SpawnerAnimals animalSpawner = new SpawnerAnimals();
    @Shadow
    public List<Teleporter> customTeleporters = new ArrayList<>();

    public MixinWorldServer(MinecraftServer p_i45284_1_, ISaveHandler p_i45284_2_, String p_i45284_3_, int p_i45284_4_,
        WorldSettings p_i45284_5_, Profiler p_i45284_6_) {
        super(p_i45284_2_, p_i45284_3_, p_i45284_5_, WorldProvider.getProviderForDimension(p_i45284_4_), p_i45284_6_);
        this.mcServer = p_i45284_1_;
        this.theEntityTracker = new EntityTracker(optimizationsAndTweaks$worldServer);
        this.thePlayerManager = new PlayerManager(optimizationsAndTweaks$worldServer);

        if (this.entityIdMap == null) {
            this.entityIdMap = new IntHashMap();
        }

        this.worldTeleporter = new Teleporter(optimizationsAndTweaks$worldServer);
        this.worldScoreboard = new ServerScoreboard(p_i45284_1_);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData) this.mapStorage
            .loadData(ScoreboardSaveData.class, "scoreboard");

        if (scoreboardsavedata == null) {
            scoreboardsavedata = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", scoreboardsavedata);
        }

        if (!(optimizationsAndTweaks$worldServer instanceof WorldServerMulti)) // Forge: We fix the global mapStorage,
                                                                               // which causes us to share
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
     * @reason optimize getPendingBlockUpdates from WorldServer to reduce Tps lags
     */
    @Overwrite
    public synchronized List<NextTickListEntry> getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
        List<NextTickListEntry> pendingBlockUpdates = optimizationsAndTweaks$collectPendingBlockUpdates(
            (WorldServer) (Object) this,
            p_72920_1_,
            p_72920_2_);
        optimizationsAndTweaks$removeProcessedEntries(p_72920_2_);
        return pendingBlockUpdates;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateWeather() {
        boolean flag = this.isRaining();
        super.updateWeather();

        if (this.prevRainingStrength != this.rainingStrength) {
            this.mcServer.getConfigurationManager()
                .sendPacketToAllPlayersInDimension(
                    new S2BPacketChangeGameState(7, this.rainingStrength),
                    this.provider.dimensionId);
        }

        if (this.prevThunderingStrength != this.thunderingStrength) {
            this.mcServer.getConfigurationManager()
                .sendPacketToAllPlayersInDimension(
                    new S2BPacketChangeGameState(8, this.thunderingStrength),
                    this.provider.dimensionId);
        }

        if (flag != this.isRaining()) {
            if (flag) {
                this.mcServer.getConfigurationManager()
                    .sendPacketToAllPlayersInDimension(
                        new S2BPacketChangeGameState(2, 0.0F),
                        this.provider.dimensionId);
            } else {
                this.mcServer.getConfigurationManager()
                    .sendPacketToAllPlayersInDimension(
                        new S2BPacketChangeGameState(1, 0.0F),
                        this.provider.dimensionId);
            }

            this.mcServer.getConfigurationManager()
                .sendPacketToAllPlayersInDimension(
                    new S2BPacketChangeGameState(7, this.rainingStrength),
                    this.provider.dimensionId);
            this.mcServer.getConfigurationManager()
                .sendPacketToAllPlayersInDimension(
                    new S2BPacketChangeGameState(8, this.thunderingStrength),
                    this.provider.dimensionId);
        }
    }

    @Overwrite
    public void scheduleBlockUpdate(int p_147464_1_, int p_147464_2_, int p_147464_3_, Block p_147464_4_,
        int p_147464_5_) {
        this.scheduleBlockUpdateWithPriority(p_147464_1_, p_147464_2_, p_147464_3_, p_147464_4_, p_147464_5_, 0);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean tickUpdates(boolean p_72955_1_) {
        return WorldServerTwo.tickUpdates(p_72955_1_, this);
    }

    /**
     * @author
     * @reason
     * @method func_147456_g = onTick
     */
    // todo fix lags caused by ExtendedBlockStorage.getID when using 32 chunks of render distance
    @Overwrite
    public void func_147456_g() {
        super.func_147456_g();

        for (Object o : this.activeChunkSet) {
            ChunkCoordIntPair chunkCoordIntPair = (ChunkCoordIntPair) o;
            optimizationsAndTweaks$processChunk(this, chunkCoordIntPair);
        }
    }

    @Overwrite
    public void initialize(WorldSettings p_72963_1_) {
        if (this.entityIdMap == null) {
            this.entityIdMap = new IntHashMap();
        }

        this.createSpawnPosition(p_72963_1_);
        super.initialize(p_72963_1_);
    }

    @Shadow
    public void createSpawnPosition(WorldSettings p_73052_1_) {
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
    public void createBonusChest() {
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

    @Overwrite
    public synchronized void scheduleBlockUpdateWithPriority(int p_147454_1_, int p_147454_2_, int p_147454_3_,
        Block p_147454_4_, int p_147454_5_, int p_147454_6_) {
        WorldServerTwo.scheduleBlockUpdateWithPriority(
            this,
            p_147454_1_,
            p_147454_2_,
            p_147454_3_,
            p_147454_4_,
            p_147454_5_,
            p_147454_6_);
    }

    @Overwrite
    public synchronized void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_,
        int p_147446_5_, int p_147446_6_) {
        WorldServerTwo
            .func_147446_b(this, p_147446_1_, p_147446_2_, p_147446_3_, p_147446_4_, p_147446_5_, p_147446_6_);
    }
    /**
     * @author quentin452
     * @reason call findChunksForSpawning every 4 ticks instead of 1
     */
    @Overwrite
    public void tick()
    {
        super.tick();

        if (this.getWorldInfo().isHardcoreModeEnabled() && this.difficultySetting != EnumDifficulty.HARD)
        {
            this.difficultySetting = EnumDifficulty.HARD;
        }

        this.provider.worldChunkMgr.cleanupCache();

        if (this.areAllPlayersAsleep())
        {
            if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
            {
                long i = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(i - i % 24000L);
            }

            this.wakeAllPlayers();
        }

        this.theProfiler.startSection("mobSpawner");

        if (this.getGameRules().getGameRuleBooleanValue("doMobSpawning")) {
            if (optimizationsAndTweaks$tickCounter % 4 == 0) {
                this.animalSpawner.findChunksForSpawning((WorldServer)(Object)this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
                optimizationsAndTweaks$tickCounter = 0;
            }
            optimizationsAndTweaks$tickCounter++;
        }

        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        int j = this.calculateSkylightSubtracted(1.0F);

        if (j != this.skylightSubtracted)
        {
            this.skylightSubtracted = j;
        }

        this.worldInfo.incrementTotalWorldTime(this.worldInfo.getWorldTotalTime() + 1L);

        if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }

        this.theProfiler.endStartSection("tickPending");
        this.tickUpdates(false);
        this.theProfiler.endStartSection("tickBlocks");
        this.func_147456_g();
        this.theProfiler.endStartSection("chunkMap");
        this.thePlayerManager.updatePlayerInstances();
        this.theProfiler.endStartSection("village");
        this.villageCollectionObj.tick();
        this.villageSiegeObj.tick();
        this.theProfiler.endStartSection("portalForcer");
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        for (Teleporter tele : customTeleporters)
        {
            tele.removeStalePortalLocations(getTotalWorldTime());
        }
        this.theProfiler.endSection();
        this.func_147488_Z();
    }
    @Shadow
    public boolean areAllPlayersAsleep()
    {
        if (this.allPlayersSleeping && !this.isRemote)
        {
            Iterator iterator = this.playerEntities.iterator();
            EntityPlayer entityplayer;

            do
            {
                if (!iterator.hasNext())
                {
                    return true;
                }

                entityplayer = (EntityPlayer)iterator.next();
            }
            while (entityplayer.isPlayerFullyAsleep());

            return false;
        }
        else
        {
            return false;
        }
    }

    @Shadow
    protected void wakeAllPlayers()
    {
    }
    @Shadow
    private void resetRainAndThunder()
    {
    }
    @Shadow
    private void func_147488_Z()
    {
    }
}
