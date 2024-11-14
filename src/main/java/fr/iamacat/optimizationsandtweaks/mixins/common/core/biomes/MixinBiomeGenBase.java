package fr.iamacat.optimizationsandtweaks.mixins.common.core.biomes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BiomeGenBase.class)
public class MixinBiomeGenBase {

    @Shadow
    public Block topBlock;
    @Shadow
    public int field_150604_aj;
    @Shadow
    public Block fillerBlock;
    @Shadow
    protected static final NoiseGeneratorPerlin temperatureNoise;
    @Shadow
    public float temperature;
    @Unique
    Block optimizationsAndTweaks$block;
    @Unique
    byte optimizationsAndTweaks$b0;
    @Unique
    Block optimizationsAndTweaks$block1;
    @Unique
    Block optimizationsAndTweaks$block2;
    @Unique
    int optimizationsAndTweaks$k;
    @Unique
    int optimizationsAndTweaks$l;
    @Unique
    int optimizationsAndTweaks$i1;
    @Unique
    int optimizationsAndTweaks$j1;
    @Unique
    int optimizationsAndTweaks$k1;
    @Unique
    int optimizationsAndTweaks$l1;
    @Unique
    int optimizationsAndTweaks$i2 = (optimizationsAndTweaks$j1 * 16 + optimizationsAndTweaks$i1)
        * optimizationsAndTweaks$k1 + optimizationsAndTweaks$l1;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public synchronized final void genBiomeTerrain(World p_150560_1_, Random p_150560_2_, Block[] p_150560_3_,
        byte[] p_150560_4_, int p_150560_5_, int p_150560_6_, double p_150560_7_) {
        optimizationsAndTweaks$Initialize(p_150560_2_, p_150560_3_, p_150560_5_, p_150560_6_, p_150560_7_);
        for (optimizationsAndTweaks$l1 = 255; optimizationsAndTweaks$l1 >= 0; --optimizationsAndTweaks$l1) {
            optimizationsAndTweaks$i2 = (optimizationsAndTweaks$j1 * 16 + optimizationsAndTweaks$i1)
                * optimizationsAndTweaks$k1 + optimizationsAndTweaks$l1;
            if (optimizationsAndTweaks$l1 <= p_150560_2_.nextInt(5)) {
                optimizationsAndTweaks$genBiomeTerrainSub1(p_150560_3_);
            } else {
                optimizationsAndTweaks$genBiomeTerrainSub2(p_150560_3_);

                if (optimizationsAndTweaks$block2 != null
                    && optimizationsAndTweaks$block2.getMaterial() != Material.air) {
                    if (optimizationsAndTweaks$block2 == Blocks.stone) {
                        if (optimizationsAndTweaks$k == -1) {
                            if (optimizationsAndTweaks$l <= 0) {
                                optimizationsAndTweaks$genBiomeTerrainSub3();
                            } else if (optimizationsAndTweaks$l1 >= 59 && optimizationsAndTweaks$l1 <= 64) {
                                optimizationsAndTweaks$genBiomeTerrainSub4();
                            }

                            if (optimizationsAndTweaks$l1 < 63 && (optimizationsAndTweaks$block == null
                                || optimizationsAndTweaks$block.getMaterial() == Material.air)) {
                                optimizationsAndTweaks$genBiomeTerrainSub5(p_150560_5_, p_150560_6_);
                                optimizationsAndTweaks$b0 = 0;
                            }

                            optimizationsAndTweaks$k = optimizationsAndTweaks$l;

                            if (optimizationsAndTweaks$l1 >= 62) {
                                optimizationsAndTweaks$genBiomeTerrainSub6(p_150560_3_, p_150560_4_);
                            } else if (optimizationsAndTweaks$l1 < 56 - optimizationsAndTweaks$l) {
                                optimizationsAndTweaks$genBiomeTerrainSub7(p_150560_3_);
                            } else {
                                optimizationsAndTweaks$genBiomeTerrainSub8(p_150560_3_);
                            }
                        } else if (optimizationsAndTweaks$k > 0) {
                            optimizationsAndTweaks$genBiomeTerrainSub9(p_150560_2_, p_150560_3_);
                        }
                    }
                } else {
                    optimizationsAndTweaks$genBiomeTerrainSub10();
                }
            }
        }
    }

