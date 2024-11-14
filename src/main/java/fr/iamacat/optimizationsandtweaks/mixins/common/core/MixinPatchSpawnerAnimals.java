package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.*;
import java.util.concurrent.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals.SpawnCreaturesTask;

import static fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals.SpawnCreaturesTask.optimizationsAndTweaks$eligibleChunksForSpawning;

@Mixin(value = SpawnerAnimals.class, priority = 999)
public class MixinPatchSpawnerAnimals {

    @Unique
    private static final ExecutorService optimizationsAndTweaks$entityCountExecutor = Executors.newFixedThreadPool(
        Runtime.getRuntime()
            .availableProcessors());

    @Shadow
    protected static ChunkPosition func_151350_a(World p_151350_0_, int p_151350_1_, int p_151350_2_) {
        Chunk chunk = p_151350_0_.getChunkFromChunkCoords(p_151350_1_, p_151350_2_);
        int k = p_151350_1_ * 16 + p_151350_0_.rand.nextInt(16);
        int l = p_151350_2_ * 16 + p_151350_0_.rand.nextInt(16);
        int i1 = p_151350_0_.rand
            .nextInt(chunk == null ? p_151350_0_.getActualHeight() : chunk.getTopFilledSegment() + 16 - 1);
        return new ChunkPosition(k, i1, l);
    }

    /**
     * @author quentin452
     * @reason optimize findChunksForSpawning
     */
    @Overwrite
    public int findChunksForSpawning(WorldServer world, boolean spawnHostileMobs, boolean spawnPeacefulMobs,
        boolean spawnAnimals) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        optimizationsAndTweaks$eligibleChunksForSpawning.clear();
        int totalSpawns = 0;
        optimizationsAndTweaks$populateEligibleChunksForSpawning(world);
        for (EnumCreatureType creatureType : EnumCreatureType.values()) {
            if (SpawnCreaturesTask.shouldSpawnCreature(
                world,
                creatureType,
                spawnHostileMobs,
                spawnPeacefulMobs,
                spawnAnimals)) {
                totalSpawns += optimizationsAndTweaks$spawnCreatures(world, creatureType);
            }
        }
        return totalSpawns;
    }

    @Unique
    private void optimizationsAndTweaks$populateEligibleChunksForSpawning(WorldServer world) {
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
            int playerChunkX = MathHelper.floor_double(player.posX / 16.0D);
            int playerChunkZ = MathHelper.floor_double(player.posZ / 16.0D);
            byte chunkRadius = 8;
            for (int offsetX = -chunkRadius; offsetX <= chunkRadius; offsetX++) {
                for (int offsetZ = -chunkRadius; offsetZ <= chunkRadius; offsetZ++) {
                    boolean isEdge = offsetX == -chunkRadius || offsetX == chunkRadius
                        || offsetZ == -chunkRadius
                        || offsetZ == chunkRadius;
                    ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(
                        offsetX + playerChunkX,
                        offsetZ + playerChunkZ);
                    optimizationsAndTweaks$eligibleChunksForSpawning.put(chunkCoords, isEdge);
                }
            }
        }
    }

    @Unique
    private int optimizationsAndTweaks$spawnCreatures(WorldServer world, EnumCreatureType creatureType) {
        int totalSpawnCount = 0;
        ChunkCoordinates spawnPoint = world.getSpawnPoint();
        List<ChunkCoordIntPair> shuffledChunks = new ArrayList<>(
            optimizationsAndTweaks$eligibleChunksForSpawning.keySet());
        Collections.shuffle(shuffledChunks);

        List<Future<Integer>> futureResults = new ArrayList<>();

        for (ChunkCoordIntPair chunkCoords : shuffledChunks) {
            if (!((Boolean) optimizationsAndTweaks$eligibleChunksForSpawning.get(chunkCoords)).booleanValue()) {
                ChunkPosition spawnPosition = func_151350_a(world, chunkCoords.chunkXPos, chunkCoords.chunkZPos);
                SpawnCreaturesTask task = new SpawnCreaturesTask(world, creatureType, spawnPosition, spawnPoint);
                Future<Integer> future = optimizationsAndTweaks$entityCountExecutor.submit(task);
                futureResults.add(future);
            }
        }

        for (Future<Integer> future : futureResults) {
            try {
                totalSpawnCount += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return totalSpawnCount;
    }

    /**
     * @author quentin452
     * @reason Moved to SpawnCreaturesTask
     */
    @Overwrite
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType p_77190_0_, World p_77190_1_, int p_77190_2_,
                                                         int p_77190_3_, int p_77190_4_) {
        System.out.println("[Don't use canCreatureTypeSpawnAtLocation method (OptimizationsAndTweaks)]");
        return false;
    }

}
