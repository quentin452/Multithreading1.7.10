package fr.iamacat.optimizationsandtweaks.mixins.common.goblins;

import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import goblin.MobSpawnerGoblinLogic;

@Mixin(MobSpawnerGoblinLogic.class)
public abstract class MixinMobSpawnerGoblinLogic {

    @Shadow
    public int spawnDelay = 20;
    @Shadow
    private String entityTypeName = "Pig";
    @Shadow
    private char goblinType = 'g';
    @Shadow
    private int goblinsLeft = 10;
    @Shadow
    private List potentialEntitySpawns;
    @Shadow
    private MobSpawnerGoblinLogic.WeightedRandomMinecart randomEntity;
    @Shadow
    public double field_98287_c;
    @Shadow
    public double field_98284_d;
    @Shadow
    private int minSpawnDelay = 400;
    @Shadow
    private int maxSpawnDelay = 800;
    @Shadow
    private int spawnCount = 2;
    @Shadow
    private Entity field_98291_j;
    @Shadow
    private int maxNearbyEntities = 6;
    @Shadow
    private int activatingRangeFromPlayer = 16;
    @Shadow
    private int spawnRange = 1;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void updateSpawner() {
        if (this.isActivated()) {
            if (this.goblinsLeft <= 0) {
                this.getSpawnerWorld()
                    .setBlock(this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), Blocks.cobblestone, 0, 2);
            }

            double d2;
            if (this.getSpawnerWorld().isRemote) {
                double d0 = ((float) this.getSpawnerX() + this.getSpawnerWorld().rand.nextFloat());
                double d1 = ((float) this.getSpawnerY() + this.getSpawnerWorld().rand.nextFloat());
                d2 = ((float) this.getSpawnerZ() + this.getSpawnerWorld().rand.nextFloat());
                this.getSpawnerWorld()
                    .spawnParticle("smoke", d0, d1, d2, 0.0, 0.0, 0.0);
                this.getSpawnerWorld()
                    .spawnParticle("flame", d0, d1, d2, 0.0, 0.0, 0.0);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }

                this.field_98284_d = this.field_98287_c;
                this.field_98287_c = (this.field_98287_c + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0;
            } else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;
                if (this.goblinsLeft > 0) {
                    for (int i = 0; i < this.spawnCount; ++i) {
                        Entity entity = EntityList
                            .createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                        if (entity == null) {
                            return;
                        }

                        AxisAlignedBB spawnAABB = AxisAlignedBB
                            .getBoundingBox(
                                this.getSpawnerX(),
                                this.getSpawnerY(),
                                this.getSpawnerZ(),
                                this.getSpawnerX() + 1,
                                this.getSpawnerY() + 1,
                                this.getSpawnerZ() + 1)
                            .expand(this.spawnRange * 2, 4.0, this.spawnRange * 2);
                        int j = this.getSpawnerWorld()
                            .getEntitiesWithinAABB(entity.getClass(), spawnAABB)
                            .size();

                        if (j >= this.maxNearbyEntities) {
                            this.resetTimer();
                            return;
                        }

                        d2 = (double) this.getSpawnerX()
                            + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble())
                                * (double) this.spawnRange;
                        double d3 = (this.getSpawnerY() + 2);
                        double d4 = (double) this.getSpawnerZ()
                            + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble())
                                * (double) this.spawnRange;
                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
                        entity.setLocationAndAngles(d2, d3, d4, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);
                        if (this.goblinType == 'r') {
                            Entity entity1 = EntityList.createEntityByName("goblin.Direwolf", this.getSpawnerWorld());
                            entity1.setLocationAndAngles(
                                d2,
                                d3,
                                d4,
                                this.getSpawnerWorld().rand.nextFloat() * 360.0F,
                                0.0F);
                            this.func_98265_a(entity1);
                        }

                        this.func_98265_a(entity);
                        --this.goblinsLeft;
                        this.getSpawnerWorld()
                            .playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), 0);
                        if (entityliving != null) {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.resetTimer();
                }
            }
        } else {
            this.goblinsLeft = 10;
        }
    }

    @Overwrite(remap = false)
    public Entity func_98265_a(Entity par1Entity) {
        if (this.getRandomEntity() != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            par1Entity.writeToNBTOptional(nbttagcompound);

            for (Object object : this.getRandomEntity().field_98222_b.func_150296_c()) {
                String s = (String) object;
                NBTBase nbtbase = this.getRandomEntity().field_98222_b.getTag(s);
                nbttagcompound.setTag(s, nbtbase.copy());
            }

            par1Entity.readFromNBT(nbttagcompound);
            if (par1Entity.worldObj != null) {
                par1Entity.worldObj.spawnEntityInWorld(par1Entity);
            }

            NBTTagCompound nbttagcompound2;
            for (Entity entity1 = par1Entity; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2) {
                nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
                Entity entity2 = EntityList.createEntityByName(nbttagcompound2.getString("id"), par1Entity.worldObj);
                if (entity2 != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    entity2.writeToNBTOptional(nbttagcompound1);

                    for (Object o : nbttagcompound2.func_150296_c()) {
                        String s1 = (String) o;
                        NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
                        nbttagcompound1.setTag(s1, nbtbase1.copy());
                    }

                    entity2.readFromNBT(nbttagcompound1);
                    assert entity1 != null;
                    entity2.setLocationAndAngles(
                        entity1.posX,
                        entity1.posY,
                        entity1.posZ,
                        entity1.rotationYaw,
                        entity1.rotationPitch);
                    if (par1Entity.worldObj != null) {
                        par1Entity.worldObj.spawnEntityInWorld(entity2);
                    }

                    entity1.mountEntity(entity2);
                }

                entity1 = entity2;
            }
        } else if (par1Entity instanceof EntityLivingBase && par1Entity.worldObj != null) {
            ((EntityLiving) par1Entity).onSpawnWithEgg(null);
            this.getSpawnerWorld()
                .spawnEntityInWorld(par1Entity);
        }

        return par1Entity;
    }

    @Shadow
    public boolean isActivated() {
        return this.getSpawnerWorld()
            .getClosestPlayer(
                this.getSpawnerX() + 0.5,
                this.getSpawnerY() + 0.5,
                this.getSpawnerZ() + 0.5,
                this.activatingRangeFromPlayer)
            != null;
    }

    @Shadow
    public abstract void func_98267_a(int var1);

    @Shadow
    public abstract World getSpawnerWorld();

    @Shadow
    public abstract int getSpawnerX();

    @Shadow
    public abstract int getSpawnerY();

    @Shadow
    public abstract int getSpawnerZ();

    @Shadow
    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }

        if (this.potentialEntitySpawns != null && !this.potentialEntitySpawns.isEmpty()) {
            this.setRandomEntity(
                (MobSpawnerGoblinLogic.WeightedRandomMinecart) WeightedRandom
                    .getRandomItem(this.getSpawnerWorld().rand, this.potentialEntitySpawns));
        }

        this.func_98267_a(1);
    }

    @Shadow
    public void setRandomEntity(MobSpawnerGoblinLogic.WeightedRandomMinecart par1WeightedRandomMinecart) {
        this.randomEntity = par1WeightedRandomMinecart;
    }

    @Shadow
    public String getEntityNameToSpawn() {
        if (this.goblinType == 'g') {
            int goblinPick = this.getSpawnerWorld().rand.nextInt(20);
            if (goblinPick <= 8) {
                return "goblin.Goblin";
            } else if (goblinPick <= 13) {
                return "goblin.GOBLINEntityGoblinRanger";
            } else {
                return goblinPick <= 18 ? "goblin.GoblinSoldier" : "goblin.GoblinBomber";
            }
        } else if (this.goblinType == 'm') {
            return "goblin.GoblinMiner";
        } else {
            return this.goblinType == 'r' ? "goblin.GoblinRider" : "goblin.Goblin";
        }
    }

    @Shadow
    public MobSpawnerGoblinLogic.WeightedRandomMinecart getRandomEntity() {
        return this.randomEntity;
    }

}
