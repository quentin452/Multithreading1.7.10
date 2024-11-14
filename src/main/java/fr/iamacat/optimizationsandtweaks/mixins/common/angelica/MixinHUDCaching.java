package fr.iamacat.optimizationsandtweaks.mixins.common.angelica;

import static com.gtnewhorizons.angelica.loading.AngelicaTweaker.LOGGER;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.event.world.WorldEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.angelica.hudcaching.HUDCaching;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mixin(HUDCaching.class)
public class MixinHUDCaching {

    @Shadow
    public static Framebuffer framebuffer;

    /**
     * @author quentin452
     * @reason Fix Invalid framebuffer operation (1286) caused by Hud Caching from angelica mod
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    @Overwrite(remap = false)
    public void onJoinWorld(WorldEvent.Load event) {
        if (event.world.isRemote) {
            LOGGER.info("World loaded - Initializing HUDCaching Fixed (OptimizationsAndTweaks)");
            int framebufferWidth = Minecraft.getMinecraft().displayWidth;
            int framebufferHeight = Minecraft.getMinecraft().displayHeight;
            framebuffer = new Framebuffer(framebufferWidth, framebufferHeight, true);
            framebuffer.setFramebufferColor(0, 0, 0, 0);
        }
    }
}
