package fr.iamacat.optimizationsandtweaks.mixins.client.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.renderer.texture.Stitcher;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Stitcher.Slot.class)
public class MixinStitcher_Slot {

    @Shadow
    private final int originX;
    @Shadow
    private final int originY;
    @Shadow
    private final int width;
    @Shadow
    private final int height;
    @Shadow
    private List subSlots;
    @Shadow
    private Stitcher.Holder holder;

    public MixinStitcher_Slot(int p_i1277_1_, int p_i1277_2_, int p_i1277_3_, int p_i1277_4_) {
        this.originX = p_i1277_1_;
        this.originY = p_i1277_2_;
        this.width = p_i1277_3_;
        this.height = p_i1277_4_;
    }

    @Shadow
    public Stitcher.Holder getStitchHolder() {
        return this.holder;
    }

    @Shadow
    public int getOriginX() {
        return this.originX;
    }

    @Shadow
    public int getOriginY() {
        return this.originY;
    }

    // todo need to optimize
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean addSlot(Stitcher.Holder p_94182_1_) {
        if (this.holder != null) {
            return false;
        } else {
            int i = p_94182_1_.getWidth();
            int j = p_94182_1_.getHeight();

            if (i <= this.width && j <= this.height) {
                if (i == this.width && j == this.height) {
                    this.holder = p_94182_1_;
                } else {
                    if (this.subSlots == null) {
                        this.subSlots = new ArrayList(1);
                        this.subSlots.add(new Stitcher.Slot(this.originX, this.originY, i, j));
                        int k = this.width - i;
                        int l = this.height - j;

                        if (l > 0 && k > 0) {
                            int i1 = Math.max(this.height, k);
                            int j1 = Math.max(this.width, l);

                            if (i1 >= j1) {
                                this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, i, l));
                                this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, this.height));
                            } else {
                                this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, j));
                                this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, this.width, l));
                            }
                        } else if (k == 0) {
                            this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, i, l));
                        } else if (l == 0) {
                            this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, j));
                        }
                    }

                    Iterator iterator = this.subSlots.iterator();
                    Stitcher.Slot slot;

                    do {
                        if (!iterator.hasNext()) {
                            return false;
                        }

                        slot = (Stitcher.Slot) iterator.next();
                    } while (!slot.addSlot(p_94182_1_));

                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Gets the slot and all its subslots
     */
    @Overwrite
    public void getAllStitchSlots(List p_94184_1_) {
        if (this.holder != null) {
            p_94184_1_.add(this);
        } else if (this.subSlots != null) {

            for (Object subSlot : this.subSlots) {
                Stitcher.Slot slot = (Stitcher.Slot) subSlot;
                slot.getAllStitchSlots(p_94184_1_);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public String toString() {
        return "Slot{originX=" + this.originX
            + ", originY="
            + this.originY
            + ", width="
            + this.width
            + ", height="
            + this.height
            + ", texture="
            + this.holder
            + ", subSlots="
            + this.subSlots
            + '}';
    }
}
