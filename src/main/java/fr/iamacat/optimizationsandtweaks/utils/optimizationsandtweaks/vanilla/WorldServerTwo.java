package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.ConcurrentSkipListSetThreadSafe;

public class WorldServerTwo {

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
