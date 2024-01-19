package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableSetMultimap;

import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import fr.iamacat.optimizationsandtweaks.eventshandler.TidyChunkBackportEventHandler;

@Mixin(value = World.class, priority = 999)
public class MixinWorld {

    @Shadow
    protected int updateLCG = (new Random()).nextInt();
    @Shadow
    public Random rand = new Random();

    @Shadow
    private int ambientTickCountdown;

    @Shadow
    public int skylightSubtracted;
    @Shadow
    int[] lightUpdateBlockList;
    @Unique
    private World world;
    @Shadow
    public boolean isRemote;

    @Shadow
    public static double MAX_ENTITY_RADIUS = 2.0D;

    @Shadow
    private ArrayList collidingBoundingBoxes;

    @Shadow
    public final WorldProvider provider;

    @Shadow
    protected IChunkProvider chunkProvider;

    @Shadow
    protected List worldAccesses = new ArrayList();
    @Shadow
    public List loadedEntityList = new ArrayList();
    @Shadow
    protected List unloadedEntityList = new ArrayList();
    /** A list of the loaded tile entities in the world */
    @Shadow
    public List loadedTileEntityList = new ArrayList();
    @Shadow
    private List addedTileEntityList = new ArrayList();
    @Shadow
    private List field_147483_b = new ArrayList();
    /** Array list of players in the world. */
    @Shadow
    public List playerEntities = new ArrayList();

    /** a list of all the lightning entities */
    @Shadow
    public List weatherEffects = new ArrayList();

    @Shadow
    private boolean field_147481_N;

    @Shadow
    public boolean captureBlockSnapshots = false;
    @Shadow
    public final Profiler theProfiler;

    public MixinWorld(World world, WorldProvider provider, Profiler theProfiler) {
        this.world = world;
        this.provider = provider;
        this.theProfiler = theProfiler;
    }

