package fr.iamacat.optimizationsandtweaks.mixins.common.xtracraft;

import com.themathe1.xtracraftMod.entity.mob.XCEntityGod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(XCEntityGod.class)
public abstract class MixinXCEntityGod extends EntityMob implements IBossDisplayData {
    @Shadow
    public static int evilState;
    @Shadow
    private static int spawnone;
    @Shadow
    private int igniteTime;
    @Shadow
    private int angerLevel = 0;
    @Shadow
    private int randomSoundDelay;
    @Shadow
    private int chargeTime;
    @Shadow
    private int fireRate;
    @Shadow
    private int tirNumber;
    @Shadow
    private int actionStop;
    @Shadow
    private int speedTime;
    @Shadow
    private int godState;
    @Shadow
    private int lastActiveTime;

    public MixinXCEntityGod(World p_i1738_1_) {
        super(p_i1738_1_);
    }
    @Unique
    @SideOnly(Side.CLIENT)
    private void optimizationsAndTweaks$disableBossStatusBar(){
        //BossStatus.setBossStatus(this, true);
    }
    /**
     * @author quentin452
     * @reason Fix crash caused by setBossStatus when spawning the God on Servers
     * @method onLivingUpdate
     */
    @Overwrite(remap = false)
    public void func_70636_d() {
        if (this.worldObj.isRemote) {
            optimizationsAndTweaks$disableBossStatusBar();
        }
        double d = this.rand.nextGaussian() * 0.02;
        double d1 = (double)10.0F;
        if (this.godState == 4 && this.igniteTime >= 60 && this.chargeTime >= 60 && evilState == 0) {
            for(int i = 0; i < 20; ++i) {
                this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            }
        }

        if (this.getHealth() <= 3000.0F && this.actionStop == 0 && (this.godState == 0 || this.godState == 6) && evilState == 0) {
            for(int i = 0; i < 20; ++i) {
                this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            }
        }

        if (this.godState == 9 && this.actionStop == 1 && evilState == 0) {
            this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
        }

        if (this.godState == 9 && this.actionStop == 1 && this.chargeTime >= 80 && evilState == 0) {
            this.worldObj.spawnParticle("explode", this.posX + (double)3.0F + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            this.worldObj.spawnParticle("explode", this.posX - (double)3.0F + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
        }

        if (this.getHealth() <= 1800.0F && this.actionStop == 3 && this.godState == 0 && evilState == 0) {
            for(int i = 0; i < 9; ++i) {
                this.worldObj.spawnParticle("largesmoke", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            }
        }

        if (this.getHealth() <= 1800.0F && this.actionStop == 3 && this.chargeTime >= 100 && evilState == 0) {
            for(int i = 0; i < 6; ++i) {
                this.worldObj.spawnParticle("largesmoke", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
                this.worldObj.spawnParticle("lava", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
                this.worldObj.spawnParticle("smoke", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
                this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
                this.worldObj.spawnParticle("flame", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            }
        }

        if (evilState == 1) {
            for(int i = 0; i < 3; ++i) {
                this.worldObj.spawnParticle("largesmoke", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, this.posY + (double)(this.rand.nextFloat() * this.height) - d * (double)10.0F, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * (double)10.0F, d, d, d);
            }
        }

        if (this.worldObj.isRemote) {
            this.readFly();
        } else {
            this.updateFly();
        }

        super.onLivingUpdate();
    }
    @Shadow
    private void readFly() {
        this.motionX = (double)this.dataWatcher.getWatchableObjectFloat(20);
        this.motionY = (double)this.dataWatcher.getWatchableObjectFloat(21);
        this.motionZ = (double)this.dataWatcher.getWatchableObjectFloat(22);
    }
    @Shadow
    private void updateFly() {
        this.dataWatcher.updateObject(20, (float)this.motionX);
        this.dataWatcher.updateObject(21, (float)this.motionY);
        this.dataWatcher.updateObject(22, (float)this.motionZ);
    }
}
//TODO FIX THIS CRASH WHEN ENABLING optimizationsAndTweaks$disableBossStatusBar
/*
java.lang.NoClassDefFoundError: com/themathe1/xtracraftMod/entity/mob/XCEntityGod
	at com.themathe1.xtracraftMod.item.XtraItems.loadItems(XtraItems.java:417) ~[XtraItems.class:?]
	at com.themathe1.xtracraftMod.XtraCraftMod.PreLoad(XtraCraftMod.java:94) ~[XtraCraftMod.class:?]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_432]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_432]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_432]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_432]
	at cpw.mods.fml.common.FMLModContainer.handleModStateEvent(FMLModContainer.java:532) ~[FMLModContainer.class:?]
	at sun.reflect.GeneratedMethodAccessor4.invoke(Unknown Source) ~[?:?]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_432]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_432]
	at com.google.common.eventbus.EventSubscriber.handleEvent(EventSubscriber.java:74) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.SynchronizedEventSubscriber.handleEvent(SynchronizedEventSubscriber.java:47) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.dispatch(EventBus.java:322) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.dispatchQueuedEvents(EventBus.java:304) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.post(EventBus.java:275) ~[guava-17.0.jar:?]
	at cpw.mods.fml.common.LoadController.sendEventToModContainer(LoadController.java:212) ~[LoadController.class:?]
	at cpw.mods.fml.common.LoadController.propogateStateMessage(LoadController.java:190) ~[LoadController.class:?]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_432]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_432]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_432]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_432]
	at com.google.common.eventbus.EventSubscriber.handleEvent(EventSubscriber.java:74) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.SynchronizedEventSubscriber.handleEvent(SynchronizedEventSubscriber.java:47) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.dispatch(EventBus.java:322) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.dispatchQueuedEvents(EventBus.java:304) ~[guava-17.0.jar:?]
	at com.google.common.eventbus.EventBus.post(EventBus.java:275) ~[guava-17.0.jar:?]
	at cpw.mods.fml.common.LoadController.distributeStateMessage(LoadController.java:119) ~[LoadController.class:?]
	at cpw.mods.fml.common.Loader.preinitializeMods(Loader.java:556) ~[Loader.class:?]
	at cpw.mods.fml.server.FMLServerHandler.beginServerLoading(FMLServerHandler.java:88) ~[FMLServerHandler.class:?]
	at cpw.mods.fml.common.FMLCommonHandler.onServerStart(FMLCommonHandler.java:314) ~[FMLCommonHandler.class:?]
	at net.minecraft.server.dedicated.DedicatedServer.func_71197_b(DedicatedServer.java:117) ~[lt.class:?]
	at net.minecraft.server.MinecraftServer.run(MinecraftServer.java:1797) ~[MinecraftServer.class:?]
	at java.lang.Thread.run(Thread.java:750) [?:1.8.0_432]
Caused by: java.lang.ClassNotFoundException: com.themathe1.xtracraftMod.entity.mob.XCEntityGod
	at net.minecraft.launchwrapper.LaunchClassLoader.findClass(LaunchClassLoader.java:191) ~[launchwrapper-1.12.jar:?]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:419) ~[?:1.8.0_432]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:352) ~[?:1.8.0_432]
	... 33 more
Caused by: org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError: An unexpected critical error was encountered
	at org.spongepowered.asm.mixin.transformer.MixinProcessor.applyMixins(MixinProcessor.java:392) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClass(MixinTransformer.java:234) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClassBytes(MixinTransformer.java:202) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.Proxy.transform(Proxy.java:72) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at net.minecraft.launchwrapper.LaunchClassLoader.runTransformers(LaunchClassLoader.java:279) ~[launchwrapper-1.12.jar:?]
	at net.minecraft.launchwrapper.LaunchClassLoader.findClass(LaunchClassLoader.java:176) ~[launchwrapper-1.12.jar:?]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:419) ~[?:1.8.0_432]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:352) ~[?:1.8.0_432]
	... 33 more
Caused by: org.spongepowered.asm.mixin.transformer.throwables.MixinPreProcessorException: Attach error for mixins.optimizationsandtweaks.json:common.xtracraft.MixinXCEntityGod from mod optimizationsandtweaks during activity: [Transform -> Method func_70636_d()V -> INVOKESTATIC -> net/minecraft/entity/boss/BossStatus::func_82824_a:(Lnet/minecraft/entity/boss/IBossDisplayData;Z)V]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.attach(MixinPreProcessorStandard.java:313) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.createContextFor(MixinPreProcessorStandard.java:277) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinInfo.createContextFor(MixinInfo.java:1292) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinApplicatorStandard.apply(MixinApplicatorStandard.java:203) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.apply(TargetClassContext.java:437) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.applyMixins(TargetClassContext.java:418) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinProcessor.applyMixins(MixinProcessor.java:363) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClass(MixinTransformer.java:234) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClassBytes(MixinTransformer.java:202) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.Proxy.transform(Proxy.java:72) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at net.minecraft.launchwrapper.LaunchClassLoader.runTransformers(LaunchClassLoader.java:279) ~[launchwrapper-1.12.jar:?]
	at net.minecraft.launchwrapper.LaunchClassLoader.findClass(LaunchClassLoader.java:176) ~[launchwrapper-1.12.jar:?]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:419) ~[?:1.8.0_432]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:352) ~[?:1.8.0_432]
	... 33 more
Caused by: java.lang.RuntimeException: java.lang.ClassNotFoundException: net.minecraft.entity.boss.BossStatus
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transformMemberReference(MixinPreProcessorStandard.java:791) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transformMethod(MixinPreProcessorStandard.java:777) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transform(MixinPreProcessorStandard.java:743) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.attach(MixinPreProcessorStandard.java:307) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.createContextFor(MixinPreProcessorStandard.java:277) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinInfo.createContextFor(MixinInfo.java:1292) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinApplicatorStandard.apply(MixinApplicatorStandard.java:203) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.apply(TargetClassContext.java:437) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.applyMixins(TargetClassContext.java:418) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinProcessor.applyMixins(MixinProcessor.java:363) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClass(MixinTransformer.java:234) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClassBytes(MixinTransformer.java:202) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.Proxy.transform(Proxy.java:72) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at net.minecraft.launchwrapper.LaunchClassLoader.runTransformers(LaunchClassLoader.java:279) ~[launchwrapper-1.12.jar:?]
	at net.minecraft.launchwrapper.LaunchClassLoader.findClass(LaunchClassLoader.java:176) ~[launchwrapper-1.12.jar:?]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:419) ~[?:1.8.0_432]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:352) ~[?:1.8.0_432]
	... 33 more
Caused by: java.lang.ClassNotFoundException: net.minecraft.entity.boss.BossStatus
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transformMemberReference(MixinPreProcessorStandard.java:791) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transformMethod(MixinPreProcessorStandard.java:777) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.transform(MixinPreProcessorStandard.java:743) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.attach(MixinPreProcessorStandard.java:307) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinPreProcessorStandard.createContextFor(MixinPreProcessorStandard.java:277) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinInfo.createContextFor(MixinInfo.java:1292) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinApplicatorStandard.apply(MixinApplicatorStandard.java:203) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.apply(TargetClassContext.java:437) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.TargetClassContext.applyMixins(TargetClassContext.java:418) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinProcessor.applyMixins(MixinProcessor.java:363) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClass(MixinTransformer.java:234) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.MixinTransformer.transformClassBytes(MixinTransformer.java:202) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at org.spongepowered.asm.mixin.transformer.Proxy.transform(Proxy.java:72) ~[+unimixins-all-1.7.10-0.1.19.jar:0.15.3+mixin.0.8.7]
	at net.minecraft.launchwrapper.LaunchClassLoader.runTransformers(LaunchClassLoader.java:279) ~[launchwrapper-1.12.jar:?]
	at net.minecraft.launchwrapper.LaunchClassLoader.findClass(LaunchClassLoader.java:176) ~[launchwrapper-1.12.jar:?]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:419) ~[?:1.8.0_432]
	at java.lang.ClassLoader.loadClass(ClassLoader.java:352) ~[?:1.8.0_432]
	... 33 more
 */
