package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLeaves.class)
public abstract class MixinBlockLeaves extends BlockLeavesBase implements IShearable {

    @Unique
    private static final int SEARCH_RADIUS = 4;
    @Unique
    private static final int AREA_SIZE = 2 * SEARCH_RADIUS + 1;

    protected MixinBlockLeaves(Material p_i45433_1_, boolean p_i45433_2_) {
        super(p_i45433_1_, p_i45433_2_);
    }

    /**
     * @reason
     */
    @Inject(method = "updateTick", at = @At("HEAD"), cancellable = true)
    public void updateTick(World worldIn, int x, int y, int z, Random random, CallbackInfo ci) {
        if (worldIn.isRemote) {
            return;
        }

        int metadata = worldIn.getBlockMetadata(x, y, z);

        if (optimizationsAndTweaks$shouldUpdateLeaves(metadata)) {
            int areaSize = 2 * SEARCH_RADIUS + 1;
            int halfArea = AREA_SIZE / 2;
            int chunkX = x >> 4;
            int chunkZ = z >> 4;

            if (worldIn.getChunkFromChunkCoords(chunkX, chunkZ)
                .getTopFilledSegment() >= y >> 4) {
                int[] blockArray = optimizationsAndTweaks$initializeBlockArray(areaSize);

                optimizationsAndTweaks$populateBlockArray(worldIn, x, y, z, areaSize, halfArea, blockArray);

                for (int iteration = 1; iteration <= 4; ++iteration) {
                    optimizationsAndTweaks$propagateDecayIteration(blockArray, areaSize, halfArea, iteration);
                }

                int centralIndex = halfArea * areaSize * areaSize + halfArea * areaSize + halfArea;
                int centralBlockStatus = blockArray[centralIndex];

                optimizationsAndTweaks$updateWorld(metadata, worldIn, x, y, z, centralBlockStatus);
            }
        }

        ci.cancel();
    }

    @Unique
    private boolean optimizationsAndTweaks$shouldUpdateLeaves(int metadata) {
        return (metadata & 12) == 8 && (metadata & 4) == 0;
    }

    @Unique
    private int[] optimizationsAndTweaks$initializeBlockArray(int areaSize) {
        return new int[areaSize * areaSize * areaSize];
    }

    @Unique
    private void optimizationsAndTweaks$populateBlockArray(World worldIn, int x, int y, int z, int areaSize,
        int halfArea, int[] blockArray) {
        int searchRadius = 4;
        Block[][][] cachedBlocks = new Block[2 * searchRadius + 1][2 * searchRadius + 1][2 * searchRadius + 1];

        // Populate cachedBlocks array
        for (int xOffset = -searchRadius; xOffset <= searchRadius; ++xOffset) {
            for (int yOffset = -searchRadius; yOffset <= searchRadius; ++yOffset) {
                for (int zOffset = -searchRadius; zOffset <= searchRadius; ++zOffset) {
                    int cachedX = xOffset + searchRadius;
                    int cachedY = yOffset + searchRadius;
                    int cachedZ = zOffset + searchRadius;

                    cachedBlocks[cachedX][cachedY][cachedZ] = worldIn.getBlock(x + xOffset, y + yOffset, z + zOffset);
                }
            }
        }

        // Populate blockArray using cachedBlocks
        for (int xOffset = -searchRadius; xOffset <= searchRadius; ++xOffset) {
            for (int yOffset = -searchRadius; yOffset <= searchRadius; ++yOffset) {
                for (int zOffset = -searchRadius; zOffset <= searchRadius; ++zOffset) {
                    int cachedX = xOffset + searchRadius;
                    int cachedY = yOffset + searchRadius;
                    int cachedZ = zOffset + searchRadius;

                    Block block = cachedBlocks[cachedX][cachedY][cachedZ];
                    int index = (xOffset + halfArea) * areaSize * areaSize + (yOffset + halfArea) * areaSize
                        + zOffset
                        + halfArea;

                    blockArray[index] = optimizationsAndTweaks$determineBlockArrayValue(
                        block,
                        worldIn,
                        x + xOffset,
                        y + yOffset,
                        z + zOffset);
                }
            }
        }
    }

