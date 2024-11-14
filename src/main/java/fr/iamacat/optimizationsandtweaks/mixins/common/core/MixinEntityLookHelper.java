package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import fr.iamacat.optimizationsandtweaks.utils.apache.commons.math3.util.FastMath;

@Mixin(EntityLookHelper.class)
public class MixinEntityLookHelper {

    @Shadow
    private EntityLiving entity;
    /** The amount of change that is made each update for an entity facing a direction. */
    @Shadow
    private float deltaLookYaw;
    /** The amount of change that is made each update for an entity facing a direction. */
    @Shadow
    private float deltaLookPitch;
    /** Whether or not the entity is trying to look at something. */
    @Shadow
    private boolean isLooking;
    @Shadow
    private double posX;
    @Shadow
    private double posY;
    @Shadow
    private double posZ;

    @Shadow
    public void setLookPositionWithEntity(Entity p_75651_1_, float p_75651_2_, float p_75651_3_) {
        this.posX = p_75651_1_.posX;

        if (p_75651_1_ instanceof EntityLivingBase) {
            this.posY = p_75651_1_.posY + (double) p_75651_1_.getEyeHeight();
        } else {
            this.posY = (p_75651_1_.boundingBox.minY + p_75651_1_.boundingBox.maxY) / 2.0D;
        }

        this.posZ = p_75651_1_.posZ;
        this.deltaLookYaw = p_75651_2_;
        this.deltaLookPitch = p_75651_3_;
        this.isLooking = true;
    }

    /**
     * @author iamacat
     * @reason no changes
     */
    @Overwrite
    public void setLookPosition(double p_75650_1_, double p_75650_3_, double p_75650_5_, float p_75650_7_,
        float p_75650_8_) {
        this.posX = p_75650_1_;
        this.posY = p_75650_3_;
        this.posZ = p_75650_5_;
        this.deltaLookYaw = p_75650_7_;
        this.deltaLookPitch = p_75650_8_;
        this.isLooking = true;
    }

    /**
     * @author iamacat
     * @reason optimize onUpdateLook
     */
    @Overwrite
    public void onUpdateLook() {
        this.entity.rotationPitch = 0.0F;

        if (this.isLooking) {
            optimizationsAndTweaks$updateEntityLooking();
        } else {
            optimizationsAndTweaks$updateEntityNonLooking();
        }

        optimizationsAndTweaks$limitEntityRotation();
    }

    @Unique
    private void optimizationsAndTweaks$updateEntityLooking() {
        this.isLooking = false;
        double dX = this.posX - this.entity.posX;
        double dY = this.posY - (this.entity.posY + (double) this.entity.getEyeHeight());
        double dZ = this.posZ - this.entity.posZ;

        double angleToDegrees = 180.0D / Math.PI;

        double atan2Result = FastMath.atan2(dZ, dX);
        float yaw = (float) (atan2Result * angleToDegrees) - 90.0F;
        float pitch = (float) (-FastMath.atan2(dY, Math.sqrt(dX * dX + dZ * dZ)) * angleToDegrees);

        this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, pitch, this.deltaLookPitch);
        this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, yaw, this.deltaLookYaw);
    }

    @Unique
    private void optimizationsAndTweaks$updateEntityNonLooking() {
        this.entity.rotationYawHead = this
            .updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
    }

    @Unique
    private void optimizationsAndTweaks$limitEntityRotation() {
        float yawDifference = MathHelper
            .wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);

        if (!this.entity.getNavigator()
            .noPath()) {
            if (yawDifference < -75.0F) {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
            }

            if (yawDifference > 75.0F) {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
            }
        }
    }

    @Unique
    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
        float f3 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

        if (f3 > p_75652_3_) {
            f3 = p_75652_3_;
        }

        if (f3 < -p_75652_3_) {
            f3 = -p_75652_3_;
        }

        return p_75652_1_ + f3;
    }
}
