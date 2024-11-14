package fr.iamacat.optimizationsandtweaks;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import fr.iamacat.optimizationsandtweaks.eventshandler.EntityItemSpawningEventHandler;
import fr.iamacat.optimizationsandtweaks.eventshandler.TidyChunkBackportEventHandler;
import fr.iamacat.optimizationsandtweaks.proxy.CommonProxy;
import fr.iamacat.optimizationsandtweaks.utilsformods.experienceore.ExperienceOreConfig;
import fr.iamacat.optimizationsandtweaks.utilsformods.mythandmonsters.recurrentcomplextrewrite.FileInjector;
import fr.iamacat.optimizationsandtweaks.utilsformods.mythandmonsters.recurrentcomplextrewrite.ModConfig;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    acceptedMinecraftVersions = Tags.MCVERSION,
    dependencies = "required-after:falsepatternlib;required-after:unimixins")
public class OptimizationsAndTweaks {

    @Mod.Instance(Tags.MODID)
    public static OptimizationsAndTweaks instance;
    @SidedProxy(clientSide = Tags.CLIENTPROXY, serverSide = Tags.SERVERPROXY)
    public static CommonProxy proxy;
    public static Configuration config;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .findContainerFor("mam") != null && OptimizationsandTweaksConfig.enableMixinMAMWorldGenerator) {
            File configFile = new File(event.getModConfigurationDirectory(), "MYTH_AND_MONSTER_structureconfig.cfg");
            ModConfig modConfig = new ModConfig(configFile, event);
            FileInjector.setModConfig(modConfig);
            ModConfig.initializeConfig(event);
        }
        if (FMLCommonHandler.instance()
            .findContainerFor("ExpOre") != null
            && OptimizationsandTweaksConfig.enableMixinWorldGenHandlerExperienceOre) {
            ExperienceOreConfig.setupAndLoad(event);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (OptimizationsandTweaksConfig.enableTidyChunkBackport) {
            TidyChunkBackportEventHandler eventHandler = new TidyChunkBackportEventHandler();
            MinecraftForge.EVENT_BUS.register(eventHandler);
        }
        if (OptimizationsandTweaksConfig.enableEntityItemSpawningDebugger) {
            EntityItemSpawningEventHandler eventHandler = new EntityItemSpawningEventHandler();
            MinecraftForge.EVENT_BUS.register(eventHandler);
        }
        MinecraftForge.EVENT_BUS.register(proxy);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
