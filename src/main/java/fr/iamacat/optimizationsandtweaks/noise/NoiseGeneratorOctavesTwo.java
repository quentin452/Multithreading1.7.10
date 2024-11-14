package fr.iamacat.optimizationsandtweaks.noise;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.NoiseGenerator;

public class NoiseGeneratorOctavesTwo extends NoiseGenerator {

    private final NoiseGeneratorImprovedTwo[] generatorCollection;
    private final int octaves;

    public NoiseGeneratorOctavesTwo(Random p_i2111_1_, int p_i2111_2_) {
        this.octaves = p_i2111_2_;
        this.generatorCollection = new NoiseGeneratorImprovedTwo[p_i2111_2_];

        for (int j = 0; j < p_i2111_2_; ++j) {
            this.generatorCollection[j] = new NoiseGeneratorImprovedTwo(p_i2111_1_);
        }
    }

    public double[] generateNoiseOctaves(double[] p_76304_1_, int p_76304_2_, int p_76304_3_, int p_76304_4_,
        int p_76304_5_, int p_76304_6_, int p_76304_7_, double p_76304_8_, double p_76304_10_, double p_76304_12_) {
        if (p_76304_1_ == null) {
            p_76304_1_ = new double[p_76304_5_ * p_76304_6_ * p_76304_7_];
        } else {
            Arrays.fill(p_76304_1_, 0.0D);
        }

        double d6 = 1.0D;

        for (int l1 = 0; l1 < this.octaves; ++l1) {
            double d3 = p_76304_2_ * d6 * p_76304_8_;
            double d4 = p_76304_3_ * d6 * p_76304_10_;
            double d5 = p_76304_4_ * d6 * p_76304_12_;
            long i2 = MathHelper.floor_double_long(d3);
            long j2 = MathHelper.floor_double_long(d5);
            d3 -= i2;
            d5 -= j2;
            i2 %= 16777216L;
            j2 %= 16777216L;
            d3 += i2;
            d5 += j2;
            this.generatorCollection[l1].populateNoiseArray(
                p_76304_1_,
                d3,
                d4,
                d5,
                p_76304_5_,
                p_76304_6_,
                p_76304_7_,
                p_76304_8_ * d6,
                p_76304_10_ * d6,
                p_76304_12_ * d6,
                d6);
            d6 /= 2.0D;
        }

        return p_76304_1_;
    }

    public double[] generateNoiseOctaves(double[] p_76305_1_, int p_76305_2_, int p_76305_3_, int p_76305_4_,
        int p_76305_5_, double p_76305_6_, double p_76305_8_, double p_76305_10_) {
        return this.generateNoiseOctaves(
            p_76305_1_,
            p_76305_2_,
            10,
            p_76305_3_,
            p_76305_4_,
            1,
            p_76305_5_,
            p_76305_6_,
            1.0D,
            p_76305_8_);
    }
}
