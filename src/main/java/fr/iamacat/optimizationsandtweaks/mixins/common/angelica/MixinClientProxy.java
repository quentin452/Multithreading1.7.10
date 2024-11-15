package fr.iamacat.optimizationsandtweaks.mixins.common.angelica;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.angelica.glsm.debug.OpenGLDebugging;
import com.gtnewhorizons.angelica.proxy.ClientProxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

@Mixin(ClientProxy.class)
public class MixinClientProxy {

    @Shadow
    private static KeyBinding glsmKeyBinding;
    @Shadow
    private boolean wasGLSMKeyPressed;

    /**
     * @author quentin452
     * @reason Fix "java.lang.NullPointerException: Unexpected error" caused by onKeypress from the ClientProxy class of
     *         the Angelica Mod.
     *         This issue appears when the Dynamic Light mod and Falsetweaks are installed, and the Falsetweaks message
     *         suggests to "Remove Dynamic Light mod because Falsetweaks already has one built in."
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onKeypress(TickEvent.ClientTickEvent event) {
        if (glsmKeyBinding != null) {
            boolean isPressed = glsmKeyBinding.getKeyCode() != 0 && Keyboard.isKeyDown(glsmKeyBinding.getKeyCode());
            if (isPressed && !this.wasGLSMKeyPressed) {
                OpenGLDebugging.checkGLSM();
            }

            this.wasGLSMKeyPressed = isPressed;
        }
    }
}
