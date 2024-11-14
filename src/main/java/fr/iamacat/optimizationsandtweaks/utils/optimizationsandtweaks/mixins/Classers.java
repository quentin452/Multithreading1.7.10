package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.mixins;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.ModContainer;
import fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.collections.maps.ArrayListThreadSafe;
import thaumcraft.api.internal.DummyInternalMethodHandler;

public class Classers {

    // MixinMinecraft

    public static final class SwitchMovingObjectType {

        public static final int[] field_152390_a = new int[MovingObjectPosition.MovingObjectType.values().length];

        static {
            try {
                field_152390_a[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_152390_a[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }

    // MixinBiomeCache

    public static class Block {

        public static WorldChunkManager chunkManager;
        /** An array of chunk rainfall values saved by this cache. */
        public float[] rainfallValues = new float[256];
        /** The array of biome types stored in this BiomeCacheBlock. */
        public BiomeGenBase[] biomes = new BiomeGenBase[256];
        /** The x coordinate of the BiomeCacheBlock. */
        public int xPosition;
        /** The z coordinate of the BiomeCacheBlock. */
        public int zPosition;
        /** The last time this BiomeCacheBlock was accessed, in milliseconds. */
        public long lastAccessTime;

        public Block(int p_i1972_2_, int p_i1972_3_, WorldChunkManager chunkManager) {
            Block.chunkManager = chunkManager;
            this.xPosition = p_i1972_2_;
            this.zPosition = p_i1972_3_;
            chunkManager.getRainfall(this.rainfallValues, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16);
            chunkManager.getBiomeGenAt(this.biomes, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16, false);
        }

        /**
         * Returns the BiomeGenBase related to the x, z position from the cache block.
         */
        public BiomeGenBase getBiomeGenAt(int p_76885_1_, int p_76885_2_) {
            return this.biomes[p_76885_1_ & 15 | (p_76885_2_ & 15) << 4];
        }
    }

    // MixinItemInfo

    public static class ItemStackKey2 {

        public final ItemStack stack;

        public ItemStackKey2(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int hashCode() {
            if (this.stack == null) return 1;
            int hashCode = 1;
            hashCode = 31 * hashCode + stack.stackSize;
            hashCode = 31 * hashCode + Item.getIdFromItem(stack.getItem());
            hashCode = 31 * hashCode + stack.getItemDamage();
            hashCode = 31 * hashCode + (!stack.hasTagCompound() ? 0
                : stack.getTagCompound()
                    .hashCode());
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof ItemStackKey2)) return false;
            return ItemStack.areItemStacksEqual(this.stack, ((ItemStackKey2) o).stack);
        }
    }

    // MixinLoader

    public static class ModIdComparator implements Comparator<ModContainer> {

        @Override
        public int compare(ModContainer o1, ModContainer o2) {
            return o1.getModId()
                .compareTo(o2.getModId());
        }
    }

    // MixinWorldGenPyramid

    public static class Pair {

        public int x;
        public int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object p) {
            if (!(p instanceof Pair)) {
                return false;
            } else {
                return ((Pair) p).x == this.x && ((Pair) p).y == this.y;
            }
        }
    }

    // MixinInfusionVisualDisguiseArmor

    public static class FakeMethodHandler extends DummyInternalMethodHandler {

        public FakeMethodHandler() {}

        public boolean isResearchComplete(String username, String researchkey) {
            return true;
        }
    }

    // MixinGuiResearchRecipe

    public static class Coord2D {

        public int x;
        public int y;

        public Coord2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // MixinWorldGenTreeBase

    public enum Quadrant {

        X_Z,
        X_NEGZ,
        NEGX_Z,
        NEGX_NEGZ;

        Quadrant() {}

        public Quadrant next() {
            switch (this) {
                case NEGX_Z:
                    return X_Z;
                case NEGX_NEGZ:
                    return NEGX_Z;
                case X_Z:
                    return X_NEGZ;
                case X_NEGZ:
                default:
                    return NEGX_NEGZ;
            }
        }

        public Quadrant previous() {
            switch (this) {
                case NEGX_Z:
                    return NEGX_NEGZ;
                case NEGX_NEGZ:
                    return X_NEGZ;
                case X_Z:
                    return NEGX_Z;
                case X_NEGZ:
                default:
                    return X_Z;
            }
        }
    }

    public static class XZCoord {

        public int x;
        public int z;

        public XZCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public XZCoord() {
            this(0, 0);
        }

        public XZCoord offset(int dir, int amount) {
            int xOff = 0;
            int zOff = 0;
            switch (dir) {
                case 2:
                    zOff = -amount;
                    break;
                case 3:
                default:
                    zOff = amount;
                    break;
                case 4:
                    xOff = -amount;
                    break;
                case 5:
                    xOff = amount;
            }

            return new XZCoord(this.x + xOff, this.z + zOff);
        }

        public XZCoord offset(int dir) {
            return this.offset(dir, 1);
        }
    }

    // MixinBlockFluidClassic

    public static class FlowCostContext {

        public final int x;
        public final int y;
        public final int z;
        public final int recurseDepth;
        public final int adjSide;

        public FlowCostContext(int x, int y, int z, int recurseDepth, int adjSide) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.recurseDepth = recurseDepth;
            this.adjSide = adjSide;
        }
    }

    // MixinLongHashMap

    public static class Entry {

        /** the key as a long (for playerInstances it is the x in the most significant 32 bits and then y) */
        public final long key;
        /** the value held by the hash at the specified key */
        public Object value;
        /** the next hashentry in the table */
        public Entry nextEntry;
        public final int hash;

        public Entry(int p_i1553_1_, long p_i1553_2_, Object p_i1553_4_, Entry p_i1553_5_) {
            this.value = p_i1553_4_;
            this.nextEntry = p_i1553_5_;
            this.key = p_i1553_2_;
            this.hash = p_i1553_1_;
        }

        public final long getKey() {
            return this.key;
        }

        public final Object getValue() {
            return this.value;
        }

        public final boolean equals(Object p_equals_1_) {
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }

            Entry entry = (Entry) p_equals_1_;
            return this.getKey() == entry.getKey() && Objects.equals(this.getValue(), entry.getValue());
        }

        public final int hashCode() {
            return getHashedKey(this.key);
        }

        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }

