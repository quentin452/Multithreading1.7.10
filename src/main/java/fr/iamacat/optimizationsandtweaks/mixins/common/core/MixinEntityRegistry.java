package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.BitSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mixin(EntityRegistry.class)
public class MixinEntityRegistry {

    @Shadow
    private BitSet availableIndicies;

    // todo check if entity ids extender features are enabled from confighelper and endlessids before injecting this
    @Inject(method = "<init>", at = @At("RETURN"))
    private void inializeAvailableIndiciesPatch(CallbackInfo ci) {
        if (FMLCommonHandler.instance()
            .findContainerFor("confighelper") != null) {
            availableIndicies = new BitSet(2147483647);
            availableIndicies.set(1, 2147483646);
        } else if (FMLCommonHandler.instance()
            .findContainerFor("endlessids") != null) {
                availableIndicies = new BitSet(65536);
                availableIndicies.set(1, 65535);
            } else {
                availableIndicies = new BitSet(256);
                availableIndicies.set(1, 255);
            }
    }
}
