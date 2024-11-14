package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import static fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.BlockGrass2.optimizationsAndTweaks$spreadGrass;

import java.util.Random;

import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGrass.class)
public class MixinBlockGrass {

    private static final int LIGHT_THRESHOLD_GRASS_TO_DIRT = 4;
    private static final int LIGHT_THRESHOLD_SPREAD_GRASS = 9;

    @Inject(method = "updateTick", at = @At("HEAD"), cancellable = true)
    public void updateTick(World worldIn, int x, int y, int z, Random random, CallbackInfo ci) {
        if (worldIn.isRemote || !worldIn.blockExists(x, y + 1, z)) {
            return;
        }

        if (worldIn.getBlockLightValue(x, y + 1, z) < LIGHT_THRESHOLD_GRASS_TO_DIRT
            && worldIn.getBlockLightOpacity(x, y + 1, z) > 2) {
            worldIn.setBlock(x, y, z, Blocks.dirt);
        } else if (worldIn.getBlockLightValue(x, y + 1, z) >= LIGHT_THRESHOLD_SPREAD_GRASS) {
            optimizationsAndTweaks$spreadGrass(worldIn, x, y, z, random);
        }
        ci.cancel();
    }
}
