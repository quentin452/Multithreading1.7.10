package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SaveHandler.class)
public abstract class MixinSaveHandler implements ISaveHandler, IPlayerFileData {

    @Shadow
    private final File worldDirectory;
    @Shadow
    private final long initializationTime = MinecraftServer.getSystemTimeMillis();

    public MixinSaveHandler(File p_i2146_1_, String p_i2146_2_, boolean p_i2146_3_) {
        this.worldDirectory = new File(p_i2146_1_, p_i2146_2_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void checkSessionLock() throws MinecraftException {
        try {
            File file1 = new File(this.worldDirectory, "session.lock");
            try (DataInputStream datainputstream = new DataInputStream(Files.newInputStream(file1.toPath()))) {
                if (datainputstream.readLong() != this.initializationTime) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
        } catch (IOException ioexception) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }
}
