package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.MapMaker;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.IEventExceptionHandler;
import cpw.mods.fml.common.eventhandler.IEventListener;

@Mixin(EventBus.class)
public abstract class MixinEventBus implements IEventExceptionHandler {

    @Shadow
    private static int maxID = 0;
    @Shadow
    private ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = new ConcurrentHashMap<>();
    @Shadow
    private Map<Object, ModContainer> listenerOwners = new MapMaker().weakKeys()
        .weakValues()
        .makeMap();
    @Shadow
    private final int busID = maxID++;
    @Shadow
    private IEventExceptionHandler exceptionHandler;
    /**
     * @author
     * @reason
     */
    // todo
    /*
     * @Overwrite(remap = false)
     * public synchronized boolean post(Event event) {
     * IEventListener[] listeners = event.getListenerList().getListeners(busID);
     * int index = 0;
     * try {
     * CompletableFuture<?>[] futures = new CompletableFuture<?>[listeners.length];
     * for (; index < listeners.length; index++) {
     * final int finalIndex = index;
     * futures[index] = CompletableFuture.runAsync(() -> listeners[finalIndex].invoke(event));
     * }
     * CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);
     * allFutures.join();
     * } catch (Throwable throwable) {
     * exceptionHandler.handleException((EventBus)(Object)this, event, listeners, index, throwable);
     * Throwables.propagate(throwable);
     * }
     * return (event.isCancelable() && event.isCanceled());
     * }
     */
}
