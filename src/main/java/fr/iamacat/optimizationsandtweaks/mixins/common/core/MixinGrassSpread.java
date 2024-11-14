package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.*;
import java.util.concurrent.*;

import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;

@Mixin(BlockGrass.class)
public abstract class MixinGrassSpread {

    @Unique
    private static final ExecutorService EXECUTOR_SERVICE = Executors
        .newFixedThreadPool(OptimizationsandTweaksConfig.numberofcpus);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
        if (!worldIn.isRemote) {
            if (worldIn.getBlockLightValue(x, y + 1, z) < 4 && worldIn.getBlockLightOpacity(x, y + 1, z) > 2) {
                worldIn.setBlock(x, y, z, Blocks.dirt);
            } else if (worldIn.getBlockLightValue(x, y + 1, z) >= 9) {
                for (int l = 0; l < 4; ++l) {
                    int i1 = x + random.nextInt(3) - 1;
                    int j1 = y + random.nextInt(5) - 3;
                    int k1 = z + random.nextInt(3) - 1;
                    worldIn.getBlock(i1, j1 + 1, k1);
                    if (worldIn.getBlock(i1, j1, k1) == Blocks.dirt && worldIn.getBlockMetadata(i1, j1, k1) == 0
                        && worldIn.getBlockLightValue(i1, j1 + 1, k1) >= 4
                        && worldIn.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
                        EXECUTOR_SERVICE.submit(() -> optimizationsAndTweaks$processGrassBlock(worldIn, i1, j1, k1));
                    }
                }
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processGrassBlock(World worldIn, int x, int y, int z) {
        worldIn.setBlock(x, y, z, Blocks.grass);
    }
}
