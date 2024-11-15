package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockGrass2 {

    public static void optimizationsAndTweaks$spreadGrass(World worldIn, int x, int y, int z, Random random) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        Chunk chunk = worldIn.getChunkFromChunkCoords(chunkX, chunkZ);
        if (!chunk.isChunkLoaded) {
            return;
        }
        if (worldIn.getTotalWorldTime() % 10 == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = x + random.nextInt(3) - 1;
                int j1 = y + random.nextInt(5) - 3;
                int k1 = z + random.nextInt(3) - 1;

                if (worldIn.blockExists(i1, j1 + 1, k1)) {
                    int lightValue = worldIn.getBlockLightValue_do(i1, j1 + 1, k1, true);
                    int lightOpacity = worldIn.getBlockLightOpacity(i1, j1 + 1, k1);

                    if (lightValue >= 4 && lightOpacity <= 2) {
                        int localX = i1 & 15;
                        int localZ = k1 & 15;
                        if (j1 >= 0 && j1 < 256) {
                            Block block = chunk.getBlock(localX, j1, localZ);
                            int metadata = chunk.getBlockMetadata(localX, j1, localZ);

                            if (block == Blocks.dirt && metadata == 0) {
                                worldIn.setBlock(i1, j1, k1, Blocks.grass, 0, 2);
                            }
                        }
                    }
                }
            }
        }
    }
}
