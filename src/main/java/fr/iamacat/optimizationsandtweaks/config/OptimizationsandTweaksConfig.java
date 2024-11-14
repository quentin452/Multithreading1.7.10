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

    @Config.Comment("Optimize IntCache Class.(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinIntCache;

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
    @Config.RangeInt(min = 1, max = 100)
    @Config.RequiresWorldRestart
    public static int batchsize;
    @Config.Comment("Optimize WorldChunkManager class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldChunkManager;
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
    @Config.Comment("Optimize World Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorld;

    @Config.Comment("Optimize EventBus Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventBus;

    @Config.Comment("Optimize EnchantmentHelper Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEnchantmentHelper;

    @Config.Comment("Optimize StructureGeneratorBase Class from Mowzies mobs")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStructureGeneratorBaseMM;
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
    @Config.Comment("Tweak BlockBuildCraftFluid class from Buildcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockBuildCraftFluid;
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
    @Config.Comment("Optimize MinecraftServer(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMinecraftServer;
    @Config.Comment("Optimize FMLClientHandler")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFMLClientHandler;
    @Config.Comment("Optimize FMLServerHandler")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFMLServerHandler;
    /*
     * todo need to fix was loaded to early crash
     * @Config.Comment("Optimize Loader")
     * @Config.DefaultBoolean(true)
     * @Config.RequiresWorldRestart
     * public static boolean enableMixinLoader;
     */
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
    @Config.Comment("Optimize WorldServer(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldServer;
    @Config.Comment("Optimize MathHelper Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMathHelper;
    @Config.Comment("Optimize Stitcher Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStitcher;
    @Config.Comment("Optimize Pathfinding for entities")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enablePathfinderOptimizations;
    @Config.Comment("Optimize StatsComponent")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinStatsComponent;

    @Config.Comment("Optimize WorldType class from vanilla")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldType;
    @Config.Comment("Optimize NBTTagCompound")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNBTTagCompound;
    @Config.Comment("Optimize EntityList")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityList;
    @Config.Comment("Optimize DataWatcher")
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

    @Config.Comment("Optimize ThaumcraftCraftingManager from Thaumcraft To Reduce loading time")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinThaumcraftCraftingManager;
    @Config.Comment("Fix null crashes caused by Thaumonomicon Book of Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableFixThaumonomiconBookNullCrashes;

    @Config.Comment("Fix ghost EntityItem generated by onEntityCollidedWithBlock from Aura Node from Thaumcraft to prevent lags")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockAiry;
    /*
     * @Config.Comment("Patch EntityRegistry availableIndicies")
     * @Config.DefaultBoolean(true)
     * @Config.RequiresWorldRestart
     * public static boolean enableMixinEntityRegistry;
     */
    @Config.Comment("Optimize ThaumcraftHelper class From Extra Utilities")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinThaumcraftHelperEU;

    @Config.Comment("Optimize BlockBreakingRegistry class From Extra Utilities")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockBreakingRegistry;
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
    @Config.Comment("Optimize Chunk ticking(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinChunk;
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
    @Config.Comment("Optimize SaveHandler Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinSaveHandler;
    @Config.Comment("Optimize BlockFLuid Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockFluidClassic;
    @Config.Comment("Optimize ChunkProviderServer Class(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinChunkProviderServer;
    @Config.Comment("Optimize EntitySwimming Class from Animal Plus")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntitySwimming;
    @Config.Comment("Optimize MapGenStructure Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMapGenStructure;
    @Config.Comment("Optimize ItemRenderer")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinItemRenderer;
    @Config.Comment("Optimize IntHashMap")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinIntHashMap;
    @Config.Comment("Optimize LongHashMap")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLongHashMap;
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

    @Config.Comment("Optimize ChunkProviderGenerate")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinChunkProviderGenerate;
    @Config.Comment("Optimize BlockGrass")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockGrass;
    @Config.Comment("Optimize CompressedStreamTools")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCompressedStreamTools;
    @Config.Comment("Optimize BiomeGenBase(not injected if DragonAPI is installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeGenBase;
    @Config.Comment("Optimize EntityAIFollowParent")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAIFollowParent;
    @Config.Comment("Optimize EntityMob Class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityMob;
    @Config.Comment("Optimize EntityAITasks")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityAITasks;

    @Config.Comment("Optimize ExtendedBlockStorage")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinExtendedBlockStorage;
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
    @Config.Comment("Optimize PFQueue Class from CoroUtil")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPFQueue;
    @Config.Comment("Optimize ZAUtil Class from ZombieAwareness")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinZAUtil;
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

    @Config.Comment("Optimize AutomagyEventHandler class from Automagy mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAutomagyEventHandler;
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

    @Config.Comment("Fix ClassCastException net.minecraft.block.BlockStaticLiquid cannot be cast to net.minecraftforge.fluids.BlockFluidBase")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBlockFluidBase;
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
    @Config.Comment("Made name of the TileEntity from Fantastic Fish to fix dupplicated names")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFantasticMod;
    @Config.Comment("Change name of TileEntities from aether to fix dupplicated names")
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
    @Config.Comment("Add a config for biomeids from Lot O Mobs mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBiomeRegistryLotsOMobs;
    @Config.Comment("Fix null crash caused by PPAPEventHandler from PPAP MOD")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPPAPEventHandler;

    @Config.Comment("Fix Cascading worldgen caused by FantasticEvents class from Fantastic Fish mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFantasticEvents;
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
    @Config.Comment("Fix Null crashes from EventHandler from Potion shards mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEventHandlerPotionShards;
    @Config.Comment("Fix AcademyGenerator Infinite Loop that freeze the server from Fossil Archeology Revival")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAcademyGenerator;
    @Config.Comment("Fix ShipWrekGenerator Infinite Loop that freeze the server from Fossil Archeology Revival")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinShipWreckGenerator;
    @Config.Comment("Fix Cascading worldgens caused by TarGenerator from Fossil and archeology mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTarGenerator;
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
    @Config.Comment("(DISABLED BY DEFAULT,causing freezes)Made sure that EntityID from TooMuchTNT mod is above 1000 to prevent crash with unknown mod(Need endlessids installed)")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTooMuchTNT;
    @Config.Comment("Made sure that EntityID from EldritchEmpire mod is above 1000 to prevent crash with unknown mod(Need config helper installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEldritchEmpireRegistration;
    @Config.Comment("Made sure that EntityID from elijahs Chocolate mod is above 1000 to prevent crash with unknown mod(Need config helper installed)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityChocolateCreeper;
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

    @Config.Comment("Fix Server attempted to spawn an unknown entity caused by thread unsafety from NetHandlerPlayClient class")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNetHandlerPlayClient;
    @Config.Comment("Add configs options to change Potion Ids from Minegicka mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinModBaseMinegicka;
    @Config.Comment("Add configs options to change Biome Ids from DisasterCraft mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAddConfigForDisastercraft;
    @Config.Comment("Add configs options to change Biome Ids from LOTR mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAddConfigForLOTRBIOMEIDS;
    @Config.Comment("Fix Concurrent Modification Exceptions caused by ForgeChunkManager(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinForgeChunkManager;
    @Config.Comment("Fix Concurrent Modification Exceptions caused by AnvilChunkLoader(You should never disable this,if its disabled it can cause large issues caused by threading)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinAnvilChunkLoader;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenTreeBase class from ObsGreenery mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenTreeBase;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenTreeBlackWattle class from ObsGreenery mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenTreeBlackWattle;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenMassiveTree class from minefactory reloaded Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenMassiveTree;
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
    @Config.Comment("Fix Some Cascading Worldgen caused by SpringPopulate Class from Buildcraft mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinSpringPopulate;
    @Config.Comment("Fix Some Cascading Worldgen caused by GOBLINWorldGenFireplace Class from Goblins mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGOBLINWorldGenFireplace;
    @Config.Comment("Fix Some Cascading Worldgen caused by GOBLINWorldGenHuts Class from Goblins mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGOBLINWorldGenHuts;
    @Config.Comment("Fix Some Cascading Worldgen caused by GOBLINWorldGenGVillage1 Class from Goblins mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGOBLINWorldGenGVillage1;
    @Config.Comment("Fix Some Cascading Worldgen caused by ThaumcraftWorldGenerator from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinFixCascadingWorldGenFromThaumcraftWorldGenerator;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenGreatwoodTrees from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenGreatwoodTrees;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenSilverTrees from Thaumcraft4 mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenSilverwoodTrees;
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
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenMisStructures Class from Fossil And Archeology Revival mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenMiscStructures;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenPyramid Class from Atum mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenPyramid;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenOasis Class from Atum mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenOasis;
    @Config.Comment("Fix Some Cascading Worldgen caused by WorldGenRuins Class from Atum mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenRuins;
    @Config.Comment("Fix Some Cascading Worldgen caused by NetherWorldGen Class from Gany's Nether mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinNetherWorldGenGanys;

    @Config.Comment("Fix Some Cascading Worldgen caused by ObsidianChestGenerator Class from SGS Treasure mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinObsidianChestGenerator;

    @Config.Comment("Fix Some Cascading Worldgen caused by PlansProcessor Class from SGS Treasure mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPlansProcessor;

    @Config.Comment("Fix Some Cascading Worldgen caused by WorldUtilSGSTREASURE Class from SGS Treasure mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldUtilSGSTREASURE;
    @Config.Comment("Rewrite MythicAndMonster generator to use RecurrentComplex(need recurent complex installed)(The mixin will be injected only if Recurrent Complex is installed")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMAMWorldGenerator;
    @Config.Comment("Fix EXTREME Cascading Worldgen caused by WorldGenNori Class from MasterChef mod(I disabled the generation code because it seem that nothing is generated in game and so make cascading worldgens for nothing)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenNori;
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
    @Config.Comment("Fix java.lang.Integer cannot be cast to java.lang.Byte caused by Hamsterific Restored mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinEntityHamster;
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

    @Config.Comment("Fix null crash caused by KeyHandler from Thaumic Revelation")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinKeyHandlerTHAUMREV;

    @Config.Comment("Fix Null crash caused by ScanManager from Thaumcraft")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinScanManager;

    @Config.Comment("Fix NoSuchMethodError caused by MobSpawnerGoblinlogic from Goblin mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMobSpawnerGoblinLogic;

    @Config.Comment("Fix Stack overflow crash when angelica is loaded by disabling some methods from GLDebug class from angelica")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGLDebug;

    @Config.Comment("Fix Invalid framebuffer operation (1286) caused by Hud Caching from angelica mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinHUDCaching;
    @Config.Comment("Reduce tps lags caused by SpawnerAnimals(Can reduce spawn speed of entities)(Require MixinChunk)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinPatchSpawnerAnimals;

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
    @Config.Comment("Fix https://github.com/quentin452/privates-minecraft-modpack/issues/905 caused by Tinkersconstruct")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinTinkerGears;
    @Config.Comment("Disable LocalizationUpdater from RemoteIO")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinLocalizationUpdater;
    @Config.Comment("Disable Version Check from Bird Nests")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean disableBirdNestVersionChecker;
    @Config.Comment("Disable Version Check from COFHCORE")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCOFHCORE;
    @Config.Comment("Disable Version Check from WEATHER CARPET")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinCloudChecking;
    @Config.Comment("Disable DevCape Rendering register from Myth and Monsters")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinMAMClientProxy;
    @Config.Comment("Disable Version check from Mal Core mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinVersionInfo;
    @Config.Comment("Add a config to change frequency or Experience ore generation from Experience ore Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinWorldGenHandlerExperienceOre;
    @Config.Comment("Remove unecessary println in KitchenCraftMachines class from KitchenCraft Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinKitchenCraftMachines;
    @Config.Comment("Make sure that isLoaded from BuildCraftConfig is calculated only one time to reduce tps lag")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBuildCraftConfig;
    @Config.Comment("Disabling Anti Fall damage (OP) from Mankini Mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinBatMankiniJump;
    @Config.Comment("Remove Grim3212 Version Checker to prevent https://github.com/quentin452/privates-minecraft-modpack/issues/903")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinGrimModule;
    @Config.Comment("Tidy Chunk Backport feature(EntityItem remover at first chunk generation to reduce tps lags)")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableTidyChunkBackport;
    @Config.Comment("Tidy Chunk Backport Number of ticks post chunk generation to check for EntityItems , 20 tick = 1 seconde")
    @Config.DefaultInt(50)
    @Config.RangeInt(min = 0, max = 1000)
    @Config.RequiresWorldRestart
    public static int TidyChunkBackportPostTick;
    @Config.Comment("Tidy Chunk Backport debugger")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableTidyChunkBackportDebugger;

    @Config.Comment("EntityItem Spawning debugger")
    @Config.DefaultBoolean(false)
    @Config.RequiresWorldRestart
    public static boolean enableEntityItemSpawningDebugger;

    @Config.Comment("Enable Mixinmcreator_ununquadiumLand for Ununqdium Land mod")
    @Config.DefaultBoolean(true)
    @Config.RequiresWorldRestart
    public static boolean enableMixinmcreator_ununquadiumLand;

    @Config.Comment("UnunquadiumLand mod Dimensionid(Require enableMixinmcreator_ununquadiumLand)")
    @Config.DefaultInt(3)
    @Config.RangeInt(min = 0, max = 65536)
    @Config.RequiresWorldRestart
    public static int LandDimensionID;
    /*
     * @Config.Comment("List of entities to ignore for entity ticking optimization.")
     * @Config.DefaultStringList({ "Wither", "EnderDragon" })
     * public static String[] optimizeEntityTickingIgnoreList;
     */
}
