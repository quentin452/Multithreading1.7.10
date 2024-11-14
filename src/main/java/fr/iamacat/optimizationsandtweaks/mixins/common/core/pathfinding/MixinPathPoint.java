package fr.iamacat.optimizationsandtweaks.mixins.common.core.pathfinding;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PathPoint.class)
public class MixinPathPoint {

    @Unique
    private static final int HILBERT_CURVE_ORDER = 4;
    /** The x coordinate of this point */
    @Shadow
    public final int xCoord;
    /** The y coordinate of this point */
    @Shadow
    public final int yCoord;
    /** The z coordinate of this point */
    @Shadow
    public final int zCoord;
    /** A hash of the coordinates used to identify this point */
    @Shadow
    public final int hash;
    /** The index of this point in its assigned path */
    @Shadow
    public int index = -1;
    /** The distance along the path to this point */
    @Shadow
    public float totalPathDistance;
    /** The linear distance to the next point */
    @Shadow
    public float distanceToNext;
    /** The distance to the target */
    @Shadow
    public float distanceToTarget;
    /** The point preceding this in its assigned path */
    @Shadow
    public PathPoint previous;

    /** Indicates this is the origin */
    @Shadow
    public boolean isFirst;

    public MixinPathPoint(int p_i2135_1_, int p_i2135_2_, int p_i2135_3_) {
        this.xCoord = p_i2135_1_;
        this.yCoord = p_i2135_2_;
        this.zCoord = p_i2135_3_;
        this.hash = makeHash(p_i2135_1_, p_i2135_2_, p_i2135_3_);
    }

    @Shadow
    public static int makeHash(int p_75830_0_, int p_75830_1_, int p_75830_2_) {
        return p_75830_1_ & 255 | (p_75830_0_ & 32767) << 8
            | (p_75830_2_ & 32767) << 24
            | (p_75830_0_ < 0 ? Integer.MIN_VALUE : 0)
            | (p_75830_2_ < 0 ? 32768 : 0);
    }

    /**
     * Returns the linear distance to another path point
     */
    @Shadow
    public float distanceTo(PathPoint p_75829_1_) {
        float f = (float) (p_75829_1_.xCoord - this.xCoord);
        float f1 = (float) (p_75829_1_.yCoord - this.yCoord);
        float f2 = (float) (p_75829_1_.zCoord - this.zCoord);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    /**
     * Returns the squared distance to another path point
     */
    @Shadow
    public float distanceToSquared(PathPoint p_75832_1_) {
        float f = (float) (p_75832_1_.xCoord - this.xCoord);
        float f1 = (float) (p_75832_1_.yCoord - this.yCoord);
        float f2 = (float) (p_75832_1_.zCoord - this.zCoord);
        return f * f + f1 * f1 + f2 * f2;
    }

    @Shadow
    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PathPoint)) {
            return false;
        } else {
            PathPoint pathpoint = (PathPoint) p_equals_1_;
            return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord
                && this.yCoord == pathpoint.yCoord
                && this.zCoord == pathpoint.zCoord;
        }
    }

    @Overwrite
    public synchronized int hashCode() {
        int h = 0;
        for (int i = 0; i < HILBERT_CURVE_ORDER; ++i) {
            h ^= optimizationsAndTweaks$hilbertCurve(this.xCoord, this.yCoord, this.zCoord, i) << (i * 2);
        }
        return h;
    }

    @Unique
    private static synchronized int optimizationsAndTweaks$hilbertCurve(int x, int y, int z, int n) {
        int t = x ^ y ^ z;
        int s = (x & y) | (~x & z);
        int u = (y & z) | (~y & x);
        x = s ^ u;
        y = t ^ u;
        z = t ^ s;
        return (x | (y << 1) | (z << 2)) >>> (31 - n);
    }

    /**
     * Returns true if this point has already been assigned to a path
     */
    @Shadow
    public boolean isAssigned() {
        return this.index >= 0;
    }

    @Shadow
    public String toString() {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
}
