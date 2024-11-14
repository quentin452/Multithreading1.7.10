package fr.iamacat.optimizationsandtweaks.mixins.common.angelica;

import com.gtnewhorizons.angelica.hudcaching.HUDCaching;
import com.gtnewhorizons.angelica.loading.AngelicaTweaker;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.gtnewhorizons.angelica.loading.AngelicaTweaker.LOGGER;

@Mixin(HUDCaching.class)
public class MixinHUDCaching {
    @Shadow
    public static Framebuffer framebuffer;
    /**
     * @author
     * @reason
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
