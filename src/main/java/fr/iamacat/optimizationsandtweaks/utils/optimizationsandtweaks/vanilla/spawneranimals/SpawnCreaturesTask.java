package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;

import cpw.mods.fml.common.eventhandler.Event;
import fr.iamacat.optimizationsandtweaks.utils.concurrentlinkedhashmap.ConcurrentHashMapV8;

public class SpawnCreaturesTask implements Callable<Integer> {

    public static ConcurrentHashMapV8<ChunkCoordIntPair, Boolean> eligibleChunksForSpawning = new ConcurrentHashMapV8<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(
        Runtime.getRuntime()
            .availableProcessors());
    private final WorldServer world;
    private final EnumCreatureType creatureType;
    private final ChunkPosition spawnPosition;
    private final ChunkCoordinates spawnPoint;

    public SpawnCreaturesTask(WorldServer world, EnumCreatureType creatureType, ChunkPosition spawnPosition,
        ChunkCoordinates spawnPoint) {
        this.world = world;
        this.creatureType = creatureType;
        this.spawnPosition = spawnPosition;
        this.spawnPoint = spawnPoint;
    }

    @Override
    public Integer call() throws Exception {
        int spawnCount = 0;
        for (int attempt = 0; attempt < 3; attempt++) {
            int spawnX = spawnPosition.chunkPosX + world.rand.nextInt(6) - world.rand.nextInt(6);
            int spawnY = spawnPosition.chunkPosY + world.rand.nextInt(1) - world.rand.nextInt(1);
            int spawnZ = spawnPosition.chunkPosZ + world.rand.nextInt(6) - world.rand.nextInt(6);
            if (canCreatureTypeSpawnAtLocation(creatureType, world, spawnX, spawnY, spawnZ)
                && isFarEnoughFromSpawnPoint(world, spawnX, spawnY, spawnZ, spawnPoint)
                && attemptToSpawnCreature(world, creatureType, spawnX, spawnY, spawnZ)) {
                spawnCount++;
            }
        }
        return spawnCount;
    }

    private boolean isFarEnoughFromSpawnPoint(WorldServer world, int x, int y, int z, ChunkCoordinates spawnPoint) {
        float dx = x + 0.5F - spawnPoint.posX;
        float dy = y - spawnPoint.posY;
        float dz = z + 0.5F - spawnPoint.posZ;
        float distanceSquared = dx * dx + dy * dy + dz * dz;
        return distanceSquared >= 576.0F;
    }

    private boolean attemptToSpawnCreature(WorldServer world, EnumCreatureType creatureType, int x, int y, int z) {
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

    private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType creatureType, World world, int x, int y,
        int z) {
        if (creatureType.getCreatureMaterial() == Material.water) {
            return world.getBlock(x, y, z)
                .getMaterial()
                .isLiquid()
                && world.getBlock(x, y - 1, z)
                    .getMaterial()
                    .isLiquid()
                && !world.getBlock(x, y + 1, z)
                    .isNormalCube();
        } else if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            return false;
        } else {
            Block block = world.getBlock(x, y - 1, z);
            boolean spawnBlock = block.canCreatureSpawn(creatureType, world, x, y - 1, z);
            return spawnBlock && block != Blocks.bedrock
                && !world.getBlock(x, y, z)
                    .isNormalCube()
                && !world.getBlock(x, y, z)
                    .getMaterial()
                    .isLiquid()
                && !world.getBlock(x, y + 1, z)
                    .isNormalCube();
        }
    }

    public static boolean shouldSpawnCreature(WorldServer world, EnumCreatureType creatureType,
        boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnAnimals) {
        return (creatureType.getPeacefulCreature() || spawnPeacefulMobs)
            && (!creatureType.getPeacefulCreature() || spawnHostileMobs)
            && (!creatureType.getAnimal() || spawnAnimals)
            && countEntities(world, creatureType, true)
                <= creatureType.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256;
    }

    public static int countEntities(WorldServer world, EnumCreatureType type, boolean forSpawnCount) {
        int totalEntities = 0;
        List<Entity> loadedEntityList = world.loadedEntityList;
        if (loadedEntityList == null) {
            System.err.println("[OptimizationsAndTweaks] world.loadedEntityList is null");
            return totalEntities;
        }
        for (Entity entity : loadedEntityList) {
            if (entity == null) {
                System.err.println("[OptimizationsAndTweaks] Encountered null entity in loadedEntityList");
                continue;
            }
            if (entity.isCreatureType(type, forSpawnCount)) {
                totalEntities++;
            }
        }
        return totalEntities;
    }

    public static void populateEligibleChunksForSpawning(WorldServer world) {
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
                    eligibleChunksForSpawning.put(chunkCoords, isEdge);
                }
            }
        }
    }

    protected static ChunkPosition func_151350_a(World p_151350_0_, int p_151350_1_, int p_151350_2_) {
        Chunk chunk = p_151350_0_.getChunkFromChunkCoords(p_151350_1_, p_151350_2_);
        int k = p_151350_1_ * 16 + p_151350_0_.rand.nextInt(16);
        int l = p_151350_2_ * 16 + p_151350_0_.rand.nextInt(16);
        int i1 = p_151350_0_.rand
            .nextInt(chunk == null ? p_151350_0_.getActualHeight() : chunk.getTopFilledSegment() + 16 - 1);
        return new ChunkPosition(k, i1, l);
    }

    public static CompletableFuture<Integer> findChunksForSpawningAsync(WorldServer world, boolean spawnHostileMobs,
        boolean spawnPeacefulMobs, boolean spawnAnimals) {
        return CompletableFuture.supplyAsync(() -> {
            if (!spawnHostileMobs && !spawnPeacefulMobs) {
                return 0;
            }
            eligibleChunksForSpawning.clear();
            populateEligibleChunksForSpawning(world);
            List<ChunkCoordIntPair> chunkCoordsList = new ArrayList<>(eligibleChunksForSpawning.keySet());
            List<Future<Integer>> futures = new ArrayList<>();
            for (EnumCreatureType creatureType : EnumCreatureType.values()) {
                if (shouldSpawnCreature(world, creatureType, spawnHostileMobs, spawnPeacefulMobs, spawnAnimals)) {
                    for (ChunkCoordIntPair chunkCoords : chunkCoordsList) {
                        Boolean isEdge = eligibleChunksForSpawning.get(chunkCoords);
                        if (isEdge != null && !isEdge) {
                            int chunkX = chunkCoords.chunkXPos;
                            int chunkZ = chunkCoords.chunkZPos;
                            ChunkPosition spawnPosition = func_151350_a(world, chunkX, chunkZ);
                            SpawnCreaturesTask task = new SpawnCreaturesTask(
                                world,
                                creatureType,
                                spawnPosition,
                                world.getSpawnPoint());
                            futures.add(executor.submit(task));
                        }
                    }
                }
            }
            return futures.stream()
                .mapToInt(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                })
                .sum();
        });
    }

}
