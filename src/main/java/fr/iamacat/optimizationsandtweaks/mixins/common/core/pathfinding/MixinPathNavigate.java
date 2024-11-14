package fr.iamacat.optimizationsandtweaks.mixins.common.core.pathfinding;

import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PathNavigate.class)
public class MixinPathNavigate {

    @Shadow
    private EntityLiving theEntity;
    @Shadow
    private World worldObj;
    /** The PathEntity being followed. */
    @Shadow
    private PathEntity currentPath;
    @Shadow
    private double speed;
    /** The number of blocks (extra) +/- in each axis that get pulled out as cache for the pathfinder's search space */
    @Shadow
    private IAttributeInstance pathSearchRange;
    @Shadow
    private boolean noSunPathfind;
    /** Time, in number of ticks, following the current path */
    @Shadow
    private int totalTicks;
    /** The time when the last position check was done (to detect successful movement) */
    @Shadow
    private int ticksAtLastPos;
    /** Coordinates of the entity's position last time a check was done (part of monitoring getting 'stuck') */
    @Shadow
    private Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

    /** Specifically, if a wooden door block is even considered to be passable by the pathfinder */
    @Shadow
    private boolean canPassOpenWoodenDoors = true;
    /** If door blocks are considered passable even when closed */
    @Shadow
    private boolean canPassClosedWoodenDoors;
    /** If water blocks are avoided (at least by the pathfinder) */
    @Shadow
    private boolean avoidsWater;
    /**
     * If the entity can swim. Swimming AI enables this and the pathfinder will also cause the entity to swim straight
     * upwards when underwater
     */
    @Shadow
    private boolean canSwim;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
        CompletableFuture<Boolean> futureResult = CompletableFuture.supplyAsync(() -> {
            PathEntity pathentity = this.getPathToEntityLiving(p_75497_1_);
            return pathentity != null && this.setPath(pathentity, p_75497_2_);
        });
        return futureResult.getNow(false);
    }

    @Shadow
    public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
        return !this.canNavigate() ? null
            : this.worldObj.getPathEntityToEntity(
                this.theEntity,
                p_75494_1_,
                this.getPathSearchRange(),
                this.canPassOpenWoodenDoors,
                this.canPassClosedWoodenDoors,
                this.avoidsWater,
                this.canSwim);
    }

    @Shadow
    private boolean canNavigate() {
        return this.theEntity.onGround || this.canSwim && this.isInLiquid()
            || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie
                && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    @Shadow
    public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
        if (p_75484_1_ == null) {
            this.currentPath = null;
            return false;
        } else {
            if (!p_75484_1_.isSamePath(this.currentPath)) {
                this.currentPath = p_75484_1_;
            }

            if (this.noSunPathfind) {
                this.removeSunnyPath();
            }

            if (this.currentPath.getCurrentPathLength() == 0) {
                return false;
            } else {
                this.speed = p_75484_2_;
                Vec3 vec3 = this.getEntityPosition();
                this.ticksAtLastPos = this.totalTicks;
                this.lastPosCheck.xCoord = vec3.xCoord;
                this.lastPosCheck.yCoord = vec3.yCoord;
                this.lastPosCheck.zCoord = vec3.zCoord;
                return true;
            }
        }
    }

    @Shadow
    private void removeSunnyPath() {
        if (!this.worldObj.canBlockSeeTheSky(
            MathHelper.floor_double(this.theEntity.posX),
            (int) (this.theEntity.boundingBox.minY + 0.5D),
            MathHelper.floor_double(this.theEntity.posZ))) {
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);

                if (this.worldObj.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)) {
                    this.currentPath.setCurrentPathLength(i - 1);
                    return;
                }
            }
        }
    }

    @Shadow
    private Vec3 getEntityPosition() {
        return Vec3.createVectorHelper(this.theEntity.posX, this.getPathableYPos(), this.theEntity.posZ);
    }

    @Shadow
    private int getPathableYPos() {
        if (this.theEntity.isInWater() && this.canSwim) {
            int i = (int) this.theEntity.boundingBox.minY;
            Block block = this.worldObj.getBlock(
                MathHelper.floor_double(this.theEntity.posX),
                i,
                MathHelper.floor_double(this.theEntity.posZ));
            int j = 0;

            do {
                if (block != Blocks.flowing_water && block != Blocks.water) {
                    return i;
                }

                ++i;
                block = this.worldObj.getBlock(
                    MathHelper.floor_double(this.theEntity.posX),
                    i,
                    MathHelper.floor_double(this.theEntity.posZ));
                ++j;
            } while (j <= 16);

            return (int) this.theEntity.boundingBox.minY;
        } else {
            return (int) (this.theEntity.boundingBox.minY + 0.5D);
        }
    }

    @Shadow
    private boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
    }

    @Shadow
    public float getPathSearchRange() {
        return (float) this.pathSearchRange.getAttributeValue();
    }

}
