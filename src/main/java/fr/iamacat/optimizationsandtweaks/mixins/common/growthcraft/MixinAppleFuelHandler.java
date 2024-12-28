package fr.iamacat.optimizationsandtweaks.mixins.common.growthcraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.handler.AppleFuelHandler;

@Mixin(AppleFuelHandler.class)
public class MixinAppleFuelHandler {

    /**
     * @author iamacatfr
     * @reason add null check
     */
    @Overwrite(remap = false)
    public int getBurnTime(ItemStack fuel) {
        if (fuel != null) {
            Item item = fuel.getItem();
            if (item != null && GrowthCraftApples.blocks.appleSapling != null
                && GrowthCraftApples.blocks.appleSapling.getItem() != null
                && GrowthCraftApples.blocks.appleSapling.getItem().equals(item)) {
                return 100;
            }
        }
        return 0;
    }
}
