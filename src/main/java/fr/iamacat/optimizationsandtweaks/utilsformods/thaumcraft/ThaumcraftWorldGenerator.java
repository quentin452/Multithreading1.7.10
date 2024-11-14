package fr.iamacat.optimizationsandtweaks.utilsformods.thaumcraft;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.createNodeAt;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

public class ThaumcraftWorldGenerator {

    private static final int SEARCH_RADIUS = 5;

    public static int countBlocksAroundPlayer(World world, int cx, int cy, int cz, Material material) {
        int radius = SEARCH_RADIUS;
        int count = 0;
        int minX = Math.max(cx - radius, (cx >> 4) << 4);
        int maxX = Math.min(cx + radius, ((cx >> 4) << 4) + 15);
        int minZ = Math.max(cz - radius, (cz >> 4) << 4);
        int maxZ = Math.min(cz + radius, ((cz >> 4) << 4) + 15);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int maxY = world.getHeightValue(x, z);
                for (int y = 0; y < maxY; y++) {
                    Block block = world.getBlock(x, y, z);
                    if (block.getMaterial() == material) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static int optimizationsAndTweaks$countFoliageAroundPlayer(World world, int x, int y, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

        if (!chunk.isChunkLoaded) {
            return 0;
        }

        int count = 0;
        int radius = SEARCH_RADIUS;

        int minX = Math.max(x - radius, chunkX << 4);
        int maxX = Math.min(x + radius, (chunkX << 4) + 15);
        int minZ = Math.max(z - radius, chunkZ << 4);
        int maxZ = Math.min(z + radius, (chunkZ << 4) + 15);

        for (int xOffset = minX; xOffset <= maxX; xOffset++) {
            for (int zOffset = minZ; zOffset <= maxZ; zOffset++) {
                int maxY = world.getHeightValue(xOffset, zOffset);

                for (int yOffset = 0; yOffset < maxY; yOffset++) {
                    Block block = world.getBlock(xOffset, yOffset, zOffset);

                    if (block != null && block.isLeaves(world, xOffset, yOffset, zOffset)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void applyThresholds(World world, int x, int y, int z, Random random, int value, NodeType type,
        NodeModifier modifier) {
        int water = countBlocksAroundPlayer(world, x, y, z, Material.water);
        int lava = countBlocksAroundPlayer(world, x, y, z, Material.lava);
        int stone = countBlocksAroundPlayer(world, x, y, z, Blocks.stone.getMaterial());
        int foliage = optimizationsAndTweaks$countFoliageAroundPlayer(world, x, y, z);

        final int THRESHOLD_WATER = 100;
        final int THRESHOLD_LAVA = 100;
        final int THRESHOLD_STONE = 500;
        final int THRESHOLD_FOLIAGE = 100;

        AspectList al = new AspectList();

        if (water > THRESHOLD_WATER) {
            al.merge(Aspect.WATER, 1);
        }

        if (lava > THRESHOLD_LAVA) {
            al.merge(Aspect.FIRE, 1);
            al.merge(Aspect.EARTH, 1);
        }

        if (stone > THRESHOLD_STONE) {
            al.merge(Aspect.EARTH, 1);
        }

        if (foliage > THRESHOLD_FOLIAGE) {
            al.merge(Aspect.PLANT, 1);
        }

        int totalAmount = al.size();
        int[] spread = new int[totalAmount];
        float totalSpread = 0.0F;

        for (int a = 0; a < totalAmount; ++a) {
            int aspectAmount = al.getAmount(al.getAspectsSorted()[a]);
            spread[a] = (aspectAmount == 2) ? 50 + random.nextInt(25) : 25 + random.nextInt(50);
            totalSpread += spread[a];
        }

        for (int a = 0; a < totalAmount; ++a) {
            float spreadValue = spread[a] / totalSpread * value;
            al.merge(al.getAspectsSorted()[a], (int) spreadValue);
        }

        createNodeAt(world, x, y, z, type, modifier, al);
    }

}
