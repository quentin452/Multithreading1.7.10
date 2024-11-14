package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import javax.imageio.ImageIO;

import net.minecraft.block.material.Material;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.*;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.*;
import net.minecraft.util.Timer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.StartupQuery;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.mixins.Classers;

@Mixin(value = Minecraft.class, priority = 999)
public abstract class MixinMinecraft implements IPlayerUsage {

    @Shadow
    private static final Logger logger = LogManager.getLogger();
    /** A 10MiB preallocation to ensure the heap is reasonably sized. */
    @Shadow
    public static byte[] memoryReserve = new byte[10485760];
    @Shadow
    private static final List macDisplayModes = Lists
        .newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
    @Shadow
    private final File fileResourcepacks;
    @Shadow
    private final Multimap field_152356_J;
    @Shadow
    private ServerData currentServerData;
    /** The RenderEngine instance used by Minecraft */
    @Shadow
    public TextureManager renderEngine;
    /** Set to 'this' in Minecraft constructor; used by some settings get methods */
    @Shadow
    private static Minecraft theMinecraft;
    @Shadow
    public PlayerControllerMP playerController;
    @Shadow
    private boolean fullscreen;
    @Shadow
    private boolean hasCrashed;

    /** Instance of CrashReport. */
    @Shadow
    private CrashReport crashReporter;
    @Shadow
    public int displayWidth;
    @Shadow
    public int displayHeight;
    @Shadow
    private Timer timer = new Timer(20.0F);
    /** Instance of PlayerUsageSnooper. */
    @Shadow
    private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper(
        "client",
        this,
        MinecraftServer.getSystemTimeMillis());
    @Shadow
    public WorldClient theWorld;
    @Shadow
    public RenderGlobal renderGlobal;
    @Shadow
    public EntityClientPlayerMP thePlayer;
    /**
     * The Entity from which the renderer determines the render viewpoint. Currently, is always the parent Minecraft
     * class's 'thePlayer' instance. Modification of its location, rotation, or other settings at render time will
     * modify the camera likewise, with the caveat of triggering chunk rebuilds as it moves, making it unsuitable for
     * changing the viewpoint mid-render.
     */
    @Shadow
    public EntityLivingBase renderViewEntity;
    @Shadow
    public Entity pointedEntity;
    @Shadow
    public EffectRenderer effectRenderer;
    @Shadow
    private final Session session;
    @Shadow
    private boolean isGamePaused;
    /** The font renderer used for displaying and measuring text. */
    @Shadow
    public FontRenderer fontRenderer;
    @Shadow
    public FontRenderer standardGalacticFontRenderer;
    /** The GuiScreen that's being displayed at the moment. */
    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public LoadingScreenRenderer loadingScreen;
    @Shadow
    public EntityRenderer entityRenderer;
    /** Mouse left click counter */
    @Shadow
    private int leftClickCounter;
    /** Display width */
    @Shadow
    private int tempDisplayWidth;
    /** Display height */
    @Shadow
    private int tempDisplayHeight;
    /** Instance of IntegratedServer. */
    @Shadow
    private IntegratedServer theIntegratedServer;
    /** Gui achievement */
    @Shadow
    public GuiAchievement guiAchievement;
    @Shadow
    public GuiIngame ingameGUI;
    /** Skip render world */
    @Shadow
    public boolean skipRenderWorld;
    /** The ray trace hit that the mouse is over. */
    @Shadow
    public MovingObjectPosition objectMouseOver;
    /** The game settings that currently hold effect. */
    @Shadow
    public GameSettings gameSettings;
    /** Mouse helper instance. */
    @Shadow
    public MouseHelper mouseHelper;
    @Shadow
    public final File mcDataDir;
    @Shadow
    private final File fileAssets;
    @Shadow
    private final String launchedVersion;
    @Shadow
    private final Proxy proxy;
    @Shadow
    private ISaveFormat saveLoader;
    /**
     * This is set to fpsCounter every debug screen update, and is shown on the debug screen. It's also sent as part of
     * the usage snooping.
     */
    @Shadow
    private static int debugFPS;
    /** When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block. */
    @Shadow
    private int rightClickDelayTimer;
    /** Checked in Minecraft's while(running) loop, if true it's set to false and the textures refreshed. */
    @Shadow
    private boolean refreshTexturePacksScheduled;
    @Shadow
    private String serverName;
    @Shadow
    private int serverPort;
    /** Does the actual gameplay have focus. If so then mouse and keys will effect the player instead of menus. */
    @Shadow
    public boolean inGameHasFocus;
    @Shadow
    long systemTime = getSystemTime();
    /** Join player counter */
    @Shadow
    private int joinPlayerCounter;
    @Shadow
    private final boolean jvm64bit;
    @Shadow
    private final boolean isDemo;
    @Shadow
    private NetworkManager myNetworkManager;
    @Shadow
    private boolean integratedServerIsRunning;

    /** The profiler instance */
    @Shadow
    public final Profiler mcProfiler = new Profiler();
    @Shadow
    private long field_83002_am = -1L;
    @Shadow
    private IReloadableResourceManager mcResourceManager;
    @Shadow
    private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
    @Shadow
    private List defaultResourcePacks = Lists.newArrayList();
    @Shadow
    public DefaultResourcePack mcDefaultResourcePack;
    @Shadow
    private ResourcePackRepository mcResourcePackRepository;
    @Shadow
    private LanguageManager mcLanguageManager;
    @Shadow
    private IStream field_152353_at;
    @Shadow
    private Framebuffer framebufferMc;
    @Shadow
    private TextureMap textureMapBlocks;
    @Shadow
    private SoundHandler mcSoundHandler;
    @Shadow
    private MusicTicker mcMusicTicker;
    @Shadow
    private ResourceLocation field_152354_ay;
    @Shadow
    private final MinecraftSessionService field_152355_az;
    @Shadow
    private SkinManager field_152350_aA;
    @Shadow
    private final Queue field_152351_aB = Queues.newArrayDeque();
    @Shadow
    private final Thread field_152352_aC = Thread.currentThread();
    /** Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly. */
    @Shadow
    volatile boolean running = true;
    /** String that shows the debug information */
    @Shadow
    public String debug = "";
    /** Approximate time (in ms) of last update to debug string */
    @Shadow
    long debugUpdateTime = getSystemTime();
    /** holds the current fps */
    @Shadow
    int fpsCounter;
    @Shadow
    long prevFrameTime = -1L;
    /** Profiler currently displayed in the debug screen pie chart */
    @Shadow
    private String debugProfilerName = "root";

