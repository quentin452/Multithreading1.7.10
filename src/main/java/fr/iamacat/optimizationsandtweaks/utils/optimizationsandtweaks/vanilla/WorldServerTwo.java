package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.ConcurrentSkipListSetThreadSafe;

public class WorldServerTwo {

    private static final ConcurrentSkipListSetThreadSafe<NextTickListEntry> pendingTickListEntries = new ConcurrentSkipListSetThreadSafe<>();

    public static boolean tickUpdates(boolean p_72955_1_, World world) {
        int i = pendingTickListEntries.size();
        if (i > 1000) {
            i = 1000;
        }
        world.theProfiler.startSection("cleaning");
        NextTickListEntry nextTickListEntry;
        for (int j = 0; j < i; ++j) {
            nextTickListEntry = pendingTickListEntries.first();
            if (!p_72955_1_ && nextTickListEntry.scheduledTime > world.getWorldInfo()
                .getWorldTotalTime()) {
                break;
            }
            pendingTickListEntries.remove(nextTickListEntry);
            world.scheduleBlockUpdate(
                nextTickListEntry.xCoord,
                nextTickListEntry.yCoord,
                nextTickListEntry.zCoord,
                nextTickListEntry.func_151351_a(),
                0);
        }
        world.theProfiler.endSection();
        world.theProfiler.startSection("ticking");
        Iterator<NextTickListEntry> iterator = pendingTickListEntries.iterator();
        while (iterator.hasNext()) {
            nextTickListEntry = iterator.next();
            iterator.remove();
            byte b0 = 0;
            if (world.checkChunksExist(
                nextTickListEntry.xCoord - b0,
                nextTickListEntry.yCoord - b0,
                nextTickListEntry.zCoord - b0,
                nextTickListEntry.xCoord + b0,
                nextTickListEntry.yCoord + b0,
                nextTickListEntry.zCoord + b0)) {
                Block block = world
                    .getBlock(nextTickListEntry.xCoord, nextTickListEntry.yCoord, nextTickListEntry.zCoord);
                if (block.getMaterial() != Material.air
                    && net.minecraft.block.Block.isEqualTo(block, nextTickListEntry.func_151351_a())) {
                    try {
                        block.updateTick(
                            world,
                            nextTickListEntry.xCoord,
                            nextTickListEntry.yCoord,
                            nextTickListEntry.zCoord,
                            world.rand);
                    } catch (Throwable throwable1) {
                        CrashReport crashreport = CrashReport
                            .makeCrashReport(throwable1, "Exception while ticking a block");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                        int k;
                        try {
                            k = world.getBlockMetadata(
                                nextTickListEntry.xCoord,
                                nextTickListEntry.yCoord,
                                nextTickListEntry.zCoord);
                        } catch (Throwable throwable) {
                            k = -1;
                        }
                        CrashReportCategory.func_147153_a(
                            crashreportcategory,
                            nextTickListEntry.xCoord,
                            nextTickListEntry.yCoord,
                            nextTickListEntry.zCoord,
                            block,
                            k);
                        throw new ReportedException(crashreport);
                    }
                }
            }
        }
        world.theProfiler.endSection();
        return !pendingTickListEntries.isEmpty();
    }

