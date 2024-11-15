package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
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

import cpw.mods.fml.common.FMLLog;
import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import fr.iamacat.optimizationsandtweaks.eventshandler.TidyChunkBackportEventHandler;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.ArrayListThreadSafe;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.HashSetThreadSafe;

@Mixin(value = World.class, priority = 999)
public abstract class MixinWorld {

    @Shadow
    public boolean restoringBlockSnapshots = false;
    @Shadow
    protected final Set activeChunkSet = new HashSetThreadSafe();

    @Unique
    private final ThreadLocalRandom optimizationsAndTweaks$rand = ThreadLocalRandom.current();
    @Shadow
    protected int updateLCG = (optimizationsAndTweaks$rand).nextInt();

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
    protected List worldAccesses = new ArrayListThreadSafe();
    @Shadow
    public List loadedEntityList = new ArrayListThreadSafe();
    @Shadow
    protected List unloadedEntityList = new ArrayListThreadSafe();
    /** A list of the loaded tile entities in the world */
    @Shadow
    public List loadedTileEntityList = new ArrayListThreadSafe();
    @Shadow
    private List addedTileEntityList = new ArrayListThreadSafe();
    @Shadow
    private List field_147483_b = new ArrayListThreadSafe();
    /** Array list of players in the world. */
    @Shadow
    public List playerEntities = new ArrayListThreadSafe();

    /** a list of all the lightning entities */
    @Shadow
    public List<Entity> weatherEffects = new ArrayListThreadSafe();

    @Shadow
    private boolean field_147481_N;

    @Shadow
    public boolean captureBlockSnapshots = false;
    @Shadow
    public final Profiler theProfiler;

    @Inject(method = "tick", at = @At(value = "INVOKE"))
    private void onTickInject(CallbackInfo info) {
        if (OptimizationsandTweaksConfig.enableTidyChunkBackport) {
            TidyChunkBackportEventHandler.injectInWorldTick((World) (Object) this);
        }
    }

    public MixinWorld(World world, WorldProvider provider, Profiler theProfiler) {
        this.world = world;
        this.provider = provider;
        this.theProfiler = theProfiler;
    }

    @Overwrite
    public void onEntityRemoved(Entity p_72847_1_) {
        for (Object worldAccess : this.worldAccesses) {
            ((IWorldAccess) worldAccess).onEntityDestroy(p_72847_1_);
        }
    }

