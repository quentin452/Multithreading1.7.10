package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.io.*;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.*;
import net.minecraft.util.ReportedException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPOutputStream2;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla.CompressTask;
/* @TODO FIX
[16:19:11] [Client thread/ERROR] [FML/]: FMLIndexedMessageCodec exception caught
io.netty.handler.codec.DecoderException: java.lang.RuntimeException: java.io.EOFException
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:99) ~[netty-all-4.0.10.Final.jar:?]
	at io.netty.handler.codec.MessageToMessageCodec.channelRead(MessageToMessageCodec.java:111) ~[netty-all-4.0.10.Final.jar:?]
	at io.netty.channel.DefaultChannelHandlerContext.invokeChannelRead(DefaultChannelHandlerContext.java:337) [netty-all-4.0.10.Final.jar:?]
	at io.netty.channel.DefaultChannelHandlerContext.fireChannelRead(DefaultChannelHandlerContext.java:323) [netty-all-4.0.10.Final.jar:?]
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:785) [netty-all-4.0.10.Final.jar:?]
	at io.netty.channel.embedded.EmbeddedChannel.writeInbound(EmbeddedChannel.java:169) [netty-all-4.0.10.Final.jar:?]
	at cpw.mods.fml.common.network.internal.FMLProxyPacket.func_148833_a(FMLProxyPacket.java:77) [FMLProxyPacket.class:?]
	at net.minecraft.network.NetworkManager.func_74428_b(NetworkManager.java:212) [ej.class:?]
	at net.minecraft.client.multiplayer.PlayerControllerMP.func_78765_e(PlayerControllerMP.java:273) [bje.class:?]
	at net.minecraft.client.Minecraft.func_71407_l(Minecraft.java:1602) [bao.class:?]
	at net.minecraft.client.Minecraft.func_71411_J(Minecraft.java:973) [bao.class:?]
	at net.minecraft.client.Minecraft.func_99999_d(Minecraft.java:9006) [bao.class:?]
	at net.minecraft.client.main.Main.main(SourceFile:148) [Main.class:?]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_432]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_432]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_432]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_432]
	at net.minecraft.launchwrapper.Launch.launch(Launch.java:135) [launchwrapper-1.12.jar:?]
	at net.minecraft.launchwrapper.Launch.main(Launch.java:28) [launchwrapper-1.12.jar:?]
Caused by: java.lang.RuntimeException: java.io.EOFException
	at com.google.common.base.Throwables.propagate(Throwables.java:160) ~[guava-17.0.jar:?]
	at cpw.mods.fml.common.network.ByteBufUtils.readTag(ByteBufUtils.java:210) ~[ByteBufUtils.class:?]
	at tragicneko.tragicmc.network.MessageAmulet.fromBytes(MessageAmulet.java:25) ~[MessageAmulet.class:?]
	at cpw.mods.fml.common.network.simpleimpl.SimpleIndexedCodec.decodeInto(SimpleIndexedCodec.java:17) ~[SimpleIndexedCodec.class:?]
	at cpw.mods.fml.common.network.simpleimpl.SimpleIndexedCodec.decodeInto(SimpleIndexedCodec.java:7) ~[SimpleIndexedCodec.class:?]
	at cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec.decode(FMLIndexedMessageToMessageCodec.java:77) ~[FMLIndexedMessageToMessageCodec.class:?]
	at cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec.decode(FMLIndexedMessageToMessageCodec.java:17) ~[FMLIndexedMessageToMessageCodec.class:?]
	at io.netty.handler.codec.MessageToMessageCodec$2.decode(MessageToMessageCodec.java:81) ~[netty-all-4.0.10.Final.jar:?]
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:89) ~[netty-all-4.0.10.Final.jar:?]
	... 18 more
Caused by: java.io.EOFException
	at fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2.readUByte(GZIPInputStream2.java:231) ~[GZIPInputStream2.class:?]
	at fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2.readUShort(GZIPInputStream2.java:221) ~[GZIPInputStream2.class:?]
	at fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2.readHeader(GZIPInputStream2.java:130) ~[GZIPInputStream2.class:?]
	at fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2.<init>(GZIPInputStream2.java:44) ~[GZIPInputStream2.class:?]
	at fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.resources.GZIPInputStream2.<init>(GZIPInputStream2.java:57) ~[GZIPInputStream2.class:?]
	at net.minecraft.nbt.CompressedStreamTools.func_152457_a(CompressedStreamTools.java:559) ~[du.class:?]
	at net.minecraft.network.PacketBuffer.func_150793_b(SourceFile:1463) ~[et.class:?]
	at cpw.mods.fml.common.network.ByteBufUtils.readTag(ByteBufUtils.java:206) ~[ByteBufUtils.class:?]
	at tragicneko.tragicmc.network.MessageAmulet.fromBytes(MessageAmulet.java:25) ~[MessageAmulet.class:?]
	at cpw.mods.fml.common.network.simpleimpl.SimpleIndexedCodec.decodeInto(SimpleIndexedCodec.java:17) ~[SimpleIndexedCodec.class:?]
	at cpw.mods.fml.common.network.simpleimpl.SimpleIndexedCodec.decodeInto(SimpleIndexedCodec.java:7) ~[SimpleIndexedCodec.class:?]
	at cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec.decode(FMLIndexedMessageToMessageCodec.java:77) ~[FMLIndexedMessageToMessageCodec.class:?]
	at cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec.decode(FMLIndexedMessageToMessageCodec.java:17) ~[FMLIndexedMessageToMessageCodec.class:?]
	at io.netty.handler.codec.MessageToMessageCodec$2.decode(MessageToMessageCodec.java:81) ~[netty-all-4.0.10.Final.jar:?]
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:89) ~[netty-all-4.0.10.Final.jar:?]
	... 18 more
 */
