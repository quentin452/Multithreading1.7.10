package fr.iamacat.optimizationsandtweaks.mixins.common.mowziesmobs;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import coolalias.structuregenapi.util.*;

@Mixin(StructureGeneratorBase.class)
public abstract class MixinStructureGeneratorBaseMM extends WorldGenerator {

    @Shadow
    public static final int SET_NO_BLOCK = Integer.MAX_VALUE;
    @Shadow
    public static final int SOUTH = 0;
    @Shadow
    public static final int WEST = 1;
    @Shadow
    public static final int NORTH = 2;
    @Shadow
    public static final int EAST = 3;
    @Shadow
    private int structureFacing;
    @Shadow
    private int manualRotations;
    @Shadow
    private int facing;
    @Shadow
    private int offsetX;
    @Shadow
    private int offsetY;
    @Shadow
    private int offsetZ;
    @Shadow
    private boolean removeStructure;
    @Shadow
    private int[][][][] blockArray;
    @Shadow
    private final List<int[][][][]> blockArrayList;
    @Shadow
    private final List<BlockData> postGenBlocks;

    public MixinStructureGeneratorBaseMM() {
        super(true);
        this.structureFacing = 3;
        this.manualRotations = 0;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.removeStructure = false;
        this.blockArrayList = new LinkedList<>();
        this.postGenBlocks = new LinkedList<>();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private final void setBlockAt(World world, int fakeID, int realID, int meta, int customData1, int customData2,
        int x, int y, int z) {
        Block block = Block.getBlockById(realID);
        boolean isRealBlock = realID >= 0;
        boolean isAirBlock = world.isAirBlock(x, y, z);
        boolean canBlockMove = !world.getBlock(x, y, z)
            .getMaterial()
            .blocksMovement();

        if (isRealBlock || isAirBlock || !canBlockMove) {
            BlockRotationData.Rotation rotationType = BlockRotationData.getBlockRotationType(block);

            if (rotationType != null) {
                int rotations = ((this.isOppositeAxis() ? this.structureFacing + 2 : this.structureFacing)
                    + this.facing) % 4;
                meta = GenHelper.getMetadata(rotations, block, meta);
            }

            if ((rotationType == BlockRotationData.Rotation.WALL_MOUNTED
                || rotationType == BlockRotationData.Rotation.LEVER)) {
                LogHelper.fine("Block " + block + " requires post-processing. Adding to list. Meta = " + meta);
                this.postGenBlocks.add(new BlockData(x, y, z, fakeID, meta, customData1, customData2));
            } else {
                world.setBlock(x, y, z, block, meta, 2);

                if (rotationType != null) {
                    GenHelper.setMetadata(world, x, y, z, meta);
                }

                if (Math.abs(fakeID) > 4095) {
                    this.onCustomBlockAdded(world, x, y, z, fakeID, customData1, customData2);
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private final boolean generateLayer(World world, Random random, int posX, int posY, int posZ, int rotations) {

        if (this.blockArray == null || this.blockArray.length == 0
            || this.blockArray[0] == null
            || this.blockArray[0].length == 0
            || this.blockArray[0][0] == null
            || this.blockArray[0][0].length == 0) {
            LogHelper.warning("Invalid block array.");
            return false;
        }

        int centerX = this.blockArray[0].length / 2;
        int centerZ = this.blockArray[0][0].length / 2;
        int y = this.removeStructure ? this.blockArray.length - 1 : 0;

        while (this.removeStructure ? y >= 0 : y < this.blockArray.length) {

            for (int x = 0; x < this.blockArray[y].length; ++x) {
                if (this.blockArray[y][x] == null) {
                    LogHelper.warning("Invalid block array at y=" + y + ", x=" + x);
                    continue;
                }

                for (int z = 0; z < this.blockArray[y][x].length; ++z) {
                    if (this.blockArray[y][x][z] == null || this.blockArray[y][x][z].length == 0) {
                        LogHelper.warning("Invalid block array at y=" + y + ", x=" + x + ", z=" + z);
                        continue;
                    }

                    if (this.blockArray[y][x][z][0] != Integer.MAX_VALUE) {

                        int rotX = posX;
                        int rotZ = posZ;
                        int rotY = posY + y + this.offsetY;
                        switch (rotations) {
                            case 0:
                                rotX = posX + x - centerX + this.offsetX;
                                rotZ = posZ + z - centerZ + this.offsetZ;
                                break;
                            case 1:
                                rotX = posX - (z - centerZ + this.offsetZ);
                                rotZ = posZ + x - centerX + this.offsetX;
                                break;
                            case 2:
                                rotX = posX - (x - centerX + this.offsetX);
                                rotZ = posZ - (z - centerZ + this.offsetZ);
                                break;
                            case 3:
                                rotX = posX + z - centerZ + this.offsetZ;
                                rotZ = posZ - (x - centerX + this.offsetX);
                                break;
                            default:
                                LogHelper.warning("Error computing number of rotations.");
                        }

                        int customData1 = this.blockArray[y][x][z].length > 2 ? this.blockArray[y][x][z][2] : 0;
                        int fakeID = this.blockArray[y][x][z][0];
                        int realID = Math.abs(fakeID) > 4095 ? this.getRealBlockID(fakeID, customData1) : fakeID;
                        if (this.removeStructure) {
                            if (!this.removeBlockAt(world, fakeID, realID, rotX, rotY, rotZ, rotations)) {
                                return false;
                            }
                        } else if (Math.abs(realID) > 4095) {
                            LogHelper.warning(
                                "Invalid block ID. Initial ID: " + fakeID + ", returned id from getRealID: " + realID);
                        } else {
                            int customData2 = this.blockArray[y][x][z].length > 3 ? this.blockArray[y][x][z][3] : 0;
                            int meta = this.blockArray[y][x][z].length > 1 ? this.blockArray[y][x][z][1] : 0;
                            this.setBlockAt(world, fakeID, realID, meta, customData1, customData2, rotX, rotY, rotZ);
                        }
                    }
                }
            }

            y = this.removeStructure ? y - 1 : y + 1;
        }

        return true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private final boolean removeBlockAt(World world, int fakeID, int realID, int x, int y, int z, int rotations) {
        Block realBlock = Block.getBlockById(Math.abs(realID));
        Block worldBlock = world.getBlock(x, y, z);
        if (realBlock != null && worldBlock != null && (realID >= 0 || worldBlock == realBlock)) {
            if (realBlock != worldBlock && !GenHelper.materialsMatch(realBlock, worldBlock)) {
                LogHelper.info(
                    "Incorrect location for structure removal, aborting. Last block id checked: world " + worldBlock
                        + ", real "
                        + realID
                        + ", fake "
                        + fakeID);
                return false;
            } else {
                world.setBlockToAir(x, y, z);
                List<Entity> list = world.getEntitiesWithinAABB(
                    Entity.class,
                    GenHelper.getHangingEntityAxisAligned(x, y, z, Direction.directionToFacing[rotations])
                        .expand(1.0, 1.0, 1.0));

                for (Entity entity : list) {
                    if (!(entity instanceof EntityPlayer)) {
                        entity.setDead();
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    @Shadow
    public abstract int getRealBlockID(int var1, int var2);

    @Shadow
    public final boolean isOppositeAxis() {
        return this.getOriginalFacing() % 2 != this.structureFacing % 2;
    }

    @Shadow
    public abstract void onCustomBlockAdded(World var1, int var2, int var3, int var4, int var5, int var6, int var7);

    @Shadow
    public final int getOriginalFacing() {
        return (this.structureFacing + (4 - this.manualRotations)) % 4;
    }
}
