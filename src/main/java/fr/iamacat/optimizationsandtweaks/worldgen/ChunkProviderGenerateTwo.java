package fr.iamacat.optimizationsandtweaks.worldgen;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import cpw.mods.fml.common.eventhandler.Event.Result;
import fr.iamacat.optimizationsandtweaks.noise.NoiseGeneratorOctavesTwo;
import fr.iamacat.optimizationsandtweaks.noise.NoiseGeneratorPerlinTwo;

public class ChunkProviderGenerateTwo implements IChunkProvider {

    // ATTENTION IT BREAK VANILLA SEED PARITY
    /** RNG. */
    private final Random rand;
    private NoiseGeneratorOctavesTwo field_147431_j;
    private NoiseGeneratorOctavesTwo field_147432_k;
    private NoiseGeneratorOctavesTwo field_147429_l;
    private NoiseGeneratorPerlinTwo field_147430_m;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctavesTwo noiseGen5;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctavesTwo noiseGen6;
    public NoiseGeneratorOctavesTwo mobSpawnerNoise;
    /** Reference to the World object. */
    private final World worldObj;
    /** are map structures going to be generated (e.g., strongholds) */
    private final boolean mapFeaturesEnabled;
    private final WorldType field_147435_p;
    private final double[] field_147434_q;
    private final float[] parabolicField;
    private double[] stoneNoise = new double[256];
    private MapGenBase caveGenerator = new MapGenCaves();
    /** Holds Stronghold Generator */
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    /** Holds Village Generator */
    private MapGenVillage villageGenerator = new MapGenVillage();
    /** Holds Mineshaft Generator */
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    /** Holds ravine generator */
    private MapGenBase ravineGenerator = new MapGenRavine();
    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;
    double[] field_147427_d;
    double[] field_147428_e;
    double[] field_147425_f;
    double[] field_147426_g;
    private final ConcurrentHashMap<Integer, Block[]> terrainBlocksMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, byte[]> biomeDataMap = new ConcurrentHashMap<>();

    {
        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
        strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
        villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
        scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen
            .getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
    }

    public ChunkProviderGenerateTwo(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
        this.worldObj = p_i2006_1_;
        this.mapFeaturesEnabled = p_i2006_4_;
        this.field_147435_p = p_i2006_1_.getWorldInfo()
            .getTerrainType();
        this.rand = new Random(p_i2006_2_);
        this.field_147431_j = new NoiseGeneratorOctavesTwo(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctavesTwo(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctavesTwo(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlinTwo(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctavesTwo(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctavesTwo(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctavesTwo(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.sqrt_float((j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }

        NoiseGenerator[] noiseGens = { field_147431_j, field_147432_k, field_147429_l, field_147430_m, noiseGen5,
            noiseGen6, mobSpawnerNoise };
        noiseGens = TerrainGen.getModdedNoiseGenerators(p_i2006_1_, this.rand, noiseGens);
        this.field_147431_j = (NoiseGeneratorOctavesTwo) noiseGens[0];
        this.field_147432_k = (NoiseGeneratorOctavesTwo) noiseGens[1];
        this.field_147429_l = (NoiseGeneratorOctavesTwo) noiseGens[2];
        this.field_147430_m = (NoiseGeneratorPerlinTwo) noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctavesTwo) noiseGens[4];
        this.noiseGen6 = (NoiseGeneratorOctavesTwo) noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctavesTwo) noiseGens[6];
    }

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

    public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_,
        BiomeGenBase[] p_147422_5_) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = createReplaceBiomeBlocksEvent(
            p_147422_1_,
            p_147422_2_,
            p_147422_3_,
            p_147422_4_,
            p_147422_5_);
        handleReplaceBiomeBlocksEvent(event);

        if (event.getResult() == Result.DENY) {
            return;
        }

        double d0 = 0.03125D;
        generateStoneNoise(p_147422_1_, p_147422_2_, d0);

        generateTerrainBlocks(p_147422_1_, p_147422_2_, p_147422_3_, p_147422_4_, p_147422_5_);
    }

    private ChunkProviderEvent.ReplaceBiomeBlocks createReplaceBiomeBlocksEvent(int p_147422_1_, int p_147422_2_,
        Block[] p_147422_3_, byte[] p_147422_4_, BiomeGenBase[] p_147422_5_) {
        return new ChunkProviderEvent.ReplaceBiomeBlocks(
            this,
            p_147422_1_,
            p_147422_2_,
            p_147422_3_,
            p_147422_4_,
            p_147422_5_,
            this.worldObj);
    }

    private void handleReplaceBiomeBlocksEvent(ChunkProviderEvent.ReplaceBiomeBlocks event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    private void generateStoneNoise(int p_147422_1_, int p_147422_2_, double d0) {
        this.stoneNoise = this.field_147430_m
            .func_151599_a(this.stoneNoise, p_147422_1_ * 16, p_147422_2_ * 16, 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
    }

    private void generateTerrainBlocks(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_,
        BiomeGenBase[] p_147422_5_) {
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

    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.rand.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(p_73154_1_, p_73154_2_, ablock);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager()
            .loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        this.replaceBlocksForBiome(p_73154_1_, p_73154_2_, ablock, abyte, this.biomesForGeneration);
        this.caveGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
        this.ravineGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);

        if (this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
            this.villageGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
            this.strongholdGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, ablock);
        }

        Chunk chunk = new Chunk(this.worldObj, ablock, abyte, p_73154_1_, p_73154_2_);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

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
        this.field_147425_f = this.field_147432_k.generateNoiseOctaves(
            this.field_147425_f,
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
                double d12 = getD12(i1);

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

    private double getD12(int i1) {
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
        return d12;
    }

    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    private final Random random = new Random();

    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BiomeGenBase biomegenbase = getBiomeForChunk(p_73153_2_, p_73153_3_);
        random.setSeed(this.worldObj.getSeed());
        long i1 = random.nextLong() / 2L * 2L + 1L;
        long j1 = random.nextLong() / 2L * 2L + 1L;
        random.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ this.worldObj.getSeed());
        boolean flag = false;

        MinecraftForge.EVENT_BUS
            .post(new PopulateChunkEvent.Pre(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag));

        if (this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.random, p_73153_2_, p_73153_3_);
            flag = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.random, p_73153_2_, p_73153_3_);
            this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.random, p_73153_2_, p_73153_3_);
            this.scatteredFeatureGenerator
                .generateStructuresInChunk(this.worldObj, this.random, p_73153_2_, p_73153_3_);
        }

        int k1;
        int l1;
        int i2;

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills
            && !flag
            && this.random.nextInt(4) == 0
            && TerrainGen.populate(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag, LAKE)) {
            k1 = k + this.random.nextInt(16) + 8;
            l1 = this.random.nextInt(256);
            i2 = l + this.random.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.random, k1, l1, i2);
        }

