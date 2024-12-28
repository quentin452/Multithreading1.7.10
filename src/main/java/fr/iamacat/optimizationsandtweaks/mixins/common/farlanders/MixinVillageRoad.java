package fr.iamacat.optimizationsandtweaks.mixins.common.farlanders;

import com.fabiulu.farlanders.common.worldgen.village.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(VillageRoad.class)
public class MixinVillageRoad {

    @Overwrite(remap = false) // TODO FIX Infinite loop caused by generate method
    public boolean func_76484_a(World world, Random rand, int i, int j, int k) {
        for(int posX = i + 1; posX <= i + 4; ++posX) {
            for(int posZ = k - 1; posZ >= k - 16; --posZ) {
                if (world.getBlock(posX, j, posZ) == Blocks.air || world.getBlock(posX, j, posZ) == Blocks.tallgrass || world.getBlock(posX, j, posZ) == Blocks.sapling || world.getBlock(posX, j, posZ) == Blocks.deadbush || world.getBlock(posX, j, posZ) == Blocks.cactus || world.getBlock(posX, j, posZ) == Blocks.red_flower || world.getBlock(posX, j, posZ) == Blocks.yellow_flower || world.getBlock(posX, j, posZ) == Blocks.water || world.getBlock(posX, j, posZ) == Blocks.lava || world.getBlock(posX, j, posZ) == Blocks.log || world.getBlock(posX, j, posZ) == Blocks.leaves || world.getBlock(posX, j, posZ) == Blocks.vine || world.getBlock(posX, j, posZ) == Blocks.snow || world.getBlock(posX, j + 1, posZ) != Blocks.air && world.getBlock(posX, j + 1, posZ) != Blocks.tallgrass && world.getBlock(posX, j + 1, posZ) != Blocks.sapling && world.getBlock(posX, j + 1, posZ) != Blocks.deadbush && world.getBlock(posX, j + 1, posZ) != Blocks.cactus && world.getBlock(posX, j + 1, posZ) != Blocks.red_flower && world.getBlock(posX, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(posX, j + 1, posZ) != Blocks.log && world.getBlock(posX, j + 1, posZ) != Blocks.leaves && world.getBlock(posX, j + 1, posZ) != Blocks.vine && world.getBlock(posX, j + 1, posZ) != Blocks.snow) {
                    if (world.getBlock(posX, j, posZ) == Blocks.air || world.getBlock(posX, j, posZ) == Blocks.tallgrass || world.getBlock(posX, j, posZ) == Blocks.sapling || world.getBlock(posX, j, posZ) == Blocks.deadbush || world.getBlock(posX, j, posZ) == Blocks.cactus || world.getBlock(posX, j, posZ) == Blocks.red_flower || world.getBlock(posX, j, posZ) == Blocks.yellow_flower && world.getBlock(posX, j, posZ) == Blocks.log && world.getBlock(posX, j, posZ) == Blocks.leaves && world.getBlock(posX, j, posZ) == Blocks.vine && world.getBlock(posX, j, posZ) == Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j - 1;

                        while(!canMoveOn) {
                            if (world.getBlock(posX, posY, posZ) != Blocks.water && world.getBlock(posX, posY, posZ) != Blocks.lava) {
                                if (world.getBlock(posX, posY, posZ) != Blocks.air && world.getBlock(posX, posY, posZ) != Blocks.tallgrass && world.getBlock(posX, posY, posZ) != Blocks.sapling && world.getBlock(posX, posY, posZ) != Blocks.deadbush && world.getBlock(posX, posY, posZ) != Blocks.cactus && world.getBlock(posX, posY, posZ) != Blocks.red_flower && world.getBlock(posX, posY, posZ) != Blocks.yellow_flower && world.getBlock(posX, posY, posZ) != Blocks.log && world.getBlock(posX, posY, posZ) != Blocks.leaves && world.getBlock(posX, posY, posZ) != Blocks.vine && world.getBlock(posX, posY, posZ) != Blocks.snow) {
                                    world.setBlock(posX, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                } else {
                                    --posY;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    } else if (world.getBlock(posX, j, posZ) != Blocks.air && world.getBlock(posX, j, posZ) != Blocks.tallgrass && world.getBlock(posX, j, posZ) != Blocks.sapling && world.getBlock(posX, j, posZ) != Blocks.deadbush && world.getBlock(posX, j, posZ) != Blocks.cactus && world.getBlock(posX, j, posZ) != Blocks.red_flower && world.getBlock(posX, j, posZ) != Blocks.yellow_flower && world.getBlock(posX, j, posZ) != Blocks.water && world.getBlock(posX, j, posZ) != Blocks.lava && world.getBlock(posX, j, posZ) != Blocks.log && world.getBlock(posX, j, posZ) != Blocks.leaves && world.getBlock(posX, j, posZ) != Blocks.vine && world.getBlock(posX, j, posZ) != Blocks.snow && world.getBlock(posX, j + 1, posZ) != Blocks.air && world.getBlock(posX, j + 1, posZ) != Blocks.tallgrass && world.getBlock(posX, j + 1, posZ) != Blocks.sapling && world.getBlock(posX, j + 1, posZ) != Blocks.deadbush && world.getBlock(posX, j + 1, posZ) != Blocks.cactus && world.getBlock(posX, j + 1, posZ) != Blocks.red_flower && world.getBlock(posX, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(posX, j + 1, posZ) != Blocks.log && world.getBlock(posX, j + 1, posZ) != Blocks.leaves && world.getBlock(posX, j + 1, posZ) != Blocks.vine && world.getBlock(posX, j + 1, posZ) != Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j + 1;

                        while(!canMoveOn) {
                            if (world.getBlock(posX, posY, posZ) != Blocks.water && world.getBlock(posX, posY, posZ) != Blocks.lava && world.getBlock(posX, posY + 1, posZ) != Blocks.water && world.getBlock(posX, posY + 1, posZ) != Blocks.lava) {
                                if (world.getBlock(posX, posY, posZ) != Blocks.air && world.getBlock(posX, posY, posZ) != Blocks.tallgrass && world.getBlock(posX, posY, posZ) != Blocks.sapling && world.getBlock(posX, posY, posZ) != Blocks.deadbush && world.getBlock(posX, posY, posZ) != Blocks.cactus && world.getBlock(posX, posY, posZ) != Blocks.red_flower && world.getBlock(posX, posY, posZ) != Blocks.yellow_flower && world.getBlock(posX, posY, posZ) != Blocks.water && world.getBlock(posX, posY, posZ) != Blocks.lava && world.getBlock(posX, posY, posZ) != Blocks.log && world.getBlock(posX, posY, posZ) != Blocks.leaves && world.getBlock(posX, posY, posZ) != Blocks.vine && world.getBlock(posX, posY, posZ) != Blocks.snow && world.getBlock(posX, posY + 1, posZ) != Blocks.air && world.getBlock(posX, posY + 1, posZ) != Blocks.tallgrass && world.getBlock(posX, posY + 1, posZ) != Blocks.sapling && world.getBlock(posX, posY + 1, posZ) != Blocks.deadbush && world.getBlock(posX, posY + 1, posZ) != Blocks.cactus && world.getBlock(posX, posY + 1, posZ) != Blocks.red_flower && world.getBlock(posX, posY + 1, posZ) != Blocks.yellow_flower && world.getBlock(posX, posY + 1, posZ) != Blocks.water && world.getBlock(posX, posY + 1, posZ) != Blocks.lava && world.getBlock(posX, posY + 1, posZ) != Blocks.log && world.getBlock(posX, posY + 1, posZ) != Blocks.leaves && world.getBlock(posX, posY + 1, posZ) != Blocks.vine && world.getBlock(posX, posY + 1, posZ) != Blocks.snow) {
                                    ++posY;
                                } else {
                                    world.setBlock(posX, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    }
                } else {
                    world.setBlock(posX, j, posZ, Blocks.gravel, 0, 2);
                }
            }
        }

        for(int var10 = i + 1; var10 <= i + 4; ++var10) {
            for(int posZ = k + 6; posZ <= k + 26; ++posZ) {
                if (world.getBlock(var10, j, posZ) == Blocks.air || world.getBlock(var10, j, posZ) == Blocks.tallgrass || world.getBlock(var10, j, posZ) == Blocks.sapling || world.getBlock(var10, j, posZ) == Blocks.deadbush || world.getBlock(var10, j, posZ) == Blocks.cactus || world.getBlock(var10, j, posZ) == Blocks.red_flower || world.getBlock(var10, j, posZ) == Blocks.yellow_flower || world.getBlock(var10, j, posZ) == Blocks.water || world.getBlock(var10, j, posZ) == Blocks.lava || world.getBlock(var10, j, posZ) == Blocks.log || world.getBlock(var10, j, posZ) == Blocks.leaves || world.getBlock(var10, j, posZ) == Blocks.vine || world.getBlock(var10, j, posZ) == Blocks.snow || world.getBlock(var10, j + 1, posZ) != Blocks.air && world.getBlock(var10, j + 1, posZ) != Blocks.tallgrass && world.getBlock(var10, j + 1, posZ) != Blocks.sapling && world.getBlock(var10, j + 1, posZ) != Blocks.deadbush && world.getBlock(var10, j + 1, posZ) != Blocks.cactus && world.getBlock(var10, j + 1, posZ) != Blocks.red_flower && world.getBlock(var10, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(var10, j + 1, posZ) != Blocks.log && world.getBlock(var10, j + 1, posZ) != Blocks.leaves && world.getBlock(var10, j + 1, posZ) != Blocks.vine && world.getBlock(var10, j + 1, posZ) != Blocks.snow) {
                    if (world.getBlock(var10, j, posZ) == Blocks.air || world.getBlock(var10, j, posZ) == Blocks.tallgrass || world.getBlock(var10, j, posZ) == Blocks.sapling || world.getBlock(var10, j, posZ) == Blocks.deadbush || world.getBlock(var10, j, posZ) == Blocks.cactus || world.getBlock(var10, j, posZ) == Blocks.red_flower || world.getBlock(var10, j, posZ) == Blocks.yellow_flower && world.getBlock(var10, j, posZ) == Blocks.log && world.getBlock(var10, j, posZ) == Blocks.leaves && world.getBlock(var10, j, posZ) == Blocks.vine && world.getBlock(var10, j, posZ) == Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j - 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var10, posY, posZ) != Blocks.water && world.getBlock(var10, posY, posZ) != Blocks.lava) {
                                if (world.getBlock(var10, posY, posZ) != Blocks.air && world.getBlock(var10, posY, posZ) != Blocks.tallgrass && world.getBlock(var10, posY, posZ) != Blocks.sapling && world.getBlock(var10, posY, posZ) != Blocks.deadbush && world.getBlock(var10, posY, posZ) != Blocks.cactus && world.getBlock(var10, posY, posZ) != Blocks.red_flower && world.getBlock(var10, posY, posZ) != Blocks.yellow_flower && world.getBlock(var10, posY, posZ) != Blocks.log && world.getBlock(var10, posY, posZ) != Blocks.leaves && world.getBlock(var10, posY, posZ) != Blocks.vine && world.getBlock(var10, posY, posZ) != Blocks.snow) {
                                    world.setBlock(var10, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                } else {
                                    --posY;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    } else if (world.getBlock(var10, j, posZ) != Blocks.air && world.getBlock(var10, j, posZ) != Blocks.tallgrass && world.getBlock(var10, j, posZ) != Blocks.sapling && world.getBlock(var10, j, posZ) != Blocks.deadbush && world.getBlock(var10, j, posZ) != Blocks.cactus && world.getBlock(var10, j, posZ) != Blocks.red_flower && world.getBlock(var10, j, posZ) != Blocks.yellow_flower && world.getBlock(var10, j, posZ) != Blocks.water && world.getBlock(var10, j, posZ) != Blocks.lava && world.getBlock(var10, j, posZ) != Blocks.log && world.getBlock(var10, j, posZ) != Blocks.leaves && world.getBlock(var10, j, posZ) != Blocks.vine && world.getBlock(var10, j, posZ) != Blocks.snow && world.getBlock(var10, j + 1, posZ) != Blocks.air && world.getBlock(var10, j + 1, posZ) != Blocks.tallgrass && world.getBlock(var10, j + 1, posZ) != Blocks.sapling && world.getBlock(var10, j + 1, posZ) != Blocks.deadbush && world.getBlock(var10, j + 1, posZ) != Blocks.cactus && world.getBlock(var10, j + 1, posZ) != Blocks.red_flower && world.getBlock(var10, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(var10, j + 1, posZ) != Blocks.log && world.getBlock(var10, j + 1, posZ) != Blocks.leaves && world.getBlock(var10, j + 1, posZ) != Blocks.vine && world.getBlock(var10, j + 1, posZ) != Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j + 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var10, posY, posZ) != Blocks.water && world.getBlock(var10, posY, posZ) != Blocks.lava && world.getBlock(var10, posY + 1, posZ) != Blocks.water && world.getBlock(var10, posY + 1, posZ) != Blocks.lava) {
                                if (world.getBlock(var10, posY, posZ) != Blocks.air && world.getBlock(var10, posY, posZ) != Blocks.tallgrass && world.getBlock(var10, posY, posZ) != Blocks.sapling && world.getBlock(var10, posY, posZ) != Blocks.deadbush && world.getBlock(var10, posY, posZ) != Blocks.cactus && world.getBlock(var10, posY, posZ) != Blocks.red_flower && world.getBlock(var10, posY, posZ) != Blocks.yellow_flower && world.getBlock(var10, posY, posZ) != Blocks.water && world.getBlock(var10, posY, posZ) != Blocks.lava && world.getBlock(var10, posY, posZ) != Blocks.log && world.getBlock(var10, posY, posZ) != Blocks.leaves && world.getBlock(var10, posY, posZ) != Blocks.vine && world.getBlock(var10, posY, posZ) != Blocks.snow && world.getBlock(var10, posY + 1, posZ) != Blocks.air && world.getBlock(var10, posY + 1, posZ) != Blocks.tallgrass && world.getBlock(var10, posY + 1, posZ) != Blocks.sapling && world.getBlock(var10, posY + 1, posZ) != Blocks.deadbush && world.getBlock(var10, posY + 1, posZ) != Blocks.cactus && world.getBlock(var10, posY + 1, posZ) != Blocks.red_flower && world.getBlock(var10, posY + 1, posZ) != Blocks.yellow_flower && world.getBlock(var10, posY + 1, posZ) != Blocks.water && world.getBlock(var10, posY + 1, posZ) != Blocks.lava && world.getBlock(var10, posY + 1, posZ) != Blocks.log && world.getBlock(var10, posY + 1, posZ) != Blocks.leaves && world.getBlock(var10, posY + 1, posZ) != Blocks.vine && world.getBlock(var10, posY + 1, posZ) != Blocks.snow) {
                                    ++posY;
                                } else {
                                    world.setBlock(var10, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    }
                } else {
                    world.setBlock(var10, j, posZ, Blocks.gravel, 0, 2);
                }
            }
        }

        for(int posZ = k + 1; posZ <= k + 4; ++posZ) {
            for(int var11 = i - 1; var11 >= i - 25; --var11) {
                if (world.getBlock(var11, j, posZ) == Blocks.air || world.getBlock(var11, j, posZ) == Blocks.tallgrass || world.getBlock(var11, j, posZ) == Blocks.sapling || world.getBlock(var11, j, posZ) == Blocks.deadbush || world.getBlock(var11, j, posZ) == Blocks.cactus || world.getBlock(var11, j, posZ) == Blocks.red_flower || world.getBlock(var11, j, posZ) == Blocks.yellow_flower || world.getBlock(var11, j, posZ) == Blocks.water || world.getBlock(var11, j, posZ) == Blocks.lava || world.getBlock(var11, j, posZ) == Blocks.log || world.getBlock(var11, j, posZ) == Blocks.leaves || world.getBlock(var11, j, posZ) == Blocks.vine || world.getBlock(var11, j, posZ) == Blocks.snow || world.getBlock(var11, j + 1, posZ) != Blocks.air && world.getBlock(var11, j + 1, posZ) != Blocks.tallgrass && world.getBlock(var11, j + 1, posZ) != Blocks.sapling && world.getBlock(var11, j + 1, posZ) != Blocks.deadbush && world.getBlock(var11, j + 1, posZ) != Blocks.cactus && world.getBlock(var11, j + 1, posZ) != Blocks.red_flower && world.getBlock(var11, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(var11, j + 1, posZ) != Blocks.log && world.getBlock(var11, j + 1, posZ) != Blocks.leaves && world.getBlock(var11, j + 1, posZ) != Blocks.vine && world.getBlock(var11, j + 1, posZ) != Blocks.snow) {
                    if (world.getBlock(var11, j, posZ) == Blocks.air || world.getBlock(var11, j, posZ) == Blocks.tallgrass || world.getBlock(var11, j, posZ) == Blocks.sapling || world.getBlock(var11, j, posZ) == Blocks.deadbush || world.getBlock(var11, j, posZ) == Blocks.cactus || world.getBlock(var11, j, posZ) == Blocks.red_flower || world.getBlock(var11, j, posZ) == Blocks.yellow_flower && world.getBlock(var11, j, posZ) == Blocks.log && world.getBlock(var11, j, posZ) == Blocks.leaves && world.getBlock(var11, j, posZ) == Blocks.vine && world.getBlock(var11, j, posZ) == Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j - 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var11, posY, posZ) != Blocks.water && world.getBlock(var11, posY, posZ) != Blocks.lava) {
                                if (world.getBlock(var11, posY, posZ) != Blocks.air && world.getBlock(var11, posY, posZ) != Blocks.tallgrass && world.getBlock(var11, posY, posZ) != Blocks.sapling && world.getBlock(var11, posY, posZ) != Blocks.deadbush && world.getBlock(var11, posY, posZ) != Blocks.cactus && world.getBlock(var11, posY, posZ) != Blocks.red_flower && world.getBlock(var11, posY, posZ) != Blocks.yellow_flower && world.getBlock(var11, posY, posZ) != Blocks.log && world.getBlock(var11, posY, posZ) != Blocks.leaves && world.getBlock(var11, posY, posZ) != Blocks.vine && world.getBlock(var11, posY, posZ) != Blocks.snow) {
                                    world.setBlock(var11, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                } else {
                                    --posY;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    } else if (world.getBlock(var11, j, posZ) != Blocks.air && world.getBlock(var11, j, posZ) != Blocks.tallgrass && world.getBlock(var11, j, posZ) != Blocks.sapling && world.getBlock(var11, j, posZ) != Blocks.deadbush && world.getBlock(var11, j, posZ) != Blocks.cactus && world.getBlock(var11, j, posZ) != Blocks.red_flower && world.getBlock(var11, j, posZ) != Blocks.yellow_flower && world.getBlock(var11, j, posZ) != Blocks.water && world.getBlock(var11, j, posZ) != Blocks.lava && world.getBlock(var11, j, posZ) != Blocks.log && world.getBlock(var11, j, posZ) != Blocks.leaves && world.getBlock(var11, j, posZ) != Blocks.vine && world.getBlock(var11, j, posZ) != Blocks.snow && world.getBlock(var11, j + 1, posZ) != Blocks.air && world.getBlock(var11, j + 1, posZ) != Blocks.tallgrass && world.getBlock(var11, j + 1, posZ) != Blocks.sapling && world.getBlock(var11, j + 1, posZ) != Blocks.deadbush && world.getBlock(var11, j + 1, posZ) != Blocks.cactus && world.getBlock(var11, j + 1, posZ) != Blocks.red_flower && world.getBlock(var11, j + 1, posZ) != Blocks.yellow_flower && world.getBlock(var11, j + 1, posZ) != Blocks.log && world.getBlock(var11, j + 1, posZ) != Blocks.leaves && world.getBlock(var11, j + 1, posZ) != Blocks.vine && world.getBlock(var11, j + 1, posZ) != Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j + 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var11, posY, posZ) != Blocks.water && world.getBlock(var11, posY, posZ) != Blocks.lava && world.getBlock(var11, posY + 1, posZ) != Blocks.water && world.getBlock(var11, posY + 1, posZ) != Blocks.lava) {
                                if (world.getBlock(var11, posY, posZ) != Blocks.air && world.getBlock(var11, posY, posZ) != Blocks.tallgrass && world.getBlock(var11, posY, posZ) != Blocks.sapling && world.getBlock(var11, posY, posZ) != Blocks.deadbush && world.getBlock(var11, posY, posZ) != Blocks.cactus && world.getBlock(var11, posY, posZ) != Blocks.red_flower && world.getBlock(var11, posY, posZ) != Blocks.yellow_flower && world.getBlock(var11, posY, posZ) != Blocks.water && world.getBlock(var11, posY, posZ) != Blocks.lava && world.getBlock(var11, posY, posZ) != Blocks.log && world.getBlock(var11, posY, posZ) != Blocks.leaves && world.getBlock(var11, posY, posZ) != Blocks.vine && world.getBlock(var11, posY, posZ) != Blocks.snow && world.getBlock(var11, posY + 1, posZ) != Blocks.air && world.getBlock(var11, posY + 1, posZ) != Blocks.tallgrass && world.getBlock(var11, posY + 1, posZ) != Blocks.sapling && world.getBlock(var11, posY + 1, posZ) != Blocks.deadbush && world.getBlock(var11, posY + 1, posZ) != Blocks.cactus && world.getBlock(var11, posY + 1, posZ) != Blocks.red_flower && world.getBlock(var11, posY + 1, posZ) != Blocks.yellow_flower && world.getBlock(var11, posY + 1, posZ) != Blocks.water && world.getBlock(var11, posY + 1, posZ) != Blocks.lava && world.getBlock(var11, posY + 1, posZ) != Blocks.log && world.getBlock(var11, posY + 1, posZ) != Blocks.leaves && world.getBlock(var11, posY + 1, posZ) != Blocks.vine && world.getBlock(var11, posY + 1, posZ) != Blocks.snow) {
                                    ++posY;
                                } else {
                                    world.setBlock(var11, posY, posZ, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    }
                } else {
                    world.setBlock(var11, j, posZ, Blocks.gravel, 0, 2);
                }
            }
        }

        for(int var22 = k + 1; var22 <= k + 4; ++var22) {
            for(int var12 = i + 6; var12 <= i + 25; ++var12) {
                if (world.getBlock(var12, j, var22) == Blocks.air || world.getBlock(var12, j, var22) == Blocks.tallgrass || world.getBlock(var12, j, var22) == Blocks.sapling || world.getBlock(var12, j, var22) == Blocks.deadbush || world.getBlock(var12, j, var22) == Blocks.cactus || world.getBlock(var12, j, var22) == Blocks.red_flower || world.getBlock(var12, j, var22) == Blocks.yellow_flower || world.getBlock(var12, j, var22) == Blocks.water || world.getBlock(var12, j, var22) == Blocks.lava || world.getBlock(var12, j, var22) == Blocks.log || world.getBlock(var12, j, var22) == Blocks.leaves || world.getBlock(var12, j, var22) == Blocks.vine || world.getBlock(var12, j, var22) == Blocks.snow || world.getBlock(var12, j + 1, var22) != Blocks.air && world.getBlock(var12, j + 1, var22) != Blocks.tallgrass && world.getBlock(var12, j + 1, var22) != Blocks.sapling && world.getBlock(var12, j + 1, var22) != Blocks.deadbush && world.getBlock(var12, j + 1, var22) != Blocks.cactus && world.getBlock(var12, j + 1, var22) != Blocks.red_flower && world.getBlock(var12, j + 1, var22) != Blocks.yellow_flower && world.getBlock(var12, j + 1, var22) != Blocks.log && world.getBlock(var12, j + 1, var22) != Blocks.leaves && world.getBlock(var12, j + 1, var22) != Blocks.vine && world.getBlock(var12, j + 1, var22) != Blocks.snow) {
                    if (world.getBlock(var12, j, var22) == Blocks.air || world.getBlock(var12, j, var22) == Blocks.tallgrass || world.getBlock(var12, j, var22) == Blocks.sapling || world.getBlock(var12, j, var22) == Blocks.deadbush || world.getBlock(var12, j, var22) == Blocks.cactus || world.getBlock(var12, j, var22) == Blocks.red_flower || world.getBlock(var12, j, var22) == Blocks.yellow_flower && world.getBlock(var12, j, var22) == Blocks.log && world.getBlock(var12, j, var22) == Blocks.leaves && world.getBlock(var12, j, var22) == Blocks.vine && world.getBlock(var12, j, var22) == Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j - 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var12, posY, var22) != Blocks.water && world.getBlock(var12, posY, var22) != Blocks.lava) {
                                if (world.getBlock(var12, posY, var22) != Blocks.air && world.getBlock(var12, posY, var22) != Blocks.tallgrass && world.getBlock(var12, posY, var22) != Blocks.sapling && world.getBlock(var12, posY, var22) != Blocks.deadbush && world.getBlock(var12, posY, var22) != Blocks.cactus && world.getBlock(var12, posY, var22) != Blocks.red_flower && world.getBlock(var12, posY, var22) != Blocks.yellow_flower && world.getBlock(var12, posY, var22) != Blocks.log && world.getBlock(var12, posY, var22) != Blocks.leaves && world.getBlock(var12, posY, var22) != Blocks.vine && world.getBlock(var12, posY, var22) != Blocks.snow) {
                                    world.setBlock(var12, posY, var22, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                } else {
                                    --posY;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    } else if (world.getBlock(var12, j, var22) != Blocks.air && world.getBlock(var12, j, var22) != Blocks.tallgrass && world.getBlock(var12, j, var22) != Blocks.sapling && world.getBlock(var12, j, var22) != Blocks.deadbush && world.getBlock(var12, j, var22) != Blocks.cactus && world.getBlock(var12, j, var22) != Blocks.red_flower && world.getBlock(var12, j, var22) != Blocks.yellow_flower && world.getBlock(var12, j, var22) != Blocks.water && world.getBlock(var12, j, var22) != Blocks.lava && world.getBlock(var12, j, var22) != Blocks.log && world.getBlock(var12, j, var22) != Blocks.leaves && world.getBlock(var12, j, var22) != Blocks.vine && world.getBlock(var12, j, var22) != Blocks.snow && world.getBlock(var12, j + 1, var22) != Blocks.air && world.getBlock(var12, j + 1, var22) != Blocks.tallgrass && world.getBlock(var12, j + 1, var22) != Blocks.sapling && world.getBlock(var12, j + 1, var22) != Blocks.deadbush && world.getBlock(var12, j + 1, var22) != Blocks.cactus && world.getBlock(var12, j + 1, var22) != Blocks.red_flower && world.getBlock(var12, j + 1, var22) != Blocks.yellow_flower && world.getBlock(var12, j + 1, var22) != Blocks.log && world.getBlock(var12, j + 1, var22) != Blocks.leaves && world.getBlock(var12, j + 1, var22) != Blocks.vine && world.getBlock(var12, j + 1, var22) != Blocks.snow) {
                        boolean canMoveOn = false;
                        int posY = j + 1;

                        while(!canMoveOn) {
                            if (world.getBlock(var12, posY, var22) != Blocks.water && world.getBlock(var12, posY, var22) != Blocks.lava && world.getBlock(var12, posY + 1, var22) != Blocks.water && world.getBlock(var12, posY + 1, var22) != Blocks.lava) {
                                if (world.getBlock(var12, posY, var22) != Blocks.air && world.getBlock(var12, posY, var22) != Blocks.tallgrass && world.getBlock(var12, posY, var22) != Blocks.sapling && world.getBlock(var12, posY, var22) != Blocks.deadbush && world.getBlock(var12, posY, var22) != Blocks.cactus && world.getBlock(var12, posY, var22) != Blocks.red_flower && world.getBlock(var12, posY, var22) != Blocks.yellow_flower && world.getBlock(var12, posY, var22) != Blocks.water && world.getBlock(var12, posY, var22) != Blocks.lava && world.getBlock(var12, posY, var22) != Blocks.log && world.getBlock(var12, posY, var22) != Blocks.leaves && world.getBlock(var12, posY, var22) != Blocks.vine && world.getBlock(var12, posY, var22) != Blocks.snow && world.getBlock(var12, posY + 1, var22) != Blocks.air && world.getBlock(var12, posY + 1, var22) != Blocks.tallgrass && world.getBlock(var12, posY + 1, var22) != Blocks.sapling && world.getBlock(var12, posY + 1, var22) != Blocks.deadbush && world.getBlock(var12, posY + 1, var22) != Blocks.cactus && world.getBlock(var12, posY + 1, var22) != Blocks.red_flower && world.getBlock(var12, posY + 1, var22) != Blocks.yellow_flower && world.getBlock(var12, posY + 1, var22) != Blocks.water && world.getBlock(var12, posY + 1, var22) != Blocks.lava && world.getBlock(var12, posY + 1, var22) != Blocks.log && world.getBlock(var12, posY + 1, var22) != Blocks.leaves && world.getBlock(var12, posY + 1, var22) != Blocks.vine && world.getBlock(var12, posY + 1, var22) != Blocks.snow) {
                                    ++posY;
                                } else {
                                    world.setBlock(var12, posY, var22, Blocks.gravel, 0, 2);
                                    canMoveOn = true;
                                }
                            } else {
                                canMoveOn = true;
                            }
                        }
                    }
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
