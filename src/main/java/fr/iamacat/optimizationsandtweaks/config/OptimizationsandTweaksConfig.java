package fr.iamacat.optimizationsandtweaks.config;

import com.falsepattern.lib.config.Config;

import fr.iamacat.optimizationsandtweaks.Tags;

@Config(modid = Tags.MODID)
public class OptimizationsandTweaksConfig {

    @Config.Comment("Optimize EntityAITarget Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAITarget;

    @Config.Comment("Enabling Block Name Debugger for getPendingBlockUpdates from WorldServer Class?.")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enablegetPendingBlockUpdatesDebugger;
    @Config.Comment("Optimize AxisAlignedBB Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAxisAlignedBB;

    @Config.Comment("Optimize IntCache Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinIntCache;
    @Config.Comment("Optimize EntityTrackerEntry Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityTrackerEntry;

    @Config.Comment("Optimize EntityTracker Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityTracker;
    @Config.Comment("Optimize NetworkManager Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNetworkManager;
    @Config.Comment("Optimize ModifiableAttributeInstance Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinModifiableAttributeInstance;
    @Config.Comment("Optimize RandomPositionGenerator Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRandomPositionGenerator;
    @Config.Comment("Optimize EntityAIWander Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIWander;
    @Config.Comment("Optimize EntityAIPlay Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIPlay;

    @Config.Comment("Optimize EntityArrowAttack Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityArrowAttack;

    @Config.Comment("Optimize CodecIBXM Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCodecIBXM;
    @Config.Comment("Optimize EntityLivingBase.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityLivingBase;
    @Config.Comment("Optimize EntityRenderer Class.")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityRenderer;

    @Config.Comment("Choose the number of processor/CPU of your computer to fix potential issues.")
    @Config.DefaultInt(6)
    @Config.RangeInt(min = 1, max = 64)
    @Config.RequiresWorldRestart
    public static int numberofcpus;
    @Config.Comment("Batch size ,if you have tps issues try lowering or highering the batch size.")
    @Config.DefaultInt(150)
    @Config.RangeInt(min = 1, max = 1000)
    @Config.RequiresWorldRestart
    public static int batchsize;
    @Config.Comment("Optimize MixinEntitySquid class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntitySquid;
    @Config.Comment("Optimize LaunchClassLoader class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLaunchClassLoader;
    @Config.Comment("Optimize EntityZombie class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityZombie;
    @Config.Comment("Optimize EntityLookHelper performances")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityLookHelper;
    @Config.Comment("Optimize BaseAttributeMap performances")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinServersideAttributeMap;
    @Config.Comment("Optimize EntityAIAttackOnCollide performances")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIAttackOnCollide;
    @Config.Comment("Optimiz World Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorld;
    @Config.Comment("Disable Attack Indicator from LotrImprovements if Lotr iam a cat fork is enabled")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMain;

    @Config.Comment("Disabling Visual Recipe loading from PackagedAuto to eliminate ram usage on Modpacks")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNeiHandlerPackagedAuto;
    @Config.Comment("Add decimal value support to Minestones mod stoneDropRate config")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMinestoneSupportDecimalValue;
    @Config.Comment("Make Minestones item with 64 item stacksize instead of 1")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinItemMinestone;

    @Config.Comment("Optimize LowerStringMap performances")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLowerStringMap;
    @Config.Comment("Optimize RenderManager")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRenderManager;
    @Config.Comment("Optimize EntityAINearestAttackableTarget")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAINearestAttackableTarget;
    @Config.Comment("Optimize Block Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlock;
    @Config.Comment("Optimize BlockLeaves Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockLeaves;

    @Config.Comment("Optimize BiomeCache Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeCache;

    @Config.Comment("Optimize RenderBlocks Class(KEEP THIS DISABLED) cause issues ")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRenderBlocks;
    @Config.Comment("Optimize BlockDynamicLiquid Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockDynamicLiquid;
    @Config.Comment("Optimize Entity Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntity;
    @Config.Comment("Optimize EntitySpellParticleFX")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntitySpellParticleFX;
    @Config.Comment("Optimize Tesselator")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTesselator;
    @Config.Comment("Optimize BlockLiquid Tick")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockLiquid;

    @Config.Comment("Optimize EntityItem")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityItem;
    @Config.Comment("Optimize OilTweakEventHandler from Buildcraft oil Tweak addon")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinOilTweakEventHandler;

    @Config.Comment("Optimize SteamcraftEventHandler from Flaxbeard's Steam Power")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinSteamcraftEventHandler;
    @Config.Comment("Optimize EntityBlockLing from Blocklings mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityBlockling;

    @Config.Comment("Optimize CommonProxy from Catwalks 2")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCommonProxyForCatWalks2;
    @Config.Comment("Optimize EntityEagle from Adventurers Amulet")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityEagle;
    @Config.Comment("Optimize MinecraftServer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMinecraftServer;
    @Config.Comment("Optimize DedicatedServer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinDedicatedServer;
    @Config.Comment("Optimize FMLClientHandler")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFMLClientHandler;
    @Config.Comment("Optimize FMLServerHandler")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFMLServerHandler;
    @Config.Comment("Optimize Loader")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLoader;
    @Config.Comment("Optimize Minecraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMinecraft;
    @Config.Comment("Optimize MinecraftServerGui")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMinecraftServerGui;
    @Config.Comment("Optimize SaveFormatOld")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinSaveFormatOld;

    @Config.Comment("Optimize WorldGenMinable")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenMinable;
    @Config.Comment("Optimize ThreadedFileIOBase")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinThreadedFileIOBase;
    @Config.Comment("Optimize WorldServer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldServer;

    @Config.Comment("Optimize PathFinder Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPathFinder;
    @Config.Comment("Optimize StatsComponent")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStatsComponent;
    @Config.Comment("Optimize NBTTagCompound")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNBTTagCompound;
    @Config.Comment("Optimize EntityList")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityList;
    @Config.Comment("Optimize DataWatcher(Avoid usage of locks and use ConcurrentHashMap)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinDataWatcher;
    @Config.Comment("Optimize TickHandler from IC2")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTickHandler;
    @Config.Comment("Optimize NEIServerUtils from NotEnoughItems")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNEIServerUtils;
    @Config.Comment("Optimize Config from IC2")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinConfig;
    @Config.Comment("Optimize EventRegistry from Practical Logistics(Disabled due to crash on certain servers)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventRegistry;

    @Config.Comment("Optimize InfotypeRegistry from Practical Logistics")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinInfotypeRegistry;
    @Config.Comment("Optimize NibbleArray")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNibbleArray;
    @Config.Comment("Optimize EntityLiving")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityLiving;
    @Config.Comment("Optimize EntityAIEatDroppedFood from Easy Breeding mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIEatDroppedFood;

    @Config.Comment("Optimize EntityAITempt + add a follower limit(30 max) at the same time")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAITempt;
    @Config.Comment("Optimize EventHandlerNEP from notenoughpets")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventHandlerNEP;
    @Config.Comment("Optimize HackTickHandler from Pneumaticraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinHackTickHandler;
    @Config.Comment("Optimize EntityAeable")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAgeable;
    @Config.Comment("Optimize PriorityExecutor from Industrialcraft2")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPriorityExecutor;

    @Config.Comment("Optimize MedUtils from DiseaseCraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMedUtils;
    @Config.Comment("Optimize PlayerAether from Aether")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPlayerAether;
    @Config.Comment("Optimize MixinPlayerAetherEvents from Aether")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPlayerAetherEvents;
    @Config.Comment("Optimize ShipKeyHandler Class from Davinci vessels")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinShipKeyHandler;
    @Config.Comment("Optimize NetherliciousEventHandler from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNetherliciousEventHandler;
    @Config.Comment("Optimize AnimTickHandler from Akatsuki mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAnimTickHandler;
    @Config.Comment("Optimize AnimationHandler from Akatsuki mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAnimationHandler;
    @Config.Comment("Optimize EntitySasosri from Akatsuki mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntitySasosri;
    @Config.Comment("Optimize EntitySasosri2 from Akatsuki mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntitySasosri2;
    @Config.Comment("Optimize PuppetKadz from Akatsuki mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPuppetKadz;
    @Config.Comment("Optimize Chunk ticking")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinChunk;
    @Config.Comment("Optimize BlockGrass Ticking")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockGrass;
    @Config.Comment("Optimize OpenGlHelper")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinOpenGlHelper;
    @Config.Comment("Optimize LanguageRegistry")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLanguageRegistry;
    @Config.Comment("Optimize RenderItem")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRenderItem;
    @Config.Comment("Optimize Vec3 Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinVec3;
    @Config.Comment("Optimize FontRenderer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFontRenderer;
    @Config.Comment("Optimize ModelRenderer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinModelRenderer;
    @Config.Comment("Optimize GuiNewChat")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGuiNewChat;
    @Config.Comment("Optimize Gui")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGui;

    @Config.Comment("Optimize RenderGlobal")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRenderGlobal;
    @Config.Comment("Optimize TextureUtil")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTextureUtil;
    @Config.Comment("Optimize ItemRenderer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinItemRenderer;
    @Config.Comment("Optimize TextureManager")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTextureManager;
    @Config.Comment("Fix Godzilla Spam Log from orespawn")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGodZillaFix;
    @Config.Comment("Fix Null Crash when openning some mods manuals when using Witchery(with Ars Magica mods)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGenericEventsWitchery;
    @Config.Comment("Fix Crash when terminating profiler when using Opis")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinopisProfilerEvent;
    @Config.Comment("Optimize EntityMoveHelper Performances")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityMoveHelper;
    @Config.Comment("Optimize EntityAnimal")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAnimal;

    @Config.Comment("Optimize EntityAIFollowParent")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIFollowParent;
    @Config.Comment("Optimize EntityAITasks")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAITasks;

    @Config.Comment("Optimize BlockFalling")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockFalling;
    @Config.Comment("Optimize LootPPHelper from Loot++ Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLootPPHelper;

    @Config.Comment("Optimize HooksCore from CofhCore Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinHooksCore;
    @Config.Comment("Optimize Utils performances class from Et futurum REQUIEM")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinUtils;
    @Config.Comment("Optimize ClassDiscoverer from CodeChickenCore")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinClassDiscoverer;

    @Config.Comment("Optimize TierRecipeManager from Traincraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTierRecipeManager;
    @Config.Comment("Optimize WardenicChargeEvents class from Thaumic Revelations")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWardenicChargeEvents;
    @Config.Comment("Optimize Matmos mod to reduce fps Lags")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableOptimizeMatmos;
    @Config.Comment("Optimize KoRINEventHandler class from Korin Blue Bedrock mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinKoRINEventHandler;
    @Config.Comment("Optimize EventHandlerEntity class from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventHandlerEntity;
    @Config.Comment("Optimize MappingThread class from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMappingThread;

    @Config.Comment("Optimize Unthaumic class from ThaumcraftMinusThaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinUnthaumic;

    @Config.Comment("Optimize StringTranslate class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStringTranslate;

    @Config.Comment("Optimize PlayerSpecials class from Instrumentus mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPlayerSpecials;
    @Config.Comment("Print stats ids to help to fix duplicated Stat list ids crash")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStatList;
    @Config.Comment("Add Missing oredict to Gems n jewels ores")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinModBlocksGemsNJewels;
    @Config.Comment("Add oredict to industrialupgrades")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRegisterOreDict;
    @Config.Comment("Fix null Crash when clicking on the Item from blockling")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinItemBlockling;
    @Config.Comment("Fix null Crash on startup caused by ColoredIron mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinColoredIron;
    @Config.Comment("(todo) Fix https://github.com/quentin452/privates-minecraft-modpack/issues/48 with FamiliarsAPI")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFamiliar;
    @Config.Comment("Fix Crash when using F7 from NEI and using Small Stairs mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldOverlayRenderer;
    @Config.Comment("Fix Exception in server tick loop caused by Portal Gun mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinSettings;
    @Config.Comment("Fix eternalfrost biomes ids are hardcoded at 255(by removing it) if endlessids ids is installed")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEFConfiguration;
    @Config.Comment("Fix Null Crash caused by Better Burning")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBetterBurning;
    @Config.Comment("Fix Null Crash caused by Hardcore Wither")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventHandler;
    @Config.Comment("Fix StackOverflow Crash caused by Shincolle")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEVENT_BUS_EventHandler;
    @Config.Comment("Disable gui from Essence of the God mod?")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixindisablingguifromEssenceofthegod;
    @Config.Comment("Fix crash from AppleFuelHandler from GrowthCraft mod")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAppleFuelHandler;
    @Config.Comment("Change name of TileEntities from aeterh to fix dupplicated names")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAetherTileEntities;
    @Config.Comment("Disable the Jewelrycraft2 book when spawning?")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityEventHandler;
    @Config.Comment("Add a config for biomeids from Minenautica mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeRegistryMinenautica;
    @Config.Comment("Fix Cascading worldgen caused by VentGeneratorSingle class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinVentGeneratorSingle;

    @Config.Comment("Fix Cascading worldgen caused by WorldGeneratorAdv class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGeneratorAdv;
    @Config.Comment("Fix Cascading worldgen caused by CrystalFormationHangingBig class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCrystalFormationHangingBig;
    @Config.Comment("Fix Cascading worldgen caused by CrystalFormationBig class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCrystalFormationBig;
    @Config.Comment("Fix Cascading worldgen caused by BiomeBlobGen class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeBlobGen;
    @Config.Comment("Fix Cascading worldgen caused by RuptureSpike class from Netherlicious mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRuptureSpike;
    @Config.Comment("Fix AluminumOxideWorldGen Infinite Loop that freeze the server from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAluminumOxideWorldGen;
    @Config.Comment("Fix Cascading worldgens caused by CanBlockStay from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCanBlockStay;
    @Config.Comment("Fix Cascading worldgens caused by BiomeGenKelpForest from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeGenKelpForest;
    @Config.Comment("Fix Cascading worldgens caused by BiomeGenGrassyPlateaus from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeGenGrassyPlateaus;
    @Config.Comment("Fix Cascading worldgens caused by Bloodgrass from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBloodgrass;
    @Config.Comment("Fix Cascading worldgens caused by GenerateCoal from Minenautica mod(Disabled by default cause problem during generation)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGenerateCoral;
    @Config.Comment("Made sure that EntityID from RunicDungeons is above 1000 to prevent crash with Minenautica(Need config helper installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCommonProxyRunicDungeons;
    @Config.Comment("Made sure that EntityID from The Real Keter is above 1000 to prevent crash with an unknown mod(Need config helper installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinKMOD_Main_Entities;

    @Config.Comment("Made sure that EntityID from OreSpiders is above 1000 to prevent crash with unknown mod(Need config helper installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityRegistererOreSpiders;
    @Config.Comment("Fix Spam logs when minefactory reloaded is installed with several mod(see the mixin to find which")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixNoSuchMethodException;
    @Config.Comment("Fix Crash between Endlessids and Lord of the rings mod(but problem remain , does not support higher id than the vanilla scope(256)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean MixinLOTRWorldProvider;
    @Config.Comment("Add configs options to change Biome Ids from LOTR mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAddConfigForLOTRBIOMEIDS;
    @Config.Comment("Fix Some Cascading Worldgen caused by Rubber Trees from minefactory reloaded Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixRubberTreesMinefactoryReloadedCascadingWorldgenFix;
    @Config.Comment("Fix Some Cascading Worldgen caused by MineFactoryReloadedWorldGen from minefactory reloaded Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingforMineFactoryReloadedWorldGen;
    @Config.Comment("Fix Some Cascading Worldgen caused by Lakes from minefactory reloaded Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixWorldGenLakesMetaMinefactoryReloadedCascadingWorldgenFix;
    @Config.Comment("Fix All Cascading Worldgen caused by Poly Gravel from Shinkeiseikan Collection Mod (ATTENTION this mixin disabling Poly Gravel generation)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingFromWorldGenPolyGravel;

    @Config.Comment("Fix Some Cascading Worldgen caused by Shincolle World Gen from Shinkeiseikan Collection Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingFromShinColleWorldGen;

    @Config.Comment("Fix Some Cascading Worldgen caused by Garden Stuff mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenCandelilla;

    @Config.Comment("Fix Completly Cascading Worldgen caused by Shipwreck Gen from Shipwreck Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingFromShipwreckGen;

    @Config.Comment("Fix Some Cascading Worldgen caused by Trees from pam's harvestcraft mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixPamsTreesCascadingWorldgenLag;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenSlimeCarnage from Slime Carnage mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingFromWorldGenSlimeCarnage;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenMadLab from Slime Carnage mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenMadLab;
    @Config.Comment("Fix Some Cascading Worldgen caused by ThaumcraftWorldGenerator from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingWorldGenFromThaumcraftWorldGenerator;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenGreatwoodTrees from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenGreatwoodTrees;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenEldritchRing from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenEldritchRing;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenCustomFlowers from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenCustomFlowers;

    @Config.Comment("Fix Some Cascading Worldgen caused by Utils Class from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinThaumcraftUtils;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenPamFruitTree from Pam's Harvestcraft mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixWorldGenPamFruitTree;
    @Config.Comment("Fix Unable to play unknown soundEvent: minecraft: for Entities from Farlanders mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenBrassTree from Steamcraft 2 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingFromWorldGenBrassTree;
    @Config.Comment("Fix Random Crash caused by oredict from Cofh Core mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinOreDictCofhFix;
    @Config.Comment("Fix null Crash during World Exiting from Ragdoll Corpse")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinRagdollCorpse;
    @Config.Comment("Fix Crash while updating neighbours from BlockTickingWater class from Cofh Core")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockTickingWater;
    @Config.Comment("Fix tps lags + reduce fps stutters caused by leaves from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPatchBlockMagicalLeavesPerformances;

    @Config.Comment("Fix Null crash caused by ScanManager from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinScanManager;
    @Config.Comment("Reduce tps lags caused by SpawnerAnimals on VoidWorld")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPatchSpawnerAnimals;

    @Config.Comment("Reduce tps lags caused by BlockLeaves especially on Modpacks")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLeaves;
    @Config.Comment("Reduce tps lags caused by BiomeGenMagicalForest from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPatchBiomeGenMagicalForest;

    @Config.Comment("Reduce tps lags caused by WorldGenCloudNine from Kingdom of the Overworld")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPatchWorldGenCloudNine;
    @Config.Comment("Fix a large bottleneck caused by EntityDarkMiresi from The Kingdom of the Overworld")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityDarkMiresi;
    @Config.Comment("Remove Version check from Mal Core mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinVersionInfo;

    @Config.Comment("Remove unecessary println in KitchenCraftMachines class from KitchenCraft Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinKitchenCraftMachines;
    @Config.Comment("Make sure that isLoaded from BuildCraftConfig is calculated only one time to reduce tps lag")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBuildCraftConfig;
    /*
     * @Config.Comment("List of entities to ignore for entity ticking optimization.")
     * @Config.DefaultStringList({ "Wither", "EnderDragon" })
     * public static String[] optimizeEntityTickingIgnoreList;
     */
}
