package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockDynamicLiquid.class)
public abstract class MixinBlockDynamicLiquid extends BlockLiquid {

    @Shadow
    int field_149815_a;
    @Shadow
    boolean[] field_149814_b = new boolean[4];
    @Shadow
    int[] field_149816_M = new int[4];

    protected MixinBlockDynamicLiquid(Material p_i45413_1_) {
        super(p_i45413_1_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
        int l = this.func_149804_e(worldIn, x, y, z);
        byte b0 = 1;

        if (this.blockMaterial == Material.lava && !worldIn.provider.isHellWorld) {
            b0 = 2;
        }

        boolean flag = true;
        int i1 = this.tickRate(worldIn);
        int j1;

        if (l > 0) {
            byte b1 = -100;
            this.field_149815_a = 0;
            int l1 = this.func_149810_a(worldIn, x - 1, y, z, b1);
            l1 = this.func_149810_a(worldIn, x + 1, y, z, l1);
            l1 = this.func_149810_a(worldIn, x, y, z - 1, l1);
            l1 = this.func_149810_a(worldIn, x, y, z + 1, l1);
            j1 = l1 + b0;

            if (j1 >= 8 || l1 < 0) {
                j1 = -1;
            }

            if (this.func_149804_e(worldIn, x, y + 1, z) >= 0) {
                int k1 = this.func_149804_e(worldIn, x, y + 1, z);

                if (k1 >= 8) {
                    j1 = k1;
                } else {
                    j1 = k1 + 8;
                }
            }

            if (this.field_149815_a >= 2 && this.blockMaterial == Material.water) {
                if (worldIn.getBlock(x, y - 1, z)
                    .getMaterial()
                    .isSolid()) {
                    j1 = 0;
                } else if (worldIn.getBlock(x, y - 1, z)
                    .getMaterial() == this.blockMaterial && worldIn.getBlockMetadata(x, y - 1, z) == 0) {
                        j1 = 0;
                    }
            }

            if (this.blockMaterial == Material.lava && l < 8 && j1 < 8 && j1 > l && random.nextInt(4) != 0) {
                i1 *= 4;
            }

            if (j1 == l) {
                if (flag) {
                    this.func_149811_n(worldIn, x, y, z);
                }
            } else {
                l = j1;

                if (j1 < 0) {
                    worldIn.setBlockToAir(x, y, z);
                } else {
                    worldIn.setBlockMetadataWithNotify(x, y, z, j1, 2);
                    worldIn.scheduleBlockUpdate(x, y, z, this, i1);
                    worldIn.notifyBlocksOfNeighborChange(x, y, z, this);
                }
            }
        } else {
            this.func_149811_n(worldIn, x, y, z);
        }

        if (this.func_149809_q(worldIn, x, y - 1, z)) {
            if (this.blockMaterial == Material.lava && worldIn.getBlock(x, y - 1, z)
                .getMaterial() == Material.water) {
                worldIn.setBlock(x, y - 1, z, Blocks.stone);
                this.func_149799_m(worldIn, x, y - 1, z);
                return;
            }

            if (l >= 8) {
                this.func_149813_h(worldIn, x, y - 1, z, l);
            } else {
                this.func_149813_h(worldIn, x, y - 1, z, l + 8);
            }
        } else if (l >= 0 && (l == 0 || this.func_149807_p(worldIn, x, y - 1, z))) {
            boolean[] aboolean = this.func_149808_o(worldIn, x, y, z);
            j1 = l + b0;

            if (l >= 8) {
                j1 = 1;
            }

            if (j1 >= 8) {
                return;
            }

            if (aboolean[0]) {
                this.func_149813_h(worldIn, x - 1, y, z, j1);
            }

            if (aboolean[1]) {
                this.func_149813_h(worldIn, x + 1, y, z, j1);
            }

            if (aboolean[2]) {
                this.func_149813_h(worldIn, x, y, z - 1, j1);
            }

            if (aboolean[3]) {
                this.func_149813_h(worldIn, x, y, z + 1, j1);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void func_149813_h(World p_149813_1_, int p_149813_2_, int p_149813_3_, int p_149813_4_, int p_149813_5_) {
        if (this.func_149809_q(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_)) {
            Block block = p_149813_1_.getBlock(p_149813_2_, p_149813_3_, p_149813_4_);

            if (this.blockMaterial == Material.lava) {
                this.func_149799_m(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_);
            } else {
                block.dropBlockAsItem(
                    p_149813_1_,
                    p_149813_2_,
                    p_149813_3_,
                    p_149813_4_,
                    p_149813_1_.getBlockMetadata(p_149813_2_, p_149813_3_, p_149813_4_),
                    0);
            }

            p_149813_1_.setBlock(p_149813_2_, p_149813_3_, p_149813_4_, this, p_149813_5_, 3);
        }
    }

    @Shadow
    private boolean func_149809_q(World p_149809_1_, int p_149809_2_, int p_149809_3_, int p_149809_4_) {
        Material material = p_149809_1_.getBlock(p_149809_2_, p_149809_3_, p_149809_4_)
            .getMaterial();
        return material == this.blockMaterial ? false
            : (material == Material.lava ? false
                : !this.func_149807_p(p_149809_1_, p_149809_2_, p_149809_3_, p_149809_4_));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean func_149807_p(World p_149807_1_, int p_149807_2_, int p_149807_3_, int p_149807_4_) {
        Block block = p_149807_1_.getBlock(p_149807_2_, p_149807_3_, p_149807_4_);
        return block == Blocks.wooden_door || block == Blocks.iron_door
            || block == Blocks.standing_sign
            || block == Blocks.ladder
            || block == Blocks.reeds
            || (block.getMaterial() == Material.portal || block.getMaterial()
                .blocksMovement());
    }

    @Shadow
    private void func_149811_n(World p_149811_1_, int p_149811_2_, int p_149811_3_, int p_149811_4_) {
        int l = p_149811_1_.getBlockMetadata(p_149811_2_, p_149811_3_, p_149811_4_);
        p_149811_1_
            .setBlock(p_149811_2_, p_149811_3_, p_149811_4_, Block.getBlockById(Block.getIdFromBlock(this) + 1), l, 2);
    }

    @Shadow
    private boolean[] func_149808_o(World p_149808_1_, int p_149808_2_, int p_149808_3_, int p_149808_4_) {
        int l;
        int i1;

        for (l = 0; l < 4; ++l) {
            this.field_149816_M[l] = 1000;
            i1 = p_149808_2_;
            int j1 = p_149808_4_;

            if (l == 0) {
                i1 = p_149808_2_ - 1;
            }

            if (l == 1) {
                ++i1;
            }

            if (l == 2) {
                j1 = p_149808_4_ - 1;
            }

            if (l == 3) {
                ++j1;
            }

            if (!this.func_149807_p(p_149808_1_, i1, p_149808_3_, j1) && (p_149808_1_.getBlock(i1, p_149808_3_, j1)
                .getMaterial() != this.blockMaterial || p_149808_1_.getBlockMetadata(i1, p_149808_3_, j1) != 0)) {
                if (this.func_149807_p(p_149808_1_, i1, p_149808_3_ - 1, j1)) {
                    this.field_149816_M[l] = this.func_149812_c(p_149808_1_, i1, p_149808_3_, j1, 1, l);
                } else {
                    this.field_149816_M[l] = 0;
                }
            }
        }

        l = this.field_149816_M[0];

        for (i1 = 1; i1 < 4; ++i1) {
            if (this.field_149816_M[i1] < l) {
                l = this.field_149816_M[i1];
            }
        }

        for (i1 = 0; i1 < 4; ++i1) {
            this.field_149814_b[i1] = this.field_149816_M[i1] == l;
        }

        return this.field_149814_b;
    }

    @Shadow
    protected int func_149810_a(World p_149810_1_, int p_149810_2_, int p_149810_3_, int p_149810_4_, int p_149810_5_) {
        int i1 = this.func_149804_e(p_149810_1_, p_149810_2_, p_149810_3_, p_149810_4_);

        if (i1 < 0) {
            return p_149810_5_;
        } else {
            if (i1 == 0) {
                ++this.field_149815_a;
            }

            if (i1 >= 8) {
                i1 = 0;
            }

            return p_149810_5_ >= 0 && i1 >= p_149810_5_ ? p_149810_5_ : i1;
        }
    }

    @Shadow
    private int func_149812_c(World p_149812_1_, int p_149812_2_, int p_149812_3_, int p_149812_4_, int p_149812_5_,
        int p_149812_6_) {
        int j1 = 1000;

        for (int k1 = 0; k1 < 4; ++k1) {
            if ((k1 != 0 || p_149812_6_ != 1) && (k1 != 1 || p_149812_6_ != 0)
                && (k1 != 2 || p_149812_6_ != 3)
                && (k1 != 3 || p_149812_6_ != 2)) {
                int l1 = p_149812_2_;
                int i2 = p_149812_4_;

                if (k1 == 0) {
                    l1 = p_149812_2_ - 1;
                }

                if (k1 == 1) {
                    ++l1;
                }

                if (k1 == 2) {
                    i2 = p_149812_4_ - 1;
                }

                if (k1 == 3) {
                    ++i2;
                }

                if (!this.func_149807_p(p_149812_1_, l1, p_149812_3_, i2) && (p_149812_1_.getBlock(l1, p_149812_3_, i2)
                    .getMaterial() != this.blockMaterial || p_149812_1_.getBlockMetadata(l1, p_149812_3_, i2) != 0)) {
                    if (!this.func_149807_p(p_149812_1_, l1, p_149812_3_ - 1, i2)) {
                        return p_149812_5_;
                    }

                    if (p_149812_5_ < 4) {
                        int j2 = this.func_149812_c(p_149812_1_, l1, p_149812_3_, i2, p_149812_5_ + 1, k1);

                        if (j2 < j1) {
                            j1 = j2;
                        }
                    }
                }
            }
        }

        return j1;
    }

    @Overwrite
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        super.onBlockAdded(worldIn, x, y, z);
        if (worldIn.getBlock(x, y, z) != this) {
            return;
        }
        worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
    }
}
