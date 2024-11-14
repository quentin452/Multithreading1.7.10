package fr.iamacat.optimizationsandtweaks.mixins.common.unuquadium;

import static fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig.LandDimensionID;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.mcreator.mcreator_ununquadiumLand;

@Mixin(mcreator_ununquadiumLand.class)
public class Mixinmcreator_ununquadiumLand {

    @Shadow
    public Object instance;
    @Unique
    private static int optimizationsAndTweaks$DIMID = LandDimensionID;
    @Shadow
    public static mcreator_ununquadiumLand.BlockTutorialPortal portal = ((mcreator_ununquadiumLand.BlockTutorialPortal) (new mcreator_ununquadiumLand.BlockTutorialPortal())
        .setBlockName("ununquadiumLand_portal")
        .setBlockTextureName("ununquadium ore"));
    @Shadow
    public static mcreator_ununquadiumLand.ModTrigger block;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void load() {
        if (!blockRegistered()) {
            GameRegistry.registerBlock(portal, "ununquadiumLand_portal");
        }

        DimensionManager
            .registerProviderType(optimizationsAndTweaks$DIMID, mcreator_ununquadiumLand.WorldProviderMod.class, false);
        DimensionManager.registerDimension(optimizationsAndTweaks$DIMID, optimizationsAndTweaks$DIMID);

        GameRegistry.addRecipe(
            new ItemStack(block, 1),
            "X1X",
            "X4X",
            "X7X",
            '1',
            new ItemStack(Blocks.wool, 1, 0),
            '4',
            new ItemStack(Blocks.obsidian, 1),
            '7',
            new ItemStack(Blocks.obsidian, 1));
    }

    private boolean blockRegistered() {
        return GameRegistry.findBlock("TestEnvironmentMod", "ununquadiumLand_portal") != null;
    }
}
