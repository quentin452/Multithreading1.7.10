package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.eventhandler.Event;
import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {

    @Unique
    private World p_i1595_1_;
    @Shadow
    private boolean canPickUpLoot;
    @Shadow
    public final EntityAITasks targetTasks;
    @Shadow
    public final EntityAITasks tasks;
    @Shadow
    private EntitySenses senses;

    @Shadow
    private PathNavigate navigator;

    @Shadow
    private EntityLookHelper lookHelper;
    @Shadow
    private EntityMoveHelper moveHelper;
    @Shadow
    private EntityJumpHelper jumpHelper;
    @Shadow
    private EntityBodyHelper bodyHelper;

    @Shadow
    private boolean persistenceRequired;

    @Unique
    private EntityLiving entityLiving;

    @Shadow
    protected void despawnEntity() {
        Event.Result result;
        if (this.persistenceRequired) {
            this.entityAge = 0;
        } else if ((this.entityAge & 0x1F) == 0x1F
            && (result = ForgeEventFactory.canEntityDespawn(entityLiving)) != Event.Result.DEFAULT) {
                if (result == Event.Result.DENY) {
                    this.entityAge = 0;
                } else {
                    this.setDead();
                }
            } else {
                EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

                if (entityplayer != null) {
                    double d0 = entityplayer.posX - this.posX;
                    double d1 = entityplayer.posY - this.posY;
                    double d2 = entityplayer.posZ - this.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (this.canDespawn() && d3 > 16384.0D) {
                        this.setDead();
                    }

                    if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                        this.setDead();
                    } else if (d3 < 1024.0D) {
                        this.entityAge = 0;
                    }
                }
            }
    }

    @Shadow
    protected boolean canDespawn() {
        return true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void updateAITasks() {
        Profiler profiler = this.worldObj.theProfiler;

        ++this.entityAge;

        profiler.startSection("checkDespawn");
        this.despawnEntity();
        profiler.endSection();

        profiler.startSection("sensing");
        this.senses.clearSensingCache();
        profiler.endSection();

        profiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        profiler.endSection();

        profiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        profiler.endSection();

        profiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        profiler.endSection();

        profiler.startSection("mob tick");
        this.updateAITick();
        profiler.endSection();

        profiler.startSection("controls");
        profiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        profiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        profiler.endStartSection("jump");
        this.jumpHelper.doJump();
        profiler.endSection();

        profiler.endSection();
    }

    public MixinEntityLiving(World p_i1594_1_, World pI15951, EntityLiving entityLiving) {
        super(p_i1594_1_);
        p_i1595_1_ = pI15951;
        this.entityLiving = entityLiving;
        this.tasks = new EntityAITasks(
            p_i1595_1_ != null && p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler : null);
        this.targetTasks = new EntityAITasks(
            p_i1595_1_ != null && p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler : null);
    }

    @Unique
    private boolean cachedCanPickUpLoot = false;
    @Unique
    private long lastCheckTime = 0L;
    @Unique
    private static final long CACHE_EXPIRATION_TIME = 1000L;

    @Inject(method = "canPickUpLoot", at = @At("HEAD"), remap = false, cancellable = true)
    public boolean canPickUpLoot(CallbackInfo ci) {
        if (OptimizationsandTweaksConfig.enableMixinEntityLiving) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastCheckTime < CACHE_EXPIRATION_TIME) {
                return cachedCanPickUpLoot;
            } else {
                boolean canPickUpLoot = this.canPickUpLoot;
                cachedCanPickUpLoot = canPickUpLoot;
                lastCheckTime = currentTime;
                return canPickUpLoot;
            }
        }
        ci.cancel();
        return false;
    }
}
