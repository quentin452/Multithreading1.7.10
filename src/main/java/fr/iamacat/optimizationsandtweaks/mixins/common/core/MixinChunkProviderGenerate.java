package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.eventhandler.Event;

@Mixin(ChunkProviderGenerate.class)
public abstract class MixinChunkProviderGenerate implements IChunkProvider {

    @Shadow
    private Random rand;
    @Shadow
    private NoiseGeneratorOctaves field_147431_j;
    @Shadow
    private NoiseGeneratorOctaves field_147432_k;
    @Shadow
    private NoiseGeneratorOctaves field_147429_l;
    @Shadow
    private NoiseGeneratorPerlin field_147430_m;
    /** A NoiseGeneratorOctaves used in generating terrain */
    @Shadow
    public NoiseGeneratorOctaves noiseGen6;
    /** Reference to the World object. */
    @Shadow
    private World worldObj;
    /** are map structures going to be generated (e.g. strongholds) */
    @Shadow
    private final boolean mapFeaturesEnabled;
    @Shadow
    private WorldType field_147435_p;
    @Shadow
    private final double[] field_147434_q;
    @Shadow
    private final float[] parabolicField;
    @Shadow
    private double[] stoneNoise = new double[256];
    @Shadow
    private MapGenBase caveGenerator = new MapGenCaves();
    /** Holds Stronghold Generator */
    @Shadow
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    /** Holds Village Generator */
    @Shadow
    private MapGenVillage villageGenerator = new MapGenVillage();

    /** Holds Mineshaft Generator */
    @Shadow
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    @Shadow
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

    /** Holds ravine generator */
    @Shadow
    private MapGenBase ravineGenerator = new MapGenRavine();
    /** The biomes that are used to generate the chunk */
    @Shadow
    private BiomeGenBase[] biomesForGeneration;
    @Shadow
    double[] field_147427_d;
    @Shadow
    double[] field_147428_e;
    @Shadow
    double[] field_147425_f;
    @Shadow
    double[] field_147426_g;

    protected MixinChunkProviderGenerate(boolean mapFeaturesEnabled) {
        this.mapFeaturesEnabled = mapFeaturesEnabled;
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_,
        BiomeGenBase[] p_147422_5_) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(
            this,
            p_147422_1_,
            p_147422_2_,
            p_147422_3_,
            p_147422_4_,
            p_147422_5_,
            this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) return;

