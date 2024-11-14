package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.common.FMLLog;

@Mixin(AnvilChunkLoader.class)
public class MixinAnvilChunkLoader {

    /**
     * @author
     * @reason
     */
    @Overwrite
    private synchronized void writeChunkToNBT(Chunk p_75820_1_, World p_75820_2_, NBTTagCompound p_75820_3_) {
        p_75820_3_.setByte("V", (byte) 1);
        p_75820_3_.setInteger("xPos", p_75820_1_.xPosition);
        p_75820_3_.setInteger("zPos", p_75820_1_.zPosition);
        p_75820_3_.setLong("LastUpdate", p_75820_2_.getTotalWorldTime());
        p_75820_3_.setIntArray("HeightMap", p_75820_1_.heightMap);
        p_75820_3_.setBoolean("TerrainPopulated", p_75820_1_.isTerrainPopulated);
        p_75820_3_.setBoolean("LightPopulated", p_75820_1_.isLightPopulated);
        p_75820_3_.setLong("InhabitedTime", p_75820_1_.inhabitedTime);
        ExtendedBlockStorage[] aextendedblockstorage = p_75820_1_.getBlockStorageArray();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = !p_75820_2_.provider.hasNoSky;
        ExtendedBlockStorage[] aextendedblockstorage1 = aextendedblockstorage;
        int i = aextendedblockstorage.length;
        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j) {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage1[j];

            if (extendedblockstorage != null) {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte) (extendedblockstorage.getYLocation() >> 4 & 255));
                nbttagcompound1.setByteArray("Blocks", extendedblockstorage.getBlockLSBArray());

                if (extendedblockstorage.getBlockMSBArray() != null) {
                    nbttagcompound1.setByteArray("Add", extendedblockstorage.getBlockMSBArray().data);
                }

                nbttagcompound1.setByteArray("Data", extendedblockstorage.getMetadataArray().data);
                nbttagcompound1.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().data);

                if (flag) {
                    nbttagcompound1.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().data);
                } else {
                    nbttagcompound1
                        .setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().data.length]);
                }

                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_75820_3_.setTag("Sections", nbttaglist);
        p_75820_3_.setByteArray("Biomes", p_75820_1_.getBiomeArray());
        p_75820_1_.hasEntities = false;
        NBTTagList nbttaglist2 = new NBTTagList();
        Iterator iterator1;

        for (i = 0; i < p_75820_1_.entityLists.length; ++i) {
            iterator1 = p_75820_1_.entityLists[i].iterator();

            while (iterator1.hasNext()) {
                Entity entity = (Entity) iterator1.next();
                nbttagcompound1 = new NBTTagCompound();

                try {
                    if (entity.writeToNBTOptional(nbttagcompound1)) {
                        p_75820_1_.hasEntities = true;
                        nbttaglist2.appendTag(nbttagcompound1);
                    }
                } catch (Exception e) {
                    FMLLog.log(
                        Level.ERROR,
                        e,
                        "An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                        entity.getClass()
                            .getName());
                }
            }
        }

        p_75820_3_.setTag("Entities", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        iterator1 = p_75820_1_.chunkTileEntityMap.values()
            .iterator();

        while (iterator1.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator1.next();
            nbttagcompound1 = new NBTTagCompound();
            try {
                tileentity.writeToNBT(nbttagcompound1);
                nbttaglist3.appendTag(nbttagcompound1);
            } catch (Exception e) {
                FMLLog.log(
                    Level.ERROR,
                    e,
                    "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                    tileentity.getClass()
                        .getName());
            }
        }

        p_75820_3_.setTag("TileEntities", nbttaglist3);
        List list = p_75820_2_.getPendingBlockUpdates(p_75820_1_, false);

        if (list != null) {
            long k = p_75820_2_.getTotalWorldTime();
            NBTTagList nbttaglist1 = new NBTTagList();

            for (Object o : list) {
                NextTickListEntry nextticklistentry = (NextTickListEntry) o;
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setInteger("i", Block.getIdFromBlock(nextticklistentry.func_151351_a()));
                nbttagcompound2.setInteger("x", nextticklistentry.xCoord);
                nbttagcompound2.setInteger("y", nextticklistentry.yCoord);
                nbttagcompound2.setInteger("z", nextticklistentry.zCoord);
                nbttagcompound2.setInteger("t", (int) (nextticklistentry.scheduledTime - k));
                nbttagcompound2.setInteger("p", nextticklistentry.priority);
                nbttaglist1.appendTag(nbttagcompound2);
            }

            p_75820_3_.setTag("TileTicks", nbttaglist1);
        }
    }

}