        public static int getHashedKey(long p_76155_0_) {
            return (int) p_76155_0_ + (int) (p_76155_0_ >>> 32) * 92821;
        }
    }

    // MixinIntHashMap
    public static class EntryIntHashMap {

        /** The hash code of this entry */

        public final int hashEntry;
        /** The object stored in this entry */
        public Object valueEntry;
        /** The next entry in this slot */
        public EntryIntHashMap nextEntry;
        /** The id of the hash slot computed from the hash */
        public final int slotHash;

        public EntryIntHashMap(int p_i1552_1_, int p_i1552_2_, Object p_i1552_3_, EntryIntHashMap p_i1552_4_) {
            this.valueEntry = p_i1552_3_;
            this.nextEntry = p_i1552_4_;
            this.hashEntry = p_i1552_2_;
            this.slotHash = p_i1552_1_;
        }

        /**
         * Returns the hash code for this entry
         */
        public final int getHash() {
            return this.hashEntry;
        }

        /**
         * Returns the object stored in this entry
         */
        public final Object getValue() {
            return this.valueEntry;
        }

        public final boolean equals(Object p_equals_1_) {
            if (p_equals_1_ instanceof EntryIntHashMap) {
                EntryIntHashMap entry = (EntryIntHashMap) p_equals_1_;
                Integer integer = this.getHash();
                Integer integer1 = entry.getHash();

                if (Objects.equals(integer, integer1)) {
                    Object object1 = this.getValue();
                    Object object2 = entry.getValue();

                    return Objects.equals(object1, object2);
                }

            }
            return false;
        }

        public final int hashCode() {
            return computeHash(this.hashEntry);
        }

        public final String toString() {
            return this.getHash() + "=" + this.getValue();
        }

        /**
         * Makes the passed in integer suitable for hashing by a number of shifts
         */
        public static int computeHash(int p_76044_0_) {
            p_76044_0_ ^= p_76044_0_ >>> 20 ^ p_76044_0_ >>> 12;
            return p_76044_0_ ^ p_76044_0_ >>> 7 ^ p_76044_0_ >>> 4;
        }
    }

    // MixinPlayerManager

    public static class PlayerInstance {

        public final List playersWatchingChunk = new ArrayListThreadSafe();
        /** note: this is final */
        public final ChunkCoordIntPair chunkLocation;
        private short[] locationOfBlockChange = new short[64];
        private int numberOfTilesToUpdate;
        /** Integer field where each bit means to make update 16x16x16 division of chunk (from bottom). */
        private int flagsYAreasToUpdate;
        /** time what is using when chunk InhabitedTime is being calculated */
        private long previousWorldTime;
        private final ConcurrentHashMap<EntityPlayerMP, Runnable> players = new ConcurrentHashMap<>();
        private boolean loaded = false;
        private Runnable loadedRunnable = () -> PlayerInstance.this.loaded = true;
        private WorldServer worldServer;
        private PlayerManager playerManager = new PlayerManager(worldServer);
        private static final Logger field_152627_a = LogManager.getLogger();

