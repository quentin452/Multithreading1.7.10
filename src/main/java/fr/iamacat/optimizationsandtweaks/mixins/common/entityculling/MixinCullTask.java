package fr.iamacat.optimizationsandtweaks.mixins.common.entityculling;

import dev.tr7zw.entityculling.CullTask;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.access.Cullable;
import dev.tr7zw.entityculling.shadow.com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import dev.tr7zw.entityculling.shadow.com.logisticscraft.occlusionculling.util.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

@Mixin(CullTask.class)
public class MixinCullTask  implements Runnable {
    @Shadow
    public boolean requestCull = false;
    @Shadow
    private final OcclusionCullingInstance culling;
    @Shadow
    private final Minecraft client = Minecraft.getMinecraft();
    @Shadow
    private final int sleepDelay;
    @Shadow
    private final int hitboxLimit;
    @Shadow
    private final Set<String> unCullable;
    @Shadow
    public long lastTime;
    @Shadow
    private Vec3d lastPos;
    @Shadow
    private Vec3d aabbMin;
    @Shadow
    private Vec3d aabbMax;

    public MixinCullTask(OcclusionCullingInstance culling, Set<String> unCullable) {
        this.sleepDelay = EntityCullingModBase.instance.config.sleepDelay;
        this.hitboxLimit = EntityCullingModBase.instance.config.hitboxLimit;
        this.culling = culling;
        this.unCullable = unCullable;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void run() {
        while (client != null) { // not correct, but the running field is hidden
            try {
                Thread.sleep(sleepDelay);

                if (EntityCullingModBase.enabled && client.theWorld != null && client.thePlayer != null && client.thePlayer.ticksExisted > 10 && client.renderViewEntity != null) {
                    Vec3 cameraMC;
                    if (EntityCullingModBase.instance.config.debugMode) {
                        cameraMC = client.thePlayer.getPosition(0).addVector(0, client.thePlayer.getEyeHeight(), 0);
                    } else {
                        cameraMC = getCameraPos();
                    }
                    if (requestCull || !(cameraMC.xCoord == lastPos.x && cameraMC.yCoord == lastPos.y && cameraMC.zCoord == lastPos.z)) {
                        long start = System.currentTimeMillis();
                        requestCull = false;
                        lastPos.set(cameraMC.xCoord, cameraMC.yCoord, cameraMC.zCoord);
                        Vec3d camera = lastPos;
                        culling.resetCache();
                        boolean noCulling = client.gameSettings.thirdPersonView != 0;
                        Iterator<TileEntity> iterator = client.theWorld.loadedTileEntityList.iterator();
                        while (iterator.hasNext()) {
                            try {
                                TileEntity entry = iterator.next();
                                if (entry == null || entry.getBlockType() == null || entry.getBlockType().getUnlocalizedName() == null) {
                                    continue;
                                }
                                if (unCullable.contains(entry.getBlockType().getUnlocalizedName())) {
                                    continue;
                                }
                                Cullable cullable = (Cullable) entry;
                                if (!cullable.isForcedVisible()) {
                                    if (noCulling) {
                                        cullable.setCulled(false);
                                        continue;
                                    }
                                    if (distanceSq(entry.xCoord, entry.yCoord, entry.zCoord, cameraMC.xCoord, cameraMC.yCoord, cameraMC.zCoord) < 64 * 64) { // 64 is the fixed max tile view distance
                                        aabbMin.set(entry.xCoord, entry.yCoord, entry.zCoord);
                                        aabbMax.set(entry.xCoord + 1, entry.yCoord + 1, entry.zCoord + 1);
                                        boolean visible = culling.isAABBVisible(aabbMin, aabbMax, camera);
                                        cullable.setCulled(!visible);
                                    }
                                }
                            } catch (NullPointerException | ConcurrentModificationException ex) {
                                continue; // Skip to next iteration on exception
                            }
                        }
                        Iterator<Entity> iterable = client.theWorld.getLoadedEntityList().iterator();
                        while (iterable.hasNext()) {
                            try {
                                Entity entity = iterable.next();
                                if (entity == null || !(entity instanceof Cullable)) {
                                    continue; // Skip to next iteration if entity is null or not Cullable
                                }
                                Cullable cullable = (Cullable) entity;
                                if (!cullable.isForcedVisible()) {
                                    if (noCulling) {
                                        cullable.setCulled(false);
                                        continue;
                                    }
                                    if (distanceSq(entity.posX, entity.posY, entity.posZ, cameraMC.xCoord, cameraMC.yCoord, cameraMC.zCoord) > EntityCullingModBase.instance.config.tracingDistance * EntityCullingModBase.instance.config.tracingDistance) {
                                        cullable.setCulled(false); // If your entity view distance is larger than tracingDistance just render it
                                        continue;
                                    }
                                    AxisAlignedBB boundingBox = entity.boundingBox;
                                    if (boundingBox.maxX - boundingBox.minX > hitboxLimit || boundingBox.maxY - boundingBox.minY > hitboxLimit || boundingBox.maxZ - boundingBox.minZ > hitboxLimit) {
                                        cullable.setCulled(false); // Too big to bother to cull
                                        continue;
                                    }
                                    aabbMin.set(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                                    aabbMax.set(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                                    boolean visible = culling.isAABBVisible(aabbMin, aabbMax, camera);
                                    cullable.setCulled(!visible);
                                }
                            } catch (NullPointerException | ConcurrentModificationException ex) {
                                continue; // Skip to next iteration on exception
                            }
                        }
                        lastTime = (System.currentTimeMillis() - start);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Shutting down culling task!");
    }


    @Shadow
    private Vec3 getCameraPos() {
        if (client.gameSettings.thirdPersonView == 0) {
            return client.renderViewEntity.getPosition(0).addVector(0, client.thePlayer.getEyeHeight(), 0);
        }
        return client.renderViewEntity.getPosition(0).addVector(0, client.thePlayer.getEyeHeight(), 0);
    }
    @Shadow
    private double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d3 = x1 - x2;
        double d4 = y1 - y2;
        double d5 = z1 - z2;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }
}
