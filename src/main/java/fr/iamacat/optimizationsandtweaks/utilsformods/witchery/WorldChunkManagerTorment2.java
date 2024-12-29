package fr.iamacat.optimizationsandtweaks.utilsformods.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.GenerateMaze;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.List;

public class WorldChunkManagerTorment2 implements IChunkProvider {
    public static final int NUM_LEVELS = 6;
    public static final int BASE_LEVEL = 10;
    public static final int LEVEL_HEIGHT = 15;
    public static final int MAZE_SIZE = 31;
    private final World world;

    public WorldChunkManagerTorment2(World world) {
        this.world = world;
    }

    @Override
    public boolean chunkExists(int i, int j) {
        return true;
    }

    @Override // FIX ENDLESSIDS COMPAT
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = new Chunk(this.world, x, z);

        for (int y = 0; y < 256; ++y) {
            int l = y >> 4;
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];
            if (extendedblockstorage == null) {
                extendedblockstorage = new ExtendedBlockStorage(y, !this.world.provider.hasNoSky);
                chunk.getBlockStorageArray()[l] = extendedblockstorage;
            }

            for (int _x = 0; _x < 16; ++_x) {
                for (int _z = 0; _z < 16; ++_z) {
                    Block blockId = Blocks.air;
                    extendedblockstorage.func_150818_a(_x, y & 15, _z, blockId);
                    extendedblockstorage.setExtBlockMetadata(_x, y & 15, _z, 0);
                }
            }
        }

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int biomeID = world.getWorldChunkManager().getBiomeGenAt((x << 4) + i, (z << 4) + j).biomeID;
                BiomeGenBase[] biomesForGeneration = new BiomeGenBase[256];
                biomesForGeneration[(j << 4) | i] = BiomeGenBase.getBiome(biomeID);
            }
        }

        return chunk;
    }
    @Override
    public Chunk loadChunk(int x, int z) {
        return this.provideChunk(x, z);
    }
    @Override
    public void populate(IChunkProvider ichunkprovider, int i, int j) {
        if (i == 0 && j == 0) {
            for(int slot = 0; slot < 6; ++slot) {
                GenerateMaze maze = new GenerateMaze(31, 31, this.world.rand);
                maze.display(this.world, i * 16 + 8 - 31, 10 + slot * 15, j * 16 + 8 - 2, Witchery.Blocks.FORCE, Witchery.Blocks.TORMENT_STONE);
            }
        }

    }
    @Override
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return true;
    }
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    @Override
    public boolean canSave() {
        return true;
    }
    @Override
    public String makeString() {
        return "TormentChunk";
    }
    @Override
    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        return null;
    }
    @Override
    public ChunkPosition func_147416_a(World world, String s, int i, int j, int k) {
        return null;
    }
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    @Override
    public void recreateStructures(int i, int j) {
    }
    @Override
    public void saveExtraData() {
    }
}
