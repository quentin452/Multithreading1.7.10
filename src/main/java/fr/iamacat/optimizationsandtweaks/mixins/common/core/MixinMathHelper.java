package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Random;

import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mixin(MathHelper.class)
public class MixinMathHelper {

    @Unique
    private static float[] SIN_TABLE2 = new float[65536];
    @Unique
    private static final float optimizationsAndTweaks$PI = (float) Math.PI;

    /** A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536. */
    /**
     * Though it looks like an array, this is really more like a mapping. Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531. Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value. Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    @Shadow
    private static final int[] multiplyDeBruijnBitPosition;

    /**
     * sin looked up in a table
     */
    @Overwrite
    public static float sin(float p_76126_0_) {
        return SIN_TABLE2[(int) (p_76126_0_ * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    @Overwrite
    public static float cos(float p_76134_0_) {
        return SIN_TABLE2[(int) (p_76134_0_ * 10430.378F + 16384.0F) & 65535];
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static float sqrt_float(float p_76129_0_) {
        return (float) Math.sqrt(p_76129_0_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static float sqrt_double(double p_76133_0_) {
        return (float) Math.sqrt(p_76133_0_);
    }

    /**
     * Returns the greatest integer less than or equal to the float argument
     */
    @Overwrite
    public static int floor_float(float p_76141_0_) {
        return (int) p_76141_0_;
    }

    /**
     * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int truncateDoubleToInt(double p_76140_0_) {
        return (int) (p_76140_0_ + 1024.0D) - 1024;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    @Overwrite
    public static int floor_double(double p_76128_0_) {
        return (int) Math.floor(p_76128_0_);
    }

    /**
     * Long version of floor_double
     */
    @Overwrite
    public static long floor_double_long(double p_76124_0_) {
        return (long) Math.floor(p_76124_0_);
    }

    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int func_154353_e(double p_154353_0_) {
        return (int) (p_154353_0_ >= 0.0D ? p_154353_0_ : -p_154353_0_ + 1.0D);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static float abs(float p_76135_0_) {
        return p_76135_0_ >= 0.0F ? p_76135_0_ : -p_76135_0_;
    }

    /**
     * Returns the unsigned value of an int.
     */
    @Overwrite
    public static int abs_int(int p_76130_0_) {
        return p_76130_0_ >= 0 ? p_76130_0_ : -p_76130_0_;
    }

    @Overwrite
    public static int ceiling_float_int(float p_76123_0_) {
        int i = (int) p_76123_0_;
        return p_76123_0_ > i ? i + 1 : i;
    }

    @Overwrite
    public static int ceiling_double_int(double p_76143_0_) {
        int i = (int) p_76143_0_;
        return p_76143_0_ > i ? i + 1 : i;
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters.
     */
    @Overwrite
    public static int clamp_int(int p_76125_0_, int p_76125_1_, int p_76125_2_) {
        return p_76125_0_ < p_76125_1_ ? p_76125_1_ : (Math.min(p_76125_0_, p_76125_2_));
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters
     */
    @Overwrite
    public static float clamp_float(float p_76131_0_, float p_76131_1_, float p_76131_2_) {
        return p_76131_0_ < p_76131_1_ ? p_76131_1_ : (Math.min(p_76131_0_, p_76131_2_));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static double clamp_double(double p_151237_0_, double p_151237_2_, double p_151237_4_) {
        return p_151237_0_ < p_151237_2_ ? p_151237_2_ : (Math.min(p_151237_0_, p_151237_4_));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_) {
        return p_151238_4_ < 0.0D ? p_151238_0_
            : (p_151238_4_ > 1.0D ? p_151238_2_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
    }

    /**
     * Maximum of the absolute value of two numbers.
     */
    @Overwrite
    public static double abs_max(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0D) {
            p_76132_0_ = -p_76132_0_;
        }

        if (p_76132_2_ < 0.0D) {
            p_76132_2_ = -p_76132_2_;
        }

        return Math.max(p_76132_0_, p_76132_2_);
    }

    /**
     * Buckets an integer with specifed bucket sizes. Args: i, bucketSize
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int bucketInt(int p_76137_0_, int p_76137_1_) {
        return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
    }

    /**
     * Tests if a string is null or of length zero
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static boolean stringNullOrLengthZero(String p_76139_0_) {
        return p_76139_0_ == null || p_76139_0_.isEmpty();
    }

    @Overwrite
    public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_) {
        return p_151240_1_ >= p_151240_2_ ? p_151240_1_
            : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_) {
        if (p_76136_2_ <= p_76136_1_) {
            return p_76136_1_;
        }
        return p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
    }

    @Overwrite
    public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_) {
        return p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static double average(long[] p_76127_0_) {
        long i = 0L;

        for (long l : p_76127_0_) {
            i += l;
        }

        return (double) i / (double) p_76127_0_.length;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    @Overwrite
    public static float wrapAngleTo180_float(float angle) {
        if (angle >= 180.0F) {
            angle -= 360.0F;
        } else if (angle < -180.0F) {
            angle += 360.0F;
        }
        return angle;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    @Overwrite
    public static double wrapAngleTo180_double(double angle) {
        if (angle >= 180.0D) {
            angle -= 360.0D;
        } else if (angle < -180.0D) {
            angle += 360.0D;
        }
        return angle;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static int parseIntWithDefault(String input, int defaultValue) {
        if (!input.isEmpty()) {
            return Integer.parseInt(input);
        }
        return defaultValue;
    }

    /**
     * parses the string as integer or returns the second parameter if it fails. this value is capped to par2
     */
    @Overwrite
    public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_) {
        int k = p_82714_1_;

        if (!p_82714_0_.isEmpty()) {
            k = Integer.parseInt(p_82714_0_);
        }

        if (k < p_82714_2_) {
            k = p_82714_2_;
        }

        return k;
    }

    /**
     * parses the string as double or returns the second parameter if it fails.
     */
    @Overwrite
    public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_) {
        return Double.parseDouble(p_82712_0_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_) {
        double d2 = p_82713_1_;

        if (!p_82713_0_.isEmpty()) {
            d2 = Double.parseDouble(p_82713_0_);
        }

        if (d2 < p_82713_3_) {
            d2 = p_82713_3_;
        }

        return d2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int roundUpToPowerOfTwo(int value) {
        if (value <= 0) return 0;

        value--;

        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;

        return value + 1;
    }

    /**
     * Is the given value a power of two? (1, 2, 4, 8, 16, ...)
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    private static boolean isPowerOfTwo(int value) {
        return (value > 0) && ((value & (value - 1)) == 0);
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int calculateLogBaseTwoDeBruijn(int value) {
        value = (value > 0) ? value : 1;
        return multiplyDeBruijnBitPosition[(int) (value * 125613361L >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value. This is effectively the index of the
     * highest bit that is set. For example, if the number in binary is 0...100101, this will return 5.
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int calculateLogBaseTwo(int value) {
        value = (value > 0) ? value : 1;

        int log2 = multiplyDeBruijnBitPosition[(int) (value * 125613361L >> 27) & 31];

        return (1 << log2) == value ? log2 : log2 - 1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int func_154354_b(int p_154354_0_, int p_154354_1_) {
        if (p_154354_1_ != 0) {
            int k = p_154354_0_ % p_154354_1_;
            return (k == 0) ? p_154354_0_ : p_154354_0_ + ((p_154354_0_ < 0) ? -p_154354_1_ : p_154354_1_) - k;
        }
        return 0;
    }

    static {
        for (int var0 = 0; var0 < 65536; ++var0) {
            SIN_TABLE2[var0] = (float) Math.sin(var0 * optimizationsAndTweaks$PI * 2.0D / 65536.0D);
        }

        multiplyDeBruijnBitPosition = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13,
            23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
    }
}
