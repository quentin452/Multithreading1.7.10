package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.io.IOException;
import java.util.*;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ReportedException;

import org.apache.commons.lang3.ObjectUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.iamacat.optimizationsandtweaks.utils.concurrentlinkedhashmap.ConcurrentHashMapV8;

@Mixin(value = DataWatcher.class, priority = 999)
public class MixinDataWatcher {

    @Shadow
    private final Entity field_151511_a;
    /** When isBlank is true the DataWatcher is not watching any objects */
    @Shadow
    private boolean isBlank = true;
    @Unique
    private static final ConcurrentHashMapV8 optimizationsAndTweaks$dataTypes = new ConcurrentHashMapV8();
    @Unique
    private final ConcurrentHashMapV8 optimizationsAndTweaks$watchedObjects = new ConcurrentHashMapV8();
    /** true if one or more object was changed */
    @Shadow
    private boolean objectChanged;

    public MixinDataWatcher(Entity p_i45313_1_) {
        this.field_151511_a = p_i45313_1_;
    }

    /**
     * adds a new object to dataWatcher to watch, to update an already existing object see updateObject. Arguments: data
     * Value Id, Object to add
     */
    @Overwrite
    public void addObject(int p_75682_1_, Object p_75682_2_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            Integer integer = (Integer) optimizationsAndTweaks$dataTypes.get(p_75682_2_.getClass());

            if (integer == null) {
                throw new IllegalArgumentException("Unknown data type: " + p_75682_2_.getClass());
            } else if (p_75682_1_ > 31) {
                throw new IllegalArgumentException(
                    "Data value id is too big with " + p_75682_1_ + "! (Max is " + 31 + ")");
            } else if (this.optimizationsAndTweaks$watchedObjects.containsKey(p_75682_1_)) {
                throw new IllegalArgumentException("Duplicate id value for " + p_75682_1_ + "!");
            } else {
                DataWatcher.WatchableObject watchableobject = new DataWatcher.WatchableObject(
                    integer,
                    p_75682_1_,
                    p_75682_2_);

                this.optimizationsAndTweaks$watchedObjects.put(p_75682_1_, watchableobject);
                this.isBlank = false;
            }
        }
    }

    /**
     * Add a new object for the DataWatcher to watch, using the specified data type.
     */
    @Overwrite
    public void addObjectByDataType(int p_82709_1_, int p_82709_2_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            DataWatcher.WatchableObject watchableobject = new DataWatcher.WatchableObject(p_82709_2_, p_82709_1_, null);
            this.optimizationsAndTweaks$watchedObjects.put(p_82709_1_, watchableobject);
            this.isBlank = false;
        }
    }

    /**
     * gets the bytevalue of a watchable object
     */
    @Overwrite
    public byte getWatchableObjectByte(int id) {
        Object obj = getWatchedObject(id).getObject();
        if (obj instanceof Integer) {
            return ((Integer) obj).byteValue();
        }
        return (Byte) obj;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public short getWatchableObjectShort(int p_75693_1_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            return (Short) this.getWatchedObject(p_75693_1_)
                .getObject();
        }
    }

    /**
     * gets a watchable object and returns it as a Integer
     */
    @Overwrite
    public int getWatchableObjectInt(int p_75679_1_) {
        Object obj = this.getWatchedObject(p_75679_1_)
            .getObject();
        if (obj instanceof Byte) {
            return ((Byte) obj).intValue();
        } else {
            return (Integer) obj;
        }
    }

    @Overwrite
    public float getWatchableObjectFloat(int p_111145_1_) {
        return (Float) this.getWatchedObject(p_111145_1_)
            .getObject();
    }

    /**
     * gets a watchable object and returns it as a String
     */
    @Overwrite
    public String getWatchableObjectString(int p_75681_1_) {
        return (String) this.getWatchedObject(p_75681_1_)
            .getObject();
    }

    /**
     * Get a watchable object as an ItemStack.
     */
    @Overwrite
    public ItemStack getWatchableObjectItemStack(int p_82710_1_) {
        return (ItemStack) this.getWatchedObject(p_82710_1_)
            .getObject();
    }

    /**
     * is threadsafe, unless it throws an exception, then
     */
    @Overwrite
    public DataWatcher.WatchableObject getWatchedObject(int p_75691_1_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            DataWatcher.WatchableObject watchableobject;

            try {
                watchableobject = (DataWatcher.WatchableObject) this.optimizationsAndTweaks$watchedObjects
                    .get(p_75691_1_);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
                crashreportcategory.addCrashSection("Data ID", p_75691_1_);
                throw new ReportedException(crashreport);
            }

            return watchableobject;
        }
    }

    /**
     * updates an already existing object
     */
    @Overwrite
    public void updateObject(int p_75692_1_, Object p_75692_2_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            DataWatcher.WatchableObject watchableobject = this.getWatchedObject(p_75692_1_);

            if (ObjectUtils.notEqual(p_75692_2_, watchableobject.getObject())) {
                watchableobject.setObject(p_75692_2_);
                this.field_151511_a.func_145781_i(p_75692_1_);
                watchableobject.setWatched(true);
                this.objectChanged = true;
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setObjectWatched(int p_82708_1_) {
        this.getWatchedObject(p_82708_1_)
            .setWatched(true);
        this.objectChanged = true;
    }

    @Shadow
    public boolean hasChanges() {
        return this.objectChanged;
    }

    /**
     * Writes the list of watched objects (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) to the specified PacketBuffer
     */
    @Overwrite
    public static void writeWatchedListToPacketBuffer(List p_151507_0_, PacketBuffer p_151507_1_) throws IOException {
        if (p_151507_0_ != null) {

            for (Object o : p_151507_0_) {
                DataWatcher.WatchableObject watchableobject = (DataWatcher.WatchableObject) o;
                writeWatchableObjectToPacketBuffer(p_151507_1_, watchableobject);
            }
        }

        p_151507_1_.writeByte(127);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public List getChanged() {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            ArrayList arraylist = null;

            if (this.objectChanged) {
                for (Object o : this.optimizationsAndTweaks$watchedObjects.values()) {
                    DataWatcher.WatchableObject watchableobject = (DataWatcher.WatchableObject) o;

                    if (watchableobject.isWatched()) {
                        watchableobject.setWatched(false);

                        if (arraylist == null) {
                            arraylist = new ArrayList();
                        }

                        arraylist.add(watchableobject);
                    }
                }
            }
            this.objectChanged = false;
            return arraylist;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_151509_a(PacketBuffer p_151509_1_) throws IOException {

        for (Object o : this.optimizationsAndTweaks$watchedObjects.values()) {
            DataWatcher.WatchableObject watchableobject = (DataWatcher.WatchableObject) o;
            writeWatchableObjectToPacketBuffer(p_151509_1_, watchableobject);
        }

        p_151509_1_.writeByte(127);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public List getAllWatched() {
        ArrayList arraylist = null;
        DataWatcher.WatchableObject watchableobject;

        for (Iterator iterator = this.optimizationsAndTweaks$watchedObjects.values()
            .iterator(); iterator.hasNext(); arraylist.add(watchableobject)) {
            watchableobject = (DataWatcher.WatchableObject) iterator.next();

            if (arraylist == null) {
                arraylist = new ArrayList();
            }
        }
        return arraylist;
    }

    /**
     * Writes a watchable object (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) to the specified PacketBuffer
     */
    @Overwrite
    public static void writeWatchableObjectToPacketBuffer(PacketBuffer p_151510_0_,
        DataWatcher.WatchableObject p_151510_1_) throws IOException {
        int i = (p_151510_1_.getObjectType() << 5 | p_151510_1_.getDataValueId() & 31) & 255;
        p_151510_0_.writeByte(i);

        switch (p_151510_1_.getObjectType()) {
            case 0:
                p_151510_0_.writeByte((Byte) p_151510_1_.getObject());
                break;
            case 1:
                p_151510_0_.writeShort((Short) p_151510_1_.getObject());
                break;
            case 2:
                p_151510_0_.writeInt((Integer) p_151510_1_.getObject());
                break;
            case 3:
                p_151510_0_.writeFloat((Float) p_151510_1_.getObject());
                break;
            case 4:
                p_151510_0_.writeStringToBuffer((String) p_151510_1_.getObject());
                break;
            case 5:
                ItemStack itemstack = (ItemStack) p_151510_1_.getObject();
                p_151510_0_.writeItemStackToBuffer(itemstack);
                break;
            case 6:
                ChunkCoordinates chunkcoordinates = (ChunkCoordinates) p_151510_1_.getObject();
                p_151510_0_.writeInt(chunkcoordinates.posX);
                p_151510_0_.writeInt(chunkcoordinates.posY);
                p_151510_0_.writeInt(chunkcoordinates.posZ);
        }
    }

    /**
     * Reads a list of watched objects (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) from the supplied PacketBuffer
     */
    @Overwrite
    public static List readWatchedListFromPacketBuffer(PacketBuffer p_151508_0_) throws IOException {
        ArrayList arraylist = null;

        for (byte b0 = p_151508_0_.readByte(); b0 != 127; b0 = p_151508_0_.readByte()) {
            if (arraylist == null) {
                arraylist = new ArrayList();
            }

            int i = (b0 & 224) >> 5;
            int j = b0 & 31;
            DataWatcher.WatchableObject watchableobject = null;

            switch (i) {
                case 0:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readByte());
                    break;
                case 1:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readShort());
                    break;
                case 2:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readInt());
                    break;
                case 3:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readFloat());
                    break;
                case 4:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readStringFromBuffer(32767));
                    break;
                case 5:
                    watchableobject = new DataWatcher.WatchableObject(i, j, p_151508_0_.readItemStackFromBuffer());
                    break;
                case 6:
                    int k = p_151508_0_.readInt();
                    int l = p_151508_0_.readInt();
                    int i1 = p_151508_0_.readInt();
                    watchableobject = new DataWatcher.WatchableObject(i, j, new ChunkCoordinates(k, l, i1));
            }

            arraylist.add(watchableobject);
        }

        return arraylist;
    }

    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    public void updateWatchedObjectsFromList(List p_75687_1_) {
        synchronized (optimizationsAndTweaks$watchedObjects) {
            for (Object o : p_75687_1_) {
                DataWatcher.WatchableObject watchableobject = (DataWatcher.WatchableObject) o;
                DataWatcher.WatchableObject watchableobject1 = (DataWatcher.WatchableObject) this.optimizationsAndTweaks$watchedObjects
                    .get(watchableobject.getDataValueId());

                if (watchableobject1 != null) {
                    watchableobject1.setObject(watchableobject.getObject());
                    this.field_151511_a.func_145781_i(watchableobject.getDataValueId());
                }
            }
        }
        this.objectChanged = true;
    }

    @Shadow
    public boolean getIsBlank() {
        return this.isBlank;
    }

    @Shadow
    public void func_111144_e() {
        this.objectChanged = false;
    }

    static {
        optimizationsAndTweaks$dataTypes.put(Byte.class, 0);
        optimizationsAndTweaks$dataTypes.put(Short.class, 1);
        optimizationsAndTweaks$dataTypes.put(Integer.class, 2);
        optimizationsAndTweaks$dataTypes.put(Float.class, 3);
        optimizationsAndTweaks$dataTypes.put(String.class, 4);
        optimizationsAndTweaks$dataTypes.put(ItemStack.class, 5);
        optimizationsAndTweaks$dataTypes.put(ChunkCoordinates.class, 6);
    }
}
