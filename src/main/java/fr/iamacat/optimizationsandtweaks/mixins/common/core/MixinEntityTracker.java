package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;
import net.minecraft.network.Packet;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import cpw.mods.fml.common.registry.EntityRegistry;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.HashSetThreadSafe;

@Mixin(EntityTracker.class)
public class MixinEntityTracker {

    @Unique
    private EntityRegistry optimizationsAndTweaks$entityRegistry = EntityRegistry.instance();
    @Shadow
    private static final Logger logger = LogManager.getLogger();
    @Shadow
    private final WorldServer theWorld;
    /** List of tracked entities, used for iteration operations on tracked entities. */
    @Shadow
    private Set trackedEntities = new HashSetThreadSafe();
    @Shadow
    private IntHashMap trackedEntityIDs = new IntHashMap();
    @Shadow
    private int entityViewDistance;

    public MixinEntityTracker(WorldServer p_i1516_1_) {
        this.theWorld = p_i1516_1_;
        this.entityViewDistance = p_i1516_1_.func_73046_m()
            .getConfigurationManager()
            .getEntityViewDistance();
    }

    /**
     * if entity is a player sends all tracked events to the player, otherwise, adds with a visibility and update arate
     * based on the class type
     */
    @Overwrite
    public void addEntityToTracker(Entity p_72786_1_) {
        if (this.optimizationsAndTweaks$entityRegistry.tryTrackingEntity((EntityTracker) (Object) this, p_72786_1_)) {
            return;
        }
        if (p_72786_1_ instanceof EntityPlayerMP) {
            this.addEntityToTracker(p_72786_1_, 512, 2);
            EntityPlayerMP entityplayermp = (EntityPlayerMP) p_72786_1_;
            for (Object trackedEntity : this.trackedEntities) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) trackedEntity;

                if (entitytrackerentry.myEntity != entityplayermp) {
                    entitytrackerentry.tryStartWachingThis(entityplayermp);
                }
            }
        } else if (p_72786_1_ instanceof EntityFishHook) {
            this.addEntityToTracker(p_72786_1_, 64, 5, true);
        } else if (p_72786_1_ instanceof EntityArrow) {
            this.addEntityToTracker(p_72786_1_, 64, 20, false);
        } else if (p_72786_1_ instanceof EntitySmallFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        } else if (p_72786_1_ instanceof EntityFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        } else if (p_72786_1_ instanceof EntitySnowball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityEnderPearl) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityEnderEye) {
            this.addEntityToTracker(p_72786_1_, 64, 4, true);
        } else if (p_72786_1_ instanceof EntityEgg) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityPotion) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityExpBottle) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityFireworkRocket) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityItem) {
            this.addEntityToTracker(p_72786_1_, 64, 20, true);
        } else if (p_72786_1_ instanceof EntityMinecart) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntityBoat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntitySquid) {
            this.addEntityToTracker(p_72786_1_, 64, 3, true);
        } else if (p_72786_1_ instanceof EntityWither) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        } else if (p_72786_1_ instanceof EntityBat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        } else if (p_72786_1_ instanceof IAnimals) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntityDragon) {
            this.addEntityToTracker(p_72786_1_, 160, 3, true);
        } else if (p_72786_1_ instanceof EntityTNTPrimed) {
            this.addEntityToTracker(p_72786_1_, 160, 10, true);
        } else if (p_72786_1_ instanceof EntityFallingBlock) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        } else if (p_72786_1_ instanceof EntityHanging) {
            this.addEntityToTracker(p_72786_1_, 160, Integer.MAX_VALUE, false);
        } else if (p_72786_1_ instanceof EntityXPOrb) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        } else if (p_72786_1_ instanceof EntityEnderCrystal) {
            this.addEntityToTracker(p_72786_1_, 256, Integer.MAX_VALUE, false);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void addEntityToTracker(Entity p_72791_1_, int p_72791_2_, int p_72791_3_) {
        this.addEntityToTracker(p_72791_1_, p_72791_2_, p_72791_3_, false);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void addEntityToTracker(Entity p_72785_1_, int p_72785_2_, final int p_72785_3_, boolean p_72785_4_) {
        if (p_72785_2_ > this.entityViewDistance) {
            p_72785_2_ = this.entityViewDistance;
        }

        try {
            if (this.trackedEntityIDs.containsItem(p_72785_1_.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(
                p_72785_1_,
                p_72785_2_,
                p_72785_3_,
                p_72785_4_);
            this.trackedEntities.add(entitytrackerentry);
            this.trackedEntityIDs.addKey(p_72785_1_.getEntityId(), entitytrackerentry);
            entitytrackerentry.sendEventsToPlayers(this.theWorld.playerEntities);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
            crashreportcategory.addCrashSection("Tracking range", p_72785_2_ + " blocks");
            crashreportcategory.addCrashSectionCallable("Update interval", () -> {
                String s = "Once per " + p_72785_3_ + " ticks";

                if (p_72785_3_ == Integer.MAX_VALUE) {
                    s = "Maximum (" + s + ")";
                }

                return s;
            });
            p_72785_1_.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
            ((EntityTrackerEntry) this.trackedEntityIDs.lookup(p_72785_1_.getEntityId())).myEntity
                .addEntityCrashInfo(crashreportcategory1);

            try {
                throw new ReportedException(crashreport);
            } catch (ReportedException reportedexception) {
                logger.error("\"Silently\" catching entity tracking error.", reportedexception);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void removeEntityFromAllTrackingPlayers(Entity p_72790_1_) {
        if (p_72790_1_ instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) p_72790_1_;

            for (Object trackedEntity : this.trackedEntities) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) trackedEntity;
                entitytrackerentry.removeFromWatchingList(entityplayermp);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.trackedEntityIDs
            .removeObject(p_72790_1_.getEntityId());

        if (entitytrackerentry1 != null) {
            this.trackedEntities.remove(entitytrackerentry1);
            entitytrackerentry1.informAllAssociatedPlayersOfItemDestruction();
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateTrackedEntities() {
        ArrayList arraylist = new ArrayList();

        for (Object trackedEntity : this.trackedEntities) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) trackedEntity;
            entitytrackerentry.sendLocationToAllClients(this.theWorld.playerEntities);

            if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.myEntity instanceof EntityPlayerMP) {
                arraylist.add(entitytrackerentry.myEntity);
            }
        }

        for (Object o : arraylist) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) o;

            for (Object trackedEntity : this.trackedEntities) {
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) trackedEntity;

                if (entitytrackerentry1.myEntity != entityplayermp) {
                    entitytrackerentry1.tryStartWachingThis(entityplayermp);
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_151247_a(Entity p_151247_1_, Packet p_151247_2_) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntityIDs
            .lookup(p_151247_1_.getEntityId());

        if (entitytrackerentry != null) {
            entitytrackerentry.func_151259_a(p_151247_2_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_151248_b(Entity p_151248_1_, Packet p_151248_2_) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntityIDs
            .lookup(p_151248_1_.getEntityId());

        if (entitytrackerentry != null) {
            entitytrackerentry.func_151261_b(p_151248_2_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_) {
        for (Object trackedEntity : this.trackedEntities) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) trackedEntity;
            entitytrackerentry.removePlayerFromTracker(p_72787_1_);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_) {
        for (Object trackedEntity : this.trackedEntities) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) trackedEntity;

            if (entitytrackerentry.myEntity != p_85172_1_
                && entitytrackerentry.myEntity.chunkCoordX == p_85172_2_.xPosition
                && entitytrackerentry.myEntity.chunkCoordZ == p_85172_2_.zPosition) {
                entitytrackerentry.tryStartWachingThis(p_85172_1_);
            }
        }
    }

    /* ======================================== FORGE START ===================================== */

    // don't expose the EntityTrackerEntry directly so mods can't mess with the data in there as easily
    /**
     * Get all players tracking the given Entity. The Entity must be part of the World that this Tracker belongs to.
     * 
     * @param entity the Entity
     * @return all players tracking the Entity
     */
    @Overwrite(remap = false)
    public Set<net.minecraft.entity.player.EntityPlayer> getTrackingPlayers(Entity entity) {
        EntityTrackerEntry entry = (EntityTrackerEntry) trackedEntityIDs.lookup(entity.getEntityId());
        if (entry == null) return java.util.Collections.emptySet();
        else return java.util.Collections.unmodifiableSet(entry.trackingPlayers);
    }

    /* ======================================== FORGE END ===================================== */
}