    @Shadow
    public void removeEntity(Entity p_72900_1_) {
        if (p_72900_1_.riddenByEntity != null) {
            p_72900_1_.riddenByEntity.mountEntity(null);
        }

        if (p_72900_1_.ridingEntity != null) {
            p_72900_1_.mountEntity(null);
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
    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
        if (p_147439_1_ >= -30000000 && p_147439_3_ >= -30000000
            && p_147439_1_ < 30000000
            && p_147439_3_ < 30000000
            && p_147439_2_ >= 0
            && p_147439_2_ < 256
            && this.blockExists(p_147439_1_, p_147439_2_, p_147439_3_)) {
            Chunk chunk = this.getChunkFromChunkCoords(p_147439_1_ >> 4, p_147439_3_ >> 4);
            if (chunk != null) {
                return chunk.getBlock(p_147439_1_ & 15, p_147439_2_, p_147439_3_ & 15);
            }
        }
        return Blocks.air;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean blockExists(int p_72899_1_, int p_72899_2_, int p_72899_3_) {
        return p_72899_2_ >= 0 && p_72899_2_ < 256 && this.chunkExists(p_72899_1_ >> 4, p_72899_3_ >> 4);
    }

    @Shadow
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_) {
        return this.chunkProvider.chunkExists(p_72916_1_, p_72916_2_);
    }

    @Shadow
    public void updateAllPlayersSleepingFlag() {}

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
                int maxLightValue = 0;

                for (EnumFacing facing : EnumFacing.values()) {
                    // Exclude EnumFacing.DOWN to prevent negative y values
                    if (facing != EnumFacing.DOWN) {
                        int lightValue = optimizationsAndTweaks$getChunkBlockLightValue(
                            p_72849_1_ + facing.getFrontOffsetX(),
                            p_72849_2_ + facing.getFrontOffsetY(),
                            p_72849_3_ + facing.getFrontOffsetZ());
                        maxLightValue = Math.max(maxLightValue, lightValue);
                    }
                }

                return maxLightValue;
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
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { x, y + 1, z });
        stack.push(new int[] { x + 1, y, z });
        stack.push(new int[] { x - 1, y, z });
        stack.push(new int[] { x, y, z + 1 });
        stack.push(new int[] { x, y, z - 1 });
        int max = 0;
        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            max = Math.max(max, getBlockLightValue_do(pos[0], pos[1], pos[2], false));
        }

        return max;
    }

    @Unique
    private int optimizationsAndTweaks$getChunkBlockLightValue(int x, int y, int z) {
        if (y >= 256 || y < 0) {
            return 0;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);

        if (chunk != null) {
            x &= 15;
            z &= 15;
            return chunk.getBlockLightValue(x, y, z, this.skylightSubtracted);
        }

        return 0;
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

    /**
     * @author
     * @reason
     */
    // playMoodSoundAndCheckLight method
    @Overwrite
    public void func_147467_a(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_) {
        optimizationsAndTweaks$playMoodSound(p_147467_1_, p_147467_2_, p_147467_3_);
        optimizationsAndTweaks$checkLight(p_147467_3_);
    }

    @Unique
    private void optimizationsAndTweaks$playMoodSound(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_) {
        this.theProfiler.endStartSection("moodSound");

        if (optimizationsAndTweaks$shouldPlayAmbientSound()) {
            optimizationsAndTweaks$playAmbientCaveSound(p_147467_1_, p_147467_2_, p_147467_3_);
        }
    }

    @Unique
    private void optimizationsAndTweaks$checkLight(Chunk chunk) {
        this.theProfiler.endStartSection("checkLight");
        chunk.enqueueRelightChecks();
    }

    @Unique
    private boolean optimizationsAndTweaks$shouldPlayAmbientSound() {
        return this.ambientTickCountdown == 0 && !this.isRemote;
    }

    @Unique
    private void optimizationsAndTweaks$playAmbientCaveSound(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_) {
        this.updateLCG = this.updateLCG * 3 + 1013904223;
        int k = this.updateLCG >> 2;
        int l = k & 15;
        int i1 = k >> 8 & 15;
        int j1 = k >> 16 & 255;
        Block block = p_147467_3_.getBlock(l, j1, i1);
        l += p_147467_1_;
        i1 += p_147467_2_;

        if (optimizationsAndTweaks$shouldPlayCaveSound(block, l, j1, i1)) {
            optimizationsAndTweaks$playCaveSound(l, j1, i1);
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$shouldPlayCaveSound(Block block, int l, int j1, int i1) {
        return block.getMaterial() == Material.air
            && this.getFullBlockLightValue(l, j1, i1) <= this.optimizationsAndTweaks$rand.nextInt(8)
            && this.getSavedLightValue(EnumSkyBlock.Sky, l, j1, i1) <= 0;
    }

    @Unique
    private void optimizationsAndTweaks$playCaveSound(int l, int j1, int i1) {
        EntityPlayer entityplayer = this.getClosestPlayer(l + 0.5D, j1 + 0.5D, i1 + 0.5D, 8.0D);

        if (entityplayer != null && entityplayer.getDistanceSq(l + 0.5D, j1 + 0.5D, i1 + 0.5D) > 4.0D) {
            this.playSoundEffect(
                l + 0.5D,
                j1 + 0.5D,
                i1 + 0.5D,
                "ambient.cave.cave",
                0.7F,
                0.8F + this.optimizationsAndTweaks$rand.nextFloat() * 0.2F);
            this.ambientTickCountdown = this.optimizationsAndTweaks$rand.nextInt(12000) + 6000;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setActivePlayerChunksAndCheckLight() {
        this.activeChunkSet.clear();
        this.theProfiler.startSection("buildList");
        this.activeChunkSet.addAll(getPersistentChunks().keySet());
        int i;
        EntityPlayer entityplayer;
        int j;
        int k;
        int l;

        for (i = 0; i < this.playerEntities.size(); ++i) {
            entityplayer = (EntityPlayer) this.playerEntities.get(i);
            j = MathHelper.floor_double(entityplayer.posX / 16.0D);
            k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
            l = this.func_152379_p();

            for (int i1 = -l; i1 <= l; ++i1) {
                for (int j1 = -l; j1 <= l; ++j1) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(i1 + j, j1 + k));
                }
            }
        }

        this.theProfiler.endSection();

        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }

        this.theProfiler.startSection("playerCheckLight");

        if (!this.playerEntities.isEmpty()) {
            i = this.optimizationsAndTweaks$rand.nextInt(this.playerEntities.size());
            entityplayer = (EntityPlayer) this.playerEntities.get(i);
            j = MathHelper.floor_double(entityplayer.posX) + this.optimizationsAndTweaks$rand.nextInt(11) - 5;
            k = MathHelper.floor_double(entityplayer.posY) + this.optimizationsAndTweaks$rand.nextInt(11) - 5;
            l = MathHelper.floor_double(entityplayer.posZ) + this.optimizationsAndTweaks$rand.nextInt(11) - 5;
            this.func_147451_t(j, k, l);
        }

        this.theProfiler.endSection();
    }

    @Shadow
    protected abstract int func_152379_p();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onEntityAdded(Entity p_72923_1_) {
        for (Object worldAccess : this.worldAccesses) {
            ((IWorldAccess) worldAccess).onEntityCreate(p_72923_1_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public synchronized void updateEntities() {
        this.theProfiler.startSection("entities");
        this.theProfiler.startSection("global");

        optimizationsAndTweaks$updateWeatherEffects();
        optimizationsAndTweaks$removeUnloadedEntities();
        optimizationsAndTweaks$updateLoadedEntities();
        optimizationsAndTweaks$updateTileEntities();

        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }

    @Unique
    private void optimizationsAndTweaks$updateWeatherEffects() {
        List<Entity> entitiesToRemove = new ArrayListThreadSafe<>();
        for (Entity entity : this.weatherEffects) {
            entity.ticksExisted++;
            try {
                entity.onUpdate();
            } catch (Throwable throwable) {
                optimizationsAndTweaks$handleEntityCrash(entity, throwable);
            }
            if (entity.isDead) {
                entitiesToRemove.add(entity);
            }
        }
        this.weatherEffects.removeAll(entitiesToRemove);
    }

    @Unique
    private void optimizationsAndTweaks$handleEntityCrash(Entity entity, Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking entity");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

        if (entity == null) {
            crashreportcategory.addCrashSection("Entity", "~~NULL~~");
        } else {
            entity.addEntityCrashInfo(crashreportcategory);
        }

        if (ForgeModContainer.removeErroringEntities) {
            FMLLog.getLogger()
                .log(org.apache.logging.log4j.Level.ERROR, crashreport.getCompleteReport());
            if (entity != null) {
                removeEntity(entity);
            }
        } else {
            throw new ReportedException(crashreport);
        }
    }

    @Unique
    private void optimizationsAndTweaks$removeUnloadedEntities() {
        for (Object obj : this.unloadedEntityList) {
            if (obj instanceof Entity) {
                Entity entity = (Entity) obj;
                int chunkX = entity.chunkCoordX;
                int chunkZ = entity.chunkCoordZ;
                if (entity.addedToChunk && this.chunkExists(chunkX, chunkZ)) {
                    this.getChunkFromChunkCoords(chunkX, chunkZ)
                        .removeEntity(entity);
                }
                onEntityRemoved(entity);
            }
        }
        this.unloadedEntityList.clear();
    }

    @Unique
    private void optimizationsAndTweaks$updateLoadedEntities() {
        if (this.loadedEntityList == null) {
            System.err.println("[OptimizationsAndTweaks] loadedEntityList is null");
            return;
        }

        Iterator<Entity> iterator = this.loadedEntityList.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity == null) {
                System.err.println("[OptimizationsAndTweaks] Encountered null entity in loadedEntityList");
                iterator.remove();
                continue;
            }

            if (entity.ridingEntity != null && (entity.ridingEntity.isDead || entity.ridingEntity.riddenByEntity != entity)) {
                entity.ridingEntity.riddenByEntity = null;
                entity.ridingEntity = null;
            }

            this.theProfiler.startSection("tick");
            if (!entity.isDead) {
                try {
                    this.updateEntity(entity);
                } catch (Throwable throwable) {
                    optimizationsAndTweaks$handleEntityCrash(entity, throwable);
                }
            }

            if (entity.isDead) {
                int chunkX = entity.chunkCoordX;
                int chunkZ = entity.chunkCoordZ;
                if (entity.addedToChunk && this.chunkExists(chunkX, chunkZ)) {
                    this.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);
                }
                iterator.remove();
                onEntityRemoved(entity);
            }

            this.theProfiler.endSection();
        }
    }

    @Unique
    private void optimizationsAndTweaks$updateTileEntities() {
        if (this.loadedTileEntityList == null) {
            System.err.println("[OptimizationsAndTweaks] loadedTileEntityList is null");
            return;
        }

        this.field_147481_N = true;
        Iterator<TileEntity> iterator = this.loadedTileEntityList.iterator();
        while (iterator.hasNext()) {
            TileEntity tileentity = iterator.next();
            if (tileentity == null) {
                System.err.println("[OptimizationsAndTweaks] Encountered null tile entity in loadedTileEntityList");
                iterator.remove();
                continue;
            }

            if (!tileentity.isInvalid() && tileentity.hasWorldObj() && this.blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)) {
                try {
                    tileentity.updateEntity();
                } catch (Throwable throwable) {
                    optimizationsAndTweaks$handleTileEntityCrash(tileentity, throwable);
                }
            }

            if (tileentity.isInvalid()) {
                iterator.remove();
                if (this.chunkExists(tileentity.xCoord >> 4, tileentity.zCoord >> 4)) {
                    Chunk chunk = this.getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);
                    if (chunk != null) {
                        chunk.removeInvalidTileEntity(tileentity.xCoord & 15, tileentity.yCoord, tileentity.zCoord & 15);
                    }
                }
            }
        }
        optimizationsAndTweaks$handlePendingTileEntities();
        this.field_147481_N = false;
    }

    @Unique
    private void optimizationsAndTweaks$handleTileEntityCrash(TileEntity tileentity, Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking block entity");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block entity being ticked");
        tileentity.func_145828_a(crashreportcategory);
        if (ForgeModContainer.removeErroringTileEntities) {
            FMLLog.getLogger()
                .log(org.apache.logging.log4j.Level.ERROR, crashreport.getCompleteReport());
            tileentity.invalidate();
            setBlockToAir(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
        } else {
            throw new ReportedException(crashreport);
        }
    }

    @Unique
    private void optimizationsAndTweaks$handlePendingTileEntities() {
        for (Object obj : this.addedTileEntityList) {
            if (obj instanceof TileEntity) {
                TileEntity tileentity1 = (TileEntity) obj;
                if (!tileentity1.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileentity1)) {
                        this.loadedTileEntityList.add(tileentity1);
                    }
                } else {
                    if (this.chunkExists(tileentity1.xCoord >> 4, tileentity1.zCoord >> 4)) {
                        Chunk chunk1 = this.getChunkFromChunkCoords(tileentity1.xCoord >> 4, tileentity1.zCoord >> 4);
                        if (chunk1 != null) {
                            chunk1.removeInvalidTileEntity(
                                tileentity1.xCoord & 15,
                                tileentity1.yCoord,
                                tileentity1.zCoord & 15);
                        }
                    }
                }
            }
        }
        this.addedTileEntityList.clear();
    }

    @Shadow
    public boolean setBlockToAir(int x, int y, int z) {
        return this.setBlock(x, y, z, Blocks.air, 0, 3);
    }

    @Shadow
    public boolean setBlock(int x, int y, int z, Block blockIn, int metadataIn, int flags) {
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
            if (y < 0) {
                return false;
            } else if (y >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                Block block1 = null;
                net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;

                if ((flags & 1) != 0) {
                    block1 = chunk.getBlock(x & 15, y, z & 15);
                }

                if (this.captureBlockSnapshots && !this.isRemote) {
                    blockSnapshot = net.minecraftforge.common.util.BlockSnapshot
                        .getBlockSnapshot((World) (Object) this, x, y, z, flags);
                    this.capturedBlockSnapshots.add(blockSnapshot);
                }

                boolean flag = chunk.func_150807_a(x & 15, y, z & 15, blockIn, metadataIn);

                if (!flag && blockSnapshot != null) {
                    this.capturedBlockSnapshots.remove(blockSnapshot);
                    blockSnapshot = null;
                }

                this.theProfiler.startSection("checkLight");
                this.func_147451_t(x, y, z);
                this.theProfiler.endSection();

                if (flag && blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    // Modularize client and physic updates
                    this.markAndNotifyBlock(x, y, z, chunk, block1, blockIn, flags);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public synchronized TileEntity getTileEntity(int x, int y, int z) {
        if (y >= 0 && y < 256) {
            TileEntity tileentity = null;
            int l;
            TileEntity tileentity1;

            if (this.field_147481_N) {
                for (l = 0; l < this.addedTileEntityList.size(); ++l) {
                    tileentity1 = (TileEntity) this.addedTileEntityList.get(l);

                    if (!tileentity1.isInvalid() && tileentity1.xCoord == x
                        && tileentity1.yCoord == y
                        && tileentity1.zCoord == z) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            if (tileentity == null) {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);

                if (chunk != null) {
                    tileentity = chunk.func_150806_e(x & 15, y, z & 15);
                }
            }

            if (tileentity == null) {
                for (l = 0; l < this.addedTileEntityList.size(); ++l) {
                    tileentity1 = (TileEntity) this.addedTileEntityList.get(l);

                    if (!tileentity1.isInvalid() && tileentity1.xCoord == x
                        && tileentity1.yCoord == y
                        && tileentity1.zCoord == z) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            return tileentity;
        } else {
            return null;
        }
    }
}
