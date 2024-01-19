package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import cpw.mods.fml.common.eventhandler.Event;
import fr.iamacat.optimizationsandtweaks.utils.agrona.collections.Object2ObjectHashMap;
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
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

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

    @Unique
    private static Object2ObjectHashMap optimizationsAndTweaks$eligibleChunksForSpawning = new Object2ObjectHashMap();

    /**
     * @author
     * @reason
     */

    @Overwrite
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType creatureType, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        if (creatureType.getCreatureMaterial() == Material.water) {
            Block blockBelow = world.getBlock(x, y - 1, z);
            Block blockAbove = world.getBlock(x, y + 1, z);
            return optimizationsAndTweaks$canCreatureSpawnInWater(block, blockBelow, blockAbove);
        }
        return optimizationsAndTweaks$canCreatureSpawnOnLand(creatureType, world, x, y, z, block, world.getBlock(x, y + 1, z));
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnInWater(Block block, Block blockBelow,
        Block blockAbove) {
        Material blockMaterial = block.getMaterial();
        Material blockBelowMaterial = blockBelow.getMaterial();
        boolean isNormalCubeAbove = blockAbove.isNormalCube();
        return blockMaterial.isLiquid() && blockBelowMaterial.isLiquid() && !isNormalCubeAbove;
    }

    @Unique
    private static boolean optimizationsAndTweaks$canCreatureSpawnOnLand(EnumCreatureType creatureType, World world,
                                                                         int x, int y, int z, Block block, Block blockAbove) {
        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            return false;
        }
        boolean isPeacefulCreature = creatureType.getPeacefulCreature();
        boolean isAnimal = creatureType.getAnimal();

        int cachedCount = world.countEntities(creatureType, true);
        if ((!isPeacefulCreature || isAnimal) && cachedCount <= creatureType.getMaxNumberOfCreature() * optimizationsAndTweaks$eligibleChunksForSpawning.size() / 256) {
            for (int spawnAttempt = 0; spawnAttempt < creatureType.getMaxNumberOfCreature(); spawnAttempt++) {
                if (block != Blocks.bedrock && !blockAbove.isNormalCube() && !blockAbove.getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author iamacatfr
     * @reason optimize findChunksForSpawning
     */
    @Overwrite
    public int findChunksForSpawning(WorldServer world, boolean peaceful, boolean hostile, boolean animals) {
        if (!peaceful && !hostile) {
            return 0;
        }

        optimizationsAndTweaks$clearEligibleChunksForSpawning();
        optimizationsAndTweaks$populateEligibleChunks(world);

        int spawnedEntities = 0;
        ChunkCoordinates spawnPoint = world.getSpawnPoint();
        Object2ObjectHashMap<ChunkCoordIntPair, Boolean> eligibleChunks = optimizationsAndTweaks$eligibleChunksForSpawning;

        for (EnumCreatureType creatureType : EnumCreatureType.values()) {
            if (optimizationsAndTweaks$shouldSpawnCreatureType(creatureType, world, peaceful, hostile, animals)) {
                for (ChunkCoordIntPair chunkCoord : eligibleChunks.keySet()) {
                    Boolean isChunkEligible = eligibleChunks.get(chunkCoord);
                    if (isChunkEligible != null && !isChunkEligible) {
                        spawnedEntities += optimizationsAndTweaks$spawnEntitiesInChunk(
                            world,
                            creatureType,
                                chunkCoord,
                            spawnPoint);
                    }
                }
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
        int b0 = 8;
        for (int l = -b0; l <= b0; ++l) {
            for (int i1 = -b0; i1 <= b0; ++i1) {
                boolean isEdge = l == -b0 || l == b0 || i1 == -b0 || i1 == b0;
                ChunkCoordIntPair chunkCoord = new ChunkCoordIntPair(l + chunkX, i1 + chunkZ);

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
                                                            ChunkCoordIntPair chunkCoord, ChunkCoordinates spawnPoint) {
        ChunkPosition chunkPosition = func_151350_a(world, chunkCoord.chunkXPos, chunkCoord.chunkZPos);

        int i = 0;
        int maxSpawnAttempts = 3;

        for (int attempt = 0; attempt < maxSpawnAttempts; attempt++) {
            int x = chunkPosition.chunkPosX + world.rand.nextInt(6) - world.rand.nextInt(6);
            int y = chunkPosition.chunkPosY + world.rand.nextInt(1) - world.rand.nextInt(1);
            int z = chunkPosition.chunkPosZ + world.rand.nextInt(6) - world.rand.nextInt(6);

            float spawnX = x + 0.5F;
            float spawnZ = z + 0.5F;

            float offsetX = spawnX - (float) spawnPoint.posX;
            float offsetY = y - (float) spawnPoint.posY;
            float offsetZ = spawnZ - (float) spawnPoint.posZ;

            float distanceSquared = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

            if (optimizationsAndTweaks$isSpawnLocationValid(
                creatureType,
                world,
                spawnX,
                (float) y,
                spawnZ,
                distanceSquared) && optimizationsAndTweaks$isPlayerCloseEnough(world, spawnX, (float) y, spawnZ)) {

                BiomeGenBase.SpawnListEntry spawnListEntry = optimizationsAndTweaks$createSpawnListEntry(creatureType, world, x, y, z);

                if (spawnListEntry != null) {
                    EntityLiving entityLiving = optimizationsAndTweaks$createEntityInstance(world, spawnListEntry);

                    if (entityLiving != null) {
                        entityLiving.setLocationAndAngles(spawnX, (float) y, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);

                        Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityLiving, world, spawnX, (float) y, spawnZ);
                        if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entityLiving.getCanSpawnHere())) {
                            world.spawnEntityInWorld(entityLiving);

                            if (!ForgeEventFactory.doSpecialSpawn(entityLiving, world, spawnX, (float) y, spawnZ)) {
                                entityLiving.onSpawnWithEgg(null);
                            }

                            i += 1;
                        }
                    }
                }
            }
        }

        return i;
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
}
