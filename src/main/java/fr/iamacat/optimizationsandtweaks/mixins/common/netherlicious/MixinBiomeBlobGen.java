package fr.iamacat.optimizationsandtweaks.mixins.common.netherlicious;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import DelirusCrux.Netherlicious.World.Features.Terrain.BiomeBlobGen;

@Mixin(BiomeBlobGen.class)
public abstract class MixinBiomeBlobGen extends WorldGenerator {

    @Shadow
    private Block splotchBlock;
    @Shadow
    private int splotchBlockMeta;
    @Shadow
    private int numberOfBlocks;
    @Shadow
    private List blockList;

    @Unique
    private void optimizationsAndTweaks$generateSplotches(World world, Random random, int x, int y, int z) {
        float var6 = random.nextFloat() * 3.1415927F;
        double var7 = ((float) (x + 8) + MathHelper.sin(var6) * (float) numberOfBlocks / 8.0F);
        double var9 = ((float) (x + 8) - MathHelper.sin(var6) * (float) numberOfBlocks / 8.0F);
        double var11 = ((float) (z + 8) + MathHelper.cos(var6) * (float) numberOfBlocks / 8.0F);
        double var13 = ((float) (z + 8) - MathHelper.cos(var6) * (float) numberOfBlocks / 8.0F);
        double var15 = (y + random.nextInt(3) - 2);
        double var17 = (y + random.nextInt(3) - 2);

        for (int var19 = 0; var19 <= numberOfBlocks; ++var19) {
            double var20 = var7 + (var9 - var7) * var19 / numberOfBlocks;
            double var22 = var15 + (var17 - var15) * var19 / numberOfBlocks;
            double var24 = var11 + (var13 - var11) * var19 / numberOfBlocks;
            double var26 = random.nextDouble() * numberOfBlocks / 16.0;
            double var28 = (double) (MathHelper.sin(var19 * 3.1415927F / numberOfBlocks) + 1.0F) * var26 + 1.0;
            double var30 = (double) (MathHelper.sin(var19 * 3.1415927F / numberOfBlocks) + 1.0F) * var26 + 1.0;

            int var32 = MathHelper.floor_double(var20 - var28 / 2.0);
            int var33 = MathHelper.floor_double(var22 - var30 / 2.0);
            int var34 = MathHelper.floor_double(var24 - var28 / 2.0);
            int var35 = MathHelper.floor_double(var20 + var28 / 2.0);
            int var36 = MathHelper.floor_double(var22 + var30 / 2.0);
            int var37 = MathHelper.floor_double(var24 + var28 / 2.0);

            if (world.checkChunksExist(var32, var33, var34, var35, var36, var37)) {
                optimizationsAndTweaks$processBlocks(
                    world,
                    var32,
                    var33,
                    var34,
                    var35,
                    var36,
                    var37,
                    var20,
                    var22,
                    var24,
                    var28,
                    var30);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processBlocks(World world, int var32, int var33, int var34, int var35,
        int var36, int var37, double var20, double var22, double var24, double var28, double var30) {
        for (int var38 = var32; var38 <= var35; ++var38) {
            double var39 = (var38 + 0.5 - var20) / (var28 / 2.0);
            if (var39 * var39 < 1.0) {
                optimizationsAndTweaks$processYAxis(
                    world,
                    var33,
                    var34,
                    var36,
                    var37,
                    var38,
                    var39,
                    var20,
                    var22,
                    var24,
                    var28,
                    var30);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processYAxis(World world, int var33, int var34, int var36, int var37, int var38,
        double var39, double var20, double var22, double var24, double var28, double var30) {
        for (int var41 = var33; var41 <= var36; ++var41) {
            double var42 = (var41 + 0.5 - var22) / (var30 / 2.0);
            if (var39 * var39 + var42 * var42 < 1.0) {
                optimizationsAndTweaks$processZAxis(
                    world,
                    var34,
                    var37,
                    var38,
                    var41,
                    var42,
                    var20,
                    var24,
                    var28,
                    var39);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processZAxis(World world, int var34, int var37, int var38, int var41,
        double var42, double var20, double var24, double var28, double var39) {
        for (int var44 = var34; var44 <= var37; ++var44) {
            double var45 = (var44 + 0.5 - var24) / (var28 / 2.0);
            if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0) {
                optimizationsAndTweaks$processBlock(world, var38, var41, var44);
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$processBlock(World world, int var38, int var41, int var44) {
        Block block = world.getBlock(var38, var41, var44);
        if (block != Blocks.air && blockList.contains(block)) {
            world.setBlock(var38, var41, var44, splotchBlock, splotchBlockMeta, 2);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean func_76484_a(World world, Random random, int x, int y, int z) {
        this.optimizationsAndTweaks$generateSplotches(world, random, x, y, z);
        return true;
    }
}
