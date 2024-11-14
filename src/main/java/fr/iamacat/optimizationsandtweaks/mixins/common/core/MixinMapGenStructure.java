package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MapGenStructure.class)
public abstract class MixinMapGenStructure extends MapGenBase {

    @Shadow
    private MapGenStructureData field_143029_e;
    /**
     * Used to store a list of all structures that have been recursively generated. Used so that during recursive
     * generation, the structure generator can avoid generating structures that intersect ones that have already been
     * placed.
     */
    @Shadow
    protected Map structureMap = new HashMap();

    @Shadow
    public abstract String func_143025_a();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean generateStructuresInChunk(World p_75051_1_, Random p_75051_2_, int p_75051_3_, int p_75051_4_) {
        this.func_143027_a(p_75051_1_);
        int k = (p_75051_3_ << 4) + 8;
        int l = (p_75051_4_ << 4) + 8;
        boolean flag = false;

        for (Object o : this.structureMap.values()) {
            StructureStart structurestart = (StructureStart) o;
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox()
                .intersectsWith(k, l, k + 15, l + 15)) {
                structurestart
                    .generateStructure(p_75051_1_, p_75051_2_, new StructureBoundingBox(k, l, k + 15, l + 15));
                flag = true;
                this.func_143026_a(structurestart.func_143019_e(), structurestart.func_143018_f(), structurestart);
            }
        }

        return flag;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void func_143027_a(World p_143027_1_) {
        if (this.field_143029_e == null) {
            this.field_143029_e = (MapGenStructureData) p_143027_1_.perWorldStorage
                .loadData(MapGenStructureData.class, this.func_143025_a());

            if (this.field_143029_e == null) {
                this.field_143029_e = new MapGenStructureData(this.func_143025_a());
                p_143027_1_.perWorldStorage.setData(this.func_143025_a(), this.field_143029_e);
            } else {
                NBTTagCompound nbttagcompound = this.field_143029_e.func_143041_a();

                for (Object o : nbttagcompound.func_150296_c()) {
                    String s = (String) o;
                    NBTBase nbtbase = nbttagcompound.getTag(s);

                    if (nbtbase.getId() == 10) {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;

                        if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
                            int i = nbttagcompound1.getInteger("ChunkX");
                            int j = nbttagcompound1.getInteger("ChunkZ");
                            StructureStart structurestart = MapGenStructureIO
                                .func_143035_a(nbttagcompound1, p_143027_1_);

                            if (structurestart != null) {
                                this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
                            }
                        }
                    }
                }
            }
        }
    }

    @Shadow
    private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart p_143026_3_) {
        this.field_143029_e
            .func_143043_a(p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
        this.field_143029_e.markDirty();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected StructureStart func_143028_c(int p_143028_1_, int p_143028_2_, int p_143028_3_) {
        for (Object object : this.structureMap.values()) {
            StructureStart structurestart = (StructureStart) object;
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox()
                .intersectsWith(p_143028_1_, p_143028_3_, p_143028_1_, p_143028_3_)) {
                for (Object o : structurestart.getComponents()) {
                    StructureComponent structurecomponent = (StructureComponent) o;
                    if (structurecomponent.getBoundingBox()
                        .isVecInside(p_143028_1_, p_143028_2_, p_143028_3_)) {
                        return structurestart;
                    }
                }
            }
        }
        return null;
    }
}