        double d0 = 0.03125D;
        this.stoneNoise = this.field_147430_m
            .func_151599_a(this.stoneNoise, (p_147422_1_ * 16), (p_147422_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                BiomeGenBase biomegenbase = p_147422_5_[l + k * 16];
                biomegenbase.genTerrainBlocks(
                    this.worldObj,
                    this.rand,
                    p_147422_3_,
                    p_147422_4_,
                    p_147422_1_ * 16 + k,
                    p_147422_2_ * 16 + l,
                    this.stoneNoise[l + k * 16]);
            }
        }
    }

    // todo fix cascading worldgens/getblock exception/error while updating neighbors errors when Endlessids is
    // installed
    /**
     * @author
     * @reason
     */
    @Overwrite
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        Block[] blocks = new Block[65536];
        byte[] blockData = new byte[65536];

        optimizationsAndTweaks$generateTerrain(p_73154_1_, p_73154_2_, blocks, blockData);

        Chunk chunk = new Chunk(worldObj, blocks, blockData, p_73154_1_, p_73154_2_);
        chunk.generateSkylightMap();

        return chunk;
    }

    @Unique
    private void optimizationsAndTweaks$generateTerrain(int x, int z, Block[] blocks, byte[] blockData) {
        optimizationsAndTweaks$initializeGeneration(blocks, blockData, x, z);

        optimizationsAndTweaks$generateStructure(caveGenerator, x, z, blocks);
        optimizationsAndTweaks$generateStructure(ravineGenerator, x, z, blocks);

        if (mapFeaturesEnabled) {
            optimizationsAndTweaks$generateStructure(mineshaftGenerator, x, z, blocks);
            optimizationsAndTweaks$generateStructure(villageGenerator, x, z, blocks);
            optimizationsAndTweaks$generateStructure(strongholdGenerator, x, z, blocks);
            optimizationsAndTweaks$generateStructure(scatteredFeatureGenerator, x, z, blocks);
        }
    }

    @Unique
    private void optimizationsAndTweaks$initializeGeneration(Block[] blocks, byte[] blockData, int x, int z) {
        optimizationsAndTweaks$initializeGenerationsub1(x, z);
        optimizationsAndTweaks$initializeGenerationsub2(blocks, x, z);
        optimizationsAndTweaks$initializeGenerationsub3(blocks, blockData, x, z);
    }

    @Unique
    private void optimizationsAndTweaks$initializeGenerationsub1(int x, int z) {
        WorldChunkManager worldChunkManager = worldObj.getWorldChunkManager();
        biomesForGeneration = worldChunkManager.loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
    }

    @Unique
    private void optimizationsAndTweaks$initializeGenerationsub2(Block[] blocks, int x, int z) {
        func_147424_a(x, z, blocks);
    }

    @Unique
    private void optimizationsAndTweaks$initializeGenerationsub3(Block[] blocks, byte[] blockData, int x, int z) {
        replaceBlocksForBiome(x, z, blocks, blockData, biomesForGeneration);
    }

    @Unique
    private void optimizationsAndTweaks$generateStructure(MapGenBase structure, int x, int z, Block[] blocks) {
        structure.func_151539_a(this, worldObj, x, z, blocks);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_147424_a(int p_147424_1_, int p_147424_2_, Block[] p_147424_3_) {
        byte b0 = 63;
        this.biomesForGeneration = this.worldObj.getWorldChunkManager()
            .getBiomesForGeneration(this.biomesForGeneration, p_147424_1_ * 4 - 2, p_147424_2_ * 4 - 2, 10, 10);
        this.func_147423_a(p_147424_1_ * 4, 0, p_147424_2_ * 4);

        for (int k = 0; k < 4; ++k) {
            int l = k * 5;
            int i1 = (k + 1) * 5;

            for (int j1 = 0; j1 < 4; ++j1) {
                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;

                for (int k2 = 0; k2 < 32; ++k2) {
                    double d0 = 0.125D;
                    double d1 = this.field_147434_q[k1 + k2];
                    double d2 = this.field_147434_q[l1 + k2];
                    double d3 = this.field_147434_q[i2 + k2];
                    double d4 = this.field_147434_q[j2 + k2];
                    double d5 = (this.field_147434_q[k1 + k2 + 1] - d1) * d0;
                    double d6 = (this.field_147434_q[l1 + k2 + 1] - d2) * d0;
                    double d7 = (this.field_147434_q[i2 + k2 + 1] - d3) * d0;
                    double d8 = (this.field_147434_q[j2 + k2 + 1] - d4) * d0;

                    for (int l2 = 0; l2 < 8; ++l2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i3 = 0; i3 < 4; ++i3) {
                            int j3 = i3 + k * 4 << 12 | j1 * 4 << 8 | k2 * 8 + l2;
                            short short1 = 256;
                            j3 -= short1;
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;

                            for (int k3 = 0; k3 < 4; ++k3) {
                                if ((d15 += d16) > 0.0D) {
                                    p_147424_3_[j3 += short1] = Blocks.stone;
                                } else if (k2 * 8 + l2 < b0) {
                                    p_147424_3_[j3 += short1] = Blocks.water;
                                } else {
                                    p_147424_3_[j3 += short1] = null;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Shadow
    private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_) {
        this.field_147426_g = this.noiseGen6
            .generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, 200.0D, 200.0D, 0.5D);
        this.field_147427_d = this.field_147429_l.generateNoiseOctaves(
            this.field_147427_d,
            p_147423_1_,
            p_147423_2_,
            p_147423_3_,
            5,
            33,
            5,
            8.555150000000001D,
            4.277575000000001D,
            8.555150000000001D);
        this.field_147428_e = this.field_147431_j.generateNoiseOctaves(
            this.field_147428_e,
            p_147423_1_,
            p_147423_2_,
            p_147423_3_,
            5,
            33,
            5,
            684.412D,
            684.412D,
            684.412D);
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;
                BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f3 = biomegenbase1.rootHeight;
                        float f4 = biomegenbase1.heightVariation;

                        if (this.field_147435_p == WorldType.AMPLIFIED && f3 > 0.0F) {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

                        if (biomegenbase1.rootHeight > biomegenbase.rootHeight) {
                            f5 /= 2.0F;
                        }

                        f += f4 * f5;
                        f1 += f3 * f5;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = this.field_147426_g[i1] / 8000.0D;

                if (d12 < 0.0D) {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;

                if (d12 < 0.0D) {
                    d12 /= 2.0D;

                    if (d12 < -1.0D) {
                        d12 = -1.0D;
                    }

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                } else {
                    if (d12 > 1.0D) {
                        d12 = 1.0D;
                    }

                    d12 /= 8.0D;
                }

                ++i1;
                double d13 = f1;
                double d14 = f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2) {
                    double d6 = (j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

                    if (d6 < 0.0D) {
                        d6 *= 4.0D;
                    }

                    double d7 = this.field_147428_e[l] / 512.0D;
                    double d8 = this.field_147425_f[l] / 512.0D;
                    double d9 = (this.field_147427_d[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

                    if (j2 > 29) {
                        double d11 = (j2 - 29) / 3.0F;
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }

                    this.field_147434_q[l] = d10;
                    ++l;
                }
            }
        }
    }
}
