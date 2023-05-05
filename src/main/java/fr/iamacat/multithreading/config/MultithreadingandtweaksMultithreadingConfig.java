package fr.iamacat.multithreading.config;

import com.falsepattern.lib.config.Config;

import fr.iamacat.multithreading.Tags;

@Config(modid = Tags.MODID)
public class MultithreadingandtweaksMultithreadingConfig {

    @Config.Comment("Enable multithreaded/batched for leaf decay.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinLeafDecay;

    @Config.Comment("Enable multithreaded for AI Task.)")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntityAITask;
    @Config.Comment("Enable multithreaded/batched for Entity Update.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntityUpdate;
    @Config.Comment("Enable multithreaded/batched for Fire Tick.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinFireTick;
    @Config.Comment("Enable multithreaded/batched for Growth Spreading.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinGrowthSpreading;
    @Config.Comment("Enable multithreaded/batched for Liquid Tick.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinliquidTick;
    @Config.Comment("Enable multithreaded/batched for Entity Spawning.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntitySpawning;

    @Config.Comment("Enable multithreaded/batched for Chunk Populating such as structure ,dungeons ets....")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinChunkPopulating;
    @Config.Comment("Enable multithreaded/batched for Entity Thunderbolt")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntityLightningBolt;
    @Config.Comment("Enable multithreaded/batched GUI/HUD/TEXT rendering")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinGUIHUD;

    @Config.Comment("Enable multithreaded/batched Particle rendering")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinParticle;

    @Config.Comment("Enable multithreaded/batched TileEntities rendering")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinTileEntities;

    @Config.Comment("Enable multithreaded Worldgen(not recommanded to be enabled with enableThreadedChunkUpdates from archaicfix)")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinWorldgen;

    @Config.Comment("Enable multithreaded Worldgen Tick(not recommanded to be enabled with enableThreadedChunkUpdates from archaicfix)")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinWorldTick;

    @Config.Comment("Enable multithreaded/batched Entities Tick")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntitiesTick;

    @Config.Comment("Enable multithreaded/batched Entities Rendering")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinEntitiesRendering;

    @Config.Comment("Enable multithreaded Tile Entities Tick")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableMixinTileEntitiesTick;

    @Config.Comment("Remove Lightning update when break or place a block for better fps")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean disablelightningupdate;

    @Config.Comment("Choose the number of processor/CPU of your computer to fix potential issues.")
    @Config.DefaultInt(6)
    @Config.RangeInt(min = 1, max = 64)
    public static int numberofcpus;

    @Config.Comment("Batch size ,if you have tps issues try lowering or highering the batch size.")
    @Config.DefaultInt(50)
    @Config.RangeInt(min = 1, max = 1000)
    public static int batchsize;
    /*
     * @Config.Comment("List of entities to ignore for entity ticking optimization.")
     * @Config.DefaultStringList({ "Wither", "EnderDragon" })
     * public static String[] optimizeEntityTickingIgnoreList;
     */
}