        if (TerrainGen.populate(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag, LAVA) && !flag
            && this.random.nextInt(8) == 0) {
            k1 = k + this.random.nextInt(16) + 8;
            l1 = this.random.nextInt(this.random.nextInt(248) + 8);
            i2 = l + this.random.nextInt(16) + 8;

            if (l1 < 63 || this.random.nextInt(10) == 0) {
                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.random, k1, l1, i2);
            }
        }

        boolean doGen = TerrainGen.populate(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag, DUNGEON);
        for (k1 = 0; doGen && k1 < 8; ++k1) {
            l1 = k + this.random.nextInt(16) + 8;
            i2 = this.random.nextInt(256);
            int j2 = l + this.random.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.worldObj, this.random, l1, i2, j2);
        }

        biomegenbase.decorate(this.worldObj, this.random, k, l);
        if (TerrainGen.populate(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag, ANIMALS)) {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.random);
        }
        k += 8;
        l += 8;

        doGen = TerrainGen.populate(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                i2 = this.worldObj.getPrecipitationHeight(k + k1, l + l1);

                if (this.worldObj.isBlockFreezable(k1 + k, i2 - 1, l1 + l)) {
                    this.worldObj.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(k1 + k, i2, l1 + l, true)) {
                    this.worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS
            .post(new PopulateChunkEvent.Post(p_73153_1_, worldObj, random, p_73153_2_, p_73153_3_, flag));

        BlockFalling.fallInstantly = false;
    }

    private BiomeGenBase getBiomeForChunk(int p_73153_2_, int p_73153_3_) {
        int biomeId = this.worldObj.getBiomeGenForCoords(p_73153_2_ * 16 + 8, p_73153_3_ * 16 + 8).biomeID;
        return BiomeGenBase.getBiome(biomeId);
    }

    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    public void saveExtraData() {}

    public boolean unloadQueuedChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public String makeString() {
        return "RandomLevelSource";
    }

    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        if (p_73155_1_ == EnumCreatureType.monster
            && this.scatteredFeatureGenerator.func_143030_a(p_73155_2_, p_73155_3_, p_73155_4_)) {
            return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
        } else {
            return biomegenbase.getSpawnableList(p_73155_1_);
        }
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
        int p_147416_5_) {
        return "Stronghold".equals(p_147416_2_) && this.strongholdGenerator != null
            ? this.strongholdGenerator.func_151545_a(p_147416_1_, p_147416_3_, p_147416_4_, p_147416_5_)
            : null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
        if (this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, p_82695_1_, p_82695_2_, null);
            this.villageGenerator.func_151539_a(this, this.worldObj, p_82695_1_, p_82695_2_, null);
            this.strongholdGenerator.func_151539_a(this, this.worldObj, p_82695_1_, p_82695_2_, null);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, p_82695_1_, p_82695_2_, null);
        }
    }
}
