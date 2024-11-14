package fr.iamacat.optimizationsandtweaks.mixins.common.cofhcore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cofh.asmhooks.block.BlockTickingWater;

@Mixin(BlockTickingWater.class)
public class MixinBlockTickingWater extends BlockDynamicLiquid {

    protected MixinBlockTickingWater(Material p_i45403_1_) {
        super(p_i45403_1_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        if (var1.getBlock(var2, var3, var4) != Blocks.water) {
            super.onBlockAdded(var1, var2, var3, var4);
            if (this.blockMaterial == Material.water && (var1.provider.isHellWorld)
                && (var1.getBlock(var2, var3, var4) != Blocks.air)) {
                var1.setBlock(var2, var3, var4, Blocks.air, 0, 2);
                var1.playAuxSFX(1004, var2, var3, var4, 0);
                var1.playAuxSFX(2000, var2, var3, var4, 4);
            }
        }
    }

    @Shadow
    public boolean isAssociatedBlock(Block var1) {
        return super.isAssociatedBlock(var1) || var1 == Blocks.water;
    }
}