    @Unique
    public final void optimizationsAndTweaks$Initialize(Random p_150560_2_, Block[] p_150560_3_, int p_150560_5_,
        int p_150560_6_, double p_150560_7_) {
        optimizationsAndTweaks$block = this.topBlock;
        optimizationsAndTweaks$b0 = (byte) (this.field_150604_aj & 255);
        optimizationsAndTweaks$block1 = this.fillerBlock;
        optimizationsAndTweaks$k = -1;
        optimizationsAndTweaks$l = (int) (p_150560_7_ / 3.0D + 3.0D + p_150560_2_.nextDouble() * 0.25D);
        optimizationsAndTweaks$i1 = p_150560_5_ & 15;
        optimizationsAndTweaks$j1 = p_150560_6_ & 15;
        optimizationsAndTweaks$k1 = p_150560_3_.length / 256;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub1(Block[] p_150560_3_) {
        p_150560_3_[optimizationsAndTweaks$i2] = Blocks.bedrock;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub2(Block[] p_150560_3_) {
        optimizationsAndTweaks$block2 = p_150560_3_[optimizationsAndTweaks$i2];
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub3() {
        optimizationsAndTweaks$block = null;
        optimizationsAndTweaks$b0 = 0;
        optimizationsAndTweaks$block1 = Blocks.stone;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub4() {
        optimizationsAndTweaks$block = this.topBlock;
        optimizationsAndTweaks$b0 = (byte) (this.field_150604_aj & 255);
        optimizationsAndTweaks$block1 = this.fillerBlock;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub5(int p_150560_5_, int p_150560_6_) {
        if (this.getFloatTemperature(p_150560_5_, optimizationsAndTweaks$l1, p_150560_6_) < 0.15F) {
            optimizationsAndTweaks$block = Blocks.ice;
        } else {
            optimizationsAndTweaks$block = Blocks.water;
        }
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub6(Block[] p_150560_3_, byte[] p_150560_4_) {
        p_150560_3_[optimizationsAndTweaks$i2] = optimizationsAndTweaks$block;
        p_150560_4_[optimizationsAndTweaks$i2] = optimizationsAndTweaks$b0;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub7(Block[] p_150560_3_) {
        optimizationsAndTweaks$block = null;
        optimizationsAndTweaks$block1 = Blocks.stone;
        p_150560_3_[optimizationsAndTweaks$i2] = Blocks.gravel;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub8(Block[] p_150560_3_) {
        p_150560_3_[optimizationsAndTweaks$i2] = optimizationsAndTweaks$block1;
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub9(Random p_150560_2_, Block[] p_150560_3_) {
        --optimizationsAndTweaks$k;
        p_150560_3_[optimizationsAndTweaks$i2] = optimizationsAndTweaks$block1;

        if (optimizationsAndTweaks$k == 0 && optimizationsAndTweaks$block1 == Blocks.sand) {
            optimizationsAndTweaks$k = p_150560_2_.nextInt(4) + Math.max(0, optimizationsAndTweaks$l1 - 63);
            optimizationsAndTweaks$block1 = Blocks.sandstone;
        }
    }

    @Unique
    public final void optimizationsAndTweaks$genBiomeTerrainSub10() {
        optimizationsAndTweaks$k = -1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_,
        int p_150573_5_, int p_150573_6_, double p_150573_7_) {
        this.genBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public final float getFloatTemperature(int p_150564_1_, int p_150564_2_, int p_150564_3_) {
        if (p_150564_2_ > 64) {
            float f = (float) temperatureNoise.func_151601_a(p_150564_1_ / 8.0D, p_150564_3_ / 8.0D) * 4.0F;
            return this.temperature - (f + p_150564_2_ - 64.0F) * 0.05F / 30.0F;
        } else {
            return this.temperature;
        }
    }

    static {
        temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
    }
}
