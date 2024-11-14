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
