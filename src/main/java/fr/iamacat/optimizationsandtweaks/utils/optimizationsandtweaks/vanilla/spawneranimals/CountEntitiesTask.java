package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals;

import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;

public class CountEntitiesTask implements Callable<Integer> {

    private final World world;
    private final EnumCreatureType type;
    private final boolean forSpawnCount;

    public CountEntitiesTask(World world, EnumCreatureType type, boolean forSpawnCount) {
        this.world = world;
        this.type = type;
        this.forSpawnCount = forSpawnCount;
    }

    @Override
    public Integer call() {
        int count = 0;
        List<Entity> loadedEntityList = world.loadedEntityList;
        for (Entity entity : loadedEntityList) {
            if (entity.isCreatureType(type, forSpawnCount)) {
                count++;
            }
        }
        return count;
    }
}
