package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import static fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.SpawnerAnimalsTask.optimizationsAndTweaks$eligibleChunksForSpawning;
import static fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.SpawnerAnimalsTask.optimizationsAndTweaks$shouldSpawnCreature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.eventhandler.Event;

@Mixin(value = SpawnerAnimals.class, priority = 999)
public class MixinPatchSpawnerAnimals {

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected static ChunkPosition func_151350_a(World world, int chunkX, int chunkZ) {
        int x = chunkX * 16 + world.rand.nextInt(16);
        int z = chunkZ * 16 + world.rand.nextInt(16);
        int minY = 0;
        int maxY = world.getActualHeight();
        int y = world.rand.nextInt(maxY - minY) + minY;
        return new ChunkPosition(x, y, z);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType creatureType, World world, int x, int y,
        int z) {
        if (!optimizationsAndTweaks$checkIfBlockExists(world, x, y, z)) {
            return false;
        }

        Block block = world.getBlock(x, y, z);
        if (creatureType.getCreatureMaterial() == Material.water) {
            return optimizationsAndTweaks$canCreatureSpawnInWater(world, x, y, z, block);
        } else {
            return optimizationsAndTweaks$canCreatureSpawnOnLand(creatureType, world, x, y, z, block);
        }
    }

    @Unique
    private static boolean optimizationsAndTweaks$checkIfBlockExists(World world, int x, int y, int z) {
        return world.blockExists(x, y, z);
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnInWater(World world, int x, int y, int z,
        Block block) {
        Block blockBelow = world.getBlock(x, y - 1, z);
        Block blockAbove = world.getBlock(x, y + 1, z);
        return optimizationsAndTweaks$canCreatureSpawnInWater(block, blockBelow, blockAbove);
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnOnLand(EnumCreatureType creatureType, World world,
        int x, int y, int z, Block block) {
        return optimizationsAndTweaks$canCreatureSpawnOnLand(
            creatureType,
            world,
            x,
            y,
            z,
            block,
            world.getBlock(x, y + 1, z));
    }

    @Unique
    private static boolean optimizationsAndTweaks$isNormalCube(Block block) {
        return block.isNormalCube();
    }

    @Unique
    private static boolean optimizationsAndTweaks$doesBlockHaveSolidTopSurface(World world, int x, int y, int z) {
        return World.doesBlockHaveSolidTopSurface(world, x, y, z);
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnOnLand(EnumCreatureType creatureType, World world,
        int x, int y, int z, Block block, Block blockAbove) {
        if (!optimizationsAndTweaks$doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            return false;
        }
        boolean isPeacefulCreature = creatureType.getPeacefulCreature();
        boolean isAnimal = creatureType.getAnimal();
        if ((!isPeacefulCreature || isAnimal) && optimizationsAndTweaks$shouldSpawnCreature(
            creatureType,
            world,
            optimizationsAndTweaks$eligibleChunksForSpawning)) {
            boolean isNormalCubeAbove = optimizationsAndTweaks$isNormalCube(blockAbove);
            for (int spawnAttempt = 0; spawnAttempt < creatureType.getMaxNumberOfCreature(); spawnAttempt++) {
                if (block != Blocks.bedrock && !isNormalCubeAbove
                    && !blockAbove.getMaterial()
                        .isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnInWater(Block block, Block blockBelow,
        Block blockAbove) {
        Material blockMaterial = block.getMaterial();
        Material blockBelowMaterial = blockBelow.getMaterial();
        boolean isNormalCubeAbove = blockAbove.isNormalCube();
        return blockMaterial.isLiquid() && blockBelowMaterial.isLiquid() && !isNormalCubeAbove;
    }

    /**
     * @author iamacatfr
     * @reason optimize findChunksForSpawning
     */

    @Overwrite
    public synchronized int findChunksForSpawning(WorldServer world, boolean peaceful, boolean hostile,
        boolean animals) {
        if (!peaceful && !hostile) {
            return 0;
        }

        optimizationsAndTweaks$clearEligibleChunksForSpawning();
        optimizationsAndTweaks$populateEligibleChunks(world);

        int spawnedEntities = 0;

        for (EnumCreatureType creatureType : EnumCreatureType.values()) {
            if (optimizationsAndTweaks$shouldSpawnCreatureType(creatureType, world, peaceful, hostile, animals)) {
                List<CompletableFuture<Integer>> spawnTasks = new ArrayList<>();
                Iterator<Object> iterator = optimizationsAndTweaks$eligibleChunksForSpawning.keySet()
                    .iterator();

                while (iterator.hasNext()) {
                    Object chunkCoord = iterator.next();
                    Boolean isChunkEligible = (Boolean) optimizationsAndTweaks$eligibleChunksForSpawning
                        .get(chunkCoord);

                    if (isChunkEligible != null && !isChunkEligible) {
                        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(
                            () -> optimizationsAndTweaks$spawnEntitiesInChunk(
                                world,
                                creatureType,
                                (ChunkCoordinates) chunkCoord));

                        spawnTasks.add(task);
                        iterator.remove();
                    }
                }

                List<CompletableFuture<Integer>> allTasks = new ArrayList<>(spawnTasks);

                CompletableFuture<Void> allOf = CompletableFuture.allOf(allTasks.toArray(new CompletableFuture[0]));

                allOf.join();

                spawnedEntities += allTasks.stream()
                    .mapToInt(CompletableFuture::join)
                    .sum();
            }
        }

        return spawnedEntities;
    }

    @Unique
    private void optimizationsAndTweaks$clearEligibleChunksForSpawning() {
        optimizationsAndTweaks$eligibleChunksForSpawning.clear();
    }

    @Unique
    private void optimizationsAndTweaks$populateEligibleChunks(WorldServer world) {
        for (Object playerObj : world.playerEntities) {
            if (playerObj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) playerObj;
                int chunkX = MathHelper.floor_double(player.posX / 16.0D);
                int chunkZ = MathHelper.floor_double(player.posZ / 16.0D);
                optimizationsAndTweaks$populateChunksAroundPlayer(chunkX, chunkZ);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$populateChunksAroundPlayer(int chunkX, int chunkZ) {
        int spawnRadius = 8;

        for (int l = -spawnRadius; l <= spawnRadius; ++l) {
            for (int i1 = -spawnRadius; i1 <= spawnRadius; ++i1) {
                boolean isEdge = l == -spawnRadius || l == spawnRadius || i1 == -spawnRadius || i1 == spawnRadius;
                ChunkCoordinates chunkCoord = new ChunkCoordinates(l + chunkX, i1, chunkZ);
                optimizationsAndTweaks$eligibleChunksForSpawning.put(chunkCoord, !isEdge);
            }
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$shouldSpawnCreatureType(EnumCreatureType creatureType, WorldServer world,
        boolean peaceful, boolean hostile, boolean animals) {
        return (!creatureType.getPeacefulCreature() || hostile) && (creatureType.getPeacefulCreature() || peaceful)
            && (!creatureType.getAnimal() || animals)
            && world.countEntities(creatureType, true)
                <= creatureType.getMaxNumberOfCreature() * optimizationsAndTweaks$eligibleChunksForSpawning.size()
                    / 256;
    }

    // do not refactor this method into smaller methods: it can cause Entity is already tracked! errors
    // todo fix Entity is already tracked! errors while refactoring the method into smaller methods
    // why i want to refactor this method into smallers : because for maintanibility, detecting performances bottlenecks
    @Unique
    private int optimizationsAndTweaks$spawnEntitiesInChunk(WorldServer world, EnumCreatureType creatureType,
        ChunkCoordinates chunkCoord) {
        ChunkPosition chunkPosition = func_151350_a(world, chunkCoord.posX, chunkCoord.posZ);

        int spawnedEntities = 0;
        int maxSpawnAttempts = 20;

        for (int attempt = 0; attempt < maxSpawnAttempts; attempt++) {
            int x = chunkPosition.chunkPosX + world.rand.nextInt(6) - world.rand.nextInt(6);
            int y = chunkPosition.chunkPosY + world.rand.nextInt(1) - world.rand.nextInt(1);
            int z = chunkPosition.chunkPosZ + world.rand.nextInt(6) - world.rand.nextInt(6);

            float spawnX = x + 0.5F;
            float spawnZ = z + 0.5F;

            float distanceSquared = spawnX * spawnX + spawnZ * spawnZ;

            if (optimizationsAndTweaks$isSpawnLocationValid(
                creatureType,
                world,
                spawnX,
                (float) y,
                spawnZ,
                distanceSquared) && optimizationsAndTweaks$isPlayerCloseEnough(world, spawnX, (float) y, spawnZ)) {

                BiomeGenBase.SpawnListEntry spawnListEntry = optimizationsAndTweaks$createSpawnListEntry(
                    creatureType,
                    world,
                    x,
                    y,
                    z);

                if (spawnListEntry != null) {
                    EntityLiving entityLiving = optimizationsAndTweaks$createEntityInstance(world, spawnListEntry);

                    if (entityLiving != null) {
                        entityLiving
                            .setLocationAndAngles(spawnX, (float) y, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);

                        if (optimizationsAndTweaks$canEntitySpawn(entityLiving, world, spawnX, (float) y, spawnZ)) {
                            world.spawnEntityInWorld(entityLiving);

                            if (!optimizationsAndTweaks$doSpecialSpawn(
                                entityLiving,
                                world,
                                spawnX,
                                (float) y,
                                spawnZ)) {
                                entityLiving.onSpawnWithEgg(null);
                            }

                            spawnedEntities += 1;
                        }
                    }
                }
            }
        }

        return spawnedEntities;
    }

    @Unique
    private BiomeGenBase.SpawnListEntry optimizationsAndTweaks$createSpawnListEntry(EnumCreatureType creatureType,
        World world, int x, int y, int z) {
        WorldServer worldServer = (WorldServer) world;
        return worldServer.spawnRandomCreature(creatureType, x, y, z);
    }

    @Unique
    private boolean optimizationsAndTweaks$isSpawnLocationValid(EnumCreatureType creatureType, World world,
        float spawnX, float spawnY, float spawnZ, float distanceSquared) {
        return distanceSquared >= 576.0F
            && canCreatureTypeSpawnAtLocation(creatureType, world, (int) spawnX, (int) spawnY, (int) spawnZ);
    }

    @Unique
    private boolean optimizationsAndTweaks$isPlayerCloseEnough(World world, float spawnX, float spawnY, float spawnZ) {
        return world.getClosestPlayer(spawnX, spawnY, spawnZ, 24.0D) == null;
    }

    @Unique
    private EntityLiving optimizationsAndTweaks$createEntityInstance(WorldServer world,
        BiomeGenBase.SpawnListEntry spawnListEntry) {
        try {
            return (EntityLiving) spawnListEntry.entityClass.getConstructor(World.class)
                .newInstance(world);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$canEntitySpawn(EntityLiving entity, World world, double x, double y,
        double z) {
        Chunk chunk = world.getChunkFromChunkCoords(MathHelper.floor_double(x) >> 4, MathHelper.floor_double(z) >> 4);
        if (!chunk.isChunkLoaded) {
            return false;
        }
        Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entity, world, (float) x, (float) y, (float) z);
        return canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entity.getCanSpawnHere());
    }

    @Unique
    private boolean optimizationsAndTweaks$doSpecialSpawn(EntityLiving entity, World world, double x, double y,
        double z) {
        return ForgeEventFactory.doSpecialSpawn(entity, world, (float) x, (float) y, (float) z);
    }
}