        public PlayerInstance(int p_i1518_2_, int p_i1518_3_, WorldServer worldServer) {
            this.worldServer = worldServer;
            this.chunkLocation = new ChunkCoordIntPair(p_i1518_2_, p_i1518_3_);
            worldServer.theChunkProviderServer.loadChunk(p_i1518_2_, p_i1518_3_, this.loadedRunnable);
        }

        public void addPlayer(final EntityPlayerMP p_73255_1_) {
            if (this.playersWatchingChunk.contains(p_73255_1_)) {
                field_152627_a.debug(
                    "Failed to add player. {} already is in chunk {}, {}",
                    new Object[] { p_73255_1_, Integer.valueOf(this.chunkLocation.chunkXPos),
                        Integer.valueOf(this.chunkLocation.chunkZPos) });
            } else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = playerManager.getWorldServer()
                        .getTotalWorldTime();
                }

                this.playersWatchingChunk.add(p_73255_1_);
                Runnable playerRunnable;

                if (this.loaded) {
                    playerRunnable = null;
                    p_73255_1_.loadedChunks.add(this.chunkLocation);
                } else {
                    playerRunnable = new Runnable() {

                        public void run() {
                            p_73255_1_.loadedChunks.add(PlayerInstance.this.chunkLocation);
                        }
                    };
                    playerManager.getWorldServer().theChunkProviderServer
                        .loadChunk(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos, playerRunnable);
                }

