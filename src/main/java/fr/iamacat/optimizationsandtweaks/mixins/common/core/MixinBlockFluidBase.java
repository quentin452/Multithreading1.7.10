package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockFluidBase.class)
public class MixinBlockFluidBase {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static double getFlowDirection(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockFluidBase) {
            Vec3 vec = ((BlockFluidBase) block).getFlowVector(world, x, y, z);
            return vec.xCoord == 0.0D && vec.zCoord == 0.0D ? -1000.0D
                : Math.atan2(vec.zCoord, vec.xCoord) - Math.PI / 2D;
        } else {
            return 0;
        }
    }
}
