package fr.iamacat.optimizationsandtweaks.mixins.common.farlanders;

import com.fabiulu.farlanders.common.worldgen.village.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(VillageRoad.class)
public class MixinVillageRoad {

    private boolean multithreadingandtweaks$isValidBlock(Block block) {
        return block == Blocks.air || block == Blocks.tallgrass || block == Blocks.sapling || block == Blocks.sand ||
            block == Blocks.deadbush || block == Blocks.cactus || block == Blocks.red_flower ||
            block == Blocks.yellow_flower || block == Blocks.water || block == Blocks.lava ||
            block == Blocks.log || block == Blocks.leaves || block == Blocks.vine || block == Blocks.snow;
    }

    private void multithreadingandtweaks$placeGravelIfValid(World world, int x, int y, int z) {
        if (!multithreadingandtweaks$isValidBlock(world.getBlock(x, y, z))) {
            world.setBlock(x, y, z, Blocks.gravel, 0, 2);
        }
    }

    @Overwrite(remap = false) // FIX Infinite loop caused by generate method
    public boolean func_76484_a(World world, Random rand, int i, int j, int k) {
        for (int posX = i + 1; posX <= i + 4; ++posX) {
            for (int posZ = k - 1; posZ >= k - 16; --posZ) {
                if (multithreadingandtweaks$isValidBlock(world.getBlock(posX, j, posZ)) || multithreadingandtweaks$isValidBlock(world.getBlock(posX, j + 1, posZ))) {
                    multithreadingandtweaks$placeGravelIfValid(world, posX, j, posZ);
                }
            }
        }
        for (int var10 = i + 1; var10 <= i + 4; ++var10) {
            for (int posZ = k + 6; posZ <= k + 26; ++posZ) {
                if (multithreadingandtweaks$isValidBlock(world.getBlock(var10, j, posZ)) || multithreadingandtweaks$isValidBlock(world.getBlock(var10, j + 1, posZ))) {
                    multithreadingandtweaks$placeGravelIfValid(world, var10, j, posZ);
                }
            }
        }
        for (int posZ = k + 1; posZ <= k + 4; ++posZ) {
            for (int var11 = i - 1; var11 >= i - 25; --var11) {
                if (multithreadingandtweaks$isValidBlock(world.getBlock(var11, j, posZ)) || multithreadingandtweaks$isValidBlock(world.getBlock(var11, j + 1, posZ))) {
                    multithreadingandtweaks$placeGravelIfValid(world, var11, j, posZ);
                }
            }
        }
        for (int var22 = k + 1; var22 <= k +
            4; ++var22) {
            for (int var12 = i + 6; var12 <= i + 25; ++var12) {
                if (multithreadingandtweaks$isValidBlock(world.getBlock(var12, j, var22)) ||
                    multithreadingandtweaks$isValidBlock(world.getBlock(var12, j + 1, var22))) {
                    multithreadingandtweaks$placeGravelIfValid(world, var12, j, var22);
                } else {
                    world.setBlock(var12, j, var22, Blocks.gravel, 0, 2);
                }
            }
        }
        (new VillageHouseMedium()).func_76484_a(world, rand, i + 13, j - 1, k - 11);
        (new VillageHouseSmallNoDoor()).func_76484_a(world, rand, i + 15, j - 1, k + 5);
        (new VillageChurch()).func_76484_a(world, rand, i - 18, j - 1, k - 9);
        (new VillageHouseSmallNoDoor()).func_76484_a(world, rand, i - 7, j - 1, k + 5);
        (new VillageFarm()).func_76484_a(world, rand, i - 22, j, k + 5);
        (new VillageHouseSmallLadder()).func_76484_a(world, rand, i + 5, j, k - 6);
        (new VillageHouseGigantic()).func_76484_a(world, rand, i - 11, j - 1, k - 11);
        (new VillageHouseSmallDoor()).func_76484_a(world, rand, i + 5, j - 1, k - 12);
        (new VillageHouseLibrary()).func_76484_a(world, rand, i - 8, j - 1, k + 13);
        (new VillageHouseSmallDoor()).func_76484_a(world, rand, i + 5, j - 1, k + 20);
        (new VillageBlacksmith()).func_76484_a(world, rand, i + 5, j - 1, k + 8);
        return true;
    }
}
