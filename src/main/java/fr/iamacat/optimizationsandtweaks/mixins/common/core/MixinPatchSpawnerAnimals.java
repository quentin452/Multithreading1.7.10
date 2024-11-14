package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.*;
import java.util.concurrent.*;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals.SpawnCreaturesTask;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.eventhandler.Event;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals.CountEntitiesTask;

@Mixin(value = SpawnerAnimals.class, priority = 999)
public class MixinPatchSpawnerAnimals {

    @Unique
    private static final ExecutorService optimizationsAndTweaks$entityCountExecutor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors());

    @Unique
    private ConcurrentHashMap optimizationsAndTweaks$eligibleChunksForSpawning = new ConcurrentHashMap();

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
            if (optimizationsAndTweaks$shouldSpawnCreature(
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
    private boolean optimizationsAndTweaks$shouldSpawnCreature(WorldServer world, EnumCreatureType creatureType,
        boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnAnimals) {
        return (creatureType.getPeacefulCreature() || spawnPeacefulMobs)
            && (!creatureType.getPeacefulCreature() || spawnHostileMobs)
            && (!creatureType.getAnimal() || spawnAnimals)
            && optimizationsAndTweaks$countEntities(world, creatureType, true)
                <= creatureType.getMaxNumberOfCreature() * optimizationsAndTweaks$eligibleChunksForSpawning.size()
                    / 256;
    }

    @Unique
    private int optimizationsAndTweaks$spawnCreatures(WorldServer world, EnumCreatureType creatureType) {
        int totalSpawnCount = 0;
        ChunkCoordinates spawnPoint = world.getSpawnPoint();
        List<ChunkCoordIntPair> shuffledChunks = new ArrayList<>(optimizationsAndTweaks$eligibleChunksForSpawning.keySet());
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


    @Unique
    private int optimizationsAndTweaks$trySpawnCreaturesAtPosition(WorldServer world, EnumCreatureType creatureType,
        ChunkPosition spawnPosition, ChunkCoordinates spawnPoint) {
        int spawnCount = 0;
        for (int attempt = 0; attempt < 3; attempt++) {
            int spawnX = spawnPosition.chunkPosX + world.rand.nextInt(6) - world.rand.nextInt(6);
            int spawnY = spawnPosition.chunkPosY + world.rand.nextInt(1) - world.rand.nextInt(1);
            int spawnZ = spawnPosition.chunkPosZ + world.rand.nextInt(6) - world.rand.nextInt(6);
            if (canCreatureTypeSpawnAtLocation(creatureType, world, spawnX, spawnY, spawnZ)
                && optimizationsAndTweaks$isFarEnoughFromSpawnPoint(world, spawnX, spawnY, spawnZ, spawnPoint)
                && optimizationsAndTweaks$attemptToSpawnCreature(world, creatureType, spawnX, spawnY, spawnZ)) {
                spawnCount++;
            }
        }
        return spawnCount;
    }

    @Unique
    private boolean optimizationsAndTweaks$isFarEnoughFromSpawnPoint(WorldServer world, int x, int y, int z,
        ChunkCoordinates spawnPoint) {
        float dx = x + 0.5F - spawnPoint.posX;
        float dy = y - spawnPoint.posY;
        float dz = z + 0.5F - spawnPoint.posZ;
        float distanceSquared = dx * dx + dy * dy + dz * dz;
        return distanceSquared >= 576.0F;
    }

    @Unique
    private boolean optimizationsAndTweaks$attemptToSpawnCreature(WorldServer world, EnumCreatureType creatureType,
        int x, int y, int z) {
        BiomeGenBase.SpawnListEntry spawnEntry = world.spawnRandomCreature(creatureType, x, y, z);
        if (spawnEntry == null) {
            return false;
        }
        try {
            EntityLiving entity = (EntityLiving) spawnEntry.entityClass.getConstructor(new Class[] { World.class })
                .newInstance(world);
            entity.setLocationAndAngles(x + 0.5F, y, z + 0.5F, world.rand.nextFloat() * 360.0F, 0.0F);
            Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entity, world, x + 0.5F, y, z + 0.5F);
            if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entity.getCanSpawnHere())) {
                world.spawnEntityInWorld(entity);
                ForgeEventFactory.doSpecialSpawn(entity, world, x + 0.5F, y, z + 0.5F);
                entity.onSpawnWithEgg(null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @author quentin452
     * @reason Optimize canCreatureTypeSpawnAtLocation
     */
    @Overwrite
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType p_77190_0_, World p_77190_1_, int p_77190_2_,
        int p_77190_3_, int p_77190_4_) {
        if (p_77190_0_.getCreatureMaterial() == Material.water) {
            return p_77190_1_.getBlock(p_77190_2_, p_77190_3_, p_77190_4_)
                .getMaterial()
                .isLiquid()
                && p_77190_1_.getBlock(p_77190_2_, p_77190_3_ - 1, p_77190_4_)
                    .getMaterial()
                    .isLiquid()
                && !p_77190_1_.getBlock(p_77190_2_, p_77190_3_ + 1, p_77190_4_)
                    .isNormalCube();
        } else if (!World.doesBlockHaveSolidTopSurface(p_77190_1_, p_77190_2_, p_77190_3_ - 1, p_77190_4_)) {
            return false;
        } else {
            Block block = p_77190_1_.getBlock(p_77190_2_, p_77190_3_ - 1, p_77190_4_);
            boolean spawnBlock = block.canCreatureSpawn(p_77190_0_, p_77190_1_, p_77190_2_, p_77190_3_ - 1, p_77190_4_);
            return spawnBlock && block != Blocks.bedrock
                && !p_77190_1_.getBlock(p_77190_2_, p_77190_3_, p_77190_4_)
                    .isNormalCube()
                && !p_77190_1_.getBlock(p_77190_2_, p_77190_3_, p_77190_4_)
                    .getMaterial()
                    .isLiquid()
                && !p_77190_1_.getBlock(p_77190_2_, p_77190_3_ + 1, p_77190_4_)
                    .isNormalCube();
        }
    }

    @Unique
    public int optimizationsAndTweaks$countEntities(WorldServer world, EnumCreatureType type, boolean forSpawnCount) {
        CountEntitiesTask task = new CountEntitiesTask(world, type, forSpawnCount);
        Future<Integer> future = optimizationsAndTweaks$entityCountExecutor.submit(task);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
