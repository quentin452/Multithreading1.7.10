package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.ForgeEventFactory;

import cpw.mods.fml.common.eventhandler.Event;

public class SpawnCreaturesTask implements Callable<Integer> {

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
    public Integer call() {
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
}
