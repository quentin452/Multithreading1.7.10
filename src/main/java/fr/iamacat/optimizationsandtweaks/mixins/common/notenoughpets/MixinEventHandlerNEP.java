package fr.iamacat.optimizationsandtweaks.mixins.common.notenoughpets;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.entity.EntityOcelotNEP;
import astrotibs.notenoughpets.entity.EntityWolfNEP;
import astrotibs.notenoughpets.events.EventHandlerNEP;
import astrotibs.notenoughpets.util.LogHelper;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

@Mixin(EventHandlerNEP.class)
public class MixinEventHandlerNEP {

    @Shadow
    public static boolean isInRange(int x, int y, int z, Village village) {
        return village.getCenter()
            .getDistanceSquared(x, y, z)
            < (float) ((village.getVillageRadius() + 32) * (village.getVillageRadius() + 32));
    }

    @Shadow
    private static Vec3 tryGetStraySpawningLocation(Village village, World world, int rangeX, int rangeY, int rangeZ) {
        int vilCenterX = village.getCenter().posX;
        int vilCenterY = village.getCenter().posY;
        int vilCenterZ = village.getCenter().posZ;
        int vilRadius = village.getVillageRadius();

        for (int k1 = 0; k1 < 10; ++k1) {
            int tryX = vilCenterX + world.rand.nextInt(2 * vilRadius + 1) - vilRadius;
            int tryY = vilCenterY + world.rand.nextInt(8) - 4;
            int tryZ = vilCenterZ + world.rand.nextInt(2 * vilRadius + 1) - vilRadius;
            if (isInRange(tryX, tryY, tryZ, village)
                && isValidSpawningLocation(tryX, tryY, tryZ, rangeX, rangeY, rangeZ, world)) {
                return Vec3.createVectorHelper((double) tryX, (double) tryY, (double) tryZ);
            }
        }

        return null;
    }