    protected MixinMinecraft(Session sessionIn, int displayWidth, int displayHeight, boolean fullscreen, boolean isDemo,
        File dataDir, File assetsDir, File resourcePackDir, Proxy proxy, String version, Multimap twitchDetails,
        String assetsJsonVersion) {
        this.mcDataDir = dataDir;
        this.fileAssets = assetsDir;
        this.fileResourcepacks = resourcePackDir;
        this.launchedVersion = version;
        this.field_152356_J = twitchDetails;
        this.mcDefaultResourcePack = new DefaultResourcePack(
            (new ResourceIndex(assetsDir, assetsJsonVersion)).func_152782_a());
        this.addDefaultResourcePack();
        this.proxy = proxy == null ? Proxy.NO_PROXY : proxy;
        this.field_152355_az = (new YggdrasilAuthenticationService(
            proxy,
            UUID.randomUUID()
                .toString())).createMinecraftSessionService();
        this.startTimerHackThread();
        this.session = sessionIn;
        logger.info("Setting user: " + sessionIn.getUsername());
        this.isDemo = isDemo;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.tempDisplayWidth = displayWidth;
        this.tempDisplayHeight = displayHeight;
        this.fullscreen = fullscreen;
        this.jvm64bit = isJvm64bit();
        ImageIO.setUseCache(false);
        Bootstrap.func_151354_b();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void shutdownMinecraftApplet() {
        try {
            this.field_152353_at.func_152923_i();
            logger.info("Stopping!");

            try {
                this.loadWorld(null);
            } catch (Throwable throwable1) {}

            try {
                GLAllocation.deleteTexturesAndDisplayLists();
            } catch (Throwable throwable) {}

            this.mcSoundHandler.unloadSounds();
        } finally {
            Display.destroy();

            if (!this.hasCrashed) {
                System.exit(0);
            }
        }

        // System.gc();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean isJvm64bit() {
        String[] astring = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];
            String s1 = System.getProperty(s);

            if (s1 != null && s1.contains("64")) {
                return true;
            }
        }

