package fr.iamacat.optimizationsandtweaks.mixins.common.etfuturumrequiem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.api.DeepslateOreRegistry;
import ganymedes01.etfuturum.api.mappings.RegistryMapping;
import ganymedes01.etfuturum.client.sound.ModSounds;
import ganymedes01.etfuturum.configuration.configs.ConfigBlocksItems;
import ganymedes01.etfuturum.core.utils.Utils;

@Mixin(DeepslateOreRegistry.class)
public class MixinDeepslateOreRegistry {

    @Shadow
    private static final Map<RegistryMapping<Block>, RegistryMapping<Block>> deepslateOres = new HashMap<>();

    /**
     * @author quentin452
     * @reason Fix java.lang.NullPointerException: Initializing game from the init method of DeepslateOreRegistry from
     *         Et Futurum Requiem with "bad" mods installed
     */
    @Overwrite
    public static void init() {
        if (ConfigBlocksItems.enableDeepslateOres) {
            for (Entry<RegistryMapping<Block>, RegistryMapping<Block>> entry : getOreMap().entrySet()) {
                optimizationsAndTweaks$processEntry(entry);
            }
        }
    }

    @Unique
    private static void optimizationsAndTweaks$processEntry(
        Entry<RegistryMapping<Block>, RegistryMapping<Block>> entry) {
        Block oreNorm = optimizationsAndTweaks$getOreNorm(entry);
        Block oreDeep = optimizationsAndTweaks$getOreDeep(entry);
        if (oreNorm == null || oreDeep == null) return; // Return false if there is an error
        optimizationsAndTweaks$setBlockSoundIfNeeded(oreDeep);
        ItemStack stackNorm = optimizationsAndTweaks$getItemStack(
            oreNorm,
            entry.getKey()
                .getMeta());
        ItemStack stackDeep = optimizationsAndTweaks$getItemStack(
            oreDeep,
            entry.getValue()
                .getMeta());
        optimizationsAndTweaks$addSmeltingRecipeIfNeeded(stackNorm, stackDeep);
    }

    @Unique
    private static Block optimizationsAndTweaks$getOreNorm(
        Entry<RegistryMapping<Block>, RegistryMapping<Block>> entry) {
        Block oreNorm = entry.getKey()
            .getObject();
        if (oreNorm == null) {
            System.err.println("[OptimizationsAndTweaks] oreNorm is null for entry: {}" + entry);
        }
        return oreNorm;
    }

    @Unique
    private static Block optimizationsAndTweaks$getOreDeep(
        Entry<RegistryMapping<Block>, RegistryMapping<Block>> entry) {
        Block oreDeep = entry.getValue()
            .getObject();
        if (oreDeep == null) {
            System.err.println("[OptimizationsAndTweaks] oreDeep is null for entry: {}" + entry);
        }
        return oreDeep;
    }

    @Unique
    private static void optimizationsAndTweaks$setBlockSoundIfNeeded(Block oreDeep) {
        boolean saltyModOre = oreDeep.getClass()
            .getName()
            .toLowerCase()
            .contains("saltymod");
        if (oreDeep.stepSound == Block.soundTypeStone || saltyModOre) {
            Utils.setBlockSound(oreDeep, ModSounds.soundDeepslate);
        }
    }

    @Unique
    private static ItemStack optimizationsAndTweaks$getItemStack(Block block, int meta) {
        if (block == null) {
            System.err.println("[OptimizationsAndTweaks] Cannot create ItemStack, block is null");
            return null;
        }
        return new ItemStack(block, 1, meta);
    }

    @Unique
    private static boolean optimizationsAndTweaks$addSmeltingRecipeIfNeeded(ItemStack stackNorm, ItemStack stackDeep) {
        if (stackNorm == null) {
            System.err.println("[OptimizationsAndTweaks] Cannot add smelting recipe: stackNorm is null.");
            return false;
        }
        if (stackDeep == null) {
            System.err.println("[OptimizationsAndTweaks] Cannot add smelting recipe: stackDeep is null.");
            return false;
        }
        try {
            ItemStack smeltingResult = FurnaceRecipes.smelting()
                .getSmeltingResult(stackNorm);
            if (smeltingResult != null) {
                GameRegistry.addSmelting(
                    stackDeep,
                    smeltingResult,
                    FurnaceRecipes.smelting()
                        .func_151398_b(stackNorm));
            } else {
                System.err.println("[OptimizationsAndTweaks] No smelting result found for stackNorm: " + stackNorm);
                return false;
            }
        } catch (Exception e) {
            System.err
                .println("[OptimizationsAndTweaks] Exception occurred while adding smelting recipe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Shadow
    public static Map<RegistryMapping<Block>, RegistryMapping<Block>> getOreMap() {
        return deepslateOres;
    }
}
