package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.world.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.spawneranimals.SpawnCreaturesTask;

@Mixin(value = SpawnerAnimals.class, priority = 999)
public class MixinPatchSpawnerAnimals {

    /**
     * @author quentin452
     * @reason optimize findChunksForSpawning
     */
    @Overwrite
    public int findChunksForSpawning(WorldServer world, boolean spawnHostileMobs, boolean spawnPeacefulMobs,
        boolean spawnAnimals) {
        return SpawnCreaturesTask.findChunksForSpawningAsync(world, spawnHostileMobs, spawnPeacefulMobs, spawnAnimals)
            .join();
    }
}