                this.players.put(p_73255_1_, playerRunnable);
            }
        }

        public void removePlayer(EntityPlayerMP p_73252_1_) {
            if (this.playersWatchingChunk.contains(p_73252_1_)) {
                // If we haven't loaded yet don't load the chunk just so we can clean it up
                if (!this.loaded) {
                    net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(
                        playerManager.getWorldServer(),
                        this.chunkLocation.chunkXPos,
                        this.chunkLocation.chunkZPos,
                        this.players.get(p_73252_1_));
                    this.playersWatchingChunk.remove(p_73252_1_);
                    this.players.remove(p_73252_1_);

                    if (this.playersWatchingChunk.isEmpty()) {
                        net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(
                            playerManager.getWorldServer(),
                            this.chunkLocation.chunkXPos,
                            this.chunkLocation.chunkZPos,
                            this.loadedRunnable);
                        long i = (long) this.chunkLocation.chunkXPos + 2147483647L
                            | (long) this.chunkLocation.chunkZPos + 2147483647L << 32;
                        playerManager.playerInstances.remove(i);
                        playerManager.playerInstanceList.remove(this);
                    }

                    return;
                }

                Chunk chunk = playerManager.getWorldServer()
                    .getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);

                if (chunk.func_150802_k()) {
                    p_73252_1_.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }

                this.players.remove(p_73252_1_);
                this.playersWatchingChunk.remove(p_73252_1_);
                p_73252_1_.loadedChunks.remove(this.chunkLocation);

                net.minecraftforge.common.MinecraftForge.EVENT_BUS
                    .post(new net.minecraftforge.event.world.ChunkWatchEvent.UnWatch(chunkLocation, p_73252_1_));

                if (this.playersWatchingChunk.isEmpty()) {
                    long i = (long) this.chunkLocation.chunkXPos + 2147483647L
                        | (long) this.chunkLocation.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(chunk);
                    playerManager.playerInstances.remove(i);
                    playerManager.playerInstanceList.remove(this);

                    if (this.numberOfTilesToUpdate > 0) {
                        playerManager.chunkWatcherWithPlayers.remove(this);
                    }

                    playerManager.getWorldServer().theChunkProviderServer
                        .unloadChunksIfNotNearSpawn(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);
                }
            }
        }

        /**
         * This method currently only increases chunk inhabited time. Extension is possible in next versions
         */
        public void processChunk() {
            this.increaseInhabitedTime(
                playerManager.getWorldServer()
                    .getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos));
        }

        /**
         * Increases chunk inhabited time every 8000 ticks
         */
        private void increaseInhabitedTime(Chunk p_111196_1_) {
            p_111196_1_.inhabitedTime += playerManager.getWorldServer()
                .getTotalWorldTime() - this.previousWorldTime;
            this.previousWorldTime = playerManager.getWorldServer()
                .getTotalWorldTime();
        }

        public void flagChunkForUpdate(int p_151253_1_, int p_151253_2_, int p_151253_3_) {
            if (this.numberOfTilesToUpdate == 0) {
                playerManager.chunkWatcherWithPlayers.add(this);
            }

            this.flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);

            // if (this.numberOfTilesToUpdate < 64) //Forge; Cache everything, so always run
            {
                short short1 = (short) (p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);

                for (int l = 0; l < this.numberOfTilesToUpdate; ++l) {
                    if (this.locationOfBlockChange[l] == short1) {
                        return;
                    }
                }

                if (numberOfTilesToUpdate == locationOfBlockChange.length) {
                    locationOfBlockChange = java.util.Arrays
                        .copyOf(locationOfBlockChange, locationOfBlockChange.length << 1);
                }
                this.locationOfBlockChange[this.numberOfTilesToUpdate++] = short1;
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet p_151251_1_) {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playersWatchingChunk.get(i);

                if (!entityplayermp.loadedChunks.contains(this.chunkLocation)) {
                    entityplayermp.playerNetServerHandler.sendPacket(p_151251_1_);
                }
            }
        }

        @SuppressWarnings("unused")
        public void sendChunkUpdate() {
            if (this.numberOfTilesToUpdate != 0) {
                int i;
                int j;
                int k;

                if (this.numberOfTilesToUpdate == 1) {
                    i = this.chunkLocation.chunkXPos * 16 + (this.locationOfBlockChange[0] >> 12 & 15);
                    j = this.locationOfBlockChange[0] & 255;
                    k = this.chunkLocation.chunkZPos * 16 + (this.locationOfBlockChange[0] >> 8 & 15);
                    this.sendToAllPlayersWatchingChunk(
                        new S23PacketBlockChange(i, j, k, playerManager.getWorldServer()));

                    if (playerManager.getWorldServer()
                        .getBlock(i, j, k)
                        .hasTileEntity(
                            playerManager.getWorldServer()
                                .getBlockMetadata(i, j, k))) {
                        this.sendTileToAllPlayersWatchingChunk(
                            playerManager.getWorldServer()
                                .getTileEntity(i, j, k));
                    }
                } else {
                    int l;

                    if (this.numberOfTilesToUpdate >= net.minecraftforge.common.ForgeModContainer.clumpingThreshold) {
                        i = this.chunkLocation.chunkXPos * 16;
                        j = this.chunkLocation.chunkZPos * 16;
                        this.sendToAllPlayersWatchingChunk(
                            new S21PacketChunkData(
                                playerManager.getWorldServer()
                                    .getChunkFromChunkCoords(
                                        this.chunkLocation.chunkXPos,
                                        this.chunkLocation.chunkZPos),
                                false,
                                this.flagsYAreasToUpdate));

                        // Forge: Grabs ALL tile entities is costly on a modded server, only send needed ones
                        for (k = 0; false && k < 16; ++k) {
                            if ((this.flagsYAreasToUpdate & 1 << k) != 0) {
                                l = k << 4;
                                List list = playerManager.getWorldServer()
                                    .func_147486_a(i, l, j, i + 16, l + 16, j + 16);

                                for (int i1 = 0; i1 < list.size(); ++i1) {
                                    this.sendTileToAllPlayersWatchingChunk((TileEntity) list.get(i1));
                                }
                            }
                        }
                    } else {
                        this.sendToAllPlayersWatchingChunk(
                            new S22PacketMultiBlockChange(
                                this.numberOfTilesToUpdate,
                                this.locationOfBlockChange,
                                playerManager.getWorldServer()
                                    .getChunkFromChunkCoords(
                                        this.chunkLocation.chunkXPos,
                                        this.chunkLocation.chunkZPos)));
                    }

                    { // Forge: Send only the tile entities that are updated, Adding this brace lets us keep the indent
                      // and the patch small
                        WorldServer world = playerManager.getWorldServer();
                        for (i = 0; i < this.numberOfTilesToUpdate; ++i) {
                            j = this.chunkLocation.chunkXPos * 16 + (this.locationOfBlockChange[i] >> 12 & 15);
                            k = this.locationOfBlockChange[i] & 255;
                            l = this.chunkLocation.chunkZPos * 16 + (this.locationOfBlockChange[i] >> 8 & 15);

                            if (world.getBlock(j, k, l)
                                .hasTileEntity(world.getBlockMetadata(j, k, l))) {
                                this.sendTileToAllPlayersWatchingChunk(
                                    playerManager.getWorldServer()
                                        .getTileEntity(j, k, l));
                            }
                        }
                    }
                }

                this.numberOfTilesToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity p_151252_1_) {
            if (p_151252_1_ != null) {
                Packet packet = p_151252_1_.getDescriptionPacket();

                if (packet != null) {
                    this.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }
    }
}