    public static void scheduleBlockUpdateWithPriority(World world, int p_147454_1_, int p_147454_2_, int p_147454_3_,
        Block p_147454_4_, int p_147454_5_, int p_147454_6_) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(p_147454_1_, p_147454_2_, p_147454_3_, p_147454_4_);
        byte b0 = 0;
        if (world.scheduledUpdatesAreImmediate && p_147454_4_.getMaterial() != Material.air) {
            if (p_147454_4_.func_149698_L()) {
                b0 = 8;
                if (world.checkChunksExist(
                    nextTickListEntry.xCoord - b0,
                    nextTickListEntry.yCoord - b0,
                    nextTickListEntry.zCoord - b0,
                    nextTickListEntry.xCoord + b0,
                    nextTickListEntry.yCoord + b0,
                    nextTickListEntry.zCoord + b0)) {
                    Block block1 = world
                        .getBlock(nextTickListEntry.xCoord, nextTickListEntry.yCoord, nextTickListEntry.zCoord);
                    if (block1.getMaterial() != Material.air && block1 == nextTickListEntry.func_151351_a()) {
                        block1.updateTick(
                            world,
                            nextTickListEntry.xCoord,
                            nextTickListEntry.yCoord,
                            nextTickListEntry.zCoord,
                            world.rand);
                    }
                }
                return;
            }
            p_147454_5_ = 1;
        }
        if (world.checkChunksExist(
            p_147454_1_ - b0,
            p_147454_2_ - b0,
            p_147454_3_ - b0,
            p_147454_1_ + b0,
            p_147454_2_ + b0,
            p_147454_3_ + b0)) {
            if (p_147454_4_.getMaterial() != Material.air) {
                nextTickListEntry.setScheduledTime((long) p_147454_5_ + world.worldInfo.getWorldTotalTime());
                nextTickListEntry.setPriority(p_147454_6_);
            }
            pendingTickListEntries.add(nextTickListEntry);
        }
    }

    public static void func_147446_b(World world, int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_,
        int p_147446_5_, int p_147446_6_) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(p_147446_1_, p_147446_2_, p_147446_3_, p_147446_4_);
        nextTickListEntry.setPriority(p_147446_6_);
        if (p_147446_4_.getMaterial() != Material.air) {
            nextTickListEntry.setScheduledTime((long) p_147446_5_ + world.worldInfo.getWorldTotalTime());
        }
        pendingTickListEntries.add(nextTickListEntry);
    }

    public static void optimizationsAndTweaks$removeProcessedEntries(boolean p_72920_2_) {
        if (p_72920_2_) {
            pendingTickListEntries.clear();
        }
    }

    public static List<NextTickListEntry> optimizationsAndTweaks$collectPendingBlockUpdates(WorldServer world,
        Chunk p_72920_1_, boolean p_72920_2_) {
        List<NextTickListEntry> pendingBlockUpdates = new ArrayList<>();
        ChunkCoordIntPair chunkcoordintpair = p_72920_1_.getChunkCoordIntPair();
        int minX = (chunkcoordintpair.chunkXPos << 4) - 2;
        int maxX = minX + 16 + 2;
        int minZ = (chunkcoordintpair.chunkZPos << 4) - 2;
        int maxZ = minZ + 16 + 2;
        Iterator<NextTickListEntry> iterator = pendingTickListEntries.iterator();
        while (iterator.hasNext()) {
            NextTickListEntry nextTickListEntry = iterator.next();
            if (optimizationsAndTweaks$isWithinBounds(nextTickListEntry, minX, maxX, minZ, maxZ)) {
                if (p_72920_2_) {
                    iterator.remove();
                }
                pendingBlockUpdates.add(nextTickListEntry);
            }
        }
        return pendingBlockUpdates;
    }

    private static boolean optimizationsAndTweaks$isWithinBounds(NextTickListEntry entry, int minX, int maxX, int minZ,
        int maxZ) {
        return entry.xCoord >= minX && entry.xCoord < maxX && entry.zCoord >= minZ && entry.zCoord < maxZ;
    }

    public static void optimizationsAndTweaks$processChunk(World world, ChunkCoordIntPair chunkCoordIntPair) {
        int chunkX = chunkCoordIntPair.chunkXPos * 16;
        int chunkZ = chunkCoordIntPair.chunkZPos * 16;
        ChunkProviderServer chunkProvider = ((WorldServer) world).theChunkProviderServer;
        Chunk chunk = chunkProvider.provideChunk(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
        if (chunk != null && chunk.isChunkLoaded) {
            optimizationsAndTweaks$handleChunk(world, chunkX, chunkZ, chunk);
            optimizationsAndTweaks$handleBlockTicks(world, chunk, chunkX, chunkZ);
        }
    }

    private static void optimizationsAndTweaks$handleChunk(World world, int chunkX, int chunkZ, Chunk chunk) {
        world.theProfiler.startSection("getChunk");
        func_147467_a(world, chunkX, chunkZ, chunk);
        world.theProfiler.endStartSection("tickChunk");
        chunk.func_150804_b(false);
        world.theProfiler.endStartSection("thunder");
        optimizationsAndTweaks$handleThunder(world, chunkX, chunkZ, chunk);
        world.theProfiler.endStartSection("iceandsnow");
        optimizationsAndTweaks$handleIceAndSnow(world, chunkX, chunkZ, chunk);
    }

    private static void optimizationsAndTweaks$handleThunder(World world, int chunkX, int chunkZ, Chunk chunk) {
        if (world.provider.canDoLightning(chunk) && world.rand.nextInt(100000) == 0
            && world.isRaining()
            && world.isThundering()) {
            world.updateLCG = world.updateLCG * 3 + 1013904223;
            int i1 = world.updateLCG >> 2;
            int j1 = chunkX + (i1 & 15);
            int k1 = chunkZ + (i1 >> 8 & 15);
            int l1 = world.getPrecipitationHeight(j1, k1);

            if (world.canLightningStrikeAt(j1, l1, k1)) {
                world.addWeatherEffect(new EntityLightningBolt(world, j1, l1, k1));
            }
        }
    }

    protected static void func_147467_a(World world, int p_147467_1_, int p_147467_2_, Chunk p_147467_3_) {
        world.theProfiler.endStartSection("moodSound");

        if (world.ambientTickCountdown == 0 && !world.isRemote) {
            world.updateLCG = world.updateLCG * 3 + 1013904223;
            int k = world.updateLCG >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 255;
            Block block = p_147467_3_.getBlock(l, j1, i1);
            l += p_147467_1_;
            i1 += p_147467_2_;

            if (block.getMaterial() == Material.air && world.getFullBlockLightValue(l, j1, i1) <= world.rand.nextInt(8)
                && world.getSavedLightValue(EnumSkyBlock.Sky, l, j1, i1) <= 0) {
                EntityPlayer entityplayer = world.getClosestPlayer(l + 0.5D, j1 + 0.5D, i1 + 0.5D, 8.0D);

                if (entityplayer != null && entityplayer.getDistanceSq(l + 0.5D, j1 + 0.5D, i1 + 0.5D) > 4.0D) {
                    world.playSoundEffect(
                        l + 0.5D,
                        j1 + 0.5D,
                        i1 + 0.5D,
                        "ambient.cave.cave",
                        0.7F,
                        0.8F + world.rand.nextFloat() * 0.2F);
                    world.ambientTickCountdown = world.rand.nextInt(12000) + 6000;
                }
            }
        }

        world.theProfiler.endStartSection("checkLight");
        p_147467_3_.enqueueRelightChecks();
    }

    private static void optimizationsAndTweaks$handleIceAndSnow(World world, int chunkX, int chunkZ, Chunk chunk) {
        if (world.provider.canDoRainSnowIce(chunk) && world.rand.nextInt(16) == 0) {
            world.updateLCG = world.updateLCG * 3 + 1013904223;
            int i1 = world.updateLCG >> 2;
            int j1 = i1 & 15;
            int k1 = i1 >> 8 & 15;
            int l1 = world.getPrecipitationHeight(j1 + chunkX, k1 + chunkZ);

            if (world.isBlockFreezableNaturally(j1 + chunkX, l1 - 1, k1 + chunkZ)) {
                world.setBlock(j1 + chunkX, l1 - 1, k1 + chunkZ, Blocks.ice);
            }

            if (world.isRaining() && world.func_147478_e(j1 + chunkX, l1, k1 + chunkZ, true)) {
                world.setBlock(j1 + chunkX, l1, k1 + chunkZ, Blocks.snow_layer);
            }

            if (world.isRaining()) {
                BiomeGenBase biomegenbase = world.getBiomeGenForCoords(j1 + chunkX, k1 + chunkZ);

                if (biomegenbase.canSpawnLightningBolt()) {
                    world.getBlock(j1 + chunkX, l1 - 1, k1 + chunkZ)
                        .fillWithRain(world, j1 + chunkX, l1 - 1, k1 + chunkZ);
                }
            }
        }
    }

    private static void optimizationsAndTweaks$handleBlockTicks(World world, Chunk chunk, int chunkX, int chunkZ) {
        world.theProfiler.endStartSection("tickBlocks");
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();

        for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage) {
            if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick()) {
                for (int i3 = 0; i3 < 3; ++i3) {
                    optimizationsAndTweaks$handleBlockTick(world, extendedblockstorage, chunkX, chunkZ);
                }
            }
        }

        world.theProfiler.endSection();
    }

    private static void optimizationsAndTweaks$handleBlockTick(World world, ExtendedBlockStorage extendedblockstorage,
        int chunkX, int chunkZ) {
        world.updateLCG = world.updateLCG * 3 + 1013904223;
        int i2 = world.updateLCG >> 2;
        int j2 = i2 & 15;
        int k2 = i2 >> 8 & 15;
        int l2 = i2 >> 16 & 15;
        Block block = extendedblockstorage.getBlockByExtId(j2, l2, k2);

        if (block.getTickRandomly()) {
            block.updateTick(world, j2 + chunkX, l2 + extendedblockstorage.getYLocation(), k2 + chunkZ, world.rand);
        }
    }
}