    @Shadow
    private static boolean isValidSpawningLocation(int spawnposX, int spawnposY, int spawnposZ, int xRange, int yRange,
        int zRange, World world) {
        if (!World.doesBlockHaveSolidTopSurface(world, spawnposX, spawnposY - 1, spawnposZ)) {
            return false;
        } else {
            int k1 = spawnposX - xRange / 2;
            int l1 = spawnposZ - zRange / 2;

            for (int checkX = k1; checkX < k1 + xRange; ++checkX) {
                for (int checkY = spawnposY; checkY < spawnposY + yRange; ++checkY) {
                    for (int checkZ = l1; checkZ < l1 + zRange; ++checkZ) {
                        if (world.getBlock(checkX, checkY, checkZ)
                            .isNormalCube()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    @Shadow
    private static int countEntityAroundVillage(Class entityClass, Village village, World world, int villageBuffer) {
        int vilX = village.getCenter().posX;
        int vilY = village.getCenter().posY;
        int vilZ = village.getCenter().posZ;
        int vilR = village.getVillageRadius();
        List list = world.getEntitiesWithinAABB(
            entityClass,
            AxisAlignedBB.getBoundingBox(
                vilX - vilR - villageBuffer,
                vilY - 4,
                vilZ - vilR - villageBuffer,
                vilX + vilR + villageBuffer,
                vilY + 4,
                vilZ + vilR + villageBuffer));
        return list != null ? list.size() : 0;
    }

    @Shadow
    private static void trySpawnStray(Village village, World world) {
        int spawnCap;
        Class strayClass;
        float strayFraction;
        Object entityStray;
        if (world.rand.nextFloat() < (!GeneralConfig.enableNECDogs ? 1.0F : GeneralConfig.strayFractionCat)
            && GeneralConfig.enableNECCats) {
            spawnCap = GeneralConfig.villageSpawnCapCat;
            strayClass = EntityOcelot.class;
            strayFraction = !GeneralConfig.enableNECDogs ? 1.0F : GeneralConfig.strayFractionCat;
            entityStray = new EntityOcelotNEP(world);
            ((EntityOcelotNEP) entityStray).setTameSkin(
                world.rand.nextInt(
                    (GeneralConfig.enableNECCats && !GeneralConfig.revertAnimalTypes
                        ? SkinVariations.catSkinArray.length
                        : 4) - 1)
                    + 1);
        } else {
            if (!GeneralConfig.enableNECDogs) {
                return;
            }

            spawnCap = GeneralConfig.villageSpawnCapDog;
            strayClass = EntityWolf.class;
            strayFraction = GeneralConfig.enableNECCats ? 1.0F - GeneralConfig.strayFractionCat : 1.0F;
            entityStray = new EntityWolfNEP(world);
            ((EntityWolfNEP) entityStray).setTameSkin(
                GeneralConfig.enableNECDogs && !GeneralConfig.revertAnimalTypes
                    ? world.rand.nextInt(SkinVariations.dogSkinArray.length - 1) + 1
                    : 0);
        }

        if (spawnCap > 0
            && countEntityAroundVillage(strayClass, village, world, 32) < Math.min(
                Math.round(
                    GeneralConfig.strayCapFromVillageRadius
                        ? (float) (village.getVillageRadius() * village.getVillageRadius() / 500)
                        : (float) Math.min(village.getNumVillagers(), village.getNumVillageDoors() / 4)
                            * strayFraction),
                spawnCap)) {
            Vec3 vec3 = tryGetStraySpawningLocation(village, world, 2, 4, 2);
            if (vec3 != null) {
                ((EntityTameable) entityStray)
                    .setLocationAndAngles(vec3.xCoord, vec3.yCoord, vec3.zCoord, world.rand.nextFloat() * 360.0F, 0.0F);
                ((EntityTameable) entityStray).setTamed(false);
                ((EntityTameable) entityStray).setGrowingAge(Math.min(24000 - world.rand.nextInt(48001), 0));
                world.spawnEntityInWorld((Entity) entityStray);
            }
        }

    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void spawnStraysWithVillageTick(TickEvent.WorldTickEvent event) {
        if (Arrays.binarySearch(GeneralConfig.strayDimensions, event.world.provider.dimensionId) >= 0
            && (GeneralConfig.enableNECCats && !GeneralConfig.revertAnimalTypes ? GeneralConfig.villageSpawnCapCat : 0)
                + (GeneralConfig.enableNECDogs && !GeneralConfig.revertAnimalTypes ? GeneralConfig.villageSpawnCapDog
                    : 0)
                > 0
            && !event.world.isRemote
            && event.phase == TickEvent.Phase.END) {
            VillageCollection vc = event.world.villageCollectionObj;
            int vcTickCounter = (Integer) ReflectionHelper
                .getPrivateValue(VillageCollection.class, vc, new String[] { "tickCounter", "field_75553_e" });
            if (vcTickCounter % 30 == 0) {
                Iterator iterator = vc.getVillageList()
                    .iterator();

                while (iterator.hasNext()) {
                    Village village = (Village) iterator.next();
                    if (GeneralConfig.debugMessages && vcTickCounter % 90 == 0) {
                        LogHelper.info(
                            "Village " + village
                                + " cats: "
                                + countEntityAroundVillage(EntityOcelot.class, village, event.world, 32)
                                + ", dogs: "
                                + countEntityAroundVillage(EntityWolf.class, village, event.world, 32));
                    }

                    if ((GeneralConfig.enableNECCats && !GeneralConfig.revertAnimalTypes
                        && GeneralConfig.villageSpawnCapCat > 0
                            ? countEntityAroundVillage(EntityOcelotNEP.class, village, event.world, 32)
                            : 0)
                        + (GeneralConfig.enableNECDogs && !GeneralConfig.revertAnimalTypes
                            && GeneralConfig.villageSpawnCapDog > 0
                                ? countEntityAroundVillage(EntityWolfNEP.class, village, event.world, 32)
                                : 0)
                        < Math.min(
                            GeneralConfig.strayCapFromVillageRadius
                                ? village.getVillageRadius() * village.getVillageRadius() / 500
                                : Math.min(village.getNumVillagers(), village.getNumVillageDoors() / 4),
                            GeneralConfig.revertAnimalTypes ? 0
                                : Math.max(
                                    GeneralConfig.enableNECCats ? GeneralConfig.villageSpawnCapCat : 0,
                                    GeneralConfig.enableNECDogs ? GeneralConfig.villageSpawnCapDog : 0))
                        && event.world.rand.nextInt(100) < GeneralConfig.villageSpawnRatePercent) {
                        trySpawnStray(village, event.world);
                    }
                }
            }
        }

    }

    @Unique
    private static void optimizationsAndTweaks$trySpawnStray(Village village, World world) {
        int spawnCap;
        Class strayClass;
        float strayFraction;
        EntityTameable entityStray;
        if (world.rand.nextFloat() < (!GeneralConfig.enableNECDogs ? 1.0F : GeneralConfig.strayFractionCat)
            && GeneralConfig.enableNECCats) {
            spawnCap = GeneralConfig.villageSpawnCapCat;
            strayClass = EntityOcelot.class;
            strayFraction = !GeneralConfig.enableNECDogs ? 1.0F : GeneralConfig.strayFractionCat;
            entityStray = new EntityOcelotNEP(world);
            ((EntityOcelotNEP) entityStray).setTameSkin(
                world.rand.nextInt(
                    (GeneralConfig.enableNECCats && !GeneralConfig.revertAnimalTypes
                        ? SkinVariations.catSkinArray.length
                        : 4) - 1)
                    + 1);
        } else {
            if (!GeneralConfig.enableNECDogs) {
                return;
            }

            spawnCap = GeneralConfig.villageSpawnCapDog;
            strayClass = EntityWolf.class;
            strayFraction = GeneralConfig.enableNECCats ? 1.0F - GeneralConfig.strayFractionCat : 1.0F;
            entityStray = new EntityWolfNEP(world);
            ((EntityWolfNEP) entityStray).setTameSkin(
                GeneralConfig.enableNECDogs && !GeneralConfig.revertAnimalTypes
                    ? world.rand.nextInt(SkinVariations.dogSkinArray.length - 1) + 1
                    : 0);
        }

        if (spawnCap > 0
            && optimizationsAndTweaks$countEntityAroundVillage(strayClass, village, world, 32) < Math.min(
                Math.round(
                    GeneralConfig.strayCapFromVillageRadius
                        ? (float) (village.getVillageRadius() * village.getVillageRadius() / 500)
                        : (float) Math.min(village.getNumVillagers(), village.getNumVillageDoors() / 4)
                            * strayFraction),
                spawnCap)) {
            Vec3 vec3 = optimizationsAndTweaks$tryGetStraySpawningLocation(village, world, 2, 4, 2);
            if (vec3 != null) {
                entityStray
                    .setLocationAndAngles(vec3.xCoord, vec3.yCoord, vec3.zCoord, world.rand.nextFloat() * 360.0F, 0.0F);
                entityStray.setTamed(false);
                entityStray.setGrowingAge(Math.min(24000 - world.rand.nextInt(48001), 0));
                world.spawnEntityInWorld(entityStray);
            }
        }

    }

    @Unique
    private static int optimizationsAndTweaks$countEntityAroundVillage(Class entityClass, Village village, World world,
        int villageBuffer) {
        int vilX = village.getCenter().posX;
        int vilY = village.getCenter().posY;
        int vilZ = village.getCenter().posZ;
        int vilR = village.getVillageRadius();
        List list = world.getEntitiesWithinAABB(
            entityClass,
            AxisAlignedBB.getBoundingBox(
                vilX - vilR - villageBuffer,
                vilY - 4,
                vilZ - vilR - villageBuffer,
                vilX + vilR + villageBuffer,
                (vilY + 4),
                (vilZ + vilR + villageBuffer)));
        return list != null ? list.size() : 0;

    }

    @Unique
    private static Vec3 optimizationsAndTweaks$tryGetStraySpawningLocation(Village village, World world, int rangeX,
        int rangeY, int rangeZ) {
        int vilCenterX = village.getCenter().posX;
        int vilCenterY = village.getCenter().posY;
        int vilCenterZ = village.getCenter().posZ;
        int vilRadius = village.getVillageRadius();

        for (int k1 = 0; k1 < 10; ++k1) {
            int tryX = vilCenterX + world.rand.nextInt(2 * vilRadius + 1) - vilRadius;
            int tryY = vilCenterY + world.rand.nextInt(8) - 4;
            int tryZ = vilCenterZ + world.rand.nextInt(2 * vilRadius + 1) - vilRadius;
            if (optimizationsAndTweaks$isInRange(tryX, tryY, tryZ, village)
                && optimizationsAndTweaks$isValidSpawningLocation(tryX, tryY, tryZ, rangeX, rangeY, rangeZ, world)) {
                return Vec3.createVectorHelper(tryX, tryY, tryZ);
            }
        }

        return null;
    }

    @Unique
    private static boolean optimizationsAndTweaks$isInRange(int x, int y, int z, Village village) {
        return village.getCenter()
            .getDistanceSquared(x, y, z)
            < (float) ((village.getVillageRadius() + 32) * (village.getVillageRadius() + 32));
    }

    @Unique
    private static boolean optimizationsAndTweaks$isValidSpawningLocation(int spawnposX, int spawnposY, int spawnposZ,
        int xRange, int yRange, int zRange, World world) {
        if (!World.doesBlockHaveSolidTopSurface(world, spawnposX, spawnposY - 1, spawnposZ)) {
            return false;
        } else {
            int k1 = spawnposX - xRange / 2;
            int l1 = spawnposZ - zRange / 2;

            for (int checkX = k1; checkX < k1 + xRange; ++checkX) {
                for (int checkY = spawnposY; checkY < spawnposY + yRange; ++checkY) {
                    for (int checkZ = l1; checkZ < l1 + zRange; ++checkZ) {
                        if (world.getBlock(checkX, checkY, checkZ)
                            .isNormalCube()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
}
