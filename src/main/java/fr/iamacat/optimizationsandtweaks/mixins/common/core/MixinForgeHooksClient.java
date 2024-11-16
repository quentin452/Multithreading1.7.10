package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgeHooksClient.class)
public class MixinForgeHooksClient {
    @Shadow
    private static int skyX, skyZ;
    @Shadow
    private static boolean skyInit;
    @Shadow
    private static int skyRGBMultiplier;
    /**
     * @author quentin452
     * @reason fix null crashes from getSkyBlendColour from ForgeHooksClient
     */
    @Overwrite
    public static int getSkyBlendColour(World world, int playerX, int playerY, int playerZ) {
        if (world == null) {
            System.err.println("[OptimizationsAndTweaks] World is null");
            return 0;
        }

        if (playerX == skyX && playerZ == skyZ && skyInit) {
            return skyRGBMultiplier;
        }
        skyInit = true;

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) {
            System.err.println("[OptimizationsAndTweaks] Minecraft instance is null");
            return 0;
        }

        GameSettings settings = minecraft.gameSettings;
        if (settings == null) {
            System.err.println("[OptimizationsAndTweaks] GameSettings is null");
            return 0;
        }

        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length) {
            distance = ranges[settings.renderDistanceChunks];
        }

        int r = 0;
        int g = 0;
        int b = 0;

        int divider = 0;
        for (int x = -distance; x <= distance; ++x) {
            for (int z = -distance; z <= distance; ++z) {
                BiomeGenBase biome = world.getBiomeGenForCoords(playerX + x, playerZ + z);
                if (biome == null) {
                    System.err.println("[OptimizationsAndTweaks] Biome is null at coordinates: " + (playerX + x) + ", " + (playerZ + z));
                    continue;
                }

                Float temperature = biome.getFloatTemperature(playerX + x, playerY, playerZ + z);
                if (temperature == null) {
                    System.err.println("[OptimizationsAndTweaks] Temperature is null for biome at coordinates: " + (playerX + x) + ", " + (playerZ + z));
                    continue;
                }

                int colour = biome.getSkyColorByTemp(temperature);
                r += (colour & 0xFF0000) >> 16;
                g += (colour & 0x00FF00) >> 8;
                b += colour & 0x0000FF;
                divider++;
            }
        }

        if (divider == 0) {
            System.err.println("[OptimizationsAndTweaks] Divider is zero, returning default sky color");
            return 0;
        }

        int multiplier = (r / divider & 255) << 16 | (g / divider & 255) << 8 | b / divider & 255;

        skyX = playerX;
        skyZ = playerZ;
        skyRGBMultiplier = multiplier;
        return skyRGBMultiplier;
    }

}
