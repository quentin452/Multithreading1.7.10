package fr.iamacat.optimizationsandtweaks.mixins.common.core.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PathEntity.class)
public class MixinPathEntity {

    /** The actual points in the path */
    @Shadow
    private final PathPoint[] points;
    /** PathEntity Array Index the Entity is currently targeting */
    @Shadow
    private int currentPathIndex;
    /** The total length of the path */
    @Shadow
    private int pathLength;

    /**
     * Directs this path to the next point in its array
     */
    @Shadow
    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    @Shadow
    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    /**
     * returns the last PathPoint of the Array
     */
    @Shadow
    public PathPoint getFinalPathPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    /**
     * return the PathPoint located at the specified PathIndex, usually the current one
     */
    @Shadow
    public PathPoint getPathPointFromIndex(int p_75877_1_) {
        return this.points[p_75877_1_];
    }

    @Shadow
    public int getCurrentPathLength() {
        return this.pathLength;
    }

    @Shadow
    public void setCurrentPathLength(int p_75871_1_) {
        this.pathLength = p_75871_1_;
    }

    @Shadow
    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    @Shadow
    public void setCurrentPathIndex(int p_75872_1_) {
        this.currentPathIndex = p_75872_1_;
    }

    public MixinPathEntity(PathPoint[] p_i2136_1_) {
        this.points = p_i2136_1_;
        this.pathLength = p_i2136_1_.length;
    }

    /**
     * Gets the vector of the PathPoint associated with the given index.
     */
    @Overwrite
    public synchronized Vec3 getVectorFromIndex(Entity p_75881_1_, int p_75881_2_) {
        double d0 = (double) this.points[p_75881_2_].xCoord + ((int) (p_75881_1_.width + 1.0F)) * 0.5D;
        double d1 = this.points[p_75881_2_].yCoord;
        double d2 = (double) this.points[p_75881_2_].zCoord + ((int) (p_75881_1_.width + 1.0F)) * 0.5D;
        return Vec3.createVectorHelper(d0, d1, d2);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3 getPosition(Entity p_75878_1_) {
        return this.getVectorFromIndex(p_75878_1_, this.currentPathIndex);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    @Overwrite
    public boolean isSamePath(PathEntity p_75876_1_) {
        if (p_75876_1_ == null) {
            return false;
        } else if (p_75876_1_.points.length != this.points.length) {
            return false;
        } else {
            for (int i = 0; i < this.points.length; ++i) {
                if (this.points[i].xCoord != p_75876_1_.points[i].xCoord
                    || this.points[i].yCoord != p_75876_1_.points[i].yCoord
                    || this.points[i].zCoord != p_75876_1_.points[i].zCoord) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Returns true if the final PathPoint in the PathEntity is equal to Vec3D coords.
     */
    @Overwrite
    public boolean isDestinationSame(Vec3 p_75880_1_) {
        PathPoint pathpoint = this.getFinalPathPoint();
        return pathpoint == null ? false
            : pathpoint.xCoord == (int) p_75880_1_.xCoord && pathpoint.zCoord == (int) p_75880_1_.zCoord;
    }
}
