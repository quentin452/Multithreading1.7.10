package fr.iamacat.optimizationsandtweaks.mixins.common.minefactoryreloaded;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

// should fix spam logs like this with highlands,pam's harvestcraft,Growthcraft,akkamaddi.ashenwheat,thermal
// expansion/foundation,runicdungeons,tinkers construct,com.scottkillen.mod.dendrology ,extrautilities and maybe other
// with MINEFACTORY RELOADED
/*
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]:
 * java.lang.NoSuchMethodException: cpw.mods.fml.common.event.FMLInterModComms$IMCMessage.<init>(java.lang.String,
 * java.lang.Object)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.Class.getConstructor0(Class.java:3082)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.Class.getConstructor(Class.java:1825)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * powercrystals.minefactoryreloaded.api.FactoryRegistry.sendMessage(FactoryRegistry.java:101)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.pam.harvestcraft.PamMFRCompatibility.getRegistry(PamMFRCompatibility.java:172)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.pam.harvestcraft.harvestcraft.onPreInit(harvestcraft.java:99)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.reflect.Method.invoke(Method.java:498)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.common.FMLModContainer.handleModStateEvent(FMLModContainer.java:532)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.GeneratedMethodAccessor11.invoke(Unknown Source)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.reflect.Method.invoke(Method.java:498)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventSubscriber.handleEvent(EventSubscriber.java:74)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.SynchronizedEventSubscriber.handleEvent(SynchronizedEventSubscriber.java:47)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.dispatch(EventBus.java:322)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.dispatchQueuedEvents(EventBus.java:304)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.post(EventBus.java:275)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.common.LoadController.sendEventToModContainer(LoadController.java:212)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.common.LoadController.propogateStateMessage(LoadController.java:190)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.reflect.Method.invoke(Method.java:498)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventSubscriber.handleEvent(EventSubscriber.java:74)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.SynchronizedEventSubscriber.handleEvent(SynchronizedEventSubscriber.java:47)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.dispatch(EventBus.java:322)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.dispatchQueuedEvents(EventBus.java:304)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * com.google.common.eventbus.EventBus.post(EventBus.java:275)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.common.LoadController.distributeStateMessage(LoadController.java:119)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.common.Loader.preinitializeMods(Loader.java:556)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * cpw.mods.fml.client.FMLClientHandler.beginMinecraftLoading(FMLClientHandler.java:243)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * net.minecraft.client.Minecraft.func_71384_a(Minecraft.java:480)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * net.minecraft.client.Minecraft.func_99999_d(Minecraft.java:6493)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * net.minecraft.client.main.Main.main(SourceFile:148)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * java.lang.reflect.Method.invoke(Method.java:498)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * net.minecraft.launchwrapper.Launch.launch(Launch.java:135)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * net.minecraft.launchwrapper.Launch.main(Launch.java:28)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * org.prismlauncher.launcher.impl.StandardLauncher.launch(StandardLauncher.java:88)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * org.prismlauncher.EntryPoint.listen(EntryPoint.java:126)
 * [00:43:25] [Client thread/INFO] [STDERR/harvestcraft]: [java.lang.Throwable$WrappedPrintStream:println:749]: at
 * org.prismlauncher.EntryPoint.main(EntryPoint.java:71)
 */

@Mixin(FactoryRegistry.class)
public class MixinFixNoSuchMethodException {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void sendMessage(String message, Object value) {
        if (OptimizationsandTweaksConfig.enableMixinFixNoSuchMethodException) {
            try {
                Class.forName("powercrystals.minefactoryreloaded.api.FactoryRegistry");
            } catch (ClassNotFoundException e) {
                System.err.println("[OptimizationsAndTweaks] MFR not present, cannot override sendMessage");
                return; // MFR not present, exit early
            }
            if (!Loader.isModLoaded("minefactoryreloaded") || Loader.instance()
                .activeModContainer() == null) {
                return;
            }
            try {
                Method m = FMLInterModComms.class
                    .getDeclaredMethod("enqueueMessage", Object.class, String.class, FMLInterModComms.IMCMessage.class);
                m.setAccessible(true);
                Constructor<FMLInterModComms.IMCMessage> c = FMLInterModComms.IMCMessage.class
                    .getDeclaredConstructor(String.class, Object.class);
                c.setAccessible(true);
                m.invoke(
                    null,
                    Loader.instance()
                        .activeModContainer(),
                    "minefactoryreloaded",
                    c.newInstance(message, value));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.err.println("[OptimizationsAndTweaks] Method not found while invoking sendMessage using reflection" + e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.err.println("[OptimizationsAndTweaks] Illegal access while invoking sendMessage using reflection" + e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                System.err.println("[OptimizationsAndTweaks] Error invoking sendMessage using reflection" + e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        // Perform any necessary actions or fallbacks here
        FactoryRegistry.sendMessage(message, value);
    }
}
