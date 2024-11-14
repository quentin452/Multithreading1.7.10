package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import static net.minecraft.world.biome.BiomeGenBase.*;

import java.util.*;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mixin(WorldChunkManager.class)
public class MixinWorldChunkManager {

    @Shadow
    public static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(
        Arrays.asList(forest, plains, taiga, taigaHills, forestHills, jungle, jungleHills));
    @Shadow
    private GenLayer genBiomes;
    /** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
    @Shadow
    private GenLayer biomeIndexLayer;
    /** The BiomeCache object for this world. */
    @Shadow
    private BiomeCache biomeCache;
    /** A list of biomes that the player can spawn in. */
    @Shadow
    private List biomesToSpawnIn;

    protected MixinWorldChunkManager() {
        this.biomeCache = new BiomeCache((WorldChunkManager) (Object) this);
        this.biomesToSpawnIn = new ArrayList();
        this.biomesToSpawnIn.addAll(allowedBiomes);
    }

    public MixinWorldChunkManager(long p_i1975_1_, WorldType p_i1975_3_) {
        this();
        GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(p_i1975_1_, p_i1975_3_);
        agenlayer = getModdedBiomeGenerators(p_i1975_3_, p_i1975_1_, agenlayer);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }

    public MixinWorldChunkManager(World p_i1976_1_) {
        this(
            p_i1976_1_.getSeed(),
            p_i1976_1_.getWorldInfo()
                .getTerrainType());
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    @Shadow
    public List getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    @Shadow
    public BiomeGenBase getBiomeGenAt(int p_76935_1_, int p_76935_2_) {
        return this.biomeCache.getBiomeGenAt(p_76935_1_, p_76935_2_);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    @Overwrite
    public synchronized float[] getRainfall(float[] p_76936_1_, int p_76936_2_, int p_76936_3_, int p_76936_4_,
        int p_76936_5_) {
        IntCache.resetIntCache();

        if (p_76936_1_ == null || p_76936_1_.length < p_76936_4_ * p_76936_5_) {
            p_76936_1_ = new float[p_76936_4_ * p_76936_5_];
        }

        int[] aint = this.biomeIndexLayer.getInts(p_76936_2_, p_76936_3_, p_76936_4_, p_76936_5_);

        for (int i1 = 0; i1 < p_76936_4_ * p_76936_5_; ++i1) {
            try {
                float f = (float) BiomeGenBase.getBiome(aint[i1])
                    .getIntRainfall() / 65536.0F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                p_76936_1_[i1] = f;
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("biome id", i1);
                crashreportcategory.addCrashSection("downfalls[] size", p_76936_1_.length);
                crashreportcategory.addCrashSection("x", p_76936_2_);
                crashreportcategory.addCrashSection("z", p_76936_3_);
                crashreportcategory.addCrashSection("w", p_76936_4_);
                crashreportcategory.addCrashSection("h", p_76936_5_);
                throw new ReportedException(crashreport);
            }
        }

        return p_76936_1_;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    @Shadow
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_) {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    @Overwrite
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, int p_76937_2_, int p_76937_3_,
        int p_76937_4_, int p_76937_5_) {
        IntCache.resetIntCache();

        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_) {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }

        int[] aint = this.genBiomes.getInts(p_76937_2_, p_76937_3_, p_76937_4_, p_76937_5_);

        try {
            for (int i1 = 0; i1 < p_76937_4_ * p_76937_5_; ++i1) {
                p_76937_1_[i1] = BiomeGenBase.getBiome(aint[i1]);
            }

            return p_76937_1_;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", p_76937_1_.length);
            crashreportcategory.addCrashSection("x", p_76937_2_);
            crashreportcategory.addCrashSection("z", p_76937_3_);
            crashreportcategory.addCrashSection("w", p_76937_4_);
            crashreportcategory.addCrashSection("h", p_76937_5_);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    @Overwrite
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] p_76933_1_, int p_76933_2_, int p_76933_3_,
        int p_76933_4_, int p_76933_5_) {
        return this.getBiomeGenAt(p_76933_1_, p_76933_2_, p_76933_3_, p_76933_4_, p_76933_5_, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    @Overwrite
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] p_76931_1_, int p_76931_2_, int p_76931_3_, int p_76931_4_,
        int p_76931_5_, boolean p_76931_6_) {
        IntCache.resetIntCache();

        if (p_76931_1_ == null || p_76931_1_.length < p_76931_4_ * p_76931_5_) {
            p_76931_1_ = new BiomeGenBase[p_76931_4_ * p_76931_5_];
        }

        if (p_76931_6_ && p_76931_4_ == 16 && p_76931_5_ == 16 && (p_76931_2_ & 15) == 0 && (p_76931_3_ & 15) == 0) {
            BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(p_76931_2_, p_76931_3_);
            System.arraycopy(abiomegenbase1, 0, p_76931_1_, 0, p_76931_4_ * p_76931_5_);
            return p_76931_1_;
        } else {
            int[] aint = this.biomeIndexLayer.getInts(p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_);

            for (int i1 = 0; i1 < p_76931_4_ * p_76931_5_; ++i1) {
                p_76931_1_[i1] = BiomeGenBase.getBiome(aint[i1]);
            }

            return p_76931_1_;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    @Overwrite
    public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List p_76940_4_) {
        IntCache.resetIntCache();
        int l = p_76940_1_ - p_76940_3_ >> 2;
        int i1 = p_76940_2_ - p_76940_3_ >> 2;
        int j1 = p_76940_1_ + p_76940_3_ >> 2;
        int k1 = p_76940_2_ + p_76940_3_ >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        try {
            for (int j2 = 0; j2 < l1 * i2; ++j2) {
                BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[j2]);

                if (!p_76940_4_.contains(biomegenbase)) {
                    return false;
                }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", p_76940_1_);
            crashreportcategory.addCrashSection("z", p_76940_2_);
            crashreportcategory.addCrashSection("radius", p_76940_3_);
            crashreportcategory.addCrashSection("allowed", p_76940_4_);
            throw new ReportedException(crashreport);
        }
    }

    @Overwrite
    public ChunkPosition findBiomePosition(int p_150795_1_, int p_150795_2_, int p_150795_3_, List p_150795_4_,
        Random p_150795_5_) {
        IntCache.resetIntCache();
        int l = p_150795_1_ - p_150795_3_ >> 2;
        int i1 = p_150795_2_ - p_150795_3_ >> 2;
        int j1 = p_150795_1_ + p_150795_3_ >> 2;
        int k1 = p_150795_2_ + p_150795_3_ >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        ChunkPosition chunkposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2) {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);

            if (p_150795_4_.contains(biomegenbase) && (chunkposition == null || p_150795_5_.nextInt(j2 + 1) == 0)) {
                chunkposition = new ChunkPosition(l2, 0, i3);
                ++j2;
            }
        }

        return chunkposition;
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    @Shadow
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    @Overwrite(remap = false)
    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }
}