@Mixin(CompressedStreamTools.class)
public abstract class MixinCompressedStreamTools {

    /**
     * Load the gzipped compound from the inputstream.
     */
    @Overwrite
    public static NBTTagCompound readCompressed(InputStream p_74796_0_) throws IOException {
        DataInputStream datainputstream = new DataInputStream(
            new BufferedInputStream(new GZIPInputStream2(p_74796_0_)));
        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = func_152456_a(datainputstream, NBTSizeTracker.field_152451_a);
        } finally {
            datainputstream.close();
        }

        return nbttagcompound;
    }

    /**
     * Write the compound, gzipped, to the outputstream.
     */
    @Overwrite
    public static void writeCompressed(NBTTagCompound p_74799_0_, OutputStream p_74799_1_) throws IOException {

        try (DataOutputStream dataoutputstream = new DataOutputStream(
            new BufferedOutputStream(new GZIPOutputStream2(p_74799_1_)))) {
            write(p_74799_0_, dataoutputstream);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static NBTTagCompound func_152457_a(byte[] p_152457_0_, NBTSizeTracker p_152457_1_) throws IOException {
        DataInputStream datainputstream = new DataInputStream(
            new BufferedInputStream(new GZIPInputStream2(new ByteArrayInputStream(p_152457_0_))));
        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = func_152456_a(datainputstream, p_152457_1_);
        } finally {
            datainputstream.close();
        }

        return nbttagcompound;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static byte[] compress(NBTTagCompound p_74798_0_) {
        CompressTask compressTask = new CompressTask(p_74798_0_);
        CompletableFuture<byte[]> future = compressTask.compressAsync();

        try {
            return future.join();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void safeWrite(NBTTagCompound p_74793_0_, File p_74793_1_) throws IOException {
        File file2 = new File(p_74793_1_.getAbsolutePath() + "_tmp");

        if (file2.exists()) {
            file2.delete();
        }

        write(p_74793_0_, file2);

        if (p_74793_1_.exists()) {
            p_74793_1_.delete();
        }

        if (p_74793_1_.exists()) {
            throw new IOException("Failed to delete " + p_74793_1_);
        } else {
            file2.renameTo(p_74793_1_);
        }
    }

    /**
     * Reads from a CompressedStream.
     */
    @Overwrite
    public static NBTTagCompound read(DataInputStream p_74794_0_) throws IOException {
        return func_152456_a(p_74794_0_, NBTSizeTracker.field_152451_a);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static NBTTagCompound func_152456_a(DataInput dataInput, NBTSizeTracker nbtSizeTracker) throws IOException {
        NBTBase nbtbase = func_152455_a(dataInput, 0, nbtSizeTracker);

        if (nbtbase instanceof NBTTagCompound) {
            return (NBTTagCompound) nbtbase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    @Shadow
    private static NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_)
        throws IOException {
        try {
            Method func_150284_a = NBTBase.class.getDeclaredMethod("func_150284_a", byte.class);
            Method func_152446_a = NBTBase.class
                .getDeclaredMethod("func_152446_a", DataInput.class, int.class, NBTSizeTracker.class);

            byte b0 = p_152455_0_.readByte();
            p_152455_2_.func_152450_a(8);

            if (b0 == 0) {
                return new NBTTagEnd();
            } else {
                NBTSizeTracker.readUTF(p_152455_2_, p_152455_0_.readUTF());
                p_152455_2_.func_152450_a(32);

                NBTBase nbtbase = (NBTBase) func_150284_a.invoke(null, b0);

                try {
                    func_152446_a.invoke(nbtbase, p_152455_0_, p_152455_1_, p_152455_2_);
                    return nbtbase;
                } catch (Exception exception) {
                    CrashReport crashreport = CrashReport.makeCrashReport(exception, "Loading NBT data");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
                    crashreportcategory.addCrashSection("Tag name", "[UNNAMED TAG]");
                    crashreportcategory.addCrashSection("Tag type", b0);
                    throw new ReportedException(crashreport);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void write(NBTTagCompound nbtTagCompound, DataOutput dataOutput) throws IOException {
        func_150663_a(nbtTagCompound, dataOutput);
    }

    @Shadow
    private static void func_150663_a(NBTBase p_150663_0_, DataOutput p_150663_1_) throws IOException {
        try {
            Method writeMethod = NBTBase.class.getDeclaredMethod("write", DataOutput.class);

            p_150663_1_.writeByte(p_150663_0_.getId());

            if (p_150663_0_.getId() != 0) {
                p_150663_1_.writeUTF("");

                writeMethod.invoke(p_150663_0_, p_150663_1_);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void write(NBTTagCompound p_74795_0_, File p_74795_1_) throws IOException {
        try (DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(p_74795_1_))) {
            write(p_74795_0_, dataoutputstream);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static NBTTagCompound read(File p_74797_0_) throws IOException {
        return func_152458_a(p_74797_0_, NBTSizeTracker.field_152451_a);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static NBTTagCompound func_152458_a(File p_152458_0_, NBTSizeTracker p_152458_1_) throws IOException {
        if (!p_152458_0_.exists()) {
            return null;
        } else {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(p_152458_0_));
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = func_152456_a(datainputstream, p_152458_1_);
            } finally {
                datainputstream.close();
            }

            return nbttagcompound;
        }
    }
}
