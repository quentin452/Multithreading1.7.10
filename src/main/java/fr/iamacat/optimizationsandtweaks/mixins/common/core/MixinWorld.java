package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import fr.iamacat.optimizationsandtweaks.utilsformods.entity.pathfinding.PathFinder2;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import fr.iamacat.optimizationsandtweaks.eventshandler.TidyChunkBackportEventHandler;

@Mixin(value = World.class, priority = 999)
public abstract class MixinWorld {
    @Shadow
    public boolean isRemote;

    @Shadow
    public final WorldProvider provider;

    @Shadow
    protected IChunkProvider chunkProvider;

    @Shadow
    public final Profiler theProfiler;

    @Inject(method = "tick", at = @At(value = "INVOKE"))
    private void onTickInject(CallbackInfo info) {
        if (OptimizationsandTweaksConfig.enableTidyChunkBackport) {
            TidyChunkBackportEventHandler.injectInWorldTick((World) (Object) this);
        }
    }

    public MixinWorld(WorldProvider provider, Profiler theProfiler) {
        this.provider = provider;
        this.theProfiler = theProfiler;
    }

    @Overwrite // FIX java.lang.NoClassDefFoundError: net.minecraft.pathfinding.PathFinder
    public PathEntity getPathEntityToEntity(Entity p_72865_1_, Entity p_72865_2_, float p_72865_3_, boolean p_72865_4_, boolean p_72865_5_, boolean p_72865_6_, boolean p_72865_7_)
    {
        this.theProfiler.startSection("pathfind");
        int i = MathHelper.floor_double(p_72865_1_.posX);
        int j = MathHelper.floor_double(p_72865_1_.posY + 1.0D);
        int k = MathHelper.floor_double(p_72865_1_.posZ);
        int l = (int)(p_72865_3_ + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache((World) (Object) this, i1, j1, k1, l1, i2, j2, 0);
        PathEntity pathentity = (new PathFinder2(chunkcache, p_72865_4_, p_72865_5_, p_72865_6_, p_72865_7_)).createEntityPathTo(p_72865_1_, p_72865_2_, p_72865_3_);
        this.theProfiler.endSection();
        return pathentity;
    }

    @Overwrite // FIX java.lang.NoClassDefFoundError: net.minecraft.pathfinding.PathFinder
    public PathEntity getEntityPathToXYZ(Entity p_72844_1_, int p_72844_2_, int p_72844_3_, int p_72844_4_, float p_72844_5_, boolean p_72844_6_, boolean p_72844_7_, boolean p_72844_8_, boolean p_72844_9_)
    {
        this.theProfiler.startSection("pathfind");
        int l = MathHelper.floor_double(p_72844_1_.posX);
        int i1 = MathHelper.floor_double(p_72844_1_.posY);
        int j1 = MathHelper.floor_double(p_72844_1_.posZ);
        int k1 = (int)(p_72844_5_ + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        ChunkCache chunkcache = new ChunkCache((World) (Object) this, l1, i2, j2, k2, l2, i3, 0);
        PathEntity pathentity = (new PathFinder2(chunkcache, p_72844_6_, p_72844_7_, p_72844_8_, p_72844_9_)).createEntityPathTo(p_72844_1_, p_72844_2_, p_72844_3_, p_72844_4_, p_72844_5_);
        this.theProfiler.endSection();
        return pathentity;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
        if (p_147439_1_ >= -30000000 && p_147439_3_ >= -30000000
            && p_147439_1_ < 30000000
            && p_147439_3_ < 30000000
            && p_147439_2_ >= 0
            && p_147439_2_ < 256
            && this.blockExists(p_147439_1_, p_147439_2_, p_147439_3_)) {
            Chunk chunk = this.getChunkFromChunkCoords(p_147439_1_ >> 4, p_147439_3_ >> 4);
            if (chunk != null) {
                return chunk.getBlock(p_147439_1_ & 15, p_147439_2_, p_147439_3_ & 15);
            }
        }
        return Blocks.air;
    }
    @Shadow
    public boolean blockExists(int p_72899_1_, int p_72899_2_, int p_72899_3_)
    {
        return p_72899_2_ >= 0 && p_72899_2_ < 256 ? this.chunkExists(p_72899_1_ >> 4, p_72899_3_ >> 4) : false;
    }
    @Shadow
    public Chunk getChunkFromChunkCoords(int p_72964_1_, int p_72964_2_)
    {
        return this.chunkProvider.provideChunk(p_72964_1_, p_72964_2_);
    }
    @Shadow
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_)
    {
        return this.chunkProvider.chunkExists(p_72916_1_, p_72916_2_);
    }
}