        return false;
    }

    @Shadow
    private void addDefaultResourcePack() {
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
    }

    @Shadow
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Shadow
    private ByteBuffer func_152340_a(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(imageStream);
        int[] aint = bufferedimage
            .getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (int k : aint) {
            bytebuffer.putInt(k << 8 | k >> 24 & 255);
        }
        bytebuffer.flip();
        return bytebuffer;
    }

    /**
     * @author
     * @reason
     */

    @Overwrite
    private void startTimerHackThread() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> running = false, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    @Shadow
    private void startGame() throws LWJGLException {
        this.gameSettings = new GameSettings(theMinecraft, this.mcDataDir);

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }

        if (this.fullscreen) {
            Display.setFullscreen(true);
            this.displayWidth = Display.getDisplayMode()
                .getWidth();
            this.displayHeight = Display.getDisplayMode()
                .getHeight();

            if (this.displayWidth <= 0) {
                this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
                this.displayHeight = 1;
            }
        } else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }

        Display.setResizable(true);
        Display.setTitle("Minecraft 1.7.10");
        logger.info("LWJGL Version: " + Sys.getVersion());
        Util.EnumOS enumos = Util.getOSType();

        if (enumos != Util.EnumOS.OSX) {
            try {
                InputStream inputstream = this.mcDefaultResourcePack
                    .func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                InputStream inputstream1 = this.mcDefaultResourcePack
                    .func_152780_c(new ResourceLocation("icons/icon_32x32.png"));

                if (inputstream != null && inputstream1 != null) {
                    Display.setIcon(
                        new ByteBuffer[] { this.func_152340_a(inputstream), this.func_152340_a(inputstream1) });
                }
            } catch (IOException ioexception) {
                logger.error("Couldn't set icon", ioexception);
            }
        }

        try {
            net.minecraftforge.client.ForgeHooksClient.createDisplay();
        } catch (LWJGLException lwjglexception) {
            logger.error("Couldn't set pixel format", lwjglexception);

            if (this.fullscreen) {
                this.updateDisplayMode();
            }

            Display.create();
        }

        OpenGlHelper.initializeTextures();

        try {
            this.field_152353_at = new TwitchStream(
                theMinecraft,
                (String) Iterables.getFirst(this.field_152356_J.get("twitch_access_token"), null));
        } catch (Throwable throwable) {
            this.field_152353_at = new NullStream(throwable);
            logger.error("Couldn't initialize twitch stream");
        }

        this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.guiAchievement = new GuiAchievement(theMinecraft);
        this.metadataSerializer_
            .registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_
            .registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_
            .registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_
            .registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_
            .registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcResourcePackRepository = new ResourcePackRepository(
            this.fileResourcepacks,
            new File(this.mcDataDir, "server-resource-packs"),
            this.mcDefaultResourcePack,
            this.metadataSerializer_,
            this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        FMLClientHandler.instance()
            .beginMinecraftLoading(theMinecraft, this.defaultResourcePacks, this.mcResourceManager);
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.field_152350_aA = new SkinManager(
            this.renderEngine,
            new File(this.fileAssets, "skins"),
            this.field_152355_az);
        cpw.mods.fml.client.SplashProgress.drawVanillaScreen();
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(theMinecraft);
        this.fontRenderer = new FontRenderer(
            this.gameSettings,
            new ResourceLocation("textures/font/ascii.png"),
            this.renderEngine,
            false);

        if (this.gameSettings.language != null) {
            this.fontRenderer.setUnicodeFlag(this.func_152349_b());
            this.fontRenderer.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(
            this.gameSettings,
            new ResourceLocation("textures/font/ascii_sga.png"),
            this.renderEngine,
            false);
        this.mcResourceManager.registerReloadListener(this.fontRenderer);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        cpw.mods.fml.common.ProgressManager.ProgressBar bar = cpw.mods.fml.common.ProgressManager
            .push("Rendering Setup", 9, true);
        bar.step("Loading Render Manager");
        RenderManager.instance.itemRenderer = new ItemRenderer(theMinecraft);
        bar.step("Loading Entity Renderer");
        this.entityRenderer = new EntityRenderer(theMinecraft, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {

            /**
             * Formats the strings based on 'IStatStringFormat' interface.
             */
            public String formatString(String p_74535_1_) {
                try {
                    return String.format(
                        p_74535_1_,
                        GameSettings.getKeyDisplayString(gameSettings.keyBindInventory.getKeyCode()));
                } catch (Exception exception) {
                    return "Error: " + exception.getLocalizedMessage();
                }
            }
        });
        bar.step("Loading GL properties");
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.checkGLError("Startup");
        bar.step("Render Global instance");
        this.renderGlobal = new RenderGlobal(theMinecraft);
        bar.step("Building Blocks Texture");
        this.textureMapBlocks = new TextureMap(0, "textures/blocks", true);
        bar.step("Anisotropy and Mipmaps");
        this.textureMapBlocks.setAnisotropicFiltering(this.gameSettings.anisotropicFiltering);
        this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        bar.step("Loading Blocks Texture");
        this.renderEngine.loadTextureMap(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        bar.step("Loading Items Texture");
        this.renderEngine.loadTextureMap(TextureMap.locationItemsTexture, new TextureMap(1, "textures/items", true));
        bar.step("Viewport");
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        cpw.mods.fml.common.ProgressManager.pop(bar);
        FMLClientHandler.instance()
            .finishMinecraftLoading();
        this.checkGLError("Post startup");
        this.ingameGUI = new net.minecraftforge.client.GuiIngameForge(theMinecraft);

        if (this.serverName != null) {
            FMLClientHandler.instance()
                .connectToServerAtStartup(this.serverName, this.serverPort);
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }

        cpw.mods.fml.client.SplashProgress.clearVanillaResources(renderEngine, field_152354_ay);
        this.field_152354_ay = null;
        this.loadingScreen = new LoadingScreenRenderer(theMinecraft);

        FMLClientHandler.instance()
            .onInitializationComplete();
        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }

        try {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        } catch (OpenGLException openglexception) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }
    }

    @Shadow
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;

            if (this.fullscreen) {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode()
                    .getWidth();
                this.displayHeight = Display.getDisplayMode()
                    .getHeight();

            } else {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;
            }
            if (this.displayWidth <= 0) {
                this.displayWidth = 1;
            }
            if (this.displayHeight <= 0) {
                this.displayHeight = 1;
            }

            if (this.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            } else {
                this.updateFramebufferSize();
            }

            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.func_147120_f();
        } catch (Exception exception) {
            logger.error("Couldn't toggle fullscreen", exception);
        }
    }

    @Shadow
    public void resize(int width, int height) {
        this.displayWidth = width <= 0 ? 1 : width;
        this.displayHeight = height <= 0 ? 1 : height;

        if (this.currentScreen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(theMinecraft, width, height);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            this.currentScreen.setWorldAndResolution(theMinecraft, k, l);
        }

        this.loadingScreen = new LoadingScreenRenderer(theMinecraft);
        this.updateFramebufferSize();
    }

    @Shadow
    public void func_147120_f() {
        Display.update();

        if (!this.fullscreen && Display.wasResized()) {
            int i = this.displayWidth;
            int j = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();

            if (this.displayWidth != i || this.displayHeight != j) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }

                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }

    @Shadow
    private void updateFramebufferSize() {
        this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);

        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }

    @Shadow
    public boolean func_152349_b() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }

    @Shadow
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        if (guiScreenIn == null && this.theWorld == null) {
            guiScreenIn = new GuiMainMenu();
        } else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        GuiScreen old = this.currentScreen;
        net.minecraftforge.client.event.GuiOpenEvent event = new net.minecraftforge.client.event.GuiOpenEvent(
            guiScreenIn);

        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;

        guiScreenIn = event.gui;
        if (old != null && guiScreenIn != old) {
            old.onGuiClosed();
        }

        if (guiScreenIn instanceof GuiMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI()
                .clearChatMessages();
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(
                theMinecraft,
                this.displayWidth,
                this.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(theMinecraft, i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    @Shadow
    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            KeyBinding.unPressAllKeys();
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    @Shadow
    public void setIngameFocus() {
        if (Display.isActive() && (!this.inGameHasFocus)) {
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }

    @Shadow
    private void checkGLError(String message) {
        int i = GL11.glGetError();

        if (i != 0) {
            String s1 = GLU.gluErrorString(i);
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + message);
            logger.error(i + ": " + s1);
        }
    }

    @Shadow
    private void updateDisplayMode() throws LWJGLException {
        HashSet hashset = new HashSet();
        Collections.addAll(hashset, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();

        if (!hashset.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {

            for (Object macDisplayMode : macDisplayModes) {
                DisplayMode displaymode1 = (DisplayMode) macDisplayMode;
                boolean flag = true;
                Iterator iterator1 = hashset.iterator();
                DisplayMode displaymode2;

                while (iterator1.hasNext()) {
                    displaymode2 = (DisplayMode) iterator1.next();

                    if (displaymode2.getBitsPerPixel() == 32 && displaymode2.getWidth() == displaymode1.getWidth()
                        && displaymode2.getHeight() == displaymode1.getHeight()) {
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    iterator1 = hashset.iterator();

                    while (iterator1.hasNext()) {
                        displaymode2 = (DisplayMode) iterator1.next();

                        if (displaymode2.getBitsPerPixel() == 32
                            && displaymode2.getWidth() == displaymode1.getWidth() / 2
                            && displaymode2.getHeight() == displaymode1.getHeight() / 2) {
                            displaymode = displaymode2;
                            break;
                        }
                    }
                }
            }
        }

        Display.setDisplayMode(displaymode);
        this.displayWidth = displaymode.getWidth();
        this.displayHeight = displaymode.getHeight();
    }

    /**
     * @author
     * @reason
     */
    // @Overwrite idk why but now this method causing crash so not injected
    public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
        FMLClientHandler.instance()
            .startIntegratedServer(folderName, worldName, worldSettingsIn);
        this.loadWorld(null);
        // System.gc();
        ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (worldSettingsIn == null) {
            assert worldinfo != null;
            worldSettingsIn = new WorldSettings(worldinfo);
        }

        try {
            this.theIntegratedServer = new IntegratedServer(theMinecraft, folderName, worldName, worldSettingsIn);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        this.loadingScreen.displayProgressMessage(I18n.format("menu.loadingLevel"));

        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            if (!StartupQuery.check()) {
                loadWorld(null);
                displayGuiScreen(null);
                return;
            }
            String s2 = this.theIntegratedServer.getUserMessage();

            if (s2 != null) {
                this.loadingScreen.resetProgresAndWorkingMessage(I18n.format(s2));
            } else {
                this.loadingScreen.resetProgresAndWorkingMessage("");
            }

            long startTime = System.currentTimeMillis();
            long delay = 200L;

            while (System.currentTimeMillis() - startTime < delay) {
                if (!StartupQuery.check()) {
                    loadWorld(null);
                    displayGuiScreen(null);
                    return;
                }
            }
        }

        this.displayGuiScreen(null);
        SocketAddress socketaddress = this.theIntegratedServer.func_147137_ag()
            .addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, theMinecraft, null));
        networkmanager
            .scheduleOutboundPacket(new C00Handshake(5, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.scheduleOutboundPacket(
            new C00PacketLoginStart(
                this.getSession()
                    .func_148256_e()));
        this.myNetworkManager = networkmanager;
    }

    @Shadow
    public Session getSession() {
        return this.session;
    }

    @Shadow
    public void loadWorld(WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }

    /**
     * @author
     * @reason
     */

    @Overwrite
    public void loadWorld(WorldClient worldClientIn, String loadingMessage) {
        if (theWorld != null) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.event.world.WorldEvent.Unload(theWorld));
        }

        if (worldClientIn == null) {
            NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();

            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }

            if (this.theIntegratedServer != null) {
                this.theIntegratedServer.initiateShutdown();
                if (loadingScreen != null) {
                    this.loadingScreen.resetProgresAndWorkingMessage(I18n.format("forge.client.shutdown.internal"));
                }
                while (!theIntegratedServer.isServerStopped()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException ie) {

                    }
                }
            }

            this.theIntegratedServer = null;
            this.guiAchievement.func_146257_b();
            this.entityRenderer.getMapItemRenderer()
                .func_148249_a();
        }

        this.renderViewEntity = null;
        this.myNetworkManager = null;

        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.resetProgresAndWorkingMessage("");
        }

        if (worldClientIn == null && this.theWorld != null) {
            if (this.mcResourcePackRepository.func_148530_e() != null) {
                this.scheduleResourcesRefresh();
            }

            this.mcResourcePackRepository.func_148529_f();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
            FMLClientHandler.instance()
                .handleClientWorldClosing(this.theWorld);
        }

        this.mcSoundHandler.stopSounds();
        this.theWorld = worldClientIn;

        if (worldClientIn != null) {
            if (this.renderGlobal != null) {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }

            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(worldClientIn);
            }

            if (this.thePlayer == null) {
                this.thePlayer = this.playerController.func_147493_a(worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.thePlayer);
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.thePlayer);
            this.renderViewEntity = this.thePlayer;
        } else {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }

        // System.gc();
        this.systemTime = 0L;
    }

    @Shadow
    public void setServerData(ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }

    @Shadow
    public NetHandlerPlayClient getNetHandler() {
        return this.thePlayer != null ? this.thePlayer.sendQueue : null;
    }

    @Shadow
    public static Minecraft getMinecraft() {
        return theMinecraft;
    }

    @Shadow
    public void scheduleResourcesRefresh() {
        this.refreshTexturePacksScheduled = true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void runGameLoop() {
        this.mcProfiler.startSection("root");

        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }

        if (this.isGamePaused && this.theWorld != null) {
            float f = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = f;
        } else {
            this.timer.updateTimer();
        }

        if ((this.theWorld == null || this.currentScreen == null) && this.refreshTexturePacksScheduled) {
            this.refreshTexturePacksScheduled = false;
            this.refreshResources();
        }

        long j = System.nanoTime();
        this.mcProfiler.startSection("tick");

        for (int i = 0; i < this.timer.elapsedTicks; ++i) {
            this.runTick();
        }

        this.mcProfiler.endStartSection("preRenderErrors");
        long k = System.nanoTime() - j;
        this.checkGLError("Pre render");
        RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GL11.glPushMatrix();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
            this.gameSettings.thirdPersonView = 0;
        }

        this.mcProfiler.endSection();

        if (!this.skipRenderWorld) {
            FMLCommonHandler.instance()
                .onRenderTickStart(this.timer.renderPartialTicks);
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
            this.mcProfiler.endSection();
            FMLCommonHandler.instance()
                .onRenderTickEnd(this.timer.renderPartialTicks);
        }

        GL11.glFlush();
        this.mcProfiler.endSection();

        if (!Display.isActive() && this.fullscreen) {
            this.toggleFullscreen();
        }

        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }

            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(k);
        } else {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }

        this.guiAchievement.func_146254_a();
        this.framebufferMc.unbindFramebuffer();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.entityRenderer.func_152430_c(this.timer.renderPartialTicks);
        GL11.glPopMatrix();
        this.mcProfiler.startSection("root");
        this.func_147120_f();
        Thread.yield();
        this.mcProfiler.startSection("stream");
        this.mcProfiler.startSection("update");
        this.field_152353_at.func_152935_j();
        this.mcProfiler.endStartSection("submit");
        this.field_152353_at.func_152922_k();
        this.mcProfiler.endSection();
        this.mcProfiler.endSection();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        this.isGamePaused = this.isSingleplayer() && this.currentScreen != null
            && this.currentScreen.doesGuiPauseGame()
            && !this.theIntegratedServer.getPublic();

        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter;
            this.debug = debugFPS + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
            WorldRenderer.chunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();

            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }

        this.mcProfiler.endSection();

        if (this.isFramerateLimitBelowMax()) {
            Display.sync(this.getLimitFramerate());
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void freeMemory() {
        memoryReserve = new byte[0];
        this.renderGlobal.deleteAllDisplayLists();
    }

    @Shadow
    public boolean isFramerateLimitBelowMax() {
        return (float) this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    @Shadow
    public int getLimitFramerate() {
        return this.theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash) {
        theCrash.getCategory()
            .addCrashSectionCallable("Launched Version", () -> launchedVersion);
        theCrash.getCategory()
            .addCrashSectionCallable("LWJGL", Sys::getVersion);
        theCrash.getCategory()
            .addCrashSectionCallable(
                "OpenGL",
                () -> GL11.glGetString(GL11.GL_RENDERER) + " GL version "
                    + GL11.glGetString(GL11.GL_VERSION)
                    + ", "
                    + GL11.glGetString(GL11.GL_VENDOR));
        theCrash.getCategory()
            .addCrashSectionCallable("GL Caps", OpenGlHelper::func_153172_c);
        theCrash.getCategory()
            .addCrashSectionCallable("Is Modded", () -> {
                String s = ClientBrandRetriever.getClientModName();
                return !s.equals("vanilla") ? "Definitely; Client brand changed to '" + s + "'"
                    : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated"
                        : "Probably not. Jar signature remains and client brand is untouched.");
            });
        theCrash.getCategory()
            .addCrashSectionCallable("Type", () -> "Client (map_client.txt)");
        theCrash.getCategory()
            .addCrashSectionCallable("Resource Packs", () -> gameSettings.resourcePacks.toString());
        theCrash.getCategory()
            .addCrashSectionCallable(
                "Current Language",
                () -> mcLanguageManager.getCurrentLanguage()
                    .toString());
        theCrash.getCategory()
            .addCrashSectionCallable(
                "Profiler Position",
                () -> mcProfiler.profilingEnabled ? mcProfiler.getNameOfLastSection() : "N/A (disabled)");
        theCrash.getCategory()
            .addCrashSectionCallable("Vec3 Pool Size", () -> {
                byte b0 = 0;
                int i = 56 * b0;
                int j = i / 1024 / 1024;
                byte b1 = 0;
                int k = 56 * b1;
                int l = k / 1024 / 1024;
                return b0 + " ("
                    + i
                    + " bytes; "
                    + j
                    + " MB) allocated, "
                    + b1
                    + " ("
                    + k
                    + " bytes; "
                    + l
                    + " MB) used";
            });
        theCrash.getCategory()
            .addCrashSectionCallable("Anisotropic Filtering", new Callable() {

                public String func_152388_a() {
                    return gameSettings.anisotropicFiltering == 1 ? "Off (1)"
                        : "On (" + gameSettings.anisotropicFiltering + ")";
                }

                public Object call() {
                    return this.func_152388_a();
                }
            });

        if (this.theWorld != null) {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }

        return theCrash;
    }

    @Shadow
    public void displayCrashReport(CrashReport crashReportIn) {
        File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
        File file2 = new File(
            file1,
            "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        System.out.println(crashReportIn.getCompleteReport());

        int retVal;
        if (crashReportIn.getFile() != null) {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            retVal = -1;
        } else if (crashReportIn.saveToFile(file2)) {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            retVal = -1;
        } else {
            System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            retVal = -2;
        }
        FMLCommonHandler.instance()
            .handleExit(retVal);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void runTick() {
        this.mcProfiler.startSection("scheduledExecutables");

        synchronized (this.field_152351_aB) {
            while (!this.field_152351_aB.isEmpty()) {
                ((FutureTask<?>) this.field_152351_aB.poll()).run();
            }
        }

        this.mcProfiler.endSection();

        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        FMLCommonHandler.instance()
            .onPreClientTick();

        this.mcProfiler.startSection("gui");

        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }

        this.mcProfiler.endStartSection("pick");
        this.entityRenderer.getMouseOver(1.0F);
        this.mcProfiler.endStartSection("gameMode");

        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }

        this.mcProfiler.endStartSection("textures");

        if (!this.isGamePaused) {
            this.renderEngine.tick();
        }

        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.getHealth() <= 0.0F) {
                this.displayGuiScreen(null);
            } else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }

        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }

        CrashReport crashreport;
        CrashReportCategory crashreportcategory;

        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            } catch (Throwable throwable1) {
                crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable(
                    "Screen name",
                    () -> currentScreen.getClass()
                        .getCanonicalName());
                throw new ReportedException(crashreport);
            }

            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                } catch (Throwable throwable) {
                    crashreport = CrashReport.makeCrashReport(throwable, "Ticking screen");
                    crashreportcategory = crashreport.makeCategory("Affected screen");
                    crashreportcategory.addCrashSectionCallable(
                        "Screen name",
                        () -> currentScreen.getClass()
                            .getCanonicalName());
                    throw new ReportedException(crashreport);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.mcProfiler.endStartSection("mouse");
            int j;

            while (Mouse.next()) {
                if (net.minecraftforge.client.ForgeHooksClient.postMouseEvent()) continue;

                j = Mouse.getEventButton();
                KeyBinding.setKeyBindState(j - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState()) {
                    KeyBinding.onTick(j - 100);
                }

                long k = getSystemTime() - this.systemTime;

                if (k <= 200L) {
                    int i = Mouse.getEventDWheel();

                    if (i != 0) {
                        this.thePlayer.inventory.changeCurrentItem(i);

                        if (this.gameSettings.noclip) {
                            if (i > 0) {
                                i = 1;
                            }

                            if (i < 0) {
                                i = -1;
                            }

                            this.gameSettings.noclipRate += i * 0.25F;
                        }
                    }

                    if (this.currentScreen == null) {
                        if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                            this.setIngameFocus();
                        }
                    } else {
                        this.currentScreen.handleMouseInput();
                    }
                }
                FMLCommonHandler.instance()
                    .fireMouseInput();
            }

            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }

            this.mcProfiler.endStartSection("keyboard");
            boolean flag;

            while (Keyboard.next()) {
                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if (Keyboard.getEventKeyState()) {
                    KeyBinding.onTick(Keyboard.getEventKey());
                }

                if (this.field_83002_am > 0L) {
                    if (getSystemTime() - this.field_83002_am >= 6000L) {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }

                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                        this.field_83002_am = -1L;
                    }
                } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                    this.field_83002_am = getSystemTime();
                }

                this.func_152348_aa();

                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == 62 && this.entityRenderer != null) {
                        this.entityRenderer.deactivateShader();
                    }

                    if (this.currentScreen != null) {
                        this.currentScreen.handleKeyboardInput();
                    } else {
                        if (Keyboard.getEventKey() == 1) {
                            this.displayInGameMenu();
                        }

                        if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }

                        if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }

                        if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
                            flag = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, flag ? -1 : 1);
                        }

                        if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
                            this.renderGlobal.loadRenderers();
                        }

                        if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                            this.gameSettings.saveOptions();
                        }

                        if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61)) {
                            RenderManager.debugBoundingBox = !RenderManager.debugBoundingBox;
                        }

                        if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                            this.gameSettings.saveOptions();
                        }

                        if (Keyboard.getEventKey() == 59) {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }

                        if (Keyboard.getEventKey() == 61) {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                        }

                        if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
                            ++this.gameSettings.thirdPersonView;

                            if (this.gameSettings.thirdPersonView > 2) {
                                this.gameSettings.thirdPersonView = 0;
                            }
                        }

                        if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }

                    if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
                        if (Keyboard.getEventKey() == 11) {
                            this.updateDebugProfilerName(0);
                        }

                        for (j = 0; j < 9; ++j) {
                            if (Keyboard.getEventKey() == 2 + j) {
                                this.updateDebugProfilerName(j + 1);
                            }
                        }
                    }
                }
                FMLCommonHandler.instance()
                    .fireKeyInput();
            }

            for (j = 0; j < 9; ++j) {
                if (this.gameSettings.keyBindsHotbar[j].isPressed()) {
                    this.thePlayer.inventory.currentItem = j;
                }
            }

            flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

            while (this.gameSettings.keyBindInventory.isPressed()) {
                if (this.playerController.func_110738_j()) {
                    this.thePlayer.func_110322_i();
                } else {
                    this.getNetHandler()
                        .addToSendQueue(
                            new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(this.thePlayer));
                }
            }

            while (this.gameSettings.keyBindDrop.isPressed()) {
                this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
            }

            while (this.gameSettings.keyBindChat.isPressed() && flag) {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag) {
                this.displayGuiScreen(new GuiChat("/"));
            }

            if (this.thePlayer.isUsingItem()) {
                if (!this.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
                }

                label391:

                while (true) {
                    if (!this.gameSettings.keyBindAttack.isPressed()) {
                        while (this.gameSettings.keyBindUseItem.isPressed()) {}

                        while (true) {
                            if (this.gameSettings.keyBindPickBlock.isPressed()) {
                                continue;
                            }

                            break label391;
                        }
                    }
                }
            } else {
                while (this.gameSettings.keyBindAttack.isPressed()) {
                    this.func_147116_af();
                }

                while (this.gameSettings.keyBindUseItem.isPressed()) {
                    this.func_147121_ag();
                }

                while (this.gameSettings.keyBindPickBlock.isPressed()) {
                    this.func_147112_ai();
                }
            }

            if (this.gameSettings.keyBindUseItem.getIsKeyPressed() && this.rightClickDelayTimer == 0
                && !this.thePlayer.isUsingItem()) {
                this.func_147121_ag();
            }

            this.func_147115_a(
                this.currentScreen == null && this.gameSettings.keyBindAttack.getIsKeyPressed() && this.inGameHasFocus);
        }

        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            this.mcProfiler.endStartSection("gameRenderer");

            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            this.mcProfiler.endStartSection("levelRenderer");

            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            this.mcProfiler.endStartSection("level");

            if (!this.isGamePaused) {
                if (this.theWorld.lastLightningBolt > 0) {
                    --this.theWorld.lastLightningBolt;
                }

                this.theWorld.updateEntities();
            }
        }

        if (!this.isGamePaused) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }

        if (this.theWorld != null) {
            if (!this.isGamePaused) {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.difficultySetting != EnumDifficulty.PEACEFUL, true);

                try {
                    this.theWorld.tick();
                } catch (Throwable throwable2) {
                    crashreport = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (this.theWorld == null) {
                        crashreportcategory = crashreport.makeCategory("Affected level");
                        crashreportcategory.addCrashSection("Problem", "Level is null!");
                    } else {
                        this.theWorld.addWorldInfoToCrashReport(crashreport);
                    }

                    throw new ReportedException(crashreport);
                }
            }

            this.mcProfiler.endStartSection("animateTick");

            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.doVoidFogParticles(
                    MathHelper.floor_double(this.thePlayer.posX),
                    MathHelper.floor_double(this.thePlayer.posY),
                    MathHelper.floor_double(this.thePlayer.posZ));
            }

            this.mcProfiler.endStartSection("particles");

            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        } else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }

        FMLCommonHandler.instance()
            .onPostClientTick();

        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }

    @Shadow
    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }

    @Shadow
    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());

            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void displayDebugInfo(long elapsedTicksTime) {
        if (this.mcProfiler.profilingEnabled) {
            List list = this.mcProfiler.getProfilingData(this.debugProfilerName);
            Profiler.Result result = (Profiler.Result) list.remove(0);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, this.displayWidth, this.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator tessellator = Tessellator.instance;
            short short1 = 160;
            int j = this.displayWidth - short1 - 10;
            int k = this.displayHeight - short1 * 2;
            GL11.glEnable(GL11.GL_BLEND);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 200);
            tessellator.addVertex((j - short1 * 1.1F), (k - short1 * 0.6F - 16.0F), 0.0D);
            tessellator.addVertex((j - short1 * 1.1F), (k + short1 * 2), 0.0D);
            tessellator.addVertex((j + short1 * 1.1F), (k + short1 * 2), 0.0D);
            tessellator.addVertex((j + short1 * 1.1F), (k - short1 * 0.6F - 16.0F), 0.0D);
            tessellator.draw();
            GL11.glDisable(GL11.GL_BLEND);
            double d0 = 0.0D;
            int i1;

            for (Object o : list) {
                Profiler.Result result1 = (Profiler.Result) o;
                i1 = MathHelper.floor_double(result1.field_76332_a / 4.0D) + 1;
                tessellator.startDrawing(6);
                tessellator.setColorOpaque_I(result1.func_76329_a());
                tessellator.addVertex(j, k, 0.0D);
                int j1;
                float f;
                float f1;
                float f2;

                for (j1 = i1; j1 >= 0; --j1) {
                    f = (float) ((d0 + result1.field_76332_a * (double) j1 / (double) i1) * Math.PI * 2.0D / 100.0D);
                    f1 = MathHelper.sin(f) * (float) short1;
                    f2 = MathHelper.cos(f) * (float) short1 * 0.5F;
                    tessellator.addVertex(j + f1, k - f2, 0.0D);
                }

                tessellator.draw();
                tessellator.startDrawing(5);
                tessellator.setColorOpaque_I((result1.func_76329_a() & 16711422) >> 1);

                for (j1 = i1; j1 >= 0; --j1) {
                    f = (float) ((d0 + result1.field_76332_a * (double) j1 / (double) i1) * Math.PI * 2.0D / 100.0D);
                    f1 = MathHelper.sin(f) * (float) short1;
                    f2 = MathHelper.cos(f) * (float) short1 * 0.5F;
                    tessellator.addVertex(j + f1, k - f2, 0.0D);
                    tessellator.addVertex(j + f1, k - f2 + 10.0F, 0.0D);
                }

                tessellator.draw();
                d0 += result1.field_76332_a;
            }

            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            String s = "";

            if (!result.field_76331_c.equals("unspecified")) {
                s = s + "[0] ";
            }

            if (result.field_76331_c.isEmpty()) {
                s = s + "ROOT ";
            } else {
                s = s + result.field_76331_c + " ";
            }

            i1 = 16777215;
            this.fontRenderer.drawStringWithShadow(s, j - short1, k - short1 / 2 - 16, i1);
            this.fontRenderer.drawStringWithShadow(
                s = decimalformat.format(result.field_76330_b) + "%",
                j + short1 - this.fontRenderer.getStringWidth(s),
                k - short1 / 2 - 16,
                i1);

            for (int k1 = 0; k1 < list.size(); ++k1) {
                Profiler.Result result2 = (Profiler.Result) list.get(k1);
                String s1 = "";

                if (result2.field_76331_c.equals("unspecified")) {
                    s1 = s1 + "[?] ";
                } else {
                    s1 = s1 + "[" + (k1 + 1) + "] ";
                }

                s1 = s1 + result2.field_76331_c;
                this.fontRenderer
                    .drawStringWithShadow(s1, j - short1, k + short1 / 2 + k1 * 8 + 20, result2.func_76329_a());
                this.fontRenderer.drawStringWithShadow(
                    s1 = decimalformat.format(result2.field_76332_a) + "%",
                    j + short1 - 50 - this.fontRenderer.getStringWidth(s1),
                    k + short1 / 2 + k1 * 8 + 20,
                    result2.func_76329_a());
                this.fontRenderer.drawStringWithShadow(
                    s1 = decimalformat.format(result2.field_76330_b) + "%",
                    j + short1 - this.fontRenderer.getStringWidth(s1),
                    k + short1 / 2 + k1 * 8 + 20,
                    result2.func_76329_a());
            }
        }
    }

    @Shadow
    public void refreshResources() {
        ArrayList arraylist = Lists.newArrayList(this.defaultResourcePacks);

        for (Object o : this.mcResourcePackRepository.getRepositoryEntries()) {
            ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) o;
            arraylist.add(entry.getResourcePack());
        }

        if (this.mcResourcePackRepository.func_148530_e() != null) {
            arraylist.add(this.mcResourcePackRepository.func_148530_e());
        }

        try {
            this.mcResourceManager.reloadResources(arraylist);
        } catch (RuntimeException runtimeexception) {
            logger.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
            arraylist.clear();
            arraylist.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.func_148527_a(Collections.emptyList());
            this.mcResourceManager.reloadResources(arraylist);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.saveOptions();
        }

        this.mcLanguageManager.parseLanguageMetadata(arraylist);

        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }

    @Shadow
    private void updateDebugProfilerName(int keyCount) {
        List list = this.mcProfiler.getProfilingData(this.debugProfilerName);

        if (list != null && !list.isEmpty()) {
            Profiler.Result result = (Profiler.Result) list.remove(0);

            if (keyCount == 0) {
                if (result.field_76331_c.length() > 0) {
                    int j = this.debugProfilerName.lastIndexOf(".");

                    if (j >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, j);
                    }
                }
            } else {
                --keyCount;

                if (keyCount < list.size()
                    && !((Profiler.Result) list.get(keyCount)).field_76331_c.equals("unspecified")) {
                    if (!this.debugProfilerName.isEmpty()) {
                        this.debugProfilerName = this.debugProfilerName + ".";
                    }

                    this.debugProfilerName = this.debugProfilerName
                        + ((Profiler.Result) list.get(keyCount)).field_76331_c;
                }
            }
        }
    }

    @Shadow
    private void func_147121_ag() {
        this.rightClickDelayTimer = 4;
        boolean flag = true;
        ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();

        if (this.objectMouseOver == null) {
            logger.warn("Null returned as 'hitResult', this shouldn't happen!");
        } else {
            switch (Classers.SwitchMovingObjectType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()]) {
                case 1:
                    if (this.playerController
                        .interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit)) {
                        flag = false;
                    }

                    break;
                case 2:
                    int i = this.objectMouseOver.blockX;
                    int j = this.objectMouseOver.blockY;
                    int k = this.objectMouseOver.blockZ;

                    if (!this.theWorld.getBlock(i, j, k)
                        .isAir(theWorld, i, j, k)) {
                        int l = itemstack != null ? itemstack.stackSize : 0;

                        boolean result = !net.minecraftforge.event.ForgeEventFactory
                            .onPlayerInteract(
                                thePlayer,
                                net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK,
                                i,
                                j,
                                k,
                                this.objectMouseOver.sideHit,
                                this.theWorld)
                            .isCanceled();
                        if (result && this.playerController.onPlayerRightClick(
                            this.thePlayer,
                            this.theWorld,
                            itemstack,
                            i,
                            j,
                            k,
                            this.objectMouseOver.sideHit,
                            this.objectMouseOver.hitVec)) {
                            flag = false;
                            this.thePlayer.swingItem();
                        }

                        if (itemstack == null) {
                            return;
                        }

                        if (itemstack.stackSize == 0) {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        } else if (itemstack.stackSize != l || this.playerController.isInCreativeMode()) {
                            this.entityRenderer.itemRenderer.resetEquippedProgress();
                        }
                    }
            }
        }

        if (flag) {
            ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();

            boolean result = !net.minecraftforge.event.ForgeEventFactory
                .onPlayerInteract(
                    thePlayer,
                    net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_AIR,
                    0,
                    0,
                    0,
                    -1,
                    this.theWorld)
                .isCanceled();
            if (result && itemstack1 != null
                && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1)) {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    @Shadow
    private void func_147116_af() {
        if (this.leftClickCounter <= 0) {
            this.thePlayer.swingItem();

            if (this.objectMouseOver == null) {
                logger.error("Null returned as 'hitResult', this shouldn't happen!");

                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            } else {
                switch (Classers.SwitchMovingObjectType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()]) {
                    case 1:
                        this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                        break;
                    case 2:
                        int i = this.objectMouseOver.blockX;
                        int j = this.objectMouseOver.blockY;
                        int k = this.objectMouseOver.blockZ;

                        if (this.theWorld.getBlock(i, j, k)
                            .getMaterial() == Material.air) {
                            if (this.playerController.isNotCreative()) {
                                this.leftClickCounter = 10;
                            }
                        } else {
                            this.playerController.clickBlock(i, j, k, this.objectMouseOver.sideHit);
                        }
                }
            }
        }
    }

    @Shadow
    private void func_147112_ai() {
        if (this.objectMouseOver != null) {
            boolean flag = this.thePlayer.capabilities.isCreativeMode;
            int j;

            if (!net.minecraftforge.common.ForgeHooks.onPickBlock(this.objectMouseOver, this.thePlayer, this.theWorld))
                return;
            // We delete this code wholly instead of commenting it out, to make sure we detect changes in it between MC
            // versions
            if (flag) {
                j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + this.thePlayer.inventory.currentItem;
                this.playerController
                    .sendSlotPacket(this.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem), j);
            }
        }
    }

    @Shadow
    private void func_147115_a(boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0) {
            if (leftClick && this.objectMouseOver != null
                && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = this.objectMouseOver.blockX;
                int j = this.objectMouseOver.blockY;
                int k = this.objectMouseOver.blockZ;

                if (this.theWorld.getBlock(i, j, k)
                    .getMaterial() != Material.air) {
                    this.playerController.onPlayerDamageBlock(i, j, k, this.objectMouseOver.sideHit);

                    if (this.thePlayer.isCurrentToolAdventureModeExempt(i, j, k)) {
                        this.effectRenderer.addBlockHitEffects(i, j, k, this.objectMouseOver);
                        this.thePlayer.swingItem();
                    }
                }
            } else {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    @Shadow
    public void shutdown() {
        this.running = false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_152348_aa() {
        int i = Keyboard.getEventKey();

        if (i != 0 && !Keyboard.isRepeatEvent()
            && (!(this.currentScreen instanceof GuiControls)
                || ((GuiControls) this.currentScreen).field_152177_g <= getSystemTime() - 20L)) {
            if (Keyboard.getEventKeyState()) {
                if (i == this.gameSettings.field_152396_an.getKeyCode()) {
                    if (this.func_152346_Z()
                        .func_152934_n()) {
                        this.func_152346_Z()
                            .func_152914_u();
                    } else if (this.func_152346_Z()
                        .func_152924_m()) {
                            this.displayGuiScreen(new GuiYesNo((result, id) -> {
                                if (result) {
                                    func_152346_Z().func_152930_t();
                                }

                                displayGuiScreen(null);
                            }, I18n.format("stream.confirm_start"), "", 0));
                        } else if (this.func_152346_Z()
                            .func_152928_D()
                            && this.func_152346_Z()
                                .func_152936_l()) {
                                    if (this.theWorld != null) {
                                        this.ingameGUI.getChatGUI()
                                            .printChatMessage(
                                                new ChatComponentText("Not ready to start streaming yet!"));
                                    }
                                } else {
                                    GuiStreamUnavailable.func_152321_a(this.currentScreen);
                                }
                } else if (i == this.gameSettings.field_152397_ao.getKeyCode()) {
                    if (this.func_152346_Z()
                        .func_152934_n()) {
                        if (this.func_152346_Z()
                            .func_152919_o()) {
                            this.func_152346_Z()
                                .func_152933_r();
                        } else {
                            this.func_152346_Z()
                                .func_152916_q();
                        }
                    }
                } else if (i == this.gameSettings.field_152398_ap.getKeyCode()) {
                    if (this.func_152346_Z()
                        .func_152934_n()) {
                        this.func_152346_Z()
                            .func_152931_p();
                    }
                } else if (i == this.gameSettings.field_152399_aq.getKeyCode()) {
                    this.field_152353_at.func_152910_a(true);
                } else if (i == this.gameSettings.field_152395_am.getKeyCode()) {
                    this.toggleFullscreen();
                } else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                    this.ingameGUI.getChatGUI()
                        .printChatMessage(
                            ScreenShotHelper.saveScreenshot(
                                this.mcDataDir,
                                this.displayWidth,
                                this.displayHeight,
                                this.framebufferMc));
                }
            } else if (i == this.gameSettings.field_152399_aq.getKeyCode()) {
                this.field_152353_at.func_152910_a(false);
            }

        }
    }

    @Shadow
    public IStream func_152346_Z() {
        return this.field_152353_at;
    }
}
