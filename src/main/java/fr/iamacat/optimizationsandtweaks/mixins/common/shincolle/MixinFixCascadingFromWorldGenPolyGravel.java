package fr.iamacat.optimizationsandtweaks.mixins.common.shincolle;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;

import com.lulan.shincolle.worldgen.WorldGenPolyGravel;

@Mixin(WorldGenPolyGravel.class)
public abstract class MixinFixCascadingFromWorldGenPolyGravel extends WorldGenerator {

    /**
     * @author iamacatfr
     * @reason disabling Poly Gravel because its useless + making cascading lags
     */
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        return false;
    }
}
