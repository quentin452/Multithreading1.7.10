package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    protected DataWatcher dataWatcher;
    @Shadow
    public final AxisAlignedBB boundingBox;
    @Shadow
    public float yOffset;
    /** Entity position X */
    @Shadow
    public double posX;
    /** Entity position Y */
    @Shadow
    public double posY;
    /** Entity position Z */
    @Shadow
    public double posZ;
    /** Entity motion Y */
    @Shadow
    public double motionY;
    /** Entity motion Z */

    @Shadow
    public World worldObj;

    @Shadow
    public float width;

    public MixinEntity() {
        this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Unique
    private String optimizationsAndTweaks$cachedEntityName = null;

    @Shadow
    public abstract String getEntityString();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public String getCommandSenderName() {
        if (optimizationsAndTweaks$cachedEntityName != null) {
            return optimizationsAndTweaks$cachedEntityName;
        }

        String entityName = getEntityString();
        String translatedName = (entityName != null) ? StatCollector.translateToLocal("entity." + entityName + ".name")
            : StatCollector.translateToLocal("entity.generic.name");

        optimizationsAndTweaks$cachedEntityName = translatedName;

        return translatedName;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void setFlag(int flag, boolean set) {
        byte data = this.dataWatcher.getWatchableObjectByte(0);
        data = set ? (byte) (data | (1 << flag & 0xff)) : (byte) (data & ~(1 << flag & 0xff));
        this.dataWatcher.updateObject(0, data);
    }

    @Overwrite
    public float getBrightness(float p_70013_1_) {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posZ);
        if (this.worldObj.blockExists(i, 0, j)) {
            double d0 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
            int k = MathHelper.floor_double(this.posY - this.yOffset + d0);
            return this.worldObj.getLightBrightness(i, k, j);
        }
        return 0.0F;
    }
}
