package fr.iamacat.optimizationsandtweaks.mixins.common.etfuturumrequiem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import ganymedes01.etfuturum.core.utils.Utils;

@Mixin(Utils.class)
public class MixinUtils {

    // Static constants to avoid resetting in each method call
    @Unique
    private static final double optimizationsAndTweaks$frac_bias = Double.longBitsToDouble(4805340802404319232L);
    @Unique
    private static final double[] optimizationsAndTweaks$asine_tab = new double[257];
    @Unique
    private static final double[] optimizationsAndTweaks$cos_tab = new double[257];

    static {
        // Initialize sine and cosine arrays once
        for (int j = 0; j < 257; ++j) {
            double ratio = (double) j / 256.0;
            double asinValue = Math.asin(ratio);
            optimizationsAndTweaks$cos_tab[j] = Math.cos(asinValue);
            optimizationsAndTweaks$asine_tab[j] = asinValue;
        }
    }

    /**
     * @author quentin452
     * @reason Optimize atan2
     */
    @Overwrite
    public static double atan2(double y, double x) {
        // Check if any of the values ​​are NaN (Not a Number) and return NaN if so.
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }

        // Handle cases where the values ​​are negative by reversing the signs if necessary
        boolean isNegativeY = y < 0.0;
        if (isNegativeY) y = -y;

        boolean isNegativeX = x < 0.0;
        if (isNegativeX) x = -x;

        // Determine if y is greater than x
        boolean isYGreaterThanX = y > x;
        if (isYGreaterThanX) {
            double temp = x;
            x = y;
            y = temp;
        }

        // Calculate magnitude and normalize components
        double magnitudeSquared = x * x + y * y;
        double invMagnitude = optimizationsAndTweaks$invSqrt(magnitudeSquared);
        x *= invMagnitude;
        y *= invMagnitude;

        // Use the pre-calculated sine and cosine tables to find the angle
        double angle = optimizationsAndTweaks$getAngle(y, x);

        // Adjust angle based on initial signs
        if (isYGreaterThanX) angle = (Math.PI / 2) - angle;
        if (isNegativeX) angle = Math.PI - angle;
        if (isNegativeY) angle = -angle;

        return angle;
    }

    @Unique
    private static double optimizationsAndTweaks$getAngle(double y, double x) {
        double biasedY = optimizationsAndTweaks$frac_bias + y;
        int index = (int) Double.doubleToRawLongBits(biasedY);
        double asinValue = optimizationsAndTweaks$asine_tab[index];
        double cosValue = optimizationsAndTweaks$cos_tab[index];
        double deltaY = biasedY - optimizationsAndTweaks$frac_bias;
        double delta = y * cosValue - x * deltaY;
        double correctionTerm = (6.0 + delta * delta) * delta * 0.16666666666666666;
        return asinValue + correctionTerm;
    }

    @Unique
    private static double optimizationsAndTweaks$invSqrt(double num) {
        return 1 / Math.sqrt(num);
    }
}