    @Unique
    private int optimizationsAndTweaks$determineBlockArrayValue(Block block, World worldIn, int x, int y, int z) {
        return block.isLeaves(worldIn, x, y, z) ? -2 : (block.canSustainLeaves(worldIn, x, y, z) ? 0 : -1);
    }

    @Unique
    private void optimizationsAndTweaks$propagateDecayIteration(int[] blockArray, int areaSize, int halfArea,
        int iteration) {
        int searchRadius = 4;

        for (int xOffset = -searchRadius; xOffset <= searchRadius; ++xOffset) {
            for (int yOffset = -searchRadius; yOffset <= searchRadius; ++yOffset) {
                for (int zOffset = -searchRadius; zOffset <= searchRadius; ++zOffset) {
                    int index = (xOffset + halfArea) * areaSize * areaSize + (yOffset + halfArea) * areaSize
                        + zOffset
                        + halfArea;

                    if (blockArray[index] == iteration - 1) {
                        optimizationsAndTweaks$propagateDecayToNeighbors(blockArray, index, areaSize, iteration);
                    }
                }
            }
        }
    }

    @Unique
    private void optimizationsAndTweaks$propagateDecayToNeighbors(int[] blockArray, int index, int areaSize,
        int iteration) {
        for (int offset : new int[] { -1, 1 }) {
            optimizationsAndTweaks$propagateDecayToNeighbor(blockArray, index + offset, iteration);
            optimizationsAndTweaks$propagateDecayToNeighbor(blockArray, index + offset * areaSize, iteration);
            optimizationsAndTweaks$propagateDecayToNeighbor(
                blockArray,
                index + offset * areaSize * areaSize,
                iteration);
        }
    }

    @Unique
    private void optimizationsAndTweaks$propagateDecayToNeighbor(int[] blockArray, int index, int iteration) {
        if (index >= 0 && index < blockArray.length && blockArray[index] == -2) {
            blockArray[index] = iteration;
        }
    }

    @Unique
    private void optimizationsAndTweaks$updateWorld(int metadata, World worldIn, int x, int y, int z,
        int centralBlockStatus) {
        if (centralBlockStatus >= 0) {
            worldIn.setBlockMetadataWithNotify(x, y, z, metadata & -9, 4);
        } else {
            removeLeaves(worldIn, x, y, z);
        }
    }

    @Shadow
    private void removeLeaves(World p_150126_1_, int p_150126_2_, int p_150126_3_, int p_150126_4_) {
        this.dropBlockAsItem(
            p_150126_1_,
            p_150126_2_,
            p_150126_3_,
            p_150126_4_,
            p_150126_1_.getBlockMetadata(p_150126_2_, p_150126_3_, p_150126_4_),
            0);
        p_150126_1_.setBlockToAir(p_150126_2_, p_150126_3_, p_150126_4_);
    }

    @Shadow
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates,
                                                                   // prevents item dupe
        {
            ArrayList<ItemStack> items = getDrops(worldIn, x, y, z, meta, fortune);
            chance = ForgeEventFactory
                .fireBlockHarvesting(items, worldIn, this, x, y, z, meta, fortune, chance, false, harvesters.get());

            for (ItemStack item : items) {
                if (worldIn.rand.nextFloat() <= chance) {
                    this.dropBlockAsItem(worldIn, x, y, z, item);
                }
            }
        }
    }

    @Shadow
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        int chance = this.func_150123_b(metadata);

        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (world.rand.nextInt(chance) == 0)
            ret.add(new ItemStack(this.getItemDropped(metadata, world.rand, fortune), 1, this.damageDropped(metadata)));

        chance = 200;
        if (fortune > 0) {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }

        this.captureDrops(true);
        this.func_150124_c(world, x, y, z, metadata, chance); // Dammet mojang
        ret.addAll(this.captureDrops(false));
        return ret;
    }

    @Shadow
    protected int func_150123_b(int p_150123_1_) {
        return 20;
    }

    @Shadow
    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_,
        int p_150124_6_) {}

}