    @Shadow
    public void onEntityRemoved(Entity p_72847_1_) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            ((IWorldAccess) this.worldAccesses.get(i)).onEntityDestroy(p_72847_1_);
        }
    }

    @Shadow
    public void removeEntity(Entity p_72900_1_) {
        if (p_72900_1_.riddenByEntity != null) {
            p_72900_1_.riddenByEntity.mountEntity((Entity) null);
        }

        if (p_72900_1_.ridingEntity != null) {
            p_72900_1_.mountEntity((Entity) null);
        }

        p_72900_1_.setDead();

        if (p_72900_1_ instanceof EntityPlayer) {
            this.playerEntities.remove(p_72900_1_);
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(p_72900_1_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Block getBlock(int x, int y, int z) {
        if (!optimizationsAndTweaks$isValidCoordinates(x, y, z)) {
            return Blocks.air;
        }

        try {
            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            if (chunk == null) {
                System.out.println(
                    "(Optimizationsandtweaks logging)Chunk is null at coordinates - x: " + (x >> 4)
                        + ", z: "
                        + (z >> 4));
                return Blocks.air;
            }

            Block block = chunk.getBlock(x & 15, y, z & 15);
            if (block == null) {
                optimizationsAndTweaks$logNullBlockDetails(x, y, z, chunk);
                return Blocks.air;
            }

            return block;
        } catch (Throwable throwable) {
            optimizationsAndTweaks$logExceptionDetails(x, y, z, throwable);
        }

        return Blocks.air;
    }

    @Unique
    private boolean optimizationsAndTweaks$isValidCoordinates(int x, int y, int z) {
        return x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000 && y >= 0 && y < 256;
    }

    @Unique
    private void optimizationsAndTweaks$logNullBlockDetails(int x, int y, int z, Chunk chunk) {
        Block blockAtCoordinates = chunk.getBlock(x & 15, y, z & 15);
        System.out.println(
            "(Optimizationsandtweaks logging)Block is null at coordinates - x: " + x + ", y: " + y + ", z: " + z);
        System.out.println(
            "(Optimizationsandtweaks logging)Block type: "
                + (blockAtCoordinates != null ? blockAtCoordinates.getUnlocalizedName() : "Unknown"));
        System.out.println("(Optimizationsandtweaks logging)Chunk: " + chunk);
    }

    @Unique
    private void optimizationsAndTweaks$logExceptionDetails(int x, int y, int z, Throwable throwable) {
        System.out.println(
            "(Optimizationsandtweaks logging)Exception getting block type in world at coordinates - x: " + x
                + ", y: "
                + y
                + ", z: "
                + z);
        System.out.println("(Optimizationsandtweaks logging)Exception details: " + throwable);
    }

    @Shadow
    public boolean blockExists(int p_72899_1_, int p_72899_2_, int p_72899_3_) {
        return p_72899_2_ >= 0 && p_72899_2_ < 256 ? this.chunkExists(p_72899_1_ >> 4, p_72899_3_ >> 4) : false;
    }

    @Shadow
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_) {
        return this.chunkProvider.chunkExists(p_72916_1_, p_72916_2_);
    }

    @Shadow
    public void updateAllPlayersSleepingFlag() {}

    @Unique
    private void optimizationsAndTweaks$handleBlockUpdates(int x, int y, int z, Chunk chunk, Block block1,
        Block blockIn, int flags) {
        this.markAndNotifyBlock(x, y, z, chunk, block1, blockIn, flags);
    }

    @Unique
    private void optimizationsAndTweaks$handleLightingUpdates(int x, int y, int z) {
        this.theProfiler.startSection("checkLight");
        this.func_147451_t(x, y, z);
        this.theProfiler.endSection();
    }

    @Shadow
    public void markAndNotifyBlock(int x, int y, int z, Chunk chunk, Block oldBlock, Block newBlock, int flag) {
        if ((flag & 2) != 0 && (chunk == null || chunk.func_150802_k())) {
            this.markBlockForUpdate(x, y, z);
        }

        if (!this.isRemote && (flag & 1) != 0) {
            this.notifyBlockChange(x, y, z, oldBlock);

            if (newBlock.hasComparatorInputOverride()) {
                this.func_147453_f(x, y, z, newBlock);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void markBlockForUpdate(int p_147471_1_, int p_147471_2_, int p_147471_3_) {
        for (Object worldAccess : this.worldAccesses) {
            ((IWorldAccess) worldAccess).markBlockForUpdate(p_147471_1_, p_147471_2_, p_147471_3_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_147453_f(int x, int yPos, int z, Block blockIn) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int i1 = x + dir.offsetX;
            int y = yPos + dir.offsetY;
            int j1 = z + dir.offsetZ;
            Block block1 = this.getBlock(i1, y, j1);

            block1.onNeighborChange((IBlockAccess) this, i1, y, j1, x, yPos, z);
            if (block1.isNormalCube((IBlockAccess) this, i1, y, j1)) {
                i1 += dir.offsetX;
                y += dir.offsetY;
                j1 += dir.offsetZ;
                Block block2 = this.getBlock(i1, y, j1);

                if (block2.getWeakChanges((IBlockAccess) this, i1, y, j1)) {
                    block2.onNeighborChange((IBlockAccess) this, i1, y, j1, x, yPos, z);
                }
            }
        }
    }

    @Shadow
    public void updateEntity(Entity p_72870_1_) {
        this.updateEntityWithOptionalForce(p_72870_1_, true);
    }

    @Shadow
    public ArrayList<net.minecraftforge.common.util.BlockSnapshot> capturedBlockSnapshots = new ArrayList<net.minecraftforge.common.util.BlockSnapshot>();

    @Shadow
    public ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunks() {
        return ForgeChunkManager.getPersistentChunksFor(world);
    }

    @Shadow
    public void updateEntityWithOptionalForce(Entity p_72866_1_, boolean p_72866_2_) {
        int i = MathHelper.floor_double(p_72866_1_.posX);
        int j = MathHelper.floor_double(p_72866_1_.posZ);
        boolean isForced = getPersistentChunks().containsKey(new ChunkCoordIntPair(i >> 4, j >> 4));
        byte b0 = isForced ? (byte) 0 : 32;
        boolean canUpdate = !p_72866_2_ || this.checkChunksExist(i - b0, 0, j - b0, i + b0, 0, j + b0);

        if (!canUpdate) {
            EntityEvent.CanUpdate event = new EntityEvent.CanUpdate(p_72866_1_);
            MinecraftForge.EVENT_BUS.post(event);
            canUpdate = event.canUpdate;
        }

        if (canUpdate) {
            p_72866_1_.lastTickPosX = p_72866_1_.posX;
            p_72866_1_.lastTickPosY = p_72866_1_.posY;
            p_72866_1_.lastTickPosZ = p_72866_1_.posZ;
            p_72866_1_.prevRotationYaw = p_72866_1_.rotationYaw;
            p_72866_1_.prevRotationPitch = p_72866_1_.rotationPitch;

            if (p_72866_2_ && p_72866_1_.addedToChunk) {
                ++p_72866_1_.ticksExisted;

                if (p_72866_1_.ridingEntity != null) {
                    p_72866_1_.updateRidden();
                } else {
                    p_72866_1_.onUpdate();
                }
            }

            this.theProfiler.startSection("chunkCheck");

            if (Double.isNaN(p_72866_1_.posX) || Double.isInfinite(p_72866_1_.posX)) {
                p_72866_1_.posX = p_72866_1_.lastTickPosX;
            }

            if (Double.isNaN(p_72866_1_.posY) || Double.isInfinite(p_72866_1_.posY)) {
                p_72866_1_.posY = p_72866_1_.lastTickPosY;
            }

            if (Double.isNaN(p_72866_1_.posZ) || Double.isInfinite(p_72866_1_.posZ)) {
                p_72866_1_.posZ = p_72866_1_.lastTickPosZ;
            }

            if (Double.isNaN(p_72866_1_.rotationPitch) || Double.isInfinite(p_72866_1_.rotationPitch)) {
                p_72866_1_.rotationPitch = p_72866_1_.prevRotationPitch;
            }

            if (Double.isNaN(p_72866_1_.rotationYaw) || Double.isInfinite(p_72866_1_.rotationYaw)) {
                p_72866_1_.rotationYaw = p_72866_1_.prevRotationYaw;
            }

            int k = MathHelper.floor_double(p_72866_1_.posX / 16.0D);
            int l = MathHelper.floor_double(p_72866_1_.posY / 16.0D);
            int i1 = MathHelper.floor_double(p_72866_1_.posZ / 16.0D);

            if (!p_72866_1_.addedToChunk || p_72866_1_.chunkCoordX != k
                || p_72866_1_.chunkCoordY != l
                || p_72866_1_.chunkCoordZ != i1) {
                if (p_72866_1_.addedToChunk && this.chunkExists(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ)) {
                    this.getChunkFromChunkCoords(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ)
                        .removeEntityAtIndex(p_72866_1_, p_72866_1_.chunkCoordY);
                }

                if (this.chunkExists(k, i1)) {
                    p_72866_1_.addedToChunk = true;
                    this.getChunkFromChunkCoords(k, i1)
                        .addEntity(p_72866_1_);
                } else {
                    p_72866_1_.addedToChunk = false;
                }
            }

            this.theProfiler.endSection();

            if (p_72866_2_ && p_72866_1_.addedToChunk && p_72866_1_.riddenByEntity != null) {
                if (!p_72866_1_.riddenByEntity.isDead && p_72866_1_.riddenByEntity.ridingEntity == p_72866_1_) {
                    this.updateEntity(p_72866_1_.riddenByEntity);
                } else {
                    p_72866_1_.riddenByEntity.ridingEntity = null;
                    p_72866_1_.riddenByEntity = null;
                }
            }
        }
    }

    @Shadow
    public boolean checkChunksExist(int p_72904_1_, int p_72904_2_, int p_72904_3_, int p_72904_4_, int p_72904_5_,
        int p_72904_6_) {
        if (p_72904_5_ >= 0 && p_72904_2_ < 256) {
            p_72904_1_ >>= 4;
            p_72904_3_ >>= 4;
            p_72904_4_ >>= 4;
            p_72904_6_ >>= 4;

            for (int k1 = p_72904_1_; k1 <= p_72904_4_; ++k1) {
                for (int l1 = p_72904_3_; l1 <= p_72904_6_; ++l1) {
                    if (!this.chunkExists(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Shadow
    public void notifyBlockChange(int p_147444_1_, int p_147444_2_, int p_147444_3_, Block p_147444_4_) {
        this.notifyBlocksOfNeighborChange(p_147444_1_, p_147444_2_, p_147444_3_, p_147444_4_);
    }

    @Shadow
    public void notifyBlocksOfNeighborChange(int p_147459_1_, int p_147459_2_, int p_147459_3_, Block p_147459_4_) {
        this.notifyBlockOfNeighborChange(p_147459_1_ - 1, p_147459_2_, p_147459_3_, p_147459_4_);
        this.notifyBlockOfNeighborChange(p_147459_1_ + 1, p_147459_2_, p_147459_3_, p_147459_4_);
        this.notifyBlockOfNeighborChange(p_147459_1_, p_147459_2_ - 1, p_147459_3_, p_147459_4_);
        this.notifyBlockOfNeighborChange(p_147459_1_, p_147459_2_ + 1, p_147459_3_, p_147459_4_);
        this.notifyBlockOfNeighborChange(p_147459_1_, p_147459_2_, p_147459_3_ - 1, p_147459_4_);
        this.notifyBlockOfNeighborChange(p_147459_1_, p_147459_2_, p_147459_3_ + 1, p_147459_4_);
    }

    @Shadow
    public void notifyBlockOfNeighborChange(int p_147460_1_, int p_147460_2_, int p_147460_3_,
        final Block p_147460_4_) {
        if (!this.isRemote) {
            Block block = this.getBlock(p_147460_1_, p_147460_2_, p_147460_3_);

            try {
                block.onNeighborBlockChange(world, p_147460_1_, p_147460_2_, p_147460_3_, p_147460_4_);
            } catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport
                    .makeCrashReport(throwable1, "Exception while updating neighbours");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                int l;

                try {
                    l = this.getBlockMetadata(p_147460_1_, p_147460_2_, p_147460_3_);
                } catch (Throwable throwable) {
                    l = -1;
                }

                crashreportcategory.addCrashSectionCallable("Source block type", () -> {
                    try {
                        return String.format(
                            "ID #%d (%s // %s)",
                            Block.getIdFromBlock(p_147460_4_),
                            p_147460_4_.getUnlocalizedName(),
                            p_147460_4_.getClass()
                                .getCanonicalName());
                    } catch (Throwable throwable2) {
                        return "ID #" + Block.getIdFromBlock(p_147460_4_);
                    }
                });
                CrashReportCategory.func_147153_a(crashreportcategory, p_147460_1_, p_147460_2_, p_147460_3_, block, l);
                throw new ReportedException(crashreport);
            }
        }
    }

    @Shadow
    public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
        if (p_72805_1_ >= -30000000 && p_72805_3_ >= -30000000 && p_72805_1_ < 30000000 && p_72805_3_ < 30000000) {
            if (p_72805_2_ < 0) {
                return 0;
            } else if (p_72805_2_ >= 256) {
                return 0;
            } else {
                Chunk chunk = this.getChunkFromChunkCoords(p_72805_1_ >> 4, p_72805_3_ >> 4);
                p_72805_1_ &= 15;
                p_72805_3_ &= 15;
                return chunk.getBlockMetadata(p_72805_1_, p_72805_2_, p_72805_3_);
            }
        } else {
            return 0;
        }
    }

    @Shadow
    public boolean func_147451_t(int x, int y, int z) {
        boolean flag = false;

        if (!this.provider.hasNoSky) {
            flag |= this.updateLightByType(EnumSkyBlock.Sky, x, y, z);
        }

        flag |= this.updateLightByType(EnumSkyBlock.Block, x, y, z);
        return flag;
    }

    @Shadow
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {
        if (!this.doChunksNearChunkExist(p_147463_2_, p_147463_3_, p_147463_4_, 17)) {
            return false;
        } else {
            int l = 0;
            int i1 = 0;
            this.theProfiler.startSection("getBrightness");
            int j1 = this.getSavedLightValue(p_147463_1_, p_147463_2_, p_147463_3_, p_147463_4_);
            int k1 = this.computeLightValue(p_147463_2_, p_147463_3_, p_147463_4_, p_147463_1_);
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            int l3;

            if (k1 > j1) {
                this.lightUpdateBlockList[i1++] = 133152;
            } else if (k1 < j1) {
                this.lightUpdateBlockList[i1++] = 133152 | j1 << 18;

                while (l < i1) {
                    l1 = this.lightUpdateBlockList[l++];
                    i2 = (l1 & 63) - 32 + p_147463_2_;
                    j2 = (l1 >> 6 & 63) - 32 + p_147463_3_;
                    k2 = (l1 >> 12 & 63) - 32 + p_147463_4_;
                    l2 = l1 >> 18 & 15;
                    i3 = this.getSavedLightValue(p_147463_1_, i2, j2, k2);

                    if (i3 == l2) {
                        this.setLightValue(p_147463_1_, i2, j2, k2, 0);

                        if (l2 > 0) {
                            j3 = MathHelper.abs_int(i2 - p_147463_2_);
                            k3 = MathHelper.abs_int(j2 - p_147463_3_);
                            l3 = MathHelper.abs_int(k2 - p_147463_4_);

                            if (j3 + k3 + l3 < 17) {
                                for (int i4 = 0; i4 < 6; ++i4) {
                                    int j4 = i2 + Facing.offsetsXForSide[i4];
                                    int k4 = j2 + Facing.offsetsYForSide[i4];
                                    int l4 = k2 + Facing.offsetsZForSide[i4];
                                    int i5 = Math.max(
                                        1,
                                        this.getBlock(j4, k4, l4)
                                            .getLightOpacity(world, j4, k4, l4));
                                    i3 = this.getSavedLightValue(p_147463_1_, j4, k4, l4);

                                    if (i3 == l2 - i5 && i1 < this.lightUpdateBlockList.length) {
                                        this.lightUpdateBlockList[i1++] = j4 - p_147463_2_ + 32
                                            | k4 - p_147463_3_ + 32 << 6
                                            | l4 - p_147463_4_ + 32 << 12
                                            | l2 - i5 << 18;
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            this.theProfiler.endSection();
            this.theProfiler.startSection("checkedPosition < toCheckCount");

            while (l < i1) {
                l1 = this.lightUpdateBlockList[l++];
                i2 = (l1 & 63) - 32 + p_147463_2_;
                j2 = (l1 >> 6 & 63) - 32 + p_147463_3_;
                k2 = (l1 >> 12 & 63) - 32 + p_147463_4_;
                l2 = this.getSavedLightValue(p_147463_1_, i2, j2, k2);
                i3 = this.computeLightValue(i2, j2, k2, p_147463_1_);

                if (i3 != l2) {
                    this.setLightValue(p_147463_1_, i2, j2, k2, i3);

                    if (i3 > l2) {
                        j3 = Math.abs(i2 - p_147463_2_);
                        k3 = Math.abs(j2 - p_147463_3_);
                        l3 = Math.abs(k2 - p_147463_4_);
                        boolean flag = i1 < this.lightUpdateBlockList.length - 6;

                        if (j3 + k3 + l3 < 17 && flag) {
                            if (this.getSavedLightValue(p_147463_1_, i2 - 1, j2, k2) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 - 1
                                    - p_147463_2_
                                    + 32
                                    + (j2 - p_147463_3_ + 32 << 6)
                                    + (k2 - p_147463_4_ + 32 << 12);
                            }

                            if (this.getSavedLightValue(p_147463_1_, i2 + 1, j2, k2) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 + 1
                                    - p_147463_2_
                                    + 32
                                    + (j2 - p_147463_3_ + 32 << 6)
                                    + (k2 - p_147463_4_ + 32 << 12);
                            }

                            if (this.getSavedLightValue(p_147463_1_, i2, j2 - 1, k2) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 - p_147463_2_
                                    + 32
                                    + (j2 - 1 - p_147463_3_ + 32 << 6)
                                    + (k2 - p_147463_4_ + 32 << 12);
                            }

                            if (this.getSavedLightValue(p_147463_1_, i2, j2 + 1, k2) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 - p_147463_2_
                                    + 32
                                    + (j2 + 1 - p_147463_3_ + 32 << 6)
                                    + (k2 - p_147463_4_ + 32 << 12);
                            }

                            if (this.getSavedLightValue(p_147463_1_, i2, j2, k2 - 1) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 - p_147463_2_
                                    + 32
                                    + (j2 - p_147463_3_ + 32 << 6)
                                    + (k2 - 1 - p_147463_4_ + 32 << 12);
                            }

                            if (this.getSavedLightValue(p_147463_1_, i2, j2, k2 + 1) < i3) {
                                this.lightUpdateBlockList[i1++] = i2 - p_147463_2_
                                    + 32
                                    + (j2 - p_147463_3_ + 32 << 6)
                                    + (k2 + 1 - p_147463_4_ + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.theProfiler.endSection();
            return true;
        }
    }

    @Shadow
    public int getSavedLightValue(EnumSkyBlock p_72972_1_, int p_72972_2_, int p_72972_3_, int p_72972_4_) {
        if (p_72972_3_ < 0) {
            p_72972_3_ = 0;
        }

        if (p_72972_3_ >= 256) {
            p_72972_3_ = 255;
        }

        if (p_72972_2_ >= -30000000 && p_72972_4_ >= -30000000 && p_72972_2_ < 30000000 && p_72972_4_ < 30000000) {
            int l = p_72972_2_ >> 4;
            int i1 = p_72972_4_ >> 4;

            if (!this.chunkExists(l, i1)) {
                return p_72972_1_.defaultLightValue;
            } else {
                Chunk chunk = this.getChunkFromChunkCoords(l, i1);
                return chunk.getSavedLightValue(p_72972_1_, p_72972_2_ & 15, p_72972_3_, p_72972_4_ & 15);
            }
        } else {
            return p_72972_1_.defaultLightValue;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setLightValue(EnumSkyBlock p_72915_1_, int p_72915_2_, int p_72915_3_, int p_72915_4_, int p_72915_5_) {
        if (p_72915_2_ >= -30000000 && p_72915_4_ >= -30000000
            && p_72915_2_ < 30000000
            && p_72915_4_ < 30000000
            && (p_72915_3_ >= 0 && (p_72915_3_ < 256 && (this.chunkExists(p_72915_2_ >> 4, p_72915_4_ >> 4))))) {
            Chunk chunk = this.getChunkFromChunkCoords(p_72915_2_ >> 4, p_72915_4_ >> 4);
            chunk.setLightValue(p_72915_1_, p_72915_2_ & 15, p_72915_3_, p_72915_4_ & 15, p_72915_5_);

            for (Object worldAccess : this.worldAccesses) {
                ((IWorldAccess) worldAccess).markBlockForRenderUpdate(p_72915_2_, p_72915_3_, p_72915_4_);
            }
        }
    }

    @Shadow
    private int computeLightValue(int x, int y, int z, EnumSkyBlock p_98179_4_) {
        if (p_98179_4_ == EnumSkyBlock.Sky && this.canBlockSeeTheSky(x, y, z)) {
            return 15;
        } else {
            Block block = this.getBlock(x, y, z);
            int blockLight = block.getLightValue(world, x, y, z);
            int l = p_98179_4_ == EnumSkyBlock.Sky ? 0 : blockLight;
            int i1 = block.getLightOpacity(world, x, y, z);

            if (i1 >= 15 && blockLight > 0) {
                i1 = 1;
            }

            if (i1 < 1) {
                i1 = 1;
            }

            if (i1 >= 15) {
                return 0;
            } else if (l >= 14) {
                return l;
            } else {
                for (int j1 = 0; j1 < 6; ++j1) {
                    int k1 = x + Facing.offsetsXForSide[j1];
                    int l1 = y + Facing.offsetsYForSide[j1];
                    int i2 = z + Facing.offsetsZForSide[j1];
                    int j2 = this.getSavedLightValue(p_98179_4_, k1, l1, i2) - i1;

                    if (j2 > l) {
                        l = j2;
                    }

                    if (l >= 14) {
                        return l;
                    }
                }

                return l;
            }
        }
    }

    @Shadow
    public boolean canBlockSeeTheSky(int p_72937_1_, int p_72937_2_, int p_72937_3_) {
        return this.getChunkFromChunkCoords(p_72937_1_ >> 4, p_72937_3_ >> 4)
            .canBlockSeeTheSky(p_72937_1_ & 15, p_72937_2_, p_72937_3_ & 15);
    }

    @Shadow
    public boolean doChunksNearChunkExist(int p_72873_1_, int p_72873_2_, int p_72873_3_, int p_72873_4_) {
        return this.checkChunksExist(
            p_72873_1_ - p_72873_4_,
            p_72873_2_ - p_72873_4_,
            p_72873_3_ - p_72873_4_,
            p_72873_1_ + p_72873_4_,
            p_72873_2_ + p_72873_4_,
            p_72873_3_ + p_72873_4_);
    }

    /**
     * Returns back a chunk looked up by chunk coordinates Args: x, y
     */
    @Shadow
    public Chunk getChunkFromChunkCoords(int p_72964_1_, int p_72964_2_) {
        return this.chunkProvider.provideChunk(p_72964_1_, p_72964_2_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean doesBlockHaveSolidTopSurface(IBlockAccess worldIn, int x, int y, int z) {
        Block block = worldIn.getBlock(x, y, z);
        Material material = block.getMaterial();

        // Check if the block's material has a solid top surface
        return material.blocksMovement() && material.isSolid();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isMaterialInBB(AxisAlignedBB bb, Material material) {
        int minX = MathHelper.floor_double(bb.minX);
        int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
        int minY = MathHelper.floor_double(bb.minY);
        int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
        int minZ = MathHelper.floor_double(bb.minZ);
        int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = this.getBlock(x, y, z);
                    if (block.getMaterial() == material) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
        double closestDistanceSq = -1.0D;
        EntityPlayer closestPlayer = null;

        for (Object entity : this.playerEntities) {
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }

            EntityPlayer entityPlayer = (EntityPlayer) entity;
            double playerDistanceSq = entityPlayer.getDistanceSq(x, y, z);

            if ((distance < 0.0D || playerDistanceSq < distance * distance)
                && (closestDistanceSq == -1.0D || playerDistanceSq < closestDistanceSq)) {
                closestDistanceSq = playerDistanceSq;
                closestPlayer = entityPlayer;
            }
        }

        return closestPlayer;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getTopSolidOrLiquidBlock(int p_72825_1_, int p_72825_2_) {
        Chunk chunk = this.getChunkFromBlockCoords(p_72825_1_, p_72825_2_);
        int chunkTopSegment = chunk.getTopFilledSegment() + 15;
        p_72825_1_ &= 15;
        p_72825_2_ &= 15;

        int x = p_72825_1_;
        int z = p_72825_2_;

        for (int k = chunkTopSegment; k > 0; --k) {
            Block block = chunk.getBlock(p_72825_1_, k, p_72825_2_);
            Material blockMaterial = block.getMaterial();

            if (blockMaterial.blocksMovement() && blockMaterial != Material.leaves
                && !block.isFoliage(world, x, k, z)) {
                return k + 1;
            }
        }

        return -1;
    }

    @Shadow
    public Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_) {
        return this.getChunkFromChunkCoords(p_72938_1_ >> 4, p_72938_2_ >> 4);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getBlockLightValue_do(int p_72849_1_, int p_72849_2_, int p_72849_3_, boolean p_72849_4_) {
        if (optimizationsAndTweaks$isWithinBounds(p_72849_1_, p_72849_2_, p_72849_3_)) {
            if (p_72849_4_ && this.getBlock(p_72849_1_, p_72849_2_, p_72849_3_)
                .getUseNeighborBrightness()) {
                return optimizationsAndTweaks$getMaxNeighborLightValue(p_72849_1_, p_72849_2_, p_72849_3_);
            } else if (p_72849_2_ < 0) {
                return 0;
            } else {
                return optimizationsAndTweaks$getChunkBlockLightValue(p_72849_1_, p_72849_2_, p_72849_3_);
            }
        } else {
            return 15;
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$isWithinBounds(int x, int y, int z) {
        return x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000;
    }

    @Unique
    private int optimizationsAndTweaks$getMaxNeighborLightValue(int x, int y, int z) {
        int[] neighborValues = new int[5];

        neighborValues[0] = getBlockLightValue_do(x, y + 1, z, false);
        neighborValues[1] = getBlockLightValue_do(x + 1, y, z, false);
        neighborValues[2] = getBlockLightValue_do(x - 1, y, z, false);
        neighborValues[3] = getBlockLightValue_do(x, y, z + 1, false);
        neighborValues[4] = getBlockLightValue_do(x, y, z - 1, false);

        int max = neighborValues[0];
        for (int i = 1; i < neighborValues.length; i++) {
            max = Math.max(max, neighborValues[i]);
        }

        return max;
    }

    @Unique
    private int optimizationsAndTweaks$getChunkBlockLightValue(int x, int y, int z) {
        if (y >= 256) y = 255;

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        x &= 15;
        z &= 15;

        return chunk.getBlockLightValue(x, y, z, this.skylightSubtracted);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void spawnParticle(String particleName, double x, double y, double z, double velocityX, double velocityY,
        double velocityZ) {
        for (Object worldAccess : this.worldAccesses) {
            ((IWorldAccess) worldAccess).spawnParticle(particleName, x, y, z, velocityX, velocityY, velocityZ);
        }
    }
    /**
     * @author
     * @reason
     */

    @Overwrite
    public int countEntities(Class<?> entityClass) {
        return (int) this.loadedEntityList.stream()
            .filter(entity -> entityClass.isInstance(entity) && (!(entity instanceof EntityLiving) || !((EntityLiving) entity).isNoDespawnRequired()))
            .count();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE"))
    private void onTickInject(CallbackInfo info) {
        if (OptimizationsandTweaksConfig.enableTidyChunkBackport) {
            TidyChunkBackportEventHandler.injectInWorldTick((World) (Object) this);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_147467_a(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_) {
        this.theProfiler.endStartSection("moodSound");

        if (this.ambientTickCountdown == 0 && !this.isRemote) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int k = this.updateLCG >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 255;

            if (p_147467_3_ != null && this.world != null) {
                Block block = p_147467_3_.getBlock(l, j1, i1);
                l += p_147467_1_;
                i1 += p_147467_2_;

                if (block != null && (block.getMaterial() == Material.air
                    && this.getFullBlockLightValue(l, j1, i1) <= this.rand.nextInt(8)
                    && this.getSavedLightValue(EnumSkyBlock.Sky, l, j1, i1) <= 0
                    && this.world.provider != null)) {
                    EntityPlayer entityplayer = this.getClosestPlayer(l + 0.5D, j1 + 0.5D, i1 + 0.5D, 8.0D);

                    if (entityplayer != null && entityplayer.worldObj != null
                        && entityplayer.getDistanceSq(l + 0.5D, j1 + 0.5D, i1 + 0.5D) > 4.0D) {
                        this.playSoundEffect(
                            l + 0.5D,
                            j1 + 0.5D,
                            i1 + 0.5D,
                            "ambient.cave.cave",
                            0.7F,
                            0.8F + this.rand.nextFloat() * 0.2F);
                        this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
                    }

                }
            }
        }
        this.theProfiler.endStartSection("checkLight");
        if (p_147467_3_ != null) {
            p_147467_3_.enqueueRelightChecks();
        }
    }

    @Shadow
    public int getFullBlockLightValue(int p_72883_1_, int p_72883_2_, int p_72883_3_) {
        if (p_72883_2_ < 0) {
            return 0;
        } else {
            if (p_72883_2_ >= 256) {
                p_72883_2_ = 255;
            }

            return this.getChunkFromChunkCoords(p_72883_1_ >> 4, p_72883_3_ >> 4)
                .getBlockLightValue(p_72883_1_ & 15, p_72883_2_, p_72883_3_ & 15, 0);
        }
    }

    @Overwrite
    public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
        for (Object worldAccess : this.worldAccesses) {
            ((IWorldAccess) worldAccess).playSound(soundName, x, y, z, volume, pitch);
        }
    }
}
