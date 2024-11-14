package fr.iamacat.optimizationsandtweaks.mixins.common.angelica;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.gtnewhorizons.angelica.glsm.GLDebug;

@Mixin(GLDebug.class)
public class MixinGLDebug {

    /**
     * @author quentin452
     * @reason Fix Stack overflow crash when angelica is loaded by disabling some methods from GLDebug class from
     *         angelica
     */
    @Overwrite
    public static void pushGroup(String group) {}

    /**
     * @author quentin452
     * @reason Fix Stack overflow crash when angelica is loaded by disabling some methods from GLDebug class from
     *         angelica
     */
    @Overwrite
    public static void popGroup() {}
}
