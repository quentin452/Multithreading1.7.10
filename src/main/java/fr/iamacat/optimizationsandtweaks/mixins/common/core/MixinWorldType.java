package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import fr.iamacat.optimizationsandtweaks.worldgen.ChunkProviderGenerateTwo;

@Mixin(WorldType.class)
public class MixinWorldType {

    /**
     * @author
     * @reason use ChunkProviderGenerateTwo instead of ChunkProviderGenerate
     */
    @Overwrite(remap = false)
    public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
        return (this.equals(WorldType.FLAT) ? new ChunkProviderFlat(
            world,
            world.getSeed(),
            world.getWorldInfo()
                .isMapFeaturesEnabled(),
            generatorOptions)
            : new ChunkProviderGenerateTwo(
                world,
                world.getSeed(),
                world.getWorldInfo()
                    .isMapFeaturesEnabled()));
    }
}
