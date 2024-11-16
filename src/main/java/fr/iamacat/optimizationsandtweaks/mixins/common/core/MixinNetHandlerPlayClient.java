package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Shadow
    private Minecraft gameController;
    @Shadow
    private WorldClient clientWorldController;

    /**
     * @author quentin452
     * @reason Fix Server attempted to spawn an unknown entity caused by thread unsafety from NetHandlerPlayClient class
     */
    @Overwrite
    public synchronized void handleSpawnMob(S0FPacketSpawnMob packetIn) {
        double d0 = (double) packetIn.func_149023_f() / 32.0D;
        double d1 = (double) packetIn.func_149034_g() / 32.0D;
        double d2 = (double) packetIn.func_149029_h() / 32.0D;
        float f = (float) (packetIn.func_149028_l() * 360) / 256.0F;
        float f1 = (float) (packetIn.func_149030_m() * 360) / 256.0F;
        EntityLivingBase entitylivingbase = (EntityLivingBase) EntityList
            .createEntityByID(packetIn.func_149025_e(), this.gameController.theWorld);
        if (entitylivingbase == null) {
            cpw.mods.fml.common.FMLLog.info(
                "Server attempted to spawn an unknown entity using ID: {0} at ({1}, {2}, {3}) Skipping!",
                packetIn.func_149025_e(),
                d0,
                d1,
                d2);
            return;
        }
        entitylivingbase.serverPosX = packetIn.func_149023_f();
        entitylivingbase.serverPosY = packetIn.func_149034_g();
        entitylivingbase.serverPosZ = packetIn.func_149029_h();
        entitylivingbase.rotationYawHead = (float) (packetIn.func_149032_n() * 360) / 256.0F;
        Entity[] aentity = entitylivingbase.getParts();

        if (aentity != null) {
            int i = packetIn.func_149024_d() - entitylivingbase.getEntityId();

            for (Entity entity : aentity) {
                entity.setEntityId(entity.getEntityId() + i);
            }
        }

        entitylivingbase.setEntityId(packetIn.func_149024_d());
        entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
        entitylivingbase.motionX = (float) packetIn.func_149026_i() / 8000.0F;
        entitylivingbase.motionY = (float) packetIn.func_149033_j() / 8000.0F;
        entitylivingbase.motionZ = (float) packetIn.func_149031_k() / 8000.0F;
        this.clientWorldController.addEntityToWorld(packetIn.func_149024_d(), entitylivingbase);
        List list = packetIn.func_149027_c();

        if (list != null) {
            entitylivingbase.getDataWatcher()
                .updateWatchedObjectsFromList(list);
        }
    }
    /**
     * @author quentin452
     * @reason Try to fix Default value cannot be lower than minimum value! when calling RangedAttribute
     */
    @Overwrite
    public void handleEntityProperties(S20PacketEntityProperties packetIn) {
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149442_c());

        if (entity != null) {
            if (!(entity instanceof EntityLivingBase)) {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            } else {
                BaseAttributeMap baseattributemap = ((EntityLivingBase) entity).getAttributeMap();
                for (Object o : packetIn.func_149441_d()) {
                    S20PacketEntityProperties.Snapshot snapshot = (S20PacketEntityProperties.Snapshot) o;
                    IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(snapshot.func_151409_a());
                    if (iattributeinstance == null) {
                        // Use a valid default value that is at least as large as the minimum value
                        double defaultValue = Math.max(2.2250738585072014E-308D, 0.1D);
                        iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute(snapshot.func_151409_a(), defaultValue, 2.2250738585072014E-308D, Double.MAX_VALUE));
                    }
                    iattributeinstance.setBaseValue(snapshot.func_151410_b());
                    iattributeinstance.removeAllModifiers();
                    for (Object object : snapshot.func_151408_c()) {
                        AttributeModifier attributemodifier = (AttributeModifier) object;
                        iattributeinstance.applyModifier(attributemodifier);
                    }
                }
            }
        }
    }
}
